package com.altomni.apn.company.web.rest;

import com.altomni.apn.company.service.CandidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/api/v3")
public class CompanyResourceTest {

    @Resource
    private CandidateService candidateService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public ResponseEntity<Object> test(){

        log.info("request to get Company");
        return candidateService.getCandidate();
    }
}
