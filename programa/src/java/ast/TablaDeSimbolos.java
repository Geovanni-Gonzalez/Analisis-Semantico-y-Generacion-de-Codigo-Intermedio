package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class TablaDeSimbolos {
    private final Stack<HashMap<String, Simbolo>> alcances;
    private final List<String> erroresSemanticos;
    private final Set<String> variablesNoDeclaradasReportadas;
    private final Set<String> funcionesNoDeclaradasReportadas;

    public TablaDeSimbolos() {
        this.alcances = new Stack<>();
        this.erroresSemanticos = new ArrayList<>();
        this.variablesNoDeclaradasReportadas = new HashSet<>();
        this.funcionesNoDeclaradasReportadas = new HashSet<>();
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
            reportarRedeclaracion(nombre, simbolo.getLinea());
            return;
        }

        alcances.peek().put(nombre, simbolo);
    }

    public void reportarTipoDeclaracionInvalido(TipoDato tipo, int linea) {
        erroresSemanticos.add("Error semántico [línea " + linea
                + "]: tipo declarado inválido '" + tipo + "'.");
    }

    public void reportarAsignacionIncompatible(TipoDato tipoOrigen, TipoDato tipoDestino, int linea) {
        erroresSemanticos.add("Error semántico [línea " + linea
                + "]: no se puede asignar tipo " + tipoOrigen
                + " a variable de tipo " + tipoDestino + ".");
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

        if (variablesNoDeclaradasReportadas.add(nombre)) {
            reportarVariableNoDeclarada(nombre, linea);
        }
        Simbolo simboloError = new Simbolo(nombre, TipoDato.ERROR, CategoriaSimb.VAR, linea);
        insertarSimboloError(simboloError);
        return simboloError;
    }

    public Simbolo buscarFuncion(String nombre, int linea) {
        for (int i = alcances.size() - 1; i >= 0; i--) {
            Simbolo simbolo = alcances.get(i).get(nombre);
            if (simbolo != null && simbolo.getCategoria() == CategoriaSimb.FUNCION) {
                return simbolo;
            }
        }

        if (funcionesNoDeclaradasReportadas.add(nombre)) {
            reportarFuncionNoDeclarada(nombre, linea);
        }
        return new Simbolo(nombre, TipoDato.ERROR, CategoriaSimb.FUNCION, linea);
    }

    public void actualizarFirmaFuncion(String nombre, List<TipoDato> tiposParametros,
                                       TipoDato tipoRetorno, int linea) {
        for (int i = alcances.size() - 1; i >= 0; i--) {
            HashMap<String, Simbolo> alcance = alcances.get(i);
            Simbolo simbolo = alcance.get(nombre);
            if (simbolo != null && simbolo.getCategoria() == CategoriaSimb.FUNCION
                    && simbolo.getLinea() == linea) {
                alcance.put(nombre, new Simbolo(nombre, tiposParametros, tipoRetorno, simbolo.getLinea()));
                return;
            }
        }
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

    public void reportarMainObligatorio() {
        erroresSemanticos.add("Error semantico: el programa debe contener exactamente un método main");
    }

    public void reportarAsignacionIncompatible(String nombre, TipoDato esperado, TipoDato recibido, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: asignacion incompatible para '" + nombre + "'" + ubicacion
                + ". Se esperaba " + esperado + " y se obtuvo " + recibido);
    }

    public void reportarOperacionIncompatible(String operador, TipoDato izquierda, TipoDato derecha, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: tipos incompatibles para operador '" + operador + "'"
                + ubicacion + ". Se obtuvo " + izquierda + " y " + derecha);
    }

    public void reportarOperacionIncompatible(String operador, TipoDato operando, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: tipo incompatible para operador '" + operador + "'"
                + ubicacion + ". Se obtuvo " + operando);
    }

    public void reportarCondicionNoBooleana(TipoDato recibido, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: condicion debe ser de tipo bool" + ubicacion
                + ", se encontro " + recibido);
    }

    public void reportarReturnSinValor(TipoDato esperado, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: funcion de tipo " + esperado
                + " debe retornar un valor" + ubicacion);
    }

    public void reportarReturnConValorEnVoid(int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: funcion void no puede retornar un valor" + ubicacion);
    }

    public void reportarReturnTipoIncompatible(TipoDato esperado, TipoDato encontrado, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: tipo incompatible en return" + ubicacion
                + ". Se esperaba " + esperado + " y se obtuvo " + encontrado);
    }

    public void reportarCantidadArgumentosIncorrecta(int esperados, int encontrados, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: se esperaban " + esperados
                + " argumentos, se encontraron " + encontrados + ubicacion);
    }

    public void reportarTipoArgumentoIncorrecto(int argumento, TipoDato esperado, TipoDato encontrado, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: argumento " + argumento + ": se esperaba tipo "
                + esperado + ", se encontró " + encontrado + ubicacion);
    }

    private void reportarVariableNoDeclarada(String nombre, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: variable no declarada '" + nombre + "'" + ubicacion);
    }

    private void reportarFuncionNoDeclarada(String nombre, int linea) {
        String ubicacion = linea > 0 ? " en linea " + linea : "";
        erroresSemanticos.add("Error semantico: función '" + nombre + "' no declarada" + ubicacion);
    }

    private void reportarRedeclaracion(String nombre, int linea) {
        erroresSemanticos.add("Error semántico [línea " + linea
                + "]: '" + nombre + "' ya está declarado en este alcance.");
    }

    private void insertarSimboloError(Simbolo simbolo) {
        if (alcances.isEmpty()) {
            abrirAlcance();
        }
        alcances.peek().putIfAbsent(simbolo.getNombre(), simbolo);
    }
}
