package uabc.david.practica5poo2026;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class BetweenleUI {
    private Betweenle juego;
    private Scanner scanner;

    public BetweenleUI() {
        this.scanner = new Scanner(System.in);
        this.juego = new Betweenle(7);
    }

    public void iniciar() {
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
        System.out.println("1. Fácil (5 letras)");
        System.out.println("2. Intermedio (6 letras)");
        System.out.println("3. Difícil (n letras)");
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

        // El jugador elige cuántos intentos quiere tener en la partida.
        System.out.println("\nSelecciona el número de intentos:");
        System.out.println("1. 10 intentos");
        System.out.println("2. 12 intentos");
        System.out.println("3. 14 intentos");
        int opcionIntentos = leerOpcion("Opción: ", 1, 3);

        int intentosElegidos;
        if (opcionIntentos == 1) {
            intentosElegidos = 10;
        } else if (opcionIntentos == 2) {
            intentosElegidos = 12;
        } else {
            intentosElegidos = 14;
        }

        System.out.println("\nCargando diccionario...");

        boolean iniciado = juego.iniciarPartida(idioma, dificultad, intentosElegidos);

        if (!iniciado) {
            System.out.println("No se encontraron palabras de esa longitud en el diccionario.");
            mostrarMenu();
            return;
        }

        int longitud = juego.getLongitudDificultad(dificultad);

        // Se muestra la longitud junto con los intentos elegidos por el jugador.
        System.out.println("Es una palabra de " + longitud +
                " letras, tienes " + intentosElegidos + " intentos disponibles.");
    }

    private int leerOpcion(String mensaje, int opc1, int opcAlt) {
        int opcion = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                opcion = Integer.parseInt(entrada);
                if (opcion >= opc1 && opcion <= opcAlt) {
                    valido = true;
                } else {
                    System.out.println("Elige una opción entre " + opc1 + " y " + opcAlt + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número válido");
            }
        }

        return opcion;
    }

    private void ejecutarPartida() {
        int numeroIntento = 1;
        boolean esGanador = false;

        while (juego.juegoActivo()) {
            System.out.println("\n-----------------------------------------");
            System.out.println(juego.getEstado());
            System.out.println("-----------------------------------------");

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

            String intento = leerEntrada("Escribe una palabra: ");
            String resultado = juego.procesarIntento(intento);

            if (resultado.equals("longitud")) {
                System.out.println("\nLa palabra debe tener " +
                        juego.getRondaActual().getLongitudPalabra() + " letras. Intenta de nuevo.");
                continue;
            }

            if (resultado.equals("no encontrada")) {
                manejarPalabraInexistente(intento);
                continue;
            }

            if (resultado.equals("fuera de rango")) {
                System.out.println("\nLa palabra " + intento.toUpperCase()
                        + " está fuera del rango válido.");
                System.out.println("Ingresa una palabra que esté alfabéticamente entre ["
                        + juego.getRondaActual().getLimiteSuperior().toUpperCase()
                        + "] y ["
                        + juego.getRondaActual().getLimiteInferior().toUpperCase()
                        + "].");
                continue;
            }

            if (resultado.equals("sin intentos")) {
                System.out.println("No te quedan intentos disponibles.");
                break;
            }

            if (resultado.equals("correcto")) {
                esGanador = true;
                break;
            }

            if (resultado.equals("antes")) {
                System.out.println("\nLa palabra secreta está ANTES de " + intento.toUpperCase() + " alfabéticamente.");
            } else if (resultado.equals("despues")) {
                System.out.println("\nLa palabra secreta está DESPUÉS de " + intento.toUpperCase() + " alfabéticamente.");
            }

            numeroIntento++;
        }

        System.out.println("\n---------------------------------------------------");
        System.out.println("               RESUMEN DE LA PARTIDA               ");
        System.out.println("---------------------------------------------------");

        mostrarHistorial(esGanador ? numeroIntento + 1 : numeroIntento);

        System.out.println("\nLA PALABRA ERA: " + juego.getRondaActual().getPalabraSecreta().toUpperCase());

        if (esGanador) {
            System.out.println("\nADIVINASTE LA PALABRA");
            System.out.println("¡GANASTE LA PARTIDA!");
        } else {
            System.out.println("\nTE QUEDASTE SIN INTENTOS");
            System.out.println("PERDISTE LA PARTIDA :(");
        }
    }

    private String leerEntrada(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim().toLowerCase();
    }

    private void mostrarHistorial(int intentosTotales) {
        ArrayList<String> historial = juego.getHistorial();
        if (historial.isEmpty()) {
            System.out.println("\nNo se ha escrito ninguna palabra");
            return;
        }

        System.out.println("\nHistorial de intentos:");
        int numero = 1;
        for (String linea : historial) {
            System.out.println("  " + numero + ". " + linea);
            numero++;
        }
    }

    private void mostrarLetrasUsadas() {
        Iterator<String> iterador = juego.getRondaActual().getLetrasUsadas().iterator();

        if (!iterador.hasNext()) {
            return;
        }

        System.out.print("\nLetras usadas: ");
        while (iterador.hasNext()) {
            System.out.print(iterador.next().toUpperCase() + " ");
        }
        System.out.println();
    }

    private void gestionarPista() {
        if (juego.getRondaActual().pistaUtilizada()) {
            System.out.println("Ya usaste tu pista en esta partida.");
            return;
        }

        System.out.println("\nElige tu pista (solo puedes usar 1 por partida):");
        System.out.println("1. Acercar el límite superior a la palabra secreta");
        System.out.println("2. Acercar el límite inferior a la palabra secreta");
        System.out.println("3. Revelar la letra inicial de la palabra secreta");
        int opcionPista = leerOpcion("Elige una opción: ", 1, 3);

        String resultadoPista = juego.pedirPista(opcionPista);

        if (resultadoPista.equals("requiere intento")) {
            System.out.println("\nNo puedes usar esta pista.");
            System.out.println("Ingresa al menos una palabra para establecer los límites iniciales.");
            return;
        }

        System.out.println("\nPista: " + resultadoPista);
    }

    private void manejarPalabraInexistente(String palabra) {
        System.out.println("\nLa palabra " + palabra + " no está en el diccionario.");
        System.out.println("¿Puedes demostrar que es una palabra válida?");
        System.out.println("1. Sí, agregarla al diccionario");
        System.out.println("2. No, escribir otra palabra");
        int opcion = leerOpcion("Elige una opción: ", 1, 2);

        if (opcion == 1) {
            juego.agregarPalabraAlDiccionario(palabra);
            System.out.println("La palabra " + palabra + " fue agregada al diccionario.");
        }
    }

    public static void main(String[] args) {
        BetweenleUI ui = new BetweenleUI();
        ui.iniciar();
    }
}