package co.edu.uniquindio.com.proptech.exceptions.specificExceptions;

import co.edu.uniquindio.com.proptech.exceptions.generalExceptions.ConflictException;

public class VipVisitDisplacementException extends ConflictException {

    private final String displacedVisitId;

    public VipVisitDisplacementException(String agentId, String displacedVisitId) {
        super("VIP visit displaced an existing visit for agent: " + agentId +
                ". Visit " + displacedVisitId + " has been moved to PENDING_RESCHEDULE.");
        this.displacedVisitId = displacedVisitId;
    }

    public String getDisplacedVisitId() {
        return displacedVisitId;
    }
}