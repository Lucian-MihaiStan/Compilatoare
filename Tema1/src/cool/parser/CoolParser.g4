parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program : (class SEMI)+ EOF ;

class : CLASS TYPE (INHERITS TYPE)? LBRACE (feature SEMI)* RBRACE ;

formal : ID COLON TYPE;

feature
    :
        ID LPAREN (formal (COMMA formal))? RPAREN COLON TYPE LBRACE expr RBRACE
        | ID COLON TYPE (ASSIGN expr)?
    ;

expr
    :
        ID ASSIGN expr
        /* | expr[@TYPE].ID( [ expr [[, expr]]∗ ] ) */
        | ID LPAREN (expr (COMMA expr)*)? RPAREN
        | IF expr THEN expr ELSE expr FI
        | WHILE expr LOOP expr POOL
        | LBRACE (expr SEMI)+ RBRACE
        | LET ID COLON TYPE (ASSIGN expr)? (COMMA ID COLON TYPE (ASSIGN expr)?)* IN expr
        | CASE expr OF (ID COLON TYPE RESULTS_CASE expr SEMI)+ ESAC
        | NEW TYPE
        | ISVOID expr
        | expr PLUS expr
        | expr MINUS expr
        | expr MULTIPLY expr
        | expr DIVIDE expr
        | TILDA expr
        | expr LT expr
        | expr LE expr
        | expr EQ expr
        | NOT expr
        | LPAREN expr RPAREN
        | ID
        | INT
        | STRING
        | TRUE
        | FALSE
    ;

