package ast;

/**
 * Expresion que representa el uso de un identificador escalar.
 */
public class IdentificadorNodo extends ExpresionNodo {
    private final String nombre;
    /**
     * Nombre : IdentificadorNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String nombre
     * Salida: Instancia inicializada de IdentificadorNodo.
     */
    public IdentificadorNodo(int linea, int columna, String nombre) {
        super(linea, columna);
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
