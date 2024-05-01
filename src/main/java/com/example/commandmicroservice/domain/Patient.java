package com.example.commandmicroservice.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients_command")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private Integer age;


    //tessssssssst
    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    private List<Condition> conditions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    private List<Observation> observations = new ArrayList<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    private List<Encounter> encounters = new ArrayList<>();

    @Column(name = "user_id")
    private String userId;


    public Patient(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Patient(Long id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Patient(String firstName, String lastName, int age, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.userId = userId;
    }

    public Patient(Long id, String firstName, String lastName, int age, List<Condition> conditions, List<Observation> observations, List<Encounter> encounters) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.conditions = conditions;
        this.observations = observations;
        this.encounters = encounters;
    }

    public Patient(Long id, String firstName, String lastName, Integer age, List<Condition> conditions, List<Observation> observations, List<Encounter> encounters, String userId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.conditions = conditions;
        this.observations = observations;
        this.encounters = encounters;
        this.userId = userId;
    }

    public Patient() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Condition> getConditions() {
        return conditions;
    }
    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    public List<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<Encounter> encounters) {
        this.encounters = encounters;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

