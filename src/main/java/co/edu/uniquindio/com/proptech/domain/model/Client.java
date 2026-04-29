package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Client extends User {

    private String email;
    
    private String phone;
    
    private double budget;
    
    private int minBedrooms;
    
    private ClientType clientType;
    
    private SearchStatus searchStatus;

    private ArrayList<GeographicZone> interestZones;
    
    private PropertyType desiredPropertyType;

    private ArrayList<UserInteraction> interactionHistory;
}
