package com.kth.journalsystem.dto;


import com.kth.journalsystem.domain.Observation;

public class ObservationDTO {
    private Long id;
    private String type;
    private double value;
    private PatientDTO patientDTO;

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
}
