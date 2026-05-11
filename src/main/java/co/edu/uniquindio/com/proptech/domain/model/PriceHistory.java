package co.edu.uniquindio.com.proptech.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PriceHistory {

        private Double oldPrice;
        private Double newPrice;
        private LocalDateTime changedAt;
}
