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

public class Compilador {
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
