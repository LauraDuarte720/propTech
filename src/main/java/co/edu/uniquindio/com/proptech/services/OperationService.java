package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.mappers.impl.AgentMapper;
import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Operation;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.NotNullOperationTypeException;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.OperationDoesNotExist;
import co.edu.uniquindio.com.proptech.repositories.OperationRepository;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OperationService {


    private final AgentService agentService;
    OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository, AgentMapper agentMapper, AgentService agentService) {
        this.operationRepository = operationRepository;
        this.agentService = agentService;
    }

    public Operation registerOperation(Operation operation) {
        if (operation.getProcessStatus().equals(ProcessStatus.CLOSED)) {
            Agent agent = operation.getAgent();
            agent.setClosedDeals(agent.getClosedDeals() + 1);
            agentService.updateAgent(agent, false);
        }
        operation.setId(CodeGenerator.generateOperationCode());
        return operationRepository.save(operation);
    }

    public Operation updateOperation(Operation operation) {
        return operationRepository.findById(operation.getId()).map(existing -> {
            Optional.ofNullable(operation.getProperty()).ifPresent(existing::setProperty);
            Optional.ofNullable(operation.getClient()).ifPresent(existing::setClient);
            Optional.ofNullable(operation.getAgent()).ifPresent(existing::setAgent);
            Optional.ofNullable(operation.getDateInitial()).ifPresent(existing::setDateInitial);
            Optional.ofNullable(operation.getDateFinal()).ifPresent(existing::setDateFinal);
            Optional.ofNullable(operation.getOperationType()).ifPresent(existing::setOperationType);
            Optional.ofNullable(operation.getValue()).ifPresent(existing::setValue);
            Optional.ofNullable(operation.getCommission()).ifPresent(existing::setCommission);
            Optional.ofNullable(operation.getProcessStatus()).ifPresent(existing::setProcessStatus);
            return operationRepository.update(existing);
        }).orElseThrow(() -> new OperationDoesNotExist("id", operation.getId()));
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
}