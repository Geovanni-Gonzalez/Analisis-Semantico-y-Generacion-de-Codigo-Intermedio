package ast;

/**
 * <strong>Nombre:</strong> EntradaNodo
 *
 * <p><strong>Objetivo:</strong> Representar la sentencia de entrada ({@code cin}): leer un valor
 * desde el usuario y guardarlo en la variable destino.</p>
 *
 * <p><strong>Entrada:</strong> Posición en el fuente y el nombre de la variable destino.</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena el nombre del destino.</p>
 */
public class EntradaNodo extends SentenciaNodo {
    private final String destino;

    /**
     * <strong>Nombre:</strong> EntradaNodo
     *
     * <p><strong>Objetivo:</strong> Crear la sentencia de entrada con el nombre de la variable que recibe el dato.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String destino.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de EntradaNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public EntradaNodo(int linea, int columna, String destino) {
        super(linea, columna);
        this.destino = destino;
    }

    /**
     * <strong>Nombre:</strong> getDestino
     *
     * <p><strong>Objetivo:</strong> Devolver el nombre de la variable donde se almacena el valor leído.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el nombre del destino.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public String getDestino() {
        return destino;
    }
}
