package tn.esprit.tpfoyer17.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoyerDTO {
    private long id; // This will map from idFoyer
    private String nomFoyer;
    private long capaciteFoyer;
}