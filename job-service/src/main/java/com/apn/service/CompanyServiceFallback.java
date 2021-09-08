package com.apn.service;

import com.apn.entity.Candidate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyServiceFallback implements CompanyService{
    @Override
    public ResponseEntity<Candidate> getCompany() {
        return ResponseEntity.badRequest().body(new Candidate(null, "company fallback", null));
    }
}
