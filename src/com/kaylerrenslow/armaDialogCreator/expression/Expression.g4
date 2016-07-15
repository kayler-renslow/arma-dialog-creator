grammar Expression;

expression returns [AST.Expr ast]:
    la=expression Plus ra=expression {$ast = new AST.AddExpr($la.ast, $ra.ast);}
    | lm=expression Minus rm=expression {$ast = new AST.SubExpr($lm.ast, $rm.ast);}
    | ls=expression Star rs=expression {$ast = new AST.MultExpr($ls.ast, $rs.ast);}
    | lf=expression FSlash rf=expression {$ast = new AST.DivExpr($lf.ast, $rf.ast);}
    | lu=unary_expression {$ast = $lu.ast;}
    | lp=paren_expression {$ast = $lp.ast;}
    | ll=literal_expression {$ast = $ll.ast;}
    ;

unary_expression returns [AST.UnaryExpr ast]:
    Plus ep=expression {$ast = new AST.UnaryExpr(true, $ep.ast);}
    Minus em=expression {$ast = new AST.UnaryExpr(false, $em.ast);}
    ;

paren_expression returns [AST.ParenExpr ast]:
    LParen e=expression RParen {$ast = new AST.ParenExpr($e.ast);}
    ;

literal_expression returns [AST.LiteralExpr ast]:
    id=Identifier {$ast = new AST.IdentifierExpr($id.text);}
    | i=int_value {$ast = new AST.IntegerExpr($i.i);}
    | f=float_value {$ast = new AST.FloatExpr($f.d);}
    ;

int_value returns [Integer i]:
    il=IntegerLiteral {$i = new Integer($il.text);}
    | hl = HexLiteral {$i = new Integer(Integer.decode($hl.text));}
    ;

float_value returns [Double d]:
    fl=FloatLiteral {$d = new Double($fl.text);}
    ;

Plus : '+';
Minus : '-';
FSlash : '/';
Star : '*';
LParen : '(';
RParen : ')';

Identifier :  Letter LetterOrDigit*;
IntegerLiteral : Digits;
FloatLiteral : (DecSignificand | DecExponent);

Digits : DIGIT+;
DecSignificand : '.' Digits | Digits '.' DIGIT+;
DecExponent : (DecSignificand | IntegerLiteral) [Ee] [+-]? DIGIT*;

HexLiteral : '0' [xX] '0'* HexDigits ;
HexDigits  : {getText().length() >= 1 && getText().length() <= 8}? [0-9a-fA-F]; //allow between 1 and 8 hex digits

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
