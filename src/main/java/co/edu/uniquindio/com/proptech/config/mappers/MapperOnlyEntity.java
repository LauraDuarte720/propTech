package co.edu.uniquindio.com.proptech.config.mappers;

public interface MapperOnlyEntity<E, C> {
    E toEntity(C entity);
}