package ast;

/**
 * <strong>Nombre:</strong> TipoDato
 *
 * <p><strong>Objetivo:</strong> Enumerar los tipos de dato que reconocen el lenguaje y
 * las fases del compilador, y ofrecer comprobaciones de compatibilidad entre ellos.</p>
 *
 * <p><strong>Entrada:</strong> Ninguna; son constantes fijas del enum.</p>
 *
 * <p><strong>Salida:</strong> Valores de tipo usados por el AST y el análisis semántico.</p>
 *
 * <p><strong>Restricciones:</strong> ERROR marca expresiones ya inválidas, EMPTY es el tipo del
 * procedimiento principal y DESCONOCIDO es el valor previo a determinar el tipo.</p>
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
     * <strong>Nombre:</strong> esNumerico
     *
     * <p><strong>Objetivo:</strong> Indicar si el tipo es numérico (int o float).</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> boolean; true si es int o float.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public boolean esNumerico() {
        return this == INT || this == FLOAT;
    }

    /**
     * <strong>Nombre:</strong> esDeclarableVariable
     *
     * <p><strong>Objetivo:</strong> Indicar si el tipo puede usarse al declarar una variable.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> boolean; true para int, float, bool, char o string.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public boolean esDeclarableVariable() {
        return this == INT || this == FLOAT || this == BOOL || this == CHAR || this == STRING;
    }

    /**
     * <strong>Nombre:</strong> esCompatibleCon
     *
     * <p><strong>Objetivo:</strong> Indicar si este tipo es compatible con otro en una operación o asignación.</p>
     *
     * <p><strong>Entrada:</strong> TipoDato otro.</p>
     *
     * <p><strong>Salida:</strong> boolean; true si son compatibles.</p>
     *
     * <p><strong>Restricciones:</strong> ERROR es compatible con todo (frena la cascada de errores);
     * dos tipos numéricos se consideran compatibles entre sí.</p>
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

    /**
     * <strong>Nombre:</strong> toString
     *
     * <p><strong>Objetivo:</strong> Devolver el nombre del tipo en minúscula, tal como se escribe en el lenguaje.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el nombre del tipo en minúscula.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
