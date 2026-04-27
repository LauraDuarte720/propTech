package co.edu.uniquindio.com.proptech.domain.enums;

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
}
