parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program
    :
        (class SEMI)+ EOF
    ;

class
    :
        CLASS
    ;

