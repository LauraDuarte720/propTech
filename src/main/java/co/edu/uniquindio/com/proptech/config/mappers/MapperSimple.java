package co.edu.uniquindio.com.proptech.config.mappers;

public interface MapperSimple<E, R> {
    R toOnlyDto(E entity);
}