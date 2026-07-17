# TECHNICAL_REVIEW — Analisis-Semantico-y-Generacion-de-Codigo-Intermedio

Fecha de revisión: 2026-07-16
Método: análisis estático de código, enunciado (`docs/Proyecto 2 Compiladores.md`), configuración, CI y git. El build no se ejecutó en esta pasada; CI (`mvn -B verify`, Temurin 17) es la señal verde. Proyecto en parejas (ver `info.txt`).

## 1. Contexto en el portafolio

Proyecto #2 de Compiladores (TEC): analizador semántico + código intermedio de tres direcciones sobre el front-end del Proyecto 1. Es el **predecesor directo** de `Generacion-Codigo-Destino-MIPS` (Proyecto #3), que reutiliza y expande este código. Para evitar evidencia duplicada, la matriz de habilidades detallada vive en el CV_EVIDENCE del Proyecto 3; aquí se documenta lo propio de esta fase.

## 2. Cumplimiento del enunciado

| Requisito | Estado | Evidencia |
|---|---|---|
| a) Preservar alcances del Proyecto I | ✅ | `lexico/`, `sintactico/` con recuperación en modo pánico |
| b) Veredicto de aceptación con tipado explícito y fuerte | ✅ | `semantico/AnalizadorSemantico`, `TablaDeSimbolos`; `salida_*/resultado_sintactico.txt` |
| c) Manejo de errores léxicos/sintácticos/semánticos (modo pánico) | ✅ | `reporte/ReportadorErrores`; capturas en `docs/img/multiples_errores_*.png` |
| Generación de código intermedio de tres direcciones | ✅ | `intermedio/GeneradorCodigoIntermedio`; `salida_codex_intermedio/codigo_intermedio.txt` |
| Extensión no exigida: prototipo MIPS verificado en QtSPIM | ✅ extra | `mips/GeneradorMIPS` (2 clases); corpus `pruebas_qtspim/` (8 pares `.chip`/`.asm` con salida esperada) |

## 3. Fortalezas

1. Separación temprana de responsabilidades: la gramática CUP delega validaciones al paquete `semantico` en lugar de concentrar lógica en las acciones — decisión que habilitó la evolución al Proyecto 3.
2. Corpus de verificación manual QtSPIM con salidas esperadas documentadas (`pruebas_qtspim/LEEME.md`) — evidencia reproducible.
3. Smoke test ejecutable en CI (caso válido aceptado + IR generado; caso inválido rechazado).

## 4. Debilidades y riesgos

| Hallazgo | Severidad | Nota |
|---|---|---|
| Una sola prueba (smoke); sin tests unitarios de tabla de símbolos ni del generador IR | Media | El Proyecto 3 corrigió esto (36 tests) |
| Javadoc boilerplate en gran parte de los 54 archivos (~9,700 LOC) | Baja-Media | En el Proyecto 3 se eliminó; aquí persiste |
| Carpetas `salida_codex_*` y `salida_debug/` con nombres de sesión de agente/debug trackeadas (19 archivos) | Baja | Renombrar a `examples/` o reducir a un único directorio de muestra |
| Duplicación de scaffolding `.github/java-upgrade/` y `.github/modernize/java-upgrade/` | Baja | Restos de tooling; eliminar |
| README previo con typos y estructura genérica | — | Reescrito en esta pasada (EN + resumen ES, enlace al Proyecto 3) |

## 5. Evaluación profesional

- 30 segundos: badge CI + estructura `programa/` clara; ahora el README posiciona el repo como fase media del pipeline y enlaza al flagship.
- Nivel demostrado: **Junior+ / Mid** en esta fase aislada (diseño semántico sólido, testing mínimo). Su valor principal es mostrar la **evolución** hacia el Proyecto 3.

## 6. Recomendaciones

Ver `IMPROVEMENT_ROADMAP.md`. Prioridad: consolidar carpetas de salida de ejemplo y limpiar scaffolding duplicado.
