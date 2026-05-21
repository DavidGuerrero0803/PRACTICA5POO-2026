package uabc.david.practica5poo2026;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ProcesadorRonda {
    private ArrayList<String> historialIntentos;
    private ArrayList<String> palabrasOrdenadas;
    private HashSet<String> letrasUsadas;
    private String dificultad;
    private String limiteSuperior;
    private String limiteInferior;
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
        this.limiteSuperior = "a".repeat(longitudPalabra);
        this.limiteInferior = "z".repeat(longitudPalabra);
        this.palabrasOrdenadas = palabrasOrdenadas;
    }

    public HashSet<String> getLetrasUsadas() {
        return letrasUsadas;
    }

    public ArrayList<String> getHistorialIntentos() {
        return historialIntentos;
    }

    public String getLimiteSuperior() {
        return limiteSuperior;
    }

    public double getProximidadSuperior() {
        return calcularProximidadLimite(limiteSuperior);
    }

    public String getLimiteInferior() {
        return limiteInferior;
    }

    public double getProximidadInferior() {
        return calcularProximidadLimite(limiteInferior);
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
        // El intento actual se le resta 1.
        intentosRestantes--;

        // Dentro del ciclo, toma la palabra y su longitud
        for (int i = 0; i < intento.length(); i++) {
            // Posteriormente, descompone la palabra en caracteres individuales.
            // Estos caracteres se añaden al HashSet que los guarda,
            // evitando duplicaciones posteriores de letras iguales.
            letrasUsadas.add(String.valueOf(intento.charAt(i)));
        }

        String resultado;

        // Usar compareTo() compara caracter por caracter.
        // Si es 0, son idénticas, si es positivo, la palabra secreta va después alfabéticamente.
        int intentoComparado = intento.compareTo(palabraSecreta);

        if (intentoComparado == 0) {
            resultado = "correcto";
        } else if (intentoComparado > 0) {
            resultado = "antes";
            limiteInferior = intento;
        } else {
            resultado = "despues";
            limiteSuperior = intento;
        }

        double proximidad = calcularProximidad(intento);
        historialIntentos.add(intento);

        return resultado;
    }

    public double calcularProximidad(String intento) {
        return calcularProximidadLimite(intento);
    }

    /**
     * Calcula el porcentaje de distancia alfabética entre un límite y la palabra secreta,
     * basándose en sus índices dentro del diccionario filtrado.
     * @param limite Palabra que marca el límite actual evaluado.
     * @return Porcentaje decimal de distancia (0.00 a 100.0).
     */
    public double calcularProximidadLimite(String limite) {
        // Si el diccionario llegase a estar vacío o con una sola palabra,
        // retorna automáticamente 0.01 para evitar excepciones.
        int totalPalabras = palabrasOrdenadas.size();
        if (totalPalabras <= 1) {
            return 0.01;
        }

        // Se usa binarySearch() ya que palabrasOrdenadas está ordenado de antes,
        // y en vez de recorrer el ArrayList de inicio a fin, inspecciona el centro y divide la lista a la mitad.
        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limite.toLowerCase());


        // Se maneja un caso si la palabra secreta/límite no existe,
        // binarySearch() regresa un número negativo.
        if (indiceSecreto < 0) {
            // Se pasa el número negativo en el índice a un entero positivo,
            // dicho índice le correspondería ocupar en la ordenación alfabética.
            indiceSecreto = -(indiceSecreto + 1);
        }
        if (indiceLimite < 0) {
            indiceLimite = -(indiceLimite + 1);
        }

        // Uso de Math.abs() para calcula el valor absoluto.
        // No importa si el límite está a la izquierda o a la derecha de la palabra secreta.
        // Es para saber la cantidad de palabras que las separan en el diccionario.
        double distanciaIndices = Math.abs(indiceLimite - indiceSecreto);

        // Este cálculo permite conocer qué tan lejos está un rango
        // en términos porcentuales basados en la lista de palabras.
        double distanciaPorcentaje = (distanciaIndices / totalPalabras) * 100.0;

        // La condición permite que el valor de la aproximación
        // no se acorte ni se alargue de más.
         if (distanciaPorcentaje < 0.001) {
            return 0.001;
        }
        if (distanciaPorcentaje > 100.0) {
            return 100.0;
        }

        // La distancia calculada se multiplica por 100, después
        // se redondea a un valor entero y luego se divide entre 100.
        return Math.round(distanciaPorcentaje * 100.0) / 100.0;
    }

    public boolean estaEnRango(String intento) {
        return intento.compareTo(limiteSuperior) > 0 && intento.compareTo(limiteInferior) < 0;
    }

    public boolean tieneIntentos() {
        return intentosRestantes > 0;
    }

    public boolean pistaUtilizada() {
        return pistaUtilizada;
    }

    public String recorrerPalabraArriba() {
        this.pistaUtilizada = true;

        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limiteSuperior.toLowerCase());

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

        this.limiteSuperior = palabrasOrdenadas.get(nuevoIndice);
        return this.limiteSuperior;
    }

    public String recorrerPalabraAbajo() {
        this.pistaUtilizada = true;

        int indiceSecreto = Collections.binarySearch(palabrasOrdenadas, palabraSecreta.toLowerCase());
        int indiceLimite = Collections.binarySearch(palabrasOrdenadas, limiteInferior.toLowerCase());

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

        this.limiteInferior = palabrasOrdenadas.get(nuevoIndice);
        return this.limiteInferior;
    }

    public String pistaLetraInicial() {
        pistaUtilizada = true;
        return String.valueOf(palabraSecreta.charAt(0));
    }

}
