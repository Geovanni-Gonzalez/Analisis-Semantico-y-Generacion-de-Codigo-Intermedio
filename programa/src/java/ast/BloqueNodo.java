package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Nodo que agrupa una secuencia de instrucciones dentro de un alcance.
 *
 * <p>Se usa para cuerpos es, bloques de control y casos de switch.
 * El parser abre/cierra alcances semanticos alrededor de estos bloques.</p>
 */
public class BloqueNodo extends Nodo {
    private final List<Nodo> instrucciones;
    /**
     * Nombre : BloqueNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, List<Nodo> instrucciones
     * Salida: Instancia inicializada de BloqueNodo.
     */
    public BloqueNodo(int linea, int columna, List<Nodo> instrucciones) {
        super(linea, columna, TipoDato.EMPTY);
        this.instrucciones = new ArrayList<>(instrucciones);
    }

    /**
     * Nombre : getInstrucciones.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna List<Nodo>.
     */
    public List<Nodo> getInstrucciones() {
        return Collections.unmodifiableList(instrucciones);
    }
}
