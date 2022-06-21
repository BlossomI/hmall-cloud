//package cn.itcast.gateway.filter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//@Order(-1)
//@Component
//public class GateWayHandler implements GlobalFilter {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//        String authorization = headers.getFirst("authorization");
//        // 3.校验
//        if(authorization!=null&&authorization.equals("2")){
//            // 放行
//            return chain.filter(exchange);
//        }
//        // 4.拦截
//        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//        // 4.1.禁止访问，设置状态码
//        return exchange.getResponse().setComplete();
//    }
//}
//
