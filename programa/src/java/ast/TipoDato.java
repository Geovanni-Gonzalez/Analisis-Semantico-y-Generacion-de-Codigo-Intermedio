package ast;

/**
 * Tipos reconocidos por el lenguaje y por las fases del compilador.
 *
 * <p>{@code ERROR} y {@code DESCONOCIDO} no son tipos declarables; se usan para
 * recuperacion y para evitar cascadas de errores durante el analisis.</p>
 */
public enum TipoDato {
    INT,
    FLOAT,
    BOOL,
    CHAR,
    STRING,
    VOID,
    ERROR,
    EMPTY,
    DESCONOCIDO;
    /**
     * Nombre : esNumerico.
     * Descripcion: Consulta una condicion booleana del objeto.
     * Entrada: Sin parametros.
     * Salida: Retorna boolean.
     */
    public boolean esNumerico() {
        return this == INT || this == FLOAT;
    }

    /**
     * Nombre : esDeclarableVariable.
     * Descripcion: Consulta una condicion booleana del objeto.
     * Entrada: Sin parametros.
     * Salida: Retorna boolean.
     */
    public boolean esDeclarableVariable() {
        return this == INT || this == FLOAT || this == BOOL || this == CHAR || this == STRING;
    }

    /**
     * Nombre : esCompatibleCon.
     * Descripcion: Consulta una condicion booleana del objeto.
     * Entrada: TipoDato otro
     * Salida: Retorna boolean.
     */
    public boolean esCompatibleCon(TipoDato otro) {
        if (this == ERROR || otro == ERROR) {
            return true;
        }

        if (this == otro) {
            return true;
        }

        return esNumerico() && otro.esNumerico();
    }

    /** Imprime el tipo como texto del lenguaje en minuscula. */
    @Override
    /**
     * Nombre : toString.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String toString() {
        return name().toLowerCase();
    }
}
