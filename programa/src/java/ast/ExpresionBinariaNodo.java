package ast;

/**
 * <strong>Nombre:</strong> ExpresionBinariaNodo
 *
 * <p><strong>Objetivo:</strong> Representar una expresión con dos operandos y un operador
 * binario, por ejemplo {@code a + b} o {@code x < y}.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, el operador y los operandos izquierdo y derecho.</p>
 *
 * <p><strong>Salida:</strong> Nodo de expresión consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena la estructura; no evalúa la operación.</p>
 */
public class ExpresionBinariaNodo extends ExpresionNodo {
    private final String operador;
    private final ExpresionNodo izquierda;
    private final ExpresionNodo derecha;

    /**
     * <strong>Nombre:</strong> ExpresionBinariaNodo
     *
     * <p><strong>Objetivo:</strong> Crear la expresión con su operador y sus dos operandos.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String operador, ExpresionNodo izquierda, ExpresionNodo derecha.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de ExpresionBinariaNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionBinariaNodo(int linea, int columna, String operador,
                                ExpresionNodo izquierda, ExpresionNodo derecha) {
        super(linea, columna);
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    /**
     * <strong>Nombre:</strong> getOperador
     *
     * <p><strong>Objetivo:</strong> Devolver el símbolo del operador ({@code +}, {@code *}, {@code <}, ...).</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el operador.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public String getOperador() {
        return operador;
    }

    /**
     * <strong>Nombre:</strong> getIzquierda
     *
     * <p><strong>Objetivo:</strong> Devolver el operando del lado izquierdo.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del operando izquierdo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getIzquierda() {
        return izquierda;
    }

    /**
     * <strong>Nombre:</strong> getDerecha
     *
     * <p><strong>Objetivo:</strong> Devolver el operando del lado derecho.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del operando derecho.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getDerecha() {
        return derecha;
    }
}
