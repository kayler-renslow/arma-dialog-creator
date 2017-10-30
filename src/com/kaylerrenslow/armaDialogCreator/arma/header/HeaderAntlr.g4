//author: Kayler
//since: April 26, 2017

grammar HeaderAntlr;

root_class[HeaderFile file] returns [AST.HeaderClassNode ast] locals[ArrayList<HeaderClass> nested, ArrayList<HeaderAssignment> assigns, String extendText]
    @init{
        $nested = new ArrayList<>();
        $assigns = new ArrayList<>();
        $ast = new AST.HeaderClassNode($file, $assigns, $nested);
    }:
    (
    help=header_class_helper[$ast, $nested, $assigns]
    )*
    ;

header_class[HeaderClass parentClass] returns [AST.HeaderClassNode ast] locals[ArrayList<HeaderClass> nested, ArrayList<HeaderAssignment> assigns, String extendText]
    @init{
        $nested = new ArrayList<>();
        $assigns = new ArrayList<>();
        $extendText = null;
        $ast = new AST.HeaderClassNode($parentClass, $assigns, $nested);
    }
    :
    Class cn=Identifier (Colon ex=Identifier {$extendText = $ex.text;})?
    (
        LBrace
        (
            header_class_helper[$ast, $nested, $assigns]
        )*
        RBrace
    )?
    Semicolon
    {
        $ast.setClassName($cn.text);
        $ast.setExtendClassName($extendText);
    }
    ;

header_class_helper[HeaderClass parentClass, ArrayList<HeaderClass> nested, ArrayList<HeaderAssignment> assigns]:
    c=header_class[$parentClass] { $nested.add($c.ast); }
    | a=assignment { $assigns.add($a.ast); }
    | aa=arr_assignment { $assigns.add($aa.ast); }
    ;


assignment returns [AST.HeaderAssignmentNode ast]:
    varName=Identifier Equal Semicolon { $ast = new AST.HeaderAssignmentNode($varName.text, null); }
    | varName=Identifier Equal val=value Semicolon { $ast = new AST.HeaderAssignmentNode($varName.text, $val.ast); }
    ;

arr_assignment returns [AST.HeaderArrayAssignmentNode ast]:
    varName=Identifier BacketPair eq=(PlusEqual | Equal) val=array Semicolon
    { $ast = new AST.HeaderArrayAssignmentNode($varName.text, $val.ast, $eq.text.equals("+=")); }
    ;

array returns [AST.HeaderArrayNode ast] locals[ArrayList<HeaderArrayItem> items] @init{ $items = new ArrayList<>(); }:
    LBrace
    (
        array_helper[$items] (Comma array_helper[$items])*
    )?
    RBrace
    { $ast = new AST.HeaderArrayNode($items); }
    ;

array_helper [ArrayList<HeaderArrayItem> items]:
    v=value  { $items.add(new AST.HeaderArrayItemNode($v.ast)); }
    | a=array { $items.add($a.ast); }
    ;

value returns [AST.HeaderValueNode ast]:
    s=String { $ast = new AST.HeaderValueNode($s.text); }
    | eq=equation { $ast = new AST.HeaderValueNode($eq.text); }
    ;

//match as much as possible. Don't worry about correctness because it will be dealt with later.
equation :
    (Plus | Minus | Star | FSlash | LParen | RParen | Number | Identifier | BSlash | String)+
    ;

String : (Quote ~('\'')* Quote)+ | (DQuote ~('"')* DQuote)+ ;
Class : 'class';
Comma : ',';
Colon : ':';
Semicolon : ';';
PlusEqual : '+=';
Equal : '=';
LBrace : '{';
RBrace : '}';
BacketPair : '[]';
Quote : '\'';
DQuote : '"';
Plus : '+' ;
Minus : '-' ;
Star : '*' ;
FSlash : '/' ;
LParen : '(' ;
RParen : ')' ;
BSlash : '\\' ;

Identifier :  Letter LetterOrDigit*;
Number : INTEGER_LITERAL | DEC_LITERAL | HEX_LITERAL;

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
Comment : ('//' ~('\r'|'\n')+ | '/*' .*? '*/') -> skip; //ignore comments

INTEGER_LITERAL : DIGITS ;
DEC_LITERAL : (DEC_SIGNIFICAND | DEC_EXPONENT) ;
HEX_LITERAL : '0' ('x'|'X') '0'* HEX_DIGIT (HEX_DIGIT? HEX_DIGIT? HEX_DIGIT? HEX_DIGIT? HEX_DIGIT? HEX_DIGIT? HEX_DIGIT? );

fragment DIGITS : ('0'..'9')+ ;

fragment DEC_SIGNIFICAND : '.' DIGITS | DIGITS '.' DIGITS ;
fragment DEC_EXPONENT : (DEC_SIGNIFICAND | INTEGER_LITERAL) ('e'|'E') ('+'|'-')? DIGITS ;

fragment HEX_DIGIT   : (DIGITS | 'a'..'f' | 'A'..'F');
