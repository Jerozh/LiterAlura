package com.Jeronimo.LiterAlura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DatosDTO(
            @JsonAlias("count") Integer total,
            @JsonAlias("results") List<DatosLibrosDTO> libros) {
    }

