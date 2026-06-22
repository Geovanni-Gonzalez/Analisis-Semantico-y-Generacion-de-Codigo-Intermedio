# Análisis Semántico y Generación de Código Intermedio

## Descripción
Proyecto de compiladores enfocado en validaciónes semanticas y generación de código intermedio sobre una base JFlex/CUP/Java.

## Objetivo
Practicar fases posteriores de un compilador: reglas semanticas, tablas y representaciónes intermedias.

## Tecnologías utilizadas
- Java
- Maven
- JFlex
- CUP

## Funcionalidades principales
- Especificacion lexica
- Especificacion sintactica
- Build Maven
- Punto de entrada Java

## Mi rol
Organicé el proyecto Maven y trabajé en la conexión entre análisis léxico, sintáctico y semántico.

## Aprendizajes clave
- Pipeline de compilación
- Integracion JFlex/CUP
- Validaciónes semanticas
- Automatización con Maven

## Instalación y ejecución
```bash
cd Analisis-Semantico-y-Generacion-de-Codigo-Intermedio/programa
mvn clean package
java -jar target/proyecto-compiladores-1.0-SNAPSHOT.jar
```
Si el JAR cambia de nombre, revisar `target/`.

## Estructura del proyecto
- `programa/pom.xml`: configuracion Maven y generacion JFlex/CUP.
- `programa/src/lexico/`: especificacion lexica JFlex.
- `programa/src/sintactico/`: gramatica CUP y acciones sintacticas.
- `programa/src/java/Main.java`: punto de entrada CLI.
- `programa/src/java/ast/`: jerarquia de nodos del AST y `TipoDato`.
- `programa/src/java/semantico/`: analisis semantico, tabla de simbolos, simbolos y categorias.
- `programa/src/java/intermedio/`: representacion de codigo de tres direcciones.
- `programa/src/java/reporte/`: formateo de errores, tokens y escritura de reportes.
- `programa/src/java/pipeline/`: orquestacion reutilizable del compilador y resultado.
- `programa/src/lib/`: librerias locales requeridas para generar el parser CUP.
- `programa/src/test/java/`: pruebas smoke ejecutadas por Maven sin dependencias externas.

## Arquitectura
El proyecto queda separado en capas para evitar que la gramatica concentre toda la logica:

```text
Fuente .chip
  -> lexico/MiLexer
  -> sintactico/Parser
  -> ast/*
  -> semantico/AnalizadorSemantico
  -> intermedio/Instruccion
  -> reporte/EscritorReportes
```

El parser CUP conserva acciones de construccion del AST, pero delega validaciones de tipos,
alcances, funciones, parametros, asignaciones y retornos al paquete `semantico`. La salida de
tokens, errores, resultado sintactico y codigo intermedio se centraliza en `reporte`. `Main`
queda como CLI y delega el flujo reutilizable al paquete `pipeline`.

## Pruebas
El comando Maven ejecuta una prueba smoke durante la fase `test`:

```bash
cd programa
mvn test
```

La prueba compila un caso valido minimo y un caso con errores semanticos para verificar que
el pipeline acepte, rechace y genere codigo intermedio cuando corresponde.

## Diseño del AST
El AST se modela con una raiz abstracta `Nodo`, que concentra `linea`, `columna` y `TipoDato tipo`. El tipo queda inicializado como `DESCONOCIDO` cuando todavia no hay informacion semantica suficiente, y se completara durante el analisis semantico del P2/M1.

```mermaid
classDiagram
    class Nodo {
        <<abstract>>
        -int linea
        -int columna
        -TipoDato tipo
    }

    class ExpresionNodo {
        <<abstract>>
    }

    class SentenciaNodo {
        <<abstract>>
    }

    Nodo <|-- ProgramaNodo
    Nodo <|-- FuncionNodo
    Nodo <|-- ParametroNodo
    Nodo <|-- BloqueNodo
    Nodo <|-- InicializacionArregloNodo
    Nodo <|-- CasoSwitchNodo
    Nodo <|-- ExpresionNodo
    Nodo <|-- SentenciaNodo

    ExpresionNodo <|-- LiteralNodo
    ExpresionNodo <|-- IdentificadorNodo
    ExpresionNodo <|-- AccesoArregloNodo
    ExpresionNodo <|-- ExpresionBinariaNodo
    ExpresionNodo <|-- ExpresionUnariaNodo
    ExpresionNodo <|-- LlamadaFuncionNodo

    SentenciaNodo <|-- DeclaracionVariableNodo
    SentenciaNodo <|-- AsignacionNodo
    SentenciaNodo <|-- IfNodo
    SentenciaNodo <|-- WhileNodo
    SentenciaNodo <|-- ReturnNodo
    SentenciaNodo <|-- BreakNodo
    SentenciaNodo <|-- EntradaNodo
    SentenciaNodo <|-- SalidaNodo
    SentenciaNodo <|-- SwitchNodo
    SentenciaNodo <|-- ExpresionSentenciaNodo

    ProgramaNodo "1" --> "*" FuncionNodo
    FuncionNodo "1" --> "1" BloqueNodo
    FuncionNodo "1" --> "*" ParametroNodo
    BloqueNodo "1" --> "*" Nodo
    IfNodo "1" --> "1" ExpresionNodo
    IfNodo "1" --> "1..2" BloqueNodo
    WhileNodo "1" --> "1" ExpresionNodo
    WhileNodo "1" --> "1" BloqueNodo
    SwitchNodo "1" --> "*" CasoSwitchNodo
```

## Capturas o demo
Ver la estructura del manual de usuario:
[`documentación/manual_usuario.md`](documentación/manual_usuario.md).

Ver la guia de documentacion interna del compilador:
[`documentación/documentacion_interna.md`](documentación/documentacion_interna.md).

Ver la documentacion de la prueba de recuperacion con multiples errores:
[`documentación/recuperacion_multiples_errores.md`](documentación/recuperacion_multiples_errores.md).

Ver tambien la prueba E2E de equivalencia entre fuente y codigo intermedio:
[`documentación/generacion_completa_equivalencia.md`](documentación/generacion_completa_equivalencia.md).

## Estado del proyecto
Proyecto académico en evolución.

## Valor técnico demostrado
Demuestra comprensión de fases de compiladores y herramientas de generación.

## Recuperación sintáctica a nivel de frase
Se implementó una recuperación local para declaraciones sin terminador de sentencia. Cuando el parser reconoce una declaración completa y el siguiente token ya pertenece a la sentencia siguiente o al cierre del bloque, reduce la declaración como válida e inserta virtualmente el terminador faltante. El reporte sintáctico registra el mensaje `se insertó '!' para recuperar el análisis`.

Este caso cubre declaraciones escalares o de arreglo que omiten el terminador antes de otra sentencia, por ejemplo `int ~ x <- 10` seguido de `cout <|x|>!`. La estrategia conserva el token inicial de la sentencia siguiente, por lo que el análisis puede continuar con menos pérdida de contexto que en modo pánico. Los errores internos de la declaración y otros tipos de sentencias siguen usando las producciones de recuperación existentes con `error`.

## Mejoras futuras
- Agregar casos de prueba
- Documentar gramática
- Completar ejemplos de código intermedio

## Autor
Geovanni González  
Estudiante de Ingeniería en Computación  
GitHub: [Geovanni-Gonzalez](https://github.com/Geovanni-Gonzalez)










