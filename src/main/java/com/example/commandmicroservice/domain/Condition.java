package com.example.commandmicroservice.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "conditions_command")
public class Condition
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conditionName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    public Condition(Long id, String conditionName, Patient patient) {
        this.id = id;
        this.conditionName = conditionName;
        this.patient = patient;
    }


    public Condition(String conditionName, Patient patient) {
        this.conditionName = conditionName;
        this.patient = patient;
    }

    public Condition() {

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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
