package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.Purpose;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "property")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
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
    @ManyToOne
    private Agent agent;
}