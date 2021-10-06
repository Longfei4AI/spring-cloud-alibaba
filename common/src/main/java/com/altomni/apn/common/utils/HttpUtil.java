package com.altomni.apn.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author longfeiwang
 */
@Slf4j
public class HttpUtil {

    private static RestTemplate restTemplate = new RestTemplate();

    /*************** Get Requests ***************/

    /*
    e.g.
    HttpUtil.getWithHeaders("http://localhost:9001/candidate/api/v3/test", HttpUtils.buildHttpHeaders(new HashMap<>(){{
            put("Authorization", token);
        }}), Object.class);
    */

    public static <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables){
        log.info(restTemplate.toString());
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> getWithHeaders(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables){
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> getWithRequestJsonBody(String url, Object requestJsonBody, Class<T> responseType, Object... uriVariables){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(requestJsonBody, headers), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> getWithHeadersAndRequestJsonBody(String url, HttpHeaders headers, Object requestJsonBody, Class<T> responseType, Object... uriVariables){
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(requestJsonBody, headers), responseType, uriVariables);
    }

    /*************** Post Requests **************/

    public static <T> ResponseEntity<T> postWithHeaders(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables){
        return restTemplate.postForEntity(url, new HttpEntity<>(headers), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> postWithRequestForm(String url, MultiValueMap<String, Object> requestForm, Class<T> responseType, Object... uriVariables){
        return restTemplate.postForEntity(url, requestForm, responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> postWithRequestJsonBody(String url, Object requestJsonBody, Class<T> responseType, Object... uriVariables){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForEntity(url, new HttpEntity<>(requestJsonBody, headers), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> postWithHeadersAndRequestForm(String url, HttpHeaders headers, MultiValueMap<String, Object> requestForm, Class<T> responseType, Object... uriVariables){
        return restTemplate.postForEntity(url, new HttpEntity<>(requestForm, headers), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> postWithHeadersAndRequestJsonBody(String url, HttpHeaders headers, Object requestJsonBody, Class<T> responseType, Object... uriVariables){
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForEntity(url, new HttpEntity<>(requestJsonBody, headers), responseType, uriVariables);
    }

    /*************** Put Requests **************/

    public static <T> ResponseEntity<T> putWithHeaders(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables){
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(headers), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> putWithRequestForm(String url, MultiValueMap<String, Object> requestForm, Class<T> responseType, Object... uriVariables){
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(requestForm), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> putWithRequestJsonBody(String url, Object requestJsonBody, Class<T> responseType, Object... uriVariables){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(requestJsonBody, headers), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> putWithHeadersAndRequestForm(String url, HttpHeaders headers, MultiValueMap<String, Object> requestForm, Class<T> responseType, Object... uriVariables){
        return restTemplate.exchange(url,HttpMethod.PUT, new HttpEntity<>(requestForm, headers), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> putWithHeadersAndRequestJsonBody(String url, HttpHeaders headers, Object requestJsonBody, Class<T> responseType, Object... uriVariables){
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(requestJsonBody, headers), responseType, uriVariables);
    }

    /*************** Delete Requests **************/

    public static <T> ResponseEntity<T> delete(String url, Class<T> responseType, Object... uriVariables){
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity(null), responseType, uriVariables);
    }

    public static <T> ResponseEntity<T> deleteWithHeaders(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables){
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), responseType, uriVariables);
    }

    /**
     * create headers:
     * HttpHeaders headers = new HttpHeaders();
     * headers.setContentType(MediaType.APPLICATION_JSON);
     * headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
     * headers.add("pageSize", "20");
     *
     * create formMap:
     * MultiValueMap<String, Object> formMap= new LinkedMultiValueMap<>();
     * map.add("email", "first.last@example.com");
     */

    public static HttpHeaders buildHttpHeaders(Map<String, String> headerMap){
        HttpHeaders headers = new HttpHeaders();
        headerMap.forEach((k, v) -> headers.add(k, v));
        return headers;
    }

    public static HttpHeaders buildHttpHeadersMultipleValues(Map<String, List<String>> headerMap){
        HttpHeaders headers = new HttpHeaders();
        headerMap.forEach((k, v) -> headers.put(k, v));
        return headers;
    }
}
