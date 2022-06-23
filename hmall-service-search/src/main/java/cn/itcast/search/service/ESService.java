package cn.itcast.search.service;

import cn.itcast.hmall.dto.common.ResultDTO;
import cn.itcast.hmall.dto.search.SearchReqDTO;

import java.util.List;
import java.util.Map;

public interface ESService {

    /**
     * ES数据导入
     */
    ResultDTO import2ES();

    /**
     * 搜索栏自动补全功能
     *
     * @param key 用户输入的此条前缀
     * @return 自动补全的词条集合
     */
    List<String> getSuggestions(String key);

    /**
     * 获取搜索过滤项
     *
     * @param searchReqDTO
     * @return
     */
    Map<String, List<String>> getFilterSearchResults(SearchReqDTO searchReqDTO);
}
