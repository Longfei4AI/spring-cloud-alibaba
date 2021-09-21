package com.altomni.apn.authority.config;

import com.alibaba.fastjson.JSON;
import com.altomni.apn.authority.exception.UserNotActivatedException;
import com.altomni.apn.authority.repository.UserRepository;
import com.altomni.apn.authority.domain.User;
import com.altomni.apn.common.dto.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component("userDetailsService")
@Slf4j
public class DomainUserDetailsService implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.info("Authenticating {}", username);
        //log.warn(new BCryptPasswordEncoder().encode("B5:hR'N^5)JKN?Ej")); $2a$10$3AdVxOJHqBLEb1UceCiwterM22E.EUjOUu117JHxbMp3QI8PuwK2m
        //log.warn(new BCryptPasswordEncoder().encode("123456")); $2a$10$pgDeeVJie8UVGXJ9itCoguuW0UT27Mr8ZvlTSG1LpaRvGq4treMnG

        if (new EmailValidator().isValid(username, null)) {
            return userRepository
                    .findOneWithAuthoritiesByEmailIgnoreCase(username)
                    .map(user -> createSpringSecurityUser(username, user))
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " was not found in the database"));
        }

        String lowercaseLogin = username.toLowerCase(Locale.ENGLISH);
        return userRepository
                .findOneWithAuthoritiesByUsername(lowercaseLogin)
                .map(user -> createSpringSecurityUser(lowercaseLogin, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user
                .getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        BeanUtils.copyProperties(user, loginUserDTO);
        return new org.springframework.security.core.userdetails.User(JSON.toJSONString(loginUserDTO), user.getPassword(), grantedAuthorities);
    }
}
