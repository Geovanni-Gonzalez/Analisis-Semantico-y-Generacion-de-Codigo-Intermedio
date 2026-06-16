package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Raiz del AST: contiene todas las funciones y el procedimiento principal.
 */
public class ProgramaNodo extends Nodo {
    private final List<FuncionNodo> funciones;
    /**
     * Nombre : ProgramaNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, List<FuncionNodo> funciones
     * Salida: Instancia inicializada de ProgramaNodo.
     */
    public ProgramaNodo(int linea, int columna, List<FuncionNodo> funciones) {
        super(linea, columna, TipoDato.EMPTY);
        this.funciones = new ArrayList<>(funciones);
    }

    /**
     * Nombre : getFunciones.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna List<FuncionNodo>.
     */
    public List<FuncionNodo> getFunciones() {
        return Collections.unmodifiableList(funciones);
    }
}
