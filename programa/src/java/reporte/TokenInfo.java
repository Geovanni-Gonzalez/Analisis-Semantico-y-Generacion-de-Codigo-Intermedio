package reporte;

/**
 * Registro simple con la informacion que el lexer envia a los reportes.
 *
 * <p>Los campos son publicos y finales para facilitar la escritura tabular sin
 * mutaciones despues de crear el token.</p>
 */
public class TokenInfo {
    /** Identificador numerico del token segun la tabla generada por CUP. */
    public final int id;
    /** Nombre simbolico del token. */
    public final String nombre;
    /** Texto original reconocido en el codigo fuente. */
    public final String lexema;
    /** Linea donde inicia el lexema. */
    public final int linea;
    /** Columna donde inicia el lexema. */
    public final int columna;
    /** Tabla o categoria de reporte a la que pertenece el token. */
    public final String tabla;
    /** Detalle adicional usado por la tabla de simbolos o literales. */
    public final String informacion;

    /**
     * Nombre : TokenInfo.
     * Descripcion: Crea una fila inmutable para los reportes de tokens.
     * Entrada: int id, String nombre, String lexema, int linea, int columna, String tabla, String informacion
     * Salida: Instancia inicializada de TokenInfo.
     */
    public TokenInfo(int id, String nombre, String lexema, int linea, int columna,
                     String tabla, String informacion) {
        this.id = id;
        this.nombre = nombre;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
        this.tabla = tabla;
        this.informacion = informacion;
    }
}
