package com.kth.journalsystem.repository;

import com.kth.journalsystem.domain.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConditionRepository  extends JpaRepository<Condition,Long>
{
    @Query("SELECT e FROM Condition e WHERE e.patient.id = ?1")
    List<Condition> getEncountersByPatientId(Long id);

}
