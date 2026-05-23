package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum representing the zone.
 */
public enum Zone {
    CENTER("Center"),
    NORTH("North"),
    SOUTH("South");

    private final String displayName;

    Zone(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static Zone fromString(String value) {
        for (Zone t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
