package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {
    private String email;
    private String phone;
    private double budget;
    private int minBedrooms;
    private ClientType clientType;
    private SearchStatus searchStatus;
    private List<Zone> interestZones;
    private List<PropertyType> desiredPropertyTypes;
    private List<UserInteraction> interactionHistory;
    private List<Property> favorites;
}
