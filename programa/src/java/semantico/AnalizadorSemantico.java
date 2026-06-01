package semantico;

import ast.AccesoArregloNodo;
import ast.AsignacionNodo;
import ast.ExpresionBinariaNodo;
import ast.ExpresionNodo;
import ast.ExpresionUnariaNodo;
import ast.IdentificadorNodo;
import ast.LlamadaFuncionNodo;
import ast.ParametroNodo;
import ast.ReturnNodo;
import ast.TipoDato;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Aplica las reglas semanticas sobre los nodos que construye el parser.
 *
 * <p>El parser conserva las acciones de construccion del AST, pero delega aqui
 * las decisiones de tipos, alcances, firmas de funciones, condiciones y
 * retornos. Cuando una expresion falla, se propaga {@link TipoDato#ERROR} para
 * reducir errores en cascada.</p>
 */
public class AnalizadorSemantico {
    private final TablaDeSimbolos tablaSimbolos;
    private final Consumer<String> reportadorSintactico;
    private int cantidadMain;
    private TipoDato tipoRetornoActual = TipoDato.DESCONOCIDO;

    public AnalizadorSemantico(TablaDeSimbolos tablaSimbolos, Consumer<String> reportadorSintactico) {
        this.tablaSimbolos = tablaSimbolos;
        this.reportadorSintactico = reportadorSintactico;
    }

    public void registrarMain() {
        cantidadMain++;
    }

    public void verificarMain() {
        if (cantidadMain == 0) {
            tablaSimbolos.reportarMainObligatorio();
        }
    }

    public void abrirPrograma() {
        tablaSimbolos.abrirAlcance();
    }

    public void cerrarPrograma() {
        tablaSimbolos.cerrarAlcance();
    }

    public void abrirFuncion(String nombre, TipoDato tipoRetorno, int linea) {
        registrarFuncion(nombre, tipoRetorno, linea);
        tipoRetornoActual = tipoRetorno;
        tablaSimbolos.abrirAlcance();
    }

    public void cerrarFuncion() {
        tablaSimbolos.cerrarAlcance();
        tipoRetornoActual = TipoDato.DESCONOCIDO;
    }

    public void abrirBloque() {
        tablaSimbolos.abrirAlcance();
    }

    public void cerrarBloque() {
        tablaSimbolos.cerrarAlcance();
    }

    public void actualizarFirmaFuncion(String nombre, TipoDato tipoRetorno, List parametros, int linea) {
        List<TipoDato> tiposParametros = new ArrayList<>();
        for (Object parametro : parametros) {
            tiposParametros.add(((ParametroNodo) parametro).getTipo());
        }
        tablaSimbolos.actualizarFirmaFuncion(nombre, tiposParametros, tipoRetorno, linea);
    }

    public void registrarParametro(String nombre, TipoDato tipo, int linea) {
        insertarSimbolo(new Simbolo(nombre, tipo, CategoriaSimb.PARAMETRO, linea));
    }

    public void registrarVariable(String nombre, TipoDato tipo, int linea) {
        insertarSimbolo(new Simbolo(nombre, tipo, CategoriaSimb.VAR, linea));
    }

    public void registrarDeclaracionVariable(String nombre, TipoDato tipo, ExpresionNodo inicializador,
                                             int linea) {
        if (!tipo.esDeclarableVariable()) {
            tablaSimbolos.reportarTipoDeclaracionInvalido(tipo, linea);
            return;
        }

        if (tablaSimbolos.existeEnAlcanceActual(nombre)) {
            insertarSimbolo(new Simbolo(nombre, tipo, CategoriaSimb.VAR, linea));
            return;
        }

        if (inicializador != null) {
            TipoDato tipoInicializador = tipoExpresion(inicializador);
            if (!tipo.esCompatibleCon(tipoInicializador)) {
                tablaSimbolos.reportarAsignacionIncompatible(tipoInicializador, tipo, linea);
                return;
            }
        }

        insertarSimbolo(new Simbolo(nombre, tipo, CategoriaSimb.VAR, linea));
    }

    public void usarIdentificador(String nombre, int linea) {
        tablaSimbolos.buscar(nombre, linea);
    }

    public void verificarAsignacion(AsignacionNodo asignacion) {
        TipoDato tipoDestino = evaluarTipo(asignacion.getDestino());
        TipoDato tipoValor = evaluarTipo(asignacion.getValor());

        if (tipoDestino == TipoDato.ERROR || tipoValor == TipoDato.ERROR
                || tipoDestino == TipoDato.DESCONOCIDO || tipoValor == TipoDato.DESCONOCIDO) {
            return;
        }

        if (tipoDestino != tipoValor) {
            tablaSimbolos.reportarAsignacionIncompatible(nombreDestino(asignacion.getDestino()),
                    tipoDestino, tipoValor, asignacion.getLinea());
        }
    }

    public TipoDato evaluarTipo(ExpresionNodo expresion) {
        if (expresion == null) {
            return TipoDato.ERROR;
        }

        /*
         * Los nodos cachean su tipo despues de la primera evaluacion. Esto
         * evita repetir busquedas y tambien conserva el tipo para la etapa de
         * codigo intermedio, por ejemplo en llamadas void vs llamadas con
         * retorno.
         */
        TipoDato tipoActual = expresion.getTipo();
        if (tipoActual != TipoDato.DESCONOCIDO) {
            return tipoActual;
        }

        TipoDato tipo = TipoDato.ERROR;
        if (expresion instanceof IdentificadorNodo) {
            IdentificadorNodo id = (IdentificadorNodo) expresion;
            tipo = tablaSimbolos.buscar(id.getNombre(), id.getLinea()).getTipo();
        } else if (expresion instanceof AccesoArregloNodo) {
            AccesoArregloNodo acceso = (AccesoArregloNodo) expresion;
            evaluarTipo(acceso.getFila());
            evaluarTipo(acceso.getColumnaIndice());
            tipo = tablaSimbolos.buscar(acceso.getNombre(), acceso.getLinea()).getTipo();
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

    public TipoDato tipoExpresion(ExpresionNodo expresion) {
        return evaluarTipo(expresion);
    }

    public void verificarCondicionBooleana(ExpresionNodo condicion) {
        TipoDato tipo = evaluarTipo(condicion);
        if (tipo == TipoDato.ERROR || tipo == TipoDato.DESCONOCIDO) {
            return;
        }
        if (tipo != TipoDato.BOOL) {
            tablaSimbolos.reportarCondicionNoBooleana(tipo, condicion.getLinea());
        }
    }

    public void verificarReturn(ReturnNodo retorno) {
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

    private void registrarFuncion(String nombre, TipoDato tipoRetorno, int linea) {
        insertarSimbolo(new Simbolo(nombre, new ArrayList<TipoDato>(), tipoRetorno, linea));
    }

    private void insertarSimbolo(Simbolo simbolo) {
        try {
            tablaSimbolos.insertar(simbolo);
        } catch (IllegalArgumentException ex) {
            reportadorSintactico.accept(ex.getMessage());
        }
    }

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
            if (izquierda == derecha) {
                return TipoDato.BOOL;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, izquierda, derecha, expresion.getLinea());
            return TipoDato.ERROR;
        }
        if ("+".equals(operador) || "-".equals(operador) || "*".equals(operador)
                || "/".equals(operador) || "%".equals(operador) || "^".equals(operador)) {
            if (izquierda.esNumerico() && derecha.esNumerico() && izquierda == derecha) {
                return izquierda;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, izquierda, derecha, expresion.getLinea());
            return TipoDato.ERROR;
        }

        return TipoDato.ERROR;
    }

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
            if (tipo.esNumerico()) {
                return tipo;
            }
            tablaSimbolos.reportarOperacionIncompatible(operador, tipo, expresion.getLinea());
            return TipoDato.ERROR;
        }

        return TipoDato.ERROR;
    }

    private String nombreDestino(ExpresionNodo destino) {
        if (destino instanceof IdentificadorNodo) {
            return ((IdentificadorNodo) destino).getNombre();
        }
        if (destino instanceof AccesoArregloNodo) {
            return ((AccesoArregloNodo) destino).getNombre();
        }
        return "<desconocido>";
    }
}
