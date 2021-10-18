package com.altomni.apn.talent.cache;

import com.altomni.apn.talent.service.TalentService;
import com.altomni.apn.talent.service.dto.TalentDTO;
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
    private TalentService candidateService;

    @CachePut(cacheNames = {"candidate"}, key = "'candidate:' + #result.id", unless = "#result == null ")
    public TalentDTO save(TalentDTO candidateDTO){
        return candidateService.save(candidateDTO);
    }

    @CachePut(cacheNames = {"candidate"}, key = "'candidate:' + #candidateDTO.id", unless = "#result == null ")
    public TalentDTO update(TalentDTO candidateDTO){
        return candidateService.save(candidateDTO);
    }

    @Cacheable(cacheNames = {"candidate"}, key = "'candidate:' + #id", unless = "#result == null")
    public TalentDTO getById(Long id){
        return candidateService.findOne(id).orElseThrow();
    }

    @CacheEvict(cacheNames = {"candidate"}, key = "'candidate:' + #id", allEntries = true, beforeInvocation = true)
    public void delete(Long id){
        candidateService.delete(id);
    }
}
