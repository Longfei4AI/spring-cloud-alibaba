package com.altomni.apn.talent.service;

import com.altomni.apn.talent.domain.Talent;
import com.altomni.apn.talent.repository.TalentRepository;
import com.altomni.apn.talent.service.dto.TalentDTO;
import com.altomni.apn.talent.service.mapper.TalentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Talent}.
 */
@Service
@Transactional
public class TalentService {

    private final Logger log = LoggerFactory.getLogger(TalentService.class);

    private final TalentRepository candidateRepository;

    private final TalentMapper candidateMapper;

    public TalentService(TalentRepository candidateRepository, TalentMapper candidateMapper) {
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }

    /**
     * Save a candidate.
     *
     * @param candidateDTO the entity to save.
     * @return the persisted entity.
     */
    public TalentDTO save(TalentDTO candidateDTO) {
        log.info("Request to save Candidate : {}", candidateDTO);
        Talent candidate = candidateMapper.toEntity(candidateDTO);
        candidate = candidateRepository.save(candidate);
        return candidateMapper.toDto(candidate);
    }

    /**
     * Partially update a candidate.
     *
     * @param candidateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TalentDTO> partialUpdate(TalentDTO candidateDTO) {
        log.debug("Request to partially update Candidate : {}", candidateDTO);

        return candidateRepository
            .findById(candidateDTO.getId())
            .map(
                existingCandidate -> {
                    candidateMapper.partialUpdate(existingCandidate, candidateDTO);
                    return existingCandidate;
                }
            )
            .map(candidateRepository::save)
            .map(candidateMapper::toDto);
    }

    /**
     * Get all the candidates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TalentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Candidates");
        return candidateRepository.findAll(pageable).map(candidateMapper::toDto);
    }

    /**
     * Get one candidate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TalentDTO> findOne(Long id) {
        log.info("Request to get Candidate : {}", id);
        return candidateRepository.findById(id).map(candidateMapper::toDto);
    }

    /**
     * Delete the candidate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.info("Request to delete Candidate : {}", id);
        candidateRepository.deleteById(id);
    }
}
