package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <strong>Nombre:</strong> GeneradorCodigo
 *
 * <p><strong>Objetivo:</strong> Generador simple (histórico) de temporales, etiquetas e
 * instrucciones de la capa AST. Es un singleton con contadores acumuladores.</p>
 *
 * <p><strong>Entrada:</strong> Instrucciones que los métodos van emitiendo.</p>
 *
 * <p><strong>Salida:</strong> Lista de instrucciones y nombres únicos de temporales/etiquetas.</p>
 *
 * <p><strong>Restricciones:</strong> Debe reiniciarse con {@link #reiniciar()} antes de reutilizarlo.</p>
 */
public final class GeneradorCodigo {
    /** Única instancia compartida del generador (patrón singleton). */
    private static final GeneradorCodigo INSTANCIA = new GeneradorCodigo();

    private int contadorTemporales;
    private int contadorEtiquetas;
    private final List<Instruccion> instrucciones;

    /**
     * <strong>Nombre:</strong> GeneradorCodigo
     *
     * <p><strong>Objetivo:</strong> Inicializar la lista de instrucciones del generador.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de GeneradorCodigo.</p>
     *
     * <p><strong>Restricciones:</strong> Es privado; la instancia se obtiene con {@link #getInstancia()}.</p>
     */
    private GeneradorCodigo() {
        instrucciones = new ArrayList<>();
    }

    /**
     * <strong>Nombre:</strong> getInstancia
     *
     * <p><strong>Objetivo:</strong> Devolver la instancia única del generador.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> La instancia singleton de GeneradorCodigo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public static GeneradorCodigo getInstancia() {
        return INSTANCIA;
    }

    /**
     * <strong>Nombre:</strong> nuevoTemp
     *
     * <p><strong>Objetivo:</strong> Generar un nombre de temporal nuevo y único.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el temporal ({@code _t0}, {@code _t1}, ...).</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public String nuevoTemp() {
        return "_t" + contadorTemporales++;
    }

    /**
     * <strong>Nombre:</strong> nuevaEtiqueta
     *
     * <p><strong>Objetivo:</strong> Generar un nombre de etiqueta nuevo y único.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con la etiqueta ({@code _L0}, {@code _L1}, ...).</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public String nuevaEtiqueta() {
        return "_L" + contadorEtiquetas++;
    }

    /**
     * <strong>Nombre:</strong> emitir
     *
     * <p><strong>Objetivo:</strong> Agregar una instrucción a la lista acumulada.</p>
     *
     * <p><strong>Entrada:</strong> Instruccion instruccion.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> La instrucción no puede ser {@code null}.</p>
     */
    public void emitir(Instruccion instruccion) {
        instrucciones.add(Objects.requireNonNull(instruccion, "instruccion"));
    }

    /**
     * <strong>Nombre:</strong> getInstrucciones
     *
     * <p><strong>Objetivo:</strong> Devolver las instrucciones emitidas hasta ahora.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> List&lt;Instruccion&gt; no modificable.</p>
     *
     * <p><strong>Restricciones:</strong> La lista no se puede modificar.</p>
     */
    public List<Instruccion> getInstrucciones() {
        return Collections.unmodifiableList(instrucciones);
    }

    /**
     * <strong>Nombre:</strong> reiniciar
     *
     * <p><strong>Objetivo:</strong> Reiniciar los contadores y vaciar la lista de instrucciones.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> No retorna valor.</p>
     *
     * <p><strong>Restricciones:</strong> Debe llamarse antes de reutilizar el generador.</p>
     */
    public void reiniciar() {
        contadorTemporales = 0;
        contadorEtiquetas = 0;
        instrucciones.clear();
    }
}
