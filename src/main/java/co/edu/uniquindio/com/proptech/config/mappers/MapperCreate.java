package co.edu.uniquindio.com.proptech.config.mappers;

public interface MapperCreate<E, C, R> {
    E toEntity(C createDto);
    R toDto(E entity);
}