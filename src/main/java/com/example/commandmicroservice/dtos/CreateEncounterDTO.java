package com.example.commandmicroservice.dtos;

import com.example.commandmicroservice.config.AccessTokenUser;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateEncounterDTO {

    private AccessTokenUser accessTokenUser;
    private Long patientId;

    public CreateEncounterDTO(@JsonProperty("patientId") Long patientId) {
        this.patientId = patientId;
    }

    public AccessTokenUser getAccessTokenUser() {
        return accessTokenUser;
    }

    public void setAccessTokenUser(AccessTokenUser accessTokenUser) {
        this.accessTokenUser = accessTokenUser;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
