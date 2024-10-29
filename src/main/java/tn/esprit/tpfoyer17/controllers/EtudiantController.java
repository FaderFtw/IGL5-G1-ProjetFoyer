package tn.esprit.tpfoyer17.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.tpfoyer17.dtos.EtudiantDTO;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.services.IEtudiantService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/etudiants")
public class EtudiantController {
    IEtudiantService etudiantService;
    EtudiantRepository etudiantRepository;

    @PostMapping
    public ResponseEntity<EtudiantDTO> addEtudiant(@Valid @RequestBody Etudiant etudiant) {
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        EtudiantDTO responseDto = mapToDto(savedEtudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<EtudiantDTO>> getAllEtudiants() {
        List<EtudiantDTO> responseDtos = etudiantService.getAllEtudiants().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{idEtudiant}")
    public ResponseEntity<EtudiantDTO> getEtudiant(@PathVariable("idEtudiant") long idEtudiant) {
        Etudiant etudiant = etudiantService.getEtudiantById(idEtudiant);
        if (etudiant != null) {
            EtudiantDTO responseDto = mapToDto(etudiant);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idEtudiant}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable("idEtudiant") long idEtudiant) {
        etudiantService.deleteEtudiant(idEtudiant);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idEtudiant}")
    public ResponseEntity<EtudiantDTO> updateEtudiant(@PathVariable("idEtudiant") long idEtudiant,
            @Valid @RequestBody Etudiant etudiant) {
        Etudiant updatedEtudiant = etudiantService.updateEtudiant(etudiant);
        if (updatedEtudiant != null) {
            EtudiantDTO responseDto = mapToDto(updatedEtudiant);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private EtudiantDTO mapToDto(Etudiant etudiant) {
        return new EtudiantDTO(
                etudiant.getIdEtudiant(),
                etudiant.getNomEtudiant(),
                etudiant.getPrenomEtudiant(),
                etudiant.getCinEtudiant(),
                etudiant.getDateNaissance() != null ? etudiant.getDateNaissance().toString() : null);
    }
}
