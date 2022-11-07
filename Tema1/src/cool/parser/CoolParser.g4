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
        | ID LPAREN (expr (COMMA expr)*)? RPAREN                                                #implicitDispatch
        | IF cond=expr THEN thenBranch=expr ELSE elseBranch=expr FI                                                        #if
        | WHILE cond=expr LOOP body=expr POOL                                                             #while
        | LBRACE (expr SEMI)+ RBRACE                                                            #body
        | LET declareVar (COMMA declareVar)* IN expr                                            #let
        | CASE cond=expr OF (case_branch)+ ESAC                                                      #case
        | NEW TYPE                                                                              #new
        | TILDA expr                                                                            #negation
        | ISVOID expr                                                                           #isVoid
        | lhValue=expr MULTIPLY rhValue=expr                                                                    #multiply
        | lhValue=expr DIVIDE rhValue=expr                                                                      #divide
        | lhValue=expr PLUS rhValue=expr                                                                        #plus
        | lhValue=expr MINUS rhValue=expr                                                                       #minus
        | lhValue=expr LE rhValue=expr                                                                          #le
        | lhValue=expr LT rhValue=expr                                                                          #lt
        | lhValue=expr EQ rhValue=expr                                                                          #eq
        | NOT value=expr                                                                              #not
        | ID ASSIGN value=expr                                                                        #assign
        | LPAREN expr RPAREN                                                                    #parenExpr
        | ID                                                                                    #id
        | INT                                                                                   #int
        | STRING                                                                                #string
        | bool                                                                                  #boolExpr
    ;

bool : TRUE | FALSE ;
