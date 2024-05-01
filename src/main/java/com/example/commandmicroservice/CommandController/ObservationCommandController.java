package com.example.commandmicroservice.CommandController;

import com.example.commandmicroservice.CommandService.ObservationCommandService;
import com.example.commandmicroservice.dtos.ObservationDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/observation")
public class ObservationCommandController
{
    @Autowired
    private ObservationCommandService observationEventProducer;
    @PostMapping("/")
    public ResponseEntity<ObservationDTO> createObservation(@RequestBody ObservationDTO observationDTO) {
        observationEventProducer.handleCreateObservationEvent(observationDTO);
        // ConditionDTO createdConditionDTO = new ConditionDTO(conditionDTO.getId(), conditionDTO.getConditionName(), conditionDTO.getPatient());
        return ResponseEntity.status(HttpStatus.CREATED).body(observationDTO);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<String>  updateObservation(@PathVariable Long id, @RequestBody ObservationDTO observationDTO){
        try {
            ConsumerRecord<String, ObservationDTO> x = new ConsumerRecord<String, ObservationDTO>("update_observation_event", 0, 0L, id.toString(), observationDTO); ;

            observationEventProducer.handleUpdateObservationEvent(x);
            return ResponseEntity.status(HttpStatus.CREATED).body("Retrieving observation with id: " + id);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteObservation(@PathVariable Long id){
        try
        {
            observationEventProducer.handleDeleteObservationEvent(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Deleting observation: " + id + ".......");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" " + id);

        }
    }
}
