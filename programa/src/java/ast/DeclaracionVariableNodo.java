package ast;

/**
 * <strong>Nombre:</strong> DeclaracionVariableNodo
 *
 * <p><strong>Objetivo:</strong> Representar la declaración de una variable escalar o de un arreglo.
 * Para un escalar lleva un inicializador opcional; para un arreglo lleva sus dimensiones y una
 * posible inicialización con valores.</p>
 *
 * <p><strong>Entrada:</strong> Posición, nombre, tipo y, según el caso, inicializador o dimensiones.</p>
 *
 * <p><strong>Salida:</strong> Nodo de sentencia consultable por las fases posteriores.</p>
 *
 * <p><strong>Restricciones:</strong> Solo se usa el modo escalar o el modo arreglo, no ambos.</p>
 */
public class DeclaracionVariableNodo extends SentenciaNodo {
    private final String nombre;
    private final ExpresionNodo inicializador;
    private final ExpresionNodo filas;
    private final ExpresionNodo columnas;
    private final InicializacionArregloNodo inicializacionArreglo;

    /**
     * <strong>Nombre:</strong> DeclaracionVariableNodo
     *
     * <p><strong>Objetivo:</strong> Declarar una variable escalar con un inicializador opcional.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String nombre, TipoDato tipoDeclarado, ExpresionNodo inicializador.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de DeclaracionVariableNodo (escalar).</p>
     *
     * <p><strong>Restricciones:</strong> El inicializador es {@code null} si no se inicializa.</p>
     */
    public DeclaracionVariableNodo(int linea, int columna, String nombre, TipoDato tipoDeclarado,
                                   ExpresionNodo inicializador) {
        this(linea, columna, nombre, tipoDeclarado, inicializador, null, null, null);
    }

    /**
     * <strong>Nombre:</strong> DeclaracionVariableNodo
     *
     * <p><strong>Objetivo:</strong> Declarar un arreglo con sus dimensiones y una inicialización opcional.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String nombre, TipoDato tipoDeclarado, ExpresionNodo filas, ExpresionNodo columnas, InicializacionArregloNodo inicializacionArreglo.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de DeclaracionVariableNodo (arreglo).</p>
     *
     * <p><strong>Restricciones:</strong> La inicialización del arreglo es {@code null} si no la hay.</p>
     */
    public DeclaracionVariableNodo(int linea, int columna, String nombre, TipoDato tipoDeclarado,
                                   ExpresionNodo filas, ExpresionNodo columnas,
                                   InicializacionArregloNodo inicializacionArreglo) {
        this(linea, columna, nombre, tipoDeclarado, null, filas, columnas, inicializacionArreglo);
    }

    /**
     * <strong>Nombre:</strong> DeclaracionVariableNodo
     *
     * <p><strong>Objetivo:</strong> Constructor base que reúne todos los campos posibles de una declaración.</p>
     *
     * <p><strong>Entrada:</strong> Posición, nombre, tipo, inicializador, filas, columnas e inicialización de arreglo.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de DeclaracionVariableNodo.</p>
     *
     * <p><strong>Restricciones:</strong> Es privado; lo usan los dos constructores públicos.</p>
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
     * <strong>Nombre:</strong> getNombre
     *
     * <p><strong>Objetivo:</strong> Devolver el nombre de la variable declarada.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el nombre.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * <strong>Nombre:</strong> getInicializador
     *
     * <p><strong>Objetivo:</strong> Devolver la expresión que da el valor inicial de un escalar.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo del inicializador, o {@code null} si no se inicializa.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getInicializador() {
        return inicializador;
    }

    /**
     * <strong>Nombre:</strong> getFilas
     *
     * <p><strong>Objetivo:</strong> Devolver el número de filas si es un arreglo.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo de las filas, o {@code null} si es escalar.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getFilas() {
        return filas;
    }

    /**
     * <strong>Nombre:</strong> getColumnas
     *
     * <p><strong>Objetivo:</strong> Devolver el número de columnas si es un arreglo.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> ExpresionNodo de las columnas, o {@code null} si es escalar.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public ExpresionNodo getColumnas() {
        return columnas;
    }

    /**
     * <strong>Nombre:</strong> getInicializacionArreglo
     *
     * <p><strong>Objetivo:</strong> Devolver los valores iniciales del arreglo.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> InicializacionArregloNodo, o {@code null} si no se inicializa.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public InicializacionArregloNodo getInicializacionArreglo() {
        return inicializacionArreglo;
    }

    /**
     * <strong>Nombre:</strong> esArreglo
     *
     * <p><strong>Objetivo:</strong> Indicar si la declaración corresponde a un arreglo (tiene dimensiones).</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> boolean; true si hay filas o columnas.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public boolean esArreglo() {
        return filas != null || columnas != null;
    }
}
