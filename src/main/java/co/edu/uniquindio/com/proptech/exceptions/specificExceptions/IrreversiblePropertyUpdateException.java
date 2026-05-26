package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.domain.dtos.AffectedPropertyDto;
import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

import java.util.List;

/**
 * Se lanza cuando una actualización de inmueble incluye un cambio irreversible
 * (asesor o barrio/zona) y el cliente todavía no ha confirmado.
 * El front debe mostrar una confirmación y reintentar con confirm=true.
 */
public class IrreversiblePropertyUpdateException extends ConflictException {

    private final boolean neighborhoodChange;
    private final boolean agentChange;
    private final List<AffectedPropertyDto> affectedProperties;

    public IrreversiblePropertyUpdateException(String message,
                                               boolean neighborhoodChange,
                                               boolean agentChange,
                                               AffectedPropertyDto affected) {
        super(message);
        this.neighborhoodChange = neighborhoodChange;
        this.agentChange = agentChange;
        this.affectedProperties = affected == null ? List.of() : List.of(affected);
    }

    public boolean isNeighborhoodChange() {
        return neighborhoodChange;
    }

    public boolean isAgentChange() {
        return agentChange;
    }

    public List<AffectedPropertyDto> getAffectedProperties() {
        return affectedProperties;
    }
}