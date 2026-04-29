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
public class Neighborhood {

    private int id;

    private String name;
    
    private Zone zone;
    
    private City city;
}
