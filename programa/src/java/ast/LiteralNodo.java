package ast;

/**
 * Expresion hoja que representa un literal del codigo fuente.
 */
public class LiteralNodo extends ExpresionNodo {
    private final Object valor;
    /**
     * Nombre : LiteralNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, Object valor, TipoDato tipo
     * Salida: Instancia inicializada de LiteralNodo.
     */
    public LiteralNodo(int linea, int columna, Object valor, TipoDato tipo) {
        super(linea, columna, tipo);
        this.valor = valor;
    }

    /**
     * Nombre : getValor.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna Object.
     */
    public Object getValor() {
        return valor;
    }
}
