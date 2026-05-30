package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProgramaNodo extends Nodo {
    private final List<FuncionNodo> funciones;

    public ProgramaNodo(int linea, int columna, List<FuncionNodo> funciones) {
        super(linea, columna, TipoDato.EMPTY);
        this.funciones = new ArrayList<>(funciones);
    }

    public List<FuncionNodo> getFunciones() {
        return Collections.unmodifiableList(funciones);
    }
}
