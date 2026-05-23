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
DIGITO = [0-9]
DIGITO_POS = [1-9]
LETRA = [a-zA-Z]
ID = ({LETRA} | "_") ({LETRA} | {DIGITO} | "_")*
ENTERO_POS = ({DIGITO_POS}{DIGITO}* | "0")
ENTERO = "-"?{ENTERO_POS}
FLOTANTE = {ENTERO}"."{DIGITO}+
FRACCION = {ENTERO}"/"{ENTERO_POS}
EXPONENTE = {ENTERO}"e"{ENTERO_POS}
CHAR    = [a-zA-Z0-9_'!\@\#\$%\^&*=]
STRING     = [a-zA-Z0-9_'!\@\#\$%\^&*=]*

%%

/* REGLAS */

"int" { return symbol(sym.INT); }
"float" { return symbol(sym.FLOAT); }
"bool" { return symbol(sym.BOOL); }
"char" { return symbol(sym.CHAR_TYPE); }
"string" { return symbol(sym.STRING_TYPE); }

"empty" { return symbol(sym.EMPTY); }
"__main__" { return symbol(sym.MAIN); }
"return" { return symbol(sym.RETURN); }
"break" { return symbol(sym.BREAK); }
"if" { return symbol(sym.IF); }
"else" { return symbol(sym.ELSE); }
"do" { return symbol(sym.DO); }
"while" { return symbol(sym.WHILE); }
"switch" { return symbol(sym.SWITCH); }
"case" { return symbol(sym.CASE); }
"default" { return symbol(sym.DEFAULT); }
"cin" { return symbol(sym.CIN); }
"cout" { return symbol(sym.COUT); }

"true" { return symbol(sym.TRUE,  true); }
"false" { return symbol(sym.FALSE, false); }

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
"//"            { return symbol(sym.DIV); }
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
{FRACCION}      { return symbol(sym.LIT_FRACCION,  yytext()); }
{FLOTANTE}      { return symbol(sym.LIT_FLOTANTE,  Double.parseDouble(yytext())); }
{ENTERO}        { return symbol(sym.LIT_ENTERO,    Integer.parseInt(yytext())); }
"'"{CHAR}"'"    { return symbol(sym.LIT_CHAR, yytext().charAt(1)); }
\"{STRING}\"       { return symbol(sym.LIT_STRING, yytext()); }

{ID}            { return symbol(sym.ID, yytext()); }

"¡¡".*          { /* ignorar */ }
"{-"([^])*"-}"  { /* ignorar */ }
[ \t\r\n]+      { /* ignorar */ }


. {
    System.err.println("Error léxico: '" + yytext() +
                       "' en línea " + (yyline + 1) +
                       ", columna " + (yycolumn + 1));
}
