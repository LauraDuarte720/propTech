package co.edu.uniquindio.com.proptech.config.mappers;

public interface MapperSimple<E, R> {
    R toDto(E entity);
}