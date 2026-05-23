package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum representing the type of client.
 */
public enum ClientType {
    BUYER("Buyer"),
    TENANT("Tenant"),
    INVESTOR("Investor");

    private final String displayName;

    ClientType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static ClientType fromString(String value) {
        for (ClientType t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
