package com.example.commandmicroservice.dtos;


import com.example.commandmicroservice.config.AccessTokenUser;
import com.example.commandmicroservice.domain.Observation;

public class ObservationDTO {

    private AccessTokenUser accessTokenUser;
    private Long id;
    private String type;
    private double value;
    private PatientDTO patientDTO;

    private Long encounterId;
    public ObservationDTO(String type, double value, PatientDTO patientDTO) {
        this.type = type;
        this.value = value;
        this.patientDTO = patientDTO;
    }

    public ObservationDTO() {
    }

    public static ObservationDTO fromEntity(Observation entity) {
        ObservationDTO dto = new ObservationDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setValue(entity.getValue());
        // Set other fields as needed
        return dto;
    }

    public AccessTokenUser getAccessTokenUser() {
        return accessTokenUser;
    }

    public void setAccessTokenUser(AccessTokenUser accessTokenUser) {
        this.accessTokenUser = accessTokenUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PatientDTO getPatientDTO() {
        return patientDTO;
    }

    public void setPatientDTO(PatientDTO patientDTO) {
        this.patientDTO = patientDTO;
    }

    public Long getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Long encounterId) {
        this.encounterId = encounterId;
    }
}
