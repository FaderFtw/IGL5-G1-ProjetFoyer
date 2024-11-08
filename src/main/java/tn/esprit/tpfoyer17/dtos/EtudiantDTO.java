package tn.esprit.tpfoyer17.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtudiantDTO {
    private long id; // Mapped from idEtudiant
    private String nomEtudiant;
    private String prenomEtudiant;
    private long cinEtudiant;
    private String dateNaissance; // Consider using a suitable date format, e.g., ISO-8601
}
