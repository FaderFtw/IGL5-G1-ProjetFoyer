package tn.esprit.tpfoyer17.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChambreResponse {
    private long id; // this will map to idChambre
    private int numeroChambre;
    private String typeChambre;
}
