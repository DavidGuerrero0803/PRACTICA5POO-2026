package uabc.david.practica5poo2026;

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
