package co.edu.uniquindio.com.proptech.domain.enums;
/**
 * Enum representing the type of operation.
 */
public enum OperationType {
    RENT("Rent"),
    SALE("Sale"),
    CONTRACT_RENEWAL("Contract Renewal"),
    DEAL_CANCELLATION("Deal Cancellation");

    private final String displayName;

    OperationType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
