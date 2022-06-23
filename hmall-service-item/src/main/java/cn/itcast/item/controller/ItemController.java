package cn.itcast.item.controller;

import cn.itcast.hmall.dto.common.PageDTO;
import cn.itcast.hmall.dto.common.ResultDTO;
import cn.itcast.hmall.pojo.item.Item;
import cn.itcast.hmall.pojo.req.PageReq;
import cn.itcast.item.service.ItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Resource
    private ItemService itemService;

    @PostMapping("/list")
    public PageDTO<Item> pageQuery(@RequestBody PageReq request) {
        return itemService.pageQuery(request);
    }

    @GetMapping("/{id}")
    public Item queryById(@PathVariable("id") Integer id) {
        return itemService.getById(id);
    }

    @PostMapping
    public void saveItem(@RequestBody Item saveReq) {
        itemService.save(saveReq);
    }

    @PutMapping
    public ResultDTO updateItem(@RequestBody Item update) {
        boolean isSuccess = itemService.updateById(update);

        return isSuccess ? ResultDTO.ok(isSuccess) : ResultDTO.error();
    }

    @PutMapping("/status/{id}/{status}")
    public ResultDTO updateStatus(@PathVariable("id") Integer id,
                                  @PathVariable("status") Integer status) {

        return itemService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResultDTO deleteItem(@PathVariable("id") Integer id) {

        boolean isSuccess = itemService.removeById(id);

        return isSuccess ? ResultDTO.ok() : ResultDTO.error();
    }
}
