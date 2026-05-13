package uabc.david.practica5poo2026;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Betweenle {
    private Diccionario diccionario;
    private Estado estadoActual;
    private int letrasDificil;
    private boolean partidaActiva;

    public Betweenle(int letrasDificil) {
        this.letrasDificil = letrasDificil;
        this.diccionario = null;
        this.estadoActual = null;
        this.partidaActiva = false;
    }

    public Diccionario getDiccionario() {
        return diccionario;
    }

    public Estado getEstadoActual() {
        return estadoActual;
    }

    public int getLetrasDificil() {
        return letrasDificil;
    }

    public boolean partidaActida() {
        return partidaActiva;
    }

    public boolean iniciarPartida(String idioma, String dificultad) {
        diccionario = new Diccionario(idioma);

        String rutaArchivo = idioma.equals("español") ? "espanol.txt" : "ingles.txt";
        diccionario.cargarDesdeArchivo(rutaArchivo);

        int longitud = obtenerLongitudPorDificultad(dificultad);
        int intentos = obtenerIntentosPorDificultad(dificultad);

        String palabraSecreta = diccionario.obtenerPalabraAleatoria(longitud);
        if (palabraSecreta == null) {
            return false;
        }

        estadoActual = new Estado(palabraSecreta, intentos, dificultad);
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

        String resultado = estadoActual.procesarIntento(palabraIngresada);

        if (resultado.equals("correcto") || !estadoActual.tieneIntentos()) {
            partidaActiva = false;
        }

        return resultado;
    }

    public boolean verificarPalabra(String palabra) {
        return diccionario.existeLaPalabra(palabra);
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

        if (opcionPista == 1) {
            String nuevoLimite = estadoActual.pistaMoverArriba();
            return "El límite superior ahora es: " + nuevoLimite;
        }

        if (opcionPista == 2) {
            String nuevoLimite = estadoActual.pistaMoverAbajo();
            return "El límite superior ahora es: " + nuevoLimite;
        }

        if (opcionPista == 3) {
            String letra = estadoActual.pistaLetraInicial();
            return "La palabra empieza con la letra: " + letra.toUpperCase();
        }

        return "opción inválida";
    }

    public ArrayList<String> obtenerHistorial() {
        return estadoActual.getHistorial()
                .stream()
                .map(entrada -> {
                    String[] partes = entrada.split("\\|");
                    String palabra = partes[0];
                    String resultado = partes[1];
                    String proximidad = partes[2];
                    return palabra + " - " + resultado.toUpperCase() + " (" +
                            proximidad + "% de proximidad)";
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int obtenerLongitudPorDificultad(String dificultad) {
        if (dificultad.equals("facil")) {
            return 5;
        }
        if (dificultad.equals("intermedio")) {
            return 6;
        }
        return letrasDificil;
    }

    public int obtenerIntentosPorDificultad(String dificultad) {
        if (dificultad.equals("facil")) {
            return 10;
        }
        if (dificultad.equals("intermedio")) {
            return 12;
        }
        return 14;
    }

}
