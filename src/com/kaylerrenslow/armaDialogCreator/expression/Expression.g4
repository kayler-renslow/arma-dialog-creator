//author: Kayler

grammar Expression;

statements returns [List<AST.Statement> lst] @init{ $lst = new ArrayList<>(); }:
    (s=statement Semicolon {$lst.add($s.ast);})*
    s2=statement Semicolon?{$lst.add($s2.ast);}
    ;

statement returns [AST.Statement ast]:
    a=assignment {$ast = new AST.Statement($a.ast);}
    | e=expression {$ast = new AST.Statement($e.ast);}
    ;

assignment returns [AST.Assignment ast]:
    i=Identifier Equal e=expression {$ast = new AST.Assignment($i.text, $e.ast);}
    ;


expression returns [AST.Expr ast]:
    lu=unary_expression {$ast = $lu.ast;}
    | lp=paren_expression {$ast = $lp.ast;}
    | ls=expression Star rs=expression {$ast = new AST.MultExpr($ls.ast, $rs.ast);}
    | lf=expression FSlash rf=expression {$ast = new AST.DivExpr($lf.ast, $rf.ast);} //don't mess with order of arguments because of order of operations
    | la=expression Plus ra=expression {$ast = new AST.AddExpr($la.ast, $ra.ast);}
    | lm=expression Minus rm=expression {$ast = new AST.SubExpr($lm.ast, $rm.ast);}
    | ll=literal_expression {$ast = $ll.ast;}
    | lmax=expression Max rmax=expression {$ast = new AST.MaxExpr($lmax.ast, $rmax.ast);}
    | lmin=expression Min rmin=expression {$ast = new AST.MinExpr($lmin.ast, $rmin.ast);}
    ;

unary_expression returns [AST.UnaryExpr ast]:
    Plus ep=paren_expression {$ast = new AST.UnaryExpr(true, $ep.ast);}
    | Plus ep1=literal_expression {$ast = new AST.UnaryExpr(true, $ep1.ast);}
    | Minus em=paren_expression {$ast = new AST.UnaryExpr(false, $em.ast);}
    | Minus em1=literal_expression {$ast = new AST.UnaryExpr(false, $em1.ast);}
    ;

paren_expression returns [AST.ParenExpr ast]:
    LParen e=expression RParen {$ast = new AST.ParenExpr($e.ast);}
    ;

literal_expression returns [AST.LiteralExpr ast]:
    id=Identifier {$ast = new AST.IdentifierExpr($id.text);}
    | i=int_value {$ast = new AST.IntegerExpr($i.i);}
    | f=float_value {$ast = new AST.FloatExpr($f.d);}
    | s=String {$ast = new AST.StringExpr($s.text);}
    ;

int_value returns [Integer i]:
    il=IntegerLiteral {$i = new Integer($il.text);}
    | hl = HexLiteral {$i = new Integer(Integer.decode($hl.text));}
    ;

float_value returns [Double d]:
    fl=FloatLiteral {$d = new Double($fl.text);}
    ;

String : (Quote ~('\'')* Quote)+ | (DQuote ~('"')* DQuote)+ ;
Quote : '\'';
DQuote : '"';

Plus : '+';
Minus : '-';
FSlash : '/';
Star : '*';
LParen : '(';
RParen : ')';
Min : 'min';
Max : 'max';
Equal : '=' ;
Semicolon : ';';


Identifier :  Letter LetterOrDigit*;
IntegerLiteral : Digits;
FloatLiteral : (DecSignificand | DecExponent);

Digits : DIGIT+;
DecSignificand : '.' Digits | Digits '.' DIGIT+;
DecExponent : (DecSignificand | IntegerLiteral) [Ee] [+-]? DIGIT*;

HexLiteral : '0' [xX] '0'* HexDigits ;
HexDigits  : [0-9a-fA-F]+; //allow between 1 and 8 hex digits

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

fragment DIGIT: ('0'..'9');
