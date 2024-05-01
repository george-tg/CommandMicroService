package com.example.commandmicroservice.CommandService;

import com.example.commandmicroservice.CommandRepository.EncounterRepository;
import com.example.commandmicroservice.domain.Encounter;
import com.example.commandmicroservice.domain.Patient;
import com.example.commandmicroservice.dtos.EncounterDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class EncounterCommandService
{
    @Autowired
    private EncounterRepository repository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    //@KafkaListener(topics = "create_encounter_event", groupId = "encounter_group")
    public void handleCreateEncounterEvent(EncounterDTO encounterDTO) {
        Patient p = new Patient(encounterDTO.getPatientDTO().getId(),encounterDTO.getPatientDTO().getFirstName(),encounterDTO.getPatientDTO().getLastName(),encounterDTO.getPatientDTO().getAge());
        Encounter encounter = new Encounter(encounterDTO.getVisitDate(),p);
        repository.save(encounter);
        kafkaTemplate.send("create_encounter_event",encounterDTO);
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

    @KafkaListener(topics = "delete_observation_event", groupId = "observation_group")
    public void handleDeleteEncounterEvent(Long id) {
        repository.deleteById(id);
        kafkaTemplate.send("delete_observation_event",id);
    }

}
