package com.altomni.apn.authority.service;

import com.altomni.apn.authority.domain.UserSecurityInterface;
import com.altomni.apn.authority.web.rest.vm.LoginVM;
import com.altomni.apn.common.dto.CredentialDTO;
import com.altomni.apn.common.dto.LoginUserDTO;
import com.altomni.apn.common.errors.CustomParameterizedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author longfeiwang
 */
@Slf4j
public abstract class UserSecurityService {

    @Value("${oauth2.secret}")
    private String secret;

    @Value("${spring.application.name}")
    private String service;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Resource
    private RestTemplate restTemplate;

    /**
     * User login. Call the oauth2 login api and return user profile
     * @param loginVM username and password
     * @return jsonObject access token & refresh token
     */
    public LoginUserDTO login(LoginVM loginVM) {
        return this.getUserWithAuthoritiesByLogin(loginVM.getUsername()).map(user -> {
            if (!user.isActivated()) {
                throw new CustomParameterizedException("Inactive user is not allowed to login");
            }
            try {
                String tokenUrl = String.format("http://%s%s/oauth/token", service, contextPath);
                log.info(tokenUrl);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.add("Authorization", String.format("Basic %s", secret));

                MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
                map.add("username", user.getId().toString() + "," + user.getAccountType());
                map.add("password", loginVM.getPassword());
                map.add("grant_type", "password");

                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

                ResponseEntity<CredentialDTO> response = restTemplate.postForEntity(tokenUrl, request, CredentialDTO.class);

                CredentialDTO credentialDTO = response.getBody();

                LoginUserDTO loginUserDTO = new LoginUserDTO();

                BeanUtils.copyProperties(user, loginUserDTO);

                loginUserDTO.setCredential(credentialDTO);

                return loginUserDTO;
            } catch (Exception e) {
                log.error("[UserService.login] Login user {}, exception {}", loginVM, ExceptionUtils.getStackTrace(e));
                throw new CustomParameterizedException("The username and password do not match.");
            }
        }).orElseThrow(() -> new CustomParameterizedException("The username and password do not match."));
    }

    public abstract Optional<UserSecurityInterface> getUserWithAuthoritiesByLogin(String login);
}
