package ast;

/**
 * <strong>Nombre:</strong> IfNodo
 *
 * <p><strong>Objetivo:</strong> Representar la sentencia condicional {@code if}: ejecutar un
 * bloque cuando la condición es verdadera y, opcionalmente, otro {@code else} cuando es falsa.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, la condición, el bloque verdadero y el bloque else.</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> El bloque else puede ser {@code null} si no existe.</p>
 */
public class IfNodo extends SentenciaNodo {
    private final ExpresionNodo condicion;
    private final BloqueNodo bloqueEntonces;
    private final BloqueNodo bloqueSino;

    /**
     * <strong>Nombre:</strong> IfNodo
     *
     * <p><strong>Objetivo:</strong> Crear el if con su condición, el bloque verdadero y el bloque else.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, ExpresionNodo condicion, BloqueNodo bloqueEntonces, BloqueNodo bloqueSino.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de IfNodo.</p>
     *
     * <p><strong>Restricciones:</strong> {@code bloqueSino} es {@code null} cuando no hay rama else.</p>
     */
    public IfNodo(int linea, int columna, ExpresionNodo condicion,
                  BloqueNodo bloqueEntonces, BloqueNodo bloqueSino) {
        super(linea, columna);
        this.condicion = condicion;
        this.bloqueEntonces = bloqueEntonces;
        this.bloqueSino = bloqueSino;
    }

    /**
     * <strong>Nombre:</strong> getCondicion
     *
     * <p><strong>Objetivo:</strong> Devolver la condición booleana que decide qué bloque se ejecuta.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo de la condición.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getCondicion() {
        return condicion;
    }

    /**
     * <strong>Nombre:</strong> getBloqueEntonces
     *
     * <p><strong>Objetivo:</strong> Devolver el bloque que se ejecuta cuando la condición es verdadera.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> BloqueNodo de la rama verdadera.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public BloqueNodo getBloqueEntonces() {
        return bloqueEntonces;
    }

    /**
     * <strong>Nombre:</strong> getBloqueSino
     *
     * <p><strong>Objetivo:</strong> Devolver el bloque {@code else}, o {@code null} si no existe.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> BloqueNodo de la rama else, o {@code null}.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public BloqueNodo getBloqueSino() {
        return bloqueSino;
    }
}
