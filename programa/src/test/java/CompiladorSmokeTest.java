import ast.AsignacionNodo;
import ast.BloqueNodo;
import ast.DeclaracionVariableNodo;
import ast.ExpresionBinariaNodo;
import ast.ExpresionNodo;
import ast.ExpresionUnariaNodo;
import ast.FuncionNodo;
import ast.IdentificadorNodo;
import ast.IfNodo;
import ast.LiteralNodo;
import ast.ProgramaNodo;
import ast.TipoDato;
import ast.WhileNodo;
import intermedio.GeneradorCodigoIntermedio;
import intermedio.Instruccion;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import pipeline.Compilador;
import pipeline.ResultadoCompilacion;

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
        verificarEtiquetasUnicasEntreIfYWhile();
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
                "inicio_func main",
                "_t0 = b * c",
                "_t1 = a + _t0",
                "r1 = _t1",
                "_t2 = -x",
                "r2 = _t2",
                "_t3 = !b",
                "r3 = _t3",
                "_t4 = 3 + z",
                "r4 = _t4",
                "fin_func main");

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
                "inicio_func main",
                "_t0 = a + b",
                "x = _t0",
                "x = 5",
                "fin_func main");

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
                "inicio_func main",
                "_t0 = x < 10",
                "if_false _t0 goto _L0",
                "y = 1",
                "_L0:",
                "fin_func main");

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
                "inicio_func main",
                "_t0 = x > 0",
                "if_false _t0 goto _L0",
                "y = 1",
                "goto _L1",
                "_L0:",
                "y = 2",
                "_L1:",
                "fin_func main");

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
                "inicio_func main",
                "_L0:",
                "_t0 = x < 3",
                "if_false _t0 goto _L1",
                "_t1 = x + 1",
                "x = _t1",
                "goto _L0",
                "_L1:",
                "fin_func main");

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
                "inicio_func main",
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
                "fin_func main");

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
}
