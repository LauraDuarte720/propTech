package co.edu.uniquindio.com.proptech.domain.enums;

public enum VisitType {

    NORMAL("Normal"),
    VIP("VIP");

    private final String displayName;

    VisitType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
