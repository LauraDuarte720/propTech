package co.edu.uniquindio.com.proptech.domain.enums;
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
}
