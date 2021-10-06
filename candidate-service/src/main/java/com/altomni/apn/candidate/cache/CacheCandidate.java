package com.altomni.apn.candidate.cache;

import com.altomni.apn.candidate.service.CandidateService;
import com.altomni.apn.candidate.service.dto.CandidateDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author longfeiwang
 */
@Service
public class CacheCandidate {
    @Resource
    private CandidateService candidateService;

    @CachePut(cacheNames = {"candidate"}, key = "'candidate:' + #result.id", unless = "#result == null ")
    public CandidateDTO save(CandidateDTO candidateDTO){
        return candidateService.save(candidateDTO);
    }

    @CachePut(cacheNames = {"candidate"}, key = "'candidate:' + #candidateDTO.id", unless = "#result == null ")
    public CandidateDTO update(CandidateDTO candidateDTO){
        return candidateService.save(candidateDTO);
    }

    @Cacheable(cacheNames = {"candidate"}, key = "'candidate:' + #id", unless = "#result == null")
    public CandidateDTO getById(Long id){
        return candidateService.findOne(id).orElseThrow();
    }

    @CacheEvict(cacheNames = {"candidate"}, key = "'candidate:' + #id", allEntries = true, beforeInvocation = true)
    public void delete(Long id){
        candidateService.delete(id);
    }
}
