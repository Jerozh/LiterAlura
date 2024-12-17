package com.Jeronimo.LiterAlura.repositorio;
import com.Jeronimo.LiterAlura.modelos.Autor;
import com.Jeronimo.LiterAlura.modelos.IdiomaEnum;
import com.Jeronimo.LiterAlura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAutorRepositorio extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Libro l JOIN l.autor a WHERE a.nombre LIKE %:nombre%")
    Optional<Autor> buscarAutorPorNombre(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE l.titulo LIKE %:titulo%")
    Optional<Libro> buscarLibroPorNombre(@Param("titulo") String titulo);

    @Query("SELECT l FROM Autor a JOIN a.libros l ORDER BY l.titulo")
    List<Libro> librosGuardados();

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :fecha AND a.fechaDeFallecimiento > :fecha")
    List<Autor> AutoresVivos(@Param("fecha") Integer fecha);

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.idiomaEnum = :idiomaEnum")
    List<Libro> librosPorIdioma(@Param("idiomaEnum") IdiomaEnum idiomaEnum);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento = :nacimiento")
    List<Autor> listarAutorPorFechaNacimiento(Integer nacimiento);

    @Query("SELECT a FROM Autor a Where a.fechaDeFallecimiento = :fallecimiento")
    List<Autor> listarAutorPorFechaFallecimiento(Integer fallecimiento);
}