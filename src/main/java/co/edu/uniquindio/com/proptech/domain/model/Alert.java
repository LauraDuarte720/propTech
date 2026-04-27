package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AlertType;
import co.edu.uniquindio.com.proptech.domain.enums.AttentionLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "alert")
public class Alert {
    @Id
    private int id;
    private AlertType alertType;
    private boolean anomal;
    private LocalDateTime timestamp;
    private boolean reviewed;
    private AttentionLevel attentionLevel;
}