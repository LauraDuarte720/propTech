package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AlertAbnormalType {
    HIGH_VISITS_NO_CLOSING("High Visits No Closing", EntityAlert.PROPERTY),
    CLIENT_MULTIPLE_VISITS_NO_CONTINUITY("Client Multiple Visits No Continuity", EntityAlert.CLIENT),
    AGENT_EXCESSIVE_OVERLOAD("Advisor Excessive Overload", EntityAlert.AGENT),
    PROPERTY_PRICE_CHANGE("Property Price Change", EntityAlert.PROPERTY),
    ZONE_INTEREST_CONCENTRATION("Zone Interest Concentration", EntityAlert.ZONE);

    private final String displayName;
    @Getter
    private final EntityAlert requiredEntity;

    AlertAbnormalType(String displayName, EntityAlert requiredEntity) {
        this.displayName = displayName;
        this.requiredEntity = requiredEntity;
    }

    @JsonValue
    public String getKey() {
        return this.name();
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static AlertAbnormalType fromString(String value) {
        for (AlertAbnormalType t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
