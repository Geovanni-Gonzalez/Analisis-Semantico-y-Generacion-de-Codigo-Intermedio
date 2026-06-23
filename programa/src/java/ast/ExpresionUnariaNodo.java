package ast;

/**
 * <strong>Nombre:</strong> ExpresionUnariaNodo
 *
 * <p><strong>Objetivo:</strong> Representar una expresión con un operador unario y un único
 * operando, por ejemplo {@code -x}, {@code ++i} o la negación lógica.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, el operador y el operando.</p>
 *
 * <p><strong>Salida:</strong> Nodo de expresión consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena la estructura; no evalúa la operación.</p>
 */
public class ExpresionUnariaNodo extends ExpresionNodo {
    private final String operador;
    private final ExpresionNodo expresion;

    /**
     * <strong>Nombre:</strong> ExpresionUnariaNodo
     *
     * <p><strong>Objetivo:</strong> Crear la expresión con su operador y su operando.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String operador, ExpresionNodo expresion.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de ExpresionUnariaNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionUnariaNodo(int linea, int columna, String operador, ExpresionNodo expresion) {
        super(linea, columna);
        this.operador = operador;
        this.expresion = expresion;
    }

    /**
     * <strong>Nombre:</strong> getOperador
     *
     * <p><strong>Objetivo:</strong> Devolver el símbolo del operador unario aplicado.</p>
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
     * <strong>Nombre:</strong> getExpresion
     *
     * <p><strong>Objetivo:</strong> Devolver el operando sobre el que actúa el operador.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del operando.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getExpresion() {
        return expresion;
    }
}
