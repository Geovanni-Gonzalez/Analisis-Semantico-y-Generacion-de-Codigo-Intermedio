package ast;

/**
 * Parametro formal declarado en la firma de una funcion.
 */
public class ParametroNodo extends Nodo {
    private final String nombre;
    /**
     * Nombre : ParametroNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String nombre, TipoDato tipo
     * Salida: Instancia inicializada de ParametroNodo.
     */
    public ParametroNodo(int linea, int columna, String nombre, TipoDato tipo) {
        super(linea, columna, tipo);
        this.nombre = nombre;
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
}
