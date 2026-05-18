package uabc.david.practica5poo2026;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;

import static java.text.Normalizer.normalize;

public class ProcesadorRonda {
    private ArrayList<String> historialIntentos;
    private ArrayList<String> palabrasOrdenadas;
    private HashSet<String> letrasUsadas;
    private String dificultad;
    private String limiteAbajo;
    private String limiteArriba;
    private String palabraSecreta;
    private int intentosRestantes;
    private int longitudPalabra;
    private boolean pistaUtilizada;

    public ProcesadorRonda(String palabraSecreta, int intentosMaximos, String dificultad, ArrayList<String> palabrasOrdenadas) {
        this.palabraSecreta = palabraSecreta;
        this.intentosRestantes = intentosMaximos;
        this.dificultad = dificultad;
        this.longitudPalabra = palabraSecreta.length();
        this.letrasUsadas = new HashSet<>();
        this.historialIntentos = new ArrayList<>();
        this.pistaUtilizada = false;
        this.limiteAbajo = "a".repeat(longitudPalabra);
        this.limiteArriba = "z".repeat(longitudPalabra);
        this.palabrasOrdenadas = palabrasOrdenadas;
    }

    public String procesarIntento(String intento) {
        intentosRestantes--;

        for (int i = 0; i < intento.length(); i++) {
            letrasUsadas.add(String.valueOf(intento.charAt(i)));
        }

        String resultado;
        int comparacion = intento.compareTo(palabraSecreta);

        if (comparacion == 0) {
            resultado = "correcto";
        } else if (comparacion > 0) {
            resultado = "antes";
            limiteArriba = intento;
        } else {
            resultado = "despues";
            limiteAbajo = intento;
        }

        double proximidad = calcularProximidad(intento);
        historialIntentos.add(intento + "|" + resultado + "|" + proximidad);

        return resultado;
    }

    public double calcularProximidad(String intento) {
        double valorAbajo = palabraAValor(limiteAbajo);
        double valorArriba = palabraAValor(limiteArriba);
        double valorIntento = palabraAValor(intento);
        double valorSecreta = palabraAValor(palabraSecreta);

        double rango = valorArriba - valorAbajo;
        if (rango == 0) {
            return 100.0;
        }

        double distancia = Math.abs(valorIntento - valorSecreta);
        double proximidad = (1.0 - (distancia / rango)) * 100.0;

        if (proximidad < 0.0) {
            return 0.0;
        }
        if (proximidad > 100.0) {
            return 100.0;
        }

        return Math.round(proximidad * 10.0) / 10.0;
    }

    private double palabraAValor(String palabra) {
        String palabraNormalizada = normalize(palabra.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[^a-zñ]", "");
        double valor = 0;
        int longitud = palabraNormalizada.length();
        for (int i = 0; i < longitud; i++) {
            int codigoLetra = palabraNormalizada.charAt(i) - 'a' + 1;
            valor += (double) codigoLetra / Math.pow(30, i);
        }
        return valor;
    }

    public boolean tieneIntentos() {
        return intentosRestantes > 0;
    }

    public String pistaMoverArriba() {
        pistaUtilizada = true;
        limiteArriba = interpolarLimite(limiteArriba, palabraSecreta, 0.01);
        return limiteArriba;
    }

    public String pistaMoverAbajo() {
        pistaUtilizada = true;
        limiteAbajo = interpolarLimite(limiteAbajo, palabraSecreta, 0.01);
        return limiteAbajo;
    }

    public String pistaLetraInicial() {
        pistaUtilizada = true;
        return String.valueOf(palabraSecreta.charAt(0));
    }

    private String interpolarLimite(String desde, String hacia, double porcentaje) {
        StringBuilder resultado = new StringBuilder();
        int longitud = Math.min(desde.length(), hacia.length());

        for (int i = 0; i < longitud; i++) {
            int charDesde = (int) desde.charAt(i);
            int charHacia = (int) hacia.charAt(i);

            int avance = (int) ((charHacia - charDesde) * porcentaje);

            if (avance == 0 && charDesde != charHacia) {
                avance = (charHacia > charDesde) ? 1 : -1;
            }

            int charNuevo = charDesde + avance;
            charNuevo = Math.max('a', Math.min('z', charNuevo));
            resultado.append((char) charNuevo);
        }

        return resultado.toString();
    }

    public HashSet<String> getLetrasUsadas() {
        return letrasUsadas;
    }

    public ArrayList<String> getHistorial() {
        return historialIntentos;
    }

    public String getLimiteAbajo() {
        return limiteAbajo;
    }

    public String getLimiteArriba() {
        return limiteArriba;
    }

    public String getPalabraSecreta() {
        return palabraSecreta;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    public int getLongitudPalabra() {
        return longitudPalabra;
    }

    public boolean pistaUtilizada() {
        return pistaUtilizada;
    }


}
