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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import pipeline.Compilador;
import pipeline.ResultadoCompilacion;
import reporte.EscritorCodigo;

public class CompiladorSmokeTest {
    public static void main(String[] args) throws Exception {
        Compilador compilador = new Compilador();

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
        verificarEscritorCodigo(compilador, valido, invalido);
    }

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
                "_t0 = b * c",
                "_t1 = a + _t0",
                "r1 = _t1",
                "_t2 = -x",
                "r2 = _t2",
                "_t3 = !b",
                "r3 = _t3",
                "_t4 = 3 + z",
                "r4 = _t4",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    private static IdentificadorNodo id(String nombre) {
        return new IdentificadorNodo(1, 1, nombre);
    }

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
                "_t0 = a + b",
                "x = _t0",
                "x = 5",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

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
                "_t0 = x < 10",
                "if_false _t0 goto _L0",
                "y = 1",
                "_L0:",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

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
                "_t0 = x > 0",
                "if_false _t0 goto _L0",
                "y = 1",
                "goto _L1",
                "_L0:",
                "y = 2",
                "_L1:",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

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
                "_t0 = x < 3",
                "if_false _t0 goto _L1",
                "_t1 = x + 1",
                "x = _t1",
                "goto _L0",
                "_L1:",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

    private static void verificarCodigoIntermedioSalida() {
        ProgramaNodo programa = new ProgramaNodo(1, 1, Arrays.asList(
                new FuncionNodo(1, 1, "main", TipoDato.VOID, Arrays.asList(),
                        new BloqueNodo(1, 1, Arrays.asList(
                                new SalidaNodo(1, 1, id("resultado")))),
                        true)));

        List<Instruccion> instrucciones = new GeneradorCodigoIntermedio().generar(programa);
        List<String> esperado = Arrays.asList(
                "begin_function main",
                "print resultado",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

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
                "_t0 = x < 10",
                "if_false _t0 goto _L0",
                "y = 1",
                "_L0:",
                "_L1:",
                "_t1 = x > 0",
                "if_false _t1 goto _L2",
                "_t2 = x - 1",
                "x = _t2",
                "goto _L1",
                "_L2:",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

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
                "_t0 = a + b",
                "return _t0",
                "end_function foo",
                "begin_function bar",
                "return",
                "end_function bar",
                "begin_function main",
                "param a",
                "param b",
                "_t1 = call foo, 2",
                "r = _t1",
                "param a",
                "call bar, 1",
                "end_function main");

        verificarInstrucciones(instrucciones, esperado);
    }

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

    private static void assertContiene(String texto, String esperado) {
        if (!texto.contains(esperado)) {
            throw new AssertionError("Se esperaba encontrar '" + esperado + "' en: " + texto);
        }
    }
}
