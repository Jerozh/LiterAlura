package com.Jeronimo.LiterAlura.modelos;

import jakarta.persistence.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private IdiomaEnum idiomaEnum;
    private Double numeroDeDescargas;
    @ManyToOne()
    private Autor autor;


    public Libro() {}

    public Libro(DatosLibrosDTO datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idiomaEnum = IdiomaEnum.fromString(datosLibro.idiomas().stream()
                .limit(1).collect(Collectors.joining()));
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public IdiomaEnum getIdioma() {
        return idiomaEnum;
    }

    public void setIdioma(IdiomaEnum idiomaEnum) {
        this.idiomaEnum = idiomaEnum;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "--------------- LIBRO ------------------" +
                "\nTítulo: " + titulo +
                "\nAutor: " + getAutor().getNombre() +
                "\nIdiomaEnum: " + idiomaEnum +
                "\nNúmero de descargas: " + numeroDeDescargas +
                "\n----------------------------------------\n";
    }
}