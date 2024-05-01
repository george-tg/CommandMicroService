package com.example.commandmicroservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateEncounterDTO {
    private Long patientId;

    public CreateEncounterDTO(@JsonProperty("patientId") Long patientId) {
        this.patientId = patientId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
