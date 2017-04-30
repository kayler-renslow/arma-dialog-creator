// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/kaylerrenslow/armaDialogCreator/arma/header\HeaderAntlr.g4 by ANTLR 4.7
package com.kaylerrenslow.armaDialogCreator.arma.header;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class HeaderAntlrParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		String=1, Class=2, Comma=3, Colon=4, Semicolon=5, PlusEqual=6, Equal=7, 
		LBrace=8, RBrace=9, BacketPair=10, Quote=11, DQuote=12, Plus=13, Minus=14, 
		Star=15, FSlash=16, LParen=17, RParen=18, Identifier=19, Number=20, Letter=21, 
		LetterOrDigit=22, WhiteSpace=23, Comment=24, INTEGER_LITERAL=25, DEC_LITERAL=26, 
		HEX_LITERAL=27;
	public static final int
		RULE_root_class = 0, RULE_header_class = 1, RULE_header_class_helper = 2, 
		RULE_assignment = 3, RULE_arr_assignment = 4, RULE_array = 5, RULE_array_helper = 6, 
		RULE_value = 7, RULE_equation = 8;
	public static final String[] ruleNames = {
		"root_class", "header_class", "header_class_helper", "assignment", "arr_assignment", 
		"array", "array_helper", "value", "equation"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'class'", "','", "':'", "';'", "'+='", "'='", "'{'", "'}'", 
		"'[]'", "'''", "'\"'", "'+'", "'-'", "'*'", "'/'", "'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Class", "Comma", "Colon", "Semicolon", "PlusEqual", "Equal", 
		"LBrace", "RBrace", "BacketPair", "Quote", "DQuote", "Plus", "Minus", 
		"Star", "FSlash", "LParen", "RParen", "Identifier", "Number", "Letter", 
		"LetterOrDigit", "WhiteSpace", "Comment", "INTEGER_LITERAL", "DEC_LITERAL", 
		"HEX_LITERAL"
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
	public String getGrammarFileName() { return "HeaderAntlr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public HeaderAntlrParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class Root_classContext extends ParserRuleContext {
		public AST.HeaderClassNode ast;
		public ArrayList<HeaderClass> nested;
		public ArrayList<HeaderAssignment> assigns;
		public String extendText;
		public Header_class_helperContext help;
		public List<Header_class_helperContext> header_class_helper() {
			return getRuleContexts(Header_class_helperContext.class);
		}
		public Header_class_helperContext header_class_helper(int i) {
			return getRuleContext(Header_class_helperContext.class,i);
		}
		public Root_classContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root_class; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).enterRoot_class(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).exitRoot_class(this);
		}
	}

	public final Root_classContext root_class() throws RecognitionException {
		Root_classContext _localctx = new Root_classContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root_class);

		        ((Root_classContext)_localctx).nested =  new ArrayList<>();
		        ((Root_classContext)_localctx).assigns =  new ArrayList<>();
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Class || _la==Identifier) {
				{
				{
				setState(18);
				((Root_classContext)_localctx).help = header_class_helper(_localctx.nested, _localctx.assigns);
				}
				}
				setState(23);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			 ((Root_classContext)_localctx).ast =  new AST.HeaderClassNode("-root class", null, _localctx.assigns, _localctx.nested); 
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

	public static class Header_classContext extends ParserRuleContext {
		public AST.HeaderClassNode ast;
		public ArrayList<HeaderClass> nested;
		public ArrayList<HeaderAssignment> assigns;
		public String extendText;
		public Token cn;
		public Token ex;
		public TerminalNode Class() { return getToken(HeaderAntlrParser.Class, 0); }
		public TerminalNode Semicolon() { return getToken(HeaderAntlrParser.Semicolon, 0); }
		public List<TerminalNode> Identifier() { return getTokens(HeaderAntlrParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(HeaderAntlrParser.Identifier, i);
		}
		public TerminalNode Colon() { return getToken(HeaderAntlrParser.Colon, 0); }
		public TerminalNode LBrace() { return getToken(HeaderAntlrParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(HeaderAntlrParser.RBrace, 0); }
		public List<Header_class_helperContext> header_class_helper() {
			return getRuleContexts(Header_class_helperContext.class);
		}
		public Header_class_helperContext header_class_helper(int i) {
			return getRuleContext(Header_class_helperContext.class,i);
		}
		public Header_classContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header_class; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).enterHeader_class(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).exitHeader_class(this);
		}
	}

	public final Header_classContext header_class() throws RecognitionException {
		Header_classContext _localctx = new Header_classContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_header_class);

		        ((Header_classContext)_localctx).nested =  new ArrayList<>();
		        ((Header_classContext)_localctx).assigns =  new ArrayList<>();
		        ((Header_classContext)_localctx).extendText =  null;
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			match(Class);
			setState(27);
			((Header_classContext)_localctx).cn = match(Identifier);
			setState(31);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Colon) {
				{
				setState(28);
				match(Colon);
				setState(29);
				((Header_classContext)_localctx).ex = match(Identifier);
				((Header_classContext)_localctx).extendText =  (((Header_classContext)_localctx).ex!=null?((Header_classContext)_localctx).ex.getText():null);
				}
			}

			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBrace) {
				{
				setState(33);
				match(LBrace);
				setState(37);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==Class || _la==Identifier) {
					{
					{
					setState(34);
					header_class_helper(_localctx.nested, _localctx.assigns);
					}
					}
					setState(39);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(40);
				match(RBrace);
				}
			}

			setState(43);
			match(Semicolon);

			        ((Header_classContext)_localctx).ast =  new AST.HeaderClassNode((((Header_classContext)_localctx).cn!=null?((Header_classContext)_localctx).cn.getText():null), _localctx.extendText, _localctx.assigns, _localctx.nested);
			    
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

	public static class Header_class_helperContext extends ParserRuleContext {
		public ArrayList<HeaderClass> nested;
		public ArrayList<HeaderAssignment> assigns;
		public Header_classContext c;
		public AssignmentContext a;
		public Arr_assignmentContext aa;
		public Header_classContext header_class() {
			return getRuleContext(Header_classContext.class,0);
		}
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public Arr_assignmentContext arr_assignment() {
			return getRuleContext(Arr_assignmentContext.class,0);
		}
		public Header_class_helperContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public Header_class_helperContext(ParserRuleContext parent, int invokingState, ArrayList<HeaderClass> nested, ArrayList<HeaderAssignment> assigns) {
			super(parent, invokingState);
			this.nested = nested;
			this.assigns = assigns;
		}
		@Override public int getRuleIndex() { return RULE_header_class_helper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).enterHeader_class_helper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).exitHeader_class_helper(this);
		}
	}

	public final Header_class_helperContext header_class_helper(ArrayList<HeaderClass> nested,ArrayList<HeaderAssignment> assigns) throws RecognitionException {
		Header_class_helperContext _localctx = new Header_class_helperContext(_ctx, getState(), nested, assigns);
		enterRule(_localctx, 4, RULE_header_class_helper);
		try {
			setState(55);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(46);
				((Header_class_helperContext)_localctx).c = header_class();
				 _localctx.nested.add(((Header_class_helperContext)_localctx).c.ast); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(49);
				((Header_class_helperContext)_localctx).a = assignment();
				 _localctx.assigns.add(((Header_class_helperContext)_localctx).a.ast); 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				((Header_class_helperContext)_localctx).aa = arr_assignment();
				 _localctx.assigns.add(((Header_class_helperContext)_localctx).aa.ast); 
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
		public AST.HeaderAssignmentNode ast;
		public Token varName;
		public ValueContext val;
		public TerminalNode Equal() { return getToken(HeaderAntlrParser.Equal, 0); }
		public TerminalNode Semicolon() { return getToken(HeaderAntlrParser.Semicolon, 0); }
		public TerminalNode Identifier() { return getToken(HeaderAntlrParser.Identifier, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).exitAssignment(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			((AssignmentContext)_localctx).varName = match(Identifier);
			setState(58);
			match(Equal);
			setState(59);
			((AssignmentContext)_localctx).val = value();
			setState(60);
			match(Semicolon);
			 ((AssignmentContext)_localctx).ast =  new AST.HeaderAssignmentNode((((AssignmentContext)_localctx).varName!=null?((AssignmentContext)_localctx).varName.getText():null), ((AssignmentContext)_localctx).val.ast); 
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

	public static class Arr_assignmentContext extends ParserRuleContext {
		public AST.HeaderArrayAssignmentNode ast;
		public Token varName;
		public Token eq;
		public ArrayContext val;
		public TerminalNode BacketPair() { return getToken(HeaderAntlrParser.BacketPair, 0); }
		public TerminalNode Semicolon() { return getToken(HeaderAntlrParser.Semicolon, 0); }
		public TerminalNode Identifier() { return getToken(HeaderAntlrParser.Identifier, 0); }
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public TerminalNode PlusEqual() { return getToken(HeaderAntlrParser.PlusEqual, 0); }
		public TerminalNode Equal() { return getToken(HeaderAntlrParser.Equal, 0); }
		public Arr_assignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arr_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).enterArr_assignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).exitArr_assignment(this);
		}
	}

	public final Arr_assignmentContext arr_assignment() throws RecognitionException {
		Arr_assignmentContext _localctx = new Arr_assignmentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_arr_assignment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			((Arr_assignmentContext)_localctx).varName = match(Identifier);
			setState(64);
			match(BacketPair);
			setState(65);
			((Arr_assignmentContext)_localctx).eq = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==PlusEqual || _la==Equal) ) {
				((Arr_assignmentContext)_localctx).eq = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(66);
			((Arr_assignmentContext)_localctx).val = array();
			setState(67);
			match(Semicolon);
			 ((Arr_assignmentContext)_localctx).ast =  new AST.HeaderArrayAssignmentNode((((Arr_assignmentContext)_localctx).varName!=null?((Arr_assignmentContext)_localctx).varName.getText():null), ((Arr_assignmentContext)_localctx).val.ast, (((Arr_assignmentContext)_localctx).eq!=null?((Arr_assignmentContext)_localctx).eq.getText():null).equals("+=")); 
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
		public AST.HeaderArrayNode ast;
		public ArrayList<HeaderArrayItem> items;
		public TerminalNode LBrace() { return getToken(HeaderAntlrParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(HeaderAntlrParser.RBrace, 0); }
		public List<Array_helperContext> array_helper() {
			return getRuleContexts(Array_helperContext.class);
		}
		public Array_helperContext array_helper(int i) {
			return getRuleContext(Array_helperContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(HeaderAntlrParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(HeaderAntlrParser.Comma, i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).exitArray(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_array);
		 ((ArrayContext)_localctx).items =  new ArrayList<>(); 
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			match(LBrace);
			setState(79);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << String) | (1L << LBrace) | (1L << Plus) | (1L << Minus) | (1L << Star) | (1L << FSlash) | (1L << LParen) | (1L << RParen) | (1L << Identifier) | (1L << Number))) != 0)) {
				{
				setState(71);
				array_helper(_localctx.items);
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==Comma) {
					{
					{
					setState(72);
					match(Comma);
					setState(73);
					array_helper(_localctx.items);
					}
					}
					setState(78);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(81);
			match(RBrace);
			 ((ArrayContext)_localctx).ast =  new AST.HeaderArrayNode(_localctx.items); 
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

	public static class Array_helperContext extends ParserRuleContext {
		public ArrayList<HeaderArrayItem> items;
		public ValueContext v;
		public ArrayContext a;
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public Array_helperContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public Array_helperContext(ParserRuleContext parent, int invokingState, ArrayList<HeaderArrayItem> items) {
			super(parent, invokingState);
			this.items = items;
		}
		@Override public int getRuleIndex() { return RULE_array_helper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).enterArray_helper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).exitArray_helper(this);
		}
	}

	public final Array_helperContext array_helper(ArrayList<HeaderArrayItem> items) throws RecognitionException {
		Array_helperContext _localctx = new Array_helperContext(_ctx, getState(), items);
		enterRule(_localctx, 12, RULE_array_helper);
		try {
			setState(90);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case String:
			case Plus:
			case Minus:
			case Star:
			case FSlash:
			case LParen:
			case RParen:
			case Identifier:
			case Number:
				enterOuterAlt(_localctx, 1);
				{
				setState(84);
				((Array_helperContext)_localctx).v = value();
				 _localctx.items.add(new AST.HeaderArrayItemNode(((Array_helperContext)_localctx).v.ast)); 
				}
				break;
			case LBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(87);
				((Array_helperContext)_localctx).a = array();
				 _localctx.items.add(((Array_helperContext)_localctx).a.ast); 
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

	public static class ValueContext extends ParserRuleContext {
		public AST.HeaderValueNode ast;
		public Token s;
		public EquationContext eq;
		public TerminalNode String() { return getToken(HeaderAntlrParser.String, 0); }
		public EquationContext equation() {
			return getRuleContext(EquationContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_value);
		try {
			setState(97);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case String:
				enterOuterAlt(_localctx, 1);
				{
				setState(92);
				((ValueContext)_localctx).s = match(String);
				 ((ValueContext)_localctx).ast =  new AST.HeaderValueNode((((ValueContext)_localctx).s!=null?((ValueContext)_localctx).s.getText():null)); 
				}
				break;
			case Plus:
			case Minus:
			case Star:
			case FSlash:
			case LParen:
			case RParen:
			case Identifier:
			case Number:
				enterOuterAlt(_localctx, 2);
				{
				setState(94);
				((ValueContext)_localctx).eq = equation();
				 ((ValueContext)_localctx).ast =  new AST.HeaderValueNode((((ValueContext)_localctx).eq!=null?_input.getText(((ValueContext)_localctx).eq.start,((ValueContext)_localctx).eq.stop):null)); 
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

	public static class EquationContext extends ParserRuleContext {
		public List<TerminalNode> Plus() { return getTokens(HeaderAntlrParser.Plus); }
		public TerminalNode Plus(int i) {
			return getToken(HeaderAntlrParser.Plus, i);
		}
		public List<TerminalNode> Minus() { return getTokens(HeaderAntlrParser.Minus); }
		public TerminalNode Minus(int i) {
			return getToken(HeaderAntlrParser.Minus, i);
		}
		public List<TerminalNode> Star() { return getTokens(HeaderAntlrParser.Star); }
		public TerminalNode Star(int i) {
			return getToken(HeaderAntlrParser.Star, i);
		}
		public List<TerminalNode> FSlash() { return getTokens(HeaderAntlrParser.FSlash); }
		public TerminalNode FSlash(int i) {
			return getToken(HeaderAntlrParser.FSlash, i);
		}
		public List<TerminalNode> LParen() { return getTokens(HeaderAntlrParser.LParen); }
		public TerminalNode LParen(int i) {
			return getToken(HeaderAntlrParser.LParen, i);
		}
		public List<TerminalNode> RParen() { return getTokens(HeaderAntlrParser.RParen); }
		public TerminalNode RParen(int i) {
			return getToken(HeaderAntlrParser.RParen, i);
		}
		public List<TerminalNode> Number() { return getTokens(HeaderAntlrParser.Number); }
		public TerminalNode Number(int i) {
			return getToken(HeaderAntlrParser.Number, i);
		}
		public List<TerminalNode> Identifier() { return getTokens(HeaderAntlrParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(HeaderAntlrParser.Identifier, i);
		}
		public EquationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).enterEquation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof HeaderAntlrListener ) ((HeaderAntlrListener)listener).exitEquation(this);
		}
	}

	public final EquationContext equation() throws RecognitionException {
		EquationContext _localctx = new EquationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_equation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(99);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Plus) | (1L << Minus) | (1L << Star) | (1L << FSlash) | (1L << LParen) | (1L << RParen) | (1L << Identifier) | (1L << Number))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(102); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Plus) | (1L << Minus) | (1L << Star) | (1L << FSlash) | (1L << LParen) | (1L << RParen) | (1L << Identifier) | (1L << Number))) != 0) );
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\35k\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\7\2\26"+
		"\n\2\f\2\16\2\31\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3\"\n\3\3\3\3\3\7"+
		"\3&\n\3\f\3\16\3)\13\3\3\3\5\3,\n\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\5\4:\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\7\3\7\3\7\3\7\7\7M\n\7\f\7\16\7P\13\7\5\7R\n\7\3\7\3\7\3\7\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\5\b]\n\b\3\t\3\t\3\t\3\t\3\t\5\td\n\t\3\n\6\ng"+
		"\n\n\r\n\16\nh\3\n\2\2\13\2\4\6\b\n\f\16\20\22\2\4\3\2\b\t\3\2\17\26\2"+
		"l\2\27\3\2\2\2\4\34\3\2\2\2\69\3\2\2\2\b;\3\2\2\2\nA\3\2\2\2\fH\3\2\2"+
		"\2\16\\\3\2\2\2\20c\3\2\2\2\22f\3\2\2\2\24\26\5\6\4\2\25\24\3\2\2\2\26"+
		"\31\3\2\2\2\27\25\3\2\2\2\27\30\3\2\2\2\30\32\3\2\2\2\31\27\3\2\2\2\32"+
		"\33\b\2\1\2\33\3\3\2\2\2\34\35\7\4\2\2\35!\7\25\2\2\36\37\7\6\2\2\37 "+
		"\7\25\2\2 \"\b\3\1\2!\36\3\2\2\2!\"\3\2\2\2\"+\3\2\2\2#\'\7\n\2\2$&\5"+
		"\6\4\2%$\3\2\2\2&)\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(*\3\2\2\2)\'\3\2\2\2"+
		"*,\7\13\2\2+#\3\2\2\2+,\3\2\2\2,-\3\2\2\2-.\7\7\2\2./\b\3\1\2/\5\3\2\2"+
		"\2\60\61\5\4\3\2\61\62\b\4\1\2\62:\3\2\2\2\63\64\5\b\5\2\64\65\b\4\1\2"+
		"\65:\3\2\2\2\66\67\5\n\6\2\678\b\4\1\28:\3\2\2\29\60\3\2\2\29\63\3\2\2"+
		"\29\66\3\2\2\2:\7\3\2\2\2;<\7\25\2\2<=\7\t\2\2=>\5\20\t\2>?\7\7\2\2?@"+
		"\b\5\1\2@\t\3\2\2\2AB\7\25\2\2BC\7\f\2\2CD\t\2\2\2DE\5\f\7\2EF\7\7\2\2"+
		"FG\b\6\1\2G\13\3\2\2\2HQ\7\n\2\2IN\5\16\b\2JK\7\5\2\2KM\5\16\b\2LJ\3\2"+
		"\2\2MP\3\2\2\2NL\3\2\2\2NO\3\2\2\2OR\3\2\2\2PN\3\2\2\2QI\3\2\2\2QR\3\2"+
		"\2\2RS\3\2\2\2ST\7\13\2\2TU\b\7\1\2U\r\3\2\2\2VW\5\20\t\2WX\b\b\1\2X]"+
		"\3\2\2\2YZ\5\f\7\2Z[\b\b\1\2[]\3\2\2\2\\V\3\2\2\2\\Y\3\2\2\2]\17\3\2\2"+
		"\2^_\7\3\2\2_d\b\t\1\2`a\5\22\n\2ab\b\t\1\2bd\3\2\2\2c^\3\2\2\2c`\3\2"+
		"\2\2d\21\3\2\2\2eg\t\3\2\2fe\3\2\2\2gh\3\2\2\2hf\3\2\2\2hi\3\2\2\2i\23"+
		"\3\2\2\2\f\27!\'+9NQ\\ch";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}