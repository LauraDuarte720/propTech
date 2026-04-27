package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.*;

@Entity
@Table(name = "userinteraction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInteraction {
    @Id
    private int id;
    private InteractionType interactionType;
    private LocalDateTime timestamp;
}