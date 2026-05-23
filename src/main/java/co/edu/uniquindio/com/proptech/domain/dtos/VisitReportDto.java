package co.edu.uniquindio.com.proptech.domain.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitReportDto {
    private PropertyDtoReturn property;
    private int totalVisits;
}