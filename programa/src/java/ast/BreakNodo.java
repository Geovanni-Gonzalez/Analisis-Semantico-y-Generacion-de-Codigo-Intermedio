package ast;

/**
 * <strong>Nombre:</strong> BreakNodo
 *
 * <p><strong>Objetivo:</strong> Representar la sentencia {@code break}, que interrumpe el flujo
 * de una estructura, típicamente para salir de un caso del switch.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente.</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> No lleva datos adicionales.</p>
 */
public class BreakNodo extends SentenciaNodo {

    /**
     * <strong>Nombre:</strong> BreakNodo
     *
     * <p><strong>Objetivo:</strong> Crear el nodo break en la posición indicada.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de BreakNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public BreakNodo(int linea, int columna) {
        super(linea, columna, TipoDato.EMPTY);
    }
}
