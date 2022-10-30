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
        ID LPAREN (formal (COMMA formal)*)? RPAREN COLON TYPE LBRACE expr RBRACE    #method
        | ID COLON TYPE (ASSIGN expr)?                                              #field
    ;

expr
    :
        ID ASSIGN expr                                                                          #id_assign_expr
        /* | expr[@TYPE].ID( [ expr [[, expr]]∗ ] ) */
        | ID LPAREN (expr (COMMA expr)*)? RPAREN                                                #id_lparen_expr_comma
        | IF expr THEN expr ELSE expr FI                                                        #if
        | WHILE expr LOOP expr POOL                                                             #while
        | LBRACE (expr SEMI)+ RBRACE                                                            #body
        | LET ID COLON TYPE (ASSIGN expr)? (COMMA ID COLON TYPE (ASSIGN expr)?)* IN expr        #let
        | CASE expr OF (ID COLON TYPE RESULTS_CASE expr SEMI)+ ESAC                             #case
        | NEW TYPE                                                                              #new
        | ISVOID expr                                                                           #isvoidcheck
        | expr PLUS expr                                                                        #plus
        | expr MINUS expr                                                                       #minus
        | expr MULTIPLY expr                                                                    #multiply
        | expr DIVIDE expr                                                                      #divide
        | TILDA expr                                                                            #tilda
        | expr LT expr                                                                          #lt
        | expr LE expr                                                                          #le
        | expr EQ expr                                                                          #eq
        | NOT expr                                                                              #not
        | LPAREN expr RPAREN                                                                    #body_expr
        | ID                                                                                    #id
        | INT                                                                                   #int
        | STRING                                                                                #string
        | bool                                                                                  #bool_expr
//        | TRUE                                                                                  #true
//        | FALSE                                                                                 #false
    ;

bool : TRUE | FALSE ;
