package ast;

/**
 * <strong>Nombre:</strong> LiteralNodo
 *
 * <p><strong>Objetivo:</strong> Representar un literal escrito en el código fuente
 * (un número, una cadena, un carácter o un booleano).</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente, el valor ya convertido y su tipo de dato.</p>
 *
 * <p><strong>Salida:</strong> Nodo de expresión hoja consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena el valor; no lo interpreta.</p>
 */
public class LiteralNodo extends ExpresionNodo {
    private final Object valor;

    /**
     * <strong>Nombre:</strong> LiteralNodo
     *
     * <p><strong>Objetivo:</strong> Crear el literal con su valor ya convertido y su tipo de dato.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, Object valor, TipoDato tipo.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de LiteralNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public LiteralNodo(int linea, int columna, Object valor, TipoDato tipo) {
        super(linea, columna, tipo);
        this.valor = valor;
    }

    /**
     * <strong>Nombre:</strong> getValor
     *
     * <p><strong>Objetivo:</strong> Devolver el valor del literal.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> Object con el valor (Integer, Float, String, etc.).</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public Object getValor() {
        return valor;
    }
}
