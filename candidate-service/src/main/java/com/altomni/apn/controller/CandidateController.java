package com.altomni.apn.controller;

import com.altomni.apn.entity.Candidate;
import com.altomni.apn.service.CandidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class CandidateController {

    @Resource
    private CandidateService candidateService;

    @GetMapping("/candidate/test")
    public ResponseEntity<Candidate> test(){
        log.info("request to get Candidate");
        return ResponseEntity.ok(candidateService.getCandidate());
    }
}
