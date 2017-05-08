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
		RParen=9, Min=10, Max=11, Identifier=12, IntegerLiteral=13, FloatLiteral=14, 
		Digits=15, DecSignificand=16, DecExponent=17, HexLiteral=18, HexDigits=19, 
		Letter=20, LetterOrDigit=21, WhiteSpace=22;
	public static final int
		RULE_expression = 0, RULE_unary_expression = 1, RULE_paren_expression = 2, 
		RULE_literal_expression = 3, RULE_int_value = 4, RULE_float_value = 5;
	public static final String[] ruleNames = {
		"expression", "unary_expression", "paren_expression", "literal_expression", 
		"int_value", "float_value"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'''", "'\"'", "'+'", "'-'", "'/'", "'*'", "'('", "')'", "'min'", 
		"'max'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Quote", "DQuote", "Plus", "Minus", "FSlash", "Star", 
		"LParen", "RParen", "Min", "Max", "Identifier", "IntegerLiteral", "FloatLiteral", 
		"Digits", "DecSignificand", "DecExponent", "HexLiteral", "HexDigits", 
		"Letter", "LetterOrDigit", "WhiteSpace"
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
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Plus:
			case Minus:
				{
				setState(13);
				((ExpressionContext)_localctx).lu = unary_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).lu.ast;
				}
				break;
			case LParen:
				{
				setState(16);
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
				setState(19);
				((ExpressionContext)_localctx).ll = literal_expression();
				((ExpressionContext)_localctx).ast =  ((ExpressionContext)_localctx).ll.ast;
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(56);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(54);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.ls = _prevctx;
						_localctx.ls = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(24);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(25);
						match(Star);
						setState(26);
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
						setState(29);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(30);
						match(FSlash);
						setState(31);
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
						setState(34);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(35);
						match(Plus);
						setState(36);
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
						setState(39);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(40);
						match(Minus);
						setState(41);
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
						setState(44);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(45);
						match(Max);
						setState(46);
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
						setState(49);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(50);
						match(Min);
						setState(51);
						((ExpressionContext)_localctx).rmin = expression(2);
						((ExpressionContext)_localctx).ast =  new AST.MinExpr(((ExpressionContext)_localctx).lmin.ast, ((ExpressionContext)_localctx).rmin.ast);
						}
						break;
					}
					} 
				}
				setState(58);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
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
		enterRule(_localctx, 2, RULE_unary_expression);
		try {
			setState(75);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(59);
				match(Plus);
				setState(60);
				((Unary_expressionContext)_localctx).ep = paren_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(true, ((Unary_expressionContext)_localctx).ep.ast);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(63);
				match(Plus);
				setState(64);
				((Unary_expressionContext)_localctx).ep1 = literal_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(true, ((Unary_expressionContext)_localctx).ep1.ast);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(67);
				match(Minus);
				setState(68);
				((Unary_expressionContext)_localctx).em = paren_expression();
				((Unary_expressionContext)_localctx).ast =  new AST.UnaryExpr(false, ((Unary_expressionContext)_localctx).em.ast);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(71);
				match(Minus);
				setState(72);
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
		enterRule(_localctx, 4, RULE_paren_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			match(LParen);
			setState(78);
			((Paren_expressionContext)_localctx).e = expression(0);
			setState(79);
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
		enterRule(_localctx, 6, RULE_literal_expression);
		try {
			setState(92);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(82);
				((Literal_expressionContext)_localctx).id = match(Identifier);
				((Literal_expressionContext)_localctx).ast =  new AST.IdentifierExpr((((Literal_expressionContext)_localctx).id!=null?((Literal_expressionContext)_localctx).id.getText():null));
				}
				break;
			case IntegerLiteral:
			case HexLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(84);
				((Literal_expressionContext)_localctx).i = int_value();
				((Literal_expressionContext)_localctx).ast =  new AST.IntegerExpr(((Literal_expressionContext)_localctx).i.i);
				}
				break;
			case FloatLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(87);
				((Literal_expressionContext)_localctx).f = float_value();
				((Literal_expressionContext)_localctx).ast =  new AST.FloatExpr(((Literal_expressionContext)_localctx).f.d);
				}
				break;
			case String:
				enterOuterAlt(_localctx, 4);
				{
				setState(90);
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
		enterRule(_localctx, 8, RULE_int_value);
		try {
			setState(98);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(94);
				((Int_valueContext)_localctx).il = match(IntegerLiteral);
				((Int_valueContext)_localctx).i =  new Integer((((Int_valueContext)_localctx).il!=null?((Int_valueContext)_localctx).il.getText():null));
				}
				break;
			case HexLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(96);
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
		enterRule(_localctx, 10, RULE_float_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
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
		case 0:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\30j\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\5\2\31\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2"+
		"9\n\2\f\2\16\2<\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3N\n\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\5\5_\n\5\3\6\3\6\3\6\3\6\5\6e\n\6\3\7\3\7\3\7\3\7\2\3\2"+
		"\b\2\4\6\b\n\f\2\2\2r\2\30\3\2\2\2\4M\3\2\2\2\6O\3\2\2\2\b^\3\2\2\2\n"+
		"d\3\2\2\2\ff\3\2\2\2\16\17\b\2\1\2\17\20\5\4\3\2\20\21\b\2\1\2\21\31\3"+
		"\2\2\2\22\23\5\6\4\2\23\24\b\2\1\2\24\31\3\2\2\2\25\26\5\b\5\2\26\27\b"+
		"\2\1\2\27\31\3\2\2\2\30\16\3\2\2\2\30\22\3\2\2\2\30\25\3\2\2\2\31:\3\2"+
		"\2\2\32\33\f\t\2\2\33\34\7\t\2\2\34\35\5\2\2\n\35\36\b\2\1\2\369\3\2\2"+
		"\2\37 \f\b\2\2 !\7\b\2\2!\"\5\2\2\t\"#\b\2\1\2#9\3\2\2\2$%\f\7\2\2%&\7"+
		"\6\2\2&\'\5\2\2\b\'(\b\2\1\2(9\3\2\2\2)*\f\6\2\2*+\7\7\2\2+,\5\2\2\7,"+
		"-\b\2\1\2-9\3\2\2\2./\f\4\2\2/\60\7\r\2\2\60\61\5\2\2\5\61\62\b\2\1\2"+
		"\629\3\2\2\2\63\64\f\3\2\2\64\65\7\f\2\2\65\66\5\2\2\4\66\67\b\2\1\2\67"+
		"9\3\2\2\28\32\3\2\2\28\37\3\2\2\28$\3\2\2\28)\3\2\2\28.\3\2\2\28\63\3"+
		"\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2\2;\3\3\2\2\2<:\3\2\2\2=>\7\6\2\2>?"+
		"\5\6\4\2?@\b\3\1\2@N\3\2\2\2AB\7\6\2\2BC\5\b\5\2CD\b\3\1\2DN\3\2\2\2E"+
		"F\7\7\2\2FG\5\6\4\2GH\b\3\1\2HN\3\2\2\2IJ\7\7\2\2JK\5\b\5\2KL\b\3\1\2"+
		"LN\3\2\2\2M=\3\2\2\2MA\3\2\2\2ME\3\2\2\2MI\3\2\2\2N\5\3\2\2\2OP\7\n\2"+
		"\2PQ\5\2\2\2QR\7\13\2\2RS\b\4\1\2S\7\3\2\2\2TU\7\16\2\2U_\b\5\1\2VW\5"+
		"\n\6\2WX\b\5\1\2X_\3\2\2\2YZ\5\f\7\2Z[\b\5\1\2[_\3\2\2\2\\]\7\3\2\2]_"+
		"\b\5\1\2^T\3\2\2\2^V\3\2\2\2^Y\3\2\2\2^\\\3\2\2\2_\t\3\2\2\2`a\7\17\2"+
		"\2ae\b\6\1\2bc\7\24\2\2ce\b\6\1\2d`\3\2\2\2db\3\2\2\2e\13\3\2\2\2fg\7"+
		"\20\2\2gh\b\7\1\2h\r\3\2\2\2\b\308:M^d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}