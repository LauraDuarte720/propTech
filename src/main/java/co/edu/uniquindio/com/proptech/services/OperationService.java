package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.domain.model.UserInteraction;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.PurposeMismatchException;
import co.edu.uniquindio.com.proptech.mappers.impl.AgentMapper;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Operation;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.NotNullOperationTypeException;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.OperationDoesNotExist;
import co.edu.uniquindio.com.proptech.repositories.OperationRepository;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import co.edu.uniquindio.com.proptech.utils.CommissionCalculator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OperationService {

    private final AgentService agentService;
    private final ClientService clientService;
    OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository, AgentMapper agentMapper,
                            AgentService agentService, ClientService clientService) {
        this.operationRepository = operationRepository;
        this.agentService = agentService;
        this.clientService = clientService;
    }

    public Operation registerOperation(Operation operation) {
        validatePurposeMatchesOperation(operation.getOperationType(), operation.getProperty());
        operation.setId(CodeGenerator.generateOperationCode());
        operation.setProcessStatus(ProcessStatus.CREATED);
        operation.setValue(operation.getProperty().getPrice());
        operation.setCommission(
                CommissionCalculator.calculate(operation.getOperationType(), operation.getProperty().getPrice())
        );
        applyPropertyStatus(operation.getOperationType(), operation.getProperty());
        return operationRepository.save(operation);
    }

    public Operation updateOperation(Operation operation) {
        return operationRepository.findById(operation.getId())
                .map(existing -> applyUpdates(existing, operation))
                .orElseThrow(() -> new OperationDoesNotExist("id", operation.getId()));
    }

    private Operation applyUpdates(Operation existing, Operation incoming) {
        applyFieldUpdates(existing, incoming);
        Optional.ofNullable(incoming.getProcessStatus())
                .ifPresent(status -> applyStatusChange(existing, status));
        return operationRepository.update(existing);
    }

    private void applyFieldUpdates(Operation existing, Operation incoming) {
        Optional.ofNullable(incoming.getProperty()).ifPresent(existing::setProperty);
        Optional.ofNullable(incoming.getClient()).ifPresent(existing::setClient);
        Optional.ofNullable(incoming.getAgent()).ifPresent(existing::setAgent);
        Optional.ofNullable(incoming.getDateInitial()).ifPresent(existing::setDateInitial);
        Optional.ofNullable(incoming.getDateFinal()).ifPresent(existing::setDateFinal);
        Optional.ofNullable(incoming.getOperationType()).ifPresent(existing::setOperationType);
        Optional.ofNullable(incoming.getValue()).ifPresent(existing::setValue);
        Optional.ofNullable(incoming.getCommission()).ifPresent(existing::setCommission);
        validatePurposeMatchesOperation(existing.getOperationType(), existing.getProperty());
    }

    private void applyStatusChange(Operation existing, ProcessStatus newStatus) {
        if (newStatus.equals(ProcessStatus.CLOSED) && !existing.getProcessStatus().equals(ProcessStatus.CLOSED)) {
            handleClosed(existing);
        }
        if (newStatus.equals(ProcessStatus.CANCELLED)) {
            existing.getProperty().setStatus(PropertyStatus.ACTIVE);
        }
        existing.setProcessStatus(newStatus);
    }

    private void handleClosed(Operation existing) {
        existing.getAgent().setClosedDeals(existing.getAgent().getClosedDeals() + 1);
        applyPropertyStatus(existing.getOperationType(), existing.getProperty());
        registerNegotiatedInteractionIfApplicable(existing);
    }

    private void registerNegotiatedInteractionIfApplicable(Operation existing) {
        if (existing.getOperationType() == OperationType.RENT
                || existing.getOperationType() == OperationType.SALE) {
            UserInteraction interaction = UserInteraction.builder()
                    .client(existing.getClient())
                    .property(existing.getProperty())
                    .interactionType(InteractionType.NEGOTIATED)
                    .build();
            clientService.registerUserInteraction(interaction);
        }
    }

    private void applyPropertyStatus(OperationType type, Property property) {
        switch (type) {
            case RENT, CONTRACT_RENEWAL -> property.setStatus(PropertyStatus.RENTED);
            case SALE -> property.setStatus(PropertyStatus.SOLD);
            case DEAL_CANCELLATION -> property.setStatus(PropertyStatus.ACTIVE);
        }
    }

    private void validatePurposeMatchesOperation(OperationType operationType, Property property) {
        Purpose purpose = property.getPurpose();
        if (operationType == OperationType.RENT && purpose != Purpose.RENT) {
            throw new PurposeMismatchException("La propiedad no está disponible para arriendo");
        }
        if (operationType == OperationType.SALE && purpose != Purpose.SALE) {
            throw new PurposeMismatchException("La propiedad no está disponible para venta");
        }
    }

    public void deleteOperation(String operationId) {
        if (operationRepository.findById(operationId).isEmpty()) {
            throw new OperationDoesNotExist("id", operationId);
        }
        operationRepository.deleteById(operationId);
    }

    public LinkedList<Operation> getAllOperations() {
        return operationRepository.getOperations();
    }

    public Operation getOperationById(String id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new OperationDoesNotExist("id", id));
    }

    public LinkedList<Operation> getOperationsByType(OperationType type) {
        if (type == null) {
            throw new NotNullOperationTypeException("The type of the operation can not be null");
        }
        return operationRepository.getOperationsByType(type);
    }

    public LinkedList<Operation> getOperationsByAgent(String agentId) {
        Agent agent = agentService.getAgentByCedula(agentId);
        return operationRepository.getOperationsByAgent(agent);
    }

    public LinkedList<Operation> getOperationsByProperty(String propertyCode) {
        return operationRepository.getOperationsByProperty(propertyCode);
    }

    public Double getTotalCommissionsByAgent(String agentId) {
        Agent agent = agentService.getAgentByCedula(agentId);
        LinkedList<Operation> ops = operationRepository.getOperationsByAgent(agent);
        double total = 0;
        for (int i = 0; i < ops.size(); i++) {
            Operation op = ops.get(i);
            if (op.getProcessStatus() == ProcessStatus.CLOSED && op.getCommission() != null) {
                total += op.getCommission();
            }
        }
        return total;
    }
}