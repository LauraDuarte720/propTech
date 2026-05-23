package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VisitType {

    NORMAL("Normal"),
    VIP("VIP");

    private final String displayName;

    VisitType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static VisitType fromString(String value) {
        for (VisitType t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
