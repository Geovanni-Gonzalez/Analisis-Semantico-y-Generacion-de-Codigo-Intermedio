package ast;

/**
 * Sentencia condicional con bloque obligatorio y bloque else opcional.
 */
public class IfNodo extends SentenciaNodo {
    private final ExpresionNodo condicion;
    private final BloqueNodo bloqueEntonces;
    private final BloqueNodo bloqueSino;
    /**
     * Nombre : IfNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, ExpresionNodo condicion, BloqueNodo bloqueEntonces, BloqueNodo bloqueSino
     * Salida: Instancia inicializada de IfNodo.
     */
    public IfNodo(int linea, int columna, ExpresionNodo condicion,
                  BloqueNodo bloqueEntonces, BloqueNodo bloqueSino) {
        super(linea, columna);
        this.condicion = condicion;
        this.bloqueEntonces = bloqueEntonces;
        this.bloqueSino = bloqueSino;
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
     * Nombre : getBloqueEntonces.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna BloqueNodo.
     */
    public BloqueNodo getBloqueEntonces() {
        return bloqueEntonces;
    }

    /**
     * Nombre : getBloqueSino.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna BloqueNodo.
     */
    public BloqueNodo getBloqueSino() {
        return bloqueSino;
    }
}
