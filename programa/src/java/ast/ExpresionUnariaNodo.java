package ast;

/**
 * Expresion formada por un operador unario y un unico operando.
 */
public class ExpresionUnariaNodo extends ExpresionNodo {
    private final String operador;
    private final ExpresionNodo expresion;
    /**
     * Nombre : ExpresionUnariaNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String operador, ExpresionNodo expresion
     * Salida: Instancia inicializada de ExpresionUnariaNodo.
     */
    public ExpresionUnariaNodo(int linea, int columna, String operador, ExpresionNodo expresion) {
        super(linea, columna);
        this.operador = operador;
        this.expresion = expresion;
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
     * Nombre : getExpresion.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getExpresion() {
        return expresion;
    }
}
