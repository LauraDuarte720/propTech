package co.edu.uniquindio.com.proptech.domain.enums;

import lombok.Getter;

@Getter
public enum InteractionWeight {
    CONSULTED(1.0),
    VISITED(4.0),
    SAVED(2.0),
    NEGOTIATED(5.0),
    DISCARDED(-1.0),
    BUYING_INTENTION(3.0),
    RENTING_INTENTION(3.0);

    private final double weight;

    InteractionWeight(double weight) {
        this.weight = weight;
    }

    public static double of(InteractionType type) {
        return switch (type) {
            case CONSULTED  -> CONSULTED.weight;
            case VISITED    -> VISITED.weight;
            case SAVED      -> SAVED.weight;
            case NEGOTIATED -> NEGOTIATED.weight;
            case DISCARDED  -> DISCARDED.weight;
            case BUYING_INTENTION -> BUYING_INTENTION.weight;
            case RENTING_INTENTION -> RENTING_INTENTION.weight;
        };
    }
}