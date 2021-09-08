package com.apn.service;

import com.apn.entity.Candidate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CandidateServiceFallback implements CandidateService {
    @Override
    public ResponseEntity<Candidate> getCandidate() {
        return ResponseEntity.badRequest().body(new Candidate(null, "fall back from company", null));
    }
}
