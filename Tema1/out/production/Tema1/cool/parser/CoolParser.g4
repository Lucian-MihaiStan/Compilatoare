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

declareVar : ID COLON TYPE (ASSIGN expr)? ;

case_branch : ID COLON TYPE RESULTS_CASE expr SEMI;

expr
    :
        expr (AT TYPE)? DOT ID LPAREN (expr (COMMA expr)*)? RPAREN                              #dispatch
        | ID LPAREN (expr (COMMA expr)*)? RPAREN                                                #implicit_dispatch
        | IF expr THEN expr ELSE expr FI                                                        #if
        | WHILE expr LOOP expr POOL                                                             #while
        | LBRACE (expr SEMI)+ RBRACE                                                            #body
        | LET declareVar (COMMA declareVar)* IN expr                                            #let
        | CASE expr OF (case_branch)+ ESAC                             #case
        | NEW TYPE                                                                              #new
        | TILDA expr                                                                            #bit_neg
        | ISVOID expr                                                                           #isvoidcheck
        | expr MULTIPLY expr                                                                    #multiply
        | expr DIVIDE expr                                                                      #divide
        | expr PLUS expr                                                                        #plus
        | expr MINUS expr                                                                       #minus
        | expr LE expr                                                                          #le
        | expr LT expr                                                                          #lt
        | expr EQ expr                                                                          #eq
        | NOT expr                                                                              #not
        | ID ASSIGN expr                                                                        #id_assign_expr
        | LPAREN expr RPAREN                                                                    #paren_expr
        | ID                                                                                    #id
        | INT                                                                                   #int
        | STRING                                                                                #string
        | bool                                                                                  #bool_expr
    ;

bool : TRUE | FALSE ;
