package cn.itcast.search.service.impl;

import cn.itcast.feign.client.ItemClient;
import cn.itcast.hmall.dto.common.PageDTO;
import cn.itcast.hmall.dto.common.ResultDTO;
import cn.itcast.hmall.dto.search.SearchReqDTO;
import cn.itcast.hmall.pojo.item.Item;
import cn.itcast.hmall.pojo.item.ItemDoc;
import cn.itcast.hmall.pojo.req.PageReq;
import cn.itcast.search.service.ESService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ESServiceImpl implements ESService {

    @Resource
    private ItemClient itemClient;

    @Resource
    private RestHighLevelClient client;

    @Override
    public ResultDTO import2ES() {

        PageReq pageReq = new PageReq();
        pageReq.setPage(1);
        pageReq.setSize(-1);

        PageDTO<Item> pageDTO = itemClient.pageQuery(pageReq);

        List<Item> itemList = pageDTO.getList();

        Lists.partition(itemList, 1000).forEach(list -> {

            BulkRequest bulkRequest = new BulkRequest();

            try {

                for (Item item : list) {
                    // 将文档类型转换成ItemDoc
                    ItemDoc itemDoc = new ItemDoc(item);

                    // 创建request对象
                    bulkRequest.add(new IndexRequest("item")
                            .id(itemDoc.getId().toString())
                            .source(JSON.toJSONString(itemDoc), XContentType.JSON));
                }

                // 发送请求
                client.bulk(bulkRequest, RequestOptions.DEFAULT);

                log.info("success");

            } catch (IOException e) {

                throw new RuntimeException();

            }
        });
        return ResultDTO.ok();
    }

    /**
     * 搜索栏自动补全功能
     *
     * @param key 用户输入的此条前缀
     * @return 自动补全的词条集合
     */
    @Override
    public List<String> getSuggestions(String key) {
        SearchRequest request = new SearchRequest("item");

        request.source()
                .suggest(new SuggestBuilder().addSuggestion(
                        "itemSuggestion",
                        SuggestBuilders.
                                completionSuggestion("suggestion")
                                .prefix(key)
                                .skipDuplicates(true)
                                .size(10)
                ));
        try {

            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            // 解析结果
            Suggest suggest = response.getSuggest();
            // 获取补全结果
            CompletionSuggestion itemsSuggest = suggest.getSuggestion("itemSuggestion");

            List<CompletionSuggestion.Entry.Option> options = itemsSuggest.getOptions();

            // 遍历获取结果
            List<String> suggestionsList = new ArrayList<>(options.size());
            for (CompletionSuggestion.Entry.Option option : options) {

                String suggestion = option.getText().string();
                suggestionsList.add(suggestion);

            }

            return suggestionsList;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * 获取搜索过滤项
     *
     * @param params 用户输入参数
     * @return 过滤项map集合
     */
    @Override
    public Map<String, List<String>> getFilterSearchResults(SearchReqDTO params) {
        try {

            // 1.准备Request
            SearchRequest request = new SearchRequest("item");
            // 2.准备DSL
            // 2.1.query
            buildBasicQuery(params, request);
            // 2.2.设置size
            request.source().size(0);
            // 2.3.聚合
            buildAggregation(request);

            // send request
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);


            Map<String, List<String>> result = new HashMap<>();

            Aggregations aggregations = response.getAggregations();

            // aggregate by brand
            List<String> brandList = getAggByName(aggregations, "brandAgg");
            result.put("brand", brandList);

            // aggregate by category
            List<String> cityList = getAggByName(aggregations, "categoryAgg");
            result.put("category", cityList);

            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageDTO<ItemDoc> basicQuery(SearchReqDTO searchReqDTO) {
        try {

            // extract page and pageSize
            Integer page = searchReqDTO.getPage();
            Integer size = searchReqDTO.getSize();

            SearchRequest request = new SearchRequest("item");

            // set basic parameters and sort
            buildBasicQuery(searchReqDTO, request);

            // set page search
            request.source().from((page - 1) * size).size(size);

            // send request
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            log.info("response status: {}", response.status());

            List<ItemDoc> itemDocs = handleResponse(response);

            PageDTO<ItemDoc> pageDTO = new PageDTO<>();

            pageDTO.setList(itemDocs);

            pageDTO.setTotal(response.getHits().getTotalHits().value);

            return pageDTO;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RabbitMQ message receiver
     * insert Doc into elasticSearch
     *
     * @param id item id
     */
    @Override
    public void insertById(Long id) {
        try {
            // invoke feign API to query mysql
            Item item = itemClient.queryById(id);

            // convert to Doc
            ItemDoc itemDoc = new ItemDoc(item);

            // prepare Request
            IndexRequest request = new IndexRequest("item").id(item.getId().toString());

            //  prepare Json doc
            request.source(JSON.toJSONString(itemDoc), XContentType.JSON);

            // send request
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * RabbitMQ message receiver
     *
     * @param id item id
     */
    @Override
    public void deleteById(Long id) {
        try {
            // 1.准备Request
            DeleteRequest request = new DeleteRequest("item", id.toString());
            // 2.发送请求
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * build basic query params
     *
     * @param params  request params
     * @param request SearchRequest object
     */
    private void buildBasicQuery(SearchReqDTO params, SearchRequest request) {
        // create bool query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // keyword
        if (StringUtils.isNotBlank(params.getKey())) {
            boolQuery.must(QueryBuilders.matchQuery("all", params.getKey()));
        } else {
            boolQuery.must(QueryBuilders.matchAllQuery());
        }

        // brand
        if (StringUtils.isNotBlank(params.getBrand())) {
            boolQuery.filter(QueryBuilders.termQuery("brand", params.getBrand()));
        }

        // category
        if (StringUtils.isNotBlank(params.getCategory())) {
            boolQuery.filter(QueryBuilders.termQuery("category", params.getCategory()));
        }

        // price range
        if (params.getMinPrice() != null && params.getMaxPrice() != null) {
            boolQuery.filter(QueryBuilders
                    .rangeQuery("price")
                    .lte(params.getMaxPrice())
                    .gte(params.getMinPrice()));
        }

        request.source().query(boolQuery);

        // sort
        if (StringUtils.isNotBlank(params.getSortBy())) {
            if (params.getSortBy().equals("price")) {
                request.source().sort(params.getSortBy(), SortOrder.ASC);
            } else if (params.getSortBy().equals("sold")) {
                request.source().sort(params.getSortBy(), SortOrder.DESC);
            }
        }

    }

    /**
     * get aggregation result
     *
     * @param aggregations 总聚合结果集和
     * @param aggName      聚合项名称
     * @return 聚合结果列表
     */
    private List<String> getAggByName(@NotNull Aggregations aggregations, String aggName) {
        // 4.1.根据聚合名称获取聚合结果
        Terms brandTerms = aggregations.get(aggName);
        // 4.2.获取buckets
        List<? extends Terms.Bucket> buckets = brandTerms.getBuckets();
        // 4.3.遍历
        List<String> brandList = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            // 4.4.获取key
            String key = bucket.getKeyAsString();
            brandList.add(key);
        }
        return brandList;
    }

    /**
     * build Aggregation param
     *
     * @param request SearchRequest object
     */
    private void buildAggregation(SearchRequest request) {
        request.source().aggregation(AggregationBuilders
                .terms("brandAgg")
                .field("brand")
                .size(20));
        request.source().aggregation(AggregationBuilders
                .terms("categoryAgg")
                .field("category")
                .size(20));
    }

    private List<ItemDoc> handleResponse(SearchResponse response) {
        // 4.解析响应
        SearchHits searchHits = response.getHits();
        // 4.1.获取总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到" + total + "条数据");
        // 4.2.文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        List<ItemDoc> itemDocsList = new ArrayList<>();
        for (SearchHit hit : hits) {
            // 获取文档source
            String json = hit.getSourceAsString();
            // 反序列化
            ItemDoc itemDoc = JSON.parseObject(json, ItemDoc.class);
            itemDocsList.add(itemDoc);

        }

        return itemDocsList;
    }


}
