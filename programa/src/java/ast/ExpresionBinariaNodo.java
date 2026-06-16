package ast;

/**
 * Expresion formada por dos operandos y un operador binario.
 *
 * <p>El operador se conserva como cadena porque proviene directamente de las
 * producciones de CUP. El analizador semantico y el generador intermedio lo
 * traducen a reglas de tipos y operaciones concretas.</p>
 */
public class ExpresionBinariaNodo extends ExpresionNodo {
    private final String operador;
    private final ExpresionNodo izquierda;
    private final ExpresionNodo derecha;
    /**
     * Nombre : ExpresionBinariaNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String operador, ExpresionNodo izquierda, ExpresionNodo derecha
     * Salida: Instancia inicializada de ExpresionBinariaNodo.
     */
    public ExpresionBinariaNodo(int linea, int columna, String operador,
                                ExpresionNodo izquierda, ExpresionNodo derecha) {
        super(linea, columna);
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }
    /**
     * Nombre : getOperador.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String getOperador() {
        return operador;
    }

    /**
     * Nombre : getIzquierda.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getIzquierda() {
        return izquierda;
    }

    /**
     * Nombre : getDerecha.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getDerecha() {
        return derecha;
    }
}
