package intermedio;

import ast.AsignacionNodo;
import ast.BloqueNodo;
import ast.DeclaracionVariableNodo;
import ast.ExpresionBinariaNodo;
import ast.ExpresionNodo;
import ast.ExpresionSentenciaNodo;
import ast.ExpresionUnariaNodo;
import ast.FuncionNodo;
import ast.IdentificadorNodo;
import ast.IfNodo;
import ast.LiteralNodo;
import ast.LlamadaFuncionNodo;
import ast.Nodo;
import ast.ProgramaNodo;
import ast.ReturnNodo;
import ast.SalidaNodo;
import ast.TipoDato;
import ast.WhileNodo;
import java.util.ArrayList;
import java.util.List;

public class GeneradorCodigoIntermedio {
    private final List<Instruccion> instrucciones = new ArrayList<>();
    private int contadorTemporales;
    private int contadorEtiquetas;

    public List<Instruccion> generar(ProgramaNodo programa) {
        instrucciones.clear();
        contadorTemporales = 0;
        contadorEtiquetas = 0;
        for (FuncionNodo funcion : programa.getFunciones()) {
            generarFuncion(funcion);
        }
        return new ArrayList<>(instrucciones);
    }

    private void generarFuncion(FuncionNodo funcion) {
        instrucciones.add(new Instruccion(Operacion.INICIO_FUNC, funcion.getNombre()));
        generarBloque(funcion.getCuerpo());
        instrucciones.add(new Instruccion(Operacion.FIN_FUNC, funcion.getNombre()));
    }

    private void generarBloque(BloqueNodo bloque) {
        for (Nodo nodo : bloque.getInstrucciones()) {
            generarNodo(nodo);
        }
    }

    private void generarNodo(Nodo nodo) {
        if (nodo instanceof AsignacionNodo) {
            generarAsignacion((AsignacionNodo) nodo);
        } else if (nodo instanceof DeclaracionVariableNodo) {
            generarDeclaracionVariable((DeclaracionVariableNodo) nodo);
        } else if (nodo instanceof ReturnNodo) {
            generarReturn((ReturnNodo) nodo);
        } else if (nodo instanceof BloqueNodo) {
            generarBloque((BloqueNodo) nodo);
        } else if (nodo instanceof IfNodo) {
            generarIf((IfNodo) nodo);
        } else if (nodo instanceof WhileNodo) {
            generarWhile((WhileNodo) nodo);
        } else if (nodo instanceof ExpresionSentenciaNodo) {
            generarExpresionSentencia((ExpresionSentenciaNodo) nodo);
        } else if (nodo instanceof SalidaNodo) {
            generarSalida((SalidaNodo) nodo);
        }
    }

    private void generarDeclaracionVariable(DeclaracionVariableNodo declaracion) {
        if (declaracion.getInicializador() == null) {
            return;
        }
        String valor = generarExpresion(declaracion.getInicializador());
        instrucciones.add(new Instruccion(Operacion.ASIG, declaracion.getNombre(), valor));
    }

    private void generarAsignacion(AsignacionNodo asignacion) {
        String valor = generarExpresion(asignacion.getValor());
        String destino = generarExpresion(asignacion.getDestino());
        instrucciones.add(new Instruccion(Operacion.ASIG, destino, valor));
    }

    private void generarReturn(ReturnNodo retorno) {
        String valor = retorno.getValor() == null ? null : generarExpresion(retorno.getValor());
        instrucciones.add(new Instruccion(Operacion.RETURN, null, valor));
    }

    private void generarExpresionSentencia(ExpresionSentenciaNodo sentencia) {
        generarExpresion(sentencia.getExpresion());
    }

    private void generarSalida(SalidaNodo salida) {
        instrucciones.add(new Instruccion(Operacion.PRINT, generarExpresion(salida.getValor())));
    }

    private void generarIf(IfNodo sentencia) {
        String condicion = generarExpresion(sentencia.getCondicion());
        String etiquetaElseOFin = nuevaEtiqueta();

        instrucciones.add(new Instruccion(Operacion.IF_FALSE, etiquetaElseOFin, condicion));
        generarBloque(sentencia.getBloqueEntonces());

        if (sentencia.getBloqueSino() == null) {
            instrucciones.add(new Instruccion(Operacion.LABEL, etiquetaElseOFin));
            return;
        }

        String etiquetaFin = nuevaEtiqueta();
        instrucciones.add(new Instruccion(Operacion.GOTO, etiquetaFin));
        instrucciones.add(new Instruccion(Operacion.LABEL, etiquetaElseOFin));
        generarBloque(sentencia.getBloqueSino());
        instrucciones.add(new Instruccion(Operacion.LABEL, etiquetaFin));
    }

    private void generarWhile(WhileNodo sentencia) {
        String etiquetaInicio = nuevaEtiqueta();
        String etiquetaSalida = nuevaEtiqueta();

        instrucciones.add(new Instruccion(Operacion.LABEL, etiquetaInicio));

        if (sentencia.isDoWhile()) {
            generarBloque(sentencia.getCuerpo());
            String condicion = generarExpresion(sentencia.getCondicion());
            instrucciones.add(new Instruccion(Operacion.IF_FALSE, etiquetaSalida, condicion));
            instrucciones.add(new Instruccion(Operacion.GOTO, etiquetaInicio));
            instrucciones.add(new Instruccion(Operacion.LABEL, etiquetaSalida));
            return;
        }

        String condicion = generarExpresion(sentencia.getCondicion());
        instrucciones.add(new Instruccion(Operacion.IF_FALSE, etiquetaSalida, condicion));
        generarBloque(sentencia.getCuerpo());
        instrucciones.add(new Instruccion(Operacion.GOTO, etiquetaInicio));
        instrucciones.add(new Instruccion(Operacion.LABEL, etiquetaSalida));
    }

    private String generarExpresion(ExpresionNodo expresion) {
        if (expresion instanceof IdentificadorNodo) {
            return ((IdentificadorNodo) expresion).getNombre();
        }
        if (expresion instanceof LiteralNodo) {
            Object valor = ((LiteralNodo) expresion).getValor();
            return valor == null ? "null" : valor.toString();
        }
        if (expresion instanceof ExpresionBinariaNodo) {
            return generarBinaria((ExpresionBinariaNodo) expresion);
        }
        if (expresion instanceof ExpresionUnariaNodo) {
            return generarUnaria((ExpresionUnariaNodo) expresion);
        }
        if (expresion instanceof LlamadaFuncionNodo) {
            return generarLlamada((LlamadaFuncionNodo) expresion);
        }
        return "<expr>";
    }

    private String generarBinaria(ExpresionBinariaNodo expresion) {
        String izquierda = generarExpresion(expresion.getIzquierda());
        String derecha = generarExpresion(expresion.getDerecha());
        String temporal = nuevoTemporal();
        instrucciones.add(new Instruccion(operacionBinaria(expresion.getOperador()), temporal, izquierda, derecha));
        return temporal;
    }

    private String generarUnaria(ExpresionUnariaNodo expresion) {
        String valor = generarExpresion(expresion.getExpresion());
        String temporal = nuevoTemporal();
        instrucciones.add(new Instruccion(operacionUnaria(expresion.getOperador()), temporal, valor));
        return temporal;
    }

    private String generarLlamada(LlamadaFuncionNodo llamada) {
        for (ExpresionNodo argumento : llamada.getArgumentos()) {
            instrucciones.add(new Instruccion(Operacion.PARAM, generarExpresion(argumento)));
        }
        if (esLlamadaVoid(llamada)) {
            instrucciones.add(new Instruccion(Operacion.CALL, null, llamada.getNombre(),
                    String.valueOf(llamada.getArgumentos().size())));
            return null;
        }
        String temporal = nuevoTemporal();
        instrucciones.add(new Instruccion(Operacion.CALL, temporal, llamada.getNombre(),
                String.valueOf(llamada.getArgumentos().size())));
        return temporal;
    }

    private boolean esLlamadaVoid(LlamadaFuncionNodo llamada) {
        return llamada.getTipo() == TipoDato.VOID || llamada.getTipo() == TipoDato.EMPTY;
    }

    private String nuevoTemporal() {
        return "_t" + contadorTemporales++;
    }

    private String nuevaEtiqueta() {
        return "_L" + contadorEtiquetas++;
    }

    private Operacion operacionBinaria(String operador) {
        switch (operador) {
            case "+":
                return Operacion.SUMA;
            case "-":
                return Operacion.RESTA;
            case "*":
                return Operacion.MULT;
            case "/":
                return Operacion.DIV;
            case "#":
                return Operacion.OR;
            case "@":
                return Operacion.AND;
            case "equal":
                return Operacion.IGUAL;
            case "n_equal":
                return Operacion.DISTINTO;
            case "less_t":
                return Operacion.MENOR;
            case "greather_t":
                return Operacion.MAYOR;
            case "less_te":
                return Operacion.MENOR_IGUAL;
            case "greather_te":
                return Operacion.MAYOR_IGUAL;
            default:
                throw new IllegalArgumentException("Operador binario no soportado: " + operador);
        }
    }

    private Operacion operacionUnaria(String operador) {
        switch (operador) {
            case "-":
                return Operacion.NEG;
            case "$":
                return Operacion.NOT;
            default:
                throw new IllegalArgumentException("Operador unario no soportado: " + operador);
        }
    }
}
