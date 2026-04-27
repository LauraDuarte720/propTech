package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "geographiczone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeographicZone {
    @Id
    private int id;
    private Zone zone;
    @ManyToOne
    private Neighborhood neighborhood;
}
