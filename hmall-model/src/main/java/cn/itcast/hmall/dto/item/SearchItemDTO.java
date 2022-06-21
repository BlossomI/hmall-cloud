package cn.itcast.hmall.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商品后台搜索 请求参数封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemDTO {
    private Integer page;
    private Integer size;
    private String name;
    private Date beginDate;
    private Date endDate;
    private String brand;
    private String category;
}
