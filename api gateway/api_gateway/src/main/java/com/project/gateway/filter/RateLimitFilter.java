package com.project.gateway.filter;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

<<<<<<< Updated upstream
import reactor.core.publisher.Mono;

@Component
=======
import java.time.Duration;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter implements GlobalFilter {

    private final Bucket bucket;

    public RateLimitFilter(){
        Bandwidth limit = Bandwidth.simple(2, Duration.ofSeconds(1));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){

        if(bucket.tryConsume(1))
            return chain.filter(exchange);

        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return exchange.getResponse().setComplete();
    }
}
/*

@Component
>>>>>>> Stashed changes
public class RateLimitFilter implements GlobalFilter, Ordered {

    private static class RequestData {
        int count;
        long timestamp;
<<<<<<< Updated upstream
    }

    private final ConcurrentHashMap<String, RequestData> map = new ConcurrentHashMap<>();
=======

        RequestData() {
            this.count = 0;
            this.timestamp = System.currentTimeMillis();
        }
    }

    private final ConcurrentHashMap<String, RequestData> map = new ConcurrentHashMap<>();
    private static final int LIMIT = 20;
    private static final long WINDOW = 60000;
>>>>>>> Stashed changes

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

<<<<<<< Updated upstream
        String ip = exchange.getRequest().getRemoteAddress()
                .getAddress().getHostAddress();
=======
        String ip = "unknown";

        if (exchange.getRequest().getRemoteAddress() != null &&
            exchange.getRequest().getRemoteAddress().getAddress() != null) {

            ip = exchange.getRequest()
                    .getRemoteAddress()
                    .getAddress()
                    .getHostAddress();
        }
>>>>>>> Stashed changes

        long now = System.currentTimeMillis();

        map.putIfAbsent(ip, new RequestData());
<<<<<<< Updated upstream

        RequestData data = map.get(ip);

        if (now - data.timestamp > 60000) {
            data.count = 0;
            data.timestamp = now;
        }

        data.count++;

        if (data.count > 20) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
=======
        RequestData data = map.get(ip);

        synchronized (data) {

            if (now - data.timestamp > WINDOW) {
                data.count = 0;
                data.timestamp = now;
            }

            data.count++;

            if (data.count > LIMIT) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }
>>>>>>> Stashed changes
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
<<<<<<< Updated upstream
        return 0;
    }
}
=======
        return -1; // run before most filters
    }
}
*/
>>>>>>> Stashed changes
