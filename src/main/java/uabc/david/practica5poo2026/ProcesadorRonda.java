package uabc.david.practica5poo2026;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
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

    public HashSet<String> getLetrasUsadas() {
        return letrasUsadas;
    }

    public ArrayList<String> getHistorial() {
        return historialIntentos;
    }

    public String getLimiteArriba() {
        return limiteArriba;
    }

    public double getProximidadLimiteArriba() {
        return calcularProximidadLimite(limiteArriba);
    }

    public String getLimiteAbajo() {
        return limiteAbajo;
    }

    public double getProximidadLimiteAbajo() {
        return calcularProximidadLimite(limiteAbajo);
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

    public String procesarIntento(String intento) {
        intentosRestantes--;

        for (int i = 0; i < intento.length(); i++) {
            letrasUsadas.add(String.valueOf(intento.charAt(i)));
        }

        String resultado;
        int intentoComparado = intento.compareTo(palabraSecreta);

        if (intentoComparado == 0) {
            resultado = "correcto";
        } else if (intentoComparado > 0) {
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
        return calcularProximidadLimite(intento);
    }

    public double calcularProximidadLimite(String limite) {
        int totalPalabras = palabrasOrdenadas.size();
        if (totalPalabras <= 1) {
            return 0.01;
        }

        // Se buscan las posiciones alfabéticas reales en el diccionario.
        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limite.toLowerCase());

        if (indiceSecreto < 0) {
            indiceSecreto = -(indiceSecreto + 1);
        }
        if (indiceLimite < 0) {
            indiceLimite = -(indiceLimite + 1);
        }

        // Distancia en cantidad de palabras reales,
        double distanciaIndices = Math.abs(indiceLimite - indiceSecreto);
        double distanciaPorcentaje = (distanciaIndices / totalPalabras) * 100.0;

         if (distanciaPorcentaje < 0.001) {
            return 0.001;
        }
        if (distanciaPorcentaje > 100.0) {
            return 100.0;
        }

        return Math.round(distanciaPorcentaje * 100.0) / 100.0;
    }

    public boolean estaEnRango(String intento) {
        return intento.compareTo(limiteAbajo) > 0 && intento.compareTo(limiteArriba) < 0;
    }

    public boolean tieneIntentos() {
        return intentosRestantes > 0;
    }

    public boolean pistaUtilizada() {
        return pistaUtilizada;
    }

    public String pistaMoverArriba() {
        this.pistaUtilizada = true;

        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limiteAbajo.toLowerCase());

        if (indiceSecreto < 0) {
            indiceSecreto = -(indiceSecreto + 1);
        }
        if (indiceLimite < 0) {
            indiceLimite = -(indiceLimite + 1);
        }

        int distancia = indiceSecreto - indiceLimite;

        int pasos = Math.max(1, (int)(palabrasOrdenadas.size() * 0.01));

        if (pasos >= distancia) {
            pasos = Math.max(1, distancia - 1);
        }

        int nuevoIndice = indiceLimite + pasos;
        nuevoIndice = Math.max(0, Math.min(indiceSecreto, nuevoIndice));

        this.limiteAbajo = palabrasOrdenadas.get(nuevoIndice);
        return this.limiteAbajo;
    }

    public String pistaMoverAbajo() {
        this.pistaUtilizada = true;

        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limiteArriba.toLowerCase());

        if (indiceSecreto < 0) {
            indiceSecreto = -(indiceSecreto + 1);
        }
        if (indiceLimite < 0) {
            indiceLimite = -(indiceLimite + 1);
        }

        int distancia = indiceLimite - indiceSecreto;

        int pasos = Math.max(1, (int)(palabrasOrdenadas.size() * 0.01));

        if (pasos >= distancia) {
            pasos = Math.max(1, distancia - 1);
        }

        int nuevoIndice = indiceLimite - pasos;
        nuevoIndice = Math.max(indiceSecreto, Math.min(palabrasOrdenadas.size() - 1, nuevoIndice));

        this.limiteArriba = palabrasOrdenadas.get(nuevoIndice);
        return this.limiteArriba;
    }

    public String pistaLetraInicial() {
        pistaUtilizada = true;
        return String.valueOf(palabraSecreta.charAt(0));
    }

}
