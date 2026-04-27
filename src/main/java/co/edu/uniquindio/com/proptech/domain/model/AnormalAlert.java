package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AlertAnormalType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "anormalalert")
public class AnormalAlert extends Alert {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertAnormalType alertAnormalType;
}