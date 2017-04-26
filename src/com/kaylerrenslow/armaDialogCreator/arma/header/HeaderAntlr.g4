//author: Kayler
//since: April 26, 2017

grammar HeaderAntlr;

root_class returns [AST.HeaderClassNode ast]:
    h=header_class {$ast = $h.ast;}
    ;

//todo add assignments and nested classes
header_class returns [AST.HeaderClassNode ast]:
    Class cn=Identifier {$ast = new AST.HeaderClassNode($cn.text, null);} |
    Class cnWithEx=Identifier Colon ex=Identifier {$ast = new AST.HeaderClassNode($cnWithEx.text, $ex.text);}
    ;


assignment returns [AST.HeaderAssignmentNode ast]:
    varName=Identifier Equal val=value Semicolon {$ast = new AST.HeaderAssignmentNode($varName.text, $val.ast);}
    ;

arr_assignment returns [AST.HeaderArrayAssignmentNode ast]:
    varName=Identifier eq=(PlusEqual | Equal) val=array Semicolon
    {$ast = new AST.HeaderArrayAssignmentNode($varName.text, $val.ast, $eq.text.equals("+="));}
    ;

array returns [AST.HeaderArrayNode ast]:
    LBrace v=value RBrace
    {$ast = new AST.HeaderArrayNode();} //move to @init ?
    ;

value returns [AST.HeaderValueNode ast]:
    v=EverythingButSemicolon {$ast = new AST.HeaderValueNode($v.text);}
    ;

EverythingButSemicolon : (String | [^;])+ ;
String : (Quote [^']* Quote)+ | (DQuote [^"]* DQuote)+ ;

Class : 'Class';
Comma : ',';
Colon : ':';
Semicolon : ';';
PlusEqual : '+=';
Equal : '=';
LBrace : '{';
RBrace : '}';
LBracket : '[';
RBracket : ']';
Quote : '\'';
DQuote : '"';


Identifier :  Letter LetterOrDigit*;

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

WhiteSpace : (' '|'\t'|'\r'|'\n'|'\r\n') -> skip; //ignore whitespace
Comment : ('//'[^\r\n]+ || '/*' .*? '*/') -> skip; //ignore comments