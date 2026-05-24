package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the type of user interaction.
 */
public enum InteractionType {
    CONSULTED("Consulted"),
    VISITED("Visited"),
    SAVED("Saved"),
    DISCARDED("Discarded"),
    NEGOTIATED("Negotiated"),
    BUYING_INTENTION("Buying Intention"),
    RENTING_INTENTION("Renting Intention");

    private final String displayName;

    InteractionType(String displayName) {
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
    public static InteractionType fromString(String value) {
        for (InteractionType t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}