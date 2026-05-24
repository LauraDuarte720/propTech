package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Client extends User implements Comparable<Client> {

    private String email;
    private String phone;
    private Double budget;
    private Integer minBedrooms;
    private ClientType clientType;
    private SearchStatus searchStatus;
    @Builder.Default
    private ArrayList<GeographicZone> interestZones = new ArrayList<>();
    private PropertyType desiredPropertyType;
    @Builder.Default
    private HashTable<InteractionType, ArrayList<UserInteraction>> interactionHistory = initInteractionHistory();

    public Client(String cedula, String name, String username, String password, String email, String phone, Double budget, Integer minBedrooms, ClientType clientType, SearchStatus searchStatus, PropertyType desiredPropertyType) {
        super(cedula, name, password, username);
        this.email = email;
        this.phone = phone;
        this.budget = budget;
        this.minBedrooms = minBedrooms;
        this.clientType = clientType;
        this.searchStatus = searchStatus;
        this.interestZones = new ArrayList<>();
        this.desiredPropertyType = desiredPropertyType;
        this.interactionHistory = initInteractionHistory();
    }

    private static HashTable<InteractionType, ArrayList<UserInteraction>> initInteractionHistory() {
        HashTable<InteractionType, ArrayList<UserInteraction>> table = new HashTable<>();
        for (InteractionType type : InteractionType.values()) {
            table.put(type, new ArrayList<>());
        }
        return table;
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
        interactionHistory.get(interaction.getInteractionType()).add(interaction);
    }

    public ArrayList<UserInteraction> getInteractionsByType(InteractionType type) {
        return interactionHistory.get(type);
    }

    public UserInteraction getInteraction(String id) {
        for (InteractionType type : InteractionType.values()) {
            ArrayList<UserInteraction> list = interactionHistory.get(type);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(id)) return list.get(i);
                }
            }
        }
        return null;
    }

    public UserInteraction getInteraction(String id, InteractionType type) {
        ArrayList<UserInteraction> list = interactionHistory.get(type);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) return list.get(i);
        }
        return null;
    }

    public boolean removeInteraction(String id) {
        for (InteractionType type : InteractionType.values()) {
            ArrayList<UserInteraction> list = interactionHistory.get(type);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(id)) {
                        list.remove(i);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean removeInteraction(String id, InteractionType type) {
        ArrayList<UserInteraction> list = interactionHistory.get(type);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Client o) {
        return Double.compare(this.budget, o.getBudget());
    }
}