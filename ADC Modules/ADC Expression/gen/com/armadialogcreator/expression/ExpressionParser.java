// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/ADC Modules/ADC Expression/src/com/armadialogcreator/expression\Expression.g4 by ANTLR 4.7
package com.armadialogcreator.expression;
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
		Count=24, For=25, From=26, To=27, Step=28, Do=29, Str=30, Abs=31, Format=32, 
		EqEq=33, NotEq=34, Lt=35, LtEq=36, Gt=37, GtEq=38, Equal=39, Semicolon=40, 
		Or=41, BarBar=42, AmpAmp=43, And=44, Not=45, Excl=46, SafeZoneX=47, SafeZoneY=48, 
		SafeZoneW=49, SafeZoneH=50, SafeZoneXAbs=51, SafeZoneWAbs=52, GetResolution=53, 
		Identifier=54, IntegerLiteral=55, FloatLiteral=56, Digits=57, DecSignificand=58, 
		DecExponent=59, HexLiteral=60, HexDigits=61, Letter=62, LetterOrDigit=63, 
		WhiteSpace=64, Comment=65;
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
		null, null, null, null, null, null, null, null, null, null, null, "'=='", 
		"'!='", "'<'", "'<='", "'>'", "'>='", "'='", "';'", null, "'||'", "'&&'", 
		null, null, "'!'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Quote", "DQuote", "LCurly", "RCurly", "LBracket", "RBracket", 
		"Plus", "Minus", "FSlash", "Perc", "Caret", "Star", "LParen", "RParen", 
		"Comma", "Min", "Max", "If", "Then", "Else", "ExitWith", "Select", "Count", 
		"For", "From", "To", "Step", "Do", "Str", "Abs", "Format", "EqEq", "NotEq", 
		"Lt", "LtEq", "Gt", "GtEq", "Equal", "Semicolon", "Or", "BarBar", "AmpAmp", 
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
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << String) | (1L << LCurly) | (1L << LBracket) | (1L << Plus) | (1L << Minus) | (1L << LParen) | (1L << If) | (1L << Count) | (1L << For) | (1L << Str) | (1L << Abs) | (1L << Format) | (1L << Not) | (1L << Excl) | (1L << SafeZoneX) | (1L << SafeZoneY) | (1L << SafeZoneW) | (1L << SafeZoneH) | (1L << SafeZoneXAbs) | (1L << SafeZoneWAbs) | (1L << GetResolution) | (1L << Identifier) | (1L << IntegerLiteral) | (1L << FloatLiteral) | (1L << HexLiteral))) != 0)) {
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
		public ExpressionContext format_exp;
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
		public TerminalNode Format() { return getToken(ExpressionParser.Format, 0); }
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
			setState(109);
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
				((ExpressionContext)_localctx).notexp = expression(25);
				((ExpressionContext)_localctx).ast =  new AST.NotExpr(((ExpressionContext)_localctx).notexp.ast);
				}
				break;
			case Abs:
				{
				setState(72);
				match(Abs);
				setState(73);
				((ExpressionContext)_localctx).absexp = expression(24);
				((ExpressionContext)_localctx).ast =  new AST.AbsExpr(((ExpressionContext)_localctx).absexp.ast);
				}
				break;
			case Count:
				{
				setState(76);
				match(Count);
				setState(77);
				((ExpressionContext)_localctx).count_r = expression(23);
				((ExpressionContext)_localctx).ast =  new AST.CountExpr(null, ((ExpressionContext)_localctx).count_r.ast);
				}
				break;
			case Str:
				{
				setState(80);
				match(Str);
				setState(81);
				((ExpressionContext)_localctx).str_exp = expression(21);
				((ExpressionContext)_localctx).ast =  new AST.StrExpr(((ExpressionContext)_localctx).str_exp.ast);
				}
				break;
			case Format:
				{
				setState(84);
				match(Format);
				setState(85);
				((ExpressionContext)_localctx).format_exp = expression(20);
				((ExpressionContext)_localctx).ast =  new AST.FormatExpr(((ExpressionContext)_localctx).format_exp.ast);
				}
				break;
			case Plus:
			case Minus:
				{
				setState(88);
				((ExpressionContext)_localctx).lu = unary_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).lu.ast;
				}
				break;
			case LParen:
				{
				setState(91);
				((ExpressionContext)_localctx).lp = paren_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).lp.ast;
				}
				break;
			case If:
				{
				setState(94);
				((ExpressionContext)_localctx).ifexp = if_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).ifexp.ast;
				}
				break;
			case For:
				{
				setState(97);
				((ExpressionContext)_localctx).forexp = for_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).forexp.ast;
				}
				break;
			case LCurly:
				{
				setState(100);
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
				setState(103);
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
				setState(106);
				((ExpressionContext)_localctx).ll = literal_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).ll.ast;
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(177);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(175);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.count_l = _prevctx;
						_localctx.count_l = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(111);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(112);
						match(Count);
						setState(113);
						((ExpressionContext)_localctx).count_r = expression(23);
						((ExpressionContext)_localctx).ast =  new AST.CountExpr(((ExpressionContext)_localctx).count_l.ast, ((ExpressionContext)_localctx).count_r.ast);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.ls = _prevctx;
						_localctx.ls = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(116);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(117);
						match(Star);
						setState(118);
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
						setState(121);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(122);
						match(FSlash);
						setState(123);
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
						setState(126);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(127);
						match(Perc);
						setState(128);
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
						setState(131);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(132);
						match(Plus);
						setState(133);
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
						setState(136);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(137);
						match(Minus);
						setState(138);
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
						setState(141);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(142);
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
						setState(143);
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
						setState(146);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(147);
						_la = _input.LA(1);
						if ( !(_la==AmpAmp || _la==And) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(148);
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
						setState(151);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(152);
						_la = _input.LA(1);
						if ( !(_la==Or || _la==BarBar) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(153);
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
						setState(156);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(157);
						match(Max);
						setState(158);
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
						setState(161);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(162);
						match(Min);
						setState(163);
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
						setState(166);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(167);
						match(Select);
						setState(168);
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
						setState(171);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(172);
						match(Caret);
						{
						((ExpressionContext)_localctx).ast =  new AST.ExponentExpr(((ExpressionContext)_localctx).lexpon.ast);
						setState(174);
						caret_expression_helper((AST.ExponentExpr)_localctx.ast);
						}
						}
						break;
					}
					} 
				}
				setState(179);
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
			setState(186);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(180);
					((Caret_expression_helperContext)_localctx).e1 = expression(0);
					setState(181);
					match(Caret);
					_localctx.ast.getExprs().add(((Caret_expression_helperContext)_localctx).e1.ast);
					}
					} 
				}
				setState(188);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			setState(189);
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
			setState(208);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(192);
				match(Plus);
				setState(193);
				((Unary_expressionContext)_localctx).ep = paren_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(true, ((Unary_expressionContext)_localctx).ep.ast);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(196);
				match(Plus);
				setState(197);
				((Unary_expressionContext)_localctx).ep1 = literal_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(true, ((Unary_expressionContext)_localctx).ep1.ast);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(200);
				match(Minus);
				setState(201);
				((Unary_expressionContext)_localctx).em = paren_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(false, ((Unary_expressionContext)_localctx).em.ast);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(204);
				match(Minus);
				setState(205);
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
			setState(210);
			match(LParen);
			setState(211);
			((Paren_expressionContext)_localctx).e = expression(0);
			setState(212);
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
			setState(228);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(215);
				((Literal_expressionContext)_localctx).id = match(Identifier);
				((Literal_expressionContext)_localctx).ast =  new AST.IdentifierExpr((((Literal_expressionContext)_localctx).id!=null?((Literal_expressionContext)_localctx).id.getText():null));
				}
				break;
			case IntegerLiteral:
			case HexLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(217);
				((Literal_expressionContext)_localctx).i = int_value();
				((Literal_expressionContext)_localctx).ast =  new AST.IntegerExpr(((Literal_expressionContext)_localctx).i.i);
				}
				break;
			case FloatLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(220);
				((Literal_expressionContext)_localctx).f = float_value();
				((Literal_expressionContext)_localctx).ast =  new AST.FloatExpr(((Literal_expressionContext)_localctx).f.d);
				}
				break;
			case String:
				enterOuterAlt(_localctx, 4);
				{
				setState(223);
				((Literal_expressionContext)_localctx).s = match(String);
				((Literal_expressionContext)_localctx).ast =  new AST.StringExpr((((Literal_expressionContext)_localctx).s!=null?((Literal_expressionContext)_localctx).s.getText():null));
				}
				break;
			case LBracket:
				enterOuterAlt(_localctx, 5);
				{
				setState(225);
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
			setState(230);
			match(If);
			setState(231);
			((If_expressionContext)_localctx).cond = expression(0);
			setState(250);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				{
				setState(232);
				match(ExitWith);
				setState(233);
				((If_expressionContext)_localctx).exitWith = expression(0);
				((If_expressionContext)_localctx).ast =  new AST.IfExpr(((If_expressionContext)_localctx).cond.ast, ((If_expressionContext)_localctx).exitWith.ast, null, AST.IfExpr.Type.ExitWith);
				}
				}
				break;
			case 2:
				{
				{
				setState(236);
				match(Then);
				setState(237);
				((If_expressionContext)_localctx).arr = array();
				((If_expressionContext)_localctx).ast =  new AST.IfExpr(((If_expressionContext)_localctx).cond.ast, ((If_expressionContext)_localctx).arr.ast);
				}
				}
				break;
			case 3:
				{
				{
				setState(240);
				match(Then);
				setState(241);
				((If_expressionContext)_localctx).condIsTrue = expression(0);
				setState(242);
				match(Else);
				setState(243);
				((If_expressionContext)_localctx).condIsFalse = expression(0);
				((If_expressionContext)_localctx).ast =  new AST.IfExpr(((If_expressionContext)_localctx).cond.ast, ((If_expressionContext)_localctx).condIsTrue.ast, ((If_expressionContext)_localctx).condIsFalse.ast, AST.IfExpr.Type.IfThen);
				}
				}
				break;
			case 4:
				{
				{
				setState(246);
				match(Then);
				setState(247);
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
			setState(280);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(252);
				match(For);
				setState(253);
				((For_expressionContext)_localctx).var = expression(0);
				setState(254);
				match(From);
				setState(255);
				((For_expressionContext)_localctx).fromExp = expression(0);
				setState(256);
				match(To);
				setState(257);
				((For_expressionContext)_localctx).toExp = expression(0);
				setState(258);
				match(Step);
				setState(259);
				((For_expressionContext)_localctx).stepExp = expression(0);
				setState(260);
				match(Do);
				setState(261);
				((For_expressionContext)_localctx).doExp = expression(0);
				((For_expressionContext)_localctx).ast =  new AST.ForVarExpr(((For_expressionContext)_localctx).var.ast, ((For_expressionContext)_localctx).fromExp.ast, ((For_expressionContext)_localctx).toExp.ast, ((For_expressionContext)_localctx).stepExp.ast, ((For_expressionContext)_localctx).doExp.ast);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(264);
				match(For);
				setState(265);
				((For_expressionContext)_localctx).var = expression(0);
				setState(266);
				match(From);
				setState(267);
				((For_expressionContext)_localctx).fromExp = expression(0);
				setState(268);
				match(To);
				setState(269);
				((For_expressionContext)_localctx).toExp = expression(0);
				setState(270);
				match(Do);
				setState(271);
				((For_expressionContext)_localctx).doExp = expression(0);
				((For_expressionContext)_localctx).ast =  new AST.ForVarExpr(((For_expressionContext)_localctx).var.ast, ((For_expressionContext)_localctx).fromExp.ast, ((For_expressionContext)_localctx).toExp.ast, null, ((For_expressionContext)_localctx).doExp.ast);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(274);
				match(For);
				setState(275);
				((For_expressionContext)_localctx).arr = expression(0);
				setState(276);
				match(Do);
				setState(277);
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
			setState(282);
			match(LBracket);
			setState(286);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << String) | (1L << LCurly) | (1L << LBracket) | (1L << Plus) | (1L << Minus) | (1L << LParen) | (1L << If) | (1L << Count) | (1L << For) | (1L << Str) | (1L << Abs) | (1L << Format) | (1L << Not) | (1L << Excl) | (1L << SafeZoneX) | (1L << SafeZoneY) | (1L << SafeZoneW) | (1L << SafeZoneH) | (1L << SafeZoneXAbs) | (1L << SafeZoneWAbs) | (1L << GetResolution) | (1L << Identifier) | (1L << IntegerLiteral) | (1L << FloatLiteral) | (1L << HexLiteral))) != 0)) {
				{
				setState(283);
				((ArrayContext)_localctx).e1 = expression(0);
				_localctx.items.add(((ArrayContext)_localctx).e1.ast);
				}
			}

			setState(294);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(288);
				match(Comma);
				setState(289);
				((ArrayContext)_localctx).e2 = expression(0);
				_localctx.items.add(((ArrayContext)_localctx).e2.ast);
				}
				}
				setState(296);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(297);
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
			setState(304);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(300);
				((Int_valueContext)_localctx).il = match(IntegerLiteral);
				((Int_valueContext)_localctx).i =  new Integer((((Int_valueContext)_localctx).il!=null?((Int_valueContext)_localctx).il.getText():null));
				}
				break;
			case HexLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(302);
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
			setState(306);
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
			setState(309);
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
			return precpred(_ctx, 22);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3C\u013a\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\2\3\2\7\2"+
		"%\n\2\f\2\16\2(\13\2\3\2\3\2\5\2,\n\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\5\3\66\n\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\5\5A\n\5\3\5\3\5\3\5\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6p\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\7\6\u00b2\n\6\f\6\16\6\u00b5\13\6\3\7\3\7\3\7\3\7\7\7\u00bb"+
		"\n\7\f\7\16\7\u00be\13\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u00d3\n\b\3\t\3\t\3\t\3\t\3\t\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00e7\n\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\5\13\u00fd\n\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\5\f\u011b\n\f\3\r\3\r\3\r\3\r\5\r\u0121\n\r\3\r\3\r\3\r\3\r\7"+
		"\r\u0127\n\r\f\r\16\r\u012a\13\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\5\16"+
		"\u0133\n\16\3\17\3\17\3\17\3\20\3\20\3\20\2\3\n\21\2\4\6\b\n\f\16\20\22"+
		"\24\26\30\32\34\36\2\7\3\2/\60\3\2#(\3\2-.\3\2+,\3\2\61\67\2\u0156\2&"+
		"\3\2\2\2\4\65\3\2\2\2\6\67\3\2\2\2\b<\3\2\2\2\no\3\2\2\2\f\u00bc\3\2\2"+
		"\2\16\u00d2\3\2\2\2\20\u00d4\3\2\2\2\22\u00e6\3\2\2\2\24\u00e8\3\2\2\2"+
		"\26\u011a\3\2\2\2\30\u011c\3\2\2\2\32\u0132\3\2\2\2\34\u0134\3\2\2\2\36"+
		"\u0137\3\2\2\2 !\5\4\3\2!\"\7*\2\2\"#\b\2\1\2#%\3\2\2\2$ \3\2\2\2%(\3"+
		"\2\2\2&$\3\2\2\2&\'\3\2\2\2\')\3\2\2\2(&\3\2\2\2)+\5\4\3\2*,\7*\2\2+*"+
		"\3\2\2\2+,\3\2\2\2,-\3\2\2\2-.\b\2\1\2.\3\3\2\2\2/\60\5\6\4\2\60\61\b"+
		"\3\1\2\61\66\3\2\2\2\62\63\5\n\6\2\63\64\b\3\1\2\64\66\3\2\2\2\65/\3\2"+
		"\2\2\65\62\3\2\2\2\66\5\3\2\2\2\678\78\2\289\7)\2\29:\5\n\6\2:;\b\4\1"+
		"\2;\7\3\2\2\2<@\7\6\2\2=>\5\2\2\2>?\b\5\1\2?A\3\2\2\2@=\3\2\2\2@A\3\2"+
		"\2\2AB\3\2\2\2BC\7\7\2\2CD\b\5\1\2D\t\3\2\2\2EF\b\6\1\2FG\t\2\2\2GH\5"+
		"\n\6\33HI\b\6\1\2Ip\3\2\2\2JK\7!\2\2KL\5\n\6\32LM\b\6\1\2Mp\3\2\2\2NO"+
		"\7\32\2\2OP\5\n\6\31PQ\b\6\1\2Qp\3\2\2\2RS\7 \2\2ST\5\n\6\27TU\b\6\1\2"+
		"Up\3\2\2\2VW\7\"\2\2WX\5\n\6\26XY\b\6\1\2Yp\3\2\2\2Z[\5\16\b\2[\\\b\6"+
		"\1\2\\p\3\2\2\2]^\5\20\t\2^_\b\6\1\2_p\3\2\2\2`a\5\24\13\2ab\b\6\1\2b"+
		"p\3\2\2\2cd\5\26\f\2de\b\6\1\2ep\3\2\2\2fg\5\b\5\2gh\b\6\1\2hp\3\2\2\2"+
		"ij\5\36\20\2jk\b\6\1\2kp\3\2\2\2lm\5\22\n\2mn\b\6\1\2np\3\2\2\2oE\3\2"+
		"\2\2oJ\3\2\2\2oN\3\2\2\2oR\3\2\2\2oV\3\2\2\2oZ\3\2\2\2o]\3\2\2\2o`\3\2"+
		"\2\2oc\3\2\2\2of\3\2\2\2oi\3\2\2\2ol\3\2\2\2p\u00b3\3\2\2\2qr\f\30\2\2"+
		"rs\7\32\2\2st\5\n\6\31tu\b\6\1\2u\u00b2\3\2\2\2vw\f\23\2\2wx\7\17\2\2"+
		"xy\5\n\6\24yz\b\6\1\2z\u00b2\3\2\2\2{|\f\22\2\2|}\7\f\2\2}~\5\n\6\23~"+
		"\177\b\6\1\2\177\u00b2\3\2\2\2\u0080\u0081\f\21\2\2\u0081\u0082\7\r\2"+
		"\2\u0082\u0083\5\n\6\22\u0083\u0084\b\6\1\2\u0084\u00b2\3\2\2\2\u0085"+
		"\u0086\f\17\2\2\u0086\u0087\7\n\2\2\u0087\u0088\5\n\6\20\u0088\u0089\b"+
		"\6\1\2\u0089\u00b2\3\2\2\2\u008a\u008b\f\16\2\2\u008b\u008c\7\13\2\2\u008c"+
		"\u008d\5\n\6\17\u008d\u008e\b\6\1\2\u008e\u00b2\3\2\2\2\u008f\u0090\f"+
		"\r\2\2\u0090\u0091\t\3\2\2\u0091\u0092\5\n\6\16\u0092\u0093\b\6\1\2\u0093"+
		"\u00b2\3\2\2\2\u0094\u0095\f\f\2\2\u0095\u0096\t\4\2\2\u0096\u0097\5\n"+
		"\6\r\u0097\u0098\b\6\1\2\u0098\u00b2\3\2\2\2\u0099\u009a\f\13\2\2\u009a"+
		"\u009b\t\5\2\2\u009b\u009c\5\n\6\f\u009c\u009d\b\6\1\2\u009d\u00b2\3\2"+
		"\2\2\u009e\u009f\f\n\2\2\u009f\u00a0\7\24\2\2\u00a0\u00a1\5\n\6\13\u00a1"+
		"\u00a2\b\6\1\2\u00a2\u00b2\3\2\2\2\u00a3\u00a4\f\t\2\2\u00a4\u00a5\7\23"+
		"\2\2\u00a5\u00a6\5\n\6\n\u00a6\u00a7\b\6\1\2\u00a7\u00b2\3\2\2\2\u00a8"+
		"\u00a9\f\b\2\2\u00a9\u00aa\7\31\2\2\u00aa\u00ab\5\n\6\t\u00ab\u00ac\b"+
		"\6\1\2\u00ac\u00b2\3\2\2\2\u00ad\u00ae\f\20\2\2\u00ae\u00af\7\16\2\2\u00af"+
		"\u00b0\b\6\1\2\u00b0\u00b2\5\f\7\2\u00b1q\3\2\2\2\u00b1v\3\2\2\2\u00b1"+
		"{\3\2\2\2\u00b1\u0080\3\2\2\2\u00b1\u0085\3\2\2\2\u00b1\u008a\3\2\2\2"+
		"\u00b1\u008f\3\2\2\2\u00b1\u0094\3\2\2\2\u00b1\u0099\3\2\2\2\u00b1\u009e"+
		"\3\2\2\2\u00b1\u00a3\3\2\2\2\u00b1\u00a8\3\2\2\2\u00b1\u00ad\3\2\2\2\u00b2"+
		"\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\13\3\2\2"+
		"\2\u00b5\u00b3\3\2\2\2\u00b6\u00b7\5\n\6\2\u00b7\u00b8\7\16\2\2\u00b8"+
		"\u00b9\b\7\1\2\u00b9\u00bb\3\2\2\2\u00ba\u00b6\3\2\2\2\u00bb\u00be\3\2"+
		"\2\2\u00bc\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00bf\3\2\2\2\u00be"+
		"\u00bc\3\2\2\2\u00bf\u00c0\5\n\6\2\u00c0\u00c1\b\7\1\2\u00c1\r\3\2\2\2"+
		"\u00c2\u00c3\7\n\2\2\u00c3\u00c4\5\20\t\2\u00c4\u00c5\b\b\1\2\u00c5\u00d3"+
		"\3\2\2\2\u00c6\u00c7\7\n\2\2\u00c7\u00c8\5\22\n\2\u00c8\u00c9\b\b\1\2"+
		"\u00c9\u00d3\3\2\2\2\u00ca\u00cb\7\13\2\2\u00cb\u00cc\5\20\t\2\u00cc\u00cd"+
		"\b\b\1\2\u00cd\u00d3\3\2\2\2\u00ce\u00cf\7\13\2\2\u00cf\u00d0\5\22\n\2"+
		"\u00d0\u00d1\b\b\1\2\u00d1\u00d3\3\2\2\2\u00d2\u00c2\3\2\2\2\u00d2\u00c6"+
		"\3\2\2\2\u00d2\u00ca\3\2\2\2\u00d2\u00ce\3\2\2\2\u00d3\17\3\2\2\2\u00d4"+
		"\u00d5\7\20\2\2\u00d5\u00d6\5\n\6\2\u00d6\u00d7\7\21\2\2\u00d7\u00d8\b"+
		"\t\1\2\u00d8\21\3\2\2\2\u00d9\u00da\78\2\2\u00da\u00e7\b\n\1\2\u00db\u00dc"+
		"\5\32\16\2\u00dc\u00dd\b\n\1\2\u00dd\u00e7\3\2\2\2\u00de\u00df\5\34\17"+
		"\2\u00df\u00e0\b\n\1\2\u00e0\u00e7\3\2\2\2\u00e1\u00e2\7\3\2\2\u00e2\u00e7"+
		"\b\n\1\2\u00e3\u00e4\5\30\r\2\u00e4\u00e5\b\n\1\2\u00e5\u00e7\3\2\2\2"+
		"\u00e6\u00d9\3\2\2\2\u00e6\u00db\3\2\2\2\u00e6\u00de\3\2\2\2\u00e6\u00e1"+
		"\3\2\2\2\u00e6\u00e3\3\2\2\2\u00e7\23\3\2\2\2\u00e8\u00e9\7\25\2\2\u00e9"+
		"\u00fc\5\n\6\2\u00ea\u00eb\7\30\2\2\u00eb\u00ec\5\n\6\2\u00ec\u00ed\b"+
		"\13\1\2\u00ed\u00fd\3\2\2\2\u00ee\u00ef\7\26\2\2\u00ef\u00f0\5\30\r\2"+
		"\u00f0\u00f1\b\13\1\2\u00f1\u00fd\3\2\2\2\u00f2\u00f3\7\26\2\2\u00f3\u00f4"+
		"\5\n\6\2\u00f4\u00f5\7\27\2\2\u00f5\u00f6\5\n\6\2\u00f6\u00f7\b\13\1\2"+
		"\u00f7\u00fd\3\2\2\2\u00f8\u00f9\7\26\2\2\u00f9\u00fa\5\n\6\2\u00fa\u00fb"+
		"\b\13\1\2\u00fb\u00fd\3\2\2\2\u00fc\u00ea\3\2\2\2\u00fc\u00ee\3\2\2\2"+
		"\u00fc\u00f2\3\2\2\2\u00fc\u00f8\3\2\2\2\u00fd\25\3\2\2\2\u00fe\u00ff"+
		"\7\33\2\2\u00ff\u0100\5\n\6\2\u0100\u0101\7\34\2\2\u0101\u0102\5\n\6\2"+
		"\u0102\u0103\7\35\2\2\u0103\u0104\5\n\6\2\u0104\u0105\7\36\2\2\u0105\u0106"+
		"\5\n\6\2\u0106\u0107\7\37\2\2\u0107\u0108\5\n\6\2\u0108\u0109\b\f\1\2"+
		"\u0109\u011b\3\2\2\2\u010a\u010b\7\33\2\2\u010b\u010c\5\n\6\2\u010c\u010d"+
		"\7\34\2\2\u010d\u010e\5\n\6\2\u010e\u010f\7\35\2\2\u010f\u0110\5\n\6\2"+
		"\u0110\u0111\7\37\2\2\u0111\u0112\5\n\6\2\u0112\u0113\b\f\1\2\u0113\u011b"+
		"\3\2\2\2\u0114\u0115\7\33\2\2\u0115\u0116\5\n\6\2\u0116\u0117\7\37\2\2"+
		"\u0117\u0118\5\n\6\2\u0118\u0119\b\f\1\2\u0119\u011b\3\2\2\2\u011a\u00fe"+
		"\3\2\2\2\u011a\u010a\3\2\2\2\u011a\u0114\3\2\2\2\u011b\27\3\2\2\2\u011c"+
		"\u0120\7\b\2\2\u011d\u011e\5\n\6\2\u011e\u011f\b\r\1\2\u011f\u0121\3\2"+
		"\2\2\u0120\u011d\3\2\2\2\u0120\u0121\3\2\2\2\u0121\u0128\3\2\2\2\u0122"+
		"\u0123\7\22\2\2\u0123\u0124\5\n\6\2\u0124\u0125\b\r\1\2\u0125\u0127\3"+
		"\2\2\2\u0126\u0122\3\2\2\2\u0127\u012a\3\2\2\2\u0128\u0126\3\2\2\2\u0128"+
		"\u0129\3\2\2\2\u0129\u012b\3\2\2\2\u012a\u0128\3\2\2\2\u012b\u012c\7\t"+
		"\2\2\u012c\u012d\b\r\1\2\u012d\31\3\2\2\2\u012e\u012f\79\2\2\u012f\u0133"+
		"\b\16\1\2\u0130\u0131\7>\2\2\u0131\u0133\b\16\1\2\u0132\u012e\3\2\2\2"+
		"\u0132\u0130\3\2\2\2\u0133\33\3\2\2\2\u0134\u0135\7:\2\2\u0135\u0136\b"+
		"\17\1\2\u0136\35\3\2\2\2\u0137\u0138\t\6\2\2\u0138\37\3\2\2\2\21&+\65"+
		"@o\u00b1\u00b3\u00bc\u00d2\u00e6\u00fc\u011a\u0120\u0128\u0132";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}