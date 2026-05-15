package co.edu.uniquindio.com.proptech.mappers;

public interface MapperOnlyEntity<E, C> {
    E toEntity(C createDto);
}