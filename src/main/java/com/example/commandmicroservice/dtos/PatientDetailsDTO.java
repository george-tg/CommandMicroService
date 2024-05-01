package com.kth.journalsystem.dto;

import java.util.ArrayList;
import java.util.List;

public class PatientDetailsDTO
{
    private Long id;

    private List<ConditionDTO> conditions = new ArrayList<>();

    private List<ObservationDTO> observations = new ArrayList<>();

    private List<EncounterDTO> encounters = new ArrayList<>();

    private String firstName;

    private String lastName;

    private int age;

    public PatientDetailsDTO(Long id, List<ConditionDTO> conditions, List<ObservationDTO> observations, List<EncounterDTO> encounters, String firstName, String lastName, int age) {
        this.id = id;
        this.conditions = conditions;
        this.observations = observations;
        this.encounters = encounters;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ConditionDTO> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionDTO> conditions) {
        this.conditions = conditions;
    }

    public List<ObservationDTO> getObservations() {
        return observations;
    }

    public void setObservations(List<ObservationDTO> observations) {
        this.observations = observations;
    }

    public List<EncounterDTO> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<EncounterDTO> encounters) {
        this.encounters = encounters;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
