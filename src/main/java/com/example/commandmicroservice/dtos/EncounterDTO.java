package com.example.commandmicroservice.dtos;




import com.example.commandmicroservice.config.AccessTokenUser;
import com.example.commandmicroservice.domain.Encounter;

import java.time.LocalDate;

public class EncounterDTO
{
    private AccessTokenUser accessTokenUser;
    private Long id;
    private LocalDate visitDate;
    private PatientDTO patientDTO;

    private ObservationDTO observationDTO;

    public EncounterDTO() {
    }

    public EncounterDTO(LocalDate visitDate, PatientDTO patientDTO, ObservationDTO observationDTO) {
        this.visitDate = visitDate;
        this.patientDTO = patientDTO;
        this.observationDTO = observationDTO;
    }


    public EncounterDTO(LocalDate visitDate, PatientDTO patient) {
        this.visitDate = visitDate;
        this.patientDTO = patient;
    }

    public EncounterDTO(Long id, LocalDate visitDate, PatientDTO patient) {
        this.id = id;
        this.visitDate = visitDate;
        this.patientDTO = patient;
    }

    public static EncounterDTO fromEntity(Encounter entity) {
        EncounterDTO dto = new EncounterDTO();
        dto.setId(entity.getId());
        dto.setVisitDate(entity.getVisitDate());
        // Set other fields as needed
        return dto;
    }

    public AccessTokenUser getAccessTokenUser() {
        return accessTokenUser;
    }

    public void setAccessTokenUser(AccessTokenUser accessTokenUser) {
        this.accessTokenUser = accessTokenUser;
    }

    public PatientDTO getPatientDTO() {
        return patientDTO;
    }

    public void setPatientDTO(PatientDTO patientDTO) {
        this.patientDTO = patientDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public ObservationDTO getObservationDTO() {
        return observationDTO;
    }

    public void setObservationDTO(ObservationDTO observationDTO) {
        this.observationDTO = observationDTO;
    }
}
