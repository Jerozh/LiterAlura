package com.Jeronimo.LiterAlura.modelos;

public enum IdiomaEnum {
    ES("es"),
    EN("en"),
    FR("fr"),
    PT("pt"),
    DE("de");

    String idioma;

    private IdiomaEnum(String idioma) {

        this.idioma = idioma;
    }

    public String getIdioma() {

        return idioma;
    }

    public static IdiomaEnum fromString(String text) {
        for (IdiomaEnum idiomaEnum : IdiomaEnum.values()) {
            if (idiomaEnum.idioma.equalsIgnoreCase(text)) {
                return idiomaEnum;
            }
        }
        throw new IllegalArgumentException("No se ha encontrado nada: " + text);
    }
}