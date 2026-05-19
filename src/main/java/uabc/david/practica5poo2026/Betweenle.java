package uabc.david.practica5poo2026;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Betweenle {
    private Diccionario diccionario;
    private ProcesadorRonda rondaActual;
    private int letrasDificil;
    private boolean partidaActiva;

    public Betweenle(int letrasDificil) {
        this.letrasDificil = letrasDificil;
        this.diccionario = null;
        this.rondaActual = null;
        this.partidaActiva = false;
    }

    public ProcesadorRonda getRondaActual() {
        return rondaActual;
    }

    public int getLongitudDificultad(String dificultad) {
        if (dificultad.equals("facil")) {
            return 5;
        }
        if (dificultad.equals("intermedio")) {
            return 6;
        }
        return letrasDificil;
    }

    public ArrayList<String> getHistorial() {
        return rondaActual.getHistorialIntentos()
                .stream()
                .map(entrada -> {
                    String[] historialPalabras = entrada.split("\\|");
                    String palabra = historialPalabras[0];
                    return palabra.toUpperCase();
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getEstado() {
        boolean sinIntentos = rondaActual.getHistorialIntentos().isEmpty();

        String etiquetaSuperior = sinIntentos ? "?" : String.valueOf(rondaActual.getProximidadSuperior());
        String etiquetaInferior = sinIntentos ? "?" : String.valueOf(rondaActual.getProximidadInferior());

        String aproxSuperior = String.format("%-2s", etiquetaSuperior);
        String aproxInferior = String.format("%-2s", etiquetaInferior);

        return  aproxSuperior + "  [ " + rondaActual.getLimiteSuperior().toUpperCase()
                + " ]\n" + "\n"
                + aproxInferior + "  [ " + rondaActual.getLimiteInferior().toUpperCase() + " ]\n"
                + "Intentos restantes: " + rondaActual.getIntentosRestantes();
    }

    public boolean iniciarPartida(String idioma, String dificultad, int intentos) {

        if (this.diccionario == null || !this.diccionario.getIdioma().equals(idioma)) {
            this.diccionario = new Diccionario(idioma);
            String rutaArchivo = idioma.equals("español") ?
                    "src/main/java/uabc/david/practica5poo2026/espanol.txt" :
                    "src/main/java/uabc/david/practica5poo2026/ingles.txt";
            this.diccionario.cargarArchivo(rutaArchivo);
        }

        int longitud = getLongitudDificultad(dificultad);

        ArrayList<String> palabrasOrdenadas = diccionario.obtenerPalabrasOrdenadas(longitud);
        if (palabrasOrdenadas.isEmpty()) {
            return false;
        }

        Random valorAleatorio = new Random();
        String palabraSecreta = palabrasOrdenadas.get(valorAleatorio.nextInt(palabrasOrdenadas.size()));

        rondaActual = new ProcesadorRonda(palabraSecreta, intentos, dificultad, palabrasOrdenadas);
        partidaActiva = true;
        return true;
    }

    public String procesarIntento(String intento) {
        String palabraIngresada = intento.trim().toLowerCase();

        if (!rondaActual.tieneIntentos()) {
            return "sin intentos";
        }

        if (palabraIngresada.length() != rondaActual.getLongitudPalabra()) {
            return "longitud";
        }

        if (!diccionario.existeLaPalabra(palabraIngresada)) {
            return "no encontrada";
        }

        // La palabra debe estar dentro del rango actual.
        if (!rondaActual.estaEnRango(palabraIngresada)) {
            return "fuera de rango";
        }

        String resultado = rondaActual.procesarIntento(palabraIngresada);

        if (resultado.equals("correcto") || !rondaActual.tieneIntentos()) {
            partidaActiva = false;
        }

        return resultado;
    }

    public void agregarPalabraAlDiccionario(String palabra) {
        diccionario.agregarPalabra(palabra);
    }

    public boolean juegoActivo() {
        return partidaActiva;
    }

    public String pedirPista(int opcionPista) {
        if (rondaActual.pistaUtilizada()) {
            return "pista utilizada";
        }

        if (opcionPista == 1 || opcionPista == 2) {
            if (rondaActual.getHistorialIntentos().isEmpty()) {
                return "requiere intento";
            }
        }

        if (opcionPista == 1) {
            String nuevoLimite = rondaActual.recorrerPalabraArriba();
            return "El límite superior ahora es: " + nuevoLimite.toUpperCase();
        }

        if (opcionPista == 2) {
            String nuevoLimite = rondaActual.recorrerPalabraAbajo();
            return "El límite inferior ahora es: " + nuevoLimite.toUpperCase();
        }

        if (opcionPista == 3) {
            String letra = rondaActual.pistaLetraInicial();
            return "La palabra empieza con la letra: " + letra.toUpperCase();
        }

        return "opción inválida";
    }

}
