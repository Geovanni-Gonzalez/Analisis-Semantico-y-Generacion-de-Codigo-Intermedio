package mips;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;

/** Administra los registros temporales generales utilizados por el generador MIPS. */
public final class AdministradorRegistros {
    private static final String[] REGISTROS = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5"};
    private final Deque<String> disponibles = new ArrayDeque<>();
    private final Set<String> ocupados = new LinkedHashSet<>();

    public AdministradorRegistros() {
        reiniciar();
    }

    /** Obtiene un registro temporal libre y lo marca como ocupado. */
    public String obtenerRegistro() {
        String registro = disponibles.pollFirst();
        if (registro == null) {
            throw new IllegalStateException("No hay registros temporales MIPS disponibles");
        }
        ocupados.add(registro);
        return registro;
    }

    /** Libera un registro para que pueda reutilizarse en la siguiente operacion. */
    public void liberarRegistro(String registro) {
        if (registro != null && ocupados.remove(registro)) {
            disponibles.addFirst(registro);
        }
    }

    /** Restablece el banco al iniciar una nueva generacion. */
    public void reiniciar() {
        disponibles.clear();
        ocupados.clear();
        disponibles.addAll(Arrays.asList(REGISTROS));
    }

    public int cantidadDisponibles() {
        return disponibles.size();
    }
}
