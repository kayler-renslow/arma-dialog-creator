// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/kaylerrenslow/armaDialogCreator/expression\Expression.g4 by ANTLR 4.7
package com.kaylerrenslow.armaDialogCreator.expression;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		String=1, Quote=2, DQuote=3, LCurly=4, RCurly=5, LBracket=6, RBracket=7, 
		Plus=8, Minus=9, FSlash=10, Perc=11, Caret=12, Star=13, LParen=14, RParen=15, 
		Comma=16, Min=17, Max=18, If=19, Then=20, Else=21, ExitWith=22, Select=23, 
		Count=24, For=25, From=26, To=27, Step=28, Do=29, Str=30, EqEq=31, NotEq=32, 
		Lt=33, LtEq=34, Gt=35, GtEq=36, Equal=37, Semicolon=38, Or=39, And=40, 
		SafeZoneX=41, SafeZoneY=42, SafeZoneW=43, SafeZoneH=44, SafeZoneXAbs=45, 
		SafeZoneWAbs=46, GetResolution=47, Identifier=48, IntegerLiteral=49, FloatLiteral=50, 
		Digits=51, DecSignificand=52, DecExponent=53, HexLiteral=54, HexDigits=55, 
		Letter=56, LetterOrDigit=57, WhiteSpace=58, Comment=59;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"String", "Quote", "DQuote", "LCurly", "RCurly", "LBracket", "RBracket", 
		"Plus", "Minus", "FSlash", "Perc", "Caret", "Star", "LParen", "RParen", 
		"Comma", "Min", "Max", "If", "Then", "Else", "ExitWith", "Select", "Count", 
		"For", "From", "To", "Step", "Do", "Str", "EqEq", "NotEq", "Lt", "LtEq", 
		"Gt", "GtEq", "Equal", "Semicolon", "Or", "And", "SafeZoneX", "SafeZoneY", 
		"SafeZoneW", "SafeZoneH", "SafeZoneXAbs", "SafeZoneWAbs", "GetResolution", 
		"Identifier", "IntegerLiteral", "FloatLiteral", "Digits", "DecSignificand", 
		"DecExponent", "HexLiteral", "HexDigits", "Letter", "LetterOrDigit", "WhiteSpace", 
		"Comment", "DIGIT", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
		"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", 
		"Y", "Z"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'''", "'\"'", "'{'", "'}'", "'['", "']'", "'+'", "'-'", "'/'", 
		"'%'", "'^'", "'*'", "'('", "')'", "','", null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, "'=='", "'!='", 
		"'<'", "'<='", "'>'", "'>='", "'='", "';'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Quote", "DQuote", "LCurly", "RCurly", "LBracket", "RBracket", 
		"Plus", "Minus", "FSlash", "Perc", "Caret", "Star", "LParen", "RParen", 
		"Comma", "Min", "Max", "If", "Then", "Else", "ExitWith", "Select", "Count", 
		"For", "From", "To", "Step", "Do", "Str", "EqEq", "NotEq", "Lt", "LtEq", 
		"Gt", "GtEq", "Equal", "Semicolon", "Or", "And", "SafeZoneX", "SafeZoneY", 
		"SafeZoneW", "SafeZoneH", "SafeZoneXAbs", "SafeZoneWAbs", "GetResolution", 
		"Identifier", "IntegerLiteral", "FloatLiteral", "Digits", "DecSignificand", 
		"DecExponent", "HexLiteral", "HexDigits", "Letter", "LetterOrDigit", "WhiteSpace", 
		"Comment"
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


	public ExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Expression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 55:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 56:
			return LetterOrDigit_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean Letter_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return Character.isJavaIdentifierStart(_input.LA(-1));
		case 1:
			return Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}
	private boolean LetterOrDigit_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return Character.isJavaIdentifierPart(_input.LA(-1));
		case 3:
			return Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2=\u0235\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\3\2\3\2\7\2\u00b2\n\2\f\2\16\2\u00b5\13\2\3\2\3\2\6"+
		"\2\u00b9\n\2\r\2\16\2\u00ba\3\2\3\2\7\2\u00bf\n\2\f\2\16\2\u00c2\13\2"+
		"\3\2\3\2\6\2\u00c6\n\2\r\2\16\2\u00c7\5\2\u00ca\n\2\3\3\3\3\3\4\3\4\3"+
		"\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3"+
		"\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3"+
		"\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3"+
		"\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3"+
		"\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\36\3"+
		"\36\3\36\3\37\3\37\3\37\3\37\3 \3 \3 \3!\3!\3!\3\"\3\"\3#\3#\3#\3$\3$"+
		"\3%\3%\3%\3&\3&\3\'\3\'\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3"+
		"*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3,\3,\3,\3-\3"+
		"-\3-\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3/\3"+
		"/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\61\3\61\7\61\u019a\n\61\f\61\16"+
		"\61\u019d\13\61\3\62\3\62\3\63\3\63\5\63\u01a3\n\63\3\64\6\64\u01a6\n"+
		"\64\r\64\16\64\u01a7\3\65\3\65\3\65\3\65\3\65\6\65\u01af\n\65\r\65\16"+
		"\65\u01b0\5\65\u01b3\n\65\3\66\3\66\5\66\u01b7\n\66\3\66\3\66\5\66\u01bb"+
		"\n\66\3\66\7\66\u01be\n\66\f\66\16\66\u01c1\13\66\3\67\3\67\3\67\7\67"+
		"\u01c6\n\67\f\67\16\67\u01c9\13\67\3\67\3\67\38\68\u01ce\n8\r8\168\u01cf"+
		"\39\39\39\39\39\39\59\u01d8\n9\3:\3:\3:\3:\3:\3:\5:\u01e0\n:\3;\3;\3;"+
		"\5;\u01e5\n;\3;\3;\3<\3<\3<\3<\6<\u01ed\n<\r<\16<\u01ee\3<\3<\3<\3<\7"+
		"<\u01f5\n<\f<\16<\u01f8\13<\3<\3<\5<\u01fc\n<\3<\3<\3=\3=\3>\3>\3?\3?"+
		"\3@\3@\3A\3A\3B\3B\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3I\3I\3J\3J\3K"+
		"\3K\3L\3L\3M\3M\3N\3N\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U\3V\3V"+
		"\3W\3W\3\u01f6\2X\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65"+
		"\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64"+
		"g\65i\66k\67m8o9q:s;u<w=y\2{\2}\2\177\2\u0081\2\u0083\2\u0085\2\u0087"+
		"\2\u0089\2\u008b\2\u008d\2\u008f\2\u0091\2\u0093\2\u0095\2\u0097\2\u0099"+
		"\2\u009b\2\u009d\2\u009f\2\u00a1\2\u00a3\2\u00a5\2\u00a7\2\u00a9\2\u00ab"+
		"\2\u00ad\2\3\2\'\3\2))\3\2$$\4\2GGgg\4\2--//\4\2ZZzz\5\2\62;CHch\6\2&"+
		"&C\\aac|\4\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&"+
		"&\62;C\\aac|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\4\2CCcc\4\2DDdd\4\2EEee"+
		"\4\2FFff\4\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4\2O"+
		"Ooo\4\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4"+
		"\2XXxx\4\2YYyy\4\2[[{{\4\2\\\\||\2\u0230\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2"+
		"\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2"+
		"A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3"+
		"\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2"+
		"\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2"+
		"g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3"+
		"\2\2\2\2u\3\2\2\2\2w\3\2\2\2\3\u00c9\3\2\2\2\5\u00cb\3\2\2\2\7\u00cd\3"+
		"\2\2\2\t\u00cf\3\2\2\2\13\u00d1\3\2\2\2\r\u00d3\3\2\2\2\17\u00d5\3\2\2"+
		"\2\21\u00d7\3\2\2\2\23\u00d9\3\2\2\2\25\u00db\3\2\2\2\27\u00dd\3\2\2\2"+
		"\31\u00df\3\2\2\2\33\u00e1\3\2\2\2\35\u00e3\3\2\2\2\37\u00e5\3\2\2\2!"+
		"\u00e7\3\2\2\2#\u00e9\3\2\2\2%\u00ed\3\2\2\2\'\u00f1\3\2\2\2)\u00f4\3"+
		"\2\2\2+\u00f9\3\2\2\2-\u00fe\3\2\2\2/\u0107\3\2\2\2\61\u010e\3\2\2\2\63"+
		"\u0114\3\2\2\2\65\u0118\3\2\2\2\67\u011d\3\2\2\29\u0120\3\2\2\2;\u0125"+
		"\3\2\2\2=\u0128\3\2\2\2?\u012c\3\2\2\2A\u012f\3\2\2\2C\u0132\3\2\2\2E"+
		"\u0134\3\2\2\2G\u0137\3\2\2\2I\u0139\3\2\2\2K\u013c\3\2\2\2M\u013e\3\2"+
		"\2\2O\u0140\3\2\2\2Q\u0143\3\2\2\2S\u0147\3\2\2\2U\u0151\3\2\2\2W\u015b"+
		"\3\2\2\2Y\u0165\3\2\2\2[\u016f\3\2\2\2]\u017c\3\2\2\2_\u0189\3\2\2\2a"+
		"\u0197\3\2\2\2c\u019e\3\2\2\2e\u01a2\3\2\2\2g\u01a5\3\2\2\2i\u01b2\3\2"+
		"\2\2k\u01b6\3\2\2\2m\u01c2\3\2\2\2o\u01cd\3\2\2\2q\u01d7\3\2\2\2s\u01df"+
		"\3\2\2\2u\u01e4\3\2\2\2w\u01fb\3\2\2\2y\u01ff\3\2\2\2{\u0201\3\2\2\2}"+
		"\u0203\3\2\2\2\177\u0205\3\2\2\2\u0081\u0207\3\2\2\2\u0083\u0209\3\2\2"+
		"\2\u0085\u020b\3\2\2\2\u0087\u020d\3\2\2\2\u0089\u020f\3\2\2\2\u008b\u0211"+
		"\3\2\2\2\u008d\u0213\3\2\2\2\u008f\u0215\3\2\2\2\u0091\u0217\3\2\2\2\u0093"+
		"\u0219\3\2\2\2\u0095\u021b\3\2\2\2\u0097\u021d\3\2\2\2\u0099\u021f\3\2"+
		"\2\2\u009b\u0221\3\2\2\2\u009d\u0223\3\2\2\2\u009f\u0225\3\2\2\2\u00a1"+
		"\u0227\3\2\2\2\u00a3\u0229\3\2\2\2\u00a5\u022b\3\2\2\2\u00a7\u022d\3\2"+
		"\2\2\u00a9\u022f\3\2\2\2\u00ab\u0231\3\2\2\2\u00ad\u0233\3\2\2\2\u00af"+
		"\u00b3\5\5\3\2\u00b0\u00b2\n\2\2\2\u00b1\u00b0\3\2\2\2\u00b2\u00b5\3\2"+
		"\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b6\3\2\2\2\u00b5"+
		"\u00b3\3\2\2\2\u00b6\u00b7\5\5\3\2\u00b7\u00b9\3\2\2\2\u00b8\u00af\3\2"+
		"\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb"+
		"\u00ca\3\2\2\2\u00bc\u00c0\5\7\4\2\u00bd\u00bf\n\3\2\2\u00be\u00bd\3\2"+
		"\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1"+
		"\u00c3\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c3\u00c4\5\7\4\2\u00c4\u00c6\3\2"+
		"\2\2\u00c5\u00bc\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c7"+
		"\u00c8\3\2\2\2\u00c8\u00ca\3\2\2\2\u00c9\u00b8\3\2\2\2\u00c9\u00c5\3\2"+
		"\2\2\u00ca\4\3\2\2\2\u00cb\u00cc\7)\2\2\u00cc\6\3\2\2\2\u00cd\u00ce\7"+
		"$\2\2\u00ce\b\3\2\2\2\u00cf\u00d0\7}\2\2\u00d0\n\3\2\2\2\u00d1\u00d2\7"+
		"\177\2\2\u00d2\f\3\2\2\2\u00d3\u00d4\7]\2\2\u00d4\16\3\2\2\2\u00d5\u00d6"+
		"\7_\2\2\u00d6\20\3\2\2\2\u00d7\u00d8\7-\2\2\u00d8\22\3\2\2\2\u00d9\u00da"+
		"\7/\2\2\u00da\24\3\2\2\2\u00db\u00dc\7\61\2\2\u00dc\26\3\2\2\2\u00dd\u00de"+
		"\7\'\2\2\u00de\30\3\2\2\2\u00df\u00e0\7`\2\2\u00e0\32\3\2\2\2\u00e1\u00e2"+
		"\7,\2\2\u00e2\34\3\2\2\2\u00e3\u00e4\7*\2\2\u00e4\36\3\2\2\2\u00e5\u00e6"+
		"\7+\2\2\u00e6 \3\2\2\2\u00e7\u00e8\7.\2\2\u00e8\"\3\2\2\2\u00e9\u00ea"+
		"\5\u0093J\2\u00ea\u00eb\5\u008bF\2\u00eb\u00ec\5\u0095K\2\u00ec$\3\2\2"+
		"\2\u00ed\u00ee\5\u0093J\2\u00ee\u00ef\5{>\2\u00ef\u00f0\5\u00a9U\2\u00f0"+
		"&\3\2\2\2\u00f1\u00f2\5\u008bF\2\u00f2\u00f3\5\u0085C\2\u00f3(\3\2\2\2"+
		"\u00f4\u00f5\5\u00a1Q\2\u00f5\u00f6\5\u0089E\2\u00f6\u00f7\5\u0083B\2"+
		"\u00f7\u00f8\5\u0095K\2\u00f8*\3\2\2\2\u00f9\u00fa\5\u0083B\2\u00fa\u00fb"+
		"\5\u0091I\2\u00fb\u00fc\5\u009fP\2\u00fc\u00fd\5\u0083B\2\u00fd,\3\2\2"+
		"\2\u00fe\u00ff\5\u0083B\2\u00ff\u0100\5\u00a9U\2\u0100\u0101\5\u008bF"+
		"\2\u0101\u0102\5\u00a1Q\2\u0102\u0103\5\u00a7T\2\u0103\u0104\5\u008bF"+
		"\2\u0104\u0105\5\u00a1Q\2\u0105\u0106\5\u0089E\2\u0106.\3\2\2\2\u0107"+
		"\u0108\5\u009fP\2\u0108\u0109\5\u0083B\2\u0109\u010a\5\u0091I\2\u010a"+
		"\u010b\5\u0083B\2\u010b\u010c\5\177@\2\u010c\u010d\5\u00a1Q\2\u010d\60"+
		"\3\2\2\2\u010e\u010f\5\177@\2\u010f\u0110\5\u0097L\2\u0110\u0111\5\u00a3"+
		"R\2\u0111\u0112\5\u0095K\2\u0112\u0113\5\u00a1Q\2\u0113\62\3\2\2\2\u0114"+
		"\u0115\5\u0085C\2\u0115\u0116\5\u0097L\2\u0116\u0117\5\u009dO\2\u0117"+
		"\64\3\2\2\2\u0118\u0119\5\u0085C\2\u0119\u011a\5\u009dO\2\u011a\u011b"+
		"\5\u0097L\2\u011b\u011c\5\u0093J\2\u011c\66\3\2\2\2\u011d\u011e\5\u00a1"+
		"Q\2\u011e\u011f\5\u0097L\2\u011f8\3\2\2\2\u0120\u0121\5\u009fP\2\u0121"+
		"\u0122\5\u00a1Q\2\u0122\u0123\5\u0083B\2\u0123\u0124\5\u0099M\2\u0124"+
		":\3\2\2\2\u0125\u0126\5\u0081A\2\u0126\u0127\5\u0097L\2\u0127<\3\2\2\2"+
		"\u0128\u0129\5\u009fP\2\u0129\u012a\5\u00a1Q\2\u012a\u012b\5\u009dO\2"+
		"\u012b>\3\2\2\2\u012c\u012d\7?\2\2\u012d\u012e\7?\2\2\u012e@\3\2\2\2\u012f"+
		"\u0130\7#\2\2\u0130\u0131\7?\2\2\u0131B\3\2\2\2\u0132\u0133\7>\2\2\u0133"+
		"D\3\2\2\2\u0134\u0135\7>\2\2\u0135\u0136\7?\2\2\u0136F\3\2\2\2\u0137\u0138"+
		"\7@\2\2\u0138H\3\2\2\2\u0139\u013a\7@\2\2\u013a\u013b\7?\2\2\u013bJ\3"+
		"\2\2\2\u013c\u013d\7?\2\2\u013dL\3\2\2\2\u013e\u013f\7=\2\2\u013fN\3\2"+
		"\2\2\u0140\u0141\5\u0097L\2\u0141\u0142\5\u009dO\2\u0142P\3\2\2\2\u0143"+
		"\u0144\5{>\2\u0144\u0145\5\u0095K\2\u0145\u0146\5\u0081A\2\u0146R\3\2"+
		"\2\2\u0147\u0148\5\u009fP\2\u0148\u0149\5{>\2\u0149\u014a\5\u0085C\2\u014a"+
		"\u014b\5\u0083B\2\u014b\u014c\5\u00adW\2\u014c\u014d\5\u0097L\2\u014d"+
		"\u014e\5\u0095K\2\u014e\u014f\5\u0083B\2\u014f\u0150\5\u00a9U\2\u0150"+
		"T\3\2\2\2\u0151\u0152\5\u009fP\2\u0152\u0153\5{>\2\u0153\u0154\5\u0085"+
		"C\2\u0154\u0155\5\u0083B\2\u0155\u0156\5\u00adW\2\u0156\u0157\5\u0097"+
		"L\2\u0157\u0158\5\u0095K\2\u0158\u0159\5\u0083B\2\u0159\u015a\5\u00ab"+
		"V\2\u015aV\3\2\2\2\u015b\u015c\5\u009fP\2\u015c\u015d\5{>\2\u015d\u015e"+
		"\5\u0085C\2\u015e\u015f\5\u0083B\2\u015f\u0160\5\u00adW\2\u0160\u0161"+
		"\5\u0097L\2\u0161\u0162\5\u0095K\2\u0162\u0163\5\u0083B\2\u0163\u0164"+
		"\5\u00a7T\2\u0164X\3\2\2\2\u0165\u0166\5\u009fP\2\u0166\u0167\5{>\2\u0167"+
		"\u0168\5\u0085C\2\u0168\u0169\5\u0083B\2\u0169\u016a\5\u00adW\2\u016a"+
		"\u016b\5\u0097L\2\u016b\u016c\5\u0095K\2\u016c\u016d\5\u0083B\2\u016d"+
		"\u016e\5\u0089E\2\u016eZ\3\2\2\2\u016f\u0170\5\u009fP\2\u0170\u0171\5"+
		"{>\2\u0171\u0172\5\u0085C\2\u0172\u0173\5\u0083B\2\u0173\u0174\5\u00ad"+
		"W\2\u0174\u0175\5\u0097L\2\u0175\u0176\5\u0095K\2\u0176\u0177\5\u0083"+
		"B\2\u0177\u0178\5\u00a9U\2\u0178\u0179\5{>\2\u0179\u017a\5}?\2\u017a\u017b"+
		"\5\u009fP\2\u017b\\\3\2\2\2\u017c\u017d\5\u009fP\2\u017d\u017e\5{>\2\u017e"+
		"\u017f\5\u0085C\2\u017f\u0180\5\u0083B\2\u0180\u0181\5\u00adW\2\u0181"+
		"\u0182\5\u0097L\2\u0182\u0183\5\u0095K\2\u0183\u0184\5\u0083B\2\u0184"+
		"\u0185\5\u00a7T\2\u0185\u0186\5{>\2\u0186\u0187\5}?\2\u0187\u0188\5\u009f"+
		"P\2\u0188^\3\2\2\2\u0189\u018a\5\u0087D\2\u018a\u018b\5\u0083B\2\u018b"+
		"\u018c\5\u00a1Q\2\u018c\u018d\5\u009dO\2\u018d\u018e\5\u0083B\2\u018e"+
		"\u018f\5\u009fP\2\u018f\u0190\5\u0097L\2\u0190\u0191\5\u0091I\2\u0191"+
		"\u0192\5\u00a3R\2\u0192\u0193\5\u00a1Q\2\u0193\u0194\5\u008bF\2\u0194"+
		"\u0195\5\u0097L\2\u0195\u0196\5\u0095K\2\u0196`\3\2\2\2\u0197\u019b\5"+
		"q9\2\u0198\u019a\5s:\2\u0199\u0198\3\2\2\2\u019a\u019d\3\2\2\2\u019b\u0199"+
		"\3\2\2\2\u019b\u019c\3\2\2\2\u019cb\3\2\2\2\u019d\u019b\3\2\2\2\u019e"+
		"\u019f\5g\64\2\u019fd\3\2\2\2\u01a0\u01a3\5i\65\2\u01a1\u01a3\5k\66\2"+
		"\u01a2\u01a0\3\2\2\2\u01a2\u01a1\3\2\2\2\u01a3f\3\2\2\2\u01a4\u01a6\5"+
		"y=\2\u01a5\u01a4\3\2\2\2\u01a6\u01a7\3\2\2\2\u01a7\u01a5\3\2\2\2\u01a7"+
		"\u01a8\3\2\2\2\u01a8h\3\2\2\2\u01a9\u01aa\7\60\2\2\u01aa\u01b3\5g\64\2"+
		"\u01ab\u01ac\5g\64\2\u01ac\u01ae\7\60\2\2\u01ad\u01af\5y=\2\u01ae\u01ad"+
		"\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1"+
		"\u01b3\3\2\2\2\u01b2\u01a9\3\2\2\2\u01b2\u01ab\3\2\2\2\u01b3j\3\2\2\2"+
		"\u01b4\u01b7\5i\65\2\u01b5\u01b7\5c\62\2\u01b6\u01b4\3\2\2\2\u01b6\u01b5"+
		"\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8\u01ba\t\4\2\2\u01b9\u01bb\t\5\2\2\u01ba"+
		"\u01b9\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb\u01bf\3\2\2\2\u01bc\u01be\5y"+
		"=\2\u01bd\u01bc\3\2\2\2\u01be\u01c1\3\2\2\2\u01bf\u01bd\3\2\2\2\u01bf"+
		"\u01c0\3\2\2\2\u01c0l\3\2\2\2\u01c1\u01bf\3\2\2\2\u01c2\u01c3\7\62\2\2"+
		"\u01c3\u01c7\t\6\2\2\u01c4\u01c6\7\62\2\2\u01c5\u01c4\3\2\2\2\u01c6\u01c9"+
		"\3\2\2\2\u01c7\u01c5\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\u01ca\3\2\2\2\u01c9"+
		"\u01c7\3\2\2\2\u01ca\u01cb\5o8\2\u01cbn\3\2\2\2\u01cc\u01ce\t\7\2\2\u01cd"+
		"\u01cc\3\2\2\2\u01ce\u01cf\3\2\2\2\u01cf\u01cd\3\2\2\2\u01cf\u01d0\3\2"+
		"\2\2\u01d0p\3\2\2\2\u01d1\u01d8\t\b\2\2\u01d2\u01d3\n\t\2\2\u01d3\u01d8"+
		"\69\2\2\u01d4\u01d5\t\n\2\2\u01d5\u01d6\t\13\2\2\u01d6\u01d8\69\3\2\u01d7"+
		"\u01d1\3\2\2\2\u01d7\u01d2\3\2\2\2\u01d7\u01d4\3\2\2\2\u01d8r\3\2\2\2"+
		"\u01d9\u01e0\t\f\2\2\u01da\u01db\n\t\2\2\u01db\u01e0\6:\4\2\u01dc\u01dd"+
		"\t\n\2\2\u01dd\u01de\t\13\2\2\u01de\u01e0\6:\5\2\u01df\u01d9\3\2\2\2\u01df"+
		"\u01da\3\2\2\2\u01df\u01dc\3\2\2\2\u01e0t\3\2\2\2\u01e1\u01e5\t\r\2\2"+
		"\u01e2\u01e3\7\17\2\2\u01e3\u01e5\7\f\2\2\u01e4\u01e1\3\2\2\2\u01e4\u01e2"+
		"\3\2\2\2\u01e5\u01e6\3\2\2\2\u01e6\u01e7\b;\2\2\u01e7v\3\2\2\2\u01e8\u01e9"+
		"\7\61\2\2\u01e9\u01ea\7\61\2\2\u01ea\u01ec\3\2\2\2\u01eb\u01ed\n\16\2"+
		"\2\u01ec\u01eb\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\u01ec\3\2\2\2\u01ee\u01ef"+
		"\3\2\2\2\u01ef\u01fc\3\2\2\2\u01f0\u01f1\7\61\2\2\u01f1\u01f2\7,\2\2\u01f2"+
		"\u01f6\3\2\2\2\u01f3\u01f5\13\2\2\2\u01f4\u01f3\3\2\2\2\u01f5\u01f8\3"+
		"\2\2\2\u01f6\u01f7\3\2\2\2\u01f6\u01f4\3\2\2\2\u01f7\u01f9\3\2\2\2\u01f8"+
		"\u01f6\3\2\2\2\u01f9\u01fa\7,\2\2\u01fa\u01fc\7\61\2\2\u01fb\u01e8\3\2"+
		"\2\2\u01fb\u01f0\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd\u01fe\b<\2\2\u01fe"+
		"x\3\2\2\2\u01ff\u0200\4\62;\2\u0200z\3\2\2\2\u0201\u0202\t\17\2\2\u0202"+
		"|\3\2\2\2\u0203\u0204\t\20\2\2\u0204~\3\2\2\2\u0205\u0206\t\21\2\2\u0206"+
		"\u0080\3\2\2\2\u0207\u0208\t\22\2\2\u0208\u0082\3\2\2\2\u0209\u020a\t"+
		"\4\2\2\u020a\u0084\3\2\2\2\u020b\u020c\t\23\2\2\u020c\u0086\3\2\2\2\u020d"+
		"\u020e\t\24\2\2\u020e\u0088\3\2\2\2\u020f\u0210\t\25\2\2\u0210\u008a\3"+
		"\2\2\2\u0211\u0212\t\26\2\2\u0212\u008c\3\2\2\2\u0213\u0214\t\27\2\2\u0214"+
		"\u008e\3\2\2\2\u0215\u0216\t\30\2\2\u0216\u0090\3\2\2\2\u0217\u0218\t"+
		"\31\2\2\u0218\u0092\3\2\2\2\u0219\u021a\t\32\2\2\u021a\u0094\3\2\2\2\u021b"+
		"\u021c\t\33\2\2\u021c\u0096\3\2\2\2\u021d\u021e\t\34\2\2\u021e\u0098\3"+
		"\2\2\2\u021f\u0220\t\35\2\2\u0220\u009a\3\2\2\2\u0221\u0222\t\36\2\2\u0222"+
		"\u009c\3\2\2\2\u0223\u0224\t\37\2\2\u0224\u009e\3\2\2\2\u0225\u0226\t"+
		" \2\2\u0226\u00a0\3\2\2\2\u0227\u0228\t!\2\2\u0228\u00a2\3\2\2\2\u0229"+
		"\u022a\t\"\2\2\u022a\u00a4\3\2\2\2\u022b\u022c\t#\2\2\u022c\u00a6\3\2"+
		"\2\2\u022d\u022e\t$\2\2\u022e\u00a8\3\2\2\2\u022f\u0230\t\6\2\2\u0230"+
		"\u00aa\3\2\2\2\u0231\u0232\t%\2\2\u0232\u00ac\3\2\2\2\u0233\u0234\t&\2"+
		"\2\u0234\u00ae\3\2\2\2\30\2\u00b3\u00ba\u00c0\u00c7\u00c9\u019b\u01a2"+
		"\u01a7\u01b0\u01b2\u01b6\u01ba\u01bf\u01c7\u01cf\u01d7\u01df\u01e4\u01ee"+
		"\u01f6\u01fb\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}