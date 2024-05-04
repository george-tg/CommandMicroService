package com.example.commandmicroservice.CommandService;

import com.example.commandmicroservice.CommandRepository.EncounterRepository;
import com.example.commandmicroservice.CommandRepository.ObservationRepository;
import com.example.commandmicroservice.domain.Condition;
import com.example.commandmicroservice.domain.Observation;
import com.example.commandmicroservice.domain.Patient;
import com.example.commandmicroservice.dtos.ConditionDTO;
import com.example.commandmicroservice.dtos.ObservationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ObservationCommandService
{
    @Autowired
    private ObservationRepository repository;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ObservationCommandService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KeycloakTokenExchangeService keycloakTokenExchangeService;

    @Autowired
    private EncounterRepository encounterRepository;

    @KafkaListener(topics = "create_patient_observation_event", groupId = "observation_group")
    public void handleCreateObservationEvent(ObservationDTO observationDTO) {
        try{
            List<String> originalScopes = observationDTO.getAccessTokenUser().getScopes();
            if(originalScopes.size() == 2 && originalScopes.contains("encounter") && originalScopes.contains("observation")) {
                String reducedScopes = "observation";
                observationDTO.setAccessTokenUser(keycloakTokenExchangeService.getLimitedScopeToken(observationDTO.getAccessTokenUser(), reducedScopes));
                List<String> scopes = observationDTO.getAccessTokenUser().getScopes();
                if (scopes.size() == 1 && scopes.contains("observation")) {
                    Patient p = new Patient(observationDTO.getPatientDTO().getId(),observationDTO.getPatientDTO().getFirstName(),observationDTO.getPatientDTO().getLastName(),observationDTO.getPatientDTO().getAge());
                    Observation observation = new Observation(observationDTO.getType(),observationDTO.getValue(),p);
                    observation.setEncounter(encounterRepository.findById(observationDTO.getEncounterId()).get());
                    repository.save(observation);
                    String jsonPayload = objectMapper.writeValueAsString(observationDTO);
                    kafkaTemplate.send("create_observation_event",jsonPayload);
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("Error serializing ObservationDTO to JSON while creating observation: " + e.getMessage(), e);
        }
    }

    public void handleUpdateObservationEvent(ConsumerRecord<String, ObservationDTO> record)
    {
        String idString = record.key(); // Extract the patient ID string from the message key
        Long id = Long.parseLong(idString); // Convert the patient ID string to Long
        ObservationDTO observationDTO = record.value(); // Extract the patient DTO from the message value

        Patient p = new Patient(observationDTO.getPatientDTO().getId(),observationDTO.getPatientDTO().getFirstName(),observationDTO.getPatientDTO().getLastName(),observationDTO.getPatientDTO().getAge());
        Observation observation = new Observation(observationDTO.getType(), observationDTO.getValue(),p);
        Optional<Observation> existingCondition = repository.findById(id);
        if (existingCondition.isPresent()) {
            observation.setId(id);
            repository.save(observation);
            kafkaTemplate.send("update_observation_event", id.toString(),observationDTO);
        } else {
            throw new RuntimeException("Can't update observation " + id);
        }
    }
    public void handleDeleteObservationEvent(Long id) {
        repository.deleteById(id);
        kafkaTemplate.send("delete_observation_event",id);
    }
    static ObservationDTO convertToDTO(Observation observation) {
        // Convert Patient entity to DTO
        // Implement this method based on your DTO structure
        return new ObservationDTO(observation.getId().toString(), observation.getValue(), PatientCommandService.convertToDTO(observation.getPatient()));
    }
}
