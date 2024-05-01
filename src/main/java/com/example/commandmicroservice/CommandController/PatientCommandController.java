package com.example.commandmicroservice.CommandController;


import com.example.commandmicroservice.CommandService.PatientCommandService;
import com.example.commandmicroservice.config.AccessTokenUser;
import com.example.commandmicroservice.dtos.PatientDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")

public class PatientCommandController
{
    @Autowired
    PatientCommandService patientEventProducer;

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_doctor')")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patient) {
        try {
            PatientDTO createdPatientDTO = new PatientDTO(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getAge());
            createdPatientDTO.setAccessTokenUser(AccessTokenUser.convert(SecurityContextHolder.getContext()));
            patientEventProducer.handleCreatePatientEvent(createdPatientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPatientDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_doctor')")
    public ResponseEntity<String> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patient){
        try {
            ConsumerRecord<String, PatientDTO> x = new ConsumerRecord<String, PatientDTO>("update_patient_event", 0, 0L, id.toString(), patient); ;

            patientEventProducer.handleUpdatePatientEvent(x);
            return ResponseEntity.status(HttpStatus.CREATED).body("Updating patient" + id + ".......");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_doctor')")
    public ResponseEntity<String> deletePatient(@PathVariable Long id){
        try {
            patientEventProducer.handleDeletePatientEvent(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Deleting patient: " + id + ".......");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" " + id);

        }
    }

}
