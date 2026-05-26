package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.Purpose;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDtoUpdate {

    @NotBlank(message = "Property ID is required")
    private String code;

    private String address;

    private NeighborhoodDtoUpdate neighborhood;

    private PropertyType propertyType;

    private Purpose purpose;

    @Positive(message = "Price must be greater than 0")
    @Max(value = 1_000_000_000L, message = "Price exceeds realistic limit")
    private Double price;

    @Positive(message = "Area must be greater than 0")
    @Max(value = 1_000_000L, message = "Area exceeds realistic limit")
    private Double area;

    @Min(value = 0, message = "Number of bedrooms cannot be negative")
    @Max(value = 100, message = "Number of bedrooms exceeds realistic limit")
    private Integer numBedrooms;

    @Min(value = 0, message = "Number of bathrooms cannot be negative")
    @Max(value = 100, message = "Number of bathrooms exceeds realistic limit")
    private Integer numBathrooms;

    private String agentId;
}