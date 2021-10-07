package com.altomni.apn.job.web.rest;

import com.altomni.apn.job.service.CompanyService;
import com.altomni.apn.job.service.JobService;
import com.altomni.apn.job.service.dto.JobDTO;
import com.altomni.apn.job.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author longfeiwang
 */
@RestController
@Slf4j
@RequestMapping("/api/v3")
public class JobResourceTest {

    @Resource
    private CompanyService companyService;

    @Resource
    private JobService jobService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public ResponseEntity<Object> getJobs(){
        log.info("request to get jobs ----");
        log.info(SecurityUtils.getCurrentUserLogin().toString());
        return companyService.getCompany();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save-success")
    public ResponseEntity<Object> saveJobSuccess(){
        log.info("request to save job success----");
        log.info(SecurityUtils.getCurrentUserLogin().toString());
        return jobService.saveTestSuccess(new JobDTO(null, "test title success", 10, 1));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save-rollback")
    public ResponseEntity<Object> saveJobRollback(){
        log.info("request to save job rollback ----");
        log.info(SecurityUtils.getCurrentUserLogin().toString());
        return jobService.saveTestRollback(new JobDTO(null, "test title rollback", 10, 1));
    }

    @GetMapping("/test-admin")
    public ResponseEntity<String> getAdmin(){
        log.info("request to get admin ----");
        log.info(SecurityUtils.getCurrentUserLogin().toString());
        return ResponseEntity.ok("Success");
    }
}
