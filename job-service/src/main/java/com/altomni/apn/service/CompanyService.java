package com.apn.service;

import com.apn.entity.Candidate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "company-service", fallback = CompanyServiceFallback.class) // service name
public interface CompanyService {
    @GetMapping("/company/test")
    ResponseEntity<Candidate> getCompany();
}
