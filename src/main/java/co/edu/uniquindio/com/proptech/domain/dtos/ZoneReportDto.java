package co.edu.uniquindio.com.proptech.domain.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoneReportDto {
    private String city;
    private int totalProperties;
    private int totalVisits;
    private int totalClosures;
}