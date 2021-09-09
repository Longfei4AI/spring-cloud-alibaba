package com.altomni.apn.service;

import com.altomni.apn.entity.Candidate;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
    public Candidate getCandidate(){
        return new Candidate(1L, "Tom", 30);
    }
}
