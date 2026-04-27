package co.edu.uniquindio.com.proptech.domain.enums;
/**
 * Enum representing the attention level.
 */
public enum AttentionLevel {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String displayName;

    AttentionLevel(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
