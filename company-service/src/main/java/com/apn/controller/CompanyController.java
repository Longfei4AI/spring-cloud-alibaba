package com.apn.controller;

import com.apn.entity.Candidate;
import com.apn.service.CandidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class CompanyController {

    @Resource
    private CandidateService candidateService;

    @GetMapping("/company/test")
    public ResponseEntity<Candidate> test(){

        log.info("request to get Company");
        return candidateService.getCandidate();
    }
}