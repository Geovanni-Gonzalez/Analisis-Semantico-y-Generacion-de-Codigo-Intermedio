package intermedio;

public class Instruccion {
    public final Operacion op;
    public final String resultado;
    public final String op1;
    public final String op2;

    public Instruccion(Operacion op, String resultado, String op1, String op2) {
        this.op = op;
        this.resultado = resultado;
        this.op1 = op1;
        this.op2 = op2;
    }

    public Instruccion(Operacion op, String resultado, String op1) {
        this(op, resultado, op1, null);
    }

    public Instruccion(Operacion op, String resultado) {
        this(op, resultado, null, null);
    }

    public Operacion getOp() {
        return op;
    }

    public String getResultado() {
        return resultado;
    }

    public String getOp1() {
        return op1;
    }

    public String getOp2() {
        return op2;
    }

    @Override
    public String toString() {
        switch (op) {
            case ASIG:
                return resultado + " = " + op1;
            case SUMA:
            case RESTA:
            case MULT:
            case DIV:
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
            case RETURN:
                return op1 == null ? "return" : "return " + op1;
            case LABEL:
                return resultado + ":";
            case INICIO_FUNC:
                return "inicio_func " + resultado;
            case FIN_FUNC:
                return resultado == null ? "fin_func" : "fin_func " + resultado;
            default:
                throw new IllegalStateException("Operacion no soportada: " + op);
        }
    }

    private String operandoUnico() {
        return op1 != null ? op1 : resultado;
    }

    private String formatearCall() {
        String llamada = op2 == null ? "call " + op1 : "call " + op1 + ", " + op2;
        return resultado == null ? llamada : resultado + " = " + llamada;
    }

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
