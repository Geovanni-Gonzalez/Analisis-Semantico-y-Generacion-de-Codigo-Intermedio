package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <strong>Nombre:</strong> SwitchNodo
 *
 * <p><strong>Objetivo:</strong> Representar la sentencia {@code switch}: evaluar una expresión y
 * ejecutar el caso cuyo valor coincide, o el caso {@code default} si ninguno coincide.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, la expresión de selección y la lista de casos.</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Copia la lista de casos y la expone como solo lectura.</p>
 */
public class SwitchNodo extends SentenciaNodo {
    private final ExpresionNodo expresion;
    private final List<CasoSwitchNodo> casos;

    /**
     * <strong>Nombre:</strong> SwitchNodo
     *
     * <p><strong>Objetivo:</strong> Crear el switch con la expresión de selección y la lista de casos.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, ExpresionNodo expresion, List&lt;CasoSwitchNodo&gt; casos.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de SwitchNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public SwitchNodo(int linea, int columna, ExpresionNodo expresion, List<CasoSwitchNodo> casos) {
        super(linea, columna);
        this.expresion = expresion;
        this.casos = new ArrayList<>(casos);
    }

    /**
     * <strong>Nombre:</strong> getExpresion
     *
     * <p><strong>Objetivo:</strong> Devolver la expresión cuyo valor se compara contra cada caso.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo de selección.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getExpresion() {
        return expresion;
    }

    /**
     * <strong>Nombre:</strong> getCasos
     *
     * <p><strong>Objetivo:</strong> Devolver los casos del switch, en orden.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> List&lt;CasoSwitchNodo&gt; no modificable.</p>
     *
     * <p><strong>Restricciones:</strong> La lista no se puede modificar.</p>
     */
    public List<CasoSwitchNodo> getCasos() {
        return Collections.unmodifiableList(casos);
    }
}
