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
@ToString
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
    public boolean isOperable(){
        return !status.equals(PropertyStatus.SOLD);
    }

    public PropertySnapshot createSnapshot() {
        return PropertySnapshot.builder()
                .agent(agent)
                .address(address)
                .neighborhood(neighborhood)
                .propertyType(propertyType)
                .purpose(purpose)
                .price(price)
                .area(area)
                .numBedrooms(numBedrooms)
                .numBathrooms(numBathrooms)
                .status(status)
                .priceHistorySize(priceHistory.size())
                .build();
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

    public void removeAgent(){
        setAgent(null);
    }

    public boolean hasSnapshots() {
        return !history.isEmpty();
    }

    @Override
    public int compareTo(Property o) {
        return Double.compare(price, o.getPrice());
    }
}