package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sentencia switch con expresion de seleccion y lista de casos.
 */
public class SwitchNodo extends SentenciaNodo {
    private final ExpresionNodo expresion;
    private final List<CasoSwitchNodo> casos;
    /**
     * Nombre : SwitchNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, ExpresionNodo expresion, List<CasoSwitchNodo> casos
     * Salida: Instancia inicializada de SwitchNodo.
     */
    public SwitchNodo(int linea, int columna, ExpresionNodo expresion, List<CasoSwitchNodo> casos) {
        super(linea, columna);
        this.expresion = expresion;
        this.casos = new ArrayList<>(casos);
    }
    /**
     * Nombre : getExpresion.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getExpresion() {
        return expresion;
    }

    /**
     * Nombre : getCasos.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna List<CasoSwitchNodo>.
     */
    public List<CasoSwitchNodo> getCasos() {
        return Collections.unmodifiableList(casos);
    }
}
