package com.altomni.apn.company.service.mapper;

import com.altomni.apn.company.domain.*;
import com.altomni.apn.company.service.dto.CompanyDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {}
