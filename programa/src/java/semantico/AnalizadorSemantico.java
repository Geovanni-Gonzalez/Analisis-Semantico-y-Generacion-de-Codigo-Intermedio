package semantico;

import ast.AccesoArregloNodo;
import ast.AsignacionNodo;
import ast.CasoSwitchNodo;
import ast.EntradaNodo;
import ast.ExpresionBinariaNodo;
import ast.ExpresionNodo;
import ast.ExpresionUnariaNodo;
import ast.IdentificadorNodo;
import ast.InicializacionArregloNodo;
import ast.LlamadaFuncionNodo;
import ast.ParametroNodo;
import ast.ReturnNodo;
import ast.SalidaNodo;
import ast.SwitchNodo;
import ast.TipoDato;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Ejecuta las validaciones semanticas mientras el parser construye el AST.
 *
 * <p>Esta clase coordina la tabla de simbolos, calcula tipos de expresiones,
 * valida asignaciones, llamadas, retornos, arreglos y estructuras de control.
 * El parser la invoca desde las acciones CUP para detectar errores tan pronto
 * como se reconoce cada construccion.</p>
 */
public class AnalizadorSemantico {
    private final TablaDeSimbolos tablaSimbolos;
    private final Consumer<String> reportadorSintactico;
    private int cantidadMain;
    private TipoDato tipoRetornoActual = TipoDato.DESCONOCIDO;
    private String funcionActual;
    private int lineaFuncionActual;
    private boolean retornoEncontradoActual;
    /**
     * Nombre : AnalizadorSemantico.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: TablaDeSimbolos tablaSimbolos, Consumer<String> reportadorSintactico
     * Salida: Instancia inicializada de AnalizadorSemantico.
     */
    public AnalizadorSemantico(TablaDeSimbolos tablaSimbolos, Consumer<String> reportadorSintactico) {
        this.tablaSimbolos = tablaSimbolos;
        this.reportadorSintactico = reportadorSintactico;
    }

    /**
     * Nombre : registrarMain.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void registrarMain() {
        cantidadMain++;
    }

    /**
     * Nombre : verificarMain.
     * Descripcion: Valida una regla o escenario especifico del compilador.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void verificarMain() {
        if (cantidadMain == 0) {
            tablaSimbolos.reportarMainObligatorio();
        }
    }

    /**
     * Nombre : abrirPrograma.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void abrirPrograma() {
        tablaSimbolos.abrirAlcance();
    }

    /**
     * Nombre : cerrarPrograma.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void cerrarPrograma() {
        tablaSimbolos.cerrarAlcance();
    }

    /**
     * Nombre : abrirFuncion.
     * Descripcion: Registra una funcion antes de analizar su cuerpo. Registrar la funcion temprano permite llamadas recursivas. Tambien guarda el tipo de retorno esperado para validar sentencias return.
     * Entrada: String nombre, TipoDato tipoRetorno, int linea
     * Salida: No retorna valor.
     */
    public void abrirFuncion(String nombre, TipoDato tipoRetorno, int linea) {
        registrarFuncion(nombre, tipoRetorno, linea);
        tipoRetornoActual = tipoRetorno;
        funcionActual = nombre;
        lineaFuncionActual = linea;
        retornoEncontradoActual = false;
        tablaSimbolos.abrirAlcance();
    }

    /**
     * Nombre : cerrarFuncion.
     * Descripcion: Cierra la funcion actual y valida que las funciones con retorno tengan al menos una sentencia return.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void cerrarFuncion() {
        boolean requiereReturn = tipoRetornoActual != TipoDato.EMPTY
                && tipoRetornoActual != TipoDato.VOID
                && tipoRetornoActual != TipoDato.ERROR
                && tipoRetornoActual != TipoDato.DESCONOCIDO;
        if (requiereReturn && !retornoEncontradoActual) {
            tablaSimbolos.reportarReturnFaltante(tipoRetornoActual, lineaFuncionActual);
        }
        tablaSimbolos.cerrarAlcance();
        tipoRetornoActual = TipoDato.DESCONOCIDO;
        funcionActual = null;
        lineaFuncionActual = 0;
        retornoEncontradoActual = false;
    }

    /**
     * Nombre : abrirBloque.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void abrirBloque() {
        tablaSimbolos.abrirAlcance();
    }

    /**
     * Nombre : cerrarBloque.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void cerrarBloque() {
        tablaSimbolos.cerrarAlcance();
    }

    /**
     * Nombre : actualizarFirmaFuncion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipoRetorno, List parametros, int linea
     * Salida: No retorna valor.
     */
    public void actualizarFirmaFuncion(String nombre, TipoDato tipoRetorno, List parametros, int linea) {
        List<TipoDato> tiposParametros = new ArrayList<>();
        for (Object parametro : parametros) {
            tiposParametros.add(((ParametroNodo) parametro).getTipo());
        }
        tablaSimbolos.actualizarFirmaFuncion(nombre, tiposParametros, tipoRetorno, linea);
    }

    /**
     * Nombre : registrarParametro.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipo, int linea
     * Salida: No retorna valor.
     */
    public void registrarParametro(String nombre, TipoDato tipo, int linea) {
        insertarSimbolo(new Simbolo(nombre, tipo, CategoriaSimb.PARAMETRO, linea, true));
        if (funcionActual != null) {
            tablaSimbolos.agregarParametroAFuncion(funcionActual, tipo, lineaFuncionActual);
        }
    }

    /**
     * Nombre : registrarVariable.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipo, int linea
     * Salida: No retorna valor.
     */
    public void registrarVariable(String nombre, TipoDato tipo, int linea) {
        insertarSimbolo(new Simbolo(nombre, tipo, CategoriaSimb.VAR, linea, false));
    }

    /**
     * Nombre : registrarDeclaracionVariable.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipo, ExpresionNodo inicializador, int linea
     * Salida: No retorna valor.
     */
    public void registrarDeclaracionVariable(String nombre, TipoDato tipo,
            ExpresionNodo inicializador, int linea) {
        if (!tipo.esDeclarableVariable()) {
            tablaSimbolos.reportarTipoDeclaracionInvalido(tipo, linea);
            return;
        }

        if (inicializador != null) {
            TipoDato tipoInicializador = tipoExpresion(inicializador);
            if (tipoInicializador != TipoDato.ERROR && tipoInicializador != TipoDato.DESCONOCIDO
                    && tipo != tipoInicializador) {
                tablaSimbolos.reportarAsignacionIncompatible(tipoInicializador, tipo, linea);
                return;
            }
        }

        insertarSimbolo(new Simbolo(nombre, tipo, CategoriaSimb.VAR, linea, inicializador != null));
    }

    /**
     * Nombre : registrarDeclaracionArreglo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipo, ExpresionNodo filas, ExpresionNodo columnas, InicializacionArregloNodo inicializacion, int linea
     * Salida: No retorna valor.
     */
    public void registrarDeclaracionArreglo(String nombre, TipoDato tipo, ExpresionNodo filas,
                                            ExpresionNodo columnas,
                                            InicializacionArregloNodo inicializacion, int linea) {
        if (!tipo.esDeclarableVariable()) {
            tablaSimbolos.reportarTipoDeclaracionInvalido(tipo, linea);
            return;
        }
        validarDimensionArreglo(nombre, "fila", filas);
        validarDimensionArreglo(nombre, "columna", columnas);
        if (inicializacion != null) {
            validarInicializacionArreglo(nombre, tipo, inicializacion);
        }
        insertarSimbolo(new Simbolo(nombre, tipo, CategoriaSimb.ARREGLO, linea, inicializacion != null));
    }

    /**
     * Nombre : usarIdentificador.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, int linea
     * Salida: No retorna valor.
     */
    public void usarIdentificador(String nombre, int linea) {
        Simbolo simbolo = tablaSimbolos.buscar(nombre, linea);
        if (simbolo.getTipo() == TipoDato.ERROR) {
            return;
        }
        if (simbolo.getCategoria() == CategoriaSimb.ARREGLO) {
            tablaSimbolos.reportarUsoArregloComoEscalar(nombre, linea);
            return;
        }
        if (!simbolo.isInicializado()) {
            tablaSimbolos.reportarVariableNoInicializada(nombre, linea);
        }
    }

    /**
     * Nombre : verificarAsignacion.
     * Descripcion: Valida una regla o escenario especifico del compilador.
     * Entrada: AsignacionNodo asignacion
     * Salida: No retorna valor.
     */
    public void verificarAsignacion(AsignacionNodo asignacion) {
        TipoDato tipoDestino = evaluarTipoDestino(asignacion.getDestino());
        TipoDato tipoValor = evaluarTipo(asignacion.getValor());

        if (tipoDestino == TipoDato.ERROR || tipoValor == TipoDato.ERROR
                || tipoDestino == TipoDato.DESCONOCIDO || tipoValor == TipoDato.DESCONOCIDO) {
            return;
        }

        if (tipoDestino != tipoValor) {
            tablaSimbolos.reportarAsignacionIncompatible(nombreDestino(asignacion.getDestino()),
                    tipoDestino, tipoValor, asignacion.getLinea());
            return;
        }

        marcarDestinoInicializado(asignacion.getDestino());
    }

    /**
     * Nombre : verificarSwitch.
     * Descripcion: Valida una regla o escenario especifico del compilador.
     * Entrada: SwitchNodo switchNodo
     * Salida: No retorna valor.
     */
    public void verificarSwitch(SwitchNodo switchNodo) {
        TipoDato tipoSwitch = evaluarTipo(switchNodo.getExpresion());
        if (tipoSwitch == TipoDato.ERROR || tipoSwitch == TipoDato.DESCONOCIDO) {
            return;
        }
        if (tipoSwitch == TipoDato.BOOL || tipoSwitch == TipoDato.FLOAT
                || tipoSwitch == TipoDato.VOID || tipoSwitch == TipoDato.EMPTY) {
            tablaSimbolos.reportarSwitchTipoInvalido(tipoSwitch, switchNodo.getLinea());
        }
        for (CasoSwitchNodo caso : switchNodo.getCasos()) {
            if (caso.isDefecto() || caso.getValor() == null) {
                continue;
            }
            TipoDato tipoCase = evaluarTipo(caso.getValor());
            if (tipoCase == TipoDato.ERROR || tipoCase == TipoDato.DESCONOCIDO) {
                continue;
            }
            if (tipoCase != tipoSwitch) {
                tablaSimbolos.reportarCaseTipoIncompatible(tipoSwitch, tipoCase, caso.getLinea());
            }
        }
    }

    /**
     * Nombre : verificarEntrada.
     * Descripcion: Valida una regla o escenario especifico del compilador.
     * Entrada: EntradaNodo entrada
     * Salida: No retorna valor.
     */
    public void verificarEntrada(EntradaNodo entrada) {
        Simbolo simbolo = tablaSimbolos.buscar(entrada.getDestino(), entrada.getLinea());
        if (simbolo.getTipo() == TipoDato.ERROR) {
            return;
        }
        if (simbolo.getCategoria() == CategoriaSimb.ARREGLO
                || !simbolo.getTipo().esDeclarableVariable()) {
            tablaSimbolos.reportarEntradaTipoInvalido(entrada.getDestino(), simbolo.getTipo(),
                    entrada.getLinea());
            return;
        }
        tablaSimbolos.marcarInicializado(entrada.getDestino());
    }

    /**
     * Nombre : verificarSalida.
     * Descripcion: Valida una regla o escenario especifico del compilador.
     * Entrada: SalidaNodo salida
     * Salida: No retorna valor.
     */
    public void verificarSalida(SalidaNodo salida) {
        TipoDato tipo = evaluarTipo(salida.getValor());
        if (tipo == TipoDato.ERROR || tipo == TipoDato.DESCONOCIDO) {
            return;
        }
        if (tipo == TipoDato.VOID || tipo == TipoDato.EMPTY) {
            tablaSimbolos.reportarSalidaTipoInvalido(tipo, salida.getLinea());
        }
    }

    /**
     * Nombre : evaluarTipo.
     * Descripcion: Calcula el tipo semantico de cualquier expresion. El resultado se guarda en el nodo para reutilizarlo. Si se detecta un error, se retorna {@link TipoDato#ERROR} para evitar cascadas innecesarias.
     * Entrada: ExpresionNodo expresion
     * Salida: Retorna TipoDato.
     */
    public TipoDato evaluarTipo(ExpresionNodo expresion) {
        if (expresion == null) {
            return TipoDato.ERROR;
        }

        TipoDato tipoActual = expresion.getTipo();
        if (tipoActual != TipoDato.DESCONOCIDO) {
            return tipoActual;
        }

        TipoDato tipo = TipoDato.ERROR;
        if (expresion instanceof IdentificadorNodo) {
            IdentificadorNodo id = (IdentificadorNodo) expresion;
            Simbolo simbolo = tablaSimbolos.buscar(id.getNombre(), id.getLinea());
            if (simbolo.getTipo() == TipoDato.ERROR) {
                return TipoDato.ERROR;
            }
            if (simbolo.getCategoria() == CategoriaSimb.ARREGLO) {
                tablaSimbolos.reportarUsoArregloComoEscalar(id.getNombre(), id.getLinea());
                return TipoDato.ERROR;
            }
            if (!simbolo.isInicializado()) {
                tablaSimbolos.reportarVariableNoInicializada(id.getNombre(), id.getLinea());
            }
            tipo = simbolo.getTipo();
        } else if (expresion instanceof AccesoArregloNodo) {
            tipo = evaluarTipoAccesoArreglo((AccesoArregloNodo) expresion, true);
        } else if (expresion instanceof LlamadaFuncionNodo) {
            tipo = evaluarTipoLlamada((LlamadaFuncionNodo) expresion);
        } else if (expresion instanceof ExpresionBinariaNodo) {
            tipo = evaluarTipoBinaria((ExpresionBinariaNodo) expresion);
        } else if (expresion instanceof ExpresionUnariaNodo) {
            tipo = evaluarTipoUnaria((ExpresionUnariaNodo) expresion);
        }

        expresion.setTipo(tipo);
        return tipo;
    }

    /**
     * Nombre : tipoExpresion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: ExpresionNodo expresion
     * Salida: Retorna TipoDato.
     */
    public TipoDato tipoExpresion(ExpresionNodo expresion) {
        return evaluarTipo(expresion);
    }

    /**
     * Nombre : verificarCondicionBooleana.
     * Descripcion: Valida una regla o escenario especifico del compilador.
     * Entrada: ExpresionNodo condicion
     * Salida: No retorna valor.
     */
    public void verificarCondicionBooleana(ExpresionNodo condicion) {
        TipoDato tipo = evaluarTipo(condicion);
        if (tipo == TipoDato.ERROR || tipo == TipoDato.DESCONOCIDO) {
            return;
        }
        if (tipo != TipoDato.BOOL) {
            tablaSimbolos.reportarCondicionNoBooleana(tipo, condicion.getLinea());
        }
    }

    /**
     * Nombre : verificarReturn.
     * Descripcion: Valida una regla o escenario especifico del compilador.
     * Entrada: ReturnNodo retorno
     * Salida: No retorna valor.
     */
    public void verificarReturn(ReturnNodo retorno) {
        retornoEncontradoActual = true;
        TipoDato tipoEsperado = tipoRetornoActual;
        ExpresionNodo valor = retorno.getValor();
        boolean funcionVoid = tipoEsperado == TipoDato.EMPTY || tipoEsperado == TipoDato.VOID;

        if (valor == null) {
            if (!funcionVoid && tipoEsperado != TipoDato.ERROR && tipoEsperado != TipoDato.DESCONOCIDO) {
                tablaSimbolos.reportarReturnSinValor(tipoEsperado, retorno.getLinea());
            }
            return;
        }

        if (funcionVoid) {
            tablaSimbolos.reportarReturnConValorEnVoid(retorno.getLinea());
            evaluarTipo(valor);
            return;
        }

        TipoDato tipoRecibido = evaluarTipo(valor);
        if (tipoEsperado == TipoDato.ERROR || tipoEsperado == TipoDato.DESCONOCIDO
                || tipoRecibido == TipoDato.ERROR || tipoRecibido == TipoDato.DESCONOCIDO) {
            return;
        }
        if (tipoEsperado != tipoRecibido) {
            tablaSimbolos.reportarReturnTipoIncompatible(tipoEsperado, tipoRecibido, retorno.getLinea());
        }
    }

    /**
     * Nombre : registrarFuncion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipoRetorno, int linea
     * Salida: No retorna valor.
     */
    private void registrarFuncion(String nombre, TipoDato tipoRetorno, int linea) {
        insertarSimbolo(new Simbolo(nombre, new ArrayList<TipoDato>(), tipoRetorno, linea));
    }

    /**
     * Nombre : insertarSimbolo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Simbolo simbolo
     * Salida: No retorna valor.
     */
    private void insertarSimbolo(Simbolo simbolo) {
        try {
            tablaSimbolos.insertar(simbolo);
        } catch (IllegalArgumentException ex) {
            reportadorSintactico.accept(ex.getMessage());
        }
    }

    /**
     * Nombre : evaluarTipoLlamada.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: LlamadaFuncionNodo llamada
     * Salida: Retorna TipoDato.
     */
    private TipoDato evaluarTipoLlamada(LlamadaFuncionNodo llamada) {
        Simbolo funcion = tablaSimbolos.buscarFuncion(llamada.getNombre(), llamada.getLinea());
        if (funcion.getTipo() == TipoDato.ERROR) {
            return TipoDato.ERROR;
        }

        List<TipoDato> parametros = funcion.getTiposParametros();
        List<ExpresionNodo> argumentos = llamada.getArgumentos();
        if (parametros.size() != argumentos.size()) {
            tablaSimbolos.reportarCantidadArgumentosIncorrecta(parametros.size(), argumentos.size(),
                    llamada.getLinea());
            return TipoDato.ERROR;
        }

        for (int i = 0; i < argumentos.size(); i++) {
            TipoDato tipoArgumento = evaluarTipo(argumentos.get(i));
            TipoDato tipoParametro = parametros.get(i);
            if (tipoArgumento == TipoDato.ERROR || tipoArgumento == TipoDato.DESCONOCIDO) {
                return TipoDato.ERROR;
            }
            if (tipoArgumento != tipoParametro) {
                tablaSimbolos.reportarTipoArgumentoIncorrecto(i + 1, tipoParametro, tipoArgumento,
                        argumentos.get(i).getLinea());
                return TipoDato.ERROR;
            }
        }

        return funcion.getTipoRetorno() != null ? funcion.getTipoRetorno() : funcion.getTipo();
    }

    /**
     * Nombre : evaluarTipoBinaria.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: ExpresionBinariaNodo expresion
     * Salida: Retorna TipoDato.
     */
    private TipoDato evaluarTipoBinaria(ExpresionBinariaNodo expresion) {
        TipoDato izquierda = evaluarTipo(expresion.getIzquierda());
        TipoDato derecha = evaluarTipo(expresion.getDerecha());
        if (izquierda == TipoDato.ERROR || derecha == TipoDato.ERROR
                || izquierda == TipoDato.DESCONOCIDO || derecha == TipoDato.DESCONOCIDO) {
            return TipoDato.ERROR;
        }

        String operador = expresion.getOperador();
        if ("#".equals(operador) || "@".equals(operador)) {
            if (izquierda == TipoDato.BOOL && derecha == TipoDato.BOOL) {
                return TipoDato.BOOL;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, izquierda, derecha, expresion.getLinea());
            return TipoDato.ERROR;
        }
        if ("equal".equals(operador) || "n_equal".equals(operador)) {
            if (izquierda == derecha) {
                return TipoDato.BOOL;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, izquierda, derecha, expresion.getLinea());
            return TipoDato.ERROR;
        }
        if ("less_t".equals(operador) || "less_te".equals(operador)
                || "greather_t".equals(operador) || "greather_te".equals(operador)) {
            if (izquierda.esNumerico() && derecha.esNumerico() && izquierda == derecha) {
                return TipoDato.BOOL;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, izquierda, derecha, expresion.getLinea());
            return TipoDato.ERROR;
        }
        if ("+".equals(operador) || "-".equals(operador) || "*".equals(operador)
                || "/".equals(operador)) {
            if (izquierda.esNumerico() && derecha.esNumerico()) {
                return izquierda == TipoDato.FLOAT || derecha == TipoDato.FLOAT
                        ? TipoDato.FLOAT
                        : TipoDato.INT;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, izquierda, derecha, expresion.getLinea());
            return TipoDato.ERROR;
        }
        if ("%".equals(operador)) {
            if (izquierda.esNumerico() && derecha.esNumerico() && izquierda == derecha) {
                return izquierda;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, izquierda, derecha, expresion.getLinea());
            return TipoDato.ERROR;
        }
        if ("^".equals(operador)) {
            if (izquierda.esNumerico() && derecha == TipoDato.INT) {
                return izquierda;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, izquierda, derecha, expresion.getLinea());
            return TipoDato.ERROR;
        }

        return TipoDato.ERROR;
    }

    /**
     * Nombre : evaluarTipoUnaria.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: ExpresionUnariaNodo expresion
     * Salida: Retorna TipoDato.
     */
    private TipoDato evaluarTipoUnaria(ExpresionUnariaNodo expresion) {
        TipoDato tipo = evaluarTipo(expresion.getExpresion());
        if (tipo == TipoDato.ERROR || tipo == TipoDato.DESCONOCIDO) {
            return TipoDato.ERROR;
        }

        String operador = expresion.getOperador();
        if ("$".equals(operador)) {
            if (tipo == TipoDato.BOOL) {
                return TipoDato.BOOL;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, tipo, expresion.getLinea());
            return TipoDato.ERROR;
        }
        if ("-".equals(operador) || "++".equals(operador) || "--".equals(operador)) {
            if (("++".equals(operador) || "--".equals(operador)) && !esModificable(expresion.getExpresion())) {
                tablaSimbolos.reportarOperacionIncompatible(operador, tipo, expresion.getLinea());
                return TipoDato.ERROR;
            }
            if (tipo.esNumerico()) {
                return tipo;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, tipo, expresion.getLinea());
            return TipoDato.ERROR;
        }

        return TipoDato.ERROR;
    }

    /**
     * Nombre : nombreDestino.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: ExpresionNodo destino
     * Salida: Retorna String.
     */
    private String nombreDestino(ExpresionNodo destino) {
        if (destino instanceof IdentificadorNodo) {
            return ((IdentificadorNodo) destino).getNombre();
        }
        if (destino instanceof AccesoArregloNodo) {
            return ((AccesoArregloNodo) destino).getNombre();
        }
        return "<desconocido>";
    }

    /**
     * Nombre : evaluarTipoDestino.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: ExpresionNodo destino
     * Salida: Retorna TipoDato.
     */
    private TipoDato evaluarTipoDestino(ExpresionNodo destino) {
        if (destino instanceof IdentificadorNodo) {
            IdentificadorNodo id = (IdentificadorNodo) destino;
            Simbolo simbolo = tablaSimbolos.buscar(id.getNombre(), id.getLinea());
            if (simbolo.getTipo() == TipoDato.ERROR) {
                return TipoDato.ERROR;
            }
            if (simbolo.getCategoria() == CategoriaSimb.ARREGLO) {
                tablaSimbolos.reportarAsignacionArregloCompleto(id.getNombre(), id.getLinea());
                return TipoDato.ERROR;
            }
            return simbolo.getTipo();
        }
        if (destino instanceof AccesoArregloNodo) {
            return evaluarTipoAccesoArreglo((AccesoArregloNodo) destino, false);
        }
        return TipoDato.ERROR;
    }

    /**
     * Nombre : evaluarTipoAccesoArreglo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: AccesoArregloNodo acceso, boolean requiereInicializado
     * Salida: Retorna TipoDato.
     */
    private TipoDato evaluarTipoAccesoArreglo(AccesoArregloNodo acceso, boolean requiereInicializado) {
        Simbolo simbolo = tablaSimbolos.buscar(acceso.getNombre(), acceso.getLinea());
        if (simbolo.getTipo() == TipoDato.ERROR) {
            return TipoDato.ERROR;
        }
        if (simbolo.getCategoria() != CategoriaSimb.ARREGLO) {
            tablaSimbolos.reportarUsoEscalarComoArreglo(acceso.getNombre(), acceso.getLinea());
            return TipoDato.ERROR;
        }
        validarIndiceArreglo(acceso.getFila());
        validarIndiceArreglo(acceso.getColumnaIndice());
        if (requiereInicializado && !simbolo.isInicializado()) {
            tablaSimbolos.reportarVariableNoInicializada(acceso.getNombre(), acceso.getLinea());
        }
        return simbolo.getTipo();
    }

    /**
     * Nombre : validarIndiceArreglo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: ExpresionNodo indice
     * Salida: No retorna valor.
     */
    private void validarIndiceArreglo(ExpresionNodo indice) {
        TipoDato tipo = evaluarTipo(indice);
        if (tipo != TipoDato.ERROR && tipo != TipoDato.DESCONOCIDO && tipo != TipoDato.INT) {
            tablaSimbolos.reportarIndiceNoEntero(tipo, indice.getLinea());
        }
    }

    /**
     * Nombre : validarDimensionArreglo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, String dimension, ExpresionNodo expresion
     * Salida: No retorna valor.
     */
    private void validarDimensionArreglo(String nombre, String dimension, ExpresionNodo expresion) {
        TipoDato tipo = evaluarTipo(expresion);
        if (tipo != TipoDato.ERROR && tipo != TipoDato.DESCONOCIDO && tipo != TipoDato.INT) {
            tablaSimbolos.reportarDimensionArregloInvalida(nombre, dimension, expresion.getLinea());
        }
    }

    /**
     * Nombre : validarInicializacionArreglo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipoEsperado, InicializacionArregloNodo inicializacion
     * Salida: No retorna valor.
     */
    private void validarInicializacionArreglo(String nombre, TipoDato tipoEsperado,
                                              InicializacionArregloNodo inicializacion) {
        for (List<ExpresionNodo> fila : inicializacion.getFilas()) {
            for (ExpresionNodo valor : fila) {
                TipoDato tipoValor = evaluarTipo(valor);
                if (tipoValor != TipoDato.ERROR && tipoValor != TipoDato.DESCONOCIDO
                        && tipoValor != tipoEsperado) {
                    tablaSimbolos.reportarInicializacionArregloIncompatible(nombre, tipoEsperado,
                            tipoValor, valor.getLinea());
                }
            }
        }
    }

    /**
     * Nombre : marcarDestinoInicializado.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: ExpresionNodo destino
     * Salida: No retorna valor.
     */
    private void marcarDestinoInicializado(ExpresionNodo destino) {
        if (destino instanceof IdentificadorNodo) {
            tablaSimbolos.marcarInicializado(((IdentificadorNodo) destino).getNombre());
        } else if (destino instanceof AccesoArregloNodo) {
            tablaSimbolos.marcarInicializado(((AccesoArregloNodo) destino).getNombre());
        }
    }

    /**
     * Nombre : esModificable.
     * Descripcion: Consulta una condicion booleana del objeto.
     * Entrada: ExpresionNodo expresion
     * Salida: Retorna boolean.
     */
    private boolean esModificable(ExpresionNodo expresion) {
        return expresion instanceof IdentificadorNodo || expresion instanceof AccesoArregloNodo;
    }
}
