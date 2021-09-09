package com.apn.controller;

import com.apn.entity.Candidate;
import com.apn.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@Slf4j
public class JobResource {

    /*
    @Resource
    private RestTemplate restTemplate;*/
    @Resource
    private CompanyService companyService;

    @GetMapping("/job/test")
    public ResponseEntity<Candidate> getJobs(){
        log.info("request to get jobs ----");
        /*return restTemplate.getForEntity(this.serviceUrl + "/company/test", Company.class);*/
        return companyService.getCompany();
    }
}
