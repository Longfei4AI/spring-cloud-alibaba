package com.altomni.apn.job.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
//@FeignClient(value = "company-service", fallback = CompanyServiceFallback.class)
@FeignClient(value = "company-service")
public interface CompanyService {
    @GetMapping("/company/api/v3/test")
    ResponseEntity<Object> getCompany();

    @PostMapping("/company/api/v3/save-success")
    ResponseEntity<Object> saveCompany();

    @PostMapping("/company/api/v3/save-rollback")
    ResponseEntity<Object> saveCompanyRollback();
}
