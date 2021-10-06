package com.altomni.apn.job.service;

import com.altomni.apn.job.domain.Job;
import com.altomni.apn.job.repository.JobRepository;
import com.altomni.apn.job.service.dto.JobDTO;
import com.altomni.apn.job.service.mapper.JobMapper;
import feign.RequestTemplate;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Job}.
 */
@Service
@Transactional
public class JobService {

    private final Logger log = LoggerFactory.getLogger(JobService.class);

    @Resource
    private JobRepository jobRepository;

    @Resource
    private JobMapper jobMapper;

    @Resource
    private CompanyService companyService;

    @Resource
    private RestTemplate restTemplate;

    /**
     * Save a job.
     *
     * @param jobDTO the entity to save.
     * @return the persisted entity.
     */
    public JobDTO save(JobDTO jobDTO) {
        log.debug("Request to save Job : {}", jobDTO);
        Job job = jobMapper.toEntity(jobDTO);
        job = jobRepository.save(job);
        return jobMapper.toDto(job);
    }

    @GlobalTransactional
    public ResponseEntity<Object> saveTestSuccess(JobDTO jobDTO) {
        log.info("Request to save Job : {}", jobDTO);
        Job job = jobMapper.toEntity(jobDTO);
        jobRepository.save(job);
        ResponseEntity<Object> response = companyService.saveCompany();
        return response;
    }

    @GlobalTransactional
    public ResponseEntity<Object> saveTestRollback(JobDTO jobDTO) {
        log.info("Request to save Job : {}", jobDTO);
        Job job = jobMapper.toEntity(jobDTO);
        jobRepository.save(job);
        ResponseEntity<Object> response = companyService.saveCompanyRollback();
        return response;
    }

    /**
     * Partially update a job.
     *
     * @param jobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JobDTO> partialUpdate(JobDTO jobDTO) {
        log.debug("Request to partially update Job : {}", jobDTO);

        return jobRepository
            .findById(jobDTO.getId())
            .map(
                existingJob -> {
                    jobMapper.partialUpdate(existingJob, jobDTO);

                    return existingJob;
                }
            )
            .map(jobRepository::save)
            .map(jobMapper::toDto);
    }

    /**
     * Get all the jobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Jobs");
        return jobRepository.findAll(pageable).map(jobMapper::toDto);
    }

    /**
     * Get one job by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JobDTO> findOne(Long id) {
        log.debug("Request to get Job : {}", id);
        return jobRepository.findById(id).map(jobMapper::toDto);
    }

    /**
     * Delete the job by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Job : {}", id);
        jobRepository.deleteById(id);
    }
}
