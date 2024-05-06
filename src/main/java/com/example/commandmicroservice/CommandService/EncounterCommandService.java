package com.example.commandmicroservice.CommandService;

import com.example.commandmicroservice.CommandRepository.EncounterRepository;
import com.example.commandmicroservice.domain.Condition;
import com.example.commandmicroservice.domain.Encounter;
import com.example.commandmicroservice.domain.Patient;
import com.example.commandmicroservice.dtos.ConditionDTO;
import com.example.commandmicroservice.dtos.EncounterDTO;
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

public class EncounterCommandService
{
    @Autowired
    private EncounterRepository repository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EncounterCommandService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KeycloakTokenExchangeService keycloakTokenExchangeService;
    @KafkaListener(topics = "create_patient_encounter_observation_event", groupId = "command_encounter_group")
    public void handleCreateEncounterEvent(EncounterDTO encounterDTO) {
        try {
            List<String> originalScopes = encounterDTO.getAccessTokenUser().getScopes();
            if (originalScopes.size() == 3 && originalScopes.contains("patient") && originalScopes.contains("encounter") && originalScopes.contains("observation")) {
                String reducedScopes = "encounter observation";
                encounterDTO.setAccessTokenUser(keycloakTokenExchangeService.getLimitedScopeToken(encounterDTO.getAccessTokenUser(), reducedScopes));
                List<String> scopes = encounterDTO.getAccessTokenUser().getScopes();
                if (scopes.size() == 2 && scopes.contains("encounter") && scopes.contains("observation")) {
                    Patient p = new Patient(encounterDTO.getPatientDTO().getId(), encounterDTO.getPatientDTO().getFirstName(), encounterDTO.getPatientDTO().getLastName(), encounterDTO.getPatientDTO().getAge());
                    Encounter encounter = new Encounter(encounterDTO.getVisitDate(), p);
                    Encounter newEncounter = repository.save(encounter);
                    encounterDTO.getObservationDTO().setAccessTokenUser(encounterDTO.getAccessTokenUser());
                    encounterDTO.getObservationDTO().setEncounterId(newEncounter.getId());
                    String encounterJsonPayload = objectMapper.writeValueAsString(encounterDTO);
                    logger.info("Sending create_encounter_event: " + encounterJsonPayload);
                    logger.info("Sending create_patient_observation_event: " + encounterDTO.getObservationDTO());
                    kafkaTemplate.send("create_encounter_event", encounterJsonPayload);
                    kafkaTemplate.send("create_patient_observation_event", encounterDTO.getObservationDTO());
                }
            }
        }catch (JsonProcessingException e) {
            logger.error("Error serializing EncounterDTO to JSON while creating encounter: " + e.getMessage(), e);
        }
    }


    // @KafkaListener(topics = "update_encounter_event", groupId = "encounter_group")
    public void handleUpdateEncounterEvent(ConsumerRecord<String, EncounterDTO> record)
    {
        String idString = record.key(); // Extract the patient ID string from the message key
        Long id = Long.parseLong(idString); // Convert the patient ID string to Long
        EncounterDTO encounterDTO = record.value(); // Extract the patient DTO from the message value

        Patient p = new Patient(encounterDTO.getPatientDTO().getId(),encounterDTO.getPatientDTO().getFirstName(),encounterDTO.getPatientDTO().getLastName(),encounterDTO.getPatientDTO().getAge());
        Encounter encounter = new Encounter(encounterDTO.getVisitDate(),p);
        Optional<Encounter> exisitionEncounter = repository.findById(id);
        if (exisitionEncounter.isPresent()) {
            encounter.setId(id);
            repository.save(encounter);
            kafkaTemplate.send("update_encounter_event",String.valueOf(id),encounterDTO);

        } else {
            throw new RuntimeException("Can't update observation " + id);
        }
    }
    static EncounterDTO convertToDTO(Encounter encounter) {
        // Convert Patient entity to DTO
        // Implement this method based on your DTO structure
        return new EncounterDTO(encounter.getId(),encounter.getVisitDate(), PatientCommandService.convertToDTO(encounter.getPatient()));
    }

    @KafkaListener(topics = "delete_observation_event", groupId = "command_observation_group")
    public void handleDeleteEncounterEvent(Long id) {
        repository.deleteById(id);
        kafkaTemplate.send("delete_observation_event",id);
    }

}
