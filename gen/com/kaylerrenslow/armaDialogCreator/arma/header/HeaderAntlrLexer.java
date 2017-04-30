// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/kaylerrenslow/armaDialogCreator/arma/header\HeaderAntlr.g4 by ANTLR 4.7
package com.kaylerrenslow.armaDialogCreator.arma.header;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class HeaderAntlrLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"String", "Class", "Comma", "Colon", "Semicolon", "PlusEqual", "Equal", 
		"LBrace", "RBrace", "BacketPair", "Quote", "DQuote", "Plus", "Minus", 
		"Star", "FSlash", "LParen", "RParen", "Identifier", "Number", "Letter", 
		"LetterOrDigit", "WhiteSpace", "Comment", "INTEGER_LITERAL", "DEC_LITERAL", 
		"HEX_LITERAL", "DIGITS", "DEC_SIGNIFICAND", "DEC_EXPONENT", "HEX_DIGIT"
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


	public HeaderAntlrLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "HeaderAntlr.g4"; }

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
		case 20:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 21:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\35\u00fe\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \3\2\3\2\7\2D\n\2\f\2\16\2G\13\2\3\2\3\2\6\2K\n\2\r\2\16\2L\3\2\3\2\7"+
		"\2Q\n\2\f\2\16\2T\13\2\3\2\3\2\6\2X\n\2\r\2\16\2Y\5\2\\\n\2\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n"+
		"\3\n\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21"+
		"\3\21\3\22\3\22\3\23\3\23\3\24\3\24\7\24\u0088\n\24\f\24\16\24\u008b\13"+
		"\24\3\25\3\25\3\25\5\25\u0090\n\25\3\26\3\26\3\26\3\26\3\26\3\26\5\26"+
		"\u0098\n\26\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u00a0\n\27\3\30\3\30\3"+
		"\30\5\30\u00a5\n\30\3\30\3\30\3\31\3\31\3\31\3\31\6\31\u00ad\n\31\r\31"+
		"\16\31\u00ae\3\31\3\31\3\31\3\31\7\31\u00b5\n\31\f\31\16\31\u00b8\13\31"+
		"\3\31\3\31\5\31\u00bc\n\31\3\31\3\31\3\32\3\32\3\33\3\33\5\33\u00c4\n"+
		"\33\3\34\3\34\3\34\7\34\u00c9\n\34\f\34\16\34\u00cc\13\34\3\34\3\34\5"+
		"\34\u00d0\n\34\3\34\5\34\u00d3\n\34\3\34\5\34\u00d6\n\34\3\34\5\34\u00d9"+
		"\n\34\3\34\5\34\u00dc\n\34\3\34\5\34\u00df\n\34\3\34\5\34\u00e2\n\34\3"+
		"\35\6\35\u00e5\n\35\r\35\16\35\u00e6\3\36\3\36\3\36\3\36\3\36\3\36\5\36"+
		"\u00ef\n\36\3\37\3\37\5\37\u00f3\n\37\3\37\3\37\5\37\u00f7\n\37\3\37\3"+
		"\37\3 \3 \5 \u00fd\n \3\u00b6\2!\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31"+
		"\61\32\63\33\65\34\67\359\2;\2=\2?\2\3\2\17\3\2))\3\2$$\6\2&&C\\aac|\4"+
		"\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aa"+
		"c|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\4\2ZZzz\4\2GGgg\4\2--//\4\2CHch\2"+
		"\u0117\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2"+
		"\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2"+
		"/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\3[\3\2\2"+
		"\2\5]\3\2\2\2\7c\3\2\2\2\te\3\2\2\2\13g\3\2\2\2\ri\3\2\2\2\17l\3\2\2\2"+
		"\21n\3\2\2\2\23p\3\2\2\2\25r\3\2\2\2\27u\3\2\2\2\31w\3\2\2\2\33y\3\2\2"+
		"\2\35{\3\2\2\2\37}\3\2\2\2!\177\3\2\2\2#\u0081\3\2\2\2%\u0083\3\2\2\2"+
		"\'\u0085\3\2\2\2)\u008f\3\2\2\2+\u0097\3\2\2\2-\u009f\3\2\2\2/\u00a4\3"+
		"\2\2\2\61\u00bb\3\2\2\2\63\u00bf\3\2\2\2\65\u00c3\3\2\2\2\67\u00c5\3\2"+
		"\2\29\u00e4\3\2\2\2;\u00ee\3\2\2\2=\u00f2\3\2\2\2?\u00fc\3\2\2\2AE\5\27"+
		"\f\2BD\n\2\2\2CB\3\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2FH\3\2\2\2GE\3\2"+
		"\2\2HI\5\27\f\2IK\3\2\2\2JA\3\2\2\2KL\3\2\2\2LJ\3\2\2\2LM\3\2\2\2M\\\3"+
		"\2\2\2NR\5\31\r\2OQ\n\3\2\2PO\3\2\2\2QT\3\2\2\2RP\3\2\2\2RS\3\2\2\2SU"+
		"\3\2\2\2TR\3\2\2\2UV\5\31\r\2VX\3\2\2\2WN\3\2\2\2XY\3\2\2\2YW\3\2\2\2"+
		"YZ\3\2\2\2Z\\\3\2\2\2[J\3\2\2\2[W\3\2\2\2\\\4\3\2\2\2]^\7e\2\2^_\7n\2"+
		"\2_`\7c\2\2`a\7u\2\2ab\7u\2\2b\6\3\2\2\2cd\7.\2\2d\b\3\2\2\2ef\7<\2\2"+
		"f\n\3\2\2\2gh\7=\2\2h\f\3\2\2\2ij\7-\2\2jk\7?\2\2k\16\3\2\2\2lm\7?\2\2"+
		"m\20\3\2\2\2no\7}\2\2o\22\3\2\2\2pq\7\177\2\2q\24\3\2\2\2rs\7]\2\2st\7"+
		"_\2\2t\26\3\2\2\2uv\7)\2\2v\30\3\2\2\2wx\7$\2\2x\32\3\2\2\2yz\7-\2\2z"+
		"\34\3\2\2\2{|\7/\2\2|\36\3\2\2\2}~\7,\2\2~ \3\2\2\2\177\u0080\7\61\2\2"+
		"\u0080\"\3\2\2\2\u0081\u0082\7*\2\2\u0082$\3\2\2\2\u0083\u0084\7+\2\2"+
		"\u0084&\3\2\2\2\u0085\u0089\5+\26\2\u0086\u0088\5-\27\2\u0087\u0086\3"+
		"\2\2\2\u0088\u008b\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a"+
		"(\3\2\2\2\u008b\u0089\3\2\2\2\u008c\u0090\5\63\32\2\u008d\u0090\5\65\33"+
		"\2\u008e\u0090\5\67\34\2\u008f\u008c\3\2\2\2\u008f\u008d\3\2\2\2\u008f"+
		"\u008e\3\2\2\2\u0090*\3\2\2\2\u0091\u0098\t\4\2\2\u0092\u0093\n\5\2\2"+
		"\u0093\u0098\6\26\2\2\u0094\u0095\t\6\2\2\u0095\u0096\t\7\2\2\u0096\u0098"+
		"\6\26\3\2\u0097\u0091\3\2\2\2\u0097\u0092\3\2\2\2\u0097\u0094\3\2\2\2"+
		"\u0098,\3\2\2\2\u0099\u00a0\t\b\2\2\u009a\u009b\n\5\2\2\u009b\u00a0\6"+
		"\27\4\2\u009c\u009d\t\6\2\2\u009d\u009e\t\7\2\2\u009e\u00a0\6\27\5\2\u009f"+
		"\u0099\3\2\2\2\u009f\u009a\3\2\2\2\u009f\u009c\3\2\2\2\u00a0.\3\2\2\2"+
		"\u00a1\u00a5\t\t\2\2\u00a2\u00a3\7\17\2\2\u00a3\u00a5\7\f\2\2\u00a4\u00a1"+
		"\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\b\30\2\2"+
		"\u00a7\60\3\2\2\2\u00a8\u00a9\7\61\2\2\u00a9\u00aa\7\61\2\2\u00aa\u00ac"+
		"\3\2\2\2\u00ab\u00ad\n\n\2\2\u00ac\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae"+
		"\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00bc\3\2\2\2\u00b0\u00b1\7\61"+
		"\2\2\u00b1\u00b2\7,\2\2\u00b2\u00b6\3\2\2\2\u00b3\u00b5\13\2\2\2\u00b4"+
		"\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b6\u00b4\3\2"+
		"\2\2\u00b7\u00b9\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00ba\7,\2\2\u00ba"+
		"\u00bc\7\61\2\2\u00bb\u00a8\3\2\2\2\u00bb\u00b0\3\2\2\2\u00bc\u00bd\3"+
		"\2\2\2\u00bd\u00be\b\31\2\2\u00be\62\3\2\2\2\u00bf\u00c0\59\35\2\u00c0"+
		"\64\3\2\2\2\u00c1\u00c4\5;\36\2\u00c2\u00c4\5=\37\2\u00c3\u00c1\3\2\2"+
		"\2\u00c3\u00c2\3\2\2\2\u00c4\66\3\2\2\2\u00c5\u00c6\7\62\2\2\u00c6\u00ca"+
		"\t\13\2\2\u00c7\u00c9\7\62\2\2\u00c8\u00c7\3\2\2\2\u00c9\u00cc\3\2\2\2"+
		"\u00ca\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cd\3\2\2\2\u00cc\u00ca"+
		"\3\2\2\2\u00cd\u00cf\5? \2\u00ce\u00d0\5? \2\u00cf\u00ce\3\2\2\2\u00cf"+
		"\u00d0\3\2\2\2\u00d0\u00d2\3\2\2\2\u00d1\u00d3\5? \2\u00d2\u00d1\3\2\2"+
		"\2\u00d2\u00d3\3\2\2\2\u00d3\u00d5\3\2\2\2\u00d4\u00d6\5? \2\u00d5\u00d4"+
		"\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d8\3\2\2\2\u00d7\u00d9\5? \2\u00d8"+
		"\u00d7\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00db\3\2\2\2\u00da\u00dc\5?"+
		" \2\u00db\u00da\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00de\3\2\2\2\u00dd"+
		"\u00df\5? \2\u00de\u00dd\3\2\2\2\u00de\u00df\3\2\2\2\u00df\u00e1\3\2\2"+
		"\2\u00e0\u00e2\5? \2\u00e1\u00e0\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e28\3"+
		"\2\2\2\u00e3\u00e5\4\62;\2\u00e4\u00e3\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6"+
		"\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7:\3\2\2\2\u00e8\u00e9\7\60\2\2"+
		"\u00e9\u00ef\59\35\2\u00ea\u00eb\59\35\2\u00eb\u00ec\7\60\2\2\u00ec\u00ed"+
		"\59\35\2\u00ed\u00ef\3\2\2\2\u00ee\u00e8\3\2\2\2\u00ee\u00ea\3\2\2\2\u00ef"+
		"<\3\2\2\2\u00f0\u00f3\5;\36\2\u00f1\u00f3\5\63\32\2\u00f2\u00f0\3\2\2"+
		"\2\u00f2\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f6\t\f\2\2\u00f5\u00f7"+
		"\t\r\2\2\u00f6\u00f5\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8"+
		"\u00f9\59\35\2\u00f9>\3\2\2\2\u00fa\u00fd\59\35\2\u00fb\u00fd\t\16\2\2"+
		"\u00fc\u00fa\3\2\2\2\u00fc\u00fb\3\2\2\2\u00fd@\3\2\2\2\36\2ELRY[\u0089"+
		"\u008f\u0097\u009f\u00a4\u00ae\u00b6\u00bb\u00c3\u00ca\u00cf\u00d2\u00d5"+
		"\u00d8\u00db\u00de\u00e1\u00e6\u00ee\u00f2\u00f6\u00fc\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}