package ast;

/**
 * Expresion que representa el acceso a una celda de un arreglo bidimensional.
 *
 * <p>Guarda el nombre del arreglo y las dos expresiones usadas como indices.
 * El analizador semantico valida que el identificador sea un arreglo y que
 * ambos indices sean de tipo {@link TipoDato#INT}.</p>
 */
public class AccesoArregloNodo extends ExpresionNodo {
    private final String nombre;
    private final ExpresionNodo fila;
    private final ExpresionNodo columnaIndice;
    /**
     * Nombre : AccesoArregloNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String nombre, ExpresionNodo fila, ExpresionNodo columnaIndice
     * Salida: Instancia inicializada de AccesoArregloNodo.
     */
    public AccesoArregloNodo(int linea, int columna, String nombre,
                             ExpresionNodo fila, ExpresionNodo columnaIndice) {
        super(linea, columna);
        this.nombre = nombre;
        this.fila = fila;
        this.columnaIndice = columnaIndice;
    }

    /**
     * Nombre : getNombre.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Nombre : getFila.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getFila() {
        return fila;
    }

    /**
     * Nombre : getColumnaIndice.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getColumnaIndice() {
        return columnaIndice;
    }
}
