package com.altomni.apn.job.service.mapper;

import com.altomni.apn.job.domain.*;
import com.altomni.apn.job.service.dto.JobDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Job} and its DTO {@link JobDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobMapper extends EntityMapper<JobDTO, Job> {}
