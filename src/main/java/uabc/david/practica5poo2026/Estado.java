package uabc.david.practica5poo2026;

import java.util.ArrayList;
import java.util.HashSet;

public class Estado {
    private HashSet<String> letrasUsadas;
    private ArrayList<String> historialIntentos;
    private String dificultad;
    private String limiteAbajo;
    private String limiteArriba;
    private String palabraSecreta;
    private int intentosRestantes;
    private int longitudPalabra;
    private boolean pistaUtilizada;

    public Estado(String palabraSecreta, int intentosMaximos, String dificultad) {
        this.palabraSecreta = palabraSecreta;
        this.intentosRestantes = intentosMaximos;
        this.dificultad = dificultad;
        this.longitudPalabra = palabraSecreta.length();
        this.letrasUsadas = new HashSet<>();
        this.historialIntentos = new ArrayList<>();
        this.pistaUtilizada = false;
        this.limiteAbajo = "a".repeat(longitudPalabra);
        this.limiteArriba = "z".repeat(longitudPalabra);
    }

    public String procesarIntento(String intento) {
        return null;
    }

    public double calcularProximidad(String intento) {
        return 0.0;
    }

    public String pistaMoverArriba() {
        return null;
    }

    public String pistaMoverAbajo() {
        return null;
    }

    public String pistaLetraInicial() {
        return null;
    }

    public boolean tieneIntentos() {
        return false;
    }

    public HashSet<String> getLetrasUsadas() {
        return letrasUsadas;
    }

    public ArrayList<String> getHistorialIntentos() {
        return historialIntentos;
    }

    public String getDificultad() {
        return dificultad;
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

    public void setLetrasUsadas(HashSet<String> letrasUsadas) {
        this.letrasUsadas = letrasUsadas;
    }

    public void setHistorialIntentos(ArrayList<String> historialIntentos) {
        this.historialIntentos = historialIntentos;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public void setLimiteAbajo(String limiteAbajo) {
        this.limiteAbajo = limiteAbajo;
    }

    public void setLimiteArriba(String limiteArriba) {
        this.limiteArriba = limiteArriba;
    }

    public void setPalabraSecreta(String palabraSecreta) {
        this.palabraSecreta = palabraSecreta;
    }

    public void setIntentosRestantes(int intentosRestantes) {
        this.intentosRestantes = intentosRestantes;
    }

    public void setLongitudPalabra(int longitudPalabra) {
        this.longitudPalabra = longitudPalabra;
    }

    public void setPistaUtilizada(boolean pistaUtilizada) {
        this.pistaUtilizada = pistaUtilizada;
    }

    public boolean pistaUtilizada() {
        return pistaUtilizada;
    }


}
