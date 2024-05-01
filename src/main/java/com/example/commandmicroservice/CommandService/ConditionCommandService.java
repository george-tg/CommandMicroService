package com.example.commandmicroservice.CommandService;

import com.example.commandmicroservice.CommandRepository.ConditionRepository;
import com.example.commandmicroservice.domain.Condition;
import com.example.commandmicroservice.domain.Patient;
import com.example.commandmicroservice.dtos.ConditionDTO;
import com.example.commandmicroservice.dtos.PatientDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ConditionCommandService
{

    @Autowired
    private ConditionRepository repository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    public void handleUpdateConditionEvent(ConsumerRecord<String, ConditionDTO> record)
    {
        String idString = record.key(); // Extract the patient ID string from the message key
        Long id = Long.parseLong(idString); // Convert the patient ID string to Long
        ConditionDTO conditionDTO = record.value(); // Extract the patient DTO from the message value

        Patient p = new Patient(conditionDTO.getPatient().getId(),conditionDTO.getPatient().getFirstName(),conditionDTO.getPatient().getLastName(),conditionDTO.getPatient().getAge());
        Condition condition = new Condition(conditionDTO.getConditionName(), p);
        Optional<Condition> existingCondition = repository.findById(id);
        if (existingCondition.isPresent()) {
            condition.setId(id);
            repository.save(condition);
            kafkaTemplate.send("update_condition_event",String.valueOf(id), conditionDTO);
        } else {
            throw new RuntimeException("Can't update condition " + id);
        }
    }
    static ConditionDTO convertConditionToDTO(Condition condition) {
        // Convert Patient entity to DTO
        // Implement this method based on your DTO structure
        return new ConditionDTO(condition.getId(), condition.getConditionName(), PatientCommandService.convertToDTO(condition.getPatient()));
    }

    public void handleCreateConditionEvent(ConditionDTO conditionDTO) {
        Condition condition = new Condition(conditionDTO.getConditionName(), new Patient(conditionDTO.getPatient().getFirstName(),conditionDTO.getPatient().getLastName(),conditionDTO.getPatient().getAge()));
        repository.save(condition);
        kafkaTemplate.send("create_condition_event",conditionDTO);
    }

    public void handleDeleteConditionEvent(Long id) {
        repository.deleteById(id);
        kafkaTemplate.send("delete_condition_event",id);
    }
}
