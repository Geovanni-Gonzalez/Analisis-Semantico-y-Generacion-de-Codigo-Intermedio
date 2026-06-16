package ast;

/**
 * Representa un caso individual dentro de un switch.
 *
 * <p>Un caso puede tener valor explicito o ser el caso por defecto. El bloque
 * asociado contiene las sentencias que deben ejecutarse cuando el caso coincide.</p>
 */
public class CasoSwitchNodo extends Nodo {
    private final ExpresionNodo valor;
    private final BloqueNodo bloque;
    private final boolean defecto;
    /**
     * Nombre : CasoSwitchNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, ExpresionNodo valor, BloqueNodo bloque, boolean defecto
     * Salida: Instancia inicializada de CasoSwitchNodo.
     */
    public CasoSwitchNodo(int linea, int columna, ExpresionNodo valor, BloqueNodo bloque, boolean defecto) {
        super(linea, columna);
        this.valor = valor;
        this.bloque = bloque;
        this.defecto = defecto;
    }

    /**
     * Nombre : getValor.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getValor() {
        return valor;
    }

    /**
     * Nombre : getBloque.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna BloqueNodo.
     */
    public BloqueNodo getBloque() {
        return bloque;
    }

    /**
     * Nombre : isDefecto.
     * Descripcion: Consulta una condicion booleana del objeto.
     * Entrada: Sin parametros.
     * Salida: Retorna boolean.
     */
    public boolean isDefecto() {
        return defecto;
    }
}
