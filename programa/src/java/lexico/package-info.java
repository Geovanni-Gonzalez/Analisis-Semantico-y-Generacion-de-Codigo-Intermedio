/**
 * Especificacion y salida generada del analizador lexico.
 *
 * <p><strong>Objetivo:</strong> reconocer lexemas del lenguaje fuente,
 * clasificarlos como tokens y registrar informacion util para reportes y para
 * el parser.</p>
 *
 * <p><strong>Entradas:</strong> caracteres del archivo fuente leidos como
 * flujo de texto UTF-8 por el lexer generado desde la especificacion
 * {@code lexico.flex}.</p>
 *
 * <p><strong>Salidas:</strong> tokens CUP, lista de {@code TokenInfo} y
 * diagnosticos lexicos cuando un patron no pertenece al lenguaje.</p>
 *
 * <p><strong>Restricciones:</strong> esta fase no valida estructura gramatical
 * ni tipos; solo clasifica secuencias de caracteres y debe mantener linea y
 * columna consistentes para las fases posteriores.</p>
 */
package lexico;
