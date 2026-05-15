package co.edu.uniquindio.com.proptech.mappers;

public interface MapperSimple<E, S> {
    S toSimpleDto(E entity);
}
