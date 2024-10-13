package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.services.IPisteServices;

import java.util.List;

@Tag(name = "\uD83C\uDFBF Piste Management")
@RestController
@RequestMapping("/piste")
@RequiredArgsConstructor
public class PisteRestController {

    private final IPisteServices pisteServices;

    @Operation(description = "Add Piste")
    @PostMapping("/add")
    public Piste addPiste(@RequestBody Piste piste){
        return  pisteServices.addPiste(piste);
    }
    @Operation(description = "Retrieve all Pistes")
    @GetMapping("/all")
    public List<Piste> getAllPistes(){
        return pisteServices.retrieveAllPistes();
    }

    @Operation(description = "Retrieve Piste by Id")
    @GetMapping("/get/{id-piste}")
    public Piste getById(@PathVariable("id-piste") Long numPiste){
        return pisteServices.retrievePiste(numPiste);
    }

    @Operation(description = "Delete Piste by Id")
    @DeleteMapping("/delete/{id-piste}")
    public void deleteById(@PathVariable("id-piste") Long numPiste){
        pisteServices.removePiste(numPiste);
    }

    // Endpoint for recommending the best piste for a skier
    @GetMapping("/recommend/{skierId}")
    public ResponseEntity<String> recommendPiste(@PathVariable Long skierId) {
        // Assuming there's a method to get a Skier by ID (skierService.getSkierById)
        Skier skier = pisteServices.findSkierById(skierId);

        if (skier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Skier not found");
        }

        // Call the service method to get piste recommendation
        String recommendation = pisteServices.recommendBestPisteForSkier(skier);
        return ResponseEntity.ok(recommendation);
    }

}
