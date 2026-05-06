package co.edu.uniquindio.com.proptech.domain.enums;
/**
 * Enum representing the status of a process.
 */
public enum ProcessStatus {
    CREATED("Created"),
    CANCELLED("Cancelled"),
    CLOSED("Closed");

    private final String displayName;

    ProcessStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
