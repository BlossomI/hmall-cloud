package cn.itcast.hmall.pojo.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemDoc {
    private Long id;
    // 名称
    private String name;
    // 价格
    private Long price;
    // 封面
    private String image;
    // 分类
    private String category;
    // 品牌
    private String brand;
    // 销量
    private Integer sold;
    // 评论数量
    private Integer commentCount;
    // 广告字段
    private Boolean isAD;
    // 搜索建议字段  用于搜索框提示用户
    private List<String> suggestion = new ArrayList<>(2);

    public ItemDoc(Item item) {
        // 属性拷贝
        BeanUtils.copyProperties(item, this);
        // 补全
        suggestion.add(item.getBrand());
        suggestion.add(item.getCategory());
    }
}