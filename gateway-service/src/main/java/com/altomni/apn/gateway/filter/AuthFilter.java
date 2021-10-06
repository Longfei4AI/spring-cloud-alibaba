package com.altomni.apn.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.geo.LineString;
import com.alibaba.nacos.common.utils.MapUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;*/
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.result.view.RequestContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

//@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof OAuth2Authentication)){
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;

        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();

        //Object principal = userAuthentication.getPrincipal();
        String principal = userAuthentication.getName();

        List<String> authorities = userAuthentication.getAuthorities().stream().map(a-> a.getAuthority()).collect(Collectors.toList());

        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();

        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();

        exchange.getRequest().getQueryParams().setAll(requestParameters);

        Map<String, Object> jsonToken = new HashMap<>(){{
            put("principal", principal);
            put("authorities", authorities);
        }};

        exchange.getResponse().getHeaders().add("json-token", JSON.toJSONString(jsonToken));*/

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
