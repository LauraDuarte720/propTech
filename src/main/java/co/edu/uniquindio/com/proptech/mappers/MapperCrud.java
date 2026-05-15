package co.edu.uniquindio.com.proptech.mappers;

public interface MapperCrud<E, C, U, R> extends MapperCreate<E, C, R> {
    E toUpdate(U updateDto);
}