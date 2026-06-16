package intermedio;

/**
 * Instruccion atomica del codigo intermedio.
 *
 * <p>La clase mantiene una estructura simple de hasta tres operandos y concentra
 * el formateo textual del `.ic`, lo que evita duplicar cadenas en el generador
 * y en las pruebas.</p>
 */
public class Instruccion {
    public final Operacion op;
    public final String resultado;
    public final String op1;
    public final String op2;

    /**
     * Nombre : Instruccion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Operacion op, String resultado, String op1, String op2
     * Salida: Instancia inicializada de Instruccion.
     */
    public Instruccion(Operacion op, String resultado, String op1, String op2) {
        this.op = op;
        this.resultado = resultado;
        this.op1 = op1;
        this.op2 = op2;
    }

    /**
     * Nombre : Instruccion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Operacion op, String resultado, String op1
     * Salida: Instancia inicializada de Instruccion.
     */
    public Instruccion(Operacion op, String resultado, String op1) {
        this(op, resultado, op1, null);
    }

    /**
     * Nombre : Instruccion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Operacion op, String resultado
     * Salida: Instancia inicializada de Instruccion.
     */
    public Instruccion(Operacion op, String resultado) {
        this(op, resultado, null, null);
    }

    /**
     * Nombre : getOp.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna Operacion.
     */
    public Operacion getOp() {
        return op;
    }

    /**
     * Nombre : getResultado.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * Nombre : getOp1.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String getOp1() {
        return op1;
    }

    /**
     * Nombre : getOp2.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String getOp2() {
        return op2;
    }

    /** Formatea la instruccion como una linea de codigo intermedio. */
    @Override
    /**
     * Nombre : toString.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String toString() {
        switch (op) {
            case ASIG:
                return resultado + " = " + op1;
            case SUMA:
            case RESTA:
            case MULT:
            case DIV:
            case MOD:
            case POW:
            case AND:
            case OR:
            case IGUAL:
            case MENOR:
            case MAYOR:
            case MENOR_IGUAL:
            case MAYOR_IGUAL:
            case DISTINTO:
                return resultado + " = " + op1 + " " + simboloBinario(op) + " " + op2;
            case NEG:
            case NOT:
                return resultado + " = " + simboloUnario(op) + op1;
            case GOTO:
                return "goto " + resultado;
            case IF_FALSE:
                return "if_false " + op1 + " goto " + resultado;
            case PARAM:
                return "param " + operandoUnico();
            case CALL:
                return formatearCall();
            case PRINT:
                return "print " + operandoUnico();
            case RETURN:
                return op1 == null ? "return" : "return " + op1;
            case LABEL:
                return resultado + ":";
            case INICIO_FUNC:
                return "begin_function " + resultado;
            case FIN_FUNC:
                return resultado == null ? "end_function" : "end_function " + resultado;
            default:
                throw new IllegalStateException("Operacion no soportada: " + op);
        }
    }

    /**
     * Nombre : operandoUnico.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    private String operandoUnico() {
        return op1 != null ? op1 : resultado;
    }

    /**
     * Nombre : formatearCall.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    private String formatearCall() {
        String llamada = op2 == null ? "call " + op1 : "call " + op1 + ", " + op2;
        return resultado == null ? llamada : resultado + " = " + llamada;
    }

    /**
     * Nombre : simboloBinario.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Operacion op
     * Salida: Retorna String.
     */
    private static String simboloBinario(Operacion op) {
        switch (op) {
            case SUMA:
                return "+";
            case RESTA:
                return "-";
            case MULT:
                return "*";
            case DIV:
                return "/";
            case MOD:
                return "%";
            case POW:
                return "^";
            case AND:
                return "&&";
            case OR:
                return "||";
            case IGUAL:
                return "==";
            case MENOR:
                return "<";
            case MAYOR:
                return ">";
            case MENOR_IGUAL:
                return "<=";
            case MAYOR_IGUAL:
                return ">=";
            case DISTINTO:
                return "!=";
            default:
                throw new IllegalArgumentException("Operacion no binaria: " + op);
        }
    }

    /**
     * Nombre : simboloUnario.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Operacion op
     * Salida: Retorna String.
     */
    private static String simboloUnario(Operacion op) {
        switch (op) {
            case NEG:
                return "-";
            case NOT:
                return "!";
            default:
                throw new IllegalArgumentException("Operacion no unaria: " + op);
        }
    }
}
