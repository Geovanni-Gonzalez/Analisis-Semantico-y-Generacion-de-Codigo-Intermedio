package ast;

/**
 * <strong>Nombre:</strong> ExpresionNodo
 *
 * <p><strong>Objetivo:</strong> Clase base de todos los nodos que producen un valor
 * (literales, identificadores, operaciones, llamadas a función, etc.).</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y, opcionalmente, el tipo de dato.</p>
 *
 * <p><strong>Salida:</strong> Nodo base reutilizado por todas las expresiones del AST.</p>
 *
 * <p><strong>Restricciones:</strong> Es abstracta; solo almacena datos.</p>
 */
public abstract class ExpresionNodo extends Nodo {

    /**
     * <strong>Nombre:</strong> ExpresionNodo
     *
     * <p><strong>Objetivo:</strong> Crear una expresión en la posición dada con tipo aún desconocido.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de ExpresionNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Constructor protegido; lo usan las subclases.</p>
     */
    protected ExpresionNodo(int linea, int columna) {
        super(linea, columna);
    }

    /**
     * <strong>Nombre:</strong> ExpresionNodo
     *
     * <p><strong>Objetivo:</strong> Crear una expresión en la posición dada con un tipo ya conocido.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, TipoDato tipo.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de ExpresionNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Constructor protegido; lo usan las subclases.</p>
     */
    protected ExpresionNodo(int linea, int columna, TipoDato tipo) {
        super(linea, columna, tipo);
    }
}
