package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the search status of a client.
 */
public enum SearchStatus {
    ACTIVE("Active"),
    PAUSED("Paused"),
    NEGOTIATING("Negotiating"),
    CLOSED("Closed"),
    INACTIVE("Inactive");

    private final String displayName;

    SearchStatus(String displayName) {
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
    public static SearchStatus fromString(String value) {
        for (SearchStatus t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown SearchStatus: " + value);
    }
}
