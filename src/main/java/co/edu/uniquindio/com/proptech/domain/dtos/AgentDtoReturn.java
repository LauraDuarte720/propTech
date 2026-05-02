package co.edu.uniquindio.com.proptech.domain.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentDtoReturn {

    private String cedula;

    private String name;

    private String username;

    private String contact;

    private GeographicZoneDtoReturn assignedZone;

    private int closedDeals;
}