package tn.esprit.tpfoyer17.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlocDTO {
    private long id; // this will map to idBloc
    private String nomBloc;
    private long capaciteBloc;

    // Optionally, you can include other fields as needed
}
