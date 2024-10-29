package tn.esprit.tpfoyer17.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import tn.esprit.tpfoyer17.dtos.ChambreResponse;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.services.IChambreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/chambres")
public class ChambreController {
    IChambreService chambreService;

    @PostMapping
    public ChambreResponse addChambre(@RequestBody Chambre chambre) {
        Chambre savedChambre = chambreService.addChambre(chambre);
        return mapToResponse(savedChambre);
    }

    @GetMapping
    public List<ChambreResponse> getAllChambres() {
        return chambreService.getAllChambres().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idChambre}")
    public ChambreResponse getChambre(@PathVariable("idChambre") long idChambre) {
        Chambre chambre = chambreService.getChambreById(idChambre);
        return mapToResponse(chambre);
    }

    @DeleteMapping("/{idChambre}")
    public void deleteChambre(@PathVariable("idChambre") long idChambre) {
        chambreService.deleteChambre(idChambre);
    }

    @PutMapping("/{idChambre}")
    public ChambreResponse updateChambre(@PathVariable("idChambre") long idChambre, @RequestBody Chambre chambre) {
        chambre.setIdChambre(idChambre); // Assuming Chambre has an 'id' field to set
        Chambre updatedChambre = chambreService.updateChambre(chambre);
        return mapToResponse(updatedChambre);
    }

    @GetMapping("/universities/{nomUniversite}")
    public List<ChambreResponse> getChambresByNomUniversite(@PathVariable("nomUniversite") String nomUniversite) {
        return chambreService.getChambresParNomUniversite(nomUniversite).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/bloc/{idBloc}/type/{typeC}")
    public List<ChambreResponse> getChambresByBlocAndType(@PathVariable("idBloc") long idBloc,
            @PathVariable("typeC") TypeChambre typeC) {
        return chambreService.getChambresParBlocEtTypeJPQL(idBloc, typeC).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/non-reserve/universities/{nomUniversite}/type/{type}")
    public List<ChambreResponse> getNonReservedChambres(@PathVariable("nomUniversite") String nomUniversite,
            @PathVariable("type") TypeChambre type) {
        return chambreService.getChambresNonReserveParNomUniversiteEtTypeChambre(nomUniversite, type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ChambreResponse mapToResponse(Chambre chambre) {
        return new ChambreResponse(chambre.getIdChambre(), (int) chambre.getNumeroChambre(),
                chambre.getTypeChambre().name());
    }
}