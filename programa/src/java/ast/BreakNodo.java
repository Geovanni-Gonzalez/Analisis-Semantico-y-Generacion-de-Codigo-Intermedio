package ast;

/**
 * Sentencia de interrupcion de flujo dentro de estructuras como switch.
 */
public class BreakNodo extends SentenciaNodo {
    /**
     * Nombre : BreakNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna
     * Salida: Instancia inicializada de BreakNodo.
     */
    public BreakNodo(int linea, int columna) {
        super(linea, columna, TipoDato.EMPTY);
    }
}
