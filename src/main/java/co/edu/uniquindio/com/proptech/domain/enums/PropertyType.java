package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the type of property.
 */
public enum PropertyType {
    APARTMENT("Apartment"),
    HOUSE("House"),
    RETAIL_SPACE("Retail Space"),
    OFFICE("Office"),
    LOT("Lot"),
    WAREHOUSE("Warehouse");

    private final String displayName;

    PropertyType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonValue
    public String getKey() {
        return this.name();
    }

    @JsonCreator
    public static PropertyType fromString(String value) {
        for (PropertyType t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
