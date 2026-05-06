package co.edu.uniquindio.com.proptech.utils.validators;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.utils.validators.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class LocationValidator {

    public ValidationResult validate(GeographicZone agentZone, Neighborhood propertyNeighborhood) {

        ValidationResult result = new ValidationResult();

        if (propertyNeighborhood == null) {
            result.add("neighborhood", "La propiedad no tiene ubicación definida");
            return result;
        }

        if (agentZone == null) {
            return result;
        }

        if (!agentZone.getCity().equals(propertyNeighborhood.getCity())) {
            result.add(
                    "city",
                    "La ciudad del agente (" + agentZone.getCity() +
                            ") no coincide con la de la propiedad (" + propertyNeighborhood.getCity() + ")"
            );
        }

        if (agentZone.getZone() != null &&
                !agentZone.getZone().equals(propertyNeighborhood.getZone())) {

            result.add(
                    "zone",
                    "La zona del agente (" + agentZone.getZone() +
                            ") no coincide con la de la propiedad (" + propertyNeighborhood.getZone() + ")"
            );
        }

        if (agentZone.getNeighborhood() != null &&
                !agentZone.getNeighborhood().equals(propertyNeighborhood.getName())) {

            result.add(
                    "neighborhood",
                    "El barrio del agente (" + agentZone.getNeighborhood() +
                            ") no coincide con el de la propiedad (" + propertyNeighborhood.getName() + ")"
            );
        }

        return result;
    }
}