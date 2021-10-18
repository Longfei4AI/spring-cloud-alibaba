package com.altomni.apn.company.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author longfeiwang
 */
@Component
@FeignClient(value = "talent-service") // service name
public interface TalentService {
    @GetMapping("/talent/api/v3/test")
    ResponseEntity<Object> getCandidate();

    @PostMapping("/talent/api/v3/save")
    ResponseEntity<Object> saveCandidate();
}
