lexer grammar Little;

COMMENT : ('--')(~[\t\r\n])*([\t\r\n]) -> skip;

KEYWORD: ('PROGRAM'|'BEGIN'|'END'|'FUNCTION'|'READ'|'WRITE'|'IF'|'ELSE'|'ENDIF'|'WHILE'|'ENDWHILE'|'CONTINUE'|'BREAK'|'RETURN'|'INT'|'VOID'|'STRING'|'FLOAT') ;

STRINGLITERAL: ('"')(~["])*('"') ;

IDENTIFIERS: ('_'|LETTER)('_'|LETTER|DIGIT)* ;

OPERATORS: ('!='|'<='|'>='|'!='|':='|'+'|'-'|'*'|'/'|'='|'<'|'>'|'('|')'|';'|',') ;

FLOATLITERAL : DIGIT'.'DIGIT+ ;

INTLITERAL : DIGIT+ ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

fragment DIGIT : [0-9] ;
fragment LETTER : [a-zA-Z] ;
