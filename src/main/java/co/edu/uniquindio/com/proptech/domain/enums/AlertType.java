package co.edu.uniquindio.com.proptech.domain.enums;
/**
 * Enum representing the type of alert.
 */
public enum AlertType {
    CONTRACT_EXPIRING("Contract Expiring"),
    PROPERTY_NO_VISITS("Property No Visits"),
    HIGH_DEMAND("High Demand"),
    PENDING_VISIT_CONFIRMATION("Pending Visit Confirmation"),
    RESERVE_NO_CLOSURE("Reserve No Closure"),
    INACTIVE_CLIENT("Inactive Client"),
    ANORMAL("Anormal");

    private final String displayName;

    AlertType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
