package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AlertType;
import co.edu.uniquindio.com.proptech.domain.enums.AttentionLevel;
import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
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
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Agent agent;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Client client;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Operation operation;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Property property;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Visit visit;
}