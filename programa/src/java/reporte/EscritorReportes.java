package reporte;

import intermedio.Instruccion;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Genera los archivos de reporte producidos por la linea de comandos.
 *
 * <p>Todos los metodos son estaticos porque la clase solo transforma datos de
 * analisis en archivos tabulares o textos de diagnostico.</p>
 */
public final class EscritorReportes {
    /**
     * Nombre : EscritorReportes.
     * Descripcion: Evita instancias de una clase utilitaria.
     * Entrada: Sin parametros.
     * Salida: Instancia inicializada de EscritorReportes.
     */
    private EscritorReportes() {
    }

    /**
     * Nombre : escribirTokens.
     * Descripcion: Escribe el listado completo de tokens reconocidos por el lexer.
     * Entrada: Path archivo, List<TokenInfo> tokens
     * Salida: No retorna valor.
     */
    public static void escribirTokens(Path archivo, List<TokenInfo> tokens) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            writer.write("ID_TOKEN\tTOKEN\tLEXEMA\tLINEA\tCOLUMNA\tTABLA\tINFORMACION");
            writer.newLine();
            for (TokenInfo token : tokens) {
                writer.write(token.id + "\t" + token.nombre + "\t" + token.lexema + "\t"
                        + token.linea + "\t" + token.columna + "\t" + token.tabla + "\t"
                        + token.informacion);
                writer.newLine();
            }
        }
    }

    /**
     * Nombre : escribirTablaSimbolos.
     * Descripcion: Escribe una vista tabular de los simbolos lexicos recolectados.
     * Entrada: Path archivo, List<TokenInfo> tokens
     * Salida: No retorna valor.
     */
    public static void escribirTablaSimbolos(Path archivo, List<TokenInfo> tokens) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            writer.write("TABLA\tLEXEMA\tTOKEN\tLINEA\tCOLUMNA\tINFORMACION");
            writer.newLine();
            for (TokenInfo token : tokens) {
                writer.write(token.tabla + "\t" + token.lexema + "\t" + token.nombre + "\t"
                        + token.linea + "\t" + token.columna + "\t" + token.informacion);
                writer.newLine();
            }
        }
    }

    /**
     * Nombre : escribirErrores.
     * Descripcion: Agrupa errores lexicos, sintacticos y semanticos en un solo archivo.
     * Entrada: Path archivo, List<String> erroresLexicos, List<String> erroresSintacticos, List<String> erroresSemanticos
     * Salida: No retorna valor.
     */
    public static void escribirErrores(Path archivo, List<String> erroresLexicos,
                                       List<String> erroresSintacticos,
                                       List<String> erroresSemanticos) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            escribirSeccionErrores(writer, "ERRORES LEXICOS", "Sin errores lexicos.", erroresLexicos);
            writer.newLine();
            escribirSeccionErrores(writer, "ERRORES SINTACTICOS", "Sin errores sintacticos.",
                    erroresSintacticos);
            writer.newLine();
            escribirSeccionErrores(writer, "ERRORES SEMANTICOS", "Sin errores semanticos.",
                    erroresSemanticos);
        }
    }

    /**
     * Nombre : escribirResultado.
     * Descripcion: Escribe el veredicto global de aceptacion del programa fuente.
     * Entrada: Path archivo, Path fuente, boolean aceptado
     * Salida: No retorna valor.
     */
    public static void escribirResultado(Path archivo, Path fuente, boolean aceptado) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            writer.write("Archivo fuente: " + fuente);
            writer.newLine();
            writer.write(aceptado
                    ? "El archivo fuente puede ser generado por la gramatica."
                    : "El archivo fuente NO puede ser generado por la gramatica.");
            writer.newLine();
        }
    }

    /**
     * Nombre : escribirCodigoIntermedio.
     * Descripcion: Escribe instrucciones intermedias sin encabezado, util para reportes simples.
     * Entrada: Path archivo, List<Instruccion> instrucciones
     * Salida: No retorna valor.
     */
    public static void escribirCodigoIntermedio(Path archivo, List<Instruccion> instrucciones) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            for (Instruccion instruccion : instrucciones) {
                writer.write(instruccion.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Nombre : escribirSeccionErrores.
     * Descripcion: Imprime una seccion de errores y un mensaje alterno cuando esta vacia.
     * Entrada: BufferedWriter writer, String titulo, String mensajeVacio, List<String> errores
     * Salida: No retorna valor.
     */
    private static void escribirSeccionErrores(BufferedWriter writer, String titulo,
                                               String mensajeVacio, List<String> errores) throws Exception {
        writer.write(titulo);
        writer.newLine();
        if (errores.isEmpty()) {
            writer.write(mensajeVacio);
            writer.newLine();
            return;
        }
        for (String error : errores) {
            writer.write(error);
            writer.newLine();
        }
    }
}
