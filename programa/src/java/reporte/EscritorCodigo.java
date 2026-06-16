package reporte;

import intermedio.Instruccion;
import intermedio.Operacion;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import pipeline.ResultadoCompilacion;

/**
 * Escribe el archivo final de codigo intermedio del compilador.
 *
 * <p>La clase centraliza el encabezado, el nombre del archivo de salida y la
 * decision de borrar resultados antiguos cuando la compilacion no fue
 * aceptada.</p>
 */
public final class EscritorCodigo {
    private static final String INTEGRANTES = "Geovanni Gonzalez";

    /**
     * Nombre : EscritorCodigo.
     * Descripcion: Evita crear instancias de una clase utilitaria.
     * Entrada: Sin parametros.
     * Salida: Instancia inicializada de EscritorCodigo.
     */
    private EscritorCodigo() {
    }

    /**
     * Nombre : escribir.
     * Descripcion: Genera el archivo .ic correspondiente a un resultado de compilacion.
     * Entrada: Path directorioSalida, ResultadoCompilacion resultado
     * Salida: Retorna Path.
     */
    public static Path escribir(Path directorioSalida, ResultadoCompilacion resultado) throws Exception {
        Files.createDirectories(directorioSalida);
        Path archivoSalida = resolverArchivoSalida(directorioSalida, resultado.getFuente());
        if (!resultado.isAceptado()) {
            Files.deleteIfExists(archivoSalida);
            return archivoSalida;
        }

        escribir(archivoSalida, resultado.getFuente(), resultado.getCodigoIntermedio());
        return archivoSalida;
    }

    /**
     * Nombre : escribir.
     * Descripcion: Escribe instrucciones ya generadas en un archivo concreto.
     * Entrada: Path archivoSalida, Path fuente, List<Instruccion> instrucciones
     * Salida: No retorna valor.
     */
    public static void escribir(Path archivoSalida, Path fuente, List<Instruccion> instrucciones)
            throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(archivoSalida, StandardCharsets.UTF_8)) {
            escribirEncabezado(writer, fuente);
            writer.newLine();
            for (Instruccion instruccion : instrucciones) {
                if (instruccion.getOp() != Operacion.LABEL) {
                    writer.write('\t');
                }
                writer.write(instruccion.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Nombre : resolverArchivoSalida.
     * Descripcion: Calcula el nombre del archivo .ic a partir del nombre del fuente.
     * Entrada: Path directorioSalida, Path fuente
     * Salida: Retorna Path.
     */
    public static Path resolverArchivoSalida(Path directorioSalida, Path fuente) {
        String nombreFuente = fuente.getFileName().toString();
        int punto = nombreFuente.lastIndexOf('.');
        String base = punto > 0 ? nombreFuente.substring(0, punto) : nombreFuente;
        return directorioSalida.resolve(base + ".ic");
    }

    /**
     * Nombre : escribirEncabezado.
     * Descripcion: Agrega metadatos humanos al inicio del codigo intermedio generado.
     * Entrada: BufferedWriter writer, Path fuente
     * Salida: No retorna valor.
     */
    private static void escribirEncabezado(BufferedWriter writer, Path fuente) throws Exception {
        writer.write("// Codigo intermedio");
        writer.newLine();
        writer.write("// Fecha: " + LocalDate.now());
        writer.newLine();
        writer.write("// Archivo fuente: " + fuente.getFileName());
        writer.newLine();
        writer.write("// Integrantes: " + INTEGRANTES);
        writer.newLine();
    }
}
