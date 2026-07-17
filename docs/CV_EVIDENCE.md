# CV_EVIDENCE — Analisis-Semantico-y-Generacion-de-Codigo-Intermedio

Verifiable material for the Master Resume. This project is the predecessor of [Generacion-Codigo-Destino-MIPS](https://github.com/Geovanni-Gonzalez/Generacion-Codigo-Destino-MIPS); its full compiler skills matrix lives there. Listed here: only what this repo adds or uniquely evidences.

## Resume bullets (pick & adapt)

- Designed and implemented the semantic-analysis phase of a compiler: scope-stacked symbol table, strong explicit typing, function/parameter/return validation, and single-entry-point enforcement, integrated into a JFlex/CUP LALR front-end with panic-mode error recovery.
- Built a three-address intermediate code generator producing inspectable `.ic` output, decoupling grammar actions from validation logic to keep the parser maintainable.
- Verified an early MIPS translation prototype against QtSPIM using a reproducible corpus of 8 programs with documented expected console outputs.
- Collaborated in a two-person team on a shared Maven codebase with CI (GitHub Actions) and an executable smoke test.

## Unique evidence (vs. Project 3)

| Item | Evidence | Note |
|---|---|---|
| Teamwork on a shared compiler codebase | `info.txt` (2-person team), git history | Only compiler project with explicit collaboration |
| Manual verification methodology | `pruebas_qtspim/LEEME.md` + 8 `.chip`/`.asm` pairs with expected outputs | Documented, reproducible test protocol |
| Architectural decision: validations out of grammar actions | `sintactico.cup` delegating to `semantico/` | Foundation that enabled Project 3's backend |

## Reinforces (already evidenced in Project 3)

Java 17, Maven codegen builds, JFlex/CUP, LALR parsing, AST design, symbol tables, type checking, three-address IR, GitHub Actions CI.

## ATS keywords

Semantic analysis, symbol table, type system, strong typing, intermediate representation, three-address code, compiler middle-end, JFlex, Java CUP, LALR, panic-mode recovery, Java 17, Maven, QtSPIM, MIPS, GitHub Actions, pair programming.
