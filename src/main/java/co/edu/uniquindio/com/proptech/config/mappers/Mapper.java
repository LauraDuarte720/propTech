package co.edu.uniquindio.com.proptech.config.mappers;

public interface Mapper<E, C, U, R> {
    E toEntity(C createDto);
    R toDto(E entity);
    E toUpdate(U updateDto);
}