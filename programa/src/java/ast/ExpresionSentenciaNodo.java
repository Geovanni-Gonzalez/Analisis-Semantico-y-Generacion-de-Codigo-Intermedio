package ast;

/**
 * <strong>Nombre:</strong> ExpresionSentenciaNodo
 *
 * <p><strong>Objetivo:</strong> Representar una sentencia que consiste en una expresión usada
 * por su efecto lateral (sin guardar su resultado), como una llamada {@code avisar<| |>}
 * o un incremento {@code ++x}.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y la expresión a ejecutar.</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo envuelve la expresión; descarta su valor.</p>
 */
public class ExpresionSentenciaNodo extends SentenciaNodo {
    private final ExpresionNodo expresion;

    /**
     * <strong>Nombre:</strong> ExpresionSentenciaNodo
     *
     * <p><strong>Objetivo:</strong> Crear la sentencia que envuelve a la expresión ejecutada.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, ExpresionNodo expresion.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de ExpresionSentenciaNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionSentenciaNodo(int linea, int columna, ExpresionNodo expresion) {
        super(linea, columna);
        this.expresion = expresion;
    }

    /**
     * <strong>Nombre:</strong> getExpresion
     *
     * <p><strong>Objetivo:</strong> Devolver la expresión que se evalúa por su efecto.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo ejecutada.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getExpresion() {
        return expresion;
    }
}
