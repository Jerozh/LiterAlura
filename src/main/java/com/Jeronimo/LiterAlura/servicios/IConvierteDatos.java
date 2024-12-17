package com.Jeronimo.LiterAlura.servicios;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);

}

