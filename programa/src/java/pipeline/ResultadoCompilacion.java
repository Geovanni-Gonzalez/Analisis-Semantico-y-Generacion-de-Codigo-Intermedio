package pipeline;

import intermedio.Instruccion;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import lexico.MiLexer;
import sintactico.Parser;

/**
 * Resultado inmutable de una corrida del pipeline.
 *
 * <p>Expone tanto los objetos internos necesarios para reportes como las
 * banderas de aceptacion que deciden si el programa puede producir codigo
 * intermedio.</p>
 */
public class ResultadoCompilacion {
    private final Path fuente;
    private final MiLexer lexerTokens;
    private final Parser parser;
    private final boolean sintaxisCompleta;
    private final boolean aceptado;
    private final List<Instruccion> codigoIntermedio;

    /**
     * Nombre : ResultadoCompilacion.
     * Descripcion: Crea el paquete de salida de una compilacion.
     * Entrada: Path fuente, MiLexer lexerTokens, Parser parser, boolean sintaxisCompleta, boolean aceptado, List<Instruccion> codigoIntermedio
     * Salida: Instancia inicializada de ResultadoCompilacion.
     */
    public ResultadoCompilacion(Path fuente, MiLexer lexerTokens, Parser parser,
                                boolean sintaxisCompleta, boolean aceptado,
                                List<Instruccion> codigoIntermedio) {
        this.fuente = fuente;
        this.lexerTokens = lexerTokens;
        this.parser = parser;
        this.sintaxisCompleta = sintaxisCompleta;
        this.aceptado = aceptado;
        this.codigoIntermedio = codigoIntermedio;
    }

    /**
     * Nombre : getFuente.
     * Descripcion: Devuelve la ruta del archivo fuente procesado.
     * Entrada: Sin parametros.
     * Salida: Retorna Path.
     */
    public Path getFuente() {
        return fuente;
    }

    /**
     * Nombre : getLexerTokens.
     * Descripcion: Devuelve el lexer que conserva el reporte completo de tokens.
     * Entrada: Sin parametros.
     * Salida: Retorna MiLexer.
     */
    public MiLexer getLexerTokens() {
        return lexerTokens;
    }

    /**
     * Nombre : getParser.
     * Descripcion: Devuelve el parser con AST, tabla de simbolos y errores sintacticos.
     * Entrada: Sin parametros.
     * Salida: Retorna Parser.
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * Nombre : isSintaxisCompleta.
     * Descripcion: Indica si el parser finalizo sin una excepcion irrecuperable.
     * Entrada: Sin parametros.
     * Salida: Retorna boolean.
     */
    public boolean isSintaxisCompleta() {
        return sintaxisCompleta;
    }

    /**
     * Nombre : isAceptado.
     * Descripcion: Indica si el fuente supero analisis lexico, sintactico y semantico.
     * Entrada: Sin parametros.
     * Salida: Retorna boolean.
     */
    public boolean isAceptado() {
        return aceptado;
    }

    /**
     * Nombre : getCodigoIntermedio.
     * Descripcion: Devuelve una vista de solo lectura del codigo intermedio generado.
     * Entrada: Sin parametros.
     * Salida: Retorna List<Instruccion>.
     */
    public List<Instruccion> getCodigoIntermedio() {
        return Collections.unmodifiableList(codigoIntermedio);
    }
}
