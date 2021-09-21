package com.altomni.apn.job.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;

@Component
public class CompanyServiceFallback implements CompanyService{
    @Override
    public ResponseEntity<Object> getCompany() {
        return ResponseEntity.badRequest().body(new HashMap<>(){{put("error", "company fallback");}});
    }
}
