package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <strong>Nombre:</strong> InicializacionArregloNodo
 *
 * <p><strong>Objetivo:</strong> Representar los valores iniciales de un arreglo bidimensional,
 * organizados como una lista de filas y, dentro de cada fila, sus columnas.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y la matriz de expresiones (filas y columnas).</p>
 *
 * <p><strong>Salida:</strong> Nodo consultable por las fases semántica e intermedia.</p>
 *
 * <p><strong>Restricciones:</strong> Copia la matriz recibida y la expone como solo lectura.</p>
 */
public class InicializacionArregloNodo extends Nodo {
    private final List<List<ExpresionNodo>> filas;

    /**
     * <strong>Nombre:</strong> InicializacionArregloNodo
     *
     * <p><strong>Objetivo:</strong> Crear la inicialización copiando la matriz de expresiones recibida.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, List&lt;List&lt;ExpresionNodo&gt;&gt; filas.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de InicializacionArregloNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public InicializacionArregloNodo(int linea, int columna, List<List<ExpresionNodo>> filas) {
        super(linea, columna);
        this.filas = new ArrayList<>();
        for (List<ExpresionNodo> fila : filas) {
            this.filas.add(new ArrayList<>(fila));
        }
    }

    /**
     * <strong>Nombre:</strong> getFilas
     *
     * <p><strong>Objetivo:</strong> Devolver las filas de valores iniciales.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> List&lt;List&lt;ExpresionNodo&gt;&gt; no modificable.</p>
     *
     * <p><strong>Restricciones:</strong> Las listas no se pueden modificar.</p>
     */
    public List<List<ExpresionNodo>> getFilas() {
        List<List<ExpresionNodo>> copia = new ArrayList<>();
        for (List<ExpresionNodo> fila : filas) {
            copia.add(Collections.unmodifiableList(fila));
        }
        return Collections.unmodifiableList(copia);
    }
}
