package cn.itcast.item.controller;

import cn.itcast.hmall.constants.MqConstants;
import cn.itcast.hmall.dto.common.PageDTO;
import cn.itcast.hmall.dto.common.ResultDTO;
import cn.itcast.hmall.pojo.item.Item;
import cn.itcast.hmall.pojo.req.PageReq;
import cn.itcast.item.service.ItemService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.InvalidParameterException;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Resource
    private ItemService itemService;

    @Resource
    private RabbitTemplate rabbit;

    /**
     * 分页查询
     *
     * @param request 用户请求数据
     * @return 通用返回对象
     */
    @PostMapping("/list")
    public PageDTO<Item> pageQuery(@RequestBody PageReq request) {
        return itemService.pageQuery(request);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Item queryById(@PathVariable("id") Long id) {
        return itemService.getById(id);
    }

    /**
     * 保存商品
     *
     * @param saveReq
     */
    @PostMapping
    public void saveItem(@RequestBody Item saveReq) {
        itemService.save(saveReq);
//        rabbit.convertAndSend(MqConstants.ITEM_EXCHANGE, MqConstants.ITEM_INSERT_KEY, saveReq.getId());
    }

    /**
     * 更新商品
     *
     * @param update
     * @return
     */
    @PutMapping
    public ResultDTO updateItem(@RequestBody Item update) {
        if (update.getId() == null) {
            throw new InvalidParameterException("Id can't be null~");
        }
        boolean isSuccess = itemService.updateById(update);

        rabbit.convertAndSend(MqConstants.ITEM_EXCHANGE, MqConstants.ITEM_INSERT_KEY, update.getId());

        return isSuccess ? ResultDTO.ok(isSuccess) : ResultDTO.error();
    }

    /**
     * 上下架商品
     *
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/status/{id}/{status}")
    public ResultDTO updateStatus(@PathVariable("id") Integer id,
                                  @PathVariable("status") Integer status) {

        return itemService.updateStatus(id, status);
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultDTO deleteItem(@PathVariable("id") Long id) {

        boolean isSuccess = itemService.removeById(id);

//        rabbit.convertAndSend(MqConstants.ITEM_EXCHANGE, MqConstants.ITEM_DELETE_KEY, id);

        return isSuccess ? ResultDTO.ok() : ResultDTO.error();
    }

}
