package ast;

import java.util.Objects;

/**
 * Representacion textual simple de una instruccion en la capa AST antigua.
 */
public class Instruccion {
    private final String texto;
    /**
     * Nombre : Instruccion.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String texto
     * Salida: Instancia inicializada de Instruccion.
     */
    public Instruccion(String texto) {
        this.texto = Objects.requireNonNull(texto, "texto");
    }

    /**
     * Nombre : getTexto.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String getTexto() {
        return texto;
    }

    /** Usa el texto como representacion imprimible de la instruccion. */
    @Override
    /**
     * Nombre : toString.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String toString() {
        return texto;
    }
}
