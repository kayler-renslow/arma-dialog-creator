//author: Kayler

grammar Expression;

statements returns [List<AST.Statement> lst] @init{ $lst = new ArrayList<>(); }:
    (s=statement Semicolon {$lst.add($s.ast);})*
    s2=statement Semicolon? {$lst.add($s2.ast);}
    ;

statement returns [AST.Statement ast]:
    a=assignment {$ast = new AST.Statement($a.ast);}
    | e=expression {$ast = new AST.Statement($e.ast);}
    ;

assignment returns [AST.Assignment ast]:
    i=Identifier Equal e=expression {$ast = new AST.Assignment($i.text, $e.ast);}
    ;

code returns [AST.Code ast] locals[List<AST.Statement> lst] @init{ $lst = new ArrayList<>(); }:
    LCurly (s=statements {$lst=$s.lst;})? RCurly {$ast = new AST.Code($lst);}
    ;

expression returns [AST.Expr ast]:
    (Not | Excl) notexp=expression {$ast = new AST.NotExpr($notexp.ast);}
    //count
    | Count count_r=expression {$ast = new AST.CountExpr(null, $count_r.ast);}
    | count_l=expression Count count_r=expression {$ast = new AST.CountExpr($count_l.ast, $count_r.ast);}
    //end count
    | Str str_exp=expression {$ast = new AST.StrExpr($str_exp.ast);}
    | lu=unary_expression {$ast = $lu.ast;}
    | lp=paren_expression {$ast = $lp.ast;}
    | ls=expression Star rs=expression {$ast = new AST.MultExpr($ls.ast, $rs.ast);}
    | lf=expression FSlash rf=expression {$ast = new AST.DivExpr($lf.ast, $rf.ast);} //don't mess with order of arguments because of order of operations
    | lperc=expression Perc rperc=expression {$ast = new AST.ModExpr($lperc.ast, $rperc.ast);}
    | lexpon=expression Caret ({$ast = new AST.ExponentExpr($lexpon.ast);} caret_expression_helper[(AST.ExponentExpr)$ast])
    | la=expression Plus ra=expression {$ast = new AST.AddExpr($la.ast, $ra.ast);}
    | lm=expression Minus rm=expression {$ast = new AST.SubExpr($lm.ast, $rm.ast);}
    | lcomp=expression compOp=(EqEq | NotEq | LtEq | Lt | GtEq | Gt) rcomp=expression {$ast = new AST.CompExpr($lcomp.ast, $rcomp.ast, $compOp.text);}
    | land=expression (And | AmpAmp) rand = expression {$ast = new AST.BinLogicalExpr(AST.BinLogicalExpr.Type.And, $land.ast, $rand.ast);}
    | lor=expression (Or | BarBar) ror = expression {$ast = new AST.BinLogicalExpr(AST.BinLogicalExpr.Type.Or, $lor.ast, $ror.ast);}
    | lmax=expression Max rmax=expression {$ast = new AST.MaxExpr($lmax.ast, $rmax.ast);}
    | lmin=expression Min rmin=expression {$ast = new AST.MinExpr($lmin.ast, $rmin.ast);}
    | select_e=expression Select select_i=expression {$ast = new AST.SelectExpr($select_e.ast, $select_i.ast);}
    | ifexp=if_expression {$ast = $ifexp.ast;}
    | forexp=for_expression {$ast = $forexp.ast;}
    | codeExp=code {$ast = new AST.CodeExpr($codeExp.ast);}
    | unaryC=unary_command {$ast = new AST.UnaryCommand($unaryC.text);}
    | ll=literal_expression {$ast = $ll.ast;}
    ;

caret_expression_helper[AST.ExponentExpr ast]:
    (e1=expression Caret {$ast.getExprs().add($e1.ast);})*
    e2=expression {$ast.getExprs().add($e2.ast);}
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
    | a=array {$ast = $a.ast;}
    ;

if_expression returns [AST.IfExpr ast]:
    If cond=expression (
        (ExitWith exitWith=expression {$ast = new AST.IfExpr($cond.ast, $exitWith.ast, null, AST.IfExpr.Type.ExitWith);})
        | (Then arr=array       {$ast = new AST.IfExpr($cond.ast, $arr.ast);})
        | (Then condIsTrue=expression Else condIsFalse=expression {$ast = new AST.IfExpr($cond.ast, $condIsTrue.ast, $condIsFalse.ast, AST.IfExpr.Type.IfThen);})
        | (Then condIsTrue=expression {$ast = new AST.IfExpr($cond.ast, $condIsTrue.ast, null, AST.IfExpr.Type.IfThen);})
    )
    ;

for_expression returns [AST.ForExpr ast]:
    (
     For var=expression From fromExp=expression To toExp=expression Step stepExp=expression Do doExp=expression
     {$ast = new AST.ForVarExpr($var.ast, $fromExp.ast, $toExp.ast, $stepExp.ast, $doExp.ast);}
     )
    |(
     For var=expression From fromExp=expression To toExp=expression Do doExp=expression
     {$ast = new AST.ForVarExpr($var.ast, $fromExp.ast, $toExp.ast, null, $doExp.ast);}
     )
    |(
     For arr=expression Do doExp=expression {$ast = new AST.ForArrExpr($arr.ast, $doExp.ast);}
    )
    ;

array returns [AST.Array ast] locals[List<AST.Expr> items] @init{$items = new ArrayList<>();}:
    LBracket
    (e1=expression {$items.add($e1.ast);})?
    (Comma e2=expression {$items.add($e2.ast);})*
    RBracket {$ast = new AST.Array($items);}
    ;

int_value returns [Integer i]:
    il=IntegerLiteral {$i = new Integer($il.text);}
    | hl = HexLiteral {$i = new Integer(Integer.decode($hl.text));}
    ;

float_value returns [Double d]:
    fl=FloatLiteral {$d = new Double($fl.text);}
    ;

unary_command :
    (
    SafeZoneX
    | SafeZoneY
    | SafeZoneW
    | SafeZoneH
    | SafeZoneXAbs
    | SafeZoneWAbs
    | GetResolution
    )
    ;

String : (Quote ~('\'')* Quote)+ | (DQuote ~('"')* DQuote)+ ;
Quote : '\'';
DQuote : '"';

LCurly : '{';
RCurly : '}';
LBracket : '[';
RBracket : ']';

Plus : '+';
Minus : '-';
FSlash : '/';
Perc :'%';
Caret :'^';
Star : '*';
LParen : '(';
RParen : ')';
Comma :',';
Min : M I N;
Max : M A X;
If : I F;
Then : T H E N;
Else : E L S E;
ExitWith : E X I T W I T H;
Select : S E L E C T;
Count : C O U N T;
For : F O R;
From : F R O M;
To : T O;
Step : S T E P;
Do : D O;
Str: S T R;
EqEq : '==' ;
NotEq : '!=' ;
Lt : '<' ;
LtEq : '<=' ;
Gt : '>' ;
GtEq : '>=' ;
Equal : '=' ;
Semicolon : ';';

Or : O R;
BarBar :'||';
AmpAmp :'&&';
And : A N D;
Not : N O T;
Excl : '!';

//unary commands
SafeZoneX : S A F E Z O N E X;
SafeZoneY : S A F E Z O N E Y;
SafeZoneW : S A F E Z O N E W;
SafeZoneH : S A F E Z O N E H;
SafeZoneXAbs : S A F E Z O N E X A B S;
SafeZoneWAbs : S A F E Z O N E W A B S;
GetResolution : G E T R E S O L U T I O N;


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
Comment : ('//' ~('\r'|'\n')+ | '/*' .*? '*/') -> skip; //ignore comments


fragment DIGIT: ('0'..'9');

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');