import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java_cup.runtime.Symbol;

public class Main {
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

        MiLexer lexerTokens = crearLexer(fuente);
        consumirTokens(lexerTokens);

        MiLexer lexerParser = crearLexer(fuente);
        lexerParser.setImprimirErrores(false);
        Parser parser = new Parser(lexerParser);
        boolean sintaxisCompleta = true;
        try {
            parser.parse();
        } catch (Exception ex) {
            sintaxisCompleta = false;
            parser.erroresSintacticos.add("Error sintactico fatal: " + ex.getMessage());
        }

        boolean aceptado = sintaxisCompleta
                && lexerTokens.getErroresLexicos().isEmpty()
                && parser.getNumErrores() == 0;

        escribirTokens(salida.resolve("tokens_report.txt"), lexerTokens);
        escribirTablaSimbolos(salida.resolve("tabla_simbolos.txt"), lexerTokens);
        escribirErrores(salida.resolve("errores_report.txt"), lexerTokens, parser);
        escribirResultado(salida.resolve("resultado_sintactico.txt"), fuente, aceptado);

        System.out.println("Archivo analizado: " + fuente);
        System.out.println(aceptado
                ? "El archivo fuente puede ser generado por la gramatica."
                : "El archivo fuente NO puede ser generado por la gramatica.");
        System.out.println("Reporte de tokens: " + salida.resolve("tokens_report.txt"));
        System.out.println("Tabla de simbolos: " + salida.resolve("tabla_simbolos.txt"));
        System.out.println("Reporte de errores: " + salida.resolve("errores_report.txt"));
        System.out.println("Resultado sintactico: " + salida.resolve("resultado_sintactico.txt"));
    }

    private static MiLexer crearLexer(Path fuente) throws Exception {
        Reader reader = Files.newBufferedReader(fuente, StandardCharsets.UTF_8);
        return new MiLexer(reader);
    }

    private static void consumirTokens(MiLexer lexer) throws Exception {
        Symbol token;
        do {
            token = lexer.next_token();
        } while (token.sym != sym.EOF);
    }

    private static void escribirTokens(Path archivo, MiLexer lexer) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            writer.write("ID_TOKEN\tTOKEN\tLEXEMA\tLINEA\tCOLUMNA\tTABLA\tINFORMACION");
            writer.newLine();
            for (MiLexer.TokenInfo token : lexer.getTokens()) {
                writer.write(token.id + "\t" + token.nombre + "\t" + token.lexema + "\t"
                        + token.linea + "\t" + token.columna + "\t" + token.tabla + "\t"
                        + token.informacion);
                writer.newLine();
            }
        }
    }

    private static void escribirTablaSimbolos(Path archivo, MiLexer lexer) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            writer.write("TABLA\tLEXEMA\tTOKEN\tLINEA\tCOLUMNA\tINFORMACION");
            writer.newLine();
            for (MiLexer.TokenInfo token : lexer.getTokens()) {
                writer.write(token.tabla + "\t" + token.lexema + "\t" + token.nombre + "\t"
                        + token.linea + "\t" + token.columna + "\t" + token.informacion);
                writer.newLine();
            }
        }
    }

    private static void escribirErrores(Path archivo, MiLexer lexer, Parser parser) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            writer.write("ERRORES LEXICOS");
            writer.newLine();
            if (lexer.getErroresLexicos().isEmpty()) {
                writer.write("Sin errores lexicos.");
                writer.newLine();
            } else {
                for (String error : lexer.getErroresLexicos()) {
                    writer.write(error);
                    writer.newLine();
                }
            }

            writer.newLine();
            writer.write("ERRORES SINTACTICOS");
            writer.newLine();
            if (parser.erroresSintacticos.isEmpty()) {
                writer.write("Sin errores sintacticos.");
                writer.newLine();
            } else {
                for (String error : parser.erroresSintacticos) {
                    writer.write(error);
                    writer.newLine();
                }
            }
        }
    }

    private static void escribirResultado(Path archivo, Path fuente, boolean aceptado) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            writer.write("Archivo fuente: " + fuente);
            writer.newLine();
            writer.write(aceptado
                    ? "El archivo fuente puede ser generado por la gramatica."
                    : "El archivo fuente NO puede ser generado por la gramatica.");
            writer.newLine();
        }
    }
}
