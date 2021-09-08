package com.apn.service;

import com.apn.entity.Candidate;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
    public Candidate getCandidate(){
        return new Candidate(1L, "Tom", 30);
    }
}
