package com.altomni.apn.company.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;

@Component
public class CandidateServiceFallback implements CandidateService {
    @Override
    public ResponseEntity<Object> getCandidate() {
        return ResponseEntity.badRequest().body(new HashMap<String, String>(){{put("error", "fall back from company");}});
    }
}
