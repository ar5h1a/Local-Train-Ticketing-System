package com.project.gateway.filter;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

    private static class RequestData {
        int count;
        long timestamp;
    }

    private final ConcurrentHashMap<String, RequestData> map = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String ip = exchange.getRequest().getRemoteAddress()
                .getAddress().getHostAddress();

        long now = System.currentTimeMillis();

        map.putIfAbsent(ip, new RequestData());

        RequestData data = map.get(ip);

        if (now - data.timestamp > 60000) {
            data.count = 0;
            data.timestamp = now;
        }

        data.count++;

        if (data.count > 20) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
