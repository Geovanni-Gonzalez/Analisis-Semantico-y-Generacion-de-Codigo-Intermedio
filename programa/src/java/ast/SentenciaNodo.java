package ast;

/**
 * <strong>Nombre:</strong> SentenciaNodo
 *
 * <p><strong>Objetivo:</strong> Clase base de los nodos que representan instrucciones
 * ejecutables (asignaciones, condicionales, ciclos, return, break, etc.).</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y, opcionalmente, el tipo de dato.</p>
 *
 * <p><strong>Salida:</strong> Nodo base reutilizado por todas las sentencias del AST.</p>
 *
 * <p><strong>Restricciones:</strong> Es abstracta; solo almacena datos.</p>
 */
public abstract class SentenciaNodo extends Nodo {

    /**
     * <strong>Nombre:</strong> SentenciaNodo
     *
     * <p><strong>Objetivo:</strong> Crear una sentencia en la posición dada con tipo aún desconocido.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de SentenciaNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Constructor protegido; lo usan las subclases.</p>
     */
    protected SentenciaNodo(int linea, int columna) {
        super(linea, columna);
    }

    /**
     * <strong>Nombre:</strong> SentenciaNodo
     *
     * <p><strong>Objetivo:</strong> Crear una sentencia en la posición dada con un tipo ya conocido.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, TipoDato tipo.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de SentenciaNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Constructor protegido; lo usan las subclases.</p>
     */
    protected SentenciaNodo(int linea, int columna, TipoDato tipo) {
        super(linea, columna, tipo);
    }
}
