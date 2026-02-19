package com.project.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long start = System.currentTimeMillis();

        System.out.println("Request → "
                + exchange.getRequest().getMethod()
                + " "
                + exchange.getRequest().getURI());

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    long time = System.currentTimeMillis() - start;
                    System.out.println("Response ← "
                            + exchange.getResponse().getStatusCode()
                            + " (" + time + "ms)");
                })
        );
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
