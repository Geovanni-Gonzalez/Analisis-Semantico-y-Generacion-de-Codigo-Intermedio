# IMPROVEMENT_ROADMAP — Analisis-Semantico-y-Generacion-de-Codigo-Intermedio

Backlog priorizado. Impacto/Esfuerzo: Alto/Medio/Bajo. Nota: el esfuerzo grande de evolución técnica ya ocurrió en el Proyecto 3; este repo debe optimizarse como **evidencia histórica limpia**, no como código a seguir desarrollando.

## Quick Wins

| # | Mejora | Impacto | Esfuerzo | Prioridad |
|---|---|---|---|---|
| 1 | ~~Reescribir README al estándar del portafolio~~ ✅ aplicado (EN + resumen ES, enlace al Proyecto 3) | — | — | Hecho |
| 2 | Consolidar `programa/salida_codex_*`, `salida_debug/`, `salida_demo*/` en un único `programa/examples/` con nombres descriptivos | Medio | Bajo | P1 |
| 3 | Eliminar scaffolding duplicado `.github/java-upgrade/` y `.github/modernize/java-upgrade/` | Bajo | Bajo | P1 |
| 4 | GitHub Topics: `compiler`, `semantic-analysis`, `intermediate-representation`, `jflex`, `cup`, `java` + descripción | Medio | Bajo | P1 |
| 5 | Untrackear `.claude/settings.local.json` | Bajo | Bajo | P2 |

## Mejoras técnicas

| # | Mejora | Impacto | Esfuerzo | Prioridad |
|---|---|---|---|---|
| 6 | Tests unitarios de `TablaDeSimbolos` y `GeneradorCodigoIntermedio` (golden files de `.ic`) | Medio | Medio | P2 |
| 7 | Eliminar Javadoc boilerplate (misma limpieza aplicada en el Proyecto 3) | Bajo-Medio | Bajo | P2 |

## Mejoras arquitectónicas

Ninguna recomendada: la evolución arquitectónica de este código continúa en `Generacion-Codigo-Destino-MIPS`. Invertir allí.

## Mejoras de GitHub

Ya presentes: badge CI, LICENSE, `.gitignore`, screenshots en `docs/img/`, enunciado en `docs/`. Faltan: Topics/descripción (item 4).
