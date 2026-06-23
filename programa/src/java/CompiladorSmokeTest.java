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
import ast.ParametroNodo;
import ast.ProgramaNodo;
import ast.ReturnNodo;
import ast.SalidaNodo;
import ast.TipoDato;
import ast.WhileNodo;
import intermedio.GeneradorCodigoIntermedio;
import intermedio.Instruccion;
import intermedio.Operacion;
import mips.AdministradorRegistros;
import mips.GeneradorMIPS;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import pipeline.Compilador;
import pipeline.ResultadoCompilacion;
import reporte.EscritorCodigo;
import reporte.EscritorMIPS;

/**
 * <strong>Nombre:</strong> CompiladorSmokeTest
 *
 * <p><strong>Objetivo:</strong> Prueba de humo ejecutable que valida el flujo principal del compilador:
 * análisis, generación de código intermedio y generación de MIPS, mediante aserciones de regresión.</p>
 *
 * <p><strong>Entrada:</strong> Archivos de prueba en disco y fragmentos de código fuente embebidos.</p>
 *
 * <p><strong>Salida:</strong> Termina con éxito si todo pasa, o lanza AssertionError ante la primera falla.</p>
 *
 * <p><strong>Restricciones:</strong> Es código de prueba; no forma parte del compilador en producción.</p>
 */
public class CompiladorSmokeTest {
    /**
     * <strong>Nombre:</strong> main
     *
     * <p><strong>Objetivo:</strong> Ejecutar todos los escenarios de regresión del compilador en orden.</p>
     *
     * <p><strong>Entrada:</strong> String[] args (no se usan).</p>
     *
     * <p><strong>Salida:</strong> No retorna valor; falla con AssertionError si una garantía básica se rompe.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public static void main(String[] args) throws Exception {
        Compilador compilador = new Compilador();

        verificarValidacionArchivoFuente(compilador);
        verificarDiagnosticoFinArchivo(compilador);

        ResultadoCompilacion valido = compilador.compilar(
                Paths.get("test_verificacion/01_minimo_valido.chip"));
        if (!valido.isAceptado()) {
            throw new AssertionError("El caso minimo valido debe ser aceptado.");
        }
        if (valido.getCodigoIntermedio().isEmpty()) {
            throw new AssertionError("El caso minimo valido debe generar codigo intermedio.");
        }

        ResultadoCompilacion invalido = compilador.compilar(
                Paths.get("test_verificacion/21_asignaciones_tipado_fuerte.chip"));
        if (invalido.isAceptado()) {
            throw new AssertionError("El caso con errores semanticos debe ser rechazado.");
        }

        verificarCodigoIntermedioExpresiones();
        verificarCodigoIntermedioDeclaracionesYAsignaciones();
        verificarCodigoIntermedioIf();
        verificarCodigoIntermedioIfElse();
        verificarCodigoIntermedioWhile();
        verificarCodigoIntermedioSalida();
        verificarEtiquetasUnicasEntreIfYWhile();
        verificarCodigoIntermedioFuncionesYLlamadas();
        verificarGeneracionIntermediaSolicitada(compilador);
        verificarPrecedenciaPotenciaYSigno(compilador);
        verificarCorreccionesSemanticas(compilador);
        verificarRestriccionesSemanticasSolicitadas(compilador);
        verificarPruebaSemanticaExtensa(compilador);
        verificarPruebaBalanceGeneral(compilador);
        verificarEscritorCodigo(compilador, valido, invalido);
        verificarGeneracionMIPS(compilador, valido, invalido);
    }

    /**
     * <strong>Nombre:</strong> verificarValidacionArchivoFuente
     *
     * <p><strong>Objetivo:</strong> Comprobar que las rutas de fuente inválidas se rechacen antes de construir el lexer.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    private static void verificarValidacionArchivoFuente(Compilador compilador) throws Exception {
        Path directorio = Files.createTempDirectory("fuente-invalida-");
        Path inexistente = directorio.resolve("no-existe.chip");
        assertFallaLecturaFuente(compilador, inexistente, "No existe el archivo fuente");
        assertFallaLecturaFuente(compilador, directorio,
                "La ruta no corresponde a un archivo regular");
        assertFallaLecturaFuente(compilador, null, "No se proporciono una ruta");
    }

    /**
     * <strong>Nombre:</strong> assertFallaLecturaFuente
     *
     * <p><strong>Objetivo:</strong> Verificar que compilar una fuente inválida lance IOException con el mensaje esperado.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador, Path fuente, String mensajeEsperado.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Falla si no se lanza la excepción o el mensaje no coincide.</p>
     */
    private static void assertFallaLecturaFuente(Compilador compilador, Path fuente,
                                                  String mensajeEsperado) throws Exception {
        try {
            compilador.compilar(fuente);
            throw new AssertionError("La ruta de fuente invalida debe rechazarse: " + fuente);
        } catch (java.io.IOException ex) {
            if (!ex.getMessage().contains(mensajeEsperado)) {
                throw new AssertionError("Mensaje inesperado al validar fuente: " + ex.getMessage());
            }
        }
    }

    /**
     * <strong>Nombre:</strong> verificarDiagnosticoFinArchivo
     *
     * <p><strong>Objetivo:</strong> Verificar que un fin de archivo inesperado conserve el símbolo y la posición en el diagnóstico sintáctico.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    private static void verificarDiagnosticoFinArchivo(Compilador compilador) throws Exception {
        ResultadoCompilacion resultado = compilarTexto(compilador,
                "empty ~ __main__<| |>\n|:\n    int ~ x <- 1 !\n");
        if (resultado.getParser().getNumErrores() == 0) {
            throw new AssertionError("El bloque sin cerrar debe producir un error sintactico.");
        }
        assertAlgunoContiene(resultado.getParser().erroresSintacticos, "token '<EOF>'");
        assertAlgunoContiene(resultado.getParser().erroresSintacticos, "linea 4, col 1");
    }

    /**
     * <strong>Nombre:</strong> verificarCodigoIntermedioExpresiones
     *
     * <p><strong>Objetivo:</strong> Verificar la traducción a código intermedio de expresiones binarias, unarias y el uso de temporales.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Compara contra una secuencia de instrucciones esperada.</p>
     */
    private static void verificarCodigoIntermedioExpresiones() {
        ExpresionNodo multiplicacion = new ExpresionBinariaNodo(1, 1, "*",
                id("b"), id("c"));
        ExpresionNodo expresionAnidada = new ExpresionBinariaNodo(1, 1, "+",
                id("a"), multiplicacion);
        ExpresionNodo restaUnaria = new ExpresionUnariaNodo(1, 1, "-", id("x"));
        ExpresionNodo notBooleano = new ExpresionUnariaNodo(1, 1, "$", id("b"));
        ExpresionNodo literalMasIdentificador = new ExpresionBinariaNodo(1, 1, "+",
                new LiteralNodo(1, 1, 3, TipoDato.INT), id("z"));

        ProgramaNodo programa = new ProgramaNodo(1, 1, Arrays.asList(
                new FuncionNodo(1, 1, "main", TipoDato.VOID, Arrays.asList(),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new AsignacionNodo(1, 1, id("r1"), expresionAnidada),
                                new AsignacionNodo(1, 1, id("r2"), restaUnaria),
                                new AsignacionNodo(1, 1, id("r3"), notBooleano),
                                new AsignacionNodo(1, 1, id("r4"), literalMasIdentificador))),
                        true)));

        List<Instruccion> instrucciones = new GeneradorCodigoIntermedio().generar(programa);
        List<String> esperado = Arrays.asList(
                "begin_function main",
                "_t0 = load a",
                "_t1 = load b",
                "_t2 = load c",
                "_t3 = _t1 * _t2",
                "_t4 = _t0 + _t3",
                "r1 = _t4",
                "_t5 = load x",
                "_t6 = -_t5",
                "r2 = _t6",
                "_t7 = load b",
                "_t8 = !_t7",
                "r3 = _t8",
                "_t9 = load z",
                "_t10 = 3 + _t9",
                "r4 = _t10",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    /**
     * <strong>Nombre:</strong> id
     *
     * <p><strong>Objetivo:</strong> Crear rápidamente un nodo identificador para construir AST de prueba.</p>
     *
     * <p><strong>Entrada:</strong> String nombre.</p>
     *
     * <p><strong>Salida:</strong> IdentificadorNodo con ese nombre.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    private static IdentificadorNodo id(String nombre) {
        return new IdentificadorNodo(1, 1, nombre);
    }

    /**
     * <strong>Nombre:</strong> verificarCodigoIntermedioDeclaracionesYAsignaciones
     *
     * <p><strong>Objetivo:</strong> Comprobar que declaraciones inicializadas y asignaciones produzcan el código intermedio esperado.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Compara contra una secuencia de instrucciones esperada.</p>
     */
    private static void verificarCodigoIntermedioDeclaracionesYAsignaciones() {
        ExpresionNodo sumaInicializador = new ExpresionBinariaNodo(1, 1, "+",
                id("a"), id("b"));

        ProgramaNodo programa = new ProgramaNodo(1, 1, Arrays.asList(
                new FuncionNodo(1, 1, "main", TipoDato.VOID, Arrays.asList(),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new DeclaracionVariableNodo(1, 1, "x", TipoDato.INT,
                                        sumaInicializador),
                                new AsignacionNodo(1, 1, id("x"),
                                        new LiteralNodo(1, 1, 5, TipoDato.INT)),
                                new DeclaracionVariableNodo(1, 1, "y", TipoDato.INT,
                                        (ExpresionNodo) null))),
                        true)));

        List<Instruccion> instrucciones = new GeneradorCodigoIntermedio().generar(programa);
        List<String> esperado = Arrays.asList(
                "begin_function main",
                "declare int x",
                "_t0 = load a",
                "_t1 = load b",
                "_t2 = _t0 + _t1",
                "x = _t2",
                "x = 5",
                "declare int y",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    /**
     * <strong>Nombre:</strong> verificarCodigoIntermedioIf
     *
     * <p><strong>Objetivo:</strong> Validar el patrón de saltos del código intermedio para un if sin else.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Compara contra una secuencia de instrucciones esperada.</p>
     */
    private static void verificarCodigoIntermedioIf() {
        ExpresionNodo condicion = new ExpresionBinariaNodo(1, 1, "less_t",
                id("x"), new LiteralNodo(1, 1, 10, TipoDato.INT));
        BloqueNodo bloqueEntonces = new BloqueNodo(1, 1, Arrays.asList(
                new AsignacionNodo(1, 1, id("y"),
                        new LiteralNodo(1, 1, 1, TipoDato.INT))));

        ProgramaNodo programa = new ProgramaNodo(1, 1, Arrays.asList(
                new FuncionNodo(1, 1, "main", TipoDato.VOID, Arrays.asList(),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new IfNodo(1, 1, condicion, bloqueEntonces, null))),
                        true)));

        List<Instruccion> instrucciones = new GeneradorCodigoIntermedio().generar(programa);
        List<String> esperado = Arrays.asList(
                "begin_function main",
                "_t0 = load x",
                "_t1 = _t0 < 10",
                "if_false _t1 goto _L0",
                "y = 1",
                "_L0:",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    /**
     * <strong>Nombre:</strong> verificarCodigoIntermedioIfElse
     *
     * <p><strong>Objetivo:</strong> Validar las etiquetas y saltos del código intermedio para un if-else.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Compara contra una secuencia de instrucciones esperada.</p>
     */
    private static void verificarCodigoIntermedioIfElse() {
        ExpresionNodo condicion = new ExpresionBinariaNodo(1, 1, "greather_t",
                id("x"), new LiteralNodo(1, 1, 0, TipoDato.INT));
        BloqueNodo bloqueEntonces = new BloqueNodo(1, 1, Arrays.asList(
                new AsignacionNodo(1, 1, id("y"),
                        new LiteralNodo(1, 1, 1, TipoDato.INT))));
        BloqueNodo bloqueSino = new BloqueNodo(1, 1, Arrays.asList(
                new AsignacionNodo(1, 1, id("y"),
                        new LiteralNodo(1, 1, 2, TipoDato.INT))));

        ProgramaNodo programa = new ProgramaNodo(1, 1, Arrays.asList(
                new FuncionNodo(1, 1, "main", TipoDato.VOID, Arrays.asList(),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new IfNodo(1, 1, condicion, bloqueEntonces, bloqueSino))),
                        true)));

        List<Instruccion> instrucciones = new GeneradorCodigoIntermedio().generar(programa);
        List<String> esperado = Arrays.asList(
                "begin_function main",
                "_t0 = load x",
                "_t1 = _t0 > 0",
                "if_false _t1 goto _L0",
                "y = 1",
                "goto _L1",
                "_L0:",
                "y = 2",
                "_L1:",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    /**
     * <strong>Nombre:</strong> verificarCodigoIntermedioWhile
     *
     * <p><strong>Objetivo:</strong> Comprobar el código intermedio de un while con su condición y su salto de regreso.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Compara contra una secuencia de instrucciones esperada.</p>
     */
    private static void verificarCodigoIntermedioWhile() {
        ExpresionNodo condicion = new ExpresionBinariaNodo(1, 1, "less_t",
                id("x"), new LiteralNodo(1, 1, 3, TipoDato.INT));
        BloqueNodo cuerpo = new BloqueNodo(1, 1, Arrays.asList(
                new AsignacionNodo(1, 1, id("x"),
                        new ExpresionBinariaNodo(1, 1, "+", id("x"),
                                new LiteralNodo(1, 1, 1, TipoDato.INT)))));

        ProgramaNodo programa = new ProgramaNodo(1, 1, Arrays.asList(
                new FuncionNodo(1, 1, "main", TipoDato.VOID, Arrays.asList(),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new WhileNodo(1, 1, condicion, cuerpo, false))),
                        true)));

        List<Instruccion> instrucciones = new GeneradorCodigoIntermedio().generar(programa);
        List<String> esperado = Arrays.asList(
                "begin_function main",
                "_L0:",
                "_t0 = load x",
                "_t1 = _t0 < 3",
                "if_false _t1 goto _L1",
                "_t2 = load x",
                "_t3 = _t2 + 1",
                "x = _t3",
                "goto _L0",
                "_L1:",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    /**
     * <strong>Nombre:</strong> verificarCodigoIntermedioSalida
     *
     * <p><strong>Objetivo:</strong> Verificar que {@code cout} se traduzca a una instrucción print en el código intermedio.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Compara contra una secuencia de instrucciones esperada.</p>
     */
    private static void verificarCodigoIntermedioSalida() {
        ProgramaNodo programa = new ProgramaNodo(1, 1, Arrays.asList(
                new FuncionNodo(1, 1, "main", TipoDato.VOID, Arrays.asList(),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new SalidaNodo(1, 1, id("resultado")))),
                        true)));

        List<Instruccion> instrucciones = new GeneradorCodigoIntermedio().generar(programa);
        List<String> esperado = Arrays.asList(
                "begin_function main",
                "_t0 = load resultado",
                "print _t0",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    /**
     * <strong>Nombre:</strong> verificarEtiquetasUnicasEntreIfYWhile
     *
     * <p><strong>Objetivo:</strong> Asegurar que estructuras de control consecutivas no reutilicen las mismas etiquetas internas.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Compara contra una secuencia de instrucciones esperada.</p>
     */
    private static void verificarEtiquetasUnicasEntreIfYWhile() {
        ExpresionNodo condicionIf = new ExpresionBinariaNodo(1, 1, "less_t",
                id("x"), new LiteralNodo(1, 1, 10, TipoDato.INT));
        BloqueNodo bloqueEntonces = new BloqueNodo(1, 1, Arrays.asList(
                new AsignacionNodo(1, 1, id("y"),
                        new LiteralNodo(1, 1, 1, TipoDato.INT))));
        ExpresionNodo condicionWhile = new ExpresionBinariaNodo(1, 1, "greather_t",
                id("x"), new LiteralNodo(1, 1, 0, TipoDato.INT));
        BloqueNodo cuerpoWhile = new BloqueNodo(1, 1, Arrays.asList(
                new AsignacionNodo(1, 1, id("x"),
                        new ExpresionBinariaNodo(1, 1, "-", id("x"),
                                new LiteralNodo(1, 1, 1, TipoDato.INT)))));

        ProgramaNodo programa = new ProgramaNodo(1, 1, Arrays.asList(
                new FuncionNodo(1, 1, "main", TipoDato.VOID, Arrays.asList(),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new IfNodo(1, 1, condicionIf, bloqueEntonces, null),
                                new WhileNodo(1, 1, condicionWhile, cuerpoWhile, false))),
                        true)));

        List<Instruccion> instrucciones = new GeneradorCodigoIntermedio().generar(programa);
        List<String> esperado = Arrays.asList(
                "begin_function main",
                "_t0 = load x",
                "_t1 = _t0 < 10",
                "if_false _t1 goto _L0",
                "y = 1",
                "_L0:",
                "_L1:",
                "_t2 = load x",
                "_t3 = _t2 > 0",
                "if_false _t3 goto _L2",
                "_t4 = load x",
                "_t5 = _t4 - 1",
                "x = _t5",
                "goto _L1",
                "_L2:",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    /**
     * <strong>Nombre:</strong> verificarCodigoIntermedioFuncionesYLlamadas
     *
     * <p><strong>Objetivo:</strong> Verificar la generación de begin/end de función, parámetros, llamadas y retornos en el código intermedio.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Compara contra una secuencia de instrucciones esperada.</p>
     */
    private static void verificarCodigoIntermedioFuncionesYLlamadas() {
        LlamadaFuncionNodo llamadaConRetorno = new LlamadaFuncionNodo(1, 1, "foo",
                Arrays.asList(id("a"), id("b")));
        llamadaConRetorno.setTipo(TipoDato.INT);
        LlamadaFuncionNodo llamadaVoid = new LlamadaFuncionNodo(1, 1, "bar",
                Arrays.asList(id("a")));
        llamadaVoid.setTipo(TipoDato.EMPTY);

        ProgramaNodo programa = new ProgramaNodo(1, 1, Arrays.asList(
                new FuncionNodo(1, 1, "foo", TipoDato.INT,
                        Arrays.asList(new ParametroNodo(1, 1, "a", TipoDato.INT),
                                new ParametroNodo(1, 1, "b", TipoDato.INT)),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new ReturnNodo(1, 1, new ExpresionBinariaNodo(1, 1, "+",
                                        id("a"), id("b"))))),
                        false),
                new FuncionNodo(1, 1, "bar", TipoDato.EMPTY,
                        Arrays.asList(new ParametroNodo(1, 1, "a", TipoDato.INT)),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new ReturnNodo(1, 1, null))),
                        false),
                new FuncionNodo(1, 1, "main", TipoDato.EMPTY, Arrays.asList(),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new AsignacionNodo(1, 1, id("r"), llamadaConRetorno),
                                new ExpresionSentenciaNodo(1, 1, llamadaVoid))),
                        true)));

        List<Instruccion> instrucciones = new GeneradorCodigoIntermedio().generar(programa);
        List<String> esperado = Arrays.asList(
                "begin_function foo",
                "parameter int a",
                "parameter int b",
                "_t0 = load a",
                "_t1 = load b",
                "_t2 = _t0 + _t1",
                "return _t2",
                "end_function foo",
                "begin_function bar",
                "parameter int a",
                "return",
                "end_function bar",
                "begin_function main",
                "_t3 = load a",
                "param _t3",
                "_t4 = load b",
                "param _t4",
                "_t5 = call foo, 2",
                "r = _t5",
                "_t6 = load a",
                "param _t6",
                "call bar, 1",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    /**
     * <strong>Nombre:</strong> verificarGeneracionIntermediaSolicitada
     *
     * <p><strong>Objetivo:</strong> Prueba integral de generación intermedia: declaraciones, cargas, arreglos, operadores, switch, E/S y funciones.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Verifica que el fuente sea aceptado y contenga las instrucciones clave.</p>
     */
    private static void verificarGeneracionIntermediaSolicitada(Compilador compilador)
            throws Exception {
        String fuente = "int ~ potencia<|int ~ base, int ~ exponente|>\n|:\n"
                + "    return~base ^ exponente!\n:|\n"
                + "empty ~ __main__<| |>\n|:\n"
                + "    int ~ literalExp <- 2e3 !\n"
                + "    float ~ literalFrac <- 1/2 !\n"
                + "    int ~ matriz <<1>><<2>> <- |: |:4,5:| :| !\n"
                + "    int ~ x !\n"
                + "    cin <|x|> !\n"
                + "    matriz <<0>> <<1>> <- x !\n"
                + "    x <- matriz <<0>> <<0>> % 2 ^ 3 !\n"
                + "    ++x !\n"
                + "    bool ~ menor <- less_t<|x,10|> !\n"
                + "    int ~ negativo <- -5 !\n"
                + "    bool ~ inverso <- $ menor !\n"
                + "    int ~ resultado <- potencia<|x,2|> !\n"
                + "    switch <|x|>\n"
                + "    |:\n"
                + "        case ~ 1:\n        |:\n x <- 2 !\n break!\n :|\n"
                + "        default ~:\n        |:\n x <- 3 !\n break!\n :|\n"
                + "    :|\n"
                + "    cout <|x|> !\n"
                + ":|\n";
        ResultadoCompilacion resultado = compilarTexto(compilador, fuente);
        if (!resultado.isAceptado()) {
            throw new AssertionError("La prueba integral de generacion debe aceptarse. Lexicos: "
                    + resultado.getLexerTokens().getErroresLexicos() + ", sintacticos: "
                    + resultado.getParser().erroresSintacticos + ", semanticos: "
                    + resultado.getParser().tablaSimbolos.getErroresSemanticos());
        }
        List<String> codigo = new java.util.ArrayList<>();
        for (Instruccion instruccion : resultado.getCodigoIntermedio()) {
            codigo.add(instruccion.toString());
        }
        assertAlgunoContiene(codigo, "parameter int base");
        assertAlgunoContiene(codigo, "parameter int exponente");
        assertAlgunoContiene(codigo, "declare int literalExp");
        assertAlgunoContiene(codigo, "literalExp = 2e3");
        assertAlgunoContiene(codigo, "declare float literalFrac");
        assertAlgunoContiene(codigo, "literalFrac = 1/2");
        assertAlgunoContiene(codigo, "declare int matriz[1][2]");
        assertAlgunoContiene(codigo, "store 4 -> matriz[0][0]");
        assertAlgunoContiene(codigo, "store 5 -> matriz[0][1]");
        assertAlgunoContiene(codigo, "read x");
        assertAlgunoContiene(codigo, "store _t");
        assertAlgunoContiene(codigo, "load matriz[0][0]");
        assertAlgunoContiene(codigo, " % ");
        assertAlgunoContiene(codigo, " ^ ");
        assertAlgunoContiene(codigo, " < 10");
        assertAlgunoContiene(codigo, " = -5");
        assertAlgunoContiene(codigo, " = !_t");
        assertAlgunoContiene(codigo, " + 1");
        assertAlgunoContiene(codigo, "param _t");
        assertAlgunoContiene(codigo, "call potencia, 2");
        assertAlgunoContiene(codigo, "if_false ");
        assertAlgunoContiene(codigo, "goto _L");
        assertAlgunoContiene(codigo, "print _t");
        verificarNumeracionContinua(codigo, "_t", " = ");
        verificarNumeracionContinua(codigo, "_L", ":");
    }

    /**
     * <strong>Nombre:</strong> verificarPrecedenciaPotenciaYSigno
     *
     * <p><strong>Objetivo:</strong> Comprobar la precedencia entre la potencia, el signo unario y los paréntesis al analizar expresiones.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Inspecciona la forma del AST resultante.</p>
     */
    private static void verificarPrecedenciaPotenciaYSigno(Compilador compilador) throws Exception {
        ResultadoCompilacion resultado = compilarTexto(compilador,
                "empty ~ __main__<| |>\n|:\n"
                + "    int ~ a <- -2 ^ 2 !\n"
                + "    int ~ b <- <|-2|> ^ 2 !\n"
                + "    int ~ c <- 3-1 !\n"
                + ":|\n");
        if (!resultado.getLexerTokens().getErroresLexicos().isEmpty()
                || resultado.getParser().getNumErrores() != 0) {
            throw new AssertionError("Las expresiones de precedencia deben ser sintacticamente validas.");
        }

        List<ast.Nodo> instrucciones = resultado.getParser().ast.getFunciones().get(0)
                .getCuerpo().getInstrucciones();
        ExpresionNodo expresionA = ((DeclaracionVariableNodo) instrucciones.get(0)).getInicializador();
        if (!(expresionA instanceof ExpresionUnariaNodo)
                || !"-".equals(((ExpresionUnariaNodo) expresionA).getOperador())
                || !(((ExpresionUnariaNodo) expresionA).getExpresion()
                        instanceof ExpresionBinariaNodo)
                || !"^".equals(((ExpresionBinariaNodo) ((ExpresionUnariaNodo) expresionA)
                        .getExpresion()).getOperador())) {
            throw new AssertionError("'-2 ^ 2' debe analizarse como '-(2 ^ 2)'.");
        }

        ExpresionNodo expresionB = ((DeclaracionVariableNodo) instrucciones.get(1)).getInicializador();
        if (!(expresionB instanceof ExpresionBinariaNodo)
                || !"^".equals(((ExpresionBinariaNodo) expresionB).getOperador())
                || !(((ExpresionBinariaNodo) expresionB).getIzquierda()
                        instanceof ExpresionUnariaNodo)) {
            throw new AssertionError("'<|-2|> ^ 2' debe analizarse como '(-2) ^ 2'.");
        }
    }

    /**
     * <strong>Nombre:</strong> verificarNumeracionContinua
     *
     * <p><strong>Objetivo:</strong> Verificar que los temporales o etiquetas con cierto prefijo se definan una sola vez y con numeración consecutiva sin huecos.</p>
     *
     * <p><strong>Entrada:</strong> List&lt;String&gt; codigo, String prefijo, String terminadorDefinicion.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Falla si hay duplicados o números faltantes.</p>
     */
    private static void verificarNumeracionContinua(List<String> codigo, String prefijo,
                                                     String terminadorDefinicion) {
        java.util.Set<Integer> numeros = new java.util.HashSet<>();
        for (String linea : codigo) {
            if (!linea.startsWith(prefijo)) {
                continue;
            }
            int fin = prefijo.length();
            while (fin < linea.length() && Character.isDigit(linea.charAt(fin))) {
                fin++;
            }
            if (fin == prefijo.length() || !linea.substring(fin).startsWith(terminadorDefinicion)) {
                continue;
            }
            int numero = Integer.parseInt(linea.substring(prefijo.length(), fin));
            if (!numeros.add(numero)) {
                throw new AssertionError("Definicion duplicada de " + prefijo + numero);
            }
        }
        for (int i = 0; i < numeros.size(); i++) {
            if (!numeros.contains(i)) {
                throw new AssertionError("Numeracion discontinua: falta " + prefijo + i);
            }
        }
    }

    /**
     * <strong>Nombre:</strong> verificarCorreccionesSemanticas
     *
     * <p><strong>Objetivo:</strong> Ejecutar casos compactos que cubren las validaciones semánticas (aceptación y rechazo).</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Cada caso se afirma como aceptado o rechazado.</p>
     */
    private static void verificarCorreccionesSemanticas(Compilador compilador) throws Exception {
        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ x !\n"
                + "    cout <|x|> !\n"
                + ":|\n", "uso de variable sin inicializar");

        assertRechazado(compilador, "int ~ dupParam<|int ~ a|>\n|:\n"
                + "    int ~ a !\n"
                + "    return ~ a !\n"
                + ":|\n"
                + "empty ~ __main__<| |>\n|:\n:|\n", "variable con mismo nombre que parametro");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ matriz <<2>><<2>> !\n"
                + "    int ~ x <- matriz !\n"
                + ":|\n", "arreglo usado como operando escalar");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ matriz <<2>><<2>> !\n"
                + "    float ~ i <- 1.0 !\n"
                + "    matriz <<i>><<0>> <- 1 !\n"
                + ":|\n", "indice de arreglo no entero");

        assertAceptado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ x <- 2e3 !\n"
                + ":|\n", "literal con exponente entero");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ x <- 1/2 !\n"
                + ":|\n", "literal fraccionario como float");

        assertAceptado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ potenciaEntera <- 2 ^ 3 !\n"
                + "    float ~ potenciaFlotante <- 2.0 ^ 3.0 !\n"
                + ":|\n", "potencia con operandos numericos homogeneos");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    float ~ potenciaMixta <- 2.0 ^ 3 !\n"
                + ":|\n", "potencia con operandos numericos mixtos");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ x <- ++5 !\n"
                + ":|\n", "incremento sobre literal");

        assertRechazado(compilador, "int ~ sinSeparador<| |>\n|:\n"
                + "    return 1 !\n"
                + ":|\n"
                + "empty ~ __main__<| |>\n|:\n:|\n",
                "return con valor exige separador");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ x <- 0 !\n"
                + "    do |:\n        x <- x + 1 !\n    :|\n"
                + "    while <|less_t<|x,10|>|>\n"
                + ":|\n", "do-while sin terminador de admiracion");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    switch <|true|>\n"
                + "    case ~ false:\n"
                + "    |:\n"
                + "        break !\n"
                + "    :|\n"
                + ":|\n", "switch booleano invalido");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ matriz <<2>><<2>> !\n"
                + "    cin <|matriz|> !\n"
                + ":|\n", "cin sobre arreglo");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    bool ~ bandera <- true !\n"
                + "    cin <|bandera|> !\n"
                + ":|\n", "cin solo admite int y float");

        assertAceptado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    cout <|\"texto\"|> !\n"
                + "    cout <|'x'|> !\n"
                + "    cout <|1|> !\n"
                + "    cout <|true|> !\n"
                + "    cout <|1.5|> !\n"
                + ":|\n", "cout admite los cinco tipos permitidos como literales");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ matriz <<0>><<2>> !\n"
                + ":|\n", "dimensiones de arreglo positivas");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ matriz <<-1>><<2>> !\n"
                + ":|\n", "dimensiones negativas de arreglo");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ matriz <<2>><<2>> <- |: |:1,2:| :| !\n"
                + ":|\n", "inicializacion respeta dimensiones declaradas");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ matriz <<2>><<2>> <- |: |:1,2:|, |:3,4:| :| !\n"
                + "    cout <|matriz <<2>> <<0>>|> !\n"
                + ":|\n", "acceso fuera de dimensiones del arreglo");

        assertRechazado(compilador, "int ~ sinReturn<| |>\n|:\n"
                + "    int ~ x <- 1 !\n"
                + ":|\n"
                + "empty ~ __main__<| |>\n|:\n:|\n", "funcion no void sin return");

        assertAceptado(compilador, "int ~ rec<|int ~ n|>\n|:\n"
                + "    return ~ rec<|n|> !\n"
                + ":|\n"
                + "empty ~ __main__<| |>\n|:\n"
                + "    int ~ x <- rec<|1|> !\n"
                + ":|\n", "llamada recursiva con parametros");
    }

    /**
     * <strong>Nombre:</strong> verificarRestriccionesSemanticasSolicitadas
     *
     * <p><strong>Objetivo:</strong> Verificar individualmente cada restricción semántica y el diagnóstico concreto que produce.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Comprueba el mensaje de error exacto de cada caso.</p>
     */
    private static void verificarRestriccionesSemanticasSolicitadas(Compilador compilador)
            throws Exception {
        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ x !\n    cout <|x|> !\n:|\n",
                "usada antes de inicializarse", "inicializacion de variables");

        assertErrorSemantico(compilador, "int ~ f<|int ~ dato|>\n|:\n"
                + "    int ~ dato <- 1 !\n    return ~ dato !\n:|\n"
                + "empty ~ __main__<| |>\n|:\n:|\n",
                "'dato' ya esta declarado", "unicidad entre parametros y variables");

        assertAceptado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ exponente <- 30e50 !\n"
                + "    float ~ fraccion <- 10/3 !\n:|\n",
                "literal exponencial int y literal fraccionario float");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ exponente <- 100e0 !\n:|\n",
                "literal exponencial exige exponente estrictamente positivo");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    float ~ fraccion <- 5/0 !\n:|\n",
                "literal fraccionario rechaza denominador cero");

        assertRechazado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ matriz <<2,2>> !\n:|\n",
                "declaracion de arreglo rechaza indices agrupados con coma");

        assertAceptado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ matriz <<2>><<2>> <- |: |:1,2:|, |:3,4:| :| !\n"
                + "    matriz <<0>><<1>> <- matriz <<1>><<0>> !\n:|\n",
                "declaracion y acceso usan un delimitador por indice");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    float ~ a <<1>><<2>> <- |: |:1.0:| :| !\n:|\n",
                "tiene dimensiones 1x1, pero se declararon 1x2",
                "dimensiones de inicializacion de arreglo");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ a <<1>><<1>> <- |: |:1:| :| !\n"
                + "    float ~ i <- 0.0 !\n    cout <|a <<i>> <<0>>|> !\n:|\n",
                "indice del arreglo debe ser de tipo int", "tipo de indices de arreglo");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ a <<1>><<1>> !\n    a <- 1 !\n:|\n",
                "no se puede asignar directamente al arreglo completo 'a'",
                "asignacion al arreglo completo");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ a <<1>><<1>> <- |: |:1:| :| !\n"
                + "    int ~ x <- a + 1 !\n:|\n",
                "'a' es un arreglo y debe accederse con indices",
                "arreglo como operando binario");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ x <- 1 !\n    int ~ y <- -x !\n:|\n",
                "operador '-' solo puede aplicarse a literales numericos",
                "negativo unario sobre identificador");

        assertAceptado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    bool ~ iguales <- equal<|true,false|> !\n"
                + "    bool ~ distintos <- n_equal<|1.0,2.0|> !\n:|\n",
                "igualdad admite booleanos y numeros");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    bool ~ iguales <- equal<|'a','b'|> !\n:|\n",
                "tipos incompatibles para operador 'equal'",
                "igualdad rechaza caracteres");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    bool ~ distintos <- n_equal<|\"a\",\"b\"|> !\n:|\n",
                "tipos incompatibles para operador 'n_equal'",
                "desigualdad rechaza cadenas");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ x <- ++7 !\n:|\n",
                "no puede aplicarse a un literal o expresion no modificable",
                "incremento unario sobre literal");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    break !\n:|\n",
                "break solo puede utilizarse dentro de un ciclo o switch",
                "break fuera de ciclo o switch");

        assertAceptado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    do |:\n"
                + "        if <|true|> |:\n            break !\n        :|\n"
                + "    :| while <|true|> !\n"
                + ":|\n",
                "break dentro de if anidado en ciclo");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ a <<1>><<1>> <- |: |:1:| :| !\n"
                + "    bool ~ b <- less_t<|a,1|> !\n:|\n",
                "'a' es un arreglo y debe accederse con indices",
                "arreglo como operando relacional");

        assertAceptado(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ a <<1>><<1>> <- |: |:1:| :| !\n"
                + "    ++a <<0>> <<0>> !\n:|\n",
                "operador unario sobre una posicion de arreglo");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    switch <|1.0|>\n    case ~ 1.0:\n    |:\n break !\n :|\n:|\n",
                "expresion de switch no puede ser de tipo float", "tipo de switch");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    int ~ opcion <- 1 !\n"
                + "    switch <|opcion|>\n    case ~ 'a':\n    |:\n break !\n :|\n:|\n",
                "tipo incompatible en case", "tipo del case respecto al switch");

        assertErrorSemantico(compilador, "empty ~ __main__<| |>\n|:\n"
                + "    char ~ c <- 'a' !\n    cin <|c|> !\n:|\n",
                "cin solo puede leer variables escalares de tipo int o float",
                "tipos admitidos por cin");

        assertErrorSemantico(compilador, "void ~ procedimiento<| |>\n|:\n return!\n:|\n"
                + "empty ~ __main__<| |>\n|:\n cout <|procedimiento<| |>|> !\n:|\n",
                "cout no puede imprimir una expresion de tipo", "tipos admitidos por cout");

        assertErrorSemantico(compilador, "int ~ f<| |>\n|:\n return~1.0!\n:|\n"
                + "empty ~ __main__<| |>\n|:\n:|\n",
                "tipo incompatible en return", "tipo de retorno de funcion");

        assertErrorSemantico(compilador, "bool ~ f<| |>\n|:\n bool ~ b <- true !\n:|\n"
                + "empty ~ __main__<| |>\n|:\n:|\n",
                "debe retornar un valor en todas las rutas", "ausencia de return");

        assertErrorSemantico(compilador, "int ~ parcial<|bool ~ condicion|>\n|:\n"
                + "    if <|condicion|> |:\n        return ~ 1 !\n    :|\n"
                + ":|\nempty ~ __main__<| |>\n|:\n:|\n",
                "debe retornar un valor en todas las rutas",
                "if sin else no garantiza retorno");

        assertAceptado(compilador, "int ~ completo<|bool ~ condicion|>\n|:\n"
                + "    if <|condicion|> |:\n        return ~ 1 !\n    :|\n"
                + "    else |:\n        return ~ 2 !\n    :|\n"
                + ":|\n"
                + "string ~ texto<| |>\n|:\n    return ~ \"ok\" !\n:|\n"
                + "void ~ procedimiento<| |>\n|:\n    return !\n:|\n"
                + "empty ~ __main__<| |>\n|:\n"
                + "    int ~ resultado <- completo<|true|> !\n"
                + "    cout <|texto<| |>|> !\n"
                + "    procedimiento<| |> !\n:|\n",
                "if completo y funciones string/void");

        assertAceptado(compilador, "int ~ porSeleccion<|int ~ opcion|>\n|:\n"
                + "    switch <|opcion|> |:\n"
                + "        case ~ 1: |:\n            return ~ 10 !\n        :|\n"
                + "        default ~: |:\n            return ~ 20 !\n        :|\n"
                + "    :|\n:|\n"
                + "empty ~ __main__<| |>\n|:\n"
                + "    int ~ resultado <- porSeleccion<|1|> !\n:|\n",
                "switch con default retorna en todas las rutas");

        assertAceptado(compilador, "int ~ rec<|int ~ n|>\n|:\n"
                + "    if <|equal<|n,0|>|> |:\n return~0!\n :|\n"
                + "    return~rec<|n|>!\n:|\n"
                + "empty ~ __main__<| |>\n|:\n int ~ r <- rec<|1|> !\n:|\n",
                "llamada recursiva con parametros tipados");
    }

    /**
     * <strong>Nombre:</strong> verificarPruebaSemanticaExtensa
     *
     * <p><strong>Objetivo:</strong> Compilar el archivo extenso con errores esperados y revisar los diagnósticos semánticos clave.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Exige rechazo sin errores léxicos ni sintácticos.</p>
     */
    private static void verificarPruebaSemanticaExtensa(Compilador compilador) throws Exception {
        ResultadoCompilacion resultado = compilador.compilar(
                Paths.get("test/prueba_semantica_extensa.chip"));
        if (resultado.isAceptado()) {
            throw new AssertionError("La prueba semantica extensa debe rechazarse.");
        }
        if (!resultado.getLexerTokens().getErroresLexicos().isEmpty()) {
            throw new AssertionError("La prueba semantica extensa no debe producir errores lexicos.");
        }
        if (resultado.getParser().getNumErrores() != 0) {
            throw new AssertionError("La prueba semantica extensa no debe producir errores sintacticos.");
        }
        List<String> errores = resultado.getParser().tablaSimbolos.getErroresSemanticos();
        if (errores.isEmpty()) {
            throw new AssertionError("La prueba semantica extensa debe producir errores semanticos.");
        }
        assertAlgunoContiene(errores, "la expresion de switch no puede ser de tipo float");
        assertAlgunoContiene(errores, "la funcion de tipo bool debe retornar un valor en todas las rutas");
        assertAlgunoContiene(errores, "no se puede asignar directamente al arreglo completo 'arr'");
    }

    /**
     * <strong>Nombre:</strong> verificarPruebaBalanceGeneral
     *
     * <p><strong>Objetivo:</strong> Compilar un caso válido integral para confirmar que no haya falsos positivos en el análisis.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Exige aceptación y generación de código intermedio.</p>
     */
    private static void verificarPruebaBalanceGeneral(Compilador compilador) throws Exception {
        ResultadoCompilacion resultado = compilador.compilar(
                Paths.get("test/prueba_balance_general.chip"));
        if (!resultado.isAceptado()) {
            throw new AssertionError("La prueba de balance general debe aceptarse.");
        }
        if (resultado.getCodigoIntermedio().isEmpty()) {
            throw new AssertionError("La prueba de balance general debe generar codigo intermedio.");
        }
    }

    /**
     * <strong>Nombre:</strong> assertAlgunoContiene
     *
     * <p><strong>Objetivo:</strong> Verificar que al menos una línea de una lista contenga el texto esperado.</p>
     *
     * <p><strong>Entrada:</strong> List&lt;String&gt; lineas, String esperado.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Falla si ninguna línea contiene el texto.</p>
     */
    private static void assertAlgunoContiene(List<String> lineas, String esperado) {
        for (String linea : lineas) {
            if (linea.contains(esperado)) {
                return;
            }
        }
        throw new AssertionError("No se encontro el error esperado: " + esperado);
    }

    /**
     * <strong>Nombre:</strong> assertAceptado
     *
     * <p><strong>Objetivo:</strong> Compilar un fragmento de fuente y fallar si el compilador lo rechaza.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador, String fuente, String caso (descripción).</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Falla si el caso no es aceptado.</p>
     */
    private static void assertAceptado(Compilador compilador, String fuente, String caso) throws Exception {
        ResultadoCompilacion resultado = compilarTexto(compilador, fuente);
        if (!resultado.isAceptado()) {
            throw new AssertionError("El caso debe aceptarse: " + caso);
        }
    }

    /**
     * <strong>Nombre:</strong> assertRechazado
     *
     * <p><strong>Objetivo:</strong> Compilar un fragmento de fuente y fallar si el compilador lo acepta.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador, String fuente, String caso (descripción).</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Falla si el caso no es rechazado.</p>
     */
    private static void assertRechazado(Compilador compilador, String fuente, String caso) throws Exception {
        ResultadoCompilacion resultado = compilarTexto(compilador, fuente);
        if (resultado.isAceptado()) {
            throw new AssertionError("El caso debe rechazarse: " + caso);
        }
    }

    /**
     * <strong>Nombre:</strong> assertErrorSemantico
     *
     * <p><strong>Objetivo:</strong> Exigir un rechazo puramente semántico (sin errores léxicos ni sintácticos) y comprobar el mensaje concreto.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador, String fuente, String mensajeEsperado, String caso (descripción).</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Falla si hay errores de otras fases o no aparece el mensaje esperado.</p>
     */
    private static void assertErrorSemantico(Compilador compilador, String fuente,
                                             String mensajeEsperado, String caso) throws Exception {
        ResultadoCompilacion resultado = compilarTexto(compilador, fuente);
        if (!resultado.getLexerTokens().getErroresLexicos().isEmpty()) {
            throw new AssertionError("El caso no debe tener errores lexicos: " + caso);
        }
        if (resultado.getParser().getNumErrores() != 0) {
            throw new AssertionError("El caso no debe tener errores sintacticos: " + caso
                    + ". Errores: " + resultado.getParser().erroresSintacticos);
        }
        if (resultado.isAceptado()) {
            throw new AssertionError("El caso debe producir un error semantico: " + caso);
        }
        assertAlgunoContiene(resultado.getParser().tablaSimbolos.getErroresSemanticos(),
                mensajeEsperado);
    }

    /**
     * <strong>Nombre:</strong> compilarTexto
     *
     * <p><strong>Objetivo:</strong> Escribir un fuente en un archivo temporal y compilarlo con el pipeline real.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador, String fuente.</p>
     *
     * <p><strong>Salida:</strong> ResultadoCompilacion del fuente compilado.</p>
     *
     * <p><strong>Restricciones:</strong> Crea un archivo temporal en UTF-8.</p>
     */
    private static ResultadoCompilacion compilarTexto(Compilador compilador, String fuente) throws Exception {
        Path archivo = Files.createTempFile("semantica-", ".chip");
        Files.write(archivo, fuente.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return compilador.compilar(archivo);
    }

    /**
     * <strong>Nombre:</strong> verificarInstrucciones
     *
     * <p><strong>Objetivo:</strong> Comparar exactamente la secuencia de instrucciones generada contra la esperada.</p>
     *
     * <p><strong>Entrada:</strong> List&lt;Instruccion&gt; instrucciones, List&lt;String&gt; esperado.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Falla si difieren en cantidad o en cualquier instrucción.</p>
     */
    private static void verificarInstrucciones(List<Instruccion> instrucciones, List<String> esperado) {
        if (instrucciones.size() != esperado.size()) {
            throw new AssertionError("Cantidad de instrucciones esperada: "
                    + esperado.size() + ", actual: " + instrucciones.size());
        }

        for (int i = 0; i < esperado.size(); i++) {
            String actual = instrucciones.get(i).toString();
            if (!esperado.get(i).equals(actual)) {
                throw new AssertionError("Instruccion " + i + " esperada: "
                        + esperado.get(i) + ", actual: " + actual);
            }
        }
    }

    /**
     * <strong>Nombre:</strong> verificarGeneracionMIPS
     *
     * <p><strong>Objetivo:</strong> Verificar la traducción a MIPS de las distintas construcciones y la persistencia del archivo {@code .asm}.</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador, ResultadoCompilacion valido, ResultadoCompilacion invalido.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Comprueba instrucciones clave y que un programa rechazado no genere {@code .asm}.</p>
     */
    private static void verificarGeneracionMIPS(Compilador compilador,
                                                 ResultadoCompilacion valido,
                                                 ResultadoCompilacion invalido) throws Exception {
        verificarAdministradorRegistros();
        verificarSaltosMIPS();
        ResultadoCompilacion aritmetica = compilador.compilar(
                Paths.get("test_verificacion/04_aritmeticas.chip"));
        if (!aritmetica.isAceptado() || aritmetica.getCodigoMIPS().isEmpty()) {
            throw new AssertionError("El caso aritmetico debe generar MIPS.");
        }
        List<String> mips = aritmetica.getCodigoMIPS();
        assertAlgunoContiene(mips, ".data");
        assertAlgunoContiene(mips, ".text");
        assertAlgunoContiene(mips, "add ");
        assertAlgunoContiene(mips, "sub ");
        assertAlgunoContiene(mips, "mul ");
        assertAlgunoContiene(mips, "div.s");
        assertAlgunoContiene(mips, "mfhi");
        assertAlgunoContiene(mips, "syscall");
        assertAlgunoContiene(mips, "_d___main____x: .word 0");
        assertAlgunoContiene(mips, "_t0: .word 0");
        assertAlgunoContiene(mips, "lw $t0,");
        assertAlgunoContiene(mips, "sw $t2,");

        ResultadoCompilacion divisionEntera = compilarTexto(compilador,
                "empty ~ __main__<| |>\n"
                + "|:\n"
                + "    int ~ cociente <- 8 / 2 !\n"
                + "    int ~ residuo <- 8 % 3 !\n"
                + ":|\n");
        if (!divisionEntera.isAceptado()) {
            throw new AssertionError("La division y el modulo enteros deben generar MIPS.");
        }
        assertAlgunoContiene(divisionEntera.getCodigoMIPS(), "div ");
        assertAlgunoContiene(divisionEntera.getCodigoMIPS(), "mflo");
        assertAlgunoContiene(divisionEntera.getCodigoMIPS(), "mfhi");

        ResultadoCompilacion funciones = compilador.compilar(
                Paths.get("test_verificacion/10_funciones.chip"));
        if (!funciones.isAceptado()) {
            throw new AssertionError("El caso de funciones debe generar MIPS.");
        }
        assertAlgunoContiene(funciones.getCodigoMIPS(), "jal _fn_sumar");
        assertAlgunoContiene(funciones.getCodigoMIPS(), "sw $ra, 0($sp)");
        assertAlgunoContiene(funciones.getCodigoMIPS(), "jr $ra");
        assertAlgunoContiene(funciones.getCodigoMIPS(), "_d_sumar__a: .word 0");
        assertAlgunoContiene(funciones.getCodigoMIPS(), "_d___main____resultado: .word 0");

        ResultadoCompilacion procedimientos = compilarTexto(compilador,
                "int ~ suma5<|int ~ a, int ~ b, int ~ c, int ~ d, int ~ e|>\n"
                + "|:\n    return ~ a + b + c + d + e !\n:|\n"
                + "void ~ avisar<| |>\n|:\n    return !\n:|\n"
                + "empty ~ __main__<| |>\n|:\n"
                + "    int ~ r <- suma5<|1,2,3,4,5|> !\n"
                + "    avisar<| |> !\n:|\n");
        if (!procedimientos.isAceptado()) {
            throw new AssertionError("Los procedimientos con parametros deben generar MIPS.");
        }
        assertAlgunoContiene(procedimientos.getCodigoMIPS(), "_fn_suma5:");
        assertAlgunoContiene(procedimientos.getCodigoMIPS(), "_fn_avisar:");
        assertAlgunoContiene(procedimientos.getCodigoMIPS(), "lw $t0, 20($sp)");
        assertAlgunoContiene(procedimientos.getCodigoMIPS(), "addiu $sp, $sp, 20");
        assertAlgunoContiene(procedimientos.getCodigoMIPS(), "jal _fn_suma5");
        assertAlgunoContiene(procedimientos.getCodigoMIPS(), "jal _fn_avisar");
        assertAlgunoContiene(procedimientos.getCodigoMIPS(), "jr $ra");
        assertAlgunoContiene(procedimientos.getCodigoMIPS(), "sw $v0,");

        ResultadoCompilacion retornoFloat = compilarTexto(compilador,
                "float ~ mitad<|float ~ x|>\n"
                + "|:\n    return ~ x / 2.0 !\n:|\n"
                + "empty ~ __main__<| |>\n|:\n"
                + "    float ~ valor <- mitad<|8.0|> !\n:|\n");
        if (!retornoFloat.isAceptado()) {
            throw new AssertionError("El retorno float debe respetar la convencion de procedimientos.");
        }
        assertAlgunoContiene(retornoFloat.getCodigoMIPS(), "mfc1 $v0, $f0");
        assertAlgunoContiene(retornoFloat.getCodigoMIPS(), "mtc1 $v0, $f0");

        ResultadoCompilacion arreglos = compilador.compilar(
                Paths.get("test_verificacion/11_arreglos_enunciado.chip"));
        if (!arreglos.isAceptado()) {
            throw new AssertionError("El caso de arreglos debe reservar memoria MIPS.");
        }
        assertAlgunoContiene(arreglos.getCodigoMIPS(), "matriz: .space 16");
        assertAlgunoContiene(arreglos.getCodigoMIPS(), "la $t7, _d___main____matriz");
        assertAlgunoContiene(arreglos.getCodigoMIPS(), "lw $t0, 0($t7)");
        assertAlgunoContiene(arreglos.getCodigoMIPS(), "sw $t0, 0($t7)");

        ResultadoCompilacion condicional = compilador.compilar(
                Paths.get("test_verificacion/07_if_else.chip"));
        ResultadoCompilacion ciclo = compilador.compilar(
                Paths.get("test_verificacion/08_do_while.chip"));
        ResultadoCompilacion seleccion = compilador.compilar(
                Paths.get("test_verificacion/09_switch.chip"));
        if (!condicional.isAceptado() || !ciclo.isAceptado() || !seleccion.isAceptado()) {
            throw new AssertionError("Las estructuras de control validas deben generar MIPS.");
        }
        assertAlgunoContiene(condicional.getCodigoMIPS(), "ble ");
        assertAlgunoContiene(ciclo.getCodigoMIPS(), "bge ");
        assertAlgunoContiene(ciclo.getCodigoMIPS(), "j _ic_");
        assertAlgunoContiene(seleccion.getCodigoMIPS(), "bne ");
        verificarCorrespondenciaEtiquetas(ciclo);

        Path salida = Files.createTempDirectory("mips-test-");
        Path archivo = EscritorMIPS.escribir(salida, valido);
        if (!archivo.getFileName().toString().equals("01_minimo_valido.asm")
                || !Files.isRegularFile(archivo)) {
            throw new AssertionError("Debe escribirse el archivo MIPS derivado del fuente.");
        }
        List<String> lineas = Files.readAllLines(archivo);
        assertAlgunoContiene(lineas, ".globl main");
        assertAlgunoContiene(lineas, "main:");

        Path archivoInvalido = EscritorMIPS.escribir(salida, invalido);
        if (Files.exists(archivoInvalido)) {
            throw new AssertionError("No debe generarse MIPS para un programa rechazado.");
        }

        // La API tambien puede utilizarse directamente con una lista 3D.
        if (new GeneradorMIPS().generarCodigo(valido.getCodigoIntermedio()).isEmpty()) {
            throw new AssertionError("GeneradorMIPS debe aceptar directamente List<Instruccion>.");
        }
    }

    /**
     * <strong>Nombre:</strong> verificarAdministradorRegistros
     *
     * <p><strong>Objetivo:</strong> Verificar que el administrador de registros reutilice los liberados, marque los ocupados y detecte el agotamiento.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Espera una excepción cuando se agotan los registros.</p>
     */
    private static void verificarAdministradorRegistros() {
        AdministradorRegistros administrador = new AdministradorRegistros();
        String primero = administrador.obtenerRegistro();
        administrador.liberarRegistro(primero);
        if (!primero.equals(administrador.obtenerRegistro())) {
            throw new AssertionError("Un registro liberado debe quedar disponible para reutilizacion.");
        }
        administrador.reiniciar();
        for (int i = 0; i < 6; i++) {
            administrador.obtenerRegistro();
        }
        if (administrador.cantidadDisponibles() != 0) {
            throw new AssertionError("El administrador debe marcar los registros obtenidos como ocupados.");
        }
        try {
            administrador.obtenerRegistro();
            throw new AssertionError("El agotamiento de registros debe detectarse explicitamente.");
        } catch (IllegalStateException esperado) {
            // Comportamiento esperado: nunca se sobreescribe silenciosamente un registro ocupado.
        }
    }

    /**
     * <strong>Nombre:</strong> verificarSaltosMIPS
     *
     * <p><strong>Objetivo:</strong> Verificar que cada operador de comparación del código intermedio genere su branch MIPS correspondiente.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Comprueba la presencia de beq, bne, blt, ble, bgt y bge.</p>
     */
    private static void verificarSaltosMIPS() {
        List<Instruccion> codigo = Arrays.asList(
                new Instruccion(Operacion.INICIO_FUNC, "__main__"),
                new Instruccion(Operacion.DECL, "a", "int"),
                new Instruccion(Operacion.DECL, "b", "int"),
                new Instruccion(Operacion.IGUAL, "_t0", "a", "b"),
                new Instruccion(Operacion.IF_FALSE, "L0", "_t0"),
                new Instruccion(Operacion.DISTINTO, "_t1", "a", "b"),
                new Instruccion(Operacion.IF_FALSE, "L1", "_t1"),
                new Instruccion(Operacion.MENOR, "_t2", "a", "b"),
                new Instruccion(Operacion.IF_FALSE, "L2", "_t2"),
                new Instruccion(Operacion.MENOR_IGUAL, "_t3", "a", "b"),
                new Instruccion(Operacion.IF_FALSE, "L3", "_t3"),
                new Instruccion(Operacion.MAYOR, "_t4", "a", "b"),
                new Instruccion(Operacion.IF_FALSE, "L4", "_t4"),
                new Instruccion(Operacion.MAYOR_IGUAL, "_t5", "a", "b"),
                new Instruccion(Operacion.IF_FALSE, "L5", "_t5"),
                new Instruccion(Operacion.FIN_FUNC, "__main__"));
        List<String> mips = new GeneradorMIPS().generarCodigo(codigo);
        assertAlgunoContiene(mips, "bne ");
        assertAlgunoContiene(mips, "beq ");
        assertAlgunoContiene(mips, "bge ");
        assertAlgunoContiene(mips, "bgt ");
        assertAlgunoContiene(mips, "ble ");
        assertAlgunoContiene(mips, "blt ");
    }

    /**
     * <strong>Nombre:</strong> verificarCorrespondenciaEtiquetas
     *
     * <p><strong>Objetivo:</strong> Verificar que cada etiqueta del código intermedio se conserve como etiqueta {@code _ic_...} en el MIPS.</p>
     *
     * <p><strong>Entrada:</strong> ResultadoCompilacion resultado.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Falla si alguna etiqueta no se conserva.</p>
     */
    private static void verificarCorrespondenciaEtiquetas(ResultadoCompilacion resultado) {
        for (Instruccion instruccion : resultado.getCodigoIntermedio()) {
            if (instruccion.op == Operacion.LABEL
                    && !resultado.getCodigoMIPS().contains("_ic_" + instruccion.resultado + ":")) {
                throw new AssertionError("La etiqueta 3D no fue conservada en MIPS: "
                        + instruccion.resultado);
            }
        }
    }

    /**
     * <strong>Nombre:</strong> verificarEscritorCodigo
     *
     * <p><strong>Objetivo:</strong> Validar el nombre del archivo, el encabezado, la indentación y la limpieza del escritor de código intermedio ({@code .ic}).</p>
     *
     * <p><strong>Entrada:</strong> Compilador compilador, ResultadoCompilacion valido, ResultadoCompilacion invalido.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Exige que un programa rechazado no genere {@code .ic}.</p>
     */
    private static void verificarEscritorCodigo(Compilador compilador,
                                                ResultadoCompilacion valido,
                                                ResultadoCompilacion invalido) throws Exception {
        Path salida = Files.createTempDirectory("codigo-intermedio-test");

        Path archivoValido = EscritorCodigo.escribir(salida, valido);
        if (!archivoValido.getFileName().toString().equals("01_minimo_valido.ic")) {
            throw new AssertionError("El archivo .ic debe derivarse del nombre fuente.");
        }
        if (!Files.isRegularFile(archivoValido)) {
            throw new AssertionError("El caso valido debe generar archivo .ic.");
        }

        List<String> lineas = Files.readAllLines(archivoValido);
        assertContiene(lineas.get(1), "// Fecha: ");
        assertContiene(lineas.get(2), "01_minimo_valido.chip");
        assertContiene(lineas.get(3), "// Integrantes: ");
        assertContiene(lineas.get(5), "\tbegin_function");

        ResultadoCompilacion conEtiqueta = compilador.compilar(
                Paths.get("test_verificacion/07_if_else.chip"));
        Path archivoConEtiqueta = EscritorCodigo.escribir(salida, conEtiqueta);
        List<String> lineasConEtiqueta = Files.readAllLines(archivoConEtiqueta);
        boolean etiquetaSinIndentacion = false;
        boolean instruccionIndentada = false;
        for (String linea : lineasConEtiqueta) {
            if (linea.matches("_L\\d+:")) {
                etiquetaSinIndentacion = true;
            }
            if (linea.startsWith("\tif_false ")) {
                instruccionIndentada = true;
            }
            if (linea.startsWith("\t_L")) {
                throw new AssertionError("Las etiquetas no deben tener indentacion.");
            }
        }
        if (!etiquetaSinIndentacion) {
            throw new AssertionError("Debe existir al menos una etiqueta sin indentacion.");
        }
        if (!instruccionIndentada) {
            throw new AssertionError("Las instrucciones deben tener una tabulacion.");
        }

        Path archivoInvalido = EscritorCodigo.escribir(salida, invalido);
        if (Files.exists(archivoInvalido)) {
            throw new AssertionError("No debe generarse archivo .ic cuando hay errores.");
        }
    }

    /**
     * <strong>Nombre:</strong> assertContiene
     *
     * <p><strong>Objetivo:</strong> Fallar si un texto no incluye la subcadena requerida.</p>
     *
     * <p><strong>Entrada:</strong> String texto, String esperado.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    private static void assertContiene(String texto, String esperado) {
        if (!texto.contains(esperado)) {
            throw new AssertionError("Se esperaba encontrar '" + esperado + "' en: " + texto);
        }
    }
}
