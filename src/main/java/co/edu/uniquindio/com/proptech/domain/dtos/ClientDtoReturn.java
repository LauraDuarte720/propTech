package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.domain.model.UserInteraction;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;

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