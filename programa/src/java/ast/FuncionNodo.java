package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <strong>Nombre:</strong> FuncionNodo
 *
 * <p><strong>Objetivo:</strong> Representar la declaración completa de una función o del
 * procedimiento principal ({@code __main__}).</p>
 *
 * <p><strong>Entrada:</strong> Posición, nombre, tipo de retorno, parámetros, cuerpo y si es el principal.</p>
 *
 * <p><strong>Salida:</strong> Nodo consultable por las fases semántica e intermedia.</p>
 *
 * <p><strong>Restricciones:</strong> Solo almacena estructura y metadatos; no valida.</p>
 */
public class FuncionNodo extends Nodo {
    private final String nombre;
    private final TipoDato tipoRetorno;
    private final List<ParametroNodo> parametros;
    private final BloqueNodo cuerpo;
    private final boolean principal;

    /**
     * <strong>Nombre:</strong> FuncionNodo
     *
     * <p><strong>Objetivo:</strong> Crear la función con su firma (nombre, tipo, parámetros) y su cuerpo.</p>
     *
     * <p><strong>Entrada:</strong> int linea, int columna, String nombre, TipoDato tipoRetorno, List&lt;ParametroNodo&gt; parametros, BloqueNodo cuerpo, boolean principal.</p>
     *
     * <p><strong>Salida:</strong> Nueva instancia de FuncionNodo.</p>
     *
     * <p><strong>Restricciones:</strong> {@code principal = true} indica el procedimiento de entrada __main__.</p>
     */
    public FuncionNodo(int linea, int columna, String nombre, TipoDato tipoRetorno,
                       List<ParametroNodo> parametros, BloqueNodo cuerpo, boolean principal) {
        super(linea, columna, tipoRetorno);
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
        this.parametros = new ArrayList<>(parametros);
        this.cuerpo = cuerpo;
        this.principal = principal;
    }

    /**
     * <strong>Nombre:</strong> getNombre
     *
     * <p><strong>Objetivo:</strong> Devolver el nombre de la función.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> String con el nombre.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * <strong>Nombre:</strong> getTipoRetorno
     *
     * <p><strong>Objetivo:</strong> Devolver el tipo de dato que retorna la función.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> TipoDato del retorno.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public TipoDato getTipoRetorno() {
        return tipoRetorno;
    }

    /**
     * <strong>Nombre:</strong> getParametros
     *
     * <p><strong>Objetivo:</strong> Devolver los parámetros formales de la función, en orden.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> List&lt;ParametroNodo&gt; no modificable.</p>
     *
     * <p><strong>Restricciones:</strong> La lista no se puede modificar.</p>
     */
    public List<ParametroNodo> getParametros() {
        return Collections.unmodifiableList(parametros);
    }

    /**
     * <strong>Nombre:</strong> getCuerpo
     *
     * <p><strong>Objetivo:</strong> Devolver el bloque de instrucciones que forma el cuerpo de la función.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> BloqueNodo del cuerpo.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public BloqueNodo getCuerpo() {
        return cuerpo;
    }

    /**
     * <strong>Nombre:</strong> isPrincipal
     *
     * <p><strong>Objetivo:</strong> Indicar si la función es el procedimiento principal __main__.</p>
     *
     * <p><strong>Entrada:</strong> Ninguna.</p>
     *
     * <p><strong>Salida:</strong> boolean; true si es el principal.</p>
     *
     * <p><strong>Restricciones:</strong> Ninguna.</p>
     */
    public boolean isPrincipal() {
        return principal;
    }
}
