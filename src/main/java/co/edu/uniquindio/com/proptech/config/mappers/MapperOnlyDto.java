package co.edu.uniquindio.com.proptech.config.mappers;

public interface MapperOnlyDto<E, R> {
    R toDto(E entity);
}