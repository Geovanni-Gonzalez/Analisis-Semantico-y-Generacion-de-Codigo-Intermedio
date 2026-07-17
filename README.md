<div align="center">

# Análisis Semántico y Generación de Código Intermedio
### Semantic analysis + three-address IR for the `.chip` language (Compiler Project #2)

[![CI](https://github.com/Geovanni-Gonzalez/Analisis-Semantico-y-Generacion-de-Codigo-Intermedio/actions/workflows/ci.yml/badge.svg)](https://github.com/Geovanni-Gonzalez/Analisis-Semantico-y-Generacion-de-Codigo-Intermedio/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)
[![Build](https://img.shields.io/badge/build-Maven-blue)](https://maven.apache.org/)
[![Lexer](https://img.shields.io/badge/lexer-JFlex%201.9.1-informational)](https://jflex.de/)
[![Parser](https://img.shields.io/badge/parser-Java%20CUP%20LALR-informational)](http://www2.cs.tum.edu/projects/cup/)

</div>

Middle-end of a compiler for `.chip`, a small imperative language for chip-configuration tooling. Project #2 of *Compilers & Interpreters* (IC5701, Tecnológico de Costa Rica): it extends the Project 1 front-end (JFlex scanner + CUP LALR parser with panic-mode recovery) with a **scope-aware, strongly-typed semantic analyzer** and a **three-address intermediate code generator**, plus an early MIPS prototype verified in QtSPIM.

> The full MIPS backend built on top of this phase lives in [Generacion-Codigo-Destino-MIPS](https://github.com/Geovanni-Gonzalez/Generacion-Codigo-Destino-MIPS) (Project #3).

## What it does

Given a `.chip` source file, the compiler:

1. Runs lexical and LALR syntactic analysis, reporting errors via **panic-mode recovery**.
2. Performs semantic analysis: scope-stacked symbol table, **strong explicit typing**, boolean-only conditions, single-`main` enforcement, return checking (`semantico/AnalizadorSemantico`, `TablaDeSimbolos`).
3. Emits readable **three-address intermediate code** to `.ic` (`intermedio/GeneradorCodigoIntermedio`).
4. Writes reports: tokens, symbol table, errors with counts, and acceptance verdict.
5. As an extension beyond the assignment, translates the IR of core constructs to MIPS (`mips/GeneradorMIPS`) — 8 sample programs with their generated `.asm` and expected console output are checked in under [`pruebas_qtspim/`](pruebas_qtspim/) for manual QtSPIM verification.

## Architecture

```
.chip → Lexer (JFlex) → Parser (CUP, LALR + panic mode) → AST (~30 nodes)
      → Semantic analyzer (symbol table, types)  → three-address IR (.ic)
      → [extension] MIPS generator (.asm)        → reports (.txt)
```

The CUP grammar keeps AST-construction actions but delegates type, scope, function, assignment, and return validation to the `semantico` package. Packages: `ast/` (Composite node hierarchy) · `semantico/` · `intermedio/` · `mips/` (early prototype) · `reporte/` (writers) · `pipeline/` (orchestrator + result DTO). ~9,700 lines of Java across 54 files; ~1,700-line CUP grammar.

## Build & run

```bash
cd programa
mvn clean package    # generates scanner/parser (JFlex/CUP), compiles, packages fat JAR
java -jar target/proyecto-compiladores-1.0-SNAPSHOT.jar <source.chip> [output_dir]
```

Outputs: `tokens_report.txt`, `tabla_simbolos.txt`, `errores_report.txt`, `resultado_sintactico.txt`, `<base>.ic` (and `.asm` for supported constructs). Sample runs are checked in under `test/`; error-recovery screenshots in [`docs/img/`](docs/img/).

## Testing

```bash
cd programa
mvn test    # executable smoke test: valid case accepted + IR generated, invalid case rejected
```

CI (GitHub Actions, Temurin 17) runs `mvn verify` on every push. The smoke test (`CompiladorSmokeTest`) covers analysis → IR → MIPS with regression assertions.

## Team

Built in pairs (course requirement): Geovanni González Aguilar and Owen Torres Porras. My focus: Maven project organization and the integration between lexical, syntactic and semantic phases.

## License

See [`LICENSE`](LICENSE).

<details>
<summary><b>Resumen en español</b></summary>

Fase media de un compilador para el lenguaje `.chip` (Proyecto #2 de Compiladores e Intérpretes, TEC). Sobre el front-end JFlex/CUP del Proyecto 1 añade: analizador semántico con tabla de símbolos por alcances y tipado fuerte explícito, generador de código intermedio de tres direcciones (`.ic`), reportes de tokens/símbolos/errores con recuperación en modo pánico, y —como extensión— un prototipo de generador MIPS con 8 programas verificados manualmente en QtSPIM (`pruebas_qtspim/`). Build Maven que genera scanner/parser, smoke test ejecutable y CI en GitHub Actions.

</details>
