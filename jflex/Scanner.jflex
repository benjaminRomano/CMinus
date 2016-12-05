
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import nodes.OperatorKind;
import types.TypeKind;

%%

%public
%class Lexer
%cup
%implements sym
%char
%line
%column

%{
    StringBuffer string = new StringBuffer();
    public Lexer(java.io.Reader in, ComplexSymbolFactory sf){
        this(in);
        symbolFactory = sf;
    }

    ComplexSymbolFactory symbolFactory;

    private Symbol symbol(String name, int sym) {
        return symbolFactory.newSymbol(name, sym, new Location(yyline+1,yycolumn+1,yychar), new Location(yyline+1,yycolumn+yylength(),yychar+yylength()));
    }

    private Symbol symbol(String name, int sym, Object val) {
        Location left = new Location(yyline+1,yycolumn+1,yychar);
        Location right= new Location(yyline+1,yycolumn+yylength(), yychar+yylength());
        return symbolFactory.newSymbol(name, sym, left, right,val);
    }

    private void error(String message) {
        System.out.println("Error at line "+(yyline+1)+", column "+(yycolumn+1)+" : "+message);
    }
%}

%eofval{
     return symbolFactory.newSymbol("EOF", EOF, new Location(yyline+1,yycolumn+1,yychar), new Location(yyline+1,yycolumn+1,yychar+1));
%eofval}

IDENTIFIER = [a-zA-Z$_] [a-zA-Z0-9$_]*

INTLITERAL = 0 | [1-9][0-9]*

//BOOLLITERAL = true | false;

new_line = \r|\n|\r\n;

white_space = {new_line} | [ \t\f]

comment = "/*" [^*] ~"*/" | "/*" "*"+ "/"

%state STRING

%%

<YYINITIAL>{
    /* keywords */
    "int"             { return symbol("int", INT, TypeKind.Integer); }
    "bool"            { return symbol("bool", BOOL, TypeKind.Boolean); }
    "do"              { return symbol("do", DO); }
    "if"              { return symbol("if", IF); }
    "else"            { return symbol("else", ELSE); }
    "void"            { return symbol("void", VOID); }
    "while"           { return symbol("while", WHILE); }
    "true"            { return symbol("boolLiteral", BOOLLITERAL, true); }
    "false"            { return symbol("boolLiteral", BOOLLITERAL, false); }

    "for"            { return symbol("for", FOR); }
    "continue"            { return symbol("continue", CONTINUE); }
    "break"           { return symbol("break", BREAK); }

    /* literals */
    {INTLITERAL} { return symbol("intLiteral", INTLITERAL, Integer.parseInt(yytext())); }

    {IDENTIFIER} { return symbol("identifier", IDENTIFIER, yytext()); }


    ";"               { return symbol(";", SEMICOLON); }
    ","               { return symbol(",", COMMA); }
    "("               { return symbol("(", OPENPAREN); }
    ")"               { return symbol(")", CLOSEPAREN); }
    "["               { return symbol("[", OPENBRACKET); }
    "]"               { return symbol("]", CLOSEBRACKET); }
    "{"               { return symbol("{", OPENBRACE); }
    "}"               { return symbol("}", CLOSEBRACE); }
    "&"               { return symbol("&", AMPERSAND); }
    "="               { return symbol("=", EQUALS, OperatorKind.Assign); }
    "+"               { return symbol("+", PLUS, OperatorKind.Add); }
    "-"               { return symbol("-", MINUS, OperatorKind.Subtract); }
    "*"               { return symbol("*", ASTERISK, OperatorKind.Multiply); }
    "/"               { return symbol("/", SLASH, OperatorKind.Divide); }
    "%"               { return symbol("%", PERCENT, OperatorKind.Mod); }
    "<="              { return symbol("<=", LESSTHANEQUALS, OperatorKind.LessThanEquals); }
    ">="              { return symbol(">=", GREATERTHANEQUALS, OperatorKind.GreaterThanEquals); }
    "=="              { return symbol("==", EQUALSEQUALS, OperatorKind.Equals); }
    "!="              { return symbol("!=", EXCLAMATIONEQUALS, OperatorKind.NotEquals); }
    "<"               { return symbol("<", LESSTHAN, OperatorKind.LessThan); }
    ">"               { return symbol(">", GREATERTHAN, OperatorKind.GreaterThan); }
    "&&"              { return symbol("&&", AMPERSANDAMPERSAND, OperatorKind.And); }
    "||"              { return symbol("||", BARBAR, OperatorKind.Or); }
    "!"               { return symbol("!", EXCLAMATION, OperatorKind.Not); }

    {white_space}     { }
    {comment}         { }
}

/* error fallback */
[^]              {  error("Illegal character <"+ yytext()+">"); }

