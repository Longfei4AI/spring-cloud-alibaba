package com.altomni.apn.company.web.rest;

import com.altomni.apn.company.service.TalentService;
import com.altomni.apn.company.service.CompanyService;
import com.altomni.apn.company.service.dto.CompanyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/api/v3")
public class CompanyResourceTest {

    @Resource
    private TalentService candidateService;

    @Resource
    private CompanyService companyService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public ResponseEntity<Object> test(@RequestHeader("Authorization") String token){
        log.info("request to get Company");
        return candidateService.getCandidate();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save-success")
    public ResponseEntity<Object> save(){
        log.info("request to save Company");
        return companyService.saveTest(new CompanyDTO(null, "test company success", 12, 0));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save-rollback")
    public ResponseEntity<Object> saveRollback(){
        log.info("request to save Company");
        return companyService.saveTestRollback(new CompanyDTO(null, "test company rollback", 12, 0));
    }
}
