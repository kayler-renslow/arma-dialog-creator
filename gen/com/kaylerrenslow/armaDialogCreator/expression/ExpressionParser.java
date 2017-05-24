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
		String=1, Quote=2, DQuote=3, Plus=4, Minus=5, FSlash=6, Star=7, LParen=8, 
		RParen=9, Min=10, Max=11, Equal=12, Semicolon=13, Identifier=14, IntegerLiteral=15, 
		FloatLiteral=16, Digits=17, DecSignificand=18, DecExponent=19, HexLiteral=20, 
		HexDigits=21, Letter=22, LetterOrDigit=23, WhiteSpace=24;
	public static final int
		RULE_statements = 0, RULE_statement = 1, RULE_assignment = 2, RULE_expression = 3, 
		RULE_unary_expression = 4, RULE_paren_expression = 5, RULE_literal_expression = 6, 
		RULE_int_value = 7, RULE_float_value = 8;
	public static final String[] ruleNames = {
		"statements", "statement", "assignment", "expression", "unary_expression", 
		"paren_expression", "literal_expression", "int_value", "float_value"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'''", "'\"'", "'+'", "'-'", "'/'", "'*'", "'('", "')'", "'min'", 
		"'max'", "'='", "';'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Quote", "DQuote", "Plus", "Minus", "FSlash", "Star", 
		"LParen", "RParen", "Min", "Max", "Equal", "Semicolon", "Identifier", 
		"IntegerLiteral", "FloatLiteral", "Digits", "DecSignificand", "DecExponent", 
		"HexLiteral", "HexDigits", "Letter", "LetterOrDigit", "WhiteSpace"
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
			setState(24);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(18);
					((StatementsContext)_localctx).s = statement();
					setState(19);
					match(Semicolon);
					_localctx.lst.add(((StatementsContext)_localctx).s.ast);
					}
					} 
				}
				setState(26);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(27);
			((StatementsContext)_localctx).s2 = statement();
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Semicolon) {
				{
				setState(28);
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
			setState(39);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(33);
				((StatementContext)_localctx).a = assignment();
				((StatementContext)_localctx).ast =  new AST.Statement(((StatementContext)_localctx).a.ast);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(36);
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
			setState(41);
			((AssignmentContext)_localctx).i = match(Identifier);
			setState(42);
			match(Equal);
			setState(43);
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

	public static class ExpressionContext extends ParserRuleContext {
		public AST.Expr ast;
		public ExpressionContext ls;
		public ExpressionContext lf;
		public ExpressionContext la;
		public ExpressionContext lm;
		public ExpressionContext lmax;
		public ExpressionContext lmin;
		public Unary_expressionContext lu;
		public Paren_expressionContext lp;
		public Literal_expressionContext ll;
		public ExpressionContext rs;
		public ExpressionContext rf;
		public ExpressionContext ra;
		public ExpressionContext rm;
		public ExpressionContext rmax;
		public ExpressionContext rmin;
		public Unary_expressionContext unary_expression() {
			return getRuleContext(Unary_expressionContext.class,0);
		}
		public Paren_expressionContext paren_expression() {
			return getRuleContext(Paren_expressionContext.class,0);
		}
		public Literal_expressionContext literal_expression() {
			return getRuleContext(Literal_expressionContext.class,0);
		}
		public TerminalNode Star() { return getToken(ExpressionParser.Star, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode FSlash() { return getToken(ExpressionParser.FSlash, 0); }
		public TerminalNode Plus() { return getToken(ExpressionParser.Plus, 0); }
		public TerminalNode Minus() { return getToken(ExpressionParser.Minus, 0); }
		public TerminalNode Max() { return getToken(ExpressionParser.Max, 0); }
		public TerminalNode Min() { return getToken(ExpressionParser.Min, 0); }
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
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Plus:
			case Minus:
				{
				setState(47);
				((ExpressionContext)_localctx).lu = unary_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).lu.ast;
				}
				break;
			case LParen:
				{
				setState(50);
				((ExpressionContext)_localctx).lp = paren_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).lp.ast;
				}
				break;
			case String:
			case Identifier:
			case IntegerLiteral:
			case FloatLiteral:
			case HexLiteral:
				{
				setState(53);
				((ExpressionContext)_localctx).ll = literal_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).ll.ast;
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(90);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(88);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.ls = _prevctx;
						_localctx.ls = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(58);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(59);
						match(Star);
						setState(60);
						((ExpressionContext)_localctx).rs = expression(8);
						((ExpressionContext)_localctx).ast =  new AST.MultExpr(((ExpressionContext)_localctx).ls.ast, ((ExpressionContext)_localctx).rs.ast);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lf = _prevctx;
						_localctx.lf = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(63);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(64);
						match(FSlash);
						setState(65);
						((ExpressionContext)_localctx).rf = expression(7);
						((ExpressionContext)_localctx).ast =  new AST.DivExpr(((ExpressionContext)_localctx).lf.ast, ((ExpressionContext)_localctx).rf.ast);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.la = _prevctx;
						_localctx.la = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(68);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(69);
						match(Plus);
						setState(70);
						((ExpressionContext)_localctx).ra = expression(6);
						((ExpressionContext)_localctx).ast =  new AST.AddExpr(((ExpressionContext)_localctx).la.ast, ((ExpressionContext)_localctx).ra.ast);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lm = _prevctx;
						_localctx.lm = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(73);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(74);
						match(Minus);
						setState(75);
						((ExpressionContext)_localctx).rm = expression(5);
						((ExpressionContext)_localctx).ast =  new AST.SubExpr(((ExpressionContext)_localctx).lm.ast, ((ExpressionContext)_localctx).rm.ast);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lmax = _prevctx;
						_localctx.lmax = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(78);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(79);
						match(Max);
						setState(80);
						((ExpressionContext)_localctx).rmax = expression(3);
						((ExpressionContext)_localctx).ast =  new AST.MaxExpr(((ExpressionContext)_localctx).lmax.ast, ((ExpressionContext)_localctx).rmax.ast);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.lmin = _prevctx;
						_localctx.lmin = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(83);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(84);
						match(Min);
						setState(85);
						((ExpressionContext)_localctx).rmin = expression(2);
						((ExpressionContext)_localctx).ast =  new AST.MinExpr(((ExpressionContext)_localctx).lmin.ast, ((ExpressionContext)_localctx).rmin.ast);
						}
						break;
					}
					} 
				}
				setState(92);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
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
		enterRule(_localctx, 8, RULE_unary_expression);
		try {
			setState(109);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(93);
				match(Plus);
				setState(94);
				((Unary_expressionContext)_localctx).ep = paren_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(true, ((Unary_expressionContext)_localctx).ep.ast);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				match(Plus);
				setState(98);
				((Unary_expressionContext)_localctx).ep1 = literal_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(true, ((Unary_expressionContext)_localctx).ep1.ast);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(101);
				match(Minus);
				setState(102);
				((Unary_expressionContext)_localctx).em = paren_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(false, ((Unary_expressionContext)_localctx).em.ast);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(105);
				match(Minus);
				setState(106);
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
		enterRule(_localctx, 10, RULE_paren_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			match(LParen);
			setState(112);
			((Paren_expressionContext)_localctx).e = expression(0);
			setState(113);
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
		public TerminalNode Identifier() { return getToken(ExpressionParser.Identifier, 0); }
		public Int_valueContext int_value() {
			return getRuleContext(Int_valueContext.class,0);
		}
		public Float_valueContext float_value() {
			return getRuleContext(Float_valueContext.class,0);
		}
		public TerminalNode String() { return getToken(ExpressionParser.String, 0); }
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
		enterRule(_localctx, 12, RULE_literal_expression);
		try {
			setState(126);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(116);
				((Literal_expressionContext)_localctx).id = match(Identifier);
				((Literal_expressionContext)_localctx).ast =  new AST.IdentifierExpr((((Literal_expressionContext)_localctx).id!=null?((Literal_expressionContext)_localctx).id.getText():null));
				}
				break;
			case IntegerLiteral:
			case HexLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
				((Literal_expressionContext)_localctx).i = int_value();
				((Literal_expressionContext)_localctx).ast =  new AST.IntegerExpr(((Literal_expressionContext)_localctx).i.i);
				}
				break;
			case FloatLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(121);
				((Literal_expressionContext)_localctx).f = float_value();
				((Literal_expressionContext)_localctx).ast =  new AST.FloatExpr(((Literal_expressionContext)_localctx).f.d);
				}
				break;
			case String:
				enterOuterAlt(_localctx, 4);
				{
				setState(124);
				((Literal_expressionContext)_localctx).s = match(String);
				((Literal_expressionContext)_localctx).ast =  new AST.StringExpr((((Literal_expressionContext)_localctx).s!=null?((Literal_expressionContext)_localctx).s.getText():null));
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
		enterRule(_localctx, 14, RULE_int_value);
		try {
			setState(132);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(128);
				((Int_valueContext)_localctx).il = match(IntegerLiteral);
				((Int_valueContext)_localctx).i =  new Integer((((Int_valueContext)_localctx).il!=null?((Int_valueContext)_localctx).il.getText():null));
				}
				break;
			case HexLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(130);
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
		enterRule(_localctx, 16, RULE_float_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 3:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		case 2:
			return precpred(_ctx, 5);
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 2);
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\32\u008c\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3"+
		"\2\3\2\3\2\7\2\31\n\2\f\2\16\2\34\13\2\3\2\3\2\5\2 \n\2\3\2\3\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\5\3*\n\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\5\5;\n\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\7\5[\n\5\f\5\16\5^\13\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6p\n\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u0081\n\b\3\t\3\t\3\t\3\t\5\t\u0087\n\t"+
		"\3\n\3\n\3\n\3\n\2\3\b\13\2\4\6\b\n\f\16\20\22\2\2\2\u0094\2\32\3\2\2"+
		"\2\4)\3\2\2\2\6+\3\2\2\2\b:\3\2\2\2\no\3\2\2\2\fq\3\2\2\2\16\u0080\3\2"+
		"\2\2\20\u0086\3\2\2\2\22\u0088\3\2\2\2\24\25\5\4\3\2\25\26\7\17\2\2\26"+
		"\27\b\2\1\2\27\31\3\2\2\2\30\24\3\2\2\2\31\34\3\2\2\2\32\30\3\2\2\2\32"+
		"\33\3\2\2\2\33\35\3\2\2\2\34\32\3\2\2\2\35\37\5\4\3\2\36 \7\17\2\2\37"+
		"\36\3\2\2\2\37 \3\2\2\2 !\3\2\2\2!\"\b\2\1\2\"\3\3\2\2\2#$\5\6\4\2$%\b"+
		"\3\1\2%*\3\2\2\2&\'\5\b\5\2\'(\b\3\1\2(*\3\2\2\2)#\3\2\2\2)&\3\2\2\2*"+
		"\5\3\2\2\2+,\7\20\2\2,-\7\16\2\2-.\5\b\5\2./\b\4\1\2/\7\3\2\2\2\60\61"+
		"\b\5\1\2\61\62\5\n\6\2\62\63\b\5\1\2\63;\3\2\2\2\64\65\5\f\7\2\65\66\b"+
		"\5\1\2\66;\3\2\2\2\678\5\16\b\289\b\5\1\29;\3\2\2\2:\60\3\2\2\2:\64\3"+
		"\2\2\2:\67\3\2\2\2;\\\3\2\2\2<=\f\t\2\2=>\7\t\2\2>?\5\b\5\n?@\b\5\1\2"+
		"@[\3\2\2\2AB\f\b\2\2BC\7\b\2\2CD\5\b\5\tDE\b\5\1\2E[\3\2\2\2FG\f\7\2\2"+
		"GH\7\6\2\2HI\5\b\5\bIJ\b\5\1\2J[\3\2\2\2KL\f\6\2\2LM\7\7\2\2MN\5\b\5\7"+
		"NO\b\5\1\2O[\3\2\2\2PQ\f\4\2\2QR\7\r\2\2RS\5\b\5\5ST\b\5\1\2T[\3\2\2\2"+
		"UV\f\3\2\2VW\7\f\2\2WX\5\b\5\4XY\b\5\1\2Y[\3\2\2\2Z<\3\2\2\2ZA\3\2\2\2"+
		"ZF\3\2\2\2ZK\3\2\2\2ZP\3\2\2\2ZU\3\2\2\2[^\3\2\2\2\\Z\3\2\2\2\\]\3\2\2"+
		"\2]\t\3\2\2\2^\\\3\2\2\2_`\7\6\2\2`a\5\f\7\2ab\b\6\1\2bp\3\2\2\2cd\7\6"+
		"\2\2de\5\16\b\2ef\b\6\1\2fp\3\2\2\2gh\7\7\2\2hi\5\f\7\2ij\b\6\1\2jp\3"+
		"\2\2\2kl\7\7\2\2lm\5\16\b\2mn\b\6\1\2np\3\2\2\2o_\3\2\2\2oc\3\2\2\2og"+
		"\3\2\2\2ok\3\2\2\2p\13\3\2\2\2qr\7\n\2\2rs\5\b\5\2st\7\13\2\2tu\b\7\1"+
		"\2u\r\3\2\2\2vw\7\20\2\2w\u0081\b\b\1\2xy\5\20\t\2yz\b\b\1\2z\u0081\3"+
		"\2\2\2{|\5\22\n\2|}\b\b\1\2}\u0081\3\2\2\2~\177\7\3\2\2\177\u0081\b\b"+
		"\1\2\u0080v\3\2\2\2\u0080x\3\2\2\2\u0080{\3\2\2\2\u0080~\3\2\2\2\u0081"+
		"\17\3\2\2\2\u0082\u0083\7\21\2\2\u0083\u0087\b\t\1\2\u0084\u0085\7\26"+
		"\2\2\u0085\u0087\b\t\1\2\u0086\u0082\3\2\2\2\u0086\u0084\3\2\2\2\u0087"+
		"\21\3\2\2\2\u0088\u0089\7\22\2\2\u0089\u008a\b\n\1\2\u008a\23\3\2\2\2"+
		"\13\32\37):Z\\o\u0080\u0086";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}