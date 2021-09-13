package com.altomni.apn.service;

import com.altomni.apn.domain.Candidate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "candidate-service", fallback = CandidateServiceFallback.class) // service name
public interface CandidateService {
    @GetMapping("/candidate/test")
    ResponseEntity<Candidate> getCandidate();
}
