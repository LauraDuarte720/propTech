package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.Purpose;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDtoReturn {

    private String code;

    private String address;

    private NeighborhoodDtoReturn neighborhood;

    private PropertyType propertyType;

    private Purpose purpose;

    private double price;

    private double area;

    private int numBedrooms;

    private int numBathrooms;

    private PropertyStatus status;

    private boolean available;

    private AgentDtoReturn agent;
}