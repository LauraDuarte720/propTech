package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.Purpose;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDtoCreate {

    @NotBlank(message = "Address is required")
    private String address;

    @Valid
    private NeighborhoodDtoCreate neighborhood;

    @NotNull(message = "Property type is required")
    private PropertyType propertyType;

    @NotNull(message = "Purpose is required")
    private Purpose purpose;

    @Positive(message = "Price must be greater than 0")
    @Max(value = 1_000_000_000L, message = "Price exceeds realistic limit")
    private double price;

    @Positive(message = "Area must be greater than 0")
    @Max(value = 1_000_000L, message = "Area exceeds realistic limit")
    private double area;

    @Min(value = 0, message = "Number of bedrooms cannot be negative")
    @Max(value = 100, message = "Number of bedrooms exceeds realistic limit")
    private int numBedrooms;

    @Min(value = 0, message = "Number of bathrooms cannot be negative")
    @Max(value =100, message = "Number of bathrooms exceeds realistic limit")
    private int numBathrooms;

    @NotNull(message = "Property status is required")
    private PropertyStatus status;

    private boolean available;
}