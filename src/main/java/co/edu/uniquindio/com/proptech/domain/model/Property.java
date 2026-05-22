package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.Purpose;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.structures.stack.Stack;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property implements Comparable<Property> {

    private String code;
    private String address;
    private Neighborhood neighborhood;
    private PropertyType propertyType;
    private Purpose purpose;
    private Double price;
    private Double area;
    private Integer numBedrooms;
    private Integer numBathrooms;
    private PropertyStatus status;
    private Agent agent;

    @Builder.Default
    private Stack<PropertySnapshot> history = new Stack<>();

    @Builder.Default
    private LinkedList<PriceHistory> priceHistory = new LinkedList<>();

    public boolean isAvailable() {
        return this.status == PropertyStatus.ACTIVE;
    }

    @Override
    public int compareTo(Property o) {
        return Double.compare(price, o.getPrice());
    }

    public PropertySnapshot createSnapshot() {
        return PropertySnapshot.builder()
                .address(address)
                .neighborhood(neighborhood)
                .purpose(purpose)
                .price(price)
                .area(area)
                .numBedrooms(numBedrooms)
                .numBathrooms(numBathrooms)
                .status(status)
                .build();
    }

    public void restoreSnapshot(PropertySnapshot snapshot) {
        this.address = snapshot.getAddress();
        this.neighborhood = snapshot.getNeighborhood();
        this.purpose = snapshot.getPurpose();
        this.price = snapshot.getPrice();
        this.area = snapshot.getArea();
        this.numBedrooms = snapshot.getNumBedrooms();
        this.numBathrooms = snapshot.getNumBathrooms();
        this.status = snapshot.getStatus();
    }

    public void saveSnapshot() {
        history.push(createSnapshot());
    }

    public PropertySnapshot getLastSnapshot() {
        if (history.isEmpty()) {
            return null;
        }
        return history.pop();
    }

    public boolean hasSnapshots() {
        return !history.isEmpty();
    }
}