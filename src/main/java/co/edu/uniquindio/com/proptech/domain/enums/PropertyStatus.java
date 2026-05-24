package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the status of a property.
 */
public enum PropertyStatus {
    NEW("New"),
    ACTIVE("Active"),
    RESERVED("Reserved"),
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

    @JsonCreator
    public static PropertyStatus fromString(String value) {
        for (PropertyStatus t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }

    @JsonValue           // 👈 agrega esto
    public String getKey() {
        return this.name();
    }
}
