package co.edu.uniquindio.com.proptech.domain.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String cedula;
    private String name;
    private String username;
    private String role; // "ADMIN", "AGENT", "CLIENT"
}