package com.altomni.apn.company.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(value = "candidate-service", fallback = CandidateServiceFallback.class) // service name
public interface CandidateService {
    @GetMapping("/candidate/api/v3/test")
    ResponseEntity<Object> getCandidate();
}
