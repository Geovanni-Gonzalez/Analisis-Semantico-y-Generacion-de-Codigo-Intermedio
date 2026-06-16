package ast;

/**
 * Sentencia de salida del lenguaje, equivalente a imprimir una expresion.
 */
public class SalidaNodo extends SentenciaNodo {
    private final ExpresionNodo valor;
    /**
     * Nombre : SalidaNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, ExpresionNodo valor
     * Salida: Instancia inicializada de SalidaNodo.
     */
    public SalidaNodo(int linea, int columna, ExpresionNodo valor) {
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
