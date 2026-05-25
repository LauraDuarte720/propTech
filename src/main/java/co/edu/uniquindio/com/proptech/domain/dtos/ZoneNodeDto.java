package co.edu.uniquindio.com.proptech.domain.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ZoneNodeDto {
    private String level;
    private String city;
    private String zone;
    private String neighborhoodName;
    private String label;
}