package tn.esprit.tpfoyer17.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import tn.esprit.tpfoyer17.dtos.FoyerDTO;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.services.IFoyerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/foyers") // Changed the base path to use a leading slash
public class FoyerController {
    IFoyerService foyerService;

    @PostMapping
    public FoyerDTO addFoyer(@RequestBody Foyer foyer) {
        Foyer createdFoyer = foyerService.addFoyer(foyer);
        return mapToDTO(createdFoyer);
    }

    @GetMapping
    public List<FoyerDTO> getAllFoyers() {
        return foyerService.getAllFoyers().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idFoyer}")
    public FoyerDTO getFoyer(@PathVariable("idFoyer") long idFoyer) {
        Foyer foyer = foyerService.getFoyerById(idFoyer);
        return mapToDTO(foyer);
    }

    @DeleteMapping("/{idFoyer}")
    public void deleteFoyer(@PathVariable("idFoyer") long idFoyer) {
        foyerService.deleteFoyer(idFoyer);
    }

    @PutMapping
    public FoyerDTO updateFoyer(@RequestBody Foyer foyer) {
        Foyer updatedFoyer = foyerService.updateFoyer(foyer);
        return mapToDTO(updatedFoyer);
    }

    @PostMapping("/affect")
    public FoyerDTO addFoyerAndAssignToUniversity(@RequestBody Foyer foyer,
            @RequestParam("idUniversite") long idUniversite) {
        Foyer createdFoyer = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, idUniversite);
        return mapToDTO(createdFoyer);
    }

    private FoyerDTO mapToDTO(Foyer foyer) {
        return FoyerDTO.builder()
                .id(foyer.getIdFoyer())
                .nomFoyer(foyer.getNomFoyer())
                .capaciteFoyer(foyer.getCapaciteFoyer())
                .build();
    }
}
