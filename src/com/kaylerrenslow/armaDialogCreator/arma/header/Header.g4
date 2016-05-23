grammar Header;

file_entries : file_entry*;
file_entry : BlockComment | InlineComment | statement;
statement : (assignment | class_declaration) Semicolon;

assignment : basic_assignment | array_assignment;
basic_assignment : Identifier Equal expression ;
array_assignment : Identifier BracketPair Equal array;

expression : add_expression;
add_expression : mult_expression Plus add_expression
    | mult_expression Minus add_expression
    | mult_expression
    ;
mult_expression : term Asterisk mult_expression
    | term FSlash mult_expression
    | term
    ;
term : (Plus | Minus)? (IntegerLiteral | FloatLiteral | Identifier | LParen expression RParen);

array : LBrace array_item (Comma array_item)* RBrace;
array_item : Identifier | String | IntegerLiteral | FloatLiteral;

class_declaration : Class Identifier class_extend? class_content?;
class_extend : Colon Identifier;
class_content : LBrace file_entries RBrace;


Define :'#define';
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
