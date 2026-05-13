package uabc.david.practica5poo2026;

import java.util.HashMap;

public class Diccionario {
    private HashMap<String, String> palabras;
    private String idioma;

    public Diccionario(String idioma) {
        this.idioma = idioma;
        this.palabras = new HashMap<>();
    }

    public String getIdioma() {
        return idioma;
    }

    public int getPalabrasTotales() {
        return palabras.size();
    }
}
