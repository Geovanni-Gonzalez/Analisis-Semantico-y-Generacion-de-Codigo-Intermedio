package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class TablaDeSimbolos {
    private final Stack<HashMap<String, Simbolo>> alcances;
    private final List<String> erroresSemanticos;

    public TablaDeSimbolos() {
        this.alcances = new Stack<>();
        this.erroresSemanticos = new ArrayList<>();
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
        return buscar(nombre, -1);
    }

    public Simbolo buscar(String nombre, int linea) {
        for (int i = alcances.size() - 1; i >= 0; i--) {
            Simbolo simbolo = alcances.get(i).get(nombre);
            if (simbolo != null) {
                return simbolo;
            }
        }

        reportarVariableNoDeclarada(nombre, linea);
        Simbolo simboloError = new Simbolo(nombre, TipoDato.ERROR, CategoriaSimb.VAR, linea);
        insertarSimboloError(simboloError);
        return simboloError;
    }

    public boolean existeEnAlcanceActual(String nombre) {
        if (alcances.isEmpty()) {
            return false;
        }
        return alcances.peek().containsKey(nombre);
    }

    public List<String> getErroresSemanticos() {
        return Collections.unmodifiableList(erroresSemanticos);
    }

    private void reportarVariableNoDeclarada(String nombre, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: variable no declarada '" + nombre + "'" + ubicacion);
    }

    private void insertarSimboloError(Simbolo simbolo) {
        if (alcances.isEmpty()) {
            abrirAlcance();
        }
        alcances.peek().putIfAbsent(simbolo.getNombre(), simbolo);
    }
}
