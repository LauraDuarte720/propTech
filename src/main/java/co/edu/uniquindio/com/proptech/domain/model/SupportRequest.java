package co.edu.uniquindio.com.proptech.domain.model;


import co.edu.uniquindio.com.proptech.domain.enums.SupportRequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportRequest {
    private Client client;
    private Property property;
    private Agent agent;
    private String message;
    private LocalDateTime date;
    private SupportRequestStatus status;
}
