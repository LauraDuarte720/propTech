package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AgentDtoReturn {

    private String cedula;

    private String name;

    private String username;

    private String contact;

    private GeographicZone assignedZone;

    private int closedDeals;

}
