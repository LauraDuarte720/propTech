package co.edu.uniquindio.com.proptech.domain.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {
    
    private String cedula;
    
    private String name;

    private String username;

    private String password;

}