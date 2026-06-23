package ast;

/**
 * <strong>Nombre:</strong> WhileNodo
 *
 * <p><strong>Objetivo:</strong> Representar un ciclo. Sirve para {@code while} (evalúa la
 * condición antes del cuerpo) y para {@code do-while} (la evalúa después), según el indicador.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, la condición, el cuerpo y si es do-while.</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> El campo {@code doWhile} distingue las dos variantes de ciclo.</p>
 */
public class WhileNodo extends SentenciaNodo {
    private final ExpresionNodo condicion;
    private final BloqueNodo cuerpo;
    private final boolean doWhile;

    /**
     * <strong>Nombre:</strong> WhileNodo
     *
     * <p><strong>Objetivo:</strong> Crear el ciclo con su condición, su cuerpo y si es do-while o while.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, ExpresionNodo condicion, BloqueNodo cuerpo, boolean doWhile.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de WhileNodo.</p>
     *
     * <p><strong>Restricciones:</strong> {@code doWhile = true} indica un do-while.</p>
     */
    public WhileNodo(int linea, int columna, ExpresionNodo condicion, BloqueNodo cuerpo, boolean doWhile) {
        super(linea, columna);
        this.condicion = condicion;
        this.cuerpo = cuerpo;
        this.doWhile = doWhile;
    }

    /**
     * <strong>Nombre:</strong> getCondicion
     *
     * <p><strong>Objetivo:</strong> Devolver la condición que mantiene el ciclo mientras sea verdadera.</p>
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
     * <strong>Nombre:</strong> getCuerpo
     *
     * <p><strong>Objetivo:</strong> Devolver el bloque de instrucciones que se repite.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> BloqueNodo del cuerpo del ciclo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public BloqueNodo getCuerpo() {
        return cuerpo;
    }

    /**
     * <strong>Nombre:</strong> isDoWhile
     *
     * <p><strong>Objetivo:</strong> Indicar si es un do-while (el cuerpo se ejecuta al menos una vez).</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> boolean; true si es do-while.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public boolean isDoWhile() {
        return doWhile;
    }
}
