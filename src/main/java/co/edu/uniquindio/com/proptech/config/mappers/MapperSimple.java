package co.edu.uniquindio.com.proptech.config.mappers;

public interface MapperSimple<E, S> {
    S toSimpleDto(E entity);
}
