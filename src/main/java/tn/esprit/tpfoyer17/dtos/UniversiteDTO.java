package tn.esprit.tpfoyer17.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversiteDTO {
    private long id; // mapped from idUniversite
    private String nomUniversite;
    private String adresse;
}
