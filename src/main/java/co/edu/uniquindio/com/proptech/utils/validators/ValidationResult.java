package co.edu.uniquindio.com.proptech.utils.validators;

import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import lombok.Getter;

@Getter
public class ValidationResult {

    private final ArrayList<FieldErrorDetail> errors = new ArrayList<>();

    public void add(String field, String message) {
        errors.add(new FieldErrorDetail(field, message));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

}
