grammar Header;

fileEntries : fileEntry*;
fileEntry : BlockComment | InlineComment | statement;
statement : (assignment | classDeclaration) Semicolon;

assignment : basicAssignment | arrayAssignment;
basicAssignment : Identifier Equal expression ;
arrayAssignment : Identifier BracketPair Equal array;

expression : addExpression;
addExpression : multExpression Plus addExpression
    | multExpression Minus addExpression
    | multExpression
    ;
multExpression : term Asterisk multExpression
    | term FSlash multExpression
    | term
    ;
term : (Plus | Minus)? (IntegerLiteral | FloatLiteral | Identifier | LParen expression RParen);

array : LBrace arrayItem (Comma arrayItem)* RBrace;
arrayItem : Identifier | String | IntegerLiteral | FloatLiteral;

classDeclaration : Class Identifier classExtend? classContent?;
classExtend : Colon Identifier;
classContent : LBrace fileEntries RBrace;


Eval : '__EVAL';
Exec : '__EXEC';
Class : 'class';
Comma : ',';
Colon : ':';
Semicolon : ';';
Plus : '+';
Minus : '-';
Asterisk : '*';
FSlash : '/';
Equal : '=';
LParen : '(';
RParen : ')';
LBrace : '{';
RBrace : '}';
BracketPair : '[]';

BlockComment : '/*' .*? '*/';
InlineComment : '//' [^\r\n]*;

Identifier :  Letter LetterOrDigit*;
String : ('"' ~'"')+ ;//if you ever decide to allow single quotes for strings, you must go back and change the search for config function tags, since tag="tag" != tag='tag'
IntegerLiteral : Digits;
FloatLiteral : (DecSignificand | DecExponent);

Digits : DIGIT+;
DecSignificand : '.' Digits | Digits '.' DIGIT+;
DecExponent : (DecSignificand | IntegerLiteral) [Ee] [+-]? DIGIT*;

HexLiteral : '0' [xX] '0'* {HEX_DIGIT} {1,8};
HexDigit   : [0-9a-fA-F];

Letter :   [a-zA-Z$_]
    |   ~[\u0000-\u00FF\uD800-\uDBFF]
    {Character.isJavaIdentifierStart(_input.LA(-1))}?
    |   [\uD800-\uDBFF] [\uDC00-\uDFFF]
    {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}? ;
LetterOrDigit: [a-zA-Z0-9$_]
    |   ~[\u0000-\u00FF\uD800-\uDBFF]
    {Character.isJavaIdentifierPart(_input.LA(-1))}?
    |    [\uD800-\uDBFF] [\uDC00-\uDFFF]
    {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?;

fragment DIGIT: ('0'..'9');
