package ast;

/**
 * <strong>Nombre:</strong> ReturnNodo
 *
 * <p><strong>Objetivo:</strong> Representar la sentencia {@code return} de una función.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y la expresión a devolver (o {@code null}).</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> El valor es {@code null} cuando se retorna sin expresión (void).</p>
 */
public class ReturnNodo extends SentenciaNodo {
    private final ExpresionNodo valor;

    /**
     * <strong>Nombre:</strong> ReturnNodo
     *
     * <p><strong>Objetivo:</strong> Crear el return con la expresión a devolver, o {@code null} si no hay valor.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, ExpresionNodo valor.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de ReturnNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ReturnNodo(int linea, int columna, ExpresionNodo valor) {
        super(linea, columna);
        this.valor = valor;
    }

    /**
     * <strong>Nombre:</strong> getValor
     *
     * <p><strong>Objetivo:</strong> Devolver la expresión que se retorna, o {@code null} en un return sin valor.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del valor, o {@code null}.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getValor() {
        return valor;
    }
}
