package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentDtoCreate {

    private String cedula;

    private String name;

    private String username;

    private String password;

    private String contact;

    private GeographicZone assignedZone;

    private int closedDeals;

}
