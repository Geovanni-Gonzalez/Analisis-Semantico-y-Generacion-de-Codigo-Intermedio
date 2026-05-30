package ast;

import java.util.HashMap;
import java.util.Stack;

public class TablaDeSimbolos {
    private final Stack<HashMap<String, Simbolo>> alcances;

    public TablaDeSimbolos() {
        this.alcances = new Stack<>();
        abrirAlcance();
    }

    public void abrirAlcance() {
        alcances.push(new HashMap<>());
    }

    public void cerrarAlcance() {
        if (alcances.isEmpty()) {
            throw new IllegalStateException("No hay alcances abiertos para cerrar.");
        }
        alcances.pop();
    }

    public void insertar(Simbolo simbolo) {
        if (alcances.isEmpty()) {
            throw new IllegalStateException("No hay un alcance abierto para insertar simbolos.");
        }

        String nombre = simbolo.getNombre();
        if (existeEnAlcanceActual(nombre)) {
            throw new IllegalArgumentException(
                    "El simbolo '" + nombre + "' ya existe en el alcance actual.");
        }

        alcances.peek().put(nombre, simbolo);
    }

    public Simbolo buscar(String nombre) {
        for (int i = alcances.size() - 1; i >= 0; i--) {
            Simbolo simbolo = alcances.get(i).get(nombre);
            if (simbolo != null) {
                return simbolo;
            }
        }
        return null;
    }

    public boolean existeEnAlcanceActual(String nombre) {
        if (alcances.isEmpty()) {
            return false;
        }
        return alcances.peek().containsKey(nombre);
    }
}
