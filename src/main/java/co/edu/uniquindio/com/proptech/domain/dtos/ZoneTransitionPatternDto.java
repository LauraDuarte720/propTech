package co.edu.uniquindio.com.proptech.domain.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ZoneTransitionPatternDto {
    private ZoneNodeDto from;
    private ZoneNodeDto to;
    private double weight;
    private int operationCount;
}