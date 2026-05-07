package co.edu.uniquindio.com.proptech.config.mappers;

public interface MapperSimple<S, E> {
    S toSimpleDto(E entity);
}
