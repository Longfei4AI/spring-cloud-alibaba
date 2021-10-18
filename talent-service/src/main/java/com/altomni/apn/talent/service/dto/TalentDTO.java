package com.altomni.apn.talent.service.dto;

import com.altomni.apn.talent.domain.Talent;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Talent} entity.
 * @author longfeiwang
 */
@ApiModel(
    description = ""
)
public class TalentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer age;

    private Integer status;

    public TalentDTO() {
    }

    public TalentDTO(Long id, String name, Integer age, Integer status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = status;
    }

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
        if (!(o instanceof TalentDTO)) {
            return false;
        }

        TalentDTO candidateDTO = (TalentDTO) o;
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
