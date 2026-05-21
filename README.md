# Análisis Semántico y Generación de Código Intermedio

**Proyecto 2 — Compiladores e Intérpretes**  
Universidad | Geovanni González

---

## Descripción

Implementación de las fases de **análisis semántico** y **generación de código intermedio** para un compilador de un lenguaje de programación de propósito educativo. El proyecto extiende el analizador léxico y sintáctico del Proyecto 1, agregando verificación de tipos, manejo de tabla de símbolos y emisión de código de tres direcciones (cuádruplos / TAC).

## Herramientas utilizadas

| Herramienta | Propósito |
|-------------|-----------|
| JFlex | Generación del analizador léxico |
| CUP | Generación del analizador sintáctico |
| Java 17+ | Lenguaje de implementación |

## Estructura del proyecto

```
src/
├── lexico.flex          # Especificación léxica (JFlex)
├── sintactico.cup       # Especificación sintáctica (CUP)
├── semantico/           # Analizador semántico y tabla de símbolos
└── codigo/              # Generación de código intermedio
```

> Los archivos `Lexer.java`, `parser.java` y `sym.java` son generados automáticamente por JFlex/CUP y están excluidos del repositorio.

## Cómo compilar y ejecutar

```bash
# 1. Generar el léxico
jflex src/lexico.flex

# 2. Generar el parser
cup src/sintactico.cup

# 3. Compilar
javac -cp cup.jar src/**/*.java

# 4. Ejecutar con un archivo fuente
java -cp .:cup.jar Main <archivo.txt>
```

## Evaluador

El punto de entrada principal es la clase `Main`. La salida incluye:
- Errores semánticos detectados (tipos incompatibles, variables no declaradas, etc.)
- Código intermedio generado en formato de cuádruplos

---

*Curso: Compiladores e Intérpretes*
