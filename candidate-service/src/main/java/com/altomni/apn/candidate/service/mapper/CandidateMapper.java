package com.altomni.apn.candidate.service.mapper;

import com.altomni.apn.candidate.service.dto.CandidateDTO;
import com.altomni.apn.candidate.domain.Candidate;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Candidate} and its DTO {@link CandidateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CandidateMapper extends EntityMapper<CandidateDTO, Candidate> {}
