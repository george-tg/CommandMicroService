package com.example.commandmicroservice.CommandController;

import com.example.commandmicroservice.CommandService.EncounterCommandService;
import com.example.commandmicroservice.CommandService.PatientCommandService;
import com.example.commandmicroservice.dtos.CreateEncounterObservationDTO;
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
    @Autowired
    private EncounterCommandService encounterCommandService;

    @Autowired
    private PatientCommandService patientCommandService;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEncounter(@PathVariable Long id){
        try {
            encounterCommandService.handleDeleteEncounterEvent(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Deleting encounter: " + id + ".......");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" " + id);

        }
    }
    @PostMapping("/")
    public ResponseEntity<String> createEncounter(@RequestBody CreateEncounterObservationDTO encounterDTO) {
        patientCommandService.addEncounterObservationEvent(encounterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Creating encounter and observation");
    }



    @PatchMapping("/{id}")
    public ResponseEntity<String>  updateEncounter(@PathVariable Long id, @RequestBody EncounterDTO encounterDTO){
        try {
            ConsumerRecord<String, EncounterDTO> x = new ConsumerRecord<String, EncounterDTO>("update_encounter_event", 0, 0L, id.toString(), encounterDTO); ;
            encounterCommandService.handleUpdateEncounterEvent(x);
            return ResponseEntity.status(HttpStatus.CREATED).body("Updating Encounter with id: " + id);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
