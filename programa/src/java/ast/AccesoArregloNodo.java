package ast;

/**
 * <strong>Nombre:</strong> AccesoArregloNodo
 *
 * <p><strong>Objetivo:</strong> Representar el acceso a una celda de un arreglo bidimensional,
 * por ejemplo {@code matriz<<i>><<j>>}.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, el nombre del arreglo y los índices de fila y columna.</p>
 *
 * <p><strong>Salida:</strong> Nodo de expresión consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena la estructura del acceso.</p>
 */
public class AccesoArregloNodo extends ExpresionNodo {
    private final String nombre;
    private final ExpresionNodo fila;
    private final ExpresionNodo columnaIndice;

    /**
     * <strong>Nombre:</strong> AccesoArregloNodo
     *
     * <p><strong>Objetivo:</strong> Crear el acceso con el nombre del arreglo y los índices de fila y columna.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String nombre, ExpresionNodo fila, ExpresionNodo columnaIndice.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de AccesoArregloNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public AccesoArregloNodo(int linea, int columna, String nombre,
                             ExpresionNodo fila, ExpresionNodo columnaIndice) {
        super(linea, columna);
        this.nombre = nombre;
        this.fila = fila;
        this.columnaIndice = columnaIndice;
    }

    /**
     * <strong>Nombre:</strong> getNombre
     *
     * <p><strong>Objetivo:</strong> Devolver el nombre del arreglo al que se accede.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el nombre del arreglo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * <strong>Nombre:</strong> getFila
     *
     * <p><strong>Objetivo:</strong> Devolver el índice de la fila (primer subíndice).</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del índice de fila.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getFila() {
        return fila;
    }

    /**
     * <strong>Nombre:</strong> getColumnaIndice
     *
     * <p><strong>Objetivo:</strong> Devolver el índice de la columna (segundo subíndice).</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del índice de columna.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getColumnaIndice() {
        return columnaIndice;
    }
}
