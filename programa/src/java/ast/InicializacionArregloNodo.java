package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Literal compuesto que contiene los valores iniciales de un arreglo 2D.
 */
public class InicializacionArregloNodo extends Nodo {
    private final List<List<ExpresionNodo>> filas;
    /**
     * Nombre : InicializacionArregloNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, List<List<ExpresionNodo>> filas
     * Salida: Instancia inicializada de InicializacionArregloNodo.
     */
    public InicializacionArregloNodo(int linea, int columna, List<List<ExpresionNodo>> filas) {
        super(linea, columna);
        this.filas = new ArrayList<>();
        for (List<ExpresionNodo> fila : filas) {
            this.filas.add(new ArrayList<>(fila));
        }
    }

    /**
     * Nombre : getFilas.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna List<List<ExpresionNodo>>.
     */
    public List<List<ExpresionNodo>> getFilas() {
        List<List<ExpresionNodo>> copia = new ArrayList<>();
        for (List<ExpresionNodo> fila : filas) {
            copia.add(Collections.unmodifiableList(fila));
        }
        return Collections.unmodifiableList(copia);
    }
}
