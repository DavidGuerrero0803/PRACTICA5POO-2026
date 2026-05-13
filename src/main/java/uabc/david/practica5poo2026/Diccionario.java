package uabc.david.practica5poo2026;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public void cargarDesdeArchivo(String rutaArchivo) {
        try {
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String palabra = linea.trim().toLowerCase();
                if (!palabra.isEmpty()) {
                    palabras.put(palabra, "");
                }
            }
            lector.close();
        } catch (IOException e) {
            System.out.println("Se produjo un error al cargar el archivo " + rutaArchivo);
        }
    }

    public void agregarPalabra(String palabra, String definicion) {
        palabras.put(palabra.toLowerCase(), definicion);
    }
}
