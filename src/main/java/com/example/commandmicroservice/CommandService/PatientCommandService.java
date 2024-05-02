package com.example.commandmicroservice.CommandService;

import com.example.commandmicroservice.CommandRepository.PatientRepository;
import com.example.commandmicroservice.config.AccessTokenUser;
import com.example.commandmicroservice.domain.Patient;
import com.example.commandmicroservice.dtos.ConditionDTO;
import com.example.commandmicroservice.dtos.CreateConditionDTO;
import com.example.commandmicroservice.dtos.PatientDTO;
import com.example.commandmicroservice.dtos.PatientDetailsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String UPDATE_PATIENT_TOPIC = "update_patient_event";
    private static final String DELETE_PATIENT_TOPIC = "delete_patient_event";
    private static final String CREATE_PATIENT_CONDITION_TOPIC = "create_patient_condition_event";

    @Autowired
    private KeycloakTokenExchangeService keycloakTokenExchangeService;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;


    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void handleDeletePatientEvent(Long id) {
        try {
            AccessTokenUser accessTokenUser = AccessTokenUser.convert(SecurityContextHolder.getContext());
            String reducedScopes = "patient";
            PatientDTO patient = new PatientDTO();
            patient.setId(id);
            patient.setAccessTokenUser(keycloakTokenExchangeService.getLimitedScopeToken(accessTokenUser, reducedScopes));
            List<String> scopes = accessTokenUser.getScopes();
            if (scopes.size() == 1 && scopes.get(0).equals("patient")) {
                patientRepository.deleteById(id);
                String jsonPayload = objectMapper.writeValueAsString(patient);
                logger.info("Sending delete_patient_event: " + jsonPayload);
                kafkaTemplate.send(DELETE_PATIENT_TOPIC, jsonPayload);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error serializing PatientDTO to JSON while deleting patient: " + e.getMessage(), e);
        }
    }

    static PatientDTO convertToDTO(Patient patient) {
        // Convert Patient entity to DTO
        // Implement this method based on your DTO structure
        return new PatientDTO(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getAge());
    }


    public void handleUpdatePatientEvent(PatientDTO patient) {
        try {
            AccessTokenUser accessTokenUser = AccessTokenUser.convert(SecurityContextHolder.getContext());
            String reducedScopes = "patient";
            patient.setAccessTokenUser(keycloakTokenExchangeService.getLimitedScopeToken(accessTokenUser, reducedScopes));
            List<String> scopes = patient.getAccessTokenUser().getScopes();
            if(scopes.size() == 1 && scopes.get(0).equals("patient")){
                Patient p = new Patient(patient.getFirstName(), patient.getLastName(), patient.getAge());
                Optional<Patient> existing = patientRepository.findById(patient.getId());
                if (existing.isPresent()) {
                    p.setId(patient.getId());
                    patientRepository.save(p);
                    String jsonPayload = objectMapper.writeValueAsString(patient);
                    logger.info("Sending update_patient_event: " + jsonPayload);
                    kafkaTemplate.send(UPDATE_PATIENT_TOPIC, jsonPayload);
                } else {
                    throw new RuntimeException("Can't update patient " + patient);
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("Error serializing PatientDTO to JSON while updating patient: " + e.getMessage(), e);
        }
    }

    public void handleUpdatePatientAddConditionEvent(CreateConditionDTO createConditionDTO) {
        AccessTokenUser accessTokenUser = AccessTokenUser.convert(SecurityContextHolder.getContext());
        String reducedScopes = "patient condition";
        createConditionDTO.setAccessTokenUser(keycloakTokenExchangeService.getLimitedScopeToken(accessTokenUser, reducedScopes));
        List<String> scopes = createConditionDTO.getAccessTokenUser().getScopes();
        if(scopes.size() == 2 && scopes.contains("patient") && scopes.contains("condition")){
            Optional<Patient> existing = patientRepository.findById(createConditionDTO.getPatientId());
            if (existing.isPresent()) {
                ConditionDTO conditionDTO = new ConditionDTO(createConditionDTO.getCondition(), new PatientDTO(existing.get().getId(), existing.get().getFirstName(), existing.get().getLastName(), existing.get().getAge()));
                conditionDTO.setAccessTokenUser(accessTokenUser);
                logger.info("Sending create_patient_condition_event: " + conditionDTO);
                kafkaTemplate.send(CREATE_PATIENT_CONDITION_TOPIC, conditionDTO);
            } else {
                throw new RuntimeException("Can't update patient to add condition " + createConditionDTO);
            }
        }
    }

    public void handleCreatePatientEvent(PatientDTO patient) {
        try {
            AccessTokenUser accessTokenUser = AccessTokenUser.convert(SecurityContextHolder.getContext());
            String reducedScopes = "patient";
            patient.setAccessTokenUser(keycloakTokenExchangeService.getLimitedScopeToken(accessTokenUser, reducedScopes));
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
