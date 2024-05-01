package com.kth.journalsystem.dto;

public class CreateObservationDTO {

    private String type;
    private double value;
    private Long patientId;

    private Long encounterId;

    public CreateObservationDTO(String type, double value, Long patientId, Long encounterId) {
        this.type = type;
        this.value = value;
        this.patientId = patientId;
        this.encounterId = encounterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Long encounterId) {
        this.encounterId = encounterId;
    }
}
