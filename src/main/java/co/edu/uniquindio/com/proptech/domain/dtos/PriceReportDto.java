package co.edu.uniquindio.com.proptech.domain.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceReportDto {
    private String range;
    private int totalProperties;
    private List<PropertyDtoReturn> properties;
}
