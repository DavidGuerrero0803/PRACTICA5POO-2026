package uabc.david.practica5poo2026;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

public class Diccionario {
    private HashMap<String, Integer> palabras;
    private String rutaArchivo;
    private String idioma;

    public Diccionario(String idioma) {
        this.idioma = idioma;
        this.palabras = new HashMap<>();
    }

    public void cargarArchivo(String archivoTexto) {
        this.rutaArchivo = archivoTexto;
        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivoTexto));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String palabra = linea.trim().toLowerCase();
                if (!palabra.isEmpty()) {
                    palabras.put(palabra, palabra.length());
                }
            }
            lector.close();
        } catch (IOException e) {
            System.out.println("Se produjo un error al cargar el archivo " + archivoTexto);
        }
    }

    public boolean existeLaPalabra(String palabra) {
        return palabras.containsKey(palabra.toLowerCase());
    }

    public void agregarPalabra(String palabra) {
        String palabraAgregada = palabra.toLowerCase();
        palabras.put(palabraAgregada, palabraAgregada.length());
    }

    public String obtenerPalabraAleatoria(int longitud) {
        ArrayList<String> filtradas = palabras.entrySet()
                .stream()
                .filter(entrada -> entrada.getValue() == longitud)
                .map(HashMap.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));

        if (filtradas.isEmpty()) {
            return null;
        }

        Random aleatorio = new Random();
        return filtradas.get(aleatorio.nextInt(filtradas.size()));
    }

    public String getIdioma() {
        return idioma;
    }

}
