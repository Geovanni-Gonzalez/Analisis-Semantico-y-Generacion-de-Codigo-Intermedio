package ast;

/**
 * Sentencia de asignacion.
 *
 * <p>El destino puede ser un identificador escalar o un acceso a arreglo. El
 * valor es cualquier expresion compatible con el tipo del destino.</p>
 */
public class AsignacionNodo extends SentenciaNodo {
    private final ExpresionNodo destino;
    private final ExpresionNodo valor;
    /**
     * Nombre : AsignacionNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, ExpresionNodo destino, ExpresionNodo valor
     * Salida: Instancia inicializada de AsignacionNodo.
     */
    public AsignacionNodo(int linea, int columna, ExpresionNodo destino, ExpresionNodo valor) {
        super(linea, columna);
        this.destino = destino;
        this.valor = valor;
    }

    /**
     * Nombre : getDestino.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getDestino() {
        return destino;
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
}
