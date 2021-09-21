package com.altomni.apn.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A DTO representing a user, with only the public attributes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO {

    private Long id;

    private String username;

    @ApiModelProperty(value = "first name")
    private String firstName;

    @ApiModelProperty(value = "last name")
    private String lastName;

    @ApiModelProperty(value = "email address. need to be unique.")
    private String email;

    @ApiModelProperty(value = "Whether user is activated. Default is true. Read Only.")
    private boolean activated;

    @ApiModelProperty(value = "Preferred language. e.g. en-US, zh-CN")
    private String langKey;

    @ApiModelProperty(value = "url link to user's image")
    private String imageUrl;

    @ApiModelProperty(value = "credit")
    private Integer credit;

    @ApiModelProperty(value = "phone number, this is used for consumer registered with phone.")
    private String phone;

    @ApiModelProperty(value = "The tenant id user belongs to. For consumer, it will be 1.")
    private Long tenantId;

    @ApiModelProperty(value = "The division id user belongs to.")
    private Long divisionId;

    private CredentialDTO credential;

}
