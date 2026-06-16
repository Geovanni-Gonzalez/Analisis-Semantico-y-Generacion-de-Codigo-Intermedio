package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Expresion que representa una invocacion  con argumentos.
 */
public class LlamadaFuncionNodo extends ExpresionNodo {
    private final String nombre;
    private final List<ExpresionNodo> argumentos;
    /**
     * Nombre : LlamadaFuncionNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String nombre, List<ExpresionNodo> argumentos
     * Salida: Instancia inicializada de LlamadaFuncionNodo.
     */
    public LlamadaFuncionNodo(int linea, int columna, String nombre, List<ExpresionNodo> argumentos) {
        super(linea, columna);
        this.nombre = nombre;
        this.argumentos = new ArrayList<>(argumentos);
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
     * Nombre : getArgumentos.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna List<ExpresionNodo>.
     */
    public List<ExpresionNodo> getArgumentos() {
        return Collections.unmodifiableList(argumentos);
    }
}
