package com.example.commandmicroservice.CommandService;

import com.example.commandmicroservice.CommandRepository.PatientRepository;
import com.example.commandmicroservice.domain.Patient;
import com.example.commandmicroservice.dtos.PatientDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PatientCommandService
{

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;


    @Autowired
    private PatientRepository patientRepository;

 //   @KafkaListener(topics = "delete_patient_event", groupId = "patient_group")
    public void handleDeletePatientEvent(Long id) {
        patientRepository.deleteById(id);
        kafkaTemplate.send("delete_patient_event",id);

    }

    static PatientDTO convertToDTO(Patient patient) {
        // Convert Patient entity to DTO
        // Implement this method based on your DTO structure
        return new PatientDTO(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getAge());
    }


   // @KafkaListener(topics = "update_patient_event", groupId = "patient_group")
    public void handleUpdatePatientEvent(ConsumerRecord<String, PatientDTO> record) {
        String idString = record.key(); // Extract the patient ID string from the message key
        Long id = Long.parseLong(idString); // Convert the patient ID string to Long
        PatientDTO patientDTO = record.value(); // Extract the patient DTO from the message value

        Patient p = new Patient(patientDTO.getFirstName(), patientDTO.getLastName(), patientDTO.getAge());
        Optional<Patient> existing = patientRepository.findById(id);
        if (existing.isPresent()) {
            p.setId(id);
            patientRepository.save(p);
            kafkaTemplate.send("update_patient_event",String.valueOf(id), patientDTO);
        } else {
            throw new RuntimeException("Can't update patient " + id);
        }
    }

  //  @KafkaListener(topics = "create_patient_event", groupId = "patient_group")
    public void handleCreatePatientEvent(PatientDTO patient) {
        Patient createdPatient = new Patient(patient.getFirstName(), patient.getLastName(), patient.getAge(), patient.getUserId());
        kafkaTemplate.send("create_patient_event", patient);

        patientRepository.save(createdPatient);

    }

}
