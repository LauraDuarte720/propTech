package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "geographiczone")
public class GeographicZone {
    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    private Zone zone;

    @Enumerated(EnumType.STRING)
    private City city;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Neighborhood neighborhood;
}
