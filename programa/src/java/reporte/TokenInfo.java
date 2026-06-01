package reporte;

public class TokenInfo {
    public final int id;
    public final String nombre;
    public final String lexema;
    public final int linea;
    public final int columna;
    public final String tabla;
    public final String informacion;

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
