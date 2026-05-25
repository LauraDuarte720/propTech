package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.Purpose;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertySnapshot {

    private String address;
    private Neighborhood neighborhood;
    private Purpose purpose;
    private Double price;
    private Double area;
    private Integer numBedrooms;
    private Integer numBathrooms;
    private PropertyStatus status;
    private Agent agent;
    private int priceHistorySize;
    private PropertyType propertyType;

}