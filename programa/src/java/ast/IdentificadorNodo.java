package ast;

/**
 * <strong>Nombre:</strong> IdentificadorNodo
 *
 * <p><strong>Objetivo:</strong> Representar el uso de un identificador escalar (una variable)
 * dentro del código, por ejemplo {@code x} en {@code x + 1}.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y el nombre de la variable.</p>
 *
 * <p><strong>Salida:</strong> Nodo de expresión consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena el nombre; no resuelve la variable.</p>
 */
public class IdentificadorNodo extends ExpresionNodo {
    private final String nombre;

    /**
     * <strong>Nombre:</strong> IdentificadorNodo
     *
     * <p><strong>Objetivo:</strong> Crear el nodo a partir del nombre de la variable referenciada.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String nombre.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de IdentificadorNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public IdentificadorNodo(int linea, int columna, String nombre) {
        super(linea, columna);
        this.nombre = nombre;
    }

    /**
     * <strong>Nombre:</strong> getNombre
     *
     * <p><strong>Objetivo:</strong> Devolver el nombre de la variable referenciada.</p>
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
}
