package uabc.david.practica5poo2026;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Esta clase representa el diccionario de palabras del juego Betweenle.
 * Carga, almacena y consulta palabras desde un archivo de texto.
 */
public class Diccionario {
    private HashMap<String, Integer> palabras;
    private String rutaArchivo;
    private String idioma;

    /**
     * Crea un diccionario vacío para el idioma indicado.
     * @param idioma El idioma del diccionario ("español" o "inglés").
     */
    public Diccionario(String idioma) {
        this.idioma = idioma;
        this.palabras = new HashMap<>();
    }

    /**
     * Regresa el idioma asociado a un diccionario.
     * @return El idioma del diccionario.
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * Lee un archivo de texto línea por línea para cargar las palabras.
     * Almacena la palabra como clave y su longitud como valor, usando el HashMap.
     * @param archivoTexto Ruta del archivo a cargar.
     */
    public void cargarArchivo(String archivoTexto) {
        this.rutaArchivo = archivoTexto;
        try {
            // El FileReader abre el canal hacia el archivo en el almacenamiento.
            BufferedReader lector = new BufferedReader(new FileReader(archivoTexto));
            String linea;
            // El bucle se ejecuta de manera secuencial leyendo línea por línea el archivo .txt.
            while ((linea = lector.readLine()) != null) {

                // Se usa .trim() para eliminar caracteres invisibles.
                // toLowerCase() se encarga de convertir las palabras a minúsculas.
                String palabra = linea.trim().toLowerCase();
                // Si la línea está vacía, se ignora.
                if (!palabra.isEmpty()) {
                    // En caso de haber información, se agrega la palabra (clave) y su longitud (valor).
                    palabras.put(palabra, palabra.length());
                }
            }
            // Se cierra el lector para evitar fugas en la memoria.
            lector.close();
        } catch (IOException e) {
            // Captura el error en caso de que el archivo no exista.
            System.out.println("Se produjo un error al cargar el archivo " + archivoTexto);
        }
    }

    /**
     * Verifica si una palabra existe en el diccionario.
     * @param palabra La palabra a buscar.
     * @return true si la palabra existe, false en caso contrario.
     */
    public boolean existeLaPalabra(String palabra) {
        return palabras.containsKey(palabra.toLowerCase());
    }

    /**
     * Agrega una nueva palabra al diccionario.
     * Esto permite que el jugador agregue palabras que considere válidas.
     * @param palabra La palabra a agregar.
     */
    public void agregarPalabra(String palabra) {
        // Hace que la palabra ingresada se haga minúscula y elimina espacios vacíos.
        String palabraAgregada = palabra.toLowerCase().trim();
        palabras.put(palabraAgregada, palabraAgregada.length());

        // Si se conoce la ruta del archivo, se escribe la nueva palabra al final del mismo.
        if (this.rutaArchivo != null) {
            try {
                // El "true" dentro de FileWriter indica modo "append" (evita sobreescribir líneas).
                BufferedWriter escritor = new BufferedWriter(new FileWriter(this.rutaArchivo, true));
                escritor.newLine();
                escritor.write(palabraAgregada);
                escritor.close();
            } catch (IOException e) {
                System.out.println("Error al intentar escribir en el archivo: " + e.getMessage());
            }
        }
    }

    /**
     * Filtra las palabras por una longitud específica, las ordena
     * alfabéticamente y las devuelve en una ArrayList.
     * @param longitud Cantidad de letras requerida en las palabras.
     * @return ArrayList de palabras ordenadas que cumplen con la longitud.
     */
    public ArrayList<String> obtenerPalabrasOrdenadas(int longitud) {
        // El HashMap de palabras pasa a ser un flujo continuo de parejas clave-valor.
        return palabras.entrySet()
                .stream()
                // Si coincide con el tamaño de la longitud, la palabra pasa;
                .filter(entrada -> entrada.getValue() == longitud)
                // Con las palabras con longitud adquiridas, el map extrae únicamente las claves.
                .map(HashMap.Entry::getKey)
                // sorted() ordena el alfabeto de la A a la Z.
                .sorted()
                // Se toman todas las palabras filtradas y se almacenan en un nuevo ArrayList.
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
