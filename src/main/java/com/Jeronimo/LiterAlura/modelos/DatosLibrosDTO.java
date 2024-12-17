package com.Jeronimo.LiterAlura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
    public record DatosLibrosDTO(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutorDTO> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Double numeroDeDescargas) {
}