package com.altomni.apn.talent.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        /*String token = httpServletRequest.getHeader("json-token");

        if (StringUtils.isNotEmpty(token)){
            JSONObject jsonObject = JSON.parseObject(token);
            String principal = jsonObject.getString("principal");
            List<String> authorities = jsonObject.getJSONArray("authorities").toJavaList(String.class);
            LoginUserDTO loginUserDTO = new LoginUserDTO(); // TODO
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUserDTO, null, AuthorityUtils.createAuthorityList(authorities.toArray(new String[authorities.size()])));
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }*/
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
