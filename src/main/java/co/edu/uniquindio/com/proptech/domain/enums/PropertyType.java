package co.edu.uniquindio.com.proptech.domain.enums;
/**
 * Enum representing the type of property.
 */
public enum PropertyType {
    APARTMENT("Apartment"),
    HOUSE("House"),
    RETAIL_SPACE("Retail Space"),
    OFFICE("Office"),
    LOT("Lot"),
    WAREHOUSE("Warehouse");

    private final String displayName;

    PropertyType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
