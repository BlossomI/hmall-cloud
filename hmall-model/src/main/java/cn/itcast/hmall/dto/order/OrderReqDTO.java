package cn.itcast.hmall.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    添加订单 请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReqDTO {
    Integer num;
    Integer paymentType;
    Long addressId;
    Long itemId;
    String password;
}
