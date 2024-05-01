package com.example.commandmicroservice.CommandController;

import com.example.commandmicroservice.CommandService.EncounterCommandService;
import com.example.commandmicroservice.dtos.ConditionDTO;
import com.example.commandmicroservice.dtos.EncounterDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/encounter")
public class EncounterCommandController
{
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEncounter(@PathVariable Long id){
        try {
            encounterEventProducer.handleDeleteEncounterEvent(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Deleting encounter: " + id + ".......");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" " + id);

        }
    }
    @Autowired
    private EncounterCommandService encounterEventProducer;
    @PostMapping("/")
    public ResponseEntity<EncounterDTO> createEncounter(@RequestBody EncounterDTO encounterDTO) {
        encounterEventProducer.handleCreateEncounterEvent(encounterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(encounterDTO);
    }



    @PatchMapping("/{id}")
    public ResponseEntity<String>  updateEncounter(@PathVariable Long id, @RequestBody EncounterDTO encounterDTO){
        try {
            ConsumerRecord<String, EncounterDTO> x = new ConsumerRecord<String, EncounterDTO>("update_encounter_event", 0, 0L, id.toString(), encounterDTO); ;
            encounterEventProducer.handleUpdateEncounterEvent(x);
            return ResponseEntity.status(HttpStatus.CREATED).body("Updating Encounter with id: " + id);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
