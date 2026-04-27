package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "userinteraction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionType interactionType;
    @Column(nullable = false)
    private LocalDateTime timestamp;
}