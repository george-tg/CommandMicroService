package com.kth.journalsystem.dto;

public class CreateConditionDTO {

    private String condition;
    private Long patientId;

    public CreateConditionDTO(String condition, Long patientId) {
        this.condition = condition;
        this.patientId = patientId;
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
