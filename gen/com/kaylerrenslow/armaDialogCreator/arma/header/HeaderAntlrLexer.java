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
		Star=15, FSlash=16, LParen=17, RParen=18, BSlash=19, Identifier=20, Number=21, 
		Letter=22, LetterOrDigit=23, WhiteSpace=24, Comment=25, INTEGER_LITERAL=26, 
		DEC_LITERAL=27, HEX_LITERAL=28;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"String", "Class", "Comma", "Colon", "Semicolon", "PlusEqual", "Equal", 
		"LBrace", "RBrace", "BacketPair", "Quote", "DQuote", "Plus", "Minus", 
		"Star", "FSlash", "LParen", "RParen", "BSlash", "Identifier", "Number", 
		"Letter", "LetterOrDigit", "WhiteSpace", "Comment", "INTEGER_LITERAL", 
		"DEC_LITERAL", "HEX_LITERAL", "DIGITS", "DEC_SIGNIFICAND", "DEC_EXPONENT", 
		"HEX_DIGIT"
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
		case 21:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 22:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\36\u0102\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\3\2\3\2\7\2F\n\2\f\2\16\2I\13\2\3\2\3\2\6\2M\n\2\r\2\16\2N\3\2"+
		"\3\2\7\2S\n\2\f\2\16\2V\13\2\3\2\3\2\6\2Z\n\2\r\2\16\2[\5\2^\n\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t"+
		"\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20"+
		"\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\7\25\u008c\n\25\f\25"+
		"\16\25\u008f\13\25\3\26\3\26\3\26\5\26\u0094\n\26\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\5\27\u009c\n\27\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u00a4\n"+
		"\30\3\31\3\31\3\31\5\31\u00a9\n\31\3\31\3\31\3\32\3\32\3\32\3\32\6\32"+
		"\u00b1\n\32\r\32\16\32\u00b2\3\32\3\32\3\32\3\32\7\32\u00b9\n\32\f\32"+
		"\16\32\u00bc\13\32\3\32\3\32\5\32\u00c0\n\32\3\32\3\32\3\33\3\33\3\34"+
		"\3\34\5\34\u00c8\n\34\3\35\3\35\3\35\7\35\u00cd\n\35\f\35\16\35\u00d0"+
		"\13\35\3\35\3\35\5\35\u00d4\n\35\3\35\5\35\u00d7\n\35\3\35\5\35\u00da"+
		"\n\35\3\35\5\35\u00dd\n\35\3\35\5\35\u00e0\n\35\3\35\5\35\u00e3\n\35\3"+
		"\35\5\35\u00e6\n\35\3\36\6\36\u00e9\n\36\r\36\16\36\u00ea\3\37\3\37\3"+
		"\37\3\37\3\37\3\37\5\37\u00f3\n\37\3 \3 \5 \u00f7\n \3 \3 \5 \u00fb\n"+
		" \3 \3 \3!\3!\5!\u0101\n!\3\u00ba\2\"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\2=\2?\2A\2\3\2\17\3\2))\3\2$$\6\2&&"+
		"C\\aac|\4\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&"+
		"\62;C\\aac|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\4\2ZZzz\4\2GGgg\4\2--//\4"+
		"\2CHch\2\u011b\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3"+
		"\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2"+
		"\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2"+
		"\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\2"+
		"9\3\2\2\2\3]\3\2\2\2\5_\3\2\2\2\7e\3\2\2\2\tg\3\2\2\2\13i\3\2\2\2\rk\3"+
		"\2\2\2\17n\3\2\2\2\21p\3\2\2\2\23r\3\2\2\2\25t\3\2\2\2\27w\3\2\2\2\31"+
		"y\3\2\2\2\33{\3\2\2\2\35}\3\2\2\2\37\177\3\2\2\2!\u0081\3\2\2\2#\u0083"+
		"\3\2\2\2%\u0085\3\2\2\2\'\u0087\3\2\2\2)\u0089\3\2\2\2+\u0093\3\2\2\2"+
		"-\u009b\3\2\2\2/\u00a3\3\2\2\2\61\u00a8\3\2\2\2\63\u00bf\3\2\2\2\65\u00c3"+
		"\3\2\2\2\67\u00c7\3\2\2\29\u00c9\3\2\2\2;\u00e8\3\2\2\2=\u00f2\3\2\2\2"+
		"?\u00f6\3\2\2\2A\u0100\3\2\2\2CG\5\27\f\2DF\n\2\2\2ED\3\2\2\2FI\3\2\2"+
		"\2GE\3\2\2\2GH\3\2\2\2HJ\3\2\2\2IG\3\2\2\2JK\5\27\f\2KM\3\2\2\2LC\3\2"+
		"\2\2MN\3\2\2\2NL\3\2\2\2NO\3\2\2\2O^\3\2\2\2PT\5\31\r\2QS\n\3\2\2RQ\3"+
		"\2\2\2SV\3\2\2\2TR\3\2\2\2TU\3\2\2\2UW\3\2\2\2VT\3\2\2\2WX\5\31\r\2XZ"+
		"\3\2\2\2YP\3\2\2\2Z[\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\^\3\2\2\2]L\3\2\2\2"+
		"]Y\3\2\2\2^\4\3\2\2\2_`\7e\2\2`a\7n\2\2ab\7c\2\2bc\7u\2\2cd\7u\2\2d\6"+
		"\3\2\2\2ef\7.\2\2f\b\3\2\2\2gh\7<\2\2h\n\3\2\2\2ij\7=\2\2j\f\3\2\2\2k"+
		"l\7-\2\2lm\7?\2\2m\16\3\2\2\2no\7?\2\2o\20\3\2\2\2pq\7}\2\2q\22\3\2\2"+
		"\2rs\7\177\2\2s\24\3\2\2\2tu\7]\2\2uv\7_\2\2v\26\3\2\2\2wx\7)\2\2x\30"+
		"\3\2\2\2yz\7$\2\2z\32\3\2\2\2{|\7-\2\2|\34\3\2\2\2}~\7/\2\2~\36\3\2\2"+
		"\2\177\u0080\7,\2\2\u0080 \3\2\2\2\u0081\u0082\7\61\2\2\u0082\"\3\2\2"+
		"\2\u0083\u0084\7*\2\2\u0084$\3\2\2\2\u0085\u0086\7+\2\2\u0086&\3\2\2\2"+
		"\u0087\u0088\7^\2\2\u0088(\3\2\2\2\u0089\u008d\5-\27\2\u008a\u008c\5/"+
		"\30\2\u008b\u008a\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2\2\2\u008d"+
		"\u008e\3\2\2\2\u008e*\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0094\5\65\33"+
		"\2\u0091\u0094\5\67\34\2\u0092\u0094\59\35\2\u0093\u0090\3\2\2\2\u0093"+
		"\u0091\3\2\2\2\u0093\u0092\3\2\2\2\u0094,\3\2\2\2\u0095\u009c\t\4\2\2"+
		"\u0096\u0097\n\5\2\2\u0097\u009c\6\27\2\2\u0098\u0099\t\6\2\2\u0099\u009a"+
		"\t\7\2\2\u009a\u009c\6\27\3\2\u009b\u0095\3\2\2\2\u009b\u0096\3\2\2\2"+
		"\u009b\u0098\3\2\2\2\u009c.\3\2\2\2\u009d\u00a4\t\b\2\2\u009e\u009f\n"+
		"\5\2\2\u009f\u00a4\6\30\4\2\u00a0\u00a1\t\6\2\2\u00a1\u00a2\t\7\2\2\u00a2"+
		"\u00a4\6\30\5\2\u00a3\u009d\3\2\2\2\u00a3\u009e\3\2\2\2\u00a3\u00a0\3"+
		"\2\2\2\u00a4\60\3\2\2\2\u00a5\u00a9\t\t\2\2\u00a6\u00a7\7\17\2\2\u00a7"+
		"\u00a9\7\f\2\2\u00a8\u00a5\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00aa\3\2"+
		"\2\2\u00aa\u00ab\b\31\2\2\u00ab\62\3\2\2\2\u00ac\u00ad\7\61\2\2\u00ad"+
		"\u00ae\7\61\2\2\u00ae\u00b0\3\2\2\2\u00af\u00b1\n\n\2\2\u00b0\u00af\3"+
		"\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3"+
		"\u00c0\3\2\2\2\u00b4\u00b5\7\61\2\2\u00b5\u00b6\7,\2\2\u00b6\u00ba\3\2"+
		"\2\2\u00b7\u00b9\13\2\2\2\u00b8\u00b7\3\2\2\2\u00b9\u00bc\3\2\2\2\u00ba"+
		"\u00bb\3\2\2\2\u00ba\u00b8\3\2\2\2\u00bb\u00bd\3\2\2\2\u00bc\u00ba\3\2"+
		"\2\2\u00bd\u00be\7,\2\2\u00be\u00c0\7\61\2\2\u00bf\u00ac\3\2\2\2\u00bf"+
		"\u00b4\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c2\b\32\2\2\u00c2\64\3\2\2"+
		"\2\u00c3\u00c4\5;\36\2\u00c4\66\3\2\2\2\u00c5\u00c8\5=\37\2\u00c6\u00c8"+
		"\5? \2\u00c7\u00c5\3\2\2\2\u00c7\u00c6\3\2\2\2\u00c88\3\2\2\2\u00c9\u00ca"+
		"\7\62\2\2\u00ca\u00ce\t\13\2\2\u00cb\u00cd\7\62\2\2\u00cc\u00cb\3\2\2"+
		"\2\u00cd\u00d0\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d1"+
		"\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d1\u00d3\5A!\2\u00d2\u00d4\5A!\2\u00d3"+
		"\u00d2\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00d7\5A"+
		"!\2\u00d6\u00d5\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d9\3\2\2\2\u00d8"+
		"\u00da\5A!\2\u00d9\u00d8\3\2\2\2\u00d9\u00da\3\2\2\2\u00da\u00dc\3\2\2"+
		"\2\u00db\u00dd\5A!\2\u00dc\u00db\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00df"+
		"\3\2\2\2\u00de\u00e0\5A!\2\u00df\u00de\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0"+
		"\u00e2\3\2\2\2\u00e1\u00e3\5A!\2\u00e2\u00e1\3\2\2\2\u00e2\u00e3\3\2\2"+
		"\2\u00e3\u00e5\3\2\2\2\u00e4\u00e6\5A!\2\u00e5\u00e4\3\2\2\2\u00e5\u00e6"+
		"\3\2\2\2\u00e6:\3\2\2\2\u00e7\u00e9\4\62;\2\u00e8\u00e7\3\2\2\2\u00e9"+
		"\u00ea\3\2\2\2\u00ea\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb<\3\2\2\2"+
		"\u00ec\u00ed\7\60\2\2\u00ed\u00f3\5;\36\2\u00ee\u00ef\5;\36\2\u00ef\u00f0"+
		"\7\60\2\2\u00f0\u00f1\5;\36\2\u00f1\u00f3\3\2\2\2\u00f2\u00ec\3\2\2\2"+
		"\u00f2\u00ee\3\2\2\2\u00f3>\3\2\2\2\u00f4\u00f7\5=\37\2\u00f5\u00f7\5"+
		"\65\33\2\u00f6\u00f4\3\2\2\2\u00f6\u00f5\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8"+
		"\u00fa\t\f\2\2\u00f9\u00fb\t\r\2\2\u00fa\u00f9\3\2\2\2\u00fa\u00fb\3\2"+
		"\2\2\u00fb\u00fc\3\2\2\2\u00fc\u00fd\5;\36\2\u00fd@\3\2\2\2\u00fe\u0101"+
		"\5;\36\2\u00ff\u0101\t\16\2\2\u0100\u00fe\3\2\2\2\u0100\u00ff\3\2\2\2"+
		"\u0101B\3\2\2\2\36\2GNT[]\u008d\u0093\u009b\u00a3\u00a8\u00b2\u00ba\u00bf"+
		"\u00c7\u00ce\u00d3\u00d6\u00d9\u00dc\u00df\u00e2\u00e5\u00ea\u00f2\u00f6"+
		"\u00fa\u0100\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}