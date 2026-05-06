package co.edu.uniquindio.com.proptech.utils;

import co.edu.uniquindio.com.proptech.domain.enums.AlertType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {

    private static final PropertyType[] types = PropertyType.values();
    private static final int[] propertyCounters = new int[types.length];
    private static int visitCounter = 1;
    private static int interactionCounter = 1;
    private static int operationCounter = 1;
    private static int zoneCounter = 1;
    private static int alertCounter = 1;

    static {
        for (int i = 0; i < propertyCounters.length; i++) {
            propertyCounters[i] = 1;
        }
    }

    private static String getPrefixFor(PropertyType type) {
        return switch (type) {
            case APARTMENT    -> "APT";
            case HOUSE        -> "HSE";
            case RETAIL_SPACE -> "RET";
            case OFFICE       -> "OFC";
            case LOT          -> "LOT";
            case WAREHOUSE    -> "WRH";
        };
    }

    private static int getIndexOf(PropertyType type) {
        for (int i = 0; i < types.length; i++) {
            if (types[i] == type) return i;
        }
        return -1;
    }

    public static String generatePropertyCode(PropertyType type) {
        int index = getIndexOf(type);
        int number = propertyCounters[index]++;
        return getPrefixFor(type) + "-" + String.format("%04d", number);
    }

    public static String generateInteractionCode() {
        return "INT-" + String.format("%04d", interactionCounter++);
    }


    public static String generateVisitCode() {
        return "VIS-" + String.format("%04d", visitCounter++);
    }

    public static String generateOperationCode() {
        return "OPR-" + String.format("%04d", operationCounter++);
    }

    public static String generateZoneCode() {
        return "ZON-" + String.format("%04d", zoneCounter++);
    }


    private static String getAlertPrefix(AlertType type) {
        return switch (type) {
            case CONTRACT_EXPIRING          -> "ALT-CEX";
            case PROPERTY_NO_VISITS         -> "ALT-PNV";
            case HIGH_DEMAND                -> "ALT-HDM";
            case PENDING_VISIT_CONFIRMATION -> "ALT-PVC";
            case RESERVE_NO_CLOSURE         -> "ALT-RNC";
            case INACTIVE_CLIENT            -> "ALT-ICL";
        };
    }

    public static String generateAlertCode(AlertType type) {
        return getAlertPrefix(type) + "-" + String.format("%04d", alertCounter++);
    }
}
