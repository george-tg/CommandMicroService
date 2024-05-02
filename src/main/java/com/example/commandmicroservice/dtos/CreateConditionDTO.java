package com.example.commandmicroservice.dtos;

import com.example.commandmicroservice.config.AccessTokenUser;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateConditionDTO {

    private AccessTokenUser accessTokenUser;
    private String condition;
    private Long patientId;

    public CreateConditionDTO(@JsonProperty("condition") String condition, @JsonProperty("patientId") Long patientId) {
        this.condition = condition;
        this.patientId = patientId;
    }

    public AccessTokenUser getAccessTokenUser() {
        return accessTokenUser;
    }

    public void setAccessTokenUser(AccessTokenUser accessTokenUser) {
        this.accessTokenUser = accessTokenUser;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
