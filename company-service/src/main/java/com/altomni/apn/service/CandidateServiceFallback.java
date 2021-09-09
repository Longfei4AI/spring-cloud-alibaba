package com.altomni.apn.service;

import com.altomni.apn.entity.Candidate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CandidateServiceFallback implements CandidateService {
    @Override
    public ResponseEntity<Candidate> getCandidate() {
        return ResponseEntity.badRequest().body(new Candidate(null, "fall back from company", null));
    }
}
