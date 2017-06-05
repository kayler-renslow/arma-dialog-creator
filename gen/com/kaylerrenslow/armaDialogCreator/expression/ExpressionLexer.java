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
		Lt=33, LtEq=34, Gt=35, GtEq=36, Equal=37, Semicolon=38, Identifier=39, 
		IntegerLiteral=40, FloatLiteral=41, Digits=42, DecSignificand=43, DecExponent=44, 
		HexLiteral=45, HexDigits=46, Letter=47, LetterOrDigit=48, WhiteSpace=49, 
		Comment=50;
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
		"Gt", "GtEq", "Equal", "Semicolon", "Identifier", "IntegerLiteral", "FloatLiteral", 
		"Digits", "DecSignificand", "DecExponent", "HexLiteral", "HexDigits", 
		"Letter", "LetterOrDigit", "WhiteSpace", "Comment", "DIGIT", "A", "B", 
		"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", 
		"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
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
		"Gt", "GtEq", "Equal", "Semicolon", "Identifier", "IntegerLiteral", "FloatLiteral", 
		"Digits", "DecSignificand", "DecExponent", "HexLiteral", "HexDigits", 
		"Letter", "LetterOrDigit", "WhiteSpace", "Comment"
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
		case 46:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 47:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\64\u01cc\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4"+
		"I\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\3\2\3\2\7\2\u00a0\n\2\f\2\16\2\u00a3"+
		"\13\2\3\2\3\2\6\2\u00a7\n\2\r\2\16\2\u00a8\3\2\3\2\7\2\u00ad\n\2\f\2\16"+
		"\2\u00b0\13\2\3\2\3\2\6\2\u00b4\n\2\r\2\16\2\u00b5\5\2\u00b8\n\2\3\3\3"+
		"\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3"+
		"\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22"+
		"\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26"+
		"\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32"+
		"\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35"+
		"\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3 \3 \3 \3!\3!\3!\3\"\3\"\3#"+
		"\3#\3#\3$\3$\3%\3%\3%\3&\3&\3\'\3\'\3(\3(\7(\u0131\n(\f(\16(\u0134\13"+
		"(\3)\3)\3*\3*\5*\u013a\n*\3+\6+\u013d\n+\r+\16+\u013e\3,\3,\3,\3,\3,\6"+
		",\u0146\n,\r,\16,\u0147\5,\u014a\n,\3-\3-\5-\u014e\n-\3-\3-\5-\u0152\n"+
		"-\3-\7-\u0155\n-\f-\16-\u0158\13-\3.\3.\3.\7.\u015d\n.\f.\16.\u0160\13"+
		".\3.\3.\3/\6/\u0165\n/\r/\16/\u0166\3\60\3\60\3\60\3\60\3\60\3\60\5\60"+
		"\u016f\n\60\3\61\3\61\3\61\3\61\3\61\3\61\5\61\u0177\n\61\3\62\3\62\3"+
		"\62\5\62\u017c\n\62\3\62\3\62\3\63\3\63\3\63\3\63\6\63\u0184\n\63\r\63"+
		"\16\63\u0185\3\63\3\63\3\63\3\63\7\63\u018c\n\63\f\63\16\63\u018f\13\63"+
		"\3\63\3\63\5\63\u0193\n\63\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67"+
		"\3\67\38\38\39\39\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3A\3A\3B\3"+
		"B\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L\3M\3M\3"+
		"N\3N\3\u018d\2O\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65"+
		"\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64"+
		"g\2i\2k\2m\2o\2q\2s\2u\2w\2y\2{\2}\2\177\2\u0081\2\u0083\2\u0085\2\u0087"+
		"\2\u0089\2\u008b\2\u008d\2\u008f\2\u0091\2\u0093\2\u0095\2\u0097\2\u0099"+
		"\2\u009b\2\3\2\'\3\2))\3\2$$\4\2GGgg\4\2--//\4\2ZZzz\5\2\62;CHch\6\2&"+
		"&C\\aac|\4\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&"+
		"&\62;C\\aac|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\4\2CCcc\4\2DDdd\4\2EEee"+
		"\4\2FFff\4\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4\2O"+
		"Ooo\4\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4"+
		"\2XXxx\4\2YYyy\4\2[[{{\4\2\\\\||\2\u01c7\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2"+
		"\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2"+
		"A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3"+
		"\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2"+
		"\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\3"+
		"\u00b7\3\2\2\2\5\u00b9\3\2\2\2\7\u00bb\3\2\2\2\t\u00bd\3\2\2\2\13\u00bf"+
		"\3\2\2\2\r\u00c1\3\2\2\2\17\u00c3\3\2\2\2\21\u00c5\3\2\2\2\23\u00c7\3"+
		"\2\2\2\25\u00c9\3\2\2\2\27\u00cb\3\2\2\2\31\u00cd\3\2\2\2\33\u00cf\3\2"+
		"\2\2\35\u00d1\3\2\2\2\37\u00d3\3\2\2\2!\u00d5\3\2\2\2#\u00d7\3\2\2\2%"+
		"\u00db\3\2\2\2\'\u00df\3\2\2\2)\u00e2\3\2\2\2+\u00e7\3\2\2\2-\u00ec\3"+
		"\2\2\2/\u00f5\3\2\2\2\61\u00fc\3\2\2\2\63\u0102\3\2\2\2\65\u0106\3\2\2"+
		"\2\67\u010b\3\2\2\29\u010e\3\2\2\2;\u0113\3\2\2\2=\u0116\3\2\2\2?\u011a"+
		"\3\2\2\2A\u011d\3\2\2\2C\u0120\3\2\2\2E\u0122\3\2\2\2G\u0125\3\2\2\2I"+
		"\u0127\3\2\2\2K\u012a\3\2\2\2M\u012c\3\2\2\2O\u012e\3\2\2\2Q\u0135\3\2"+
		"\2\2S\u0139\3\2\2\2U\u013c\3\2\2\2W\u0149\3\2\2\2Y\u014d\3\2\2\2[\u0159"+
		"\3\2\2\2]\u0164\3\2\2\2_\u016e\3\2\2\2a\u0176\3\2\2\2c\u017b\3\2\2\2e"+
		"\u0192\3\2\2\2g\u0196\3\2\2\2i\u0198\3\2\2\2k\u019a\3\2\2\2m\u019c\3\2"+
		"\2\2o\u019e\3\2\2\2q\u01a0\3\2\2\2s\u01a2\3\2\2\2u\u01a4\3\2\2\2w\u01a6"+
		"\3\2\2\2y\u01a8\3\2\2\2{\u01aa\3\2\2\2}\u01ac\3\2\2\2\177\u01ae\3\2\2"+
		"\2\u0081\u01b0\3\2\2\2\u0083\u01b2\3\2\2\2\u0085\u01b4\3\2\2\2\u0087\u01b6"+
		"\3\2\2\2\u0089\u01b8\3\2\2\2\u008b\u01ba\3\2\2\2\u008d\u01bc\3\2\2\2\u008f"+
		"\u01be\3\2\2\2\u0091\u01c0\3\2\2\2\u0093\u01c2\3\2\2\2\u0095\u01c4\3\2"+
		"\2\2\u0097\u01c6\3\2\2\2\u0099\u01c8\3\2\2\2\u009b\u01ca\3\2\2\2\u009d"+
		"\u00a1\5\5\3\2\u009e\u00a0\n\2\2\2\u009f\u009e\3\2\2\2\u00a0\u00a3\3\2"+
		"\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a4\3\2\2\2\u00a3"+
		"\u00a1\3\2\2\2\u00a4\u00a5\5\5\3\2\u00a5\u00a7\3\2\2\2\u00a6\u009d\3\2"+
		"\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9"+
		"\u00b8\3\2\2\2\u00aa\u00ae\5\7\4\2\u00ab\u00ad\n\3\2\2\u00ac\u00ab\3\2"+
		"\2\2\u00ad\u00b0\3\2\2\2\u00ae\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af"+
		"\u00b1\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b1\u00b2\5\7\4\2\u00b2\u00b4\3\2"+
		"\2\2\u00b3\u00aa\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5"+
		"\u00b6\3\2\2\2\u00b6\u00b8\3\2\2\2\u00b7\u00a6\3\2\2\2\u00b7\u00b3\3\2"+
		"\2\2\u00b8\4\3\2\2\2\u00b9\u00ba\7)\2\2\u00ba\6\3\2\2\2\u00bb\u00bc\7"+
		"$\2\2\u00bc\b\3\2\2\2\u00bd\u00be\7}\2\2\u00be\n\3\2\2\2\u00bf\u00c0\7"+
		"\177\2\2\u00c0\f\3\2\2\2\u00c1\u00c2\7]\2\2\u00c2\16\3\2\2\2\u00c3\u00c4"+
		"\7_\2\2\u00c4\20\3\2\2\2\u00c5\u00c6\7-\2\2\u00c6\22\3\2\2\2\u00c7\u00c8"+
		"\7/\2\2\u00c8\24\3\2\2\2\u00c9\u00ca\7\61\2\2\u00ca\26\3\2\2\2\u00cb\u00cc"+
		"\7\'\2\2\u00cc\30\3\2\2\2\u00cd\u00ce\7`\2\2\u00ce\32\3\2\2\2\u00cf\u00d0"+
		"\7,\2\2\u00d0\34\3\2\2\2\u00d1\u00d2\7*\2\2\u00d2\36\3\2\2\2\u00d3\u00d4"+
		"\7+\2\2\u00d4 \3\2\2\2\u00d5\u00d6\7.\2\2\u00d6\"\3\2\2\2\u00d7\u00d8"+
		"\5\u0081A\2\u00d8\u00d9\5y=\2\u00d9\u00da\5\u0083B\2\u00da$\3\2\2\2\u00db"+
		"\u00dc\5\u0081A\2\u00dc\u00dd\5i\65\2\u00dd\u00de\5\u0097L\2\u00de&\3"+
		"\2\2\2\u00df\u00e0\5y=\2\u00e0\u00e1\5s:\2\u00e1(\3\2\2\2\u00e2\u00e3"+
		"\5\u008fH\2\u00e3\u00e4\5w<\2\u00e4\u00e5\5q9\2\u00e5\u00e6\5\u0083B\2"+
		"\u00e6*\3\2\2\2\u00e7\u00e8\5q9\2\u00e8\u00e9\5\177@\2\u00e9\u00ea\5\u008d"+
		"G\2\u00ea\u00eb\5q9\2\u00eb,\3\2\2\2\u00ec\u00ed\5q9\2\u00ed\u00ee\5\u0097"+
		"L\2\u00ee\u00ef\5y=\2\u00ef\u00f0\5\u008fH\2\u00f0\u00f1\5\u0095K\2\u00f1"+
		"\u00f2\5y=\2\u00f2\u00f3\5\u008fH\2\u00f3\u00f4\5w<\2\u00f4.\3\2\2\2\u00f5"+
		"\u00f6\5\u008dG\2\u00f6\u00f7\5q9\2\u00f7\u00f8\5\177@\2\u00f8\u00f9\5"+
		"q9\2\u00f9\u00fa\5m\67\2\u00fa\u00fb\5\u008fH\2\u00fb\60\3\2\2\2\u00fc"+
		"\u00fd\5m\67\2\u00fd\u00fe\5\u0085C\2\u00fe\u00ff\5\u0091I\2\u00ff\u0100"+
		"\5\u0083B\2\u0100\u0101\5\u008fH\2\u0101\62\3\2\2\2\u0102\u0103\5s:\2"+
		"\u0103\u0104\5\u0085C\2\u0104\u0105\5\u008bF\2\u0105\64\3\2\2\2\u0106"+
		"\u0107\5s:\2\u0107\u0108\5\u008bF\2\u0108\u0109\5\u0085C\2\u0109\u010a"+
		"\5\u0081A\2\u010a\66\3\2\2\2\u010b\u010c\5\u008fH\2\u010c\u010d\5\u0085"+
		"C\2\u010d8\3\2\2\2\u010e\u010f\5\u008dG\2\u010f\u0110\5\u008fH\2\u0110"+
		"\u0111\5q9\2\u0111\u0112\5\u0087D\2\u0112:\3\2\2\2\u0113\u0114\5o8\2\u0114"+
		"\u0115\5\u0085C\2\u0115<\3\2\2\2\u0116\u0117\5\u008dG\2\u0117\u0118\5"+
		"\u008fH\2\u0118\u0119\5\u008bF\2\u0119>\3\2\2\2\u011a\u011b\7?\2\2\u011b"+
		"\u011c\7?\2\2\u011c@\3\2\2\2\u011d\u011e\7#\2\2\u011e\u011f\7?\2\2\u011f"+
		"B\3\2\2\2\u0120\u0121\7>\2\2\u0121D\3\2\2\2\u0122\u0123\7>\2\2\u0123\u0124"+
		"\7?\2\2\u0124F\3\2\2\2\u0125\u0126\7@\2\2\u0126H\3\2\2\2\u0127\u0128\7"+
		"@\2\2\u0128\u0129\7?\2\2\u0129J\3\2\2\2\u012a\u012b\7?\2\2\u012bL\3\2"+
		"\2\2\u012c\u012d\7=\2\2\u012dN\3\2\2\2\u012e\u0132\5_\60\2\u012f\u0131"+
		"\5a\61\2\u0130\u012f\3\2\2\2\u0131\u0134\3\2\2\2\u0132\u0130\3\2\2\2\u0132"+
		"\u0133\3\2\2\2\u0133P\3\2\2\2\u0134\u0132\3\2\2\2\u0135\u0136\5U+\2\u0136"+
		"R\3\2\2\2\u0137\u013a\5W,\2\u0138\u013a\5Y-\2\u0139\u0137\3\2\2\2\u0139"+
		"\u0138\3\2\2\2\u013aT\3\2\2\2\u013b\u013d\5g\64\2\u013c\u013b\3\2\2\2"+
		"\u013d\u013e\3\2\2\2\u013e\u013c\3\2\2\2\u013e\u013f\3\2\2\2\u013fV\3"+
		"\2\2\2\u0140\u0141\7\60\2\2\u0141\u014a\5U+\2\u0142\u0143\5U+\2\u0143"+
		"\u0145\7\60\2\2\u0144\u0146\5g\64\2\u0145\u0144\3\2\2\2\u0146\u0147\3"+
		"\2\2\2\u0147\u0145\3\2\2\2\u0147\u0148\3\2\2\2\u0148\u014a\3\2\2\2\u0149"+
		"\u0140\3\2\2\2\u0149\u0142\3\2\2\2\u014aX\3\2\2\2\u014b\u014e\5W,\2\u014c"+
		"\u014e\5Q)\2\u014d\u014b\3\2\2\2\u014d\u014c\3\2\2\2\u014e\u014f\3\2\2"+
		"\2\u014f\u0151\t\4\2\2\u0150\u0152\t\5\2\2\u0151\u0150\3\2\2\2\u0151\u0152"+
		"\3\2\2\2\u0152\u0156\3\2\2\2\u0153\u0155\5g\64\2\u0154\u0153\3\2\2\2\u0155"+
		"\u0158\3\2\2\2\u0156\u0154\3\2\2\2\u0156\u0157\3\2\2\2\u0157Z\3\2\2\2"+
		"\u0158\u0156\3\2\2\2\u0159\u015a\7\62\2\2\u015a\u015e\t\6\2\2\u015b\u015d"+
		"\7\62\2\2\u015c\u015b\3\2\2\2\u015d\u0160\3\2\2\2\u015e\u015c\3\2\2\2"+
		"\u015e\u015f\3\2\2\2\u015f\u0161\3\2\2\2\u0160\u015e\3\2\2\2\u0161\u0162"+
		"\5]/\2\u0162\\\3\2\2\2\u0163\u0165\t\7\2\2\u0164\u0163\3\2\2\2\u0165\u0166"+
		"\3\2\2\2\u0166\u0164\3\2\2\2\u0166\u0167\3\2\2\2\u0167^\3\2\2\2\u0168"+
		"\u016f\t\b\2\2\u0169\u016a\n\t\2\2\u016a\u016f\6\60\2\2\u016b\u016c\t"+
		"\n\2\2\u016c\u016d\t\13\2\2\u016d\u016f\6\60\3\2\u016e\u0168\3\2\2\2\u016e"+
		"\u0169\3\2\2\2\u016e\u016b\3\2\2\2\u016f`\3\2\2\2\u0170\u0177\t\f\2\2"+
		"\u0171\u0172\n\t\2\2\u0172\u0177\6\61\4\2\u0173\u0174\t\n\2\2\u0174\u0175"+
		"\t\13\2\2\u0175\u0177\6\61\5\2\u0176\u0170\3\2\2\2\u0176\u0171\3\2\2\2"+
		"\u0176\u0173\3\2\2\2\u0177b\3\2\2\2\u0178\u017c\t\r\2\2\u0179\u017a\7"+
		"\17\2\2\u017a\u017c\7\f\2\2\u017b\u0178\3\2\2\2\u017b\u0179\3\2\2\2\u017c"+
		"\u017d\3\2\2\2\u017d\u017e\b\62\2\2\u017ed\3\2\2\2\u017f\u0180\7\61\2"+
		"\2\u0180\u0181\7\61\2\2\u0181\u0183\3\2\2\2\u0182\u0184\n\16\2\2\u0183"+
		"\u0182\3\2\2\2\u0184\u0185\3\2\2\2\u0185\u0183\3\2\2\2\u0185\u0186\3\2"+
		"\2\2\u0186\u0193\3\2\2\2\u0187\u0188\7\61\2\2\u0188\u0189\7,\2\2\u0189"+
		"\u018d\3\2\2\2\u018a\u018c\13\2\2\2\u018b\u018a\3\2\2\2\u018c\u018f\3"+
		"\2\2\2\u018d\u018e\3\2\2\2\u018d\u018b\3\2\2\2\u018e\u0190\3\2\2\2\u018f"+
		"\u018d\3\2\2\2\u0190\u0191\7,\2\2\u0191\u0193\7\61\2\2\u0192\u017f\3\2"+
		"\2\2\u0192\u0187\3\2\2\2\u0193\u0194\3\2\2\2\u0194\u0195\b\63\2\2\u0195"+
		"f\3\2\2\2\u0196\u0197\4\62;\2\u0197h\3\2\2\2\u0198\u0199\t\17\2\2\u0199"+
		"j\3\2\2\2\u019a\u019b\t\20\2\2\u019bl\3\2\2\2\u019c\u019d\t\21\2\2\u019d"+
		"n\3\2\2\2\u019e\u019f\t\22\2\2\u019fp\3\2\2\2\u01a0\u01a1\t\4\2\2\u01a1"+
		"r\3\2\2\2\u01a2\u01a3\t\23\2\2\u01a3t\3\2\2\2\u01a4\u01a5\t\24\2\2\u01a5"+
		"v\3\2\2\2\u01a6\u01a7\t\25\2\2\u01a7x\3\2\2\2\u01a8\u01a9\t\26\2\2\u01a9"+
		"z\3\2\2\2\u01aa\u01ab\t\27\2\2\u01ab|\3\2\2\2\u01ac\u01ad\t\30\2\2\u01ad"+
		"~\3\2\2\2\u01ae\u01af\t\31\2\2\u01af\u0080\3\2\2\2\u01b0\u01b1\t\32\2"+
		"\2\u01b1\u0082\3\2\2\2\u01b2\u01b3\t\33\2\2\u01b3\u0084\3\2\2\2\u01b4"+
		"\u01b5\t\34\2\2\u01b5\u0086\3\2\2\2\u01b6\u01b7\t\35\2\2\u01b7\u0088\3"+
		"\2\2\2\u01b8\u01b9\t\36\2\2\u01b9\u008a\3\2\2\2\u01ba\u01bb\t\37\2\2\u01bb"+
		"\u008c\3\2\2\2\u01bc\u01bd\t \2\2\u01bd\u008e\3\2\2\2\u01be\u01bf\t!\2"+
		"\2\u01bf\u0090\3\2\2\2\u01c0\u01c1\t\"\2\2\u01c1\u0092\3\2\2\2\u01c2\u01c3"+
		"\t#\2\2\u01c3\u0094\3\2\2\2\u01c4\u01c5\t$\2\2\u01c5\u0096\3\2\2\2\u01c6"+
		"\u01c7\t\6\2\2\u01c7\u0098\3\2\2\2\u01c8\u01c9\t%\2\2\u01c9\u009a\3\2"+
		"\2\2\u01ca\u01cb\t&\2\2\u01cb\u009c\3\2\2\2\30\2\u00a1\u00a8\u00ae\u00b5"+
		"\u00b7\u0132\u0139\u013e\u0147\u0149\u014d\u0151\u0156\u015e\u0166\u016e"+
		"\u0176\u017b\u0185\u018d\u0192\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}