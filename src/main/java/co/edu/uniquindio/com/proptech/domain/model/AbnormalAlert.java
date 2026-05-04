package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AlertAbnormalType;
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
public class AbnormalAlert extends Alert {

    private AlertAbnormalType alertAbnormalType;

    private AttentionLevel attentionLevel;
}