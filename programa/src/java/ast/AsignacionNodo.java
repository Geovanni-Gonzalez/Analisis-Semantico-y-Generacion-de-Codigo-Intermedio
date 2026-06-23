package ast;

/**
 * <strong>Nombre:</strong> AsignacionNodo
 *
 * <p><strong>Objetivo:</strong> Representar una asignación: guardar un valor en un destino,
 * por ejemplo {@code x <- 5} o {@code v[0] <- a + b}.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, el destino y el valor a asignar.</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena la estructura; no realiza la asignación.</p>
 */
public class AsignacionNodo extends SentenciaNodo {
    private final ExpresionNodo destino;
    private final ExpresionNodo valor;

    /**
     * <strong>Nombre:</strong> AsignacionNodo
     *
     * <p><strong>Objetivo:</strong> Crear la asignación con su destino (lado izquierdo) y su valor (lado derecho).</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, ExpresionNodo destino, ExpresionNodo valor.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de AsignacionNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public AsignacionNodo(int linea, int columna, ExpresionNodo destino, ExpresionNodo valor) {
        super(linea, columna);
        this.destino = destino;
        this.valor = valor;
    }

    /**
     * <strong>Nombre:</strong> getDestino
     *
     * <p><strong>Objetivo:</strong> Devolver el destino donde se almacena el valor (variable o acceso a arreglo).</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del destino.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getDestino() {
        return destino;
    }

    /**
     * <strong>Nombre:</strong> getValor
     *
     * <p><strong>Objetivo:</strong> Devolver la expresión cuyo resultado se asigna al destino.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del valor.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getValor() {
        return valor;
    }
}
