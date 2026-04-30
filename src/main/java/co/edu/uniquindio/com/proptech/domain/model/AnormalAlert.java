package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AlertAnormalType;
import co.edu.uniquindio.com.proptech.domain.enums.AttentionLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AnormalAlert extends Alert {

    private AlertAnormalType alertAnormalType;

    private AttentionLevel attentionLevel;
}