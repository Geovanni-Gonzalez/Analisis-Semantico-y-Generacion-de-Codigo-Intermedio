package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <strong>Nombre:</strong> BloqueNodo
 *
 * <p><strong>Objetivo:</strong> Agrupar una secuencia de instrucciones que forman un alcance
 * (un bloque {@code |: ... :|}), como el cuerpo de una función, un if o un ciclo.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y la lista de instrucciones del bloque.</p>
 *
 * <p><strong>Salida:</strong> Nodo que agrupa las instrucciones del alcance.</p>
 *
 * <p><strong>Restricciones:</strong> Copia la lista recibida y la expone como solo lectura.</p>
 */
public class BloqueNodo extends Nodo {
    private final List<Nodo> instrucciones;

    /**
     * <strong>Nombre:</strong> BloqueNodo
     *
     * <p><strong>Objetivo:</strong> Crear el bloque copiando la lista de instrucciones que contiene.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, List&lt;Nodo&gt; instrucciones.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de BloqueNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public BloqueNodo(int linea, int columna, List<Nodo> instrucciones) {
        super(linea, columna, TipoDato.EMPTY);
        this.instrucciones = new ArrayList<>(instrucciones);
    }

    /**
     * <strong>Nombre:</strong> getInstrucciones
     *
     * <p><strong>Objetivo:</strong> Devolver las instrucciones del bloque, en orden.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> List&lt;Nodo&gt; no modificable con las instrucciones.</p>
     *
     * <p><strong>Restricciones:</strong> La lista no se puede modificar.</p>
     */
    public List<Nodo> getInstrucciones() {
        return Collections.unmodifiableList(instrucciones);
    }
}
