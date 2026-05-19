import java_cup.runtime.*;

%%
%class MiLexer
%unicode
%cup
%line
%column

%{
private Symbol symbol(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1, yytext());
}

private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
}
%}

/* DEFINICIONES */

DIGITO      = [0-9]
LETRA       = [a-zA-Z]
ID          = {LETRA}({LETRA}|{DIGITO}|"_")*
ENTERO      = 0|[1-9][0-9]*
FLOTANTE    = {ENTERO}"."{DIGITO}+

FRACCION    = {ENTERO}"/"{ENTERO}
EXPONENTE   = {ENTERO}"e"{ENTERO}

CHAR        = \'([a-zA-Z0-9_])\'
STRING      = \"([^\"\n]*)\"

%%

/* REGLAS */

"int"       { return symbol(sym.INT); }
"float"     { return symbol(sym.FLOAT); }
"bool"      { return symbol(sym.BOOL); }
"char"      { return symbol(sym.CHAR_TYPE); }
"string"    { return symbol(sym.STRING_TYPE); }

"if"        { return symbol(sym.IF); }
"else"      { return symbol(sym.ELSE); }
"do"        { return symbol(sym.DO); }
"while"     { return symbol(sym.WHILE); }
"switch"    { return symbol(sym.SWITCH); }
"case"      { return symbol(sym.CASE); }
"default"   { return symbol(sym.DEFAULT); }

"cin"       { return symbol(sym.CIN); }
"cout"      { return symbol(sym.COUT); }

"return"    { return symbol(sym.RETURN); }
"break"     { return symbol(sym.BREAK); }

"empty"     { return symbol(sym.EMPTY); }
"__main__"  { return symbol(sym.MAIN); }

/* booleanos */
"true"      { return symbol(sym.TRUE, true); }
"false"     { return symbol(sym.FALSE, false); }

/* SÍMBOLOS */

"<-"        { return symbol(sym.ASSIGN); }
"~"         { return symbol(sym.SEPARATOR); }

"|:"        { return symbol(sym.L_BLOCK); }
":|"        { return symbol(sym.R_BLOCK); }

"<|"        { return symbol(sym.L_PAREN); }
"|>"        { return symbol(sym.R_PAREN); }

"<<"        { return symbol(sym.L_ARRAY); }
">>"        { return symbol(sym.R_ARRAY); }

","         { return symbol(sym.COMMA); }
"!"         { return symbol(sym.END_EXPR); }

"+"         { return symbol(sym.PLUS); }
"-"         { return symbol(sym.MINUS); }
"*"         { return symbol(sym.TIMES); }
"/"         { return symbol(sym.DIV); }
"%"         { return symbol(sym.MOD); }
"^"         { return symbol(sym.POW); }

"@"         { return symbol(sym.AND_LOG); }
"#"         { return symbol(sym.OR_LOG); }

"greather_t"   { return symbol(sym.GREATER_T); }
"less_t"       { return symbol(sym.LESS_T); }
"greather_te"  { return symbol(sym.GREATER_TE); }
"less_te"      { return symbol(sym.LESS_TE); }
"equal"        { return symbol(sym.EQUAL_OP); }
"n_equal"      { return symbol(sym.N_EQUAL); }

/* LITERALES */

{FRACCION}  { return symbol(sym.LIT_FRACCION, yytext()); }
{EXPONENTE} { return symbol(sym.LIT_EXPONENTE, yytext()); }
{FLOTANTE}  { return symbol(sym.LIT_FLOTANTE, Double.parseDouble(yytext())); }
{ENTERO}    { return symbol(sym.LIT_ENTERO, Integer.parseInt(yytext())); }

{CHAR}      { return symbol(sym.LIT_CHAR, yytext().charAt(1)); }
{STRING}    { return symbol(sym.LIT_STRING, yytext()); }

/* ID */

{ID}        { return symbol(sym.ID, yytext()); }

/* COMENTARIOS */

"¡¡".*              { /* ignorar */ }
"{-"([^])*"-}"      { /* ignorar */ }

/* ESPACIOS */

[ \t\r\n]+          { /* ignorar */ }

/* ERROR */

. {
    System.err.println("Error léxico: '" + yytext() +
                       "' en línea " + (yyline + 1));
}
