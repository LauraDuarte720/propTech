package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AlertType;
import co.edu.uniquindio.com.proptech.domain.enums.AttentionLevel;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Alert {

    private String id;

    private AlertType alertType;

    private boolean anormal;

    private LocalDateTime timestamp;

    private boolean reviewed;

    private Agent agent;

    private Client client;

    private Operation operation;

    private Property property;

    private Visit visit;
}