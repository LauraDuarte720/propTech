package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the purpose of a property.
 */
public enum Purpose {
    SALE("Sale"),
    RENT("Rent");

    private final String displayName;

    Purpose(String displayName) {
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
    public static Purpose fromString(String value) {
        for (Purpose t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
