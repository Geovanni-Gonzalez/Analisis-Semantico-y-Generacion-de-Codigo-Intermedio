# Documentacion interna del compilador

Esta guia resume las decisiones internas del proyecto para mantener el
compilador, agregar reglas semanticas y validar la generacion de codigo
intermedio sin duplicar logica entre la CLI, las pruebas y los reportes.

## Flujo principal

```text
Fuente .lang/.chip
  -> MiLexer
  -> Parser CUP
  -> AST
  -> AnalizadorSemantico + TablaDeSimbolos
  -> GeneradorCodigoIntermedio
  -> EscritorReportes / EscritorCodigo
```

La entrada de consola esta en `programa/src/java/Main.java`. Esa clase debe
mantenerse como una capa delgada: valida rutas, crea el directorio de salida,
llama a `pipeline.Compilador` y escribe artefactos con las clases de `reporte`.

## Responsabilidades

### `pipeline`

`Compilador` orquesta una corrida completa y devuelve un
`ResultadoCompilacion`. Ejecuta una pasada lexica para reportar tokens y una
segunda pasada para alimentar el parser. Solo genera codigo intermedio cuando
no hay errores lexicos, sintacticos ni semanticos.

### `ast`

Contiene los nodos del programa. Los nodos deben conservar estructura, posicion
y tipo, pero no deben validar reglas semanticas ni formatear codigo intermedio.

### `semantico`

`AnalizadorSemantico` aplica reglas de tipos, firmas de funciones, retornos,
asignaciones y condiciones booleanas. `TablaDeSimbolos` administra una pila de
alcances y acumula errores semanticos.

Cuando una expresion falla, se usa `TipoDato.ERROR` para evitar cascadas. Cuando
el tipo aun no se conoce, se mantiene `TipoDato.DESCONOCIDO`.

### `intermedio`

`GeneradorCodigoIntermedio` traduce el AST aceptado a instrucciones de tres
direcciones. `Operacion` define las operaciones soportadas e `Instruccion`
centraliza su representacion textual.

Convenciones actuales:

- Temporales: `_t0`, `_t1`, `_t2`, ...
- Etiquetas: `_L0`, `_L1`, `_L2`, ...
- Llamadas con retorno: `param ...` seguido de `_tN = call nombre, n`.
- Llamadas `empty`/`void`: `call nombre, n` sin temporal.
- Salida observable: `print valor`.

### `reporte`

Centraliza la escritura a disco:

- `tokens_report.txt`
- `tabla_simbolos.txt`
- `errores_report.txt`
- `resultado_sintactico.txt`
- `<nombre_fuente>.ic`

## Flujo de errores

1. El lexer acumula errores lexicos.
2. El parser acumula errores sintacticos y aplica recuperacion cuando puede.
3. Las acciones semanticas delegan validaciones a `AnalizadorSemantico`.
4. `TablaDeSimbolos` acumula errores semanticos.
5. Si existe cualquier error, `ResultadoCompilacion.isAceptado()` es `false` y
   `EscritorCodigo` elimina/no genera el `.ic`.

## Como agregar una construccion del lenguaje

1. Agregar tokens en `programa/src/lexico/lexico.flex` si aplica.
2. Agregar reglas en `programa/src/sintactico/sintactico.cup`.
3. Crear o extender nodos en `programa/src/java/ast`.
4. Agregar validaciones en `AnalizadorSemantico`.
5. Agregar operaciones de IR en `Operacion` si se necesita una instruccion
   nueva.
6. Formatear la instruccion en `Instruccion.toString()`.
7. Emitir la instruccion en `GeneradorCodigoIntermedio`.
8. Cubrir el cambio en `CompiladorSmokeTest`.

## Invariantes

- Un programa invalido no debe producir archivo `.ic`.
- Las etiquetas del `.ic` no se indentan; las instrucciones normales si.
- `__main__` debe existir exactamente una vez y no debe recibir parametros.
- Las funciones con tipo concreto deben retornar un valor compatible.
- Las funciones `empty`/`void` no deben retornar valor.
- Las condiciones de `if` y `do-while` deben ser `bool`.
- Los temporales y etiquetas se reinician por cada generacion de programa.
- La CLI debe usar `pipeline.Compilador`, no reconstruir el flujo manualmente.

## Verificacion recomendada

```bash
cd programa
mvn -q clean package
java -jar target/proyecto-compiladores-1.0-SNAPSHOT.jar ../test/generacion_completa.lang ../test/salida_generacion_completa
```

Despues de ejecutar, revisar:

- `errores_report.txt` no contiene errores para casos validos.
- `resultado_sintactico.txt` coincide con el estado esperado.
- `<nombre>.ic` existe solo para programas aceptados.
