package com.altomni.apn.authority.config;

import com.alibaba.fastjson.JSON;
import com.altomni.apn.authority.domain.UserSecurityInterface;
import com.altomni.apn.authority.domain.enumeration.AccountType;
import com.altomni.apn.authority.exception.UserNotActivatedException;
import com.altomni.apn.authority.repository.UserAdminRepository;
import com.altomni.apn.authority.repository.UserRepository;
import com.altomni.apn.common.dto.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@Slf4j
public class DomainUserDetailsService implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserAdminRepository userAdminRepository;

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        log.info("Authenticating {}", login);
        String[] split = login.split(",");
        long userId = Long.parseLong(split[0]);
        String accountType = split[1];
        UserSecurityInterface user = accountType.equalsIgnoreCase(AccountType.ADMIN.toString()) ?
                userAdminRepository.findOneWithAuthoritiesById(userId).orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found in the database")) :
                userRepository.findOneWithAuthoritiesById(userId).orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found in the database"));
        return createSpringSecurityUser(login, user);
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, UserSecurityInterface user) {
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
