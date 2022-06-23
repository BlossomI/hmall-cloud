package cn.itcast.feign.client;
import cn.itcast.hmall.dto.common.PageDTO;
import cn.itcast.hmall.pojo.item.Item;
import cn.itcast.hmall.pojo.req.PageReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 商品服务 Feign接口
 *
 * TODO 需要远程调用的方法自己补充哦
 *
 */
@FeignClient("itemservice")
public interface ItemClient {

    @PostMapping("/item/list")
    PageDTO<Item> pageQuery(@RequestBody PageReq request);


}
