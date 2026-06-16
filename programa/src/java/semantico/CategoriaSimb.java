package semantico;

/**
 * Clasifica el rol de un simbolo dentro de la tabla de simbolos.
 */
public enum CategoriaSimb {
    /** Variable escalar declarada en un alcance. */
    VAR,
    /** Arreglo bidimensional declarado en un alcance. */
    ARREGLO,
    /** Parametro formal de una funcion. */
    PARAMETRO,
    /** Funcion de nivel superior. */
    FUNCION
}
