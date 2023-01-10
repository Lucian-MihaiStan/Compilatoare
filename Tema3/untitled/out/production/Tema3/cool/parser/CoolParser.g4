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
        ID LPAREN (formalParams+=formal (COMMA formalParams+=formal)*)? RPAREN COLON TYPE LBRACE methodBody=expr RBRACE     #method
        | ID COLON TYPE (ASSIGN expr)?                                                                                      #field
    ;

declareVar : ID COLON TYPE (ASSIGN initialValue=expr)? ;

caseBranch : ID COLON TYPE RESULTS_CASE branchBody=expr SEMI;

expr
    :
        obj=expr (AT TYPE)? DOT ID LPAREN (params+=expr (COMMA params+=expr)*)? RPAREN          #dispatch
        | ID LPAREN (params+=expr (COMMA params+=expr)*)? RPAREN                                #implicitDispatch
        | IF cond=expr THEN thenBranch=expr ELSE elseBranch=expr FI                             #if
        | WHILE cond=expr LOOP body=expr POOL                                                   #while
        | LBRACE (statements+=expr SEMI)+ RBRACE                                                #body
        | LET decVars+=declareVar (COMMA decVars+=declareVar)* IN letBody=expr                  #let
        | CASE cond=expr OF (branches+=caseBranch)+ ESAC                                        #case
        | NEW TYPE                                                                              #new
        | TILDA value=expr                                                                      #negation
        | ISVOID value=expr                                                                     #isVoid
        | lhValue=expr MULTIPLY rhValue=expr                                                    #multiply
        | lhValue=expr DIVIDE rhValue=expr                                                      #divide
        | lhValue=expr PLUS rhValue=expr                                                        #plus
        | lhValue=expr MINUS rhValue=expr                                                       #minus
        | lhValue=expr LE rhValue=expr                                                          #le
        | lhValue=expr LT rhValue=expr                                                          #lt
        | lhValue=expr EQ rhValue=expr                                                          #eq
        | NOT value=expr                                                                        #not
        | ID ASSIGN value=expr                                                                  #assign
        | LPAREN expr RPAREN                                                                    #parenExpr
        | ID                                                                                    #id
        | INT                                                                                   #int
        | STRING                                                                                #string
        | bool                                                                                  #boolExpr
    ;

bool : TRUE | FALSE ;
