package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.OperationDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.OperationDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.OperationDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Operation;
import org.springframework.stereotype.Component;

@Component
public class OperationMapper implements MapperCrud<Operation, OperationDtoCreate, OperationDtoUpdate, OperationDtoReturn> {

    @Override
    public Operation toEntity(OperationDtoCreate dto) {
        return Operation.builder()
                .property(null)
                .client(null)
                .agent(null)
                .dateInitial(dto.getDateInitial())
                .dateFinal(dto.getDateFinal())
                .operationType(dto.getOperationType())
                .value(dto.getValue())
                .commission(dto.getCommission())
                .processStatus(dto.getProcessStatus())
                .build();
    }

    @Override
    public OperationDtoReturn toDto(Operation entity) {
        return OperationDtoReturn.builder()
                .id(entity.getId())
                .property(null)
                .client(null)
                .agent(null)
                .dateInitial(entity.getDateInitial())
                .dateFinal(entity.getDateFinal())
                .operationType(entity.getOperationType())
                .value(entity.getValue())
                .commission(entity.getCommission())
                .processStatus(entity.getProcessStatus())
                .build();
    }

    @Override
    public Operation toUpdate(OperationDtoUpdate dto) {
        return Operation.builder()
                .id(dto.getId())
                .property(null)
                .client(null)
                .agent(null)
                .dateInitial(dto.getDateInitial())
                .dateFinal(dto.getDateFinal())
                .operationType(dto.getOperationType())
                .value(dto.getValue())
                .commission(dto.getCommission())
                .processStatus(dto.getProcessStatus())
                .build();
    }
}