// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/armadialogcreator/arma/header\HeaderAntlr.g4 by ANTLR 4.7
package com.armadialogcreator.arma.header;
import com.armadialogcreator.arma.header.HeaderArrayItem;
import com.armadialogcreator.arma.header.HeaderAssignment;
import com.armadialogcreator.arma.header.HeaderClass;
import com.armadialogcreator.arma.header.HeaderFile;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
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
		Star=15, FSlash=16, LParen=17, RParen=18, BSlash=19, Identifier=20, Number=21, 
		Letter=22, LetterOrDigit=23, WhiteSpace=24, Comment=25, INTEGER_LITERAL=26, 
		DEC_LITERAL=27, HEX_LITERAL=28;
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
		"'[]'", "'''", "'\"'", "'+'", "'-'", "'*'", "'/'", "'('", "')'", "'\\'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Class", "Comma", "Colon", "Semicolon", "PlusEqual", "Equal", 
		"LBrace", "RBrace", "BacketPair", "Quote", "DQuote", "Plus", "Minus", 
		"Star", "FSlash", "LParen", "RParen", "BSlash", "Identifier", "Number", 
		"Letter", "LetterOrDigit", "WhiteSpace", "Comment", "INTEGER_LITERAL", 
		"DEC_LITERAL", "HEX_LITERAL"
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
		public HeaderFile file;
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
		public Root_classContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public Root_classContext(ParserRuleContext parent, int invokingState, HeaderFile file) {
			super(parent, invokingState);
			this.file = file;
		}
		@Override public int getRuleIndex() { return RULE_root_class; }
	}

	public final Root_classContext root_class(HeaderFile file) throws RecognitionException {
		Root_classContext _localctx = new Root_classContext(_ctx, getState(), file);
		enterRule(_localctx, 0, RULE_root_class);

		        ((Root_classContext)_localctx).nested =  new ArrayList<>();
		        ((Root_classContext)_localctx).assigns =  new ArrayList<>();
		        ((Root_classContext)_localctx).ast =  new AST.HeaderClassNode(_localctx.file, _localctx.assigns, _localctx.nested);
		    
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
				((Root_classContext)_localctx).help = header_class_helper(_localctx.ast, _localctx.nested, _localctx.assigns);
				}
				}
				setState(23);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class Header_classContext extends ParserRuleContext {
		public HeaderClass parentClass;
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
		public Header_classContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public Header_classContext(ParserRuleContext parent, int invokingState, HeaderClass parentClass) {
			super(parent, invokingState);
			this.parentClass = parentClass;
		}
		@Override public int getRuleIndex() { return RULE_header_class; }
	}

	public final Header_classContext header_class(HeaderClass parentClass) throws RecognitionException {
		Header_classContext _localctx = new Header_classContext(_ctx, getState(), parentClass);
		enterRule(_localctx, 2, RULE_header_class);

		        ((Header_classContext)_localctx).nested =  new ArrayList<>();
		        ((Header_classContext)_localctx).assigns =  new ArrayList<>();
		        ((Header_classContext)_localctx).extendText =  null;
		        ((Header_classContext)_localctx).ast =  new AST.HeaderClassNode(_localctx.parentClass, _localctx.assigns, _localctx.nested);
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			match(Class);
			setState(25);
			((Header_classContext)_localctx).cn = match(Identifier);
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Colon) {
				{
				setState(26);
				match(Colon);
				setState(27);
				((Header_classContext)_localctx).ex = match(Identifier);
				((Header_classContext)_localctx).extendText =  (((Header_classContext)_localctx).ex!=null?((Header_classContext)_localctx).ex.getText():null);
				}
			}

			setState(39);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBrace) {
				{
				setState(31);
				match(LBrace);
				setState(35);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==Class || _la==Identifier) {
					{
					{
					setState(32);
					header_class_helper(_localctx.ast, _localctx.nested, _localctx.assigns);
					}
					}
					setState(37);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(38);
				match(RBrace);
				}
			}

			setState(41);
			match(Semicolon);

			        _localctx.ast.setClassName((((Header_classContext)_localctx).cn!=null?((Header_classContext)_localctx).cn.getText():null));
			        _localctx.ast.setExtendClassName(_localctx.extendText);
			    
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
		public HeaderClass parentClass;
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
		public Header_class_helperContext(ParserRuleContext parent, int invokingState, HeaderClass parentClass, ArrayList<HeaderClass> nested, ArrayList<HeaderAssignment> assigns) {
			super(parent, invokingState);
			this.parentClass = parentClass;
			this.nested = nested;
			this.assigns = assigns;
		}
		@Override public int getRuleIndex() { return RULE_header_class_helper; }
	}

	public final Header_class_helperContext header_class_helper(HeaderClass parentClass,ArrayList<HeaderClass> nested,ArrayList<HeaderAssignment> assigns) throws RecognitionException {
		Header_class_helperContext _localctx = new Header_class_helperContext(_ctx, getState(), parentClass, nested, assigns);
		enterRule(_localctx, 4, RULE_header_class_helper);
		try {
			setState(53);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(44);
				((Header_class_helperContext)_localctx).c = header_class(_localctx.parentClass);
				 _localctx.nested.add(((Header_class_helperContext)_localctx).c.ast); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(47);
				((Header_class_helperContext)_localctx).a = assignment();
				 _localctx.assigns.add(((Header_class_helperContext)_localctx).a.ast); 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(50);
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
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_assignment);
		try {
			setState(65);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(55);
				((AssignmentContext)_localctx).varName = match(Identifier);
				setState(56);
				match(Equal);
				setState(57);
				match(Semicolon);
				 ((AssignmentContext)_localctx).ast =  new AST.HeaderAssignmentNode((((AssignmentContext)_localctx).varName!=null?((AssignmentContext)_localctx).varName.getText():null), null); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(59);
				((AssignmentContext)_localctx).varName = match(Identifier);
				setState(60);
				match(Equal);
				setState(61);
				((AssignmentContext)_localctx).val = value();
				setState(62);
				match(Semicolon);
				 ((AssignmentContext)_localctx).ast =  new AST.HeaderAssignmentNode((((AssignmentContext)_localctx).varName!=null?((AssignmentContext)_localctx).varName.getText():null), ((AssignmentContext)_localctx).val.ast); 
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
	}

	public final Arr_assignmentContext arr_assignment() throws RecognitionException {
		Arr_assignmentContext _localctx = new Arr_assignmentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_arr_assignment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			((Arr_assignmentContext)_localctx).varName = match(Identifier);
			setState(68);
			match(BacketPair);
			setState(69);
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
			setState(70);
			((Arr_assignmentContext)_localctx).val = array();
			setState(71);
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
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_array);
		 ((ArrayContext)_localctx).items =  new ArrayList<>(); 
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			match(LBrace);
			setState(83);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << String) | (1L << LBrace) | (1L << Plus) | (1L << Minus) | (1L << Star) | (1L << FSlash) | (1L << LParen) | (1L << RParen) | (1L << BSlash) | (1L << Identifier) | (1L << Number))) != 0)) {
				{
				setState(75);
				array_helper(_localctx.items);
				setState(80);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==Comma) {
					{
					{
					setState(76);
					match(Comma);
					setState(77);
					array_helper(_localctx.items);
					}
					}
					setState(82);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(85);
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
	}

	public final Array_helperContext array_helper(ArrayList<HeaderArrayItem> items) throws RecognitionException {
		Array_helperContext _localctx = new Array_helperContext(_ctx, getState(), items);
		enterRule(_localctx, 12, RULE_array_helper);
		try {
			setState(94);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case String:
			case Plus:
			case Minus:
			case Star:
			case FSlash:
			case LParen:
			case RParen:
			case BSlash:
			case Identifier:
			case Number:
				enterOuterAlt(_localctx, 1);
				{
				setState(88);
				((Array_helperContext)_localctx).v = value();
				 _localctx.items.add(new AST.HeaderArrayItemNode(((Array_helperContext)_localctx).v.ast)); 
				}
				break;
			case LBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(91);
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
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_value);
		try {
			setState(101);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				((ValueContext)_localctx).s = match(String);
				 ((ValueContext)_localctx).ast =  new AST.HeaderValueNode((((ValueContext)_localctx).s!=null?((ValueContext)_localctx).s.getText():null)); 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(98);
				((ValueContext)_localctx).eq = equation();
				 ((ValueContext)_localctx).ast =  new AST.HeaderValueNode((((ValueContext)_localctx).eq!=null?_input.getText(((ValueContext)_localctx).eq.start,((ValueContext)_localctx).eq.stop):null)); 
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
		public List<TerminalNode> BSlash() { return getTokens(HeaderAntlrParser.BSlash); }
		public TerminalNode BSlash(int i) {
			return getToken(HeaderAntlrParser.BSlash, i);
		}
		public List<TerminalNode> String() { return getTokens(HeaderAntlrParser.String); }
		public TerminalNode String(int i) {
			return getToken(HeaderAntlrParser.String, i);
		}
		public EquationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equation; }
	}

	public final EquationContext equation() throws RecognitionException {
		EquationContext _localctx = new EquationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_equation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(103);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << String) | (1L << Plus) | (1L << Minus) | (1L << Star) | (1L << FSlash) | (1L << LParen) | (1L << RParen) | (1L << BSlash) | (1L << Identifier) | (1L << Number))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(106); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << String) | (1L << Plus) | (1L << Minus) | (1L << Star) | (1L << FSlash) | (1L << LParen) | (1L << RParen) | (1L << BSlash) | (1L << Identifier) | (1L << Number))) != 0) );
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\36o\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\7\2\26"+
		"\n\2\f\2\16\2\31\13\2\3\3\3\3\3\3\3\3\3\3\5\3 \n\3\3\3\3\3\7\3$\n\3\f"+
		"\3\16\3\'\13\3\3\3\5\3*\n\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\5\48\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5D\n\5\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\7\7Q\n\7\f\7\16\7T\13\7\5\7V\n"+
		"\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\5\ba\n\b\3\t\3\t\3\t\3\t\3\t\5"+
		"\th\n\t\3\n\6\nk\n\n\r\n\16\nl\3\n\2\2\13\2\4\6\b\n\f\16\20\22\2\4\3\2"+
		"\b\t\4\2\3\3\17\27\2q\2\27\3\2\2\2\4\32\3\2\2\2\6\67\3\2\2\2\bC\3\2\2"+
		"\2\nE\3\2\2\2\fL\3\2\2\2\16`\3\2\2\2\20g\3\2\2\2\22j\3\2\2\2\24\26\5\6"+
		"\4\2\25\24\3\2\2\2\26\31\3\2\2\2\27\25\3\2\2\2\27\30\3\2\2\2\30\3\3\2"+
		"\2\2\31\27\3\2\2\2\32\33\7\4\2\2\33\37\7\26\2\2\34\35\7\6\2\2\35\36\7"+
		"\26\2\2\36 \b\3\1\2\37\34\3\2\2\2\37 \3\2\2\2 )\3\2\2\2!%\7\n\2\2\"$\5"+
		"\6\4\2#\"\3\2\2\2$\'\3\2\2\2%#\3\2\2\2%&\3\2\2\2&(\3\2\2\2\'%\3\2\2\2"+
		"(*\7\13\2\2)!\3\2\2\2)*\3\2\2\2*+\3\2\2\2+,\7\7\2\2,-\b\3\1\2-\5\3\2\2"+
		"\2./\5\4\3\2/\60\b\4\1\2\608\3\2\2\2\61\62\5\b\5\2\62\63\b\4\1\2\638\3"+
		"\2\2\2\64\65\5\n\6\2\65\66\b\4\1\2\668\3\2\2\2\67.\3\2\2\2\67\61\3\2\2"+
		"\2\67\64\3\2\2\28\7\3\2\2\29:\7\26\2\2:;\7\t\2\2;<\7\7\2\2<D\b\5\1\2="+
		">\7\26\2\2>?\7\t\2\2?@\5\20\t\2@A\7\7\2\2AB\b\5\1\2BD\3\2\2\2C9\3\2\2"+
		"\2C=\3\2\2\2D\t\3\2\2\2EF\7\26\2\2FG\7\f\2\2GH\t\2\2\2HI\5\f\7\2IJ\7\7"+
		"\2\2JK\b\6\1\2K\13\3\2\2\2LU\7\n\2\2MR\5\16\b\2NO\7\5\2\2OQ\5\16\b\2P"+
		"N\3\2\2\2QT\3\2\2\2RP\3\2\2\2RS\3\2\2\2SV\3\2\2\2TR\3\2\2\2UM\3\2\2\2"+
		"UV\3\2\2\2VW\3\2\2\2WX\7\13\2\2XY\b\7\1\2Y\r\3\2\2\2Z[\5\20\t\2[\\\b\b"+
		"\1\2\\a\3\2\2\2]^\5\f\7\2^_\b\b\1\2_a\3\2\2\2`Z\3\2\2\2`]\3\2\2\2a\17"+
		"\3\2\2\2bc\7\3\2\2ch\b\t\1\2de\5\22\n\2ef\b\t\1\2fh\3\2\2\2gb\3\2\2\2"+
		"gd\3\2\2\2h\21\3\2\2\2ik\t\3\2\2ji\3\2\2\2kl\3\2\2\2lj\3\2\2\2lm\3\2\2"+
		"\2m\23\3\2\2\2\r\27\37%)\67CRU`gl";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
