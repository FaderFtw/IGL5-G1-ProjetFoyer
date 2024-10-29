package tn.esprit.tpfoyer17.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationDTO {
    String id; // Maps to idReservation
    Date anneeUniversitaire;
    boolean estValide;

    // You may include additional fields as needed
}