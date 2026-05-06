package co.edu.uniquindio.com.proptech.utils.validators;

import lombok.Getter;

@Getter
public class FieldErrorDetail {

    private String field;
    private String message;

    public FieldErrorDetail(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
