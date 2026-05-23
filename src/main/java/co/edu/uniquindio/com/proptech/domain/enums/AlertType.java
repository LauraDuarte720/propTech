package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

public enum AlertType {
    CONTRACT_EXPIRING("Contract Expiring", EntityAlert.OPERATION),
    PROPERTY_NO_VISITS("Property No Visits", EntityAlert.PROPERTY),
    HIGH_DEMAND("High Demand", EntityAlert.PROPERTY),
    PENDING_VISIT_CONFIRMATION("Pending Visit Confirmation", EntityAlert.VISIT),
    RESERVE_NO_CLOSURE("Reserve No Closure", EntityAlert.PROPERTY),
    INACTIVE_CLIENT("Inactive Client", EntityAlert.CLIENT);

    private final String displayName;
    @Getter
    private final EntityAlert requiredEntity;

    AlertType(String displayName, EntityAlert requiredEntity) {
        this.displayName = displayName;
        this.requiredEntity = requiredEntity;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static AlertType fromString(String value) {
        for (AlertType t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
