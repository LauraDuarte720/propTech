package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDtoReturn {

    private String cedula;

    private String name;

    private String username;

    private String email;

    private String phone;

    private double budget;

    private int minBedrooms;

    private ClientType clientType;

    private SearchStatus searchStatus;

    private PropertyType desiredPropertyType;
}