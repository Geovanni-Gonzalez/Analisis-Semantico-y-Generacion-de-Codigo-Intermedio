package ast;

/**
 * <strong>Nombre:</strong> SalidaNodo
 *
 * <p><strong>Objetivo:</strong> Representar la sentencia de salida ({@code cout}): imprimir el
 * valor de una expresión.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y la expresión a imprimir.</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena la expresión a imprimir.</p>
 */
public class SalidaNodo extends SentenciaNodo {
    private final ExpresionNodo valor;

    /**
     * <strong>Nombre:</strong> SalidaNodo
     *
     * <p><strong>Objetivo:</strong> Crear la sentencia de salida con la expresión a imprimir.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, ExpresionNodo valor.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de SalidaNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public SalidaNodo(int linea, int columna, ExpresionNodo valor) {
        super(linea, columna);
        this.valor = valor;
    }

    /**
     * <strong>Nombre:</strong> getValor
     *
     * <p><strong>Objetivo:</strong> Devolver la expresión cuyo valor se imprime.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo a imprimir.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getValor() {
        return valor;
    }
}
