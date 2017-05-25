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
		Plus=8, Minus=9, FSlash=10, Star=11, LParen=12, RParen=13, Comma=14, Min=15, 
		Max=16, If=17, Then=18, Else=19, ExitWith=20, Select=21, Equal=22, Semicolon=23, 
		Identifier=24, IntegerLiteral=25, FloatLiteral=26, Digits=27, DecSignificand=28, 
		DecExponent=29, HexLiteral=30, HexDigits=31, Letter=32, LetterOrDigit=33, 
		WhiteSpace=34;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"String", "Quote", "DQuote", "LCurly", "RCurly", "LBracket", "RBracket", 
		"Plus", "Minus", "FSlash", "Star", "LParen", "RParen", "Comma", "Min", 
		"Max", "If", "Then", "Else", "ExitWith", "Select", "Equal", "Semicolon", 
		"Identifier", "IntegerLiteral", "FloatLiteral", "Digits", "DecSignificand", 
		"DecExponent", "HexLiteral", "HexDigits", "Letter", "LetterOrDigit", "WhiteSpace", 
		"DIGIT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'''", "'\"'", "'{'", "'}'", "'['", "']'", "'+'", "'-'", "'/'", 
		"'*'", "'('", "')'", "','", "'min'", "'max'", "'if'", "'then'", "'else'", 
		"'exitWith'", "'select'", "'='", "';'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Quote", "DQuote", "LCurly", "RCurly", "LBracket", "RBracket", 
		"Plus", "Minus", "FSlash", "Star", "LParen", "RParen", "Comma", "Min", 
		"Max", "If", "Then", "Else", "ExitWith", "Select", "Equal", "Semicolon", 
		"Identifier", "IntegerLiteral", "FloatLiteral", "Digits", "DecSignificand", 
		"DecExponent", "HexLiteral", "HexDigits", "Letter", "LetterOrDigit", "WhiteSpace"
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
		case 31:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 32:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2$\u00fb\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\3\2\3\2\7\2L\n\2\f\2\16\2O\13\2\3\2\3\2\6\2S\n"+
		"\2\r\2\16\2T\3\2\3\2\7\2Y\n\2\f\2\16\2\\\13\2\3\2\3\2\6\2`\n\2\r\2\16"+
		"\2a\5\2d\n\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3"+
		"\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3"+
		"\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3"+
		"\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3"+
		"\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\7\31\u00ab"+
		"\n\31\f\31\16\31\u00ae\13\31\3\32\3\32\3\33\3\33\5\33\u00b4\n\33\3\34"+
		"\6\34\u00b7\n\34\r\34\16\34\u00b8\3\35\3\35\3\35\3\35\3\35\6\35\u00c0"+
		"\n\35\r\35\16\35\u00c1\5\35\u00c4\n\35\3\36\3\36\5\36\u00c8\n\36\3\36"+
		"\3\36\5\36\u00cc\n\36\3\36\7\36\u00cf\n\36\f\36\16\36\u00d2\13\36\3\37"+
		"\3\37\3\37\7\37\u00d7\n\37\f\37\16\37\u00da\13\37\3\37\3\37\3 \6 \u00df"+
		"\n \r \16 \u00e0\3!\3!\3!\3!\3!\3!\5!\u00e9\n!\3\"\3\"\3\"\3\"\3\"\3\""+
		"\5\"\u00f1\n\"\3#\3#\3#\5#\u00f6\n#\3#\3#\3$\3$\2\2%\3\3\5\4\7\5\t\6\13"+
		"\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'"+
		"\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G\2\3\2"+
		"\16\3\2))\3\2$$\4\2GGgg\4\2--//\4\2ZZzz\5\2\62;CHch\6\2&&C\\aac|\4\2\2"+
		"\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aac|\5"+
		"\2\13\f\17\17\"\"\2\u010d\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2"+
		"\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67"+
		"\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2"+
		"\2\2\2E\3\2\2\2\3c\3\2\2\2\5e\3\2\2\2\7g\3\2\2\2\ti\3\2\2\2\13k\3\2\2"+
		"\2\rm\3\2\2\2\17o\3\2\2\2\21q\3\2\2\2\23s\3\2\2\2\25u\3\2\2\2\27w\3\2"+
		"\2\2\31y\3\2\2\2\33{\3\2\2\2\35}\3\2\2\2\37\177\3\2\2\2!\u0083\3\2\2\2"+
		"#\u0087\3\2\2\2%\u008a\3\2\2\2\'\u008f\3\2\2\2)\u0094\3\2\2\2+\u009d\3"+
		"\2\2\2-\u00a4\3\2\2\2/\u00a6\3\2\2\2\61\u00a8\3\2\2\2\63\u00af\3\2\2\2"+
		"\65\u00b3\3\2\2\2\67\u00b6\3\2\2\29\u00c3\3\2\2\2;\u00c7\3\2\2\2=\u00d3"+
		"\3\2\2\2?\u00de\3\2\2\2A\u00e8\3\2\2\2C\u00f0\3\2\2\2E\u00f5\3\2\2\2G"+
		"\u00f9\3\2\2\2IM\5\5\3\2JL\n\2\2\2KJ\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2"+
		"\2\2NP\3\2\2\2OM\3\2\2\2PQ\5\5\3\2QS\3\2\2\2RI\3\2\2\2ST\3\2\2\2TR\3\2"+
		"\2\2TU\3\2\2\2Ud\3\2\2\2VZ\5\7\4\2WY\n\3\2\2XW\3\2\2\2Y\\\3\2\2\2ZX\3"+
		"\2\2\2Z[\3\2\2\2[]\3\2\2\2\\Z\3\2\2\2]^\5\7\4\2^`\3\2\2\2_V\3\2\2\2`a"+
		"\3\2\2\2a_\3\2\2\2ab\3\2\2\2bd\3\2\2\2cR\3\2\2\2c_\3\2\2\2d\4\3\2\2\2"+
		"ef\7)\2\2f\6\3\2\2\2gh\7$\2\2h\b\3\2\2\2ij\7}\2\2j\n\3\2\2\2kl\7\177\2"+
		"\2l\f\3\2\2\2mn\7]\2\2n\16\3\2\2\2op\7_\2\2p\20\3\2\2\2qr\7-\2\2r\22\3"+
		"\2\2\2st\7/\2\2t\24\3\2\2\2uv\7\61\2\2v\26\3\2\2\2wx\7,\2\2x\30\3\2\2"+
		"\2yz\7*\2\2z\32\3\2\2\2{|\7+\2\2|\34\3\2\2\2}~\7.\2\2~\36\3\2\2\2\177"+
		"\u0080\7o\2\2\u0080\u0081\7k\2\2\u0081\u0082\7p\2\2\u0082 \3\2\2\2\u0083"+
		"\u0084\7o\2\2\u0084\u0085\7c\2\2\u0085\u0086\7z\2\2\u0086\"\3\2\2\2\u0087"+
		"\u0088\7k\2\2\u0088\u0089\7h\2\2\u0089$\3\2\2\2\u008a\u008b\7v\2\2\u008b"+
		"\u008c\7j\2\2\u008c\u008d\7g\2\2\u008d\u008e\7p\2\2\u008e&\3\2\2\2\u008f"+
		"\u0090\7g\2\2\u0090\u0091\7n\2\2\u0091\u0092\7u\2\2\u0092\u0093\7g\2\2"+
		"\u0093(\3\2\2\2\u0094\u0095\7g\2\2\u0095\u0096\7z\2\2\u0096\u0097\7k\2"+
		"\2\u0097\u0098\7v\2\2\u0098\u0099\7Y\2\2\u0099\u009a\7k\2\2\u009a\u009b"+
		"\7v\2\2\u009b\u009c\7j\2\2\u009c*\3\2\2\2\u009d\u009e\7u\2\2\u009e\u009f"+
		"\7g\2\2\u009f\u00a0\7n\2\2\u00a0\u00a1\7g\2\2\u00a1\u00a2\7e\2\2\u00a2"+
		"\u00a3\7v\2\2\u00a3,\3\2\2\2\u00a4\u00a5\7?\2\2\u00a5.\3\2\2\2\u00a6\u00a7"+
		"\7=\2\2\u00a7\60\3\2\2\2\u00a8\u00ac\5A!\2\u00a9\u00ab\5C\"\2\u00aa\u00a9"+
		"\3\2\2\2\u00ab\u00ae\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad"+
		"\62\3\2\2\2\u00ae\u00ac\3\2\2\2\u00af\u00b0\5\67\34\2\u00b0\64\3\2\2\2"+
		"\u00b1\u00b4\59\35\2\u00b2\u00b4\5;\36\2\u00b3\u00b1\3\2\2\2\u00b3\u00b2"+
		"\3\2\2\2\u00b4\66\3\2\2\2\u00b5\u00b7\5G$\2\u00b6\u00b5\3\2\2\2\u00b7"+
		"\u00b8\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b98\3\2\2\2"+
		"\u00ba\u00bb\7\60\2\2\u00bb\u00c4\5\67\34\2\u00bc\u00bd\5\67\34\2\u00bd"+
		"\u00bf\7\60\2\2\u00be\u00c0\5G$\2\u00bf\u00be\3\2\2\2\u00c0\u00c1\3\2"+
		"\2\2\u00c1\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c4\3\2\2\2\u00c3"+
		"\u00ba\3\2\2\2\u00c3\u00bc\3\2\2\2\u00c4:\3\2\2\2\u00c5\u00c8\59\35\2"+
		"\u00c6\u00c8\5\63\32\2\u00c7\u00c5\3\2\2\2\u00c7\u00c6\3\2\2\2\u00c8\u00c9"+
		"\3\2\2\2\u00c9\u00cb\t\4\2\2\u00ca\u00cc\t\5\2\2\u00cb\u00ca\3\2\2\2\u00cb"+
		"\u00cc\3\2\2\2\u00cc\u00d0\3\2\2\2\u00cd\u00cf\5G$\2\u00ce\u00cd\3\2\2"+
		"\2\u00cf\u00d2\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1<"+
		"\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d3\u00d4\7\62\2\2\u00d4\u00d8\t\6\2\2"+
		"\u00d5\u00d7\7\62\2\2\u00d6\u00d5\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d6"+
		"\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00db\3\2\2\2\u00da\u00d8\3\2\2\2\u00db"+
		"\u00dc\5? \2\u00dc>\3\2\2\2\u00dd\u00df\t\7\2\2\u00de\u00dd\3\2\2\2\u00df"+
		"\u00e0\3\2\2\2\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1@\3\2\2\2"+
		"\u00e2\u00e9\t\b\2\2\u00e3\u00e4\n\t\2\2\u00e4\u00e9\6!\2\2\u00e5\u00e6"+
		"\t\n\2\2\u00e6\u00e7\t\13\2\2\u00e7\u00e9\6!\3\2\u00e8\u00e2\3\2\2\2\u00e8"+
		"\u00e3\3\2\2\2\u00e8\u00e5\3\2\2\2\u00e9B\3\2\2\2\u00ea\u00f1\t\f\2\2"+
		"\u00eb\u00ec\n\t\2\2\u00ec\u00f1\6\"\4\2\u00ed\u00ee\t\n\2\2\u00ee\u00ef"+
		"\t\13\2\2\u00ef\u00f1\6\"\5\2\u00f0\u00ea\3\2\2\2\u00f0\u00eb\3\2\2\2"+
		"\u00f0\u00ed\3\2\2\2\u00f1D\3\2\2\2\u00f2\u00f6\t\r\2\2\u00f3\u00f4\7"+
		"\17\2\2\u00f4\u00f6\7\f\2\2\u00f5\u00f2\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6"+
		"\u00f7\3\2\2\2\u00f7\u00f8\b#\2\2\u00f8F\3\2\2\2\u00f9\u00fa\4\62;\2\u00fa"+
		"H\3\2\2\2\25\2MTZac\u00ac\u00b3\u00b8\u00c1\u00c3\u00c7\u00cb\u00d0\u00d8"+
		"\u00e0\u00e8\u00f0\u00f5\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}