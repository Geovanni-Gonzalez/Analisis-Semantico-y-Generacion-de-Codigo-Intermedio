package ast;

/**
 * <strong>Nombre:</strong> Nodo
 *
 * <p><strong>Objetivo:</strong> Clase base de todos los nodos del árbol sintáctico
 * abstracto (AST). Guarda la posición en el código fuente y el tipo de dato.</p>
 *
 * <p><strong>Entrada:</strong> Línea y columna del fuente y, opcionalmente, el tipo de dato.</p>
 *
 * <p><strong>Salida:</strong> Nodo base reutilizado por todas las subclases del AST.</p>
 *
 * <p><strong>Restricciones:</strong> Es abstracta; solo almacena datos, no ejecuta análisis.</p>
 */
public abstract class Nodo {
    private final int linea;
    private final int columna;
    private TipoDato tipo;

    /**
     * <strong>Nombre:</strong> Nodo
     *
     * <p><strong>Objetivo:</strong> Crear un nodo en la posición dada con tipo aún desconocido.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de Nodo.</p>
     *
     * <p><strong>Restricciones:</strong> Constructor protegido; lo usan las subclases.</p>
     */
    protected Nodo(int linea, int columna) {
        this(linea, columna, TipoDato.DESCONOCIDO);
    }

    /**
     * <strong>Nombre:</strong> Nodo
     *
     * <p><strong>Objetivo:</strong> Crear un nodo en la posición dada con un tipo ya conocido.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, TipoDato tipo.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de Nodo.</p>
     *
     * <p><strong>Restricciones:</strong> Constructor protegido; lo usan las subclases.</p>
     */
    protected Nodo(int linea, int columna, TipoDato tipo) {
        this.linea = linea;
        this.columna = columna;
        this.tipo = tipo;
    }

    /**
     * <strong>Nombre:</strong> getLinea
     *
     * <p><strong>Objetivo:</strong> Indicar la línea del código fuente donde aparece el nodo.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> int con el número de línea.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public int getLinea() {
        return linea;
    }

    /**
     * <strong>Nombre:</strong> getColumna
     *
     * <p><strong>Objetivo:</strong> Indicar la columna del código fuente donde aparece el nodo.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> int con el número de columna.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public int getColumna() {
        return columna;
    }

    /**
     * <strong>Nombre:</strong> getTipo
     *
     * <p><strong>Objetivo:</strong> Obtener el tipo de dato del nodo.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> TipoDato del nodo.</p>
     *
     * <p><strong>Restricciones:</strong> El valor puede ser DESCONOCIDO hasta que el análisis semántico lo asigne.</p>
     */
    public TipoDato getTipo() {
        return tipo;
    }

    /**
     * <strong>Nombre:</strong> setTipo
     *
     * <p><strong>Objetivo:</strong> Asignar el tipo de dato del nodo.</p>
     *
     * <p><strong>Entrada:</strong> TipoDato tipo.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Lo usa el análisis semántico al determinar el tipo.</p>
     */
    public void setTipo(TipoDato tipo) {
        this.tipo = tipo;
    }
}
