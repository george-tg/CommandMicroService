package com.example.commandmicroservice.dtos;


import com.example.commandmicroservice.config.AccessTokenUser;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateEncounterObservationDTO {

    private AccessTokenUser accessTokenUser;
    private Long patientId;

    private String observationType;
    private double observationValue;

    public CreateEncounterObservationDTO(@JsonProperty("patientId") Long patientId, @JsonProperty("observationType") String observationType, @JsonProperty("observationValue") double observationValue) {
        this.patientId = patientId;
        this.observationType = observationType;
        this.observationValue = observationValue;
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

    public String getObservationType() {
        return observationType;
    }

    public void setObservationType(String observationType) {
        this.observationType = observationType;
    }

    public double getObservationValue() {
        return observationValue;
    }

    public void setObservationValue(double observationValue) {
        this.observationValue = observationValue;
    }
}
