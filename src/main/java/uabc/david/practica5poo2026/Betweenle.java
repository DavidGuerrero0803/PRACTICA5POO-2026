package uabc.david.practica5poo2026;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Betweenle {
    private Diccionario diccionario;
    private ProcesadorRonda estadoActual;
    private int letrasDificil;
    private boolean partidaActiva;

    public Betweenle(int letrasDificil) {
        this.letrasDificil = letrasDificil;
        this.diccionario = null;
        this.estadoActual = null;
        this.partidaActiva = false;
    }

    public ProcesadorRonda getEstadoActual() {
        return estadoActual;
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

    public int getIntentosDificultad(String dificultad) {
        if (dificultad.equals("facil")) {
            return 10;
        }
        if (dificultad.equals("intermedio")) {
            return 12;
        }
        return 14;
    }

    public ArrayList<String> getHistorial() {
        return estadoActual.getHistorialIntentos()
                .stream()
                .map(entrada -> {
                    String[] historialPalabras = entrada.split("\\|");
                    String palabra = historialPalabras[0];
                    return palabra.toUpperCase();
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public String getEstado() {
        boolean sinIntentos = estadoActual.getHistorialIntentos().isEmpty();

        String aproxSuperior = sinIntentos ? "?" : String.valueOf(estadoActual.getProximidadLimiteAbajo());
        String aproxInferior = sinIntentos ? "?" : String.valueOf(estadoActual.getProximidadLimiteArriba());

        String palabraOculta = (" -".repeat(estadoActual.getLongitudPalabra())).trim();

        String etiquetaSuperior = String.format("%-2s", aproxSuperior);
        String etiquetaInferior = String.format("%-2s", aproxInferior);

        String limiteSuperior = estadoActual.getLimiteAbajo()
                .toUpperCase().chars()
                .collect(StringBuilder::new,
                        (sb, c) -> sb.append((char) c).append(' '),
                        StringBuilder::append)
                .toString().trim();

        String limiteInferior = estadoActual.getLimiteArriba()
                .toUpperCase().chars()
                .collect(StringBuilder::new,
                        (sb, c) -> sb.append((char) c).append(' '),
                        StringBuilder::append)
                .toString().trim();

        int anchoFila = etiquetaSuperior.length() + 8 + limiteSuperior.length() + 2;
        int margen = (anchoFila - palabraOculta.length()) / 2;
        String espaciado = " ".repeat(Math.max(0, margen));

        return  etiquetaSuperior + "  [ " + limiteSuperior
                + " ]\n"
                + espaciado + palabraOculta + "\n"
                + etiquetaInferior + "  [ " + limiteInferior + " ]\n"
                + "Intentos restantes: " + estadoActual.getIntentosRestantes();
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

        estadoActual = new ProcesadorRonda(palabraSecreta, intentos, dificultad, palabrasOrdenadas);
        partidaActiva = true;
        return true;
    }

    public String procesarIntento(String intento) {
        String palabraIngresada = intento.trim().toLowerCase();

        if (!estadoActual.tieneIntentos()) {
            return "sin intentos";
        }

        if (palabraIngresada.length() != estadoActual.getLongitudPalabra()) {
            return "longitud";
        }

        if (!diccionario.existeLaPalabra(palabraIngresada)) {
            return "no encontrada";
        }

        // La palabra debe estar dentro del rango actual.
        if (!estadoActual.estaEnRango(palabraIngresada)) {
            return "fuera de rango";
        }

        String resultado = estadoActual.procesarIntento(palabraIngresada);

        if (resultado.equals("correcto") || !estadoActual.tieneIntentos()) {
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
        if (estadoActual.pistaUtilizada()) {
            return "pista utilizada";
        }

        if (opcionPista == 1 || opcionPista == 2) {
            if (estadoActual.getHistorialIntentos().isEmpty()) {
                return "requiere intento";
            }
        }

        if (opcionPista == 1) {
            String nuevoLimite = estadoActual.pistaMoverArriba();
            return "El límite superior ahora es: " + nuevoLimite.toUpperCase();
        }

        if (opcionPista == 2) {
            String nuevoLimite = estadoActual.pistaMoverAbajo();
            return "El límite inferior ahora es: " + nuevoLimite.toUpperCase();
        }

        if (opcionPista == 3) {
            String letra = estadoActual.pistaLetraInicial();
            return "La palabra empieza con la letra: " + letra.toUpperCase();
        }

        return "opción inválida";
    }

}
