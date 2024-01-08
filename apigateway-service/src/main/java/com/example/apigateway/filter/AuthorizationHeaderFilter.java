package com.example.apigateway.filter;

import com.example.apigateway.dto.JwtDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment env;
    public static class Config{

    }
    public AuthorizationHeaderFilter(Environment env){
        super(Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Global Pre Filter. Suppose we can extract JWT and perform Authentication
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authorizationHeader.substring(7);

            JwtDto jwtDto = getUserInfo(exchange, token);

            if(jwtDto == null){
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            exchange.getAttributes().put("userId", jwtDto.getUserId());
            exchange.getAttributes().put("role", jwtDto.getRole());
            System.out.println(exchange.getAttributes().get("userId"));
            System.out.println(exchange.getAttributes().get("role"));

            return chain.filter(exchange);
        };
    }
    private JwtDto getUserInfo(ServerWebExchange exchange, String token){

        Jws<Claims> claims;
        try{
            claims = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(token);
        } catch(Exception e){
            return null;
        }
        Long userId = claims.getBody().get("userId", Long.class);
        String role = claims.getBody().get("role", String.class);
        if(userId == null || role == null){
            return null;
        }
        return new JwtDto(userId, role);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        try{
            String requestURI = String.valueOf(exchange.getRequest().getPath());
            System.out.println("requestURI : " + requestURI);
            Map<String, String> map = new HashMap<>();
            //redirectURI는 로그인 후 다시 원래 페이지로 돌아가기 위함이다.
            map.put("requestURI", "/accounts/signin?redirectURI="+requestURI);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            System.out.println(json);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(json.getBytes())));
        } catch(Exception ex){
            log.error(ex.getMessage());
            return response.setComplete();
        }
    }
}
