package ast;

/**
 * Clase base para nodos que representan instrucciones ejecutables.
 */
public abstract class SentenciaNodo extends Nodo {
    /**
     * Nombre : SentenciaNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna
     * Salida: Instancia inicializada de SentenciaNodo.
     */
    protected SentenciaNodo(int linea, int columna) {
        super(linea, columna);
    }
    /**
     * Nombre : SentenciaNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, TipoDato tipo
     * Salida: Instancia inicializada de SentenciaNodo.
     */
    protected SentenciaNodo(int linea, int columna, TipoDato tipo) {
        super(linea, columna, tipo);
    }
}
