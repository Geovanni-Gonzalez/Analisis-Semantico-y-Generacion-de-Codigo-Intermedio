package ast;

/**
 * Sentencia de entrada del lenguaje, equivalente a leer hacia una variable.
 */
public class EntradaNodo extends SentenciaNodo {
    private final String destino;
    /**
     * Nombre : EntradaNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String destino
     * Salida: Instancia inicializada de EntradaNodo.
     */
    public EntradaNodo(int linea, int columna, String destino) {
        super(linea, columna);
        this.destino = destino;
    }

    /**
     * Nombre : getDestino.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String getDestino() {
        return destino;
    }
}
