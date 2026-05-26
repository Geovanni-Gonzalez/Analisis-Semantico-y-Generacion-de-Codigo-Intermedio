import java_cup.runtime.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

%%

%class MiLexer
%unicode
%cup
%line
%column
%state COMMENT

%{
public static class TokenInfo {
    public final int id;
    public final String nombre;
    public final String lexema;
    public final int linea;
    public final int columna;
    public final String tabla;
    public final String informacion;

    public TokenInfo(int id, String nombre, String lexema, int linea, int columna,
                     String tabla, String informacion) {
        this.id = id;
        this.nombre = nombre;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
        this.tabla = tabla;
        this.informacion = informacion;
    }
}

private final List<TokenInfo> tokens = new ArrayList<>();
private final List<String> erroresLexicos = new ArrayList<>();
private boolean imprimirErrores = true;

public List<TokenInfo> getTokens() {
    return Collections.unmodifiableList(tokens);
}

public List<String> getErroresLexicos() {
    return Collections.unmodifiableList(erroresLexicos);
}

public void setImprimirErrores(boolean imprimirErrores) {
    this.imprimirErrores = imprimirErrores;
}

private Symbol symbol(int type) {
    return symbol(type, yytext());
}

private Symbol symbol(int type, Object value) {
    int linea = yyline + 1;
    int columna = yycolumn + 1;
    String lexema = yytext();
    tokens.add(new TokenInfo(type, nombreToken(type), lexema, linea, columna,
                             tablaPara(type), informacionPara(type, lexema, value)));
    return new Symbol(type, linea, columna, value);
}

private void errorLexico() {
    String error = "Error lexico: '" + yytext() + "' en linea " + (yyline + 1)
            + ", columna " + (yycolumn + 1);
    erroresLexicos.add(error);
    if (imprimirErrores) {
        System.err.println(error);
    }
}

private String nombreToken(int type) {
    try {
        for (java.lang.reflect.Field field : sym.class.getFields()) {
            if (field.getType() == int.class && field.getInt(null) == type) {
                return field.getName();
            }
        }
    } catch (IllegalAccessException ignored) {
    }
    return "TOKEN_" + type;
}

private String tablaPara(int type) {
    switch (type) {
        case sym.ID:
            return "Tabla de identificadores";
        case sym.LIT_ENTERO:
        case sym.LIT_FLOTANTE:
        case sym.LIT_FRACCION:
        case sym.LIT_EXPONENTE:
        case sym.LIT_CHAR:
        case sym.LIT_STRING:
        case sym.TRUE:
        case sym.FALSE:
            return "Tabla de literales/constantes";
        default:
            return "Tabla de palabras reservadas y simbolos";
    }
}

private String informacionPara(int type, String lexema, Object value) {
    switch (type) {
        case sym.ID:
            return "lexema=" + lexema + ", clase=identificador";
        case sym.LIT_ENTERO:
        case sym.LIT_FLOTANTE:
        case sym.LIT_FRACCION:
        case sym.LIT_EXPONENTE:
        case sym.LIT_CHAR:
        case sym.LIT_STRING:
        case sym.TRUE:
        case sym.FALSE:
            return "lexema=" + lexema + ", valor=" + value;
        default:
            return "lexema=" + lexema;
    }
}
%}

/* DEFINICIONES */
DIGITO = [0-9]
DIGITO_POS = [1-9]
LETRA = [a-zA-Z]
ID = ({LETRA} | "_") ({LETRA} | {DIGITO} | "_")*
ENTERO_POS = ({DIGITO_POS}{DIGITO}* | "0")
ENTERO = "-"?{ENTERO_POS}
FLOTANTE = {ENTERO}"."{DIGITO}+
FRACCION = {ENTERO}"/"{ENTERO_POS}
EXPONENTE = {ENTERO}"e"{ENTERO_POS}
CHAR = [^\r\n']
STRING = [^\r\n\"]*

%%

/* REGLAS */

"int"           { return symbol(sym.INT); }
"float"         { return symbol(sym.FLOAT); }
"bool"          { return symbol(sym.BOOL); }
"char"          { return symbol(sym.CHAR_TYPE); }
"string"        { return symbol(sym.STRING_TYPE); }

"empty"         { return symbol(sym.EMPTY); }
"__main__"      { return symbol(sym.MAIN); }
"return"        { return symbol(sym.RETURN); }
"break"         { return symbol(sym.BREAK); }
"if"            { return symbol(sym.IF); }
"else"          { return symbol(sym.ELSE); }
"do"            { return symbol(sym.DO); }
"while"         { return symbol(sym.WHILE); }
"switch"        { return symbol(sym.SWITCH); }
"case"          { return symbol(sym.CASE); }
"default"       { return symbol(sym.DEFAULT); }
"cin"           { return symbol(sym.CIN); }
"cout"          { return symbol(sym.COUT); }

"true"          { return symbol(sym.TRUE, true); }
"false"         { return symbol(sym.FALSE, false); }

"equal"         { return symbol(sym.EQUAL); }
"n_equal"       { return symbol(sym.N_EQUAL); }
"less_t"        { return symbol(sym.LESS_T); }
"less_te"       { return symbol(sym.LESS_TE); }
"greather_t"    { return symbol(sym.GREATHER_T); }
"greather_te"   { return symbol(sym.GREATHER_TE); }

"++"            { return symbol(sym.INC); }
"--"            { return symbol(sym.DEC); }
"+"             { return symbol(sym.PLUS); }
"-"             { return symbol(sym.MINUS); }
"*"             { return symbol(sym.TIMES); }
"/"             { return symbol(sym.DIV); }
"%"             { return symbol(sym.MOD); }
"^"             { return symbol(sym.POW); }

"@"             { return symbol(sym.AND_LOG); }
"#"             { return symbol(sym.OR_LOG); }
"$"             { return symbol(sym.NOT_LOG); }

"<-"            { return symbol(sym.ASSIGN); }

"<|"            { return symbol(sym.L_PAREN); }
"|>"            { return symbol(sym.R_PAREN); }

"|:"            { return symbol(sym.L_BLOCK); }
":|"            { return symbol(sym.R_BLOCK); }

"<<"            { return symbol(sym.L_ARRAY); }
">>"            { return symbol(sym.R_ARRAY); }

"~"             { return symbol(sym.SEPARATOR); }
","             { return symbol(sym.COMMA); }
":"             { return symbol(sym.COLON); }
"!"             { return symbol(sym.END_EXPR); }

{EXPONENTE}     { return symbol(sym.LIT_EXPONENTE, yytext()); }
{FRACCION}      { return symbol(sym.LIT_FRACCION, yytext()); }
{FLOTANTE}      { return symbol(sym.LIT_FLOTANTE, Double.parseDouble(yytext())); }
{ENTERO}        { return symbol(sym.LIT_ENTERO, Integer.parseInt(yytext())); }
"'"{CHAR}{CHAR}+"'" { return symbol(sym.BAD_CHAR, yytext()); }
"'"{CHAR}"'"     { return symbol(sym.LIT_CHAR, yytext().charAt(1)); }
\"{STRING}\"    { return symbol(sym.LIT_STRING, yytext()); }

{ID}            { return symbol(sym.ID, yytext()); }

"\u00A1\u00A1".* { /* ignorar */ }
\{\-            { yybegin(COMMENT); }
<COMMENT>\-\}   { yybegin(YYINITIAL); }
<COMMENT>[^-\r\n]+ { /* ignorar */ }
<COMMENT>\r|\n  { /* ignorar */ }
<COMMENT>-      { /* ignorar */ }
[ \t\r\n]+      { /* ignorar */ }

.               { errorLexico(); }
