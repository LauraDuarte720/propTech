package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AlertType;
import co.edu.uniquindio.com.proptech.domain.enums.AttentionLevel;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;
    @Column(nullable = false)
    private boolean anomal;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @Column(nullable = false)
    private boolean reviewed;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttentionLevel attentionLevel;
}