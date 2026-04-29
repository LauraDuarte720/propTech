package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AlertAnormalType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "anormalalert")
@PrimaryKeyJoinColumn(name = "id")
public class AnormalAlert extends Alert {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertAnormalType alertAnormalType;
}