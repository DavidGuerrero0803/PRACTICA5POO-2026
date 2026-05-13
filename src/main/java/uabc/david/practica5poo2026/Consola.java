package uabc.david.practica5poo2026;

import java.util.Scanner;

public class Consola {
    private Betweenle juego;
    private Scanner scanner;

    public Consola() {
        this.scanner = new Scanner(System.in);
        this.juego = new Betweenle(3);
    }

    public void iniciar() {
        System.out.println("BIENVENIDO A BETWEENLE");
        mostrarMenu();
        ejecutarPartida();
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\nSelecciona el idioma del diccionario:");
        System.out.println("1. Español");
        System.out.println("2. Inglés");
        int opcionIdioma = leerOpcion("Opción: ", 1, 2);
        String idioma = opcionIdioma == 1 ? "español" : "ingles";

        System.out.println("\nSelecciona la dificultad:");
        System.out.println("1. Fácil (5 letras, 10 intentos)");
        System.out.println("2. Intermedio (6 letras, 12 intentos)");
        System.out.println("3. Difícil (n letras, 14 intentos)");
        int opcionDificultad = leerOpcion("Opción: ", 1, 3);

        String dificultad;
        if (opcionDificultad == 1) {
            dificultad = "facil";
        } else if (opcionDificultad == 2) {
            dificultad = "intermedio";
        } else {
            dificultad = "dificil";
            int letras = leerOpcion("Ingresa el número de letras para el modo difícil: ", 7, 15);
            juego = new Betweenle(letras);
        }

        System.out.println("\nCargando diccionario");
        boolean iniciado = juego.iniciarPartida(idioma, dificultad);

        if (!iniciado) {
            System.out.println("No se encontraron palabras de esa longitud en el diccionario.");
            mostrarMenu();
            return;
        }

        int longitud = juego.obtenerLongitudPorDificultad(dificultad);
        int intentos = juego.obtenerIntentosPorDificultad(dificultad);

        System.out.println("Suerte! Palabra de " + longitud +
                " letras | " + intentos + " intentos disponibles.");
    }

    private int leerOpcion(String mensaje, int min, int max) {
        int opcion = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                opcion = Integer.parseInt(entrada);
                if (opcion >= min && opcion <= max) {
                    valido = true;
                } else {
                    System.out.println("Elige una opción entre " + min + " y " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número válido");
            }
        }

        return opcion;
    }


    public void ejecutarPartida() {

    }

    public void mostrarHistorial() {

    }

    public void mostrarLetrasUsadas() {

    }

    public void gestionarPista() {

    }

    public static void main(String[] args) {
        Consola ui = new Consola();
        ui.iniciar();
    }
}
