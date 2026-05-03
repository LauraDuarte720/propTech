package co.edu.uniquindio.com.proptech.config.mappers;

public interface MapperCrud<E, C, U, R> {
    E toEntity(C createDto);
    R toDto(E entity);
    E toUpdate(U updateDto);
}