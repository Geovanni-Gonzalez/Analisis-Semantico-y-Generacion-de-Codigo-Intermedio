import java.nio.file.Paths;
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
    }
}
