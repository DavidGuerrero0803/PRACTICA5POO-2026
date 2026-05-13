package uabc.david.practica5poo2026;

import java.util.ArrayList;
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


    private void ejecutarPartida() {
        int numeroIntento = 1;

        while (juego.juegoActivo()) {
            System.out.println("\n-------------------------------------------------");
            System.out.println(juego.obtenerEstado());
            System.out.println("-------------------------------------------------");

            mostrarHistorial(numeroIntento);
            mostrarLetrasUsadas();

            System.out.println("\nOpciones:");
            System.out.println("[1] Escribir una palabra");
            System.out.println("[2] Pedir pista");
            int accion = leerOpcion("Elige una opción: ", 1, 2);

            if (accion == 2) {
                gestionarPista();
                continue;
            }

            String intento = leerEntrada("Escribe tu palabra: ");
            String resultado = juego.procesarIntento(intento);

            if (resultado.equals("longitud")) {
                System.out.println("La palabra debe tener " +
                        juego.getEstadoActual().getLongitudPalabra() + " letras. Intenta de nuevo.");
                continue;
            }

            if (resultado.equals("no encontrada")) {
                continue;
            }

            if (resultado.equals("sin intentos")) {
                System.out.println("No te quedan intentos disponibles.");
                break;
            }

            if (resultado.equals("correcto")) {
                mostrarHistorial(numeroIntento + 1);
                System.out.println("\nGANASTE!");
                System.out.println("La palabra era: " + intento.toUpperCase());
                return;
            }

            if (resultado.equals("antes")) {
                System.out.println("La palabra secreta está ANTES de " + intento + " alfabéticamente.");
            } else if (resultado.equals("despues")) {
                System.out.println("La palabra secreta está DESPUÉS de " + intento + " alfabéticamente.");
            }

            numeroIntento++;
        }

        System.out.println("\n SE ACABARON LOS INTENTOS");
        System.out.println("La palabra era: " + juego.getEstadoActual().getPalabraSecreta().toUpperCase());

    }

    private String leerEntrada(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim().toLowerCase();
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
