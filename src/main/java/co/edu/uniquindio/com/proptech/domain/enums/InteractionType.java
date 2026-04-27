package co.edu.uniquindio.com.proptech.domain.enums;
/**
 * Enum representing the type of user interaction.
 */
public enum InteractionType {
    CONSULTED("Consulted"),
    VISITED("Visited"),
    SAVED("Saved"),
    DISCARDED("Discarded"),
    NEGOTIATED("Negotiated");

    private final String displayName;

    InteractionType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
