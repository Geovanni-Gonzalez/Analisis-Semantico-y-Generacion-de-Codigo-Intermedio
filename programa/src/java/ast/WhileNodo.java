package ast;

/**
 * Sentencia de ciclo while o do-while.
 */
public class WhileNodo extends SentenciaNodo {
    private final ExpresionNodo condicion;
    private final BloqueNodo cuerpo;
    private final boolean doWhile;
    /**
     * Nombre : WhileNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, ExpresionNodo condicion, BloqueNodo cuerpo, boolean doWhile
     * Salida: Instancia inicializada de WhileNodo.
     */
    public WhileNodo(int linea, int columna, ExpresionNodo condicion, BloqueNodo cuerpo, boolean doWhile) {
        super(linea, columna);
        this.condicion = condicion;
        this.cuerpo = cuerpo;
        this.doWhile = doWhile;
    }
    /**
     * Nombre : getCondicion.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getCondicion() {
        return condicion;
    }

    /**
     * Nombre : getCuerpo.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna BloqueNodo.
     */
    public BloqueNodo getCuerpo() {
        return cuerpo;
    }

    /**
     * Nombre : isDoWhile.
     * Descripcion: Consulta una condicion booleana del objeto.
     * Entrada: Sin parametros.
     * Salida: Retorna boolean.
     */
    public boolean isDoWhile() {
        return doWhile;
    }
}
