package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;
import org.yaml.snakeyaml.nodes.CollectionNode;

public class ZonesNotMatchingException extends ConflictException {
    public ZonesNotMatchingException(String message) {
        super(message);
    }
}
