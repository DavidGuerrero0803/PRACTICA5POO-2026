package uabc.david.practica5poo2026;

import java.io.*;
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

    public String getIdioma() {
        return idioma;
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
        String palabraAgregada = palabra.toLowerCase().trim();
        palabras.put(palabraAgregada, palabraAgregada.length());

        if (this.rutaArchivo != null) {
            try {
                BufferedWriter escritor = new BufferedWriter(new FileWriter(this.rutaArchivo, true));
                escritor.newLine();
                escritor.write(palabraAgregada);
                escritor.close();
            } catch (IOException e) {
                System.out.println("Error al intentar escribir en el archivo: " + e.getMessage());
            }
        }
    }

    public ArrayList<String> obtenerPalabrasOrdenadas(int longitud) {
        return palabras.entrySet()
                .stream()
                .filter(entrada -> entrada.getValue() == longitud)
                .map(HashMap.Entry::getKey)
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
