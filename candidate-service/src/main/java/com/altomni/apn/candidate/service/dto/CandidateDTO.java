package com.altomni.apn.candidate.service.dto;

import com.altomni.apn.candidate.domain.Candidate;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Candidate} entity.
 */
@ApiModel(
    description = ""
)
public class CandidateDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer age;

    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidateDTO)) {
            return false;
        }

        CandidateDTO candidateDTO = (CandidateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, candidateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidateDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", status=" + getStatus() +
            "}";
    }
}
