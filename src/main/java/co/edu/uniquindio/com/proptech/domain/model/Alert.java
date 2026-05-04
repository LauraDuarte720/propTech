package co.edu.uniquindio.com.proptech.domain.model;

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

    private LocalDateTime timestamp;

    private boolean reviewed;

    private Agent agent;

    private Client client;

    private Operation operation;

    private Property property;

    private Visit visit;
}