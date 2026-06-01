package pipeline;

import intermedio.GeneradorCodigoIntermedio;
import intermedio.Instruccion;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java_cup.runtime.Symbol;
import lexico.MiLexer;
import reporte.ReportadorErrores;
import sintactico.Parser;
import sintactico.sym;

/**
 * Fachada reutilizable del compilador.
 *
 * <p>Centraliza el flujo completo para que la CLI y las pruebas no dupliquen
 * la secuencia lexer -> parser -> semantica -> codigo intermedio. La clase
 * ejecuta una pasada lexica independiente para conservar el reporte completo
 * de tokens y una segunda pasada para alimentar al parser.</p>
 */
public class Compilador {
    /**
     * Compila un archivo fuente y devuelve todos los artefactos en memoria.
     * El codigo intermedio se genera solamente si no existen errores lexicos,
     * sintacticos ni semanticos.
     */
    public ResultadoCompilacion compilar(Path fuente) throws Exception {
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
            parser.erroresSintacticos.add(ReportadorErrores.reportarSintactico(0, 0,
                    "error fatal del parser: " + ex.getMessage()));
        }

        boolean aceptado = sintaxisCompleta
                && lexerTokens.getErroresLexicos().isEmpty()
                && parser.getNumErrores() == 0
                && parser.tablaSimbolos.getErroresSemanticos().isEmpty();

        List<Instruccion> codigoIntermedio = aceptado && parser.ast != null
                ? new GeneradorCodigoIntermedio().generar(parser.ast)
                : Collections.emptyList();

        return new ResultadoCompilacion(fuente, lexerTokens, parser, sintaxisCompleta,
                aceptado, codigoIntermedio);
    }

    private MiLexer crearLexer(Path fuente) throws Exception {
        Reader reader = Files.newBufferedReader(fuente, StandardCharsets.UTF_8);
        return new MiLexer(reader);
    }

    private void consumirTokens(MiLexer lexer) throws Exception {
        Symbol token;
        do {
            token = lexer.next_token();
        } while (token.sym != sym.EOF);
    }
}
