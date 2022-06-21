package cn.itcast.feign.client;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 用户服务 feign接口
 *
 * TODO 需要的接口自己补充哦
 */
@FeignClient("userservice")
public interface UserClient {
}
