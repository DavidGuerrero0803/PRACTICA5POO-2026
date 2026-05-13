package uabc.david.practica5poo2026;

public class Betweenle {
    private Diccionario diccionario;
    private Estado estadoActual;
    private int letrasDificil;

    public Betweenle(int letrasDificil) {
        this.letrasDificil = letrasDificil;
        this.diccionario = null;
        this.estadoActual = null;
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

}
