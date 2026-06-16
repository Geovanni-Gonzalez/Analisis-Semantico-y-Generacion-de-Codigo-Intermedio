import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import pipeline.Compilador;
import pipeline.ResultadoCompilacion;
import reporte.EscritorCodigo;
import reporte.EscritorReportes;

/**
 * Punto de entrada de la aplicacion de consola.
 *
 * <p>Valida argumentos, ejecuta el pipeline completo y escribe los reportes
 * esperados en el directorio de salida.</p>
 */
public class Main {
    /**
     * Nombre : main.
     * Descripcion: Ejecuta el compilador sobre un archivo fuente recibido por linea de comandos.
     * Entrada: String[] args
     * Salida: No retorna valor.
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Uso: java -jar target/proyecto-compiladores-1.0-SNAPSHOT.jar <archivo_fuente> [directorio_salida]");
            return;
        }

        Path fuente = Paths.get(args[0]).toAbsolutePath().normalize();
        if (!Files.isRegularFile(fuente)) {
            System.err.println("No existe el archivo fuente: " + fuente);
            return;
        }

        Path salida = args.length > 1
                ? Paths.get(args[1]).toAbsolutePath().normalize()
                : Paths.get("salida").toAbsolutePath().normalize();
        Files.createDirectories(salida);

        ResultadoCompilacion resultado = new Compilador().compilar(fuente);

        EscritorReportes.escribirTokens(salida.resolve("tokens_report.txt"),
                resultado.getLexerTokens().getTokens());
        EscritorReportes.escribirTablaSimbolos(salida.resolve("tabla_simbolos.txt"),
                resultado.getLexerTokens().getTokens());
        EscritorReportes.escribirErrores(salida.resolve("errores_report.txt"),
                resultado.getLexerTokens().getErroresLexicos(),
                resultado.getParser().erroresSintacticos,
                resultado.getParser().tablaSimbolos.getErroresSemanticos());
        EscritorReportes.escribirResultado(salida.resolve("resultado_sintactico.txt"),
                fuente, resultado.isAceptado());
        Path codigoIntermedio = EscritorCodigo.escribir(salida, resultado);

        System.out.println("Archivo analizado: " + fuente);
        System.out.println(resultado.isAceptado()
                ? "El archivo fuente puede ser generado por la gramatica."
                : "El archivo fuente NO puede ser generado por la gramatica.");
        System.out.println("Reporte de tokens: " + salida.resolve("tokens_report.txt"));
        System.out.println("Tabla de simbolos: " + salida.resolve("tabla_simbolos.txt"));
        System.out.println("Reporte de errores: " + salida.resolve("errores_report.txt"));
        System.out.println("Resultado sintactico: " + salida.resolve("resultado_sintactico.txt"));
        System.out.println(resultado.isAceptado()
                ? "Codigo intermedio: " + codigoIntermedio
                : "Codigo intermedio no generado por errores de analisis.");
    }
}
