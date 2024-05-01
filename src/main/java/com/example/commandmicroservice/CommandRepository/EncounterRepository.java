package com.example.commandmicroservice.CommandRepository;

import com.example.commandmicroservice.domain.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EncounterRepository extends JpaRepository<Encounter, Long> {
    @Query("SELECT e FROM Encounter e WHERE e.patient.id = ?1")
    List<Encounter> getEncountersByPatientId(Long id);

}
