package co.edu.uniquindio.com.proptech.domain.enums;
/**
 * Enum representing the status of a property.
 */
public enum PropertyStatus {
    NEW("New"),
    ACTIVE("Active"),
    RESERVED("Reserved"),
    UNDER_NEGOTIATION("Under Negotiation"),
    SOLD("Sold"),
    RENTED("Rented"),
    INACTIVE("Inactive");

    private final String displayName;

    PropertyStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
