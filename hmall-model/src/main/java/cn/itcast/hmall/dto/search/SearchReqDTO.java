package cn.itcast.hmall.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * 商品es搜索请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchReqDTO {
    // 搜索关键字
    private String key;
    // 第几页
    private Integer page;
    // 每页条数
    private Integer size;
    // 排序字段
    private String sortBy;
    // 分类参数
    private String category;
    // 品牌参数
    private String brand;
    // 最小价格
    private Double minPrice;
    // 最大价格
    private Double maxPrice;
}
