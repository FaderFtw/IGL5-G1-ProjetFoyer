package tn.esprit.tpfoyer17.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer17.dtos.UniversiteDTO;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.services.IUniversiteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/universites") // Corrected spelling for consistency
public class UniversiteController {
    IUniversiteService universiteService;

    @PostMapping
    public UniversiteDTO addingUniversite(@RequestBody Universite universite) {
        Universite createdUniversite = universiteService.addUniversite(universite);
        return mapToDTO(createdUniversite);
    }

    @GetMapping
    public List<UniversiteDTO> gettingAllUniversites() {
        return universiteService.getAllUniversites()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}") // Use path variable for RESTful API
    public UniversiteDTO gettingUniversite(@PathVariable("id") long id) {
        Universite universite = universiteService.getUniversiteById(id);
        return mapToDTO(universite);
    }

    @DeleteMapping("/{id}")
    public void deletingUniversite(@PathVariable("id") long id) {
        universiteService.deleteUniversite(id);
    }

    @PutMapping("/{id}") // Specify id in the path
    public UniversiteDTO updatingUniversite(@PathVariable("id") long id, @RequestBody Universite universite) {
        Universite updatedUniversite = universiteService.updateUniversite(universite);
        return mapToDTO(updatedUniversite);
    }

    @PutMapping("/{id}/affecter-foyer") // Use path variable
    public UniversiteDTO affecterFoyerAUniversite(@PathVariable("id") long id,
            @RequestParam("nomUniversite") String nomUniversite) {
        Universite universite = universiteService.affecterFoyerAUniversite(id, nomUniversite);
        return mapToDTO(universite);
    }

    @PutMapping("/{id}/desaffecter-foyer") // Use path variable
    public UniversiteDTO desaffecterFoyerAUniversite(@PathVariable("id") long id) {
        Universite universite = universiteService.desaffecterFoyerAUniversite(id);
        return mapToDTO(universite);
    }

    private UniversiteDTO mapToDTO(Universite universite) {
        return UniversiteDTO.builder()
                .id(universite.getIdUniversite())
                .nomUniversite(universite.getNomUniversite())
                .adresse(universite.getAdresse())
                .build();
    }
}
