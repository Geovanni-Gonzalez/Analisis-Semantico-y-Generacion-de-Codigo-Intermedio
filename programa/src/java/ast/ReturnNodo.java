package ast;

/**
 * Sentencia de retorno de una funcion.
 */
public class ReturnNodo extends SentenciaNodo {
    private final ExpresionNodo valor;
    /**
     * Nombre : ReturnNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, ExpresionNodo valor
     * Salida: Instancia inicializada de ReturnNodo.
     */
    public ReturnNodo(int linea, int columna, ExpresionNodo valor) {
        super(linea, columna);
        this.valor = valor;
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
