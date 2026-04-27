package co.edu.uniquindio.com.proptech.domain.enums;
/**
 * Enum representing the purpose of a property.
 */
public enum Purpose {
    SALE("Sale"),
    RENT("Rent");

    private final String displayName;

    Purpose(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
