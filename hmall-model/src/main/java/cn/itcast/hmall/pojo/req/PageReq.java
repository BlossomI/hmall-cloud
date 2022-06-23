package cn.itcast.hmall.pojo.req;

import lombok.Data;

import java.util.Date;

@Data
public class PageReq {
    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 分类
     */
    private String category;
}
