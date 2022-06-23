package cn.itcast.hmall.constants;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class MqConstants {

    /**
     * 交换机
     */
    public final static String ITEM_EXCHANGE = "item.topic";

    /**
     * 监听新增和修改的队列
     */
    public final static String ITEM_INSERT_QUEUE = "item.insert.queue";

    /**
     * 监听删除的队列
     */
    public final static String ITEM_DELETE_QUEUE = "item.delete.queue";

    /**
     * 新增或修改的RoutingKey
     */
    public final static String ITEM_INSERT_KEY = "item.insert";

    /**
     * 删除的RoutingKey
     */
    public final static String ITEM_DELETE_KEY = "item.delete";
}