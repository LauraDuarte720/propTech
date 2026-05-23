package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum representing the city.
 */
public enum City {
    ARAUCA("Arauca"),
    ARMENIA("Armenia"),
    BARRANQUILLA("Barranquilla"),
    BOGOTA("Bogota"),
    BUCARAMANGA("Bucaramanga"),
    CALI("Cali"),
    CARTAGENA("Cartagena"),
    CUCUTA("Cucuta"),
    FLORENCIA("Florencia"),
    IBAGUE("Ibague"),
    INIRIDA("Inirida"),
    LETICIA("Leticia"),
    MANIZALES("Manizales"),
    MEDELLIN("Medellin"),
    MITU("Mitu"),
    MOCOA("Mocoa"),
    MONTERIA("Monteria"),
    NEIVA("Neiva"),
    PASTO("Pasto"),
    PEREIRA("Pereira"),
    POPAYAN("Popayan"),
    PUERTO_CARRENO("Puerto Carreno"),
    QUIBDO("Quibdo"),
    RIOHACHA("Riohacha"),
    SAN_ANDRES("San Andres"),
    SAN_JOSE_DEL_GUAVIARE("San Jose del Guaviare"),
    SANTA_MARTA("Santa Marta"),
    SINCELEJO("Sincelejo"),
    TUNJA("Tunja"),
    VALLEDUPAR("Valledupar"),
    VILLAVICENCIO("Villavicencio"),
    YOPAL("Yopal");

    private final String displayName;

    City(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static City fromString(String value) {
        for (City t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
