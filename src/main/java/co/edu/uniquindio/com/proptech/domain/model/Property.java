package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.Purpose;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {
    
    private String code;
    
    private String address;

    private City city;

    private Neighborhood neighborhood;

    private PropertyType propertyType;

    private Purpose purpose;

    private double price;

    private double area;

    private int numBedrooms;

    private int numBathrooms;

    private PropertyStatus status;

    private boolean available;

    private Agent agent;
}