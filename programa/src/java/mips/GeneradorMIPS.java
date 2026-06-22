package mips;

import intermedio.Instruccion;
import intermedio.Operacion;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Traduce instrucciones de tres direcciones a ensamblador MIPS para SPIM. */
public final class GeneradorMIPS {
    private final List<String> salida = new ArrayList<>();
    private final AdministradorRegistros registros = new AdministradorRegistros();
    private final Map<String, String> tipos = new LinkedHashMap<>();
    /** Relacion estable entre cada variable/temporal 3D y su etiqueta en .data. */
    private final Map<String, String> direcciones = new LinkedHashMap<>();
    private final Map<String, Integer> columnasArreglo = new LinkedHashMap<>();
    private final Map<String, String> cadenas = new LinkedHashMap<>();
    private final Map<String, String> flotantes = new LinkedHashMap<>();
    private final Map<String, Integer> parametrosFuncion = new LinkedHashMap<>();
    private final Map<String, String> retornosFuncion = new LinkedHashMap<>();
    private int contadorCadena;
    private int contadorFlotante;
    private int contadorEtiquetaInterna;
    private String funcionActual;
    private int indiceParametroFormal;

    /** Genera un programa MIPS completo a partir del codigo intermedio validado. */
    public List<String> generarCodigo(List<Instruccion> codigoIntermedio) {
        reiniciar();
        analizar(codigoIntermedio);
        emitirDatos();
        salida.add(".text");
        salida.add(".globl main");
        traducir(codigoIntermedio);
        return new ArrayList<>(salida);
    }

    private void reiniciar() {
        salida.clear();
        registros.reiniciar();
        tipos.clear();
        direcciones.clear();
        columnasArreglo.clear();
        dimensionesDeclaradas.clear();
        cadenas.clear();
        flotantes.clear();
        parametrosFuncion.clear();
        retornosFuncion.clear();
        contadorCadena = 0;
        contadorFlotante = 0;
        contadorEtiquetaInterna = 0;
        funcionActual = null;
        indiceParametroFormal = 0;
    }

    private void analizar(List<Instruccion> codigo) {
        String funcion = null;
        for (Instruccion instruccion : codigo) {
            if (instruccion.op == Operacion.INICIO_FUNC) {
                funcion = instruccion.resultado;
                parametrosFuncion.put(funcion, 0);
                continue;
            }
            if (instruccion.op == Operacion.FIN_FUNC) {
                funcion = null;
                continue;
            }
            if (funcion == null) {
                continue;
            }
            if (instruccion.op == Operacion.DECL || instruccion.op == Operacion.FORMAL_PARAM) {
                tipos.put(clave(funcion, instruccion.resultado), normalizarTipo(instruccion.op1));
                if (instruccion.op == Operacion.FORMAL_PARAM) {
                    parametrosFuncion.put(funcion, parametrosFuncion.get(funcion) + 1);
                }
            } else if (instruccion.op == Operacion.DECL_ARRAY) {
                tipos.put(clave(funcion, instruccion.resultado), normalizarTipo(instruccion.op1));
                int[] dimensiones = dimensiones(instruccion.op2);
                columnasArreglo.put(clave(funcion, instruccion.resultado), dimensiones[1]);
                dimensionesDeclaradas.put(clave(funcion, instruccion.resultado),
                        dimensiones[0] * dimensiones[1]);
            }
            registrarConstante(instruccion.op1);
            registrarConstante(instruccion.op2);
        }

        // Propaga tipos de temporales y resultados hasta alcanzar un punto fijo.
        for (int vuelta = 0; vuelta < 4; vuelta++) {
            funcion = null;
            for (Instruccion i : codigo) {
                if (i.op == Operacion.INICIO_FUNC) {
                    funcion = i.resultado;
                    continue;
                }
                if (i.op == Operacion.FIN_FUNC) {
                    funcion = null;
                    continue;
                }
                if (funcion == null || i.resultado == null) {
                    if (funcion != null && i.op == Operacion.RETURN && i.op1 != null) {
                        retornosFuncion.put(funcion, tipoOperando(i.op1, funcion));
                    }
                    continue;
                }
                String tipo = tipoResultado(i, funcion);
                if (tipo != null) {
                    tipos.put(clave(funcion, i.resultado), tipo);
                }
            }
        }
        construirTablaDirecciones();
    }

    private void construirTablaDirecciones() {
        Map<String, Integer> repeticiones = new LinkedHashMap<>();
        for (String clave : tipos.keySet()) {
            String base = etiquetaDato(clave);
            int repeticion = repeticiones.getOrDefault(base, 0);
            repeticiones.put(base, repeticion + 1);
            direcciones.put(clave, repeticion == 0 ? base : base + "_" + repeticion);
        }
    }

    private String tipoResultado(Instruccion i, String funcion) {
        switch (i.op) {
            case LOAD:
            case NEG:
                return tipoOperando(i.op1, funcion);
            case CALL:
                return retornosFuncion.getOrDefault(i.op1, "int");
            case SUMA:
            case RESTA:
            case MULT:
            case DIV:
            case MOD:
            case POW:
                return esFloat(tipoOperando(i.op1, funcion))
                        || esFloat(tipoOperando(i.op2, funcion)) ? "float" : "int";
            case AND:
            case OR:
            case NOT:
            case IGUAL:
            case DISTINTO:
            case MENOR:
            case MAYOR:
            case MENOR_IGUAL:
            case MAYOR_IGUAL:
                return "bool";
            default:
                return null;
        }
    }

    private void emitirDatos() {
        salida.add(".data");
        for (Map.Entry<String, String> entrada : tipos.entrySet()) {
            String etiqueta = direccionDato(entrada.getKey());
            if (columnasArreglo.containsKey(entrada.getKey())) {
                // El espacio exacto se reemplaza durante el segundo recorrido de declaraciones.
                salida.add(etiqueta + ": .space " + espacioArreglo(entrada.getKey()));
            } else if (esFloat(entrada.getValue())) {
                salida.add(etiqueta + ": .float 0.0");
            } else {
                salida.add(etiqueta + ": .word 0");
            }
        }
        for (Map.Entry<String, String> entrada : cadenas.entrySet()) {
            salida.add(entrada.getValue() + ": .asciiz " + entrada.getKey());
        }
        for (Map.Entry<String, String> entrada : flotantes.entrySet()) {
            salida.add(entrada.getValue() + ": .float " + valorFloat(entrada.getKey()));
        }
        salida.add("");
    }

    private int espacioArreglo(String clave) {
        return dimensionesDeclaradas.getOrDefault(clave, 1) * 4;
    }

    private final Map<String, Integer> dimensionesDeclaradas = new LinkedHashMap<>();

    private void traducir(List<Instruccion> codigo) {
        funcionActual = null;
        for (int indice = 0; indice < codigo.size(); indice++) {
            Instruccion i = codigo.get(indice);
            if (indice + 1 < codigo.size()
                    && puedeFusionarSalto(i, codigo.get(indice + 1))) {
                traducirSaltoComparacion(i, codigo.get(indice + 1).resultado);
                indice++;
                continue;
            }
            switch (i.op) {
                case INICIO_FUNC:
                    iniciarFuncion(i.resultado);
                    break;
                case FIN_FUNC:
                    finalizarFuncion(i.resultado);
                    break;
                case DECL:
                case DECL_ARRAY:
                    break;
                case FORMAL_PARAM:
                    traducirParametroFormal(i);
                    break;
                case LOAD:
                case ASIG:
                case STORE_ARRAY:
                    traducirTransferencia(i);
                    break;
                case SUMA:
                case RESTA:
                case MULT:
                case DIV:
                case MOD:
                case POW:
                case AND:
                case OR:
                case IGUAL:
                case DISTINTO:
                case MENOR:
                case MAYOR:
                case MENOR_IGUAL:
                case MAYOR_IGUAL:
                    traducirBinaria(i);
                    break;
                case NEG:
                case NOT:
                    traducirUnaria(i);
                    break;
                case LABEL:
                    salida.add(etiquetaCodigo(i.resultado) + ":");
                    break;
                case GOTO:
                    instruccion("j " + etiquetaCodigo(i.resultado));
                    break;
                case IF_FALSE:
                    String condicion = cargarValor(i.op1);
                    instruccion("beq " + condicion + ", $zero, " + etiquetaCodigo(i.resultado));
                    registros.liberarRegistro(condicion);
                    break;
                case PARAM:
                    String argumento = i.op1 != null ? i.op1 : i.resultado;
                    String registroParametro = registros.obtenerRegistro();
                    if (esFloat(tipoOperando(argumento, funcionActual))) {
                        cargarFloat(argumento, "$f0");
                        instruccion("mfc1 " + registroParametro + ", $f0");
                    } else {
                        cargarEntero(argumento, registroParametro);
                    }
                    instruccion("addiu $sp, $sp, -4");
                    instruccion("sw " + registroParametro + ", 0($sp)");
                    registros.liberarRegistro(registroParametro);
                    break;
                case CALL:
                    traducirLlamada(i);
                    break;
                case RETURN:
                    traducirRetorno(i);
                    break;
                case PRINT:
                    traducirPrint(i.op1 != null ? i.op1 : i.resultado);
                    break;
                case READ:
                    traducirRead(i.op1 != null ? i.op1 : i.resultado);
                    break;
                default:
                    instruccion("# Operacion no implementada: " + i.op);
            }
        }
    }

    private boolean puedeFusionarSalto(Instruccion comparacion, Instruccion salto) {
        return esComparacion(comparacion.op)
                && salto.op == Operacion.IF_FALSE
                && comparacion.resultado != null
                && comparacion.resultado.equals(salto.op1)
                && !esFloat(tipoOperando(comparacion.op1, funcionActual))
                && !esFloat(tipoOperando(comparacion.op2, funcionActual));
    }

    /** Traduce directamente el caso falso de una comparacion hacia su etiqueta 3D. */
    private void traducirSaltoComparacion(Instruccion comparacion, String destino) {
        String izquierdo = cargarValor(comparacion.op1);
        String derecho = cargarValor(comparacion.op2);
        String operacion;
        switch (comparacion.op) {
            case IGUAL: operacion = "bne"; break;
            case DISTINTO: operacion = "beq"; break;
            case MENOR: operacion = "bge"; break;
            case MENOR_IGUAL: operacion = "bgt"; break;
            case MAYOR: operacion = "ble"; break;
            case MAYOR_IGUAL: operacion = "blt"; break;
            default: throw new IllegalStateException("Comparacion no soportada en salto: " + comparacion.op);
        }
        instruccion(operacion + " " + izquierdo + ", " + derecho + ", " + etiquetaCodigo(destino));
        registros.liberarRegistro(derecho);
        registros.liberarRegistro(izquierdo);
    }

    private void traducirTransferencia(Instruccion i) {
        if (esFloat(tipoOperando(i.op1, funcionActual))) {
            cargarFloat(i.op1, "$f0");
            guardar(i.resultado, null, "$f0");
            return;
        }
        String registro = cargarValor(i.op1);
        guardar(i.resultado, registro, null);
        registros.liberarRegistro(registro);
    }

    private void iniciarFuncion(String nombre) {
        funcionActual = nombre;
        indiceParametroFormal = 0;
        salida.add("");
        salida.add(etiquetaFuncion(nombre) + ":");
        if (!"__main__".equals(nombre)) {
            instruccion("addiu $sp, $sp, -4");
            instruccion("sw $ra, 0($sp)");
        }
    }

    private void finalizarFuncion(String nombre) {
        salida.add(etiquetaEpilogo(nombre) + ":");
        if ("__main__".equals(nombre)) {
            instruccion("li $v0, 10");
            instruccion("syscall");
        } else {
            instruccion("lw $ra, 0($sp)");
            instruccion("addiu $sp, $sp, 4");
            instruccion("jr $ra");
        }
        funcionActual = null;
    }

    private void traducirParametroFormal(Instruccion i) {
        int total = parametrosFuncion.getOrDefault(funcionActual, 0);
        int desplazamiento = 4 * (total - indiceParametroFormal);
        String registro = registros.obtenerRegistro();
        instruccion("lw " + registro + ", " + desplazamiento + "($sp)");
        instruccion("sw " + registro + ", " + etiqueta(i.resultado));
        registros.liberarRegistro(registro);
        indiceParametroFormal++;
    }

    private void traducirLlamada(Instruccion i) {
        instruccion("jal " + etiquetaFuncion(i.op1));
        int cantidad = parseEntero(i.op2, 0);
        if (cantidad > 0) {
            instruccion("addiu $sp, $sp, " + (cantidad * 4));
        }
        if (i.resultado != null) {
            if (esFloat(tipoOperando(i.resultado, funcionActual))) {
                instruccion("mtc1 $v0, $f0");
                instruccion("s.s $f0, " + etiqueta(i.resultado));
            } else {
                instruccion("sw $v0, " + etiqueta(i.resultado));
            }
        }
    }

    private void traducirRetorno(Instruccion i) {
        if (i.op1 != null) {
            if (esFloat(tipoOperando(i.op1, funcionActual))) {
                cargarFloat(i.op1, "$f0");
                instruccion("mfc1 $v0, $f0");
            } else {
                cargarEntero(i.op1, "$v0");
            }
        }
        instruccion("j " + etiquetaEpilogo(funcionActual));
    }

    private void traducirBinaria(Instruccion i) {
        boolean flotante = esFloat(tipoOperando(i.op1, funcionActual))
                || esFloat(tipoOperando(i.op2, funcionActual));
        if (flotante && esComparacion(i.op)) {
            traducirComparacionFloat(i);
            return;
        }
        if (flotante && esAritmetica(i.op)) {
            cargarFloat(i.op1, "$f0");
            cargarFloat(i.op2, "$f2");
            String operacion;
            switch (i.op) {
                case SUMA: operacion = "add.s"; break;
                case RESTA: operacion = "sub.s"; break;
                case MULT: operacion = "mul.s"; break;
                case DIV: operacion = "div.s"; break;
                case MOD:
                    instruccion("div.s $f4, $f0, $f2");
                    instruccion("trunc.w.s $f6, $f4");
                    instruccion("cvt.s.w $f6, $f6");
                    instruccion("mul.s $f6, $f6, $f2");
                    instruccion("sub.s $f4, $f0, $f6");
                    instruccion("s.s $f4, " + etiqueta(i.resultado));
                    return;
                case POW:
                    traducirPotenciaFloat(i.resultado);
                    return;
                default: throw new IllegalStateException("Operacion flotante no soportada: " + i.op);
            }
            instruccion(operacion + " $f4, $f0, $f2");
            instruccion("s.s $f4, " + etiqueta(i.resultado));
            return;
        }

        String izquierdo = cargarValor(i.op1);
        String derecho = cargarValor(i.op2);
        String resultado = registros.obtenerRegistro();
        switch (i.op) {
            case SUMA: instruccion("add " + resultado + ", " + izquierdo + ", " + derecho); break;
            case RESTA: instruccion("sub " + resultado + ", " + izquierdo + ", " + derecho); break;
            case MULT: instruccion("mul " + resultado + ", " + izquierdo + ", " + derecho); break;
            case DIV:
                instruccion("div " + izquierdo + ", " + derecho);
                instruccion("mflo " + resultado);
                break;
            case MOD:
                instruccion("div " + izquierdo + ", " + derecho);
                instruccion("mfhi " + resultado);
                break;
            case POW:
                traducirPotenciaEntera(izquierdo, derecho, resultado);
                break;
            case AND: instruccion("and " + resultado + ", " + izquierdo + ", " + derecho); break;
            case OR: instruccion("or " + resultado + ", " + izquierdo + ", " + derecho); break;
            case IGUAL: instruccion("seq " + resultado + ", " + izquierdo + ", " + derecho); break;
            case DISTINTO: instruccion("sne " + resultado + ", " + izquierdo + ", " + derecho); break;
            case MENOR: instruccion("slt " + resultado + ", " + izquierdo + ", " + derecho); break;
            case MAYOR: instruccion("sgt " + resultado + ", " + izquierdo + ", " + derecho); break;
            case MENOR_IGUAL: instruccion("sle " + resultado + ", " + izquierdo + ", " + derecho); break;
            case MAYOR_IGUAL: instruccion("sge " + resultado + ", " + izquierdo + ", " + derecho); break;
            default: throw new IllegalStateException("Operacion binaria no soportada: " + i.op);
        }
        instruccion("sw " + resultado + ", " + etiqueta(i.resultado));
        registros.liberarRegistro(resultado);
        registros.liberarRegistro(derecho);
        registros.liberarRegistro(izquierdo);
    }

    private void traducirComparacionFloat(Instruccion i) {
        cargarFloat(i.op1, "$f0");
        cargarFloat(i.op2, "$f2");
        String verdadero = nuevaEtiquetaInterna("cmp_true");
        String fin = nuevaEtiquetaInterna("cmp_fin");
        String resultado = registros.obtenerRegistro();
        instruccion("li " + resultado + ", 0");
        switch (i.op) {
            case IGUAL:
                instruccion("c.eq.s $f0, $f2");
                instruccion("bc1t " + verdadero);
                break;
            case DISTINTO:
                instruccion("c.eq.s $f0, $f2");
                instruccion("bc1f " + verdadero);
                break;
            case MENOR:
                instruccion("c.lt.s $f0, $f2");
                instruccion("bc1t " + verdadero);
                break;
            case MENOR_IGUAL:
                instruccion("c.le.s $f0, $f2");
                instruccion("bc1t " + verdadero);
                break;
            case MAYOR:
                instruccion("c.lt.s $f2, $f0");
                instruccion("bc1t " + verdadero);
                break;
            case MAYOR_IGUAL:
                instruccion("c.le.s $f2, $f0");
                instruccion("bc1t " + verdadero);
                break;
            default: throw new IllegalStateException("Comparacion flotante no soportada: " + i.op);
        }
        instruccion("j " + fin);
        salida.add(verdadero + ":");
        instruccion("li " + resultado + ", 1");
        salida.add(fin + ":");
        instruccion("sw " + resultado + ", " + etiqueta(i.resultado));
        registros.liberarRegistro(resultado);
    }

    private void traducirPotenciaFloat(String resultado) {
        String ciclo = nuevaEtiquetaInterna("powf");
        String fin = nuevaEtiquetaInterna("powf_fin");
        String contador = registros.obtenerRegistro();
        instruccion("li " + contador + ", 1");
        instruccion("mtc1 " + contador + ", $f4");
        instruccion("cvt.s.w $f4, $f4");
        instruccion("trunc.w.s $f6, $f2");
        instruccion("mfc1 " + contador + ", $f6");
        salida.add(ciclo + ":");
        instruccion("blez " + contador + ", " + fin);
        instruccion("mul.s $f4, $f4, $f0");
        instruccion("addiu " + contador + ", " + contador + ", -1");
        instruccion("j " + ciclo);
        salida.add(fin + ":");
        instruccion("s.s $f4, " + etiqueta(resultado));
        registros.liberarRegistro(contador);
    }

    private void traducirPotenciaEntera(String base, String exponente, String resultado) {
        String ciclo = nuevaEtiquetaInterna("pow");
        String fin = nuevaEtiquetaInterna("pow_fin");
        instruccion("li " + resultado + ", 1");
        salida.add(ciclo + ":");
        instruccion("blez " + exponente + ", " + fin);
        instruccion("mul " + resultado + ", " + resultado + ", " + base);
        instruccion("addiu " + exponente + ", " + exponente + ", -1");
        instruccion("j " + ciclo);
        salida.add(fin + ":");
    }

    private void traducirUnaria(Instruccion i) {
        if (i.op == Operacion.NEG && esFloat(tipoOperando(i.op1, funcionActual))) {
            cargarFloat(i.op1, "$f0");
            instruccion("neg.s $f2, $f0");
            instruccion("s.s $f2, " + etiqueta(i.resultado));
            return;
        }
        String operando = cargarValor(i.op1);
        String resultado = registros.obtenerRegistro();
        if (i.op == Operacion.NEG) {
            instruccion("sub " + resultado + ", $zero, " + operando);
        } else {
            instruccion("seq " + resultado + ", " + operando + ", $zero");
        }
        instruccion("sw " + resultado + ", " + etiqueta(i.resultado));
        registros.liberarRegistro(resultado);
        registros.liberarRegistro(operando);
    }

    private void traducirPrint(String operando) {
        String tipo = tipoOperando(operando, funcionActual);
        if ("string".equals(tipo)) {
            cargarEntero(operando, "$a0");
            instruccion("li $v0, 4");
        } else if ("char".equals(tipo)) {
            cargarEntero(operando, "$a0");
            instruccion("li $v0, 11");
        } else if (esFloat(tipo)) {
            cargarFloat(operando, "$f12");
            instruccion("li $v0, 2");
        } else {
            cargarEntero(operando, "$a0");
            instruccion("li $v0, 1");
        }
        instruccion("syscall");
    }

    private void traducirRead(String destino) {
        if (esFloat(tipoOperando(destino, funcionActual))) {
            instruccion("li $v0, 6");
            instruccion("syscall");
            guardar(destino, "$t0", "$f0");
        } else {
            instruccion("li $v0, 5");
            instruccion("syscall");
            guardar(destino, "$v0", "$f0");
        }
    }

    /** Obtiene un temporal administrado y carga en el el valor entero solicitado. */
    private String cargarValor(String operando) {
        String registro = registros.obtenerRegistro();
        cargarEntero(operando, registro);
        return registro;
    }

    private void cargarEntero(String operando, String registro) {
        if (operando == null) {
            instruccion("move " + registro + ", $zero");
        } else if (esCadena(operando)) {
            instruccion("la " + registro + ", " + cadenas.get(operando));
        } else if (esChar(operando)) {
            instruccion("li " + registro + ", " + (int) valorChar(operando));
        } else if ("true".equals(operando) || "false".equals(operando)) {
            instruccion("li " + registro + ", " + ("true".equals(operando) ? 1 : 0));
        } else if (esEnteroLiteral(operando)) {
            instruccion("li " + registro + ", " + valorEntero(operando));
        } else if (esAccesoArreglo(operando)) {
            direccionArreglo(operando, "$t7");
            instruccion("lw " + registro + ", 0($t7)");
        } else {
            instruccion("lw " + registro + ", " + etiqueta(operando));
        }
    }

    private void cargarFloat(String operando, String registro) {
        if (esFloatLiteral(operando)) {
            instruccion("l.s " + registro + ", " + flotantes.get(operando));
        } else if (esEnteroLiteral(operando)) {
            cargarEntero(operando, "$t6");
            instruccion("mtc1 $t6, " + registro);
            instruccion("cvt.s.w " + registro + ", " + registro);
        } else if (esAccesoArreglo(operando)) {
            direccionArreglo(operando, "$t7");
            instruccion("l.s " + registro + ", 0($t7)");
        } else {
            instruccion("l.s " + registro + ", " + etiqueta(operando));
        }
    }

    private void guardar(String destino, String registroEntero, String registroFloat) {
        if (esAccesoArreglo(destino)) {
            direccionArreglo(destino, "$t7");
            instruccion((esFloat(tipoOperando(destino, funcionActual)) ? "s.s " + registroFloat
                    : "sw " + registroEntero) + ", 0($t7)");
        } else if (esFloat(tipoOperando(destino, funcionActual))) {
            instruccion("s.s " + registroFloat + ", " + etiqueta(destino));
        } else {
            instruccion("sw " + registroEntero + ", " + etiqueta(destino));
        }
    }

    private void direccionArreglo(String acceso, String registroDireccion) {
        int primero = acceso.indexOf('[');
        String nombre = acceso.substring(0, primero);
        int cierreFila = acceso.indexOf(']', primero);
        int inicioColumna = acceso.indexOf('[', cierreFila);
        int cierreColumna = acceso.indexOf(']', inicioColumna);
        String fila = acceso.substring(primero + 1, cierreFila);
        String columna = acceso.substring(inicioColumna + 1, cierreColumna);
        cargarEntero(fila, "$t8");
        cargarEntero(columna, "$t9");
        int columnas = columnasArreglo.getOrDefault(clave(funcionActual, nombre), 1);
        instruccion("li $t6, " + columnas);
        instruccion("mul $t8, $t8, $t6");
        instruccion("add $t8, $t8, $t9");
        instruccion("sll $t8, $t8, 2");
        instruccion("la " + registroDireccion + ", " + etiqueta(nombre));
        instruccion("add " + registroDireccion + ", " + registroDireccion + ", $t8");
    }

    private String tipoOperando(String operando, String funcion) {
        if (operando == null) return "int";
        if (esCadena(operando)) return "string";
        if (esChar(operando)) return "char";
        if ("true".equals(operando) || "false".equals(operando)) return "bool";
        if (esFloatLiteral(operando)) return "float";
        if (esEnteroLiteral(operando)) return "int";
        String base = esAccesoArreglo(operando) ? operando.substring(0, operando.indexOf('[')) : operando;
        return tipos.getOrDefault(clave(funcion, base), "int");
    }

    private void registrarConstante(String valor) {
        if (esCadena(valor)) {
            cadenas.computeIfAbsent(valor, k -> "_str_" + contadorCadena++);
        } else if (esFloatLiteral(valor)) {
            flotantes.computeIfAbsent(valor, k -> "_flt_" + contadorFlotante++);
        }
    }

    private String etiqueta(String operando) {
        String base = esAccesoArreglo(operando) ? operando.substring(0, operando.indexOf('[')) : operando;
        return direccionDato(clave(funcionActual, base));
    }

    private String direccionDato(String clave) {
        String direccion = direcciones.get(clave);
        if (direccion == null) {
            throw new IllegalStateException("No se reservo memoria MIPS para " + clave);
        }
        return direccion;
    }

    private static String clave(String funcion, String nombre) {
        return (funcion == null ? "global" : funcion) + "::" + nombre;
    }

    private static String etiquetaDato(String clave) {
        return "_d_" + limpiar(clave.replace("::", "__"));
    }

    private static String etiquetaFuncion(String nombre) {
        return "__main__".equals(nombre) ? "main" : "_fn_" + limpiar(nombre);
    }

    private static String etiquetaEpilogo(String nombre) {
        return etiquetaFuncion(nombre) + "__fin";
    }

    private static String etiquetaCodigo(String nombre) {
        return "_ic_" + limpiar(nombre);
    }

    private String nuevaEtiquetaInterna(String prefijo) {
        return "_mips_" + prefijo + "_" + contadorEtiquetaInterna++;
    }

    private void instruccion(String texto) {
        salida.add("\t" + texto);
    }

    private static String limpiar(String texto) {
        return texto.replaceAll("[^A-Za-z0-9_]", "_");
    }

    private static String normalizarTipo(String tipo) {
        return tipo == null ? "int" : tipo.toLowerCase(Locale.ROOT);
    }

    private static boolean esFloat(String tipo) {
        return "float".equals(tipo);
    }

    private static boolean esAritmetica(Operacion op) {
        return op == Operacion.SUMA || op == Operacion.RESTA || op == Operacion.MULT
                || op == Operacion.DIV || op == Operacion.MOD || op == Operacion.POW;
    }

    private static boolean esComparacion(Operacion op) {
        return op == Operacion.IGUAL || op == Operacion.DISTINTO || op == Operacion.MENOR
                || op == Operacion.MAYOR || op == Operacion.MENOR_IGUAL
                || op == Operacion.MAYOR_IGUAL;
    }

    private static boolean esCadena(String valor) {
        return valor != null && valor.length() >= 2 && valor.startsWith("\"") && valor.endsWith("\"");
    }

    private static boolean esChar(String valor) {
        return valor != null && valor.length() >= 3 && valor.startsWith("'") && valor.endsWith("'");
    }

    private static char valorChar(String valor) {
        return valor.charAt(1);
    }

    private static boolean esFloatLiteral(String valor) {
        return valor != null && (valor.matches("[0-9]+\\.[0-9]+")
                || valor.matches("[0-9]+/[1-9][0-9]*"));
    }

    private static boolean esEnteroLiteral(String valor) {
        return valor != null && (valor.matches("[0-9]+") || valor.matches("[0-9]+e[1-9][0-9]*"));
    }

    private static boolean esAccesoArreglo(String valor) {
        return valor != null && valor.matches("[A-Za-z_][A-Za-z0-9_]*\\[[^]]+\\]\\[[^]]+\\]");
    }

    private static int valorEntero(String valor) {
        if (valor.contains("e")) {
            String[] partes = valor.split("e", 2);
            double calculado = Double.parseDouble(partes[0]) * Math.pow(10, Integer.parseInt(partes[1]));
            return (int) calculado;
        }
        return Integer.parseInt(valor);
    }

    private static String valorFloat(String valor) {
        if (valor.contains("/")) {
            String[] partes = valor.split("/", 2);
            return String.valueOf(Double.parseDouble(partes[0]) / Double.parseDouble(partes[1]));
        }
        return valor;
    }

    private static int parseEntero(String valor, int defecto) {
        try {
            return Integer.parseInt(valor);
        } catch (RuntimeException ex) {
            return defecto;
        }
    }

    private int[] dimensiones(String texto) {
        if (texto != null && texto.matches("\\[[0-9]+\\]\\[[0-9]+\\]")) {
            int medio = texto.indexOf("][");
            int filas = Integer.parseInt(texto.substring(1, medio));
            int columnas = Integer.parseInt(texto.substring(medio + 2, texto.length() - 1));
            return new int[] { filas, columnas };
        }
        return new int[] { 1, 1 };
    }
}
