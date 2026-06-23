package ast;

/**
 * <strong>Nombre:</strong> CasoSwitchNodo
 *
 * <p><strong>Objetivo:</strong> Representar un caso individual de un {@code switch}: el valor con
 * el que se compara (o el caso por defecto) y el bloque que se ejecuta al coincidir.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, el valor, el bloque y si es el caso default.</p>
 *
 * <p><strong>Salida:</strong> Nodo consultable por las fases semántica e intermedia.</p>
 *
 * <p><strong>Restricciones:</strong> En el caso default el valor es {@code null}.</p>
 */
public class CasoSwitchNodo extends Nodo {
    private final ExpresionNodo valor;
    private final BloqueNodo bloque;
    private final boolean defecto;

    /**
     * <strong>Nombre:</strong> CasoSwitchNodo
     *
     * <p><strong>Objetivo:</strong> Crear el caso con su valor, su bloque y si es el caso {@code default}.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, ExpresionNodo valor, BloqueNodo bloque, boolean defecto.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de CasoSwitchNodo.</p>
     *
     * <p><strong>Restricciones:</strong> {@code defecto = true} marca el caso default.</p>
     */
    public CasoSwitchNodo(int linea, int columna, ExpresionNodo valor, BloqueNodo bloque, boolean defecto) {
        super(linea, columna);
        this.valor = valor;
        this.bloque = bloque;
        this.defecto = defecto;
    }

    /**
     * <strong>Nombre:</strong> getValor
     *
     * <p><strong>Objetivo:</strong> Devolver el valor con el que se compara la expresión del switch.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del valor, o {@code null} en el caso default.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getValor() {
        return valor;
    }

    /**
     * <strong>Nombre:</strong> getBloque
     *
     * <p><strong>Objetivo:</strong> Devolver el bloque que se ejecuta cuando este caso coincide.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> BloqueNodo del caso.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public BloqueNodo getBloque() {
        return bloque;
    }

    /**
     * <strong>Nombre:</strong> isDefecto
     *
     * <p><strong>Objetivo:</strong> Indicar si es el caso {@code default}.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> boolean; true si es el caso default.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public boolean isDefecto() {
        return defecto;
    }
}
