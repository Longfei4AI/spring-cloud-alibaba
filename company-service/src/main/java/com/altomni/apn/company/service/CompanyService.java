package com.altomni.apn.company.service;

import com.altomni.apn.company.domain.Company;
import com.altomni.apn.company.repository.CompanyRepository;
import com.altomni.apn.company.service.dto.CompanyDTO;
import com.altomni.apn.company.service.mapper.CompanyMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    @Resource
    private CandidateService candidateService;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);
        return companyMapper.toDto(company);
    }

    public ResponseEntity<Object> saveTest(CompanyDTO companyDTO) {
        log.info("Request to save Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        companyRepository.save(company);
        ResponseEntity<Object> response = candidateService.saveCandidate();
        return response;
    }

    public ResponseEntity<Object> saveTestRollback(CompanyDTO companyDTO) {
        log.info("Request to save Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        companyRepository.save(company);
        ResponseEntity<Object> response = candidateService.saveCandidate();
        int a = 1/0;
        return response;
    }

    /**
     * Partially update a company.
     *
     * @param companyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyDTO> partialUpdate(CompanyDTO companyDTO) {
        log.debug("Request to partially update Company : {}", companyDTO);

        return companyRepository
            .findById(companyDTO.getId())
            .map(
                existingCompany -> {
                    companyMapper.partialUpdate(existingCompany, companyDTO);

                    return existingCompany;
                }
            )
            .map(companyRepository::save)
            .map(companyMapper::toDto);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable).map(companyMapper::toDto);
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyDTO> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id).map(companyMapper::toDto);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
