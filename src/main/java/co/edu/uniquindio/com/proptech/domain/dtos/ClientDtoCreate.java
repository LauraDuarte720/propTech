package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDtoCreate {

    private String cedula;

    private String name;

    private String username;

    private String password;

    private String email;

    private String phone;

    private double budget;

    private int minBedrooms;

    private ClientType clientType;

    private SearchStatus searchStatus;

    private PropertyType desiredPropertyType;

}
