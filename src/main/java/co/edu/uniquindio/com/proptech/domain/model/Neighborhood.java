package co.edu.uniquindio.com.proptech.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "neighborhood")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Neighborhood {
    @Id
    private int id;
    private String name;
    private GeographicZone geographicZone;
}
