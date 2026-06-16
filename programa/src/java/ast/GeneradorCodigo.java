package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Generador historico/simple de temporales, etiquetas e instrucciones AST.
 *
 * <p>Permanece como utilidad singleton para codigo que aun use la capa
 * {@code ast.Instruccion}. El generador principal actual esta en
 * {@code intermedio.GeneradorCodigoIntermedio}.</p>
 */
public final class GeneradorCodigo {
    /**
     * Nombre : GeneradorCodigo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Instancia inicializada de GeneradorCodigo.
     */
    private static final GeneradorCodigo INSTANCIA = new GeneradorCodigo();

    private int contadorTemporales;
    private int contadorEtiquetas;
    private final List<Instruccion> instrucciones;
    /**
     * Nombre : GeneradorCodigo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Instancia inicializada de GeneradorCodigo.
     */
    private GeneradorCodigo() {
        instrucciones = new ArrayList<>();
    }
    /**
     * Nombre : getInstancia.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna GeneradorCodigo.
     */
    public static GeneradorCodigo getInstancia() {
        return INSTANCIA;
    }

    /**
     * Nombre : nuevoTemp.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String nuevoTemp() {
        return "_t" + contadorTemporales++;
    }

    /**
     * Nombre : nuevaEtiqueta.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String nuevaEtiqueta() {
        return "_L" + contadorEtiquetas++;
    }

    /**
     * Nombre : emitir.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Instruccion instruccion
     * Salida: No retorna valor.
     */
    public void emitir(Instruccion instruccion) {
        instrucciones.add(Objects.requireNonNull(instruccion, "instruccion"));
    }

    /**
     * Nombre : getInstrucciones.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna List<Instruccion>.
     */
    public List<Instruccion> getInstrucciones() {
        return Collections.unmodifiableList(instrucciones);
    }

    /**
     * Nombre : reiniciar.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: Sin parametros.
     * Salida: No retorna valor.
     */
    public void reiniciar() {
        contadorTemporales = 0;
        contadorEtiquetas = 0;
        instrucciones.clear();
    }
}
