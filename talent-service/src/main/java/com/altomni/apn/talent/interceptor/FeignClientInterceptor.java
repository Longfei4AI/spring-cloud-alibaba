package com.altomni.apn.talent.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * pass token between microservices
 */
@Component
public class FeignClientInterceptor implements RequestInterceptor {

  private static final String AUTHORIZATION_HEADER="Authorization";
  private static final String TOKEN_TYPE = "Bearer";

  @Override
  public void apply(RequestTemplate requestTemplate) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
      OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
      if (StringUtils.isNotEmpty(details.getTokenValue())) {
        requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, details.getTokenValue()));
      }
    }
  }
}