package com.kth.journalsystem.dto;



import com.kth.journalsystem.domain.Encounter;

import java.time.LocalDate;
import java.util.List;

public class EncounterDTO
{
    private Long id;
    private LocalDate visitDate;
    private PatientDTO patientDTO;

    private List<ObservationDTO> observations;

    public EncounterDTO()
    {

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

}
