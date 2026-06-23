package ast;

/**
 * <strong>Nombre:</strong> ParametroNodo
 *
 * <p><strong>Objetivo:</strong> Representar un parámetro formal de la firma de una función,
 * por ejemplo {@code int ~ a} en {@code sumar<|int ~ a, int ~ b|>}.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, el nombre y el tipo declarado.</p>
 *
 * <p><strong>Salida:</strong> Nodo consultable por las fases semántica e intermedia.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena la estructura del parámetro.</p>
 */
public class ParametroNodo extends Nodo {
    private final String nombre;

    /**
     * <strong>Nombre:</strong> ParametroNodo
     *
     * <p><strong>Objetivo:</strong> Crear el parámetro con su nombre y su tipo declarado.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String nombre, TipoDato tipo.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de ParametroNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ParametroNodo(int linea, int columna, String nombre, TipoDato tipo) {
        super(linea, columna, tipo);
        this.nombre = nombre;
    }

    /**
     * <strong>Nombre:</strong> getNombre
     *
     * <p><strong>Objetivo:</strong> Devolver el nombre del parámetro tal como se usa dentro de la función.</p>
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
