package ast;

/**
 * Sentencia que declara una variable escalar o un arreglo.
 *
 * <p>Para escalares se usa {@code inicializador}; para arreglos se usan
 * {@code filas}, {@code columnas} e {@code inicializacionArreglo}. Esta clase
 * solo modela la estructura; la validez de tipos se revisa en semantica.</p>
 */
public class DeclaracionVariableNodo extends SentenciaNodo {
    private final String nombre;
    private final ExpresionNodo inicializador;
    private final ExpresionNodo filas;
    private final ExpresionNodo columnas;
    private final InicializacionArregloNodo inicializacionArreglo;
    /**
     * Nombre : DeclaracionVariableNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String nombre, TipoDato tipoDeclarado, ExpresionNodo inicializador
     * Salida: Instancia inicializada de DeclaracionVariableNodo.
     */
    public DeclaracionVariableNodo(int linea, int columna, String nombre, TipoDato tipoDeclarado,
                                   ExpresionNodo inicializador) {
        this(linea, columna, nombre, tipoDeclarado, inicializador, null, null, null);
    }
    /**
     * Nombre : DeclaracionVariableNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String nombre, TipoDato tipoDeclarado, ExpresionNodo filas, ExpresionNodo columnas, InicializacionArregloNodo inicializacionArreglo
     * Salida: Instancia inicializada de DeclaracionVariableNodo.
     */
    public DeclaracionVariableNodo(int linea, int columna, String nombre, TipoDato tipoDeclarado,
                                   ExpresionNodo filas, ExpresionNodo columnas,
                                   InicializacionArregloNodo inicializacionArreglo) {
        this(linea, columna, nombre, tipoDeclarado, null, filas, columnas, inicializacionArreglo);
    }
    /**
     * Nombre : DeclaracionVariableNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String nombre, TipoDato tipoDeclarado, ExpresionNodo inicializador, ExpresionNodo filas, ExpresionNodo columnas, InicializacionArregloNodo inicializacionArreglo
     * Salida: Instancia inicializada de DeclaracionVariableNodo.
     */
    private DeclaracionVariableNodo(int linea, int columna, String nombre, TipoDato tipoDeclarado,
                                    ExpresionNodo inicializador, ExpresionNodo filas,
                                    ExpresionNodo columnas,
                                    InicializacionArregloNodo inicializacionArreglo) {
        super(linea, columna, tipoDeclarado);
        this.nombre = nombre;
        this.inicializador = inicializador;
        this.filas = filas;
        this.columnas = columnas;
        this.inicializacionArreglo = inicializacionArreglo;
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

    /**
     * Nombre : getInicializador.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getInicializador() {
        return inicializador;
    }

    /**
     * Nombre : getFilas.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getFilas() {
        return filas;
    }

    /**
     * Nombre : getColumnas.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna ExpresionNodo.
     */
    public ExpresionNodo getColumnas() {
        return columnas;
    }

    /**
     * Nombre : getInicializacionArreglo.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna InicializacionArregloNodo.
     */
    public InicializacionArregloNodo getInicializacionArreglo() {
        return inicializacionArreglo;
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
