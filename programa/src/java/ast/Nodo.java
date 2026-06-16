package ast;

/**
 * Clase base de todos los nodos del arbol sintactico abstracto.
 *
 * <p>Todos los nodos conservan su ubicacion en el archivo fuente para reportar
 * errores y un tipo asociado que puede ser declarado, inferido o desconocido.</p>
 */
public abstract class Nodo {
    private final int linea;
    private final int columna;
    private TipoDato tipo;
    /**
     * Nombre : Nodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna
     * Salida: Instancia inicializada de Nodo.
     */
    protected Nodo(int linea, int columna) {
        this(linea, columna, TipoDato.DESCONOCIDO);
    }
    /**
     * Nombre : Nodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, TipoDato tipo
     * Salida: Instancia inicializada de Nodo.
     */
    protected Nodo(int linea, int columna, TipoDato tipo) {
        this.linea = linea;
        this.columna = columna;
        this.tipo = tipo;
    }
    /**
     * Nombre : getLinea.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna int.
     */
    public int getLinea() {
        return linea;
    }

    /**
     * Nombre : getColumna.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna int.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Nombre : getTipo.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna TipoDato.
     */
    public TipoDato getTipo() {
        return tipo;
    }

    /**
     * Nombre : setTipo.
     * Descripcion: Actualiza el valor asociado a esta propiedad.
     * Entrada: TipoDato tipo
     * Salida: No retorna valor.
     */
    public void setTipo(TipoDato tipo) {
        this.tipo = tipo;
    }
}
