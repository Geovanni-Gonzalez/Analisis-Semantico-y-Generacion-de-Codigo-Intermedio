package semantico;

import ast.TipoDato;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simbolo {
    private final String nombre;
    private final TipoDato tipo;
    private final CategoriaSimb categoria;
    private final int linea;
    private final List<TipoDato> tiposParametros;
    private final TipoDato tipoRetorno;
    private boolean inicializado;

    public Simbolo(String nombre, TipoDato tipo, CategoriaSimb categoria, int linea) {
        this(nombre, tipo, categoria, linea, Collections.emptyList(), null, false);
    }

    public Simbolo(String nombre, TipoDato tipo, CategoriaSimb categoria, int linea, boolean inicializado) {
        this(nombre, tipo, categoria, linea, Collections.emptyList(), null, inicializado);
    }

    public Simbolo(String nombre, List<TipoDato> tiposParametros, TipoDato tipoRetorno, int linea) {
        this(nombre, tipoRetorno, CategoriaSimb.FUNCION, linea, tiposParametros, tipoRetorno, true);
    }

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

    public String getNombre() {
        return nombre;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public CategoriaSimb getCategoria() {
        return categoria;
    }

    public int getLinea() {
        return linea;
    }

    public List<TipoDato> getTiposParametros() {
        return Collections.unmodifiableList(tiposParametros);
    }

    public TipoDato getTipoRetorno() {
        return tipoRetorno;
    }

    public boolean isInicializado() {
        return inicializado;
    }

    public void setInicializado(boolean inicializado) {
        this.inicializado = inicializado;
    }

    public void agregarTipoParametro(TipoDato tipoParametro) {
        tiposParametros.add(tipoParametro);
    }
}
