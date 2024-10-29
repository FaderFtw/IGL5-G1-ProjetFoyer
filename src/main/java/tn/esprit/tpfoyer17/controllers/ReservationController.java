package tn.esprit.tpfoyer17.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer17.dtos.ReservationDTO;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.services.IReservationService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/reservations")
public class ReservationController {
    IReservationService reservationService;

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservations().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idReservation}")
    public ReservationDTO getReservation(@PathVariable("idReservation") String idReservation) {
        Reservation reservation = reservationService.getReservationById(idReservation);
        return convertToDto(reservation);
    }

    @DeleteMapping("/{idReservation}")
    public void deleteReservation(@PathVariable("idReservation") String idReservation) {
        reservationService.deleteReservation(idReservation);
    }

    @PutMapping
    public ReservationDTO updateReservation(@RequestBody Reservation reservation) {
        Reservation updatedReservation = reservationService.updateReservation(reservation);
        return convertToDto(updatedReservation);
    }

    @PostMapping("/ajouter")
    public ReservationDTO addReservation(@RequestParam("id") long id, @RequestParam("cinEtudiant") long cinEtudiant) {
        Reservation newReservation = reservationService.ajouterReservation(id, cinEtudiant);
        return convertToDto(newReservation);
    }

    @PostMapping("/annuler")
    public ReservationDTO cancelReservation(@RequestParam("cinEtudiant") long cinEtudiant) {
        Reservation canceledReservation = reservationService.annulerReservation(cinEtudiant);
        return convertToDto(canceledReservation);
    }

    @GetMapping("/byYearAndUniversity")
    public List<ReservationDTO> getReservationsByYearAndUniversity(
            @RequestParam("anneeUniversitaire") Date anneeUniversitaire,
            @RequestParam("nomUniversite") String nomUniversite) {
        return reservationService.getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversitaire, nomUniversite)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReservationDTO convertToDto(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getIdReservation())
                .anneeUniversitaire(reservation.getAnneeUniversitaire())
                .estValide(reservation.isEstValide())
                .build();
    }
}
