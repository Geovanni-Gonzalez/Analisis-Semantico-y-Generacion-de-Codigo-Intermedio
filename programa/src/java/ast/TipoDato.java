package ast;

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

    public boolean esNumerico() {
        return this == INT || this == FLOAT;
    }

    public boolean esCompatibleCon(TipoDato otro) {
        if (this == ERROR || otro == ERROR) {
            return true;
        }

        if (this == otro) {
            return true;
        }

        return esNumerico() && otro.esNumerico();
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
