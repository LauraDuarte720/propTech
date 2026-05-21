package co.edu.uniquindio.com.proptech.domain.enums;
/**
 * Enum representing the status of a visit.
 */
public enum VisitStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    COMPLETED("Completed"),
    CANCELED("Canceled"),
    RESCHEDULED("Rescheduled"),
    PENDINGRESCHEDULE("Pending Reschedule");

    private final String displayName;

    VisitStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
