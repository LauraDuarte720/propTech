package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
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

    public Client(String cedula, String name, String username, String password, String email, String phone, double budget, int minBedrooms, ClientType clientType, SearchStatus searchStatus, PropertyType desiredPropertyType) {
        super(cedula, name, password, username);
        this.email = email;
        this.phone = phone;
        this.budget = budget;
        this.minBedrooms = minBedrooms;
        this.clientType = clientType;
        this.searchStatus = searchStatus;
        this.interestZones = new ArrayList<>();
        this.desiredPropertyType = desiredPropertyType;
        this.interactionHistory = new ArrayList<>();
    }

    public void addInterestZone(GeographicZone zone) {
        interestZones.add(zone);
    }

    public boolean removeInterestZone(GeographicZone zone) {
        for (int i = 0; i < interestZones.size(); i++) {
            if (interestZones.get(i).equals(zone)) {
                interestZones.remove(i);
                return true;
            }
        }
        return false;
    }

    public void addInteraction(UserInteraction interaction) {
        interactionHistory.add(interaction);
    }

    public UserInteraction getInteraction(String id) {
        for (int i = 0; i < interactionHistory.size(); i++) {
            UserInteraction ui = interactionHistory.get(i);
            if (ui.getId().equals(id)) return ui;
        }
        return null;
    }

    public boolean removeInteraction(String id) {
        for (int i = 0; i < interactionHistory.size(); i++) {
            if (interactionHistory.get(i).getId().equals(id)) {
                interactionHistory.remove(i);
                return true;
            }
        }
        return false;
    }
}
