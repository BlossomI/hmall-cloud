package cn.itcast.search.mq;

import cn.itcast.hmall.constants.MqConstants;
import cn.itcast.search.service.ESService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ItemListener {

    @Resource
    private ESService esService;

    /**
     * 监听item新增或修改的业务
     *
     * @param id id
     */
    @RabbitListener(queues = MqConstants.ITEM_INSERT_QUEUE)
    public void listenHotelInsertOrUpdate(Long id) {
        esService.insertById(id);
    }

    /**
     * 监听Item删除的业务
     *
     * @param id id
     */
    @RabbitListener(queues = MqConstants.ITEM_DELETE_QUEUE)
    public void listenHotelDelete(Long id) {
        esService.deleteById(id);
    }

}
