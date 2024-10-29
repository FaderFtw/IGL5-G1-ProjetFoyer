package tn.esprit.tpfoyer17.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer17.dtos.BlocDTO;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.services.IBlocService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/blocs")
@Tag(name = "Gestion des Blocs", description = "les apis pour gérer tout les Bloc")
public class BlocController {
    IBlocService blocService;

    @Operation(description = "methode pour ajouter des blocs", summary = "ajouter un bloc", operationId = "1")
    @PostMapping
    public ResponseEntity<BlocDTO> addingBloc(@RequestBody Bloc bloc) {
        Bloc savedBloc = blocService.addBloc(bloc);
        BlocDTO blocDTO = mapToDTO(savedBloc);
        return ResponseEntity.status(201).body(blocDTO);
    }

    @GetMapping
    public ResponseEntity<List<BlocDTO>> gettingAllBlocs() {
        List<BlocDTO> blocDTOs = blocService.getAllBlocs()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(blocDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlocDTO> gettingBloc(@PathVariable("id") long id) {
        Bloc bloc = blocService.getBlocById(id);
        BlocDTO blocDTO = mapToDTO(bloc);
        return ResponseEntity.ok(blocDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletingBloc(@PathVariable("id") long id) {
        blocService.deleteBloc(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlocDTO> updatingBloc(@PathVariable("id") long id, @RequestBody Bloc bloc) {
        Bloc updatedBloc = blocService.updateBloc(bloc);
        BlocDTO blocDTO = mapToDTO(updatedBloc);
        return ResponseEntity.ok(blocDTO);
    }

    @PutMapping("/{id}/affecter-chambre")
    public ResponseEntity<BlocDTO> affecterChambresABloc(@PathVariable("id") long id,
            @RequestBody List<Long> numChambre) {
        Bloc updatedBloc = blocService.affecterChambresABloc(numChambre, id);
        BlocDTO blocDTO = mapToDTO(updatedBloc);
        return ResponseEntity.ok(blocDTO);
    }

    private BlocDTO mapToDTO(Bloc bloc) {
        return BlocDTO.builder()
                .id(bloc.getIdBloc())
                .nomBloc(bloc.getNomBloc())
                .capaciteBloc(bloc.getCapaciteBloc())
                .build();
    }
}
