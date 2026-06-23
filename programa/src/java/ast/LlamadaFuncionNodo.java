package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <strong>Nombre:</strong> LlamadaFuncionNodo
 *
 * <p><strong>Objetivo:</strong> Representar la invocación de una función con sus argumentos, por
 * ejemplo {@code sumar<|7, 8|>}. Con esta forma se usan también los operadores relacionales
 * del lenguaje ({@code equal}, {@code less_t}, etc.).</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, el nombre invocado y la lista de argumentos.</p>
 *
 * <p><strong>Salida:</strong> Nodo de expresión consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Copia la lista de argumentos y la expone como solo lectura.</p>
 */
public class LlamadaFuncionNodo extends ExpresionNodo {
    private final String nombre;
    private final List<ExpresionNodo> argumentos;

    /**
     * <strong>Nombre:</strong> LlamadaFuncionNodo
     *
     * <p><strong>Objetivo:</strong> Crear la llamada con el nombre invocado y la lista de argumentos.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String nombre, List&lt;ExpresionNodo&gt; argumentos.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de LlamadaFuncionNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public LlamadaFuncionNodo(int linea, int columna, String nombre, List<ExpresionNodo> argumentos) {
        super(linea, columna);
        this.nombre = nombre;
        this.argumentos = new ArrayList<>(argumentos);
    }

    /**
     * <strong>Nombre:</strong> getNombre
     *
     * <p><strong>Objetivo:</strong> Devolver el nombre de la función u operador invocado.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el nombre.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * <strong>Nombre:</strong> getArgumentos
     *
     * <p><strong>Objetivo:</strong> Devolver los argumentos pasados en la llamada, en orden.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> List&lt;ExpresionNodo&gt; no modificable.</p>
     *
     * <p><strong>Restricciones:</strong> La lista no se puede modificar.</p>
     */
    public List<ExpresionNodo> getArgumentos() {
        return Collections.unmodifiableList(argumentos);
    }
}
