package com.kth.journalsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatientRequestDTO {
    private Long id;

    public PatientRequestDTO(@JsonProperty("id") Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
