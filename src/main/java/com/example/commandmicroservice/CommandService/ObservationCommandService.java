package com.example.commandmicroservice.CommandService;

import com.example.commandmicroservice.CommandRepository.ObservationRepository;
import com.example.commandmicroservice.domain.Observation;
import com.example.commandmicroservice.domain.Patient;
import com.example.commandmicroservice.dtos.ObservationDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class ObservationCommandService
{
    @Autowired
    private ObservationRepository repository;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void handleCreateObservationEvent(ObservationDTO observationDTO) {
        Patient p = new Patient(observationDTO.getPatientDTO().getId(),observationDTO.getPatientDTO().getFirstName(),observationDTO.getPatientDTO().getLastName(),observationDTO.getPatientDTO().getAge());
        Observation observation = new Observation(observationDTO.getType(),observationDTO.getValue(),p);
        repository.save(observation);
        kafkaTemplate.send("create_observation_event",observationDTO);
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
