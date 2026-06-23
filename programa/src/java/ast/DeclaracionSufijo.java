package ast;

/**
 * <strong>Nombre:</strong> DeclaracionSufijo
 *
 * <p><strong>Objetivo:</strong> Estructura auxiliar que usa la gramática para guardar la parte
 * que va después del nombre en una declaración: el inicializador de un escalar, o las
 * dimensiones e inicialización de un arreglo.</p>
 *
 * <p><strong>Entrada:</strong> Un inicializador, o bien las dimensiones y la inicialización de un arreglo.</p>
 *
 * <p><strong>Salida:</strong> Objeto que la regla sintáctica usa para construir el {@link DeclaracionVariableNodo} adecuado.</p>
 *
 * <p><strong>Restricciones:</strong> Solo se usa uno de los dos modos (escalar o arreglo) a la vez.</p>
 */
public class DeclaracionSufijo {
    public final ExpresionNodo inicializador;
    public final ExpresionNodo filas;
    public final ExpresionNodo columnas;
    public final InicializacionArregloNodo inicializacionArreglo;

    /**
     * <strong>Nombre:</strong> DeclaracionSufijo
     *
     * <p><strong>Objetivo:</strong> Crear el sufijo de una variable escalar: solo su inicializador.</p>
     *
     * <p><strong>Entrada:</strong> ExpresionNodo inicializador.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de DeclaracionSufijo (modo escalar).</p>
     *
     * <p><strong>Restricciones:</strong> Deja en {@code null} las dimensiones del arreglo.</p>
     */
    public DeclaracionSufijo(ExpresionNodo inicializador) {
        this.inicializador = inicializador;
        this.filas = null;
        this.columnas = null;
        this.inicializacionArreglo = null;
    }

    /**
     * <strong>Nombre:</strong> DeclaracionSufijo
     *
     * <p><strong>Objetivo:</strong> Crear el sufijo de un arreglo: sus dimensiones y su inicialización opcional.</p>
     *
     * <p><strong>Entrada:</strong> ExpresionNodo filas, ExpresionNodo columnas, InicializacionArregloNodo inicializacionArreglo.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de DeclaracionSufijo (modo arreglo).</p>
     *
     * <p><strong>Restricciones:</strong> Deja en {@code null} el inicializador escalar.</p>
     */
    public DeclaracionSufijo(ExpresionNodo filas, ExpresionNodo columnas,
                             InicializacionArregloNodo inicializacionArreglo) {
        this.inicializador = null;
        this.filas = filas;
        this.columnas = columnas;
        this.inicializacionArreglo = inicializacionArreglo;
    }

    /**
     * <strong>Nombre:</strong> esArreglo
     *
     * <p><strong>Objetivo:</strong> Indicar si el sufijo describe un arreglo (tiene dimensiones).</p>
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
