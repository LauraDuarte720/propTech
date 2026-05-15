package co.edu.uniquindio.com.proptech.mappers;

public interface MapperOnlyDto<E, R> {
    R toDto(E entity);
}