package ast;

/**
 * Sentencia compuesta por una expresion usada por su efecto lateral.
 *
 * <p>Se utiliza, por ejemplo, para llamadas a funciones sin asignar su retorno.</p>
 */
public class ExpresionSentenciaNodo extends SentenciaNodo {
    private final ExpresionNodo expresion;
    /**
     * Nombre : ExpresionSentenciaNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, ExpresionNodo expresion
     * Salida: Instancia inicializada de ExpresionSentenciaNodo.
     */
    public ExpresionSentenciaNodo(int linea, int columna, ExpresionNodo expresion) {
        super(linea, columna);
        this.expresion = expresion;
    }

    /**
     * Nombre : getExpresion.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getExpresion() {
        return expresion;
    }
}
