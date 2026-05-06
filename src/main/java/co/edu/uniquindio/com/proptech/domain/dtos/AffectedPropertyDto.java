package co.edu.uniquindio.com.proptech.domain.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AffectedPropertyDto {

    private String code;
    private String address;
}