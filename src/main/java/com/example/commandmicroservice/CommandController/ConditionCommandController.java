package com.example.commandmicroservice.CommandController;

import com.example.commandmicroservice.CommandService.ConditionCommandService;
import com.example.commandmicroservice.dtos.ConditionDTO;
import com.example.commandmicroservice.dtos.PatientDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/condition")

public class ConditionCommandController
{
    @Autowired
    ConditionCommandService conditionEventProducer;

    @PostMapping("/")
    public ResponseEntity<ConditionDTO> createCondition(@RequestBody ConditionDTO conditionDTO) {
        conditionEventProducer.handleCreateConditionEvent(conditionDTO);
        ConditionDTO createdConditionDTO = new ConditionDTO(conditionDTO.getId(), conditionDTO.getConditionName(), conditionDTO.getPatient());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConditionDTO);
    }


    @PatchMapping("/{id}")
    public String updateCondition(@PathVariable Long id, @RequestBody ConditionDTO updatedConditionDTO){
        try {
            ConsumerRecord<String, ConditionDTO> x = new ConsumerRecord<String, ConditionDTO>("update_condition_event", 0, 0L, id.toString(), updatedConditionDTO); ;

            conditionEventProducer.handleUpdateConditionEvent(x);
            return "Updating condition" + id + ".......";

        }catch (Exception e){
            return "updating failed: " + e.toString();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCondition(@PathVariable Long id){
        try {
            conditionEventProducer.handleDeleteConditionEvent(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Deleting condition: " + id + ".......");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" " + id);

        }
    }

}
