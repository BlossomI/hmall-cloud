package cn.itcast.feign.client;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 商品服务 Feign接口
 *
 * TODO 需要远程调用的方法自己补充哦
 *
 */
@FeignClient("itemservice")
public interface ItemClient {


}
