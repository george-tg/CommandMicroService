package com.example.commandmicroservice.CommandService;

import com.example.commandmicroservice.CommandRepository.PatientRepository;
import com.example.commandmicroservice.config.AccessTokenUser;
import com.example.commandmicroservice.config.KeycloakLogoutHandler;
import com.example.commandmicroservice.domain.Patient;
import com.example.commandmicroservice.dtos.PatientDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PatientCommandService
{
    private static final Logger logger = LoggerFactory.getLogger(PatientCommandService.class);
    private static final String CREATE_PATIENT_TOPIC = "create_patient_event";

    @Autowired
    private KeycloakTokenExchangeService keycloakTokenExchangeService;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;


    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void handleDeletePatientEvent(Long id) {
        patientRepository.deleteById(id);
        kafkaTemplate.send("delete_patient_event",id);

    }

    static PatientDTO convertToDTO(Patient patient) {
        // Convert Patient entity to DTO
        // Implement this method based on your DTO structure
        return new PatientDTO(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getAge());
    }


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

    public void handleCreatePatientEvent(PatientDTO patient) {
        try {
            AccessTokenUser accessTokenUser = AccessTokenUser.convert(SecurityContextHolder.getContext());
            patient.setAccessTokenUser(keycloakTokenExchangeService.getLimitedScopeToken(accessTokenUser));
            List<String> scopes = patient.getAccessTokenUser().getScopes();
            if(scopes.size() == 1 && scopes.get(0).equals("patient")){
                Patient createdPatient = new Patient(patient.getFirstName(), patient.getLastName(), patient.getAge(), patient.getUserId());
                patientRepository.save(createdPatient);
                String jsonPayload = objectMapper.writeValueAsString(patient);
                logger.info("Sending create_patient_event: " + jsonPayload);
                kafkaTemplate.send(CREATE_PATIENT_TOPIC, jsonPayload);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error serializing PatientDTO to JSON while creating patient: " + e.getMessage(), e);
        }
    }


}
