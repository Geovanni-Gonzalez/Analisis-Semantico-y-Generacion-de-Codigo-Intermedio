package semantico;

import ast.TipoDato;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import reporte.ReportadorErrores;

/**
 * Tabla de simbolos con alcances anidados y acumulacion de errores semanticos.
 *
 * <p>La cima de la pila representa el alcance actual. Las busquedas se hacen
 * desde el alcance mas interno hacia el global para respetar sombreado y
 * visibilidad. Cuando falta un simbolo, se registra un simbolo de error para
 * que el analisis pueda continuar sin reportes repetidos.</p>
 */
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
        reportar("tipo declarado invalido '" + tipo + "'", linea);
    }

    public void reportarAsignacionIncompatible(TipoDato tipoOrigen, TipoDato tipoDestino, int linea) {
        reportar("no se puede asignar tipo " + tipoOrigen
                + " a variable de tipo " + tipoDestino, linea);
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
        reportar("el programa debe contener exactamente un metodo main", 0);
    }

    public void reportarAsignacionIncompatible(String nombre, TipoDato esperado, TipoDato recibido, int linea) {
        reportar("asignacion incompatible para '" + nombre + "': se esperaba tipo "
                + esperado + " y se obtuvo tipo " + recibido, linea);
    }

    public void reportarOperacionIncompatible(String operador, TipoDato izquierda, TipoDato derecha, int linea) {
        reportar("tipos incompatibles para operador '" + operador
                + "': operando izquierdo de tipo " + izquierda
                + " y operando derecho de tipo " + derecha, linea);
    }

    public void reportarOperacionIncompatible(String operador, TipoDato operando, int linea) {
        reportar("tipo incompatible para operador '" + operador
                + "': se obtuvo tipo " + operando, linea);
    }

    public void reportarCondicionNoBooleana(TipoDato recibido, int linea) {
        reportar("la condicion debe ser de tipo bool, pero se encontro tipo " + recibido, linea);
    }

    public void reportarReturnSinValor(TipoDato esperado, int linea) {
        reportar("la funcion de tipo " + esperado + " debe retornar un valor", linea);
    }

    public void reportarReturnConValorEnVoid(int linea) {
        reportar("la funcion void no puede retornar un valor", linea);
    }

    public void reportarReturnTipoIncompatible(TipoDato esperado, TipoDato encontrado, int linea) {
        reportar("tipo incompatible en return: se esperaba tipo " + esperado
                + " y se obtuvo tipo " + encontrado, linea);
    }

    public void reportarCantidadArgumentosIncorrecta(int esperados, int encontrados, int linea) {
        reportar("cantidad de argumentos incorrecta: se esperaban " + esperados
                + " argumentos y se encontraron " + encontrados, linea);
    }

    public void reportarTipoArgumentoIncorrecto(int argumento, TipoDato esperado, TipoDato encontrado, int linea) {
        reportar("argumento " + argumento + " incompatible: se esperaba tipo "
                + esperado + " y se encontro tipo " + encontrado, linea);
    }

    private void reportarVariableNoDeclarada(String nombre, int linea) {
        reportar("variable '" + nombre + "' no declarada", linea);
    }

    private void reportarFuncionNoDeclarada(String nombre, int linea) {
        reportar("funcion '" + nombre + "' no declarada", linea);
    }

    private void reportarRedeclaracion(String nombre, int linea) {
        reportar("'" + nombre + "' ya esta declarado en este alcance", linea);
    }

    private void reportar(String descripcion, int linea) {
        erroresSemanticos.add(ReportadorErrores.reportarSemantico(linea, 0, descripcion));
    }

    private void insertarSimboloError(Simbolo simbolo) {
        if (alcances.isEmpty()) {
            abrirAlcance();
        }
        alcances.peek().putIfAbsent(simbolo.getNombre(), simbolo);
    }
}
