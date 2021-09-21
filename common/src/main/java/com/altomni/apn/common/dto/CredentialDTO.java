package com.altomni.apn.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDTO {

        private String access_token;

        private String refresh_token;

        private String scope;

        private int expires_in;

    }