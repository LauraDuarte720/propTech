package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AlertAnormalType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "anomalalert")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnomalAlert extends Alert {
    private AlertAnormalType alertAnormalType;
}