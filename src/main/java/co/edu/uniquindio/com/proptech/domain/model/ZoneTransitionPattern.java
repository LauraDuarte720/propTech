package co.edu.uniquindio.com.proptech.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZoneTransitionPattern {

    private ZoneNode from;
    private ZoneNode to;
    private double   weight;
    private int      operationCount;
}