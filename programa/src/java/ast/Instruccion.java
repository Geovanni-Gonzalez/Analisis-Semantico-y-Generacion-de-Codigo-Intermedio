package ast;

import java.util.Objects;

/**
 * <strong>Nombre:</strong> Instruccion (ast)
 *
 * <p><strong>Objetivo:</strong> Representación textual simple de una instrucción, usada por el
 * generador histórico {@link GeneradorCodigo}. Solo envuelve una línea de texto.</p>
 *
 * <p><strong>Entrada:</strong> El texto de la instrucción.</p>
 *
 * <p><strong>Salida:</strong> Objeto inmutable que representa la línea de instrucción.</p>
 *
 * <p><strong>Restricciones:</strong> El texto no puede ser {@code null}.</p>
 */
public class Instruccion {
    private final String texto;

    /**
     * <strong>Nombre:</strong> Instruccion
     *
     * <p><strong>Objetivo:</strong> Crear la instrucción a partir de su texto.</p>
     *
     * <p><strong>Entrada:</strong> String texto.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de Instruccion.</p>
     *
     * <p><strong>Restricciones:</strong> Lanza excepción si el texto es {@code null}.</p>
     */
    public Instruccion(String texto) {
        this.texto = Objects.requireNonNull(texto, "texto");
    }

    /**
     * <strong>Nombre:</strong> getTexto
     *
     * <p><strong>Objetivo:</strong> Devolver el texto de la instrucción.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el texto.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public String getTexto() {
        return texto;
    }

    /**
     * <strong>Nombre:</strong> toString
     *
     * <p><strong>Objetivo:</strong> Devolver el texto como representación imprimible de la instrucción.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el texto.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    @Override
    public String toString() {
        return texto;
    }
}
