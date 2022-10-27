lexer grammar CoolLexer;

tokens { ERROR }

@header{
    package cool.lexer;	
}

@members{    
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
}

CLASS : ('c' | 'C') ('l' | 'L') ('a' | 'A') ('s' | 'S') ('s' | 'S') ;
ELSE : ('e' | 'E') ('l' | 'L') ('s' | 'S') ('e' | 'E') ;
FALSE : 'f' ('a' | 'A') ('l' | 'L') ('s' | 'S') ('e' | 'E') ;
FI : ('f' | 'F') ('i' | 'I') ;
IF : ('i' | 'I') ('f' | 'F') ;
IN : ('i' | 'I') ('n' | 'N') ;
INHERITS : ('i' | 'I') ('n' | 'N') ('h' | 'H') ('e' | 'E') ('r' | 'R') ('i' | 'I') ('t' | 'T') ('s' | 'S') ;
ISVOID : ('i' | 'I') ('s' | 'S') ('v' | 'V') ('o' | 'O') ('i' | 'I') ('d' | 'D') ;
LET : ('l' | 'L') ('e' | 'E') ('t' | 'T') ;
LOOP : ('l' | 'L') ('o' | 'O') ('o' | 'O') ('p' | 'P') ;
POOL : ('p' | 'P') ('o' | 'O') ('o' | 'O') ('l' | 'L') ;
THEN : ('t' | 'T') ('h' | 'H') ('e' | 'E') ('n' | 'N') ;
WHILE : ('w' | 'W') ('h' | 'H') ('i' | 'I') ('l' | 'L') ('e' | 'E') ;
CASE : ('c' | 'C') ('a' | 'A') ('s' | 'S') ('e' | 'E') ;
ESAC : ('e' | 'E') ('s' | 'S') ('a' | 'A') ('c' | 'C') ;
NEW : ('n' | 'N') ('e' | 'E') ('w' | 'W') ;
OF : ('o' | 'O') ('f' | 'F') ;
NOT : ('n' | 'N') ('o' | 'O') ('t' | 'T') ;
TRUE : 't' ('r' | 'R') ('u' | 'U') ('e' | 'E') ;

SEMI : ';' ;

fragment UPPER_CASE_LETTER : [A-Z];
fragment LOWER_CASE_LETTER : [a-z];

/*Identifiers*/

/* TYPE ID */
TYPE_ID : UPPER_CASE_LETTER+ (('_') | UPPER_CASE_LETTER | LOWER_CASE_LETTER | DIGIT)*;

/* OBJECT ID */
OBJECT_ID : LOWER_CASE_LETTER+ (('_') | UPPER_CASE_LETTER | LOWER_CASE_LETTER | DIGIT)*;

/*
    Integer
*/
fragment DIGIT : [0-9];
INT : DIGIT+;


WS
    :   [ \n\f\r\t]+ -> skip
    ; 