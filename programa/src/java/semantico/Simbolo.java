package semantico;

import ast.TipoDato;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entrada individual de la tabla de simbolos.
 *
 * <p>Un simbolo conserva el nombre, tipo, categoria, linea de declaracion y,
 * cuando aplica, firma  y estado de inicializacion.</p>
 */
public class Simbolo {
    private final String nombre;
    private final TipoDato tipo;
    private final CategoriaSimb categoria;
    private final int linea;
    private final List<TipoDato> tiposParametros;
    private final TipoDato tipoRetorno;
    private boolean inicializado;
    /**
     * Nombre : Simbolo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipo, CategoriaSimb categoria, int linea
     * Salida: Instancia inicializada de Simbolo.
     */
    public Simbolo(String nombre, TipoDato tipo, CategoriaSimb categoria, int linea) {
        this(nombre, tipo, categoria, linea, Collections.emptyList(), null, false);
    }

    /**
     * Nombre : Simbolo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipo, CategoriaSimb categoria, int linea, boolean inicializado
     * Salida: Instancia inicializada de Simbolo.
     */
    public Simbolo(String nombre, TipoDato tipo, CategoriaSimb categoria, int linea, boolean inicializado) {
        this(nombre, tipo, categoria, linea, Collections.emptyList(), null, inicializado);
    }

    /**
     * Nombre : Simbolo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, List<TipoDato> tiposParametros, TipoDato tipoRetorno, int linea
     * Salida: Instancia inicializada de Simbolo.
     */
    public Simbolo(String nombre, List<TipoDato> tiposParametros, TipoDato tipoRetorno, int linea) {
        this(nombre, tipoRetorno, CategoriaSimb.FUNCION, linea, tiposParametros, tipoRetorno, true);
    }

    /**
     * Nombre : Simbolo.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: String nombre, TipoDato tipo, CategoriaSimb categoria, int linea, List<TipoDato> tiposParametros, TipoDato tipoRetorno, boolean inicializado
     * Salida: Instancia inicializada de Simbolo.
     */
    public Simbolo(String nombre, TipoDato tipo, CategoriaSimb categoria, int linea,
                   List<TipoDato> tiposParametros, TipoDato tipoRetorno, boolean inicializado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.categoria = categoria;
        this.linea = linea;
        this.tiposParametros = new ArrayList<>(tiposParametros);
        this.tipoRetorno = tipoRetorno;
        this.inicializado = inicializado;
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
     * Nombre : getTipo.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna TipoDato.
     */
    public TipoDato getTipo() {
        return tipo;
    }

    /**
     * Nombre : getCategoria.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna CategoriaSimb.
     */
    public CategoriaSimb getCategoria() {
        return categoria;
    }

    /**
     * Nombre : getLinea.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna int.
     */
    public int getLinea() {
        return linea;
    }

    /**
     * Nombre : getTiposParametros.
     * Descripcion: Consulta el valor asociado a esta propiedad.
     * Entrada: Sin parametros.
     * Salida: Retorna List<TipoDato>.
     */
    public List<TipoDato> getTiposParametros() {
        return Collections.unmodifiableList(tiposParametros);
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
     * Nombre : isInicializado.
     * Descripcion: Consulta una condicion booleana del objeto.
     * Entrada: Sin parametros.
     * Salida: Retorna boolean.
     */
    public boolean isInicializado() {
        return inicializado;
    }

    /**
     * Nombre : setInicializado.
     * Descripcion: Actualiza el valor asociado a esta propiedad.
     * Entrada: boolean inicializado
     * Salida: No retorna valor.
     */
    public void setInicializado(boolean inicializado) {
        this.inicializado = inicializado;
    }

    /**
     * Nombre : agregarTipoParametro.
     * Descripcion: Ejecuta la responsabilidad principal indicada por el nombre de la funcion.
     * Entrada: TipoDato tipoParametro
     * Salida: No retorna valor.
     */
    public void agregarTipoParametro(TipoDato tipoParametro) {
        tiposParametros.add(tipoParametro);
    }
}
