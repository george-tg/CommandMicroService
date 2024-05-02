package com.example.commandmicroservice.dtos;


import com.example.commandmicroservice.config.AccessTokenUser;
import com.example.commandmicroservice.domain.Condition;

public class ConditionDTO
{
    private AccessTokenUser accessTokenUser;
    private Long id;
    private String conditionName;
    private PatientDTO patient;

    public ConditionDTO() {
    }

    public ConditionDTO(String conditionName, PatientDTO patient) {
        this.conditionName = conditionName;
        this.patient = patient;
    }


    public ConditionDTO(Long id, String conditionName, PatientDTO patient) {
        this.id = id;
        this.conditionName = conditionName;
        this.patient = patient;
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

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }
    public static ConditionDTO fromEntity(Condition entity) {
        ConditionDTO dto = new ConditionDTO();
        dto.setId(entity.getId());
        dto.setConditionName(entity.getConditionName());
        return dto;
    }


}
