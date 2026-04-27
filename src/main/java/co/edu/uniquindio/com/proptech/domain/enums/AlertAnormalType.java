package co.edu.uniquindio.com.proptech.domain.enums;

public enum AlertAnormalType {
    HIGH_VISITS_NO_CLOSING("High Visits No Closing"),
    CLIENT_MULTIPLE_VISITS_NO_CONTINUITY("Client Multiple Visits No Continuity"),
    ADVISOR_EXCESSIVE_OVERLOAD("Advisor Excessive Overload"),
    PROPERTY_PRICE_CHANGE("Property Price Change"),
    ZONE_INTEREST_CONCENTRATION("Zone Interest Concentration");

    private final String displayName;

    AlertAnormalType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
