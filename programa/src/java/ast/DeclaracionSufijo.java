package ast;

/**
 * Estructura auxiliar usada por la gramatica para conservar el sufijo de una
 * declaracion.
 *
 * <p>Permite representar con un solo objeto tanto una declaracion escalar con
 * inicializador opcional como una declaracion de arreglo con dimensiones e
 * inicializacion opcional.</p>
 */
public class DeclaracionSufijo {
    public final ExpresionNodo inicializador;
    public final ExpresionNodo filas;
    public final ExpresionNodo columnas;
    public final InicializacionArregloNodo inicializacionArreglo;
    /**
     * Nombre : DeclaracionSufijo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: ExpresionNodo inicializador
     * Salida: Instancia inicializada de DeclaracionSufijo.
     */
    public DeclaracionSufijo(ExpresionNodo inicializador) {
        this.inicializador = inicializador;
        this.filas = null;
        this.columnas = null;
        this.inicializacionArreglo = null;
    }

    /**
     * Nombre : DeclaracionSufijo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: ExpresionNodo filas, ExpresionNodo columnas, InicializacionArregloNodo inicializacionArreglo
     * Salida: Instancia inicializada de DeclaracionSufijo.
     */
    public DeclaracionSufijo(ExpresionNodo filas, ExpresionNodo columnas,
                             InicializacionArregloNodo inicializacionArreglo) {
        this.inicializador = null;
        this.filas = filas;
        this.columnas = columnas;
        this.inicializacionArreglo = inicializacionArreglo;
    }

    /**
     * Nombre : esArreglo.
     * Descripcion: Consulta una condicion booleana del objeto.
     * Entrada: Sin parametros.
     * Salida: Retorna boolean.
     */
    public boolean esArreglo() {
        return filas != null || columnas != null;
    }
}
