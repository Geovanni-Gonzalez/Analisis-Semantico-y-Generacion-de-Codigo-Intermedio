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
 * Administra simbolos, alcances y errores semanticos.
 *
 * <p>La tabla se modela como una pila de mapas: cada bloque abre un nuevo
 * alcance y las busquedas recorren desde el alcance mas interno hacia el global.</p>
 */
public class TablaDeSimbolos {
    private final Stack<HashMap<String, Simbolo>> alcances;
    private final List<String> erroresSemanticos;
    private final Set<String> variablesNoDeclaradasReportadas;
    private final Set<String> funcionesNoDeclaradasReportadas;
    /**
     * Nombre : TablaDeSimbolos.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Instancia inicializada de TablaDeSimbolos.
     */
    public TablaDeSimbolos() {
        this.alcances = new Stack<>();
        this.erroresSemanticos = new ArrayList<>();
        this.variablesNoDeclaradasReportadas = new HashSet<>();
        this.funcionesNoDeclaradasReportadas = new HashSet<>();
    }

    /**
     * Nombre : abrirAlcance.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void abrirAlcance() {
        alcances.push(new HashMap<>());
    }

    /**
     * Nombre : cerrarAlcance.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void cerrarAlcance() {
        if (alcances.isEmpty()) {
            throw new IllegalStateException("No hay alcances abiertos para cerrar.");
        }
        alcances.pop();
    }

    /**
     * Nombre : insertar.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Simbolo simbolo
     * Salida: No retorna valor.
     */
    public void insertar(Simbolo simbolo) {
        if (alcances.isEmpty()) {
            throw new IllegalStateException("No hay un alcance abierto para insertar simbolos.");
        }

        String nombre = simbolo.getNombre();
        if (existeEnAlcanceActual(nombre)) {
            reportarRedeclaracion(nombre, simbolo.getLinea());
            return;
        }
        if ((simbolo.getCategoria() == CategoriaSimb.VAR
                || simbolo.getCategoria() == CategoriaSimb.ARREGLO)
                && existeParametroVisible(nombre)) {
            reportarRedeclaracion(nombre, simbolo.getLinea());
            return;
        }

        alcances.peek().put(nombre, simbolo);
    }

    /**
     * Nombre : reportarTipoDeclaracionInvalido.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato tipo, int linea
     * Salida: No retorna valor.
     */
    public void reportarTipoDeclaracionInvalido(TipoDato tipo, int linea) {
        reportar("tipo declarado invalido '" + tipo + "'", linea);
    }

    /**
     * Nombre : reportarAsignacionIncompatible.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato tipoOrigen, TipoDato tipoDestino, int linea
     * Salida: No retorna valor.
     */
    public void reportarAsignacionIncompatible(TipoDato tipoOrigen, TipoDato tipoDestino, int linea) {
        reportar("no se puede asignar tipo " + tipoOrigen
                + " a variable de tipo " + tipoDestino, linea);
    }

    /**
     * Nombre : buscar.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre
     * Salida: Retorna Simbolo.
     */
    public Simbolo buscar(String nombre) {
        return buscar(nombre, -1);
    }

    /**
     * Nombre : buscar.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, int linea
     * Salida: Retorna Simbolo.
     */
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
        return new Simbolo(nombre, TipoDato.ERROR, CategoriaSimb.VAR, linea);
    }

    /**
     * Nombre : buscarFuncion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, int linea
     * Salida: Retorna Simbolo.
     */
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

    /**
     * Nombre : actualizarFirmaFuncion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, List<TipoDato> tiposParametros, TipoDato tipoRetorno, int linea
     * Salida: No retorna valor.
     */
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

    /**
     * Nombre : existeEnAlcanceActual.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre
     * Salida: Retorna boolean.
     */
    public boolean existeEnAlcanceActual(String nombre) {
        if (alcances.isEmpty()) {
            return false;
        }
        return alcances.peek().containsKey(nombre);
    }

    /**
     * Nombre : agregarParametroAFuncion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombreFuncion, TipoDato tipoParametro, int lineaFuncion
     * Salida: No retorna valor.
     */
    public void agregarParametroAFuncion(String nombreFuncion, TipoDato tipoParametro, int lineaFuncion) {
        Simbolo funcion = buscarFuncionPorFirma(nombreFuncion, lineaFuncion);
        if (funcion != null) {
            funcion.agregarTipoParametro(tipoParametro);
        }
    }

    /**
     * Nombre : marcarInicializado.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre
     * Salida: No retorna valor.
     */
    public void marcarInicializado(String nombre) {
        Simbolo simbolo = buscarSinReportar(nombre);
        if (simbolo != null) {
            simbolo.setInicializado(true);
        }
    }

    /**
     * Nombre : getErroresSemanticos.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna List<String>.
     */
    public List<String> getErroresSemanticos() {
        return Collections.unmodifiableList(erroresSemanticos);
    }
    /**
     * Nombre : reportarMainObligatorio.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void reportarMainObligatorio() {
        reportar("el programa debe contener exactamente un metodo main", 0);
    }

    /**
     * Nombre : reportarAsignacionIncompatible.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, TipoDato esperado, TipoDato recibido, int linea
     * Salida: No retorna valor.
     */
    public void reportarAsignacionIncompatible(String nombre, TipoDato esperado, TipoDato recibido, int linea) {
        reportar("asignacion incompatible para '" + nombre + "': se esperaba tipo "
                + esperado + " y se obtuvo tipo " + recibido, linea);
    }

    /**
     * Nombre : reportarOperacionIncompatible.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String operador, TipoDato izquierda, TipoDato derecha, int linea
     * Salida: No retorna valor.
     */
    public void reportarOperacionIncompatible(String operador, TipoDato izquierda, TipoDato derecha, int linea) {
        reportar("tipos incompatibles para operador '" + operador
                + "': operando izquierdo de tipo " + izquierda
                + " y operando derecho de tipo " + derecha, linea);
    }

    /**
     * Nombre : reportarOperacionIncompatible.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String operador, TipoDato operando, int linea
     * Salida: No retorna valor.
     */
    public void reportarOperacionIncompatible(String operador, TipoDato operando, int linea) {
        reportar("tipo incompatible para operador '" + operador
                + "': se obtuvo tipo " + operando, linea);
    }

    /**
     * Nombre : reportarVariableNoInicializada.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, int linea
     * Salida: No retorna valor.
     */
    public void reportarVariableNoInicializada(String nombre, int linea) {
        reportar("variable '" + nombre + "' usada antes de inicializarse", linea);
    }

    /**
     * Nombre : reportarUsoArregloComoEscalar.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, int linea
     * Salida: No retorna valor.
     */
    public void reportarUsoArregloComoEscalar(String nombre, int linea) {
        reportar("'" + nombre + "' es un arreglo y debe accederse con indices", linea);
    }

    /**
     * Nombre : reportarUsoEscalarComoArreglo.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, int linea
     * Salida: No retorna valor.
     */
    public void reportarUsoEscalarComoArreglo(String nombre, int linea) {
        reportar("'" + nombre + "' no es un arreglo", linea);
    }

    /**
     * Nombre : reportarAsignacionArregloCompleto.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, int linea
     * Salida: No retorna valor.
     */
    public void reportarAsignacionArregloCompleto(String nombre, int linea) {
        reportar("no se puede asignar directamente al arreglo completo '" + nombre + "'", linea);
    }

    /**
     * Nombre : reportarInicializacionArregloIncompatible.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, TipoDato esperado, TipoDato encontrado, int linea
     * Salida: No retorna valor.
     */
    public void reportarInicializacionArregloIncompatible(String nombre, TipoDato esperado,
                                                          TipoDato encontrado, int linea) {
        reportar("inicializacion incompatible en arreglo '" + nombre + "': se esperaba tipo "
                + esperado + " y se obtuvo tipo " + encontrado, linea);
    }

    /**
     * Nombre : reportarDimensionArregloInvalida.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, String dimension, int linea
     * Salida: No retorna valor.
     */
    public void reportarDimensionArregloInvalida(String nombre, String dimension, int linea) {
        reportar("la dimension " + dimension + " del arreglo '" + nombre
                + "' debe ser de tipo int", linea);
    }

    /**
     * Nombre : reportarSwitchTipoInvalido.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato tipo, int linea
     * Salida: No retorna valor.
     */
    public void reportarSwitchTipoInvalido(TipoDato tipo, int linea) {
        reportar("la expresion de switch no puede ser de tipo " + tipo, linea);
    }

    /**
     * Nombre : reportarCaseTipoIncompatible.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato esperado, TipoDato encontrado, int linea
     * Salida: No retorna valor.
     */
    public void reportarCaseTipoIncompatible(TipoDato esperado, TipoDato encontrado, int linea) {
        reportar("tipo incompatible en case: se esperaba tipo " + esperado
                + " y se obtuvo tipo " + encontrado, linea);
    }

    /**
     * Nombre : reportarEntradaTipoInvalido.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, TipoDato tipo, int linea
     * Salida: No retorna valor.
     */
    public void reportarEntradaTipoInvalido(String nombre, TipoDato tipo, int linea) {
        reportar("cin solo puede leer variables escalares declarables; '" + nombre
                + "' tiene tipo " + tipo, linea);
    }

    /**
     * Nombre : reportarSalidaTipoInvalido.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato tipo, int linea
     * Salida: No retorna valor.
     */
    public void reportarSalidaTipoInvalido(TipoDato tipo, int linea) {
        reportar("cout no puede imprimir una expresion de tipo " + tipo, linea);
    }

    /**
     * Nombre : reportarReturnFaltante.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato esperado, int linea
     * Salida: No retorna valor.
     */
    public void reportarReturnFaltante(TipoDato esperado, int linea) {
        reportar("la funcion de tipo " + esperado + " debe contener al menos un return", linea);
    }

    /**
     * Nombre : reportarIndiceNoEntero.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato tipoIndice, int linea
     * Salida: No retorna valor.
     */
    public void reportarIndiceNoEntero(TipoDato tipoIndice, int linea) {
        reportar("el indice del arreglo debe ser de tipo int, se encontro " + tipoIndice, linea);
    }

    /**
     * Nombre : reportarCondicionNoBooleana.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato recibido, int linea
     * Salida: No retorna valor.
     */
    public void reportarCondicionNoBooleana(TipoDato recibido, int linea) {
        reportar("la condicion debe ser de tipo bool, pero se encontro tipo " + recibido, linea);
    }

    /**
     * Nombre : reportarReturnSinValor.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato esperado, int linea
     * Salida: No retorna valor.
     */
    public void reportarReturnSinValor(TipoDato esperado, int linea) {
        reportar("la funcion de tipo " + esperado + " debe retornar un valor", linea);
    }

    /**
     * Nombre : reportarReturnConValorEnVoid.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: int linea
     * Salida: No retorna valor.
     */
    public void reportarReturnConValorEnVoid(int linea) {
        reportar("la funcion void no puede retornar un valor", linea);
    }

    /**
     * Nombre : reportarReturnTipoIncompatible.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: TipoDato esperado, TipoDato encontrado, int linea
     * Salida: No retorna valor.
     */
    public void reportarReturnTipoIncompatible(TipoDato esperado, TipoDato encontrado, int linea) {
        reportar("tipo incompatible en return: se esperaba tipo " + esperado
                + " y se obtuvo tipo " + encontrado, linea);
    }

    /**
     * Nombre : reportarCantidadArgumentosIncorrecta.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: int esperados, int encontrados, int linea
     * Salida: No retorna valor.
     */
    public void reportarCantidadArgumentosIncorrecta(int esperados, int encontrados, int linea) {
        reportar("cantidad de argumentos incorrecta: se esperaban " + esperados
                + " argumentos y se encontraron " + encontrados, linea);
    }

    /**
     * Nombre : reportarTipoArgumentoIncorrecto.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: int argumento, TipoDato esperado, TipoDato encontrado, int linea
     * Salida: No retorna valor.
     */
    public void reportarTipoArgumentoIncorrecto(int argumento, TipoDato esperado, TipoDato encontrado, int linea) {
        reportar("argumento " + argumento + " incompatible: se esperaba tipo "
                + esperado + " y se encontro tipo " + encontrado, linea);
    }

    /**
     * Nombre : reportarVariableNoDeclarada.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, int linea
     * Salida: No retorna valor.
     */
    private void reportarVariableNoDeclarada(String nombre, int linea) {
        reportar("variable '" + nombre + "' no declarada", linea);
    }

    /**
     * Nombre : reportarFuncionNoDeclarada.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, int linea
     * Salida: No retorna valor.
     */
    private void reportarFuncionNoDeclarada(String nombre, int linea) {
        reportar("funcion '" + nombre + "' no declarada", linea);
    }

    /**
     * Nombre : reportarRedeclaracion.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String nombre, int linea
     * Salida: No retorna valor.
     */
    private void reportarRedeclaracion(String nombre, int linea) {
        reportar("'" + nombre + "' ya esta declarado en este alcance", linea);
    }

    /**
     * Nombre : existeParametroVisible.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre
     * Salida: Retorna boolean.
     */
    private boolean existeParametroVisible(String nombre) {
        for (int i = alcances.size() - 1; i >= 0; i--) {
            Simbolo simbolo = alcances.get(i).get(nombre);
            if (simbolo != null) {
                return simbolo.getCategoria() == CategoriaSimb.PARAMETRO;
            }
        }
        return false;
    }

    /**
     * Nombre : buscarSinReportar.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre
     * Salida: Retorna Simbolo.
     */
    private Simbolo buscarSinReportar(String nombre) {
        for (int i = alcances.size() - 1; i >= 0; i--) {
            Simbolo simbolo = alcances.get(i).get(nombre);
            if (simbolo != null) {
                return simbolo;
            }
        }
        return null;
    }

    /**
     * Nombre : buscarFuncionPorFirma.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, int linea
     * Salida: Retorna Simbolo.
     */
    private Simbolo buscarFuncionPorFirma(String nombre, int linea) {
        for (int i = alcances.size() - 1; i >= 0; i--) {
            Simbolo simbolo = alcances.get(i).get(nombre);
            if (simbolo != null && simbolo.getCategoria() == CategoriaSimb.FUNCION
                    && simbolo.getLinea() == linea) {
                return simbolo;
            }
        }
        return null;
    }

    /**
     * Nombre : reportar.
     * Descripcion: Registra un diagnostico de error para el reporte semantico.
     * Entrada: String descripcion, int linea
     * Salida: No retorna valor.
     */
    private void reportar(String descripcion, int linea) {
        erroresSemanticos.add(ReportadorErrores.reportarSemantico(linea, 0, descripcion));
    }

    /**
     * Nombre : insertarSimboloError.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Simbolo simbolo
     * Salida: No retorna valor.
     */
    private void insertarSimboloError(Simbolo simbolo) {
        if (alcances.isEmpty()) {
            abrirAlcance();
        }
        alcances.peek().putIfAbsent(simbolo.getNombre(), simbolo);
    }
}
