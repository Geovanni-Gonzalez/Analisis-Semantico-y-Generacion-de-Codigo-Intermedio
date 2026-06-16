package reporte;

/**
 * Fabrica centralizada para mensajes de error del compilador.
 *
 * <p>Normaliza linea y columna para que los reportes tengan un formato comun
 * sin importar si provienen del lexer, parser o analizador semantico.</p>
 */
public final class ReportadorErrores {
    /**
     * Fases del compilador que pueden producir diagnosticos.
     */
    public enum Tipo {
        /** Error producido durante el analisis lexico. */
        LEXICO("lexico"),
        /** Error producido durante el analisis sintactico. */
        SINTACTICO("sintactico"),
        /** Error producido durante el analisis semantico. */
        SEMANTICO("semantico");

        private final String etiqueta;

        /**
         * Nombre : Tipo.
         * Descripcion: Inicializa la etiqueta textual asociada a un tipo de error.
         * Entrada: String etiqueta
         * Salida: Instancia inicializada de Tipo.
         */
        Tipo(String etiqueta) {
            this.etiqueta = etiqueta;
        }
    }

    /**
     * Nombre : ReportadorErrores.
     * Descripcion: Evita crear instancias de una clase utilitaria.
     * Entrada: Sin parametros.
     * Salida: Instancia inicializada de ReportadorErrores.
     */
    private ReportadorErrores() {
    }

    /**
     * Nombre : lexico.
     * Descripcion: Formatea un error lexico sin imprimirlo.
     * Entrada: int linea, int columna, String descripcion
     * Salida: Retorna String.
     */
    public static String lexico(int linea, int columna, String descripcion) {
        return formatear(Tipo.LEXICO, linea, columna, descripcion);
    }

    /**
     * Nombre : sintactico.
     * Descripcion: Formatea un error sintactico sin imprimirlo.
     * Entrada: int linea, int columna, String descripcion
     * Salida: Retorna String.
     */
    public static String sintactico(int linea, int columna, String descripcion) {
        return formatear(Tipo.SINTACTICO, linea, columna, descripcion);
    }

    /**
     * Nombre : semantico.
     * Descripcion: Formatea un error semantico sin imprimirlo.
     * Entrada: int linea, int columna, String descripcion
     * Salida: Retorna String.
     */
    public static String semantico(int linea, int columna, String descripcion) {
        return formatear(Tipo.SEMANTICO, linea, columna, descripcion);
    }

    /**
     * Nombre : reportarLexico.
     * Descripcion: Formatea e imprime un error lexico.
     * Entrada: int linea, int columna, String descripcion
     * Salida: Retorna String.
     */
    public static String reportarLexico(int linea, int columna, String descripcion) {
        return reportar(Tipo.LEXICO, linea, columna, descripcion);
    }

    /**
     * Nombre : reportarSintactico.
     * Descripcion: Formatea e imprime un error sintactico.
     * Entrada: int linea, int columna, String descripcion
     * Salida: Retorna String.
     */
    public static String reportarSintactico(int linea, int columna, String descripcion) {
        return reportar(Tipo.SINTACTICO, linea, columna, descripcion);
    }

    /**
     * Nombre : reportarSemantico.
     * Descripcion: Formatea e imprime un error semantico.
     * Entrada: int linea, int columna, String descripcion
     * Salida: Retorna String.
     */
    public static String reportarSemantico(int linea, int columna, String descripcion) {
        return reportar(Tipo.SEMANTICO, linea, columna, descripcion);
    }

    /**
     * Nombre : reportar.
     * Descripcion: Imprime en stderr un diagnostico de la fase indicada y lo devuelve.
     * Entrada: Tipo tipo, int linea, int columna, String descripcion
     * Salida: Retorna String.
     */
    public static String reportar(Tipo tipo, int linea, int columna, String descripcion) {
        String mensaje = formatear(tipo, linea, columna, descripcion);
        System.err.println(mensaje);
        return mensaje;
    }

    /**
     * Nombre : formatear.
     * Descripcion: Construye el mensaje canonico de error con coordenadas normalizadas.
     * Entrada: Tipo tipo, int linea, int columna, String descripcion
     * Salida: Retorna String.
     */
    public static String formatear(Tipo tipo, int linea, int columna, String descripcion) {
        int lineaNormalizada = linea > 0 ? linea : 1;
        int columnaNormalizada = columna > 0 ? columna : 1;
        return "Error " + tipo.etiqueta + " [linea " + lineaNormalizada
                + ", col " + columnaNormalizada + "]: " + descripcion;
    }
}
