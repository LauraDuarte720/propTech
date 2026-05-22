package co.edu.uniquindio.com.proptech.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ZoneTransitionPattern {

    private ZoneNode from;
    private ZoneNode to;
    private double   weight;   // peso acumulado en la arista del grafo

    public String getDescription() {
        return from.getLabel() + " → " + to.getLabel()
                + " (frecuencia: " + (int) weight + ")";
    }
}