// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/kaylerrenslow/armaDialogCreator/expression\Expression.g4 by ANTLR 4.7
package com.kaylerrenslow.armaDialogCreator.expression;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExpressionParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		String=1, Quote=2, DQuote=3, LCurly=4, RCurly=5, LBracket=6, RBracket=7, 
		Plus=8, Minus=9, FSlash=10, Perc=11, Caret=12, Star=13, LParen=14, RParen=15, 
		Comma=16, Min=17, Max=18, If=19, Then=20, Else=21, ExitWith=22, Select=23, 
		Count=24, For=25, From=26, To=27, Step=28, Do=29, Str=30, Abs=31, EqEq=32, 
		NotEq=33, Lt=34, LtEq=35, Gt=36, GtEq=37, Equal=38, Semicolon=39, Or=40, 
		BarBar=41, AmpAmp=42, And=43, Not=44, Excl=45, SafeZoneX=46, SafeZoneY=47, 
		SafeZoneW=48, SafeZoneH=49, SafeZoneXAbs=50, SafeZoneWAbs=51, GetResolution=52, 
		Identifier=53, IntegerLiteral=54, FloatLiteral=55, Digits=56, DecSignificand=57, 
		DecExponent=58, HexLiteral=59, HexDigits=60, Letter=61, LetterOrDigit=62, 
		WhiteSpace=63, Comment=64;
	public static final int
		RULE_statements = 0, RULE_statement = 1, RULE_assignment = 2, RULE_code = 3, 
		RULE_expression = 4, RULE_caret_expression_helper = 5, RULE_unary_expression = 6, 
		RULE_paren_expression = 7, RULE_literal_expression = 8, RULE_if_expression = 9, 
		RULE_for_expression = 10, RULE_array = 11, RULE_int_value = 12, RULE_float_value = 13, 
		RULE_unary_command = 14;
	public static final String[] ruleNames = {
		"statements", "statement", "assignment", "code", "expression", "caret_expression_helper", 
		"unary_expression", "paren_expression", "literal_expression", "if_expression", 
		"for_expression", "array", "int_value", "float_value", "unary_command"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'''", "'\"'", "'{'", "'}'", "'['", "']'", "'+'", "'-'", "'/'", 
		"'%'", "'^'", "'*'", "'('", "')'", "','", null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, "'=='", "'!='", 
		"'<'", "'<='", "'>'", "'>='", "'='", "';'", null, "'||'", "'&&'", null, 
		null, "'!'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Quote", "DQuote", "LCurly", "RCurly", "LBracket", "RBracket", 
		"Plus", "Minus", "FSlash", "Perc", "Caret", "Star", "LParen", "RParen", 
		"Comma", "Min", "Max", "If", "Then", "Else", "ExitWith", "Select", "Count", 
		"For", "From", "To", "Step", "Do", "Str", "Abs", "EqEq", "NotEq", "Lt", 
		"LtEq", "Gt", "GtEq", "Equal", "Semicolon", "Or", "BarBar", "AmpAmp", 
		"And", "Not", "Excl", "SafeZoneX", "SafeZoneY", "SafeZoneW", "SafeZoneH", 
		"SafeZoneXAbs", "SafeZoneWAbs", "GetResolution", "Identifier", "IntegerLiteral", 
		"FloatLiteral", "Digits", "DecSignificand", "DecExponent", "HexLiteral", 
		"HexDigits", "Letter", "LetterOrDigit", "WhiteSpace", "Comment"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Expression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ExpressionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class StatementsContext extends ParserRuleContext {
		public List<AST.Statement> lst;
		public StatementContext s;
		public StatementContext s2;
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<TerminalNode> Semicolon() { return getTokens(ExpressionParser.Semicolon); }
		public TerminalNode Semicolon(int i) {
			return getToken(ExpressionParser.Semicolon, i);
		}
		public StatementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterStatements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitStatements(this);
		}
	}

	public final StatementsContext statements() throws RecognitionException {
		StatementsContext _localctx = new StatementsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_statements);
		 ((StatementsContext)_localctx).lst =  new ArrayList<>(); 
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(36);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(30);
					((StatementsContext)_localctx).s = statement();
					setState(31);
					match(Semicolon);
					_localctx.lst.add(((StatementsContext)_localctx).s.ast);
					}
					} 
				}
				setState(38);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(39);
			((StatementsContext)_localctx).s2 = statement();
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Semicolon) {
				{
				setState(40);
				match(Semicolon);
				}
			}

			_localctx.lst.add(((StatementsContext)_localctx).s2.ast);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public AST.Statement ast;
		public AssignmentContext a;
		public ExpressionContext e;
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(51);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(45);
				((StatementContext)_localctx).a = assignment();
				((StatementContext)_localctx).ast =  new AST.Statement(((StatementContext)_localctx).a.ast);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(48);
				((StatementContext)_localctx).e = expression(0);
				((StatementContext)_localctx).ast =  new AST.Statement(((StatementContext)_localctx).e.ast);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentContext extends ParserRuleContext {
		public AST.Assignment ast;
		public Token i;
		public ExpressionContext e;
		public TerminalNode Equal() { return getToken(ExpressionParser.Equal, 0); }
		public TerminalNode Identifier() { return getToken(ExpressionParser.Identifier, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitAssignment(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			((AssignmentContext)_localctx).i = match(Identifier);
			setState(54);
			match(Equal);
			setState(55);
			((AssignmentContext)_localctx).e = expression(0);
			((AssignmentContext)_localctx).ast =  new AST.Assignment((((AssignmentContext)_localctx).i!=null?((AssignmentContext)_localctx).i.getText():null), ((AssignmentContext)_localctx).e.ast);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CodeContext extends ParserRuleContext {
		public AST.Code ast;
		public List<AST.Statement> lst;
		public StatementsContext s;
		public TerminalNode LCurly() { return getToken(ExpressionParser.LCurly, 0); }
		public TerminalNode RCurly() { return getToken(ExpressionParser.RCurly, 0); }
		public StatementsContext statements() {
			return getRuleContext(StatementsContext.class,0);
		}
		public CodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterCode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitCode(this);
		}
	}

	public final CodeContext code() throws RecognitionException {
		CodeContext _localctx = new CodeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_code);
		 ((CodeContext)_localctx).lst =  new ArrayList<>(); 
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(LCurly);
			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << String) | (1L << LCurly) | (1L << LBracket) | (1L << Plus) | (1L << Minus) | (1L << LParen) | (1L << If) | (1L << Count) | (1L << For) | (1L << Str) | (1L << Abs) | (1L << Not) | (1L << Excl) | (1L << SafeZoneX) | (1L << SafeZoneY) | (1L << SafeZoneW) | (1L << SafeZoneH) | (1L << SafeZoneXAbs) | (1L << SafeZoneWAbs) | (1L << GetResolution) | (1L << Identifier) | (1L << IntegerLiteral) | (1L << FloatLiteral) | (1L << HexLiteral))) != 0)) {
				{
				setState(59);
				((CodeContext)_localctx).s = statements();
				((CodeContext)_localctx).lst = ((CodeContext)_localctx).s.lst;
				}
			}

			setState(64);
			match(RCurly);
			((CodeContext)_localctx).ast =  new AST.Code(_localctx.lst);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public AST.Expr ast;
		public ExpressionContext count_l;
		public ExpressionContext ls;
		public ExpressionContext lf;
		public ExpressionContext lperc;
		public ExpressionContext lexpon;
		public ExpressionContext la;
		public ExpressionContext lm;
		public ExpressionContext lcomp;
		public ExpressionContext land;
		public ExpressionContext lor;
		public ExpressionContext lmax;
		public ExpressionContext lmin;
		public ExpressionContext select_e;
		public ExpressionContext notexp;
		public ExpressionContext absexp;
		public ExpressionContext count_r;
		public ExpressionContext str_exp;
		public Unary_expressionContext lu;
		public Paren_expressionContext lp;
		public If_expressionContext ifexp;
		public For_expressionContext forexp;
		public CodeContext codeExp;
		public Unary_commandContext unaryC;
		public Literal_expressionContext ll;
		public ExpressionContext rs;
		public ExpressionContext rf;
		public ExpressionContext rperc;
		public ExpressionContext ra;
		public ExpressionContext rm;
		public Token compOp;
		public ExpressionContext rcomp;
		public ExpressionContext rand;
		public ExpressionContext ror;
		public ExpressionContext rmax;
		public ExpressionContext rmin;
		public ExpressionContext select_i;
		public TerminalNode Not() { return getToken(ExpressionParser.Not, 0); }
		public TerminalNode Excl() { return getToken(ExpressionParser.Excl, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode Abs() { return getToken(ExpressionParser.Abs, 0); }
		public TerminalNode Count() { return getToken(ExpressionParser.Count, 0); }
		public TerminalNode Str() { return getToken(ExpressionParser.Str, 0); }
		public Unary_expressionContext unary_expression() {
			return getRuleContext(Unary_expressionContext.class,0);
		}
		public Paren_expressionContext paren_expression() {
			return getRuleContext(Paren_expressionContext.class,0);
		}
		public If_expressionContext if_expression() {
			return getRuleContext(If_expressionContext.class,0);
		}
		public For_expressionContext for_expression() {
			return getRuleContext(For_expressionContext.class,0);
		}
		public CodeContext code() {
			return getRuleContext(CodeContext.class,0);
		}
		public Unary_commandContext unary_command() {
			return getRuleContext(Unary_commandContext.class,0);
		}
		public Literal_expressionContext literal_expression() {
			return getRuleContext(Literal_expressionContext.class,0);
		}
		public TerminalNode Star() { return getToken(ExpressionParser.Star, 0); }
		public TerminalNode FSlash() { return getToken(ExpressionParser.FSlash, 0); }
		public TerminalNode Perc() { return getToken(ExpressionParser.Perc, 0); }
		public TerminalNode Plus() { return getToken(ExpressionParser.Plus, 0); }
		public TerminalNode Minus() { return getToken(ExpressionParser.Minus, 0); }
		public TerminalNode EqEq() { return getToken(ExpressionParser.EqEq, 0); }
		public TerminalNode NotEq() { return getToken(ExpressionParser.NotEq, 0); }
		public TerminalNode LtEq() { return getToken(ExpressionParser.LtEq, 0); }
		public TerminalNode Lt() { return getToken(ExpressionParser.Lt, 0); }
		public TerminalNode GtEq() { return getToken(ExpressionParser.GtEq, 0); }
		public TerminalNode Gt() { return getToken(ExpressionParser.Gt, 0); }
		public TerminalNode And() { return getToken(ExpressionParser.And, 0); }
		public TerminalNode AmpAmp() { return getToken(ExpressionParser.AmpAmp, 0); }
		public TerminalNode Or() { return getToken(ExpressionParser.Or, 0); }
		public TerminalNode BarBar() { return getToken(ExpressionParser.BarBar, 0); }
		public TerminalNode Max() { return getToken(ExpressionParser.Max, 0); }
		public TerminalNode Min() { return getToken(ExpressionParser.Min, 0); }
		public TerminalNode Select() { return getToken(ExpressionParser.Select, 0); }
		public TerminalNode Caret() { return getToken(ExpressionParser.Caret, 0); }
		public Caret_expression_helperContext caret_expression_helper() {
			return getRuleContext(Caret_expression_helperContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Not:
			case Excl:
				{
				setState(68);
				_la = _input.LA(1);
				if ( !(_la==Not || _la==Excl) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(69);
				((ExpressionContext)_localctx).notexp = expression(24);
				((ExpressionContext)_localctx).ast =  new AST.NotExpr(((ExpressionContext)_localctx).notexp.ast);
				}
				break;
			case Abs:
				{
				setState(72);
				match(Abs);
				setState(73);
				((ExpressionContext)_localctx).absexp = expression(23);
				((ExpressionContext)_localctx).ast =  new AST.AbsExpr(((ExpressionContext)_localctx).absexp.ast);
				}
				break;
			case Count:
				{
				setState(76);
				match(Count);
				setState(77);
				((ExpressionContext)_localctx).count_r = expression(22);
				((ExpressionContext)_localctx).ast =  new AST.CountExpr(null, ((ExpressionContext)_localctx).count_r.ast);
				}
				break;
			case Str:
				{
				setState(80);
				match(Str);
				setState(81);
				((ExpressionContext)_localctx).str_exp = expression(20);
				((ExpressionContext)_localctx).ast =  new AST.StrExpr(((ExpressionContext)_localctx).str_exp.ast);
				}
				break;
			case Plus:
			case Minus:
				{
				setState(84);
				((ExpressionContext)_localctx).lu = unary_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).lu.ast;
				}
				break;
			case LParen:
				{
				setState(87);
				((ExpressionContext)_localctx).lp = paren_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).lp.ast;
				}
				break;
			case If:
				{
				setState(90);
				((ExpressionContext)_localctx).ifexp = if_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).ifexp.ast;
				}
				break;
			case For:
				{
				setState(93);
				((ExpressionContext)_localctx).forexp = for_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).forexp.ast;
				}
				break;
			case LCurly:
				{
				setState(96);
				((ExpressionContext)_localctx).codeExp = code();
				((ExpressionContext)_localctx).ast =  new AST.CodeExpr(((ExpressionContext)_localctx).codeExp.ast);
				}
				break;
			case SafeZoneX:
			case SafeZoneY:
			case SafeZoneW:
			case SafeZoneH:
			case SafeZoneXAbs:
			case SafeZoneWAbs:
			case GetResolution:
				{
				setState(99);
				((ExpressionContext)_localctx).unaryC = unary_command();
				((ExpressionContext)_localctx).ast =  new AST.UnaryCommand((((ExpressionContext)_localctx).unaryC!=null?_input.getText(((ExpressionContext)_localctx).unaryC.start,((ExpressionContext)_localctx).unaryC.stop):null));
				}
				break;
			case String:
			case LBracket:
			case Identifier:
			case IntegerLiteral:
			case FloatLiteral:
			case HexLiteral:
				{
				setState(102);
				((ExpressionContext)_localctx).ll = literal_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).ll.ast;
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(173);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(171);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.count_l = _prevctx;
						_localctx.count_l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(107);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(108);
						match(Count);
						setState(109);
						((ExpressionContext)_localctx).count_r = expression(22);
						((ExpressionContext)_localctx).ast =  new AST.CountExpr(((ExpressionContext)_localctx).count_l.ast, ((ExpressionContext)_localctx).count_r.ast);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.ls = _prevctx;
						_localctx.ls = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(112);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(113);
						match(Star);
						setState(114);
						((ExpressionContext)_localctx).rs = expression(18);
						((ExpressionContext)_localctx).ast =  new AST.MultExpr(((ExpressionContext)_localctx).ls.ast, ((ExpressionContext)_localctx).rs.ast);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lf = _prevctx;
						_localctx.lf = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(117);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(118);
						match(FSlash);
						setState(119);
						((ExpressionContext)_localctx).rf = expression(17);
						((ExpressionContext)_localctx).ast =  new AST.DivExpr(((ExpressionContext)_localctx).lf.ast, ((ExpressionContext)_localctx).rf.ast);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lperc = _prevctx;
						_localctx.lperc = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(122);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(123);
						match(Perc);
						setState(124);
						((ExpressionContext)_localctx).rperc = expression(16);
						((ExpressionContext)_localctx).ast =  new AST.ModExpr(((ExpressionContext)_localctx).lperc.ast, ((ExpressionContext)_localctx).rperc.ast);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.la = _prevctx;
						_localctx.la = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(127);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(128);
						match(Plus);
						setState(129);
						((ExpressionContext)_localctx).ra = expression(14);
						((ExpressionContext)_localctx).ast =  new AST.AddExpr(((ExpressionContext)_localctx).la.ast, ((ExpressionContext)_localctx).ra.ast);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lm = _prevctx;
						_localctx.lm = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(132);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(133);
						match(Minus);
						setState(134);
						((ExpressionContext)_localctx).rm = expression(13);
						((ExpressionContext)_localctx).ast =  new AST.SubExpr(((ExpressionContext)_localctx).lm.ast, ((ExpressionContext)_localctx).rm.ast);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lcomp = _prevctx;
						_localctx.lcomp = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(137);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(138);
						((ExpressionContext)_localctx).compOp = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EqEq) | (1L << NotEq) | (1L << Lt) | (1L << LtEq) | (1L << Gt) | (1L << GtEq))) != 0)) ) {
							((ExpressionContext)_localctx).compOp = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(139);
						((ExpressionContext)_localctx).rcomp = expression(12);
						((ExpressionContext)_localctx).ast =  new AST.CompExpr(((ExpressionContext)_localctx).lcomp.ast, ((ExpressionContext)_localctx).rcomp.ast, (((ExpressionContext)_localctx).compOp!=null?((ExpressionContext)_localctx).compOp.getText():null));
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.land = _prevctx;
						_localctx.land = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(142);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(143);
						_la = _input.LA(1);
						if ( !(_la==AmpAmp || _la==And) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(144);
						((ExpressionContext)_localctx).rand = expression(11);
						((ExpressionContext)_localctx).ast =  new AST.BinLogicalExpr(AST.BinLogicalExpr.Type.And, ((ExpressionContext)_localctx).land.ast, ((ExpressionContext)_localctx).rand.ast);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lor = _prevctx;
						_localctx.lor = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(147);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(148);
						_la = _input.LA(1);
						if ( !(_la==Or || _la==BarBar) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(149);
						((ExpressionContext)_localctx).ror = expression(10);
						((ExpressionContext)_localctx).ast =  new AST.BinLogicalExpr(AST.BinLogicalExpr.Type.Or, ((ExpressionContext)_localctx).lor.ast, ((ExpressionContext)_localctx).ror.ast);
						}
						break;
					case 10:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lmax = _prevctx;
						_localctx.lmax = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(152);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(153);
						match(Max);
						setState(154);
						((ExpressionContext)_localctx).rmax = expression(9);
						((ExpressionContext)_localctx).ast =  new AST.MaxExpr(((ExpressionContext)_localctx).lmax.ast, ((ExpressionContext)_localctx).rmax.ast);
						}
						break;
					case 11:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lmin = _prevctx;
						_localctx.lmin = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(157);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(158);
						match(Min);
						setState(159);
						((ExpressionContext)_localctx).rmin = expression(8);
						((ExpressionContext)_localctx).ast =  new AST.MinExpr(((ExpressionContext)_localctx).lmin.ast, ((ExpressionContext)_localctx).rmin.ast);
						}
						break;
					case 12:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.select_e = _prevctx;
						_localctx.select_e = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(162);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(163);
						match(Select);
						setState(164);
						((ExpressionContext)_localctx).select_i = expression(7);
						((ExpressionContext)_localctx).ast =  new AST.SelectExpr(((ExpressionContext)_localctx).select_e.ast, ((ExpressionContext)_localctx).select_i.ast);
						}
						break;
					case 13:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lexpon = _prevctx;
						_localctx.lexpon = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(167);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(168);
						match(Caret);
						{
						((ExpressionContext)_localctx).ast =  new AST.ExponentExpr(((ExpressionContext)_localctx).lexpon.ast);
						setState(170);
						caret_expression_helper((AST.ExponentExpr)_localctx.ast);
						}
						}
						break;
					}
					} 
				}
				setState(175);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Caret_expression_helperContext extends ParserRuleContext {
		public AST.ExponentExpr ast;
		public ExpressionContext e1;
		public ExpressionContext e2;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> Caret() { return getTokens(ExpressionParser.Caret); }
		public TerminalNode Caret(int i) {
			return getToken(ExpressionParser.Caret, i);
		}
		public Caret_expression_helperContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public Caret_expression_helperContext(ParserRuleContext parent, int invokingState, AST.ExponentExpr ast) {
			super(parent, invokingState);
			this.ast = ast;
		}
		@Override public int getRuleIndex() { return RULE_caret_expression_helper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterCaret_expression_helper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitCaret_expression_helper(this);
		}
	}

	public final Caret_expression_helperContext caret_expression_helper(AST.ExponentExpr ast) throws RecognitionException {
		Caret_expression_helperContext _localctx = new Caret_expression_helperContext(_ctx, getState(), ast);
		enterRule(_localctx, 10, RULE_caret_expression_helper);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(176);
					((Caret_expression_helperContext)_localctx).e1 = expression(0);
					setState(177);
					match(Caret);
					_localctx.ast.getExprs().add(((Caret_expression_helperContext)_localctx).e1.ast);
					}
					} 
				}
				setState(184);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			setState(185);
			((Caret_expression_helperContext)_localctx).e2 = expression(0);
			_localctx.ast.getExprs().add(((Caret_expression_helperContext)_localctx).e2.ast);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unary_expressionContext extends ParserRuleContext {
		public AST.UnaryExpr ast;
		public Paren_expressionContext ep;
		public Literal_expressionContext ep1;
		public Paren_expressionContext em;
		public Literal_expressionContext em1;
		public TerminalNode Plus() { return getToken(ExpressionParser.Plus, 0); }
		public Paren_expressionContext paren_expression() {
			return getRuleContext(Paren_expressionContext.class,0);
		}
		public Literal_expressionContext literal_expression() {
			return getRuleContext(Literal_expressionContext.class,0);
		}
		public TerminalNode Minus() { return getToken(ExpressionParser.Minus, 0); }
		public Unary_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterUnary_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitUnary_expression(this);
		}
	}

	public final Unary_expressionContext unary_expression() throws RecognitionException {
		Unary_expressionContext _localctx = new Unary_expressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_unary_expression);
		try {
			setState(204);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(188);
				match(Plus);
				setState(189);
				((Unary_expressionContext)_localctx).ep = paren_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(true, ((Unary_expressionContext)_localctx).ep.ast);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(192);
				match(Plus);
				setState(193);
				((Unary_expressionContext)_localctx).ep1 = literal_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(true, ((Unary_expressionContext)_localctx).ep1.ast);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(196);
				match(Minus);
				setState(197);
				((Unary_expressionContext)_localctx).em = paren_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(false, ((Unary_expressionContext)_localctx).em.ast);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(200);
				match(Minus);
				setState(201);
				((Unary_expressionContext)_localctx).em1 = literal_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(false, ((Unary_expressionContext)_localctx).em1.ast);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Paren_expressionContext extends ParserRuleContext {
		public AST.ParenExpr ast;
		public ExpressionContext e;
		public TerminalNode LParen() { return getToken(ExpressionParser.LParen, 0); }
		public TerminalNode RParen() { return getToken(ExpressionParser.RParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Paren_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paren_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterParen_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitParen_expression(this);
		}
	}

	public final Paren_expressionContext paren_expression() throws RecognitionException {
		Paren_expressionContext _localctx = new Paren_expressionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_paren_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(LParen);
			setState(207);
			((Paren_expressionContext)_localctx).e = expression(0);
			setState(208);
			match(RParen);
			((Paren_expressionContext)_localctx).ast =  new AST.ParenExpr(((Paren_expressionContext)_localctx).e.ast);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Literal_expressionContext extends ParserRuleContext {
		public AST.LiteralExpr ast;
		public Token id;
		public Int_valueContext i;
		public Float_valueContext f;
		public Token s;
		public ArrayContext a;
		public TerminalNode Identifier() { return getToken(ExpressionParser.Identifier, 0); }
		public Int_valueContext int_value() {
			return getRuleContext(Int_valueContext.class,0);
		}
		public Float_valueContext float_value() {
			return getRuleContext(Float_valueContext.class,0);
		}
		public TerminalNode String() { return getToken(ExpressionParser.String, 0); }
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public Literal_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterLiteral_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitLiteral_expression(this);
		}
	}

	public final Literal_expressionContext literal_expression() throws RecognitionException {
		Literal_expressionContext _localctx = new Literal_expressionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_literal_expression);
		try {
			setState(224);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(211);
				((Literal_expressionContext)_localctx).id = match(Identifier);
				((Literal_expressionContext)_localctx).ast =  new AST.IdentifierExpr((((Literal_expressionContext)_localctx).id!=null?((Literal_expressionContext)_localctx).id.getText():null));
				}
				break;
			case IntegerLiteral:
			case HexLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(213);
				((Literal_expressionContext)_localctx).i = int_value();
				((Literal_expressionContext)_localctx).ast =  new AST.IntegerExpr(((Literal_expressionContext)_localctx).i.i);
				}
				break;
			case FloatLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(216);
				((Literal_expressionContext)_localctx).f = float_value();
				((Literal_expressionContext)_localctx).ast =  new AST.FloatExpr(((Literal_expressionContext)_localctx).f.d);
				}
				break;
			case String:
				enterOuterAlt(_localctx, 4);
				{
				setState(219);
				((Literal_expressionContext)_localctx).s = match(String);
				((Literal_expressionContext)_localctx).ast =  new AST.StringExpr((((Literal_expressionContext)_localctx).s!=null?((Literal_expressionContext)_localctx).s.getText():null));
				}
				break;
			case LBracket:
				enterOuterAlt(_localctx, 5);
				{
				setState(221);
				((Literal_expressionContext)_localctx).a = array();
				((Literal_expressionContext)_localctx).ast =  ((Literal_expressionContext)_localctx).a.ast;
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class If_expressionContext extends ParserRuleContext {
		public AST.IfExpr ast;
		public ExpressionContext cond;
		public ExpressionContext exitWith;
		public ArrayContext arr;
		public ExpressionContext condIsTrue;
		public ExpressionContext condIsFalse;
		public TerminalNode If() { return getToken(ExpressionParser.If, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ExitWith() { return getToken(ExpressionParser.ExitWith, 0); }
		public TerminalNode Then() { return getToken(ExpressionParser.Then, 0); }
		public TerminalNode Else() { return getToken(ExpressionParser.Else, 0); }
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public If_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterIf_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitIf_expression(this);
		}
	}

	public final If_expressionContext if_expression() throws RecognitionException {
		If_expressionContext _localctx = new If_expressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_if_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			match(If);
			setState(227);
			((If_expressionContext)_localctx).cond = expression(0);
			setState(246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				{
				setState(228);
				match(ExitWith);
				setState(229);
				((If_expressionContext)_localctx).exitWith = expression(0);
				((If_expressionContext)_localctx).ast =  new AST.IfExpr(((If_expressionContext)_localctx).cond.ast, ((If_expressionContext)_localctx).exitWith.ast, null, AST.IfExpr.Type.ExitWith);
				}
				}
				break;
			case 2:
				{
				{
				setState(232);
				match(Then);
				setState(233);
				((If_expressionContext)_localctx).arr = array();
				((If_expressionContext)_localctx).ast =  new AST.IfExpr(((If_expressionContext)_localctx).cond.ast, ((If_expressionContext)_localctx).arr.ast);
				}
				}
				break;
			case 3:
				{
				{
				setState(236);
				match(Then);
				setState(237);
				((If_expressionContext)_localctx).condIsTrue = expression(0);
				setState(238);
				match(Else);
				setState(239);
				((If_expressionContext)_localctx).condIsFalse = expression(0);
				((If_expressionContext)_localctx).ast =  new AST.IfExpr(((If_expressionContext)_localctx).cond.ast, ((If_expressionContext)_localctx).condIsTrue.ast, ((If_expressionContext)_localctx).condIsFalse.ast, AST.IfExpr.Type.IfThen);
				}
				}
				break;
			case 4:
				{
				{
				setState(242);
				match(Then);
				setState(243);
				((If_expressionContext)_localctx).condIsTrue = expression(0);
				((If_expressionContext)_localctx).ast =  new AST.IfExpr(((If_expressionContext)_localctx).cond.ast, ((If_expressionContext)_localctx).condIsTrue.ast, null, AST.IfExpr.Type.IfThen);
				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_expressionContext extends ParserRuleContext {
		public AST.ForExpr ast;
		public ExpressionContext var;
		public ExpressionContext fromExp;
		public ExpressionContext toExp;
		public ExpressionContext stepExp;
		public ExpressionContext doExp;
		public ExpressionContext arr;
		public TerminalNode For() { return getToken(ExpressionParser.For, 0); }
		public TerminalNode From() { return getToken(ExpressionParser.From, 0); }
		public TerminalNode To() { return getToken(ExpressionParser.To, 0); }
		public TerminalNode Step() { return getToken(ExpressionParser.Step, 0); }
		public TerminalNode Do() { return getToken(ExpressionParser.Do, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public For_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterFor_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitFor_expression(this);
		}
	}

	public final For_expressionContext for_expression() throws RecognitionException {
		For_expressionContext _localctx = new For_expressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_for_expression);
		try {
			setState(276);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(248);
				match(For);
				setState(249);
				((For_expressionContext)_localctx).var = expression(0);
				setState(250);
				match(From);
				setState(251);
				((For_expressionContext)_localctx).fromExp = expression(0);
				setState(252);
				match(To);
				setState(253);
				((For_expressionContext)_localctx).toExp = expression(0);
				setState(254);
				match(Step);
				setState(255);
				((For_expressionContext)_localctx).stepExp = expression(0);
				setState(256);
				match(Do);
				setState(257);
				((For_expressionContext)_localctx).doExp = expression(0);
				((For_expressionContext)_localctx).ast =  new AST.ForVarExpr(((For_expressionContext)_localctx).var.ast, ((For_expressionContext)_localctx).fromExp.ast, ((For_expressionContext)_localctx).toExp.ast, ((For_expressionContext)_localctx).stepExp.ast, ((For_expressionContext)_localctx).doExp.ast);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(260);
				match(For);
				setState(261);
				((For_expressionContext)_localctx).var = expression(0);
				setState(262);
				match(From);
				setState(263);
				((For_expressionContext)_localctx).fromExp = expression(0);
				setState(264);
				match(To);
				setState(265);
				((For_expressionContext)_localctx).toExp = expression(0);
				setState(266);
				match(Do);
				setState(267);
				((For_expressionContext)_localctx).doExp = expression(0);
				((For_expressionContext)_localctx).ast =  new AST.ForVarExpr(((For_expressionContext)_localctx).var.ast, ((For_expressionContext)_localctx).fromExp.ast, ((For_expressionContext)_localctx).toExp.ast, null, ((For_expressionContext)_localctx).doExp.ast);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(270);
				match(For);
				setState(271);
				((For_expressionContext)_localctx).arr = expression(0);
				setState(272);
				match(Do);
				setState(273);
				((For_expressionContext)_localctx).doExp = expression(0);
				((For_expressionContext)_localctx).ast =  new AST.ForArrExpr(((For_expressionContext)_localctx).arr.ast, ((For_expressionContext)_localctx).doExp.ast);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayContext extends ParserRuleContext {
		public AST.Array ast;
		public List<AST.Expr> items;
		public ExpressionContext e1;
		public ExpressionContext e2;
		public TerminalNode LBracket() { return getToken(ExpressionParser.LBracket, 0); }
		public TerminalNode RBracket() { return getToken(ExpressionParser.RBracket, 0); }
		public List<TerminalNode> Comma() { return getTokens(ExpressionParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(ExpressionParser.Comma, i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitArray(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_array);
		((ArrayContext)_localctx).items =  new ArrayList<>();
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278);
			match(LBracket);
			setState(282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << String) | (1L << LCurly) | (1L << LBracket) | (1L << Plus) | (1L << Minus) | (1L << LParen) | (1L << If) | (1L << Count) | (1L << For) | (1L << Str) | (1L << Abs) | (1L << Not) | (1L << Excl) | (1L << SafeZoneX) | (1L << SafeZoneY) | (1L << SafeZoneW) | (1L << SafeZoneH) | (1L << SafeZoneXAbs) | (1L << SafeZoneWAbs) | (1L << GetResolution) | (1L << Identifier) | (1L << IntegerLiteral) | (1L << FloatLiteral) | (1L << HexLiteral))) != 0)) {
				{
				setState(279);
				((ArrayContext)_localctx).e1 = expression(0);
				_localctx.items.add(((ArrayContext)_localctx).e1.ast);
				}
			}

			setState(290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(284);
				match(Comma);
				setState(285);
				((ArrayContext)_localctx).e2 = expression(0);
				_localctx.items.add(((ArrayContext)_localctx).e2.ast);
				}
				}
				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(293);
			match(RBracket);
			((ArrayContext)_localctx).ast =  new AST.Array(_localctx.items);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Int_valueContext extends ParserRuleContext {
		public Integer i;
		public Token il;
		public Token hl;
		public TerminalNode IntegerLiteral() { return getToken(ExpressionParser.IntegerLiteral, 0); }
		public TerminalNode HexLiteral() { return getToken(ExpressionParser.HexLiteral, 0); }
		public Int_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_int_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterInt_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitInt_value(this);
		}
	}

	public final Int_valueContext int_value() throws RecognitionException {
		Int_valueContext _localctx = new Int_valueContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_int_value);
		try {
			setState(300);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(296);
				((Int_valueContext)_localctx).il = match(IntegerLiteral);
				((Int_valueContext)_localctx).i =  new Integer((((Int_valueContext)_localctx).il!=null?((Int_valueContext)_localctx).il.getText():null));
				}
				break;
			case HexLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(298);
				((Int_valueContext)_localctx).hl = match(HexLiteral);
				((Int_valueContext)_localctx).i =  new Integer(Integer.decode((((Int_valueContext)_localctx).hl!=null?((Int_valueContext)_localctx).hl.getText():null)));
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Float_valueContext extends ParserRuleContext {
		public Double d;
		public Token fl;
		public TerminalNode FloatLiteral() { return getToken(ExpressionParser.FloatLiteral, 0); }
		public Float_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_float_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterFloat_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitFloat_value(this);
		}
	}

	public final Float_valueContext float_value() throws RecognitionException {
		Float_valueContext _localctx = new Float_valueContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_float_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			((Float_valueContext)_localctx).fl = match(FloatLiteral);
			((Float_valueContext)_localctx).d =  new Double((((Float_valueContext)_localctx).fl!=null?((Float_valueContext)_localctx).fl.getText():null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unary_commandContext extends ParserRuleContext {
		public TerminalNode SafeZoneX() { return getToken(ExpressionParser.SafeZoneX, 0); }
		public TerminalNode SafeZoneY() { return getToken(ExpressionParser.SafeZoneY, 0); }
		public TerminalNode SafeZoneW() { return getToken(ExpressionParser.SafeZoneW, 0); }
		public TerminalNode SafeZoneH() { return getToken(ExpressionParser.SafeZoneH, 0); }
		public TerminalNode SafeZoneXAbs() { return getToken(ExpressionParser.SafeZoneXAbs, 0); }
		public TerminalNode SafeZoneWAbs() { return getToken(ExpressionParser.SafeZoneWAbs, 0); }
		public TerminalNode GetResolution() { return getToken(ExpressionParser.GetResolution, 0); }
		public Unary_commandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_command; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).enterUnary_command(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ExpressionListener ) ((ExpressionListener)listener).exitUnary_command(this);
		}
	}

	public final Unary_commandContext unary_command() throws RecognitionException {
		Unary_commandContext _localctx = new Unary_commandContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_unary_command);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SafeZoneX) | (1L << SafeZoneY) | (1L << SafeZoneW) | (1L << SafeZoneH) | (1L << SafeZoneXAbs) | (1L << SafeZoneWAbs) | (1L << GetResolution))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 4:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 21);
		case 1:
			return precpred(_ctx, 17);
		case 2:
			return precpred(_ctx, 16);
		case 3:
			return precpred(_ctx, 15);
		case 4:
			return precpred(_ctx, 13);
		case 5:
			return precpred(_ctx, 12);
		case 6:
			return precpred(_ctx, 11);
		case 7:
			return precpred(_ctx, 10);
		case 8:
			return precpred(_ctx, 9);
		case 9:
			return precpred(_ctx, 8);
		case 10:
			return precpred(_ctx, 7);
		case 11:
			return precpred(_ctx, 6);
		case 12:
			return precpred(_ctx, 14);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3B\u0136\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\2\3\2\7\2"+
		"%\n\2\f\2\16\2(\13\2\3\2\3\2\5\2,\n\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\5\3\66\n\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\5\5A\n\5\3\5\3\5\3\5\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\5\6l\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6"+
		"\u00ae\n\6\f\6\16\6\u00b1\13\6\3\7\3\7\3\7\3\7\7\7\u00b7\n\7\f\7\16\7"+
		"\u00ba\13\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\5\b\u00cf\n\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00e3\n\n\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\5\13\u00f9\n\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f"+
		"\u0117\n\f\3\r\3\r\3\r\3\r\5\r\u011d\n\r\3\r\3\r\3\r\3\r\7\r\u0123\n\r"+
		"\f\r\16\r\u0126\13\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\5\16\u012f\n\16\3"+
		"\17\3\17\3\17\3\20\3\20\3\20\2\3\n\21\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\36\2\7\3\2./\3\2\"\'\3\2,-\3\2*+\3\2\60\66\2\u0151\2&\3\2\2\2\4\65"+
		"\3\2\2\2\6\67\3\2\2\2\b<\3\2\2\2\nk\3\2\2\2\f\u00b8\3\2\2\2\16\u00ce\3"+
		"\2\2\2\20\u00d0\3\2\2\2\22\u00e2\3\2\2\2\24\u00e4\3\2\2\2\26\u0116\3\2"+
		"\2\2\30\u0118\3\2\2\2\32\u012e\3\2\2\2\34\u0130\3\2\2\2\36\u0133\3\2\2"+
		"\2 !\5\4\3\2!\"\7)\2\2\"#\b\2\1\2#%\3\2\2\2$ \3\2\2\2%(\3\2\2\2&$\3\2"+
		"\2\2&\'\3\2\2\2\')\3\2\2\2(&\3\2\2\2)+\5\4\3\2*,\7)\2\2+*\3\2\2\2+,\3"+
		"\2\2\2,-\3\2\2\2-.\b\2\1\2.\3\3\2\2\2/\60\5\6\4\2\60\61\b\3\1\2\61\66"+
		"\3\2\2\2\62\63\5\n\6\2\63\64\b\3\1\2\64\66\3\2\2\2\65/\3\2\2\2\65\62\3"+
		"\2\2\2\66\5\3\2\2\2\678\7\67\2\289\7(\2\29:\5\n\6\2:;\b\4\1\2;\7\3\2\2"+
		"\2<@\7\6\2\2=>\5\2\2\2>?\b\5\1\2?A\3\2\2\2@=\3\2\2\2@A\3\2\2\2AB\3\2\2"+
		"\2BC\7\7\2\2CD\b\5\1\2D\t\3\2\2\2EF\b\6\1\2FG\t\2\2\2GH\5\n\6\32HI\b\6"+
		"\1\2Il\3\2\2\2JK\7!\2\2KL\5\n\6\31LM\b\6\1\2Ml\3\2\2\2NO\7\32\2\2OP\5"+
		"\n\6\30PQ\b\6\1\2Ql\3\2\2\2RS\7 \2\2ST\5\n\6\26TU\b\6\1\2Ul\3\2\2\2VW"+
		"\5\16\b\2WX\b\6\1\2Xl\3\2\2\2YZ\5\20\t\2Z[\b\6\1\2[l\3\2\2\2\\]\5\24\13"+
		"\2]^\b\6\1\2^l\3\2\2\2_`\5\26\f\2`a\b\6\1\2al\3\2\2\2bc\5\b\5\2cd\b\6"+
		"\1\2dl\3\2\2\2ef\5\36\20\2fg\b\6\1\2gl\3\2\2\2hi\5\22\n\2ij\b\6\1\2jl"+
		"\3\2\2\2kE\3\2\2\2kJ\3\2\2\2kN\3\2\2\2kR\3\2\2\2kV\3\2\2\2kY\3\2\2\2k"+
		"\\\3\2\2\2k_\3\2\2\2kb\3\2\2\2ke\3\2\2\2kh\3\2\2\2l\u00af\3\2\2\2mn\f"+
		"\27\2\2no\7\32\2\2op\5\n\6\30pq\b\6\1\2q\u00ae\3\2\2\2rs\f\23\2\2st\7"+
		"\17\2\2tu\5\n\6\24uv\b\6\1\2v\u00ae\3\2\2\2wx\f\22\2\2xy\7\f\2\2yz\5\n"+
		"\6\23z{\b\6\1\2{\u00ae\3\2\2\2|}\f\21\2\2}~\7\r\2\2~\177\5\n\6\22\177"+
		"\u0080\b\6\1\2\u0080\u00ae\3\2\2\2\u0081\u0082\f\17\2\2\u0082\u0083\7"+
		"\n\2\2\u0083\u0084\5\n\6\20\u0084\u0085\b\6\1\2\u0085\u00ae\3\2\2\2\u0086"+
		"\u0087\f\16\2\2\u0087\u0088\7\13\2\2\u0088\u0089\5\n\6\17\u0089\u008a"+
		"\b\6\1\2\u008a\u00ae\3\2\2\2\u008b\u008c\f\r\2\2\u008c\u008d\t\3\2\2\u008d"+
		"\u008e\5\n\6\16\u008e\u008f\b\6\1\2\u008f\u00ae\3\2\2\2\u0090\u0091\f"+
		"\f\2\2\u0091\u0092\t\4\2\2\u0092\u0093\5\n\6\r\u0093\u0094\b\6\1\2\u0094"+
		"\u00ae\3\2\2\2\u0095\u0096\f\13\2\2\u0096\u0097\t\5\2\2\u0097\u0098\5"+
		"\n\6\f\u0098\u0099\b\6\1\2\u0099\u00ae\3\2\2\2\u009a\u009b\f\n\2\2\u009b"+
		"\u009c\7\24\2\2\u009c\u009d\5\n\6\13\u009d\u009e\b\6\1\2\u009e\u00ae\3"+
		"\2\2\2\u009f\u00a0\f\t\2\2\u00a0\u00a1\7\23\2\2\u00a1\u00a2\5\n\6\n\u00a2"+
		"\u00a3\b\6\1\2\u00a3\u00ae\3\2\2\2\u00a4\u00a5\f\b\2\2\u00a5\u00a6\7\31"+
		"\2\2\u00a6\u00a7\5\n\6\t\u00a7\u00a8\b\6\1\2\u00a8\u00ae\3\2\2\2\u00a9"+
		"\u00aa\f\20\2\2\u00aa\u00ab\7\16\2\2\u00ab\u00ac\b\6\1\2\u00ac\u00ae\5"+
		"\f\7\2\u00adm\3\2\2\2\u00adr\3\2\2\2\u00adw\3\2\2\2\u00ad|\3\2\2\2\u00ad"+
		"\u0081\3\2\2\2\u00ad\u0086\3\2\2\2\u00ad\u008b\3\2\2\2\u00ad\u0090\3\2"+
		"\2\2\u00ad\u0095\3\2\2\2\u00ad\u009a\3\2\2\2\u00ad\u009f\3\2\2\2\u00ad"+
		"\u00a4\3\2\2\2\u00ad\u00a9\3\2\2\2\u00ae\u00b1\3\2\2\2\u00af\u00ad\3\2"+
		"\2\2\u00af\u00b0\3\2\2\2\u00b0\13\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\u00b3"+
		"\5\n\6\2\u00b3\u00b4\7\16\2\2\u00b4\u00b5\b\7\1\2\u00b5\u00b7\3\2\2\2"+
		"\u00b6\u00b2\3\2\2\2\u00b7\u00ba\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9"+
		"\3\2\2\2\u00b9\u00bb\3\2\2\2\u00ba\u00b8\3\2\2\2\u00bb\u00bc\5\n\6\2\u00bc"+
		"\u00bd\b\7\1\2\u00bd\r\3\2\2\2\u00be\u00bf\7\n\2\2\u00bf\u00c0\5\20\t"+
		"\2\u00c0\u00c1\b\b\1\2\u00c1\u00cf\3\2\2\2\u00c2\u00c3\7\n\2\2\u00c3\u00c4"+
		"\5\22\n\2\u00c4\u00c5\b\b\1\2\u00c5\u00cf\3\2\2\2\u00c6\u00c7\7\13\2\2"+
		"\u00c7\u00c8\5\20\t\2\u00c8\u00c9\b\b\1\2\u00c9\u00cf\3\2\2\2\u00ca\u00cb"+
		"\7\13\2\2\u00cb\u00cc\5\22\n\2\u00cc\u00cd\b\b\1\2\u00cd\u00cf\3\2\2\2"+
		"\u00ce\u00be\3\2\2\2\u00ce\u00c2\3\2\2\2\u00ce\u00c6\3\2\2\2\u00ce\u00ca"+
		"\3\2\2\2\u00cf\17\3\2\2\2\u00d0\u00d1\7\20\2\2\u00d1\u00d2\5\n\6\2\u00d2"+
		"\u00d3\7\21\2\2\u00d3\u00d4\b\t\1\2\u00d4\21\3\2\2\2\u00d5\u00d6\7\67"+
		"\2\2\u00d6\u00e3\b\n\1\2\u00d7\u00d8\5\32\16\2\u00d8\u00d9\b\n\1\2\u00d9"+
		"\u00e3\3\2\2\2\u00da\u00db\5\34\17\2\u00db\u00dc\b\n\1\2\u00dc\u00e3\3"+
		"\2\2\2\u00dd\u00de\7\3\2\2\u00de\u00e3\b\n\1\2\u00df\u00e0\5\30\r\2\u00e0"+
		"\u00e1\b\n\1\2\u00e1\u00e3\3\2\2\2\u00e2\u00d5\3\2\2\2\u00e2\u00d7\3\2"+
		"\2\2\u00e2\u00da\3\2\2\2\u00e2\u00dd\3\2\2\2\u00e2\u00df\3\2\2\2\u00e3"+
		"\23\3\2\2\2\u00e4\u00e5\7\25\2\2\u00e5\u00f8\5\n\6\2\u00e6\u00e7\7\30"+
		"\2\2\u00e7\u00e8\5\n\6\2\u00e8\u00e9\b\13\1\2\u00e9\u00f9\3\2\2\2\u00ea"+
		"\u00eb\7\26\2\2\u00eb\u00ec\5\30\r\2\u00ec\u00ed\b\13\1\2\u00ed\u00f9"+
		"\3\2\2\2\u00ee\u00ef\7\26\2\2\u00ef\u00f0\5\n\6\2\u00f0\u00f1\7\27\2\2"+
		"\u00f1\u00f2\5\n\6\2\u00f2\u00f3\b\13\1\2\u00f3\u00f9\3\2\2\2\u00f4\u00f5"+
		"\7\26\2\2\u00f5\u00f6\5\n\6\2\u00f6\u00f7\b\13\1\2\u00f7\u00f9\3\2\2\2"+
		"\u00f8\u00e6\3\2\2\2\u00f8\u00ea\3\2\2\2\u00f8\u00ee\3\2\2\2\u00f8\u00f4"+
		"\3\2\2\2\u00f9\25\3\2\2\2\u00fa\u00fb\7\33\2\2\u00fb\u00fc\5\n\6\2\u00fc"+
		"\u00fd\7\34\2\2\u00fd\u00fe\5\n\6\2\u00fe\u00ff\7\35\2\2\u00ff\u0100\5"+
		"\n\6\2\u0100\u0101\7\36\2\2\u0101\u0102\5\n\6\2\u0102\u0103\7\37\2\2\u0103"+
		"\u0104\5\n\6\2\u0104\u0105\b\f\1\2\u0105\u0117\3\2\2\2\u0106\u0107\7\33"+
		"\2\2\u0107\u0108\5\n\6\2\u0108\u0109\7\34\2\2\u0109\u010a\5\n\6\2\u010a"+
		"\u010b\7\35\2\2\u010b\u010c\5\n\6\2\u010c\u010d\7\37\2\2\u010d\u010e\5"+
		"\n\6\2\u010e\u010f\b\f\1\2\u010f\u0117\3\2\2\2\u0110\u0111\7\33\2\2\u0111"+
		"\u0112\5\n\6\2\u0112\u0113\7\37\2\2\u0113\u0114\5\n\6\2\u0114\u0115\b"+
		"\f\1\2\u0115\u0117\3\2\2\2\u0116\u00fa\3\2\2\2\u0116\u0106\3\2\2\2\u0116"+
		"\u0110\3\2\2\2\u0117\27\3\2\2\2\u0118\u011c\7\b\2\2\u0119\u011a\5\n\6"+
		"\2\u011a\u011b\b\r\1\2\u011b\u011d\3\2\2\2\u011c\u0119\3\2\2\2\u011c\u011d"+
		"\3\2\2\2\u011d\u0124\3\2\2\2\u011e\u011f\7\22\2\2\u011f\u0120\5\n\6\2"+
		"\u0120\u0121\b\r\1\2\u0121\u0123\3\2\2\2\u0122\u011e\3\2\2\2\u0123\u0126"+
		"\3\2\2\2\u0124\u0122\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0127\3\2\2\2\u0126"+
		"\u0124\3\2\2\2\u0127\u0128\7\t\2\2\u0128\u0129\b\r\1\2\u0129\31\3\2\2"+
		"\2\u012a\u012b\78\2\2\u012b\u012f\b\16\1\2\u012c\u012d\7=\2\2\u012d\u012f"+
		"\b\16\1\2\u012e\u012a\3\2\2\2\u012e\u012c\3\2\2\2\u012f\33\3\2\2\2\u0130"+
		"\u0131\79\2\2\u0131\u0132\b\17\1\2\u0132\35\3\2\2\2\u0133\u0134\t\6\2"+
		"\2\u0134\37\3\2\2\2\21&+\65@k\u00ad\u00af\u00b8\u00ce\u00e2\u00f8\u0116"+
		"\u011c\u0124\u012e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}