lexer grammar CoolLexer;

tokens { ERROR }

@header{
    package cool.parser;


}

@members{

    private static final int MAX_STRING_LENGTH = 1024;
    public static final String EOF_STRING_ERROR = "EOF in string constant";
    public static final String STRING_CONSTANT_TO_LONG_ERROR = "String constant too long";
    public static final String INVALID_CHARACTER_ERROR = "Invalid character: ";
    public static final String EOF_COMMENT_ERROR = "EOF in comment";
    public static final String UNMATCHED_COMMENT_ERROR = "Unmatched *)";
    public static final String UNTERMINATED_STRING_CONSTANT_ERROR = "Unterminated string constant";
    public static final String STRING_NULL_CHARACTER_ERROR = "String contains null character";
    public static final String NULL_STRING = "\0";

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

/* parentheses */
LPAREN : '(' ;
RPAREN : ')' ;

/* braces */
LBRACE : '{' ;
RBRACE : '}' ;

COMMA : ',' ;
COLON : ':' ;
SEMI : ';' ;

ASSIGN : '<-' ;
RESULTS_CASE : '=>' ;

PLUS : '+' ;
MINUS : '-' ;
MULTIPLY : '*' ;
DIVIDE : '/' ;
TILDA : '~' ;

LT : '<' ;
LE : '<=' ;
EQ : '=' ;
DOT : '.';
AT : '@';

QUOTE : '"';

fragment UPPER_CASE_LETTER : [A-Z] ;
fragment LOWER_CASE_LETTER : [a-z] ;

/*Identifiers*/

/* type identifier */
TYPE : UPPER_CASE_LETTER+ (('_') | UPPER_CASE_LETTER | LOWER_CASE_LETTER | DIGIT)* ;


/* object identifier */
ID : LOWER_CASE_LETTER+ (('_') | UPPER_CASE_LETTER | LOWER_CASE_LETTER | DIGIT)* ;

fragment NEW_LINE : '\r'? '\n';
fragment SINGLE_LINE_COMMENT_START : '--' ;
fragment OPEN_PAREN_STAR : '(*' ;
fragment CLOSE_PAREN_STAR : '*)' ;

LINE_COMMENT
    : SINGLE_LINE_COMMENT_START .*? (NEW_LINE | EOF) -> skip
    ;

BLOCK_COMMENT
    : OPEN_PAREN_STAR
      (BLOCK_COMMENT | .)*?
      (CLOSE_PAREN_STAR { skip(); } | EOF { raiseError(EOF_COMMENT_ERROR); })
    ;

UNMATCHED_COMMENT : CLOSE_PAREN_STAR { raiseError(UNMATCHED_COMMENT_ERROR); } ;

/*
    Integer
*/
fragment DIGIT : [0-9] ;
INT : DIGIT+ ;

STRING: QUOTE ('\\"' | '\\' NEW_LINE | .)*? (
	QUOTE {
		String str = getText().substring(1, getText().length() - 1).replace("\\\r\n", "\r\n").replace("\\\n", "\n").replace("\\n", "\n").replace("\\t", "\t").replace("\\f", "\f").replaceAll("\\\\(?!\\\\)", "");

		if (str.length() > MAX_STRING_LENGTH) {
			raiseError(STRING_CONSTANT_TO_LONG_ERROR);
            return;
        }

		if (str.contains(NULL_STRING)) {
			raiseError(STRING_NULL_CHARACTER_ERROR);
		    return;
		}

		setText(str);
	}
	| EOF { raiseError(EOF_STRING_ERROR); }
	| NEW_LINE { raiseError(UNTERMINATED_STRING_CONSTANT_ERROR); }
);

WS
    :   [ \n\f\r\t]+ -> skip
    ;

INVALID_CHARACTER : . { raiseError(INVALID_CHARACTER_ERROR + getText()); } ;
