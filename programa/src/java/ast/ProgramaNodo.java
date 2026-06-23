package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <strong>Nombre:</strong> ProgramaNodo
 *
 * <p><strong>Objetivo:</strong> Ser la raíz del árbol sintáctico abstracto: agrupa todas las
 * funciones del programa, incluido el procedimiento principal {@code __main__}.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y la lista de funciones declaradas.</p>
 *
 * <p><strong>Salida:</strong> Nodo raíz consultable por las fases semántica e intermedia.</p>
 *
 * <p><strong>Restricciones:</strong> Copia la lista de funciones y la expone como solo lectura.</p>
 */
public class ProgramaNodo extends Nodo {
    private final List<FuncionNodo> funciones;

    /**
     * <strong>Nombre:</strong> ProgramaNodo
     *
     * <p><strong>Objetivo:</strong> Crear el programa con la lista de funciones declaradas.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, List&lt;FuncionNodo&gt; funciones.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de ProgramaNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ProgramaNodo(int linea, int columna, List<FuncionNodo> funciones) {
        super(linea, columna, TipoDato.EMPTY);
        this.funciones = new ArrayList<>(funciones);
    }

    /**
     * <strong>Nombre:</strong> getFunciones
     *
     * <p><strong>Objetivo:</strong> Devolver las funciones del programa, en orden de declaración.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> List&lt;FuncionNodo&gt; no modificable.</p>
     *
     * <p><strong>Restricciones:</strong> La lista no se puede modificar.</p>
     */
    public List<FuncionNodo> getFunciones() {
        return Collections.unmodifiableList(funciones);
    }
}
