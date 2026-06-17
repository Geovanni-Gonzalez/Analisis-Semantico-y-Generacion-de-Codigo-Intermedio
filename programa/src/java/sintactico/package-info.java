/**
 * Especificacion y salida generada del analizador sintactico.
 *
 * <p><strong>Objetivo:</strong> aplicar la gramatica CUP para reconocer
 * programas validos, construir el AST y activar validaciones semanticas
 * asociadas a las reglas reducidas.</p>
 *
 * <p><strong>Entradas:</strong> tokens entregados por el lexer, valores
 * semanticos de esos tokens y posiciones de fuente para reportar errores.</p>
 *
 * <p><strong>Salidas:</strong> {@code ProgramaNodo} como AST principal, tabla
 * de simbolos poblada, lista de errores sintacticos y estado de recuperacion
 * ante fallos de parseo.</p>
 *
 * <p><strong>Restricciones:</strong> el parser no escribe archivos ni genera
 * codigo intermedio; debe recuperarse de errores cuando sea posible para
 * producir varios diagnosticos en una misma corrida.</p>
 */
package sintactico;
