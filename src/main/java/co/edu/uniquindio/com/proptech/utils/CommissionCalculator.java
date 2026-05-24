package co.edu.uniquindio.com.proptech.utils;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;

public class CommissionCalculator {

    private static final double SALE_COMMISSION_RATE   = 0.03; // 3%
    private static final double RENT_COMMISSION_RATE   = 0.08; // 8%

    public static double calculate(OperationType type, Double propertyValue) {
        if (propertyValue == null || propertyValue <= 0) return 0;
        return switch (type) {
            case SALE             -> propertyValue * SALE_COMMISSION_RATE;
            case RENT             -> propertyValue * RENT_COMMISSION_RATE;
            case CONTRACT_RENEWAL -> propertyValue * RENT_COMMISSION_RATE;
            case DEAL_CANCELLATION -> 0;
        };
    }
}