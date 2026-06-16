package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Declaracion completa de una funcion o del procedimiento principal.
 */
public class FuncionNodo extends Nodo {
    private final String nombre;
    private final TipoDato tipoRetorno;
    private final List<ParametroNodo> parametros;
    private final BloqueNodo cuerpo;
    private final boolean principal;
    /**
     * Nombre : FuncionNodo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: int linea, int columna, String nombre, TipoDato tipoRetorno, List<ParametroNodo> parametros, BloqueNodo cuerpo, boolean principal
     * Salida: Instancia inicializada Nodo.
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
     * Nombre : getNombre.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna String.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Nombre : getTipoRetorno.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna TipoDato.
     */
    public TipoDato getTipoRetorno() {
        return tipoRetorno;
    }

    /**
     * Nombre : getParametros.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna List<ParametroNodo>.
     */
    public List<ParametroNodo> getParametros() {
        return Collections.unmodifiableList(parametros);
    }

    /**
     * Nombre : getCuerpo.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna BloqueNodo.
     */
    public BloqueNodo getCuerpo() {
        return cuerpo;
    }

    /**
     * Nombre : isPrincipal.
     * Descripcion: Consulta una condicion booleana del objeto.
     * Entrada: Sin parametros.
     * Salida: Retorna boolean.
     */
    public boolean isPrincipal() {
        return principal;
    }
}
