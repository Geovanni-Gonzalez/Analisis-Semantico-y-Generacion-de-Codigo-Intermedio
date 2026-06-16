package ast;

/**
 * Clase base para todos los nodos que producen un valor.
 *
 * <p>Las expresiones conservan un {@link TipoDato} calculado por el analizador
 * semantico para evitar reevaluar tipos en subarboles ya visitados.</p>
 */
public abstract class ExpresionNodo extends Nodo {
    /**
     * Nombre : ExpresionNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna
     * Salida: Instancia inicializada de ExpresionNodo.
     */
    protected ExpresionNodo(int linea, int columna) {
        super(linea, columna);
    }
    /**
     * Nombre : ExpresionNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, TipoDato tipo
     * Salida: Instancia inicializada de ExpresionNodo.
     */
    protected ExpresionNodo(int linea, int columna, TipoDato tipo) {
        super(linea, columna, tipo);
    }
}
