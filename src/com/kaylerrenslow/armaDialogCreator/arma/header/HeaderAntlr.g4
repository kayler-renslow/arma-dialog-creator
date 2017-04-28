//author: Kayler
//since: April 26, 2017

grammar HeaderAntlr;

root_class returns [AST.HeaderClassNode ast] locals[ArrayList<HeaderClass> nested, ArrayList<HeaderAssignment> assigns, String extendText]
    @init{
        $nested = new ArrayList<>();
        $assigns = new ArrayList<>();
    }:
    help=header_class_helper[$nested, $assigns] { $ast = new AST.HeaderClassNode("-root class", null, $assigns, $nested); }
    ;

header_class returns [AST.HeaderClassNode ast] locals[ArrayList<HeaderClass> nested, ArrayList<HeaderAssignment> assigns, String extendText]
    @init{
        $nested = new ArrayList<>();
        $assigns = new ArrayList<>();
        $extendText = null;
    }
    :
    Class cn=Identifier (Colon ex=Identifier {$extendText = $ex.text;})?
    (
        LBrace
        (
            header_class_helper[$nested, $assigns]
        )*
        RBrace
    )?
    Semicolon
    {
        $ast = new AST.HeaderClassNode($cn.text, $extendText, $assigns, $nested);
    }
    ;

header_class_helper[ArrayList<HeaderClass> nested, ArrayList<HeaderAssignment> assigns]:
    c=header_class { $nested.add($c.ast); }
    | a=assignment { $assigns.add($a.ast); }
    | aa=arr_assignment { $assigns.add($aa.ast); }
    ;


assignment returns [AST.HeaderAssignmentNode ast]:
    varName=Identifier Equal val=value Semicolon { $ast = new AST.HeaderAssignmentNode($varName.text, $val.ast); }
    ;

arr_assignment returns [AST.HeaderArrayAssignmentNode ast]:
    varName=Identifier BacketPair eq=(PlusEqual | Equal) val=array Semicolon
    { $ast = new AST.HeaderArrayAssignmentNode($varName.text, $val.ast, $eq.text.equals("+=")); }
    ;

array returns [AST.HeaderArrayNode ast] locals[ArrayList<HeaderArrayItem> items] @init{ $items = new ArrayList<>(); }:
    LBrace
    (
        (array_helper[$items] Comma)+
        | array_helper[$items]
    )?
    RBrace
    { $ast = new AST.HeaderArrayNode($items); }
    ;

array_helper [ArrayList<HeaderArrayItem> items]:
    v=value  { $items.add(new AST.HeaderArrayItemNode($v.ast)); }
    | a=array { $items.add($a.ast); }
    ;

value returns [AST.HeaderValueNode ast]:
    v=everythingButSemicolon { $ast = new AST.HeaderValueNode($v.text); }
    ;

everythingButSemicolon : String | NOT_SEMI ;

String : (Quote [^']* Quote)+ | (DQuote [^"]* DQuote)+ ;
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

fragment NOT_SEMI: [^;}]+ ;

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
Comment : ('//'[^\r\n]+ | '/*' .*? '*/') -> skip; //ignore comments