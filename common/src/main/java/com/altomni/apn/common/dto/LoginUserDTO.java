package com.altomni.apn.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a user, with only the public attributes.
 * @author longfeiwang
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

    @ApiModelProperty(value = "url link to user's image")
    private String imageUrl;

    @ApiModelProperty(value = "phone number, this is used for consumer registered with phone.")
    private String phone;

    @ApiModelProperty(value = "The tenant id user belongs to. For consumer, it will be 1.")
    private Long tenantId;

    private CredentialDTO credential;

}
