package com.altomni.apn.talent.service.mapper;

import com.altomni.apn.talent.service.dto.TalentDTO;
import com.altomni.apn.talent.domain.Talent;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Talent} and its DTO {@link TalentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TalentMapper extends EntityMapper<TalentDTO, Talent> {}
