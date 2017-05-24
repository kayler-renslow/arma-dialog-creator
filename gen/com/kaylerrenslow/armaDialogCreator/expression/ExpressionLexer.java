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
		String=1, Quote=2, DQuote=3, Plus=4, Minus=5, FSlash=6, Star=7, LParen=8, 
		RParen=9, Min=10, Max=11, Equal=12, Semicolon=13, Identifier=14, IntegerLiteral=15, 
		FloatLiteral=16, Digits=17, DecSignificand=18, DecExponent=19, HexLiteral=20, 
		HexDigits=21, Letter=22, LetterOrDigit=23, WhiteSpace=24;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"String", "Quote", "DQuote", "Plus", "Minus", "FSlash", "Star", "LParen", 
		"RParen", "Min", "Max", "Equal", "Semicolon", "Identifier", "IntegerLiteral", 
		"FloatLiteral", "Digits", "DecSignificand", "DecExponent", "HexLiteral", 
		"HexDigits", "Letter", "LetterOrDigit", "WhiteSpace", "DIGIT"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\32\u00c0\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\3\2\3\2\7\28\n\2\f\2\16\2;\13\2\3\2\3\2\6\2?\n\2\r\2\16"+
		"\2@\3\2\3\2\7\2E\n\2\f\2\16\2H\13\2\3\2\3\2\6\2L\n\2\r\2\16\2M\5\2P\n"+
		"\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\7\17p\n\17"+
		"\f\17\16\17s\13\17\3\20\3\20\3\21\3\21\5\21y\n\21\3\22\6\22|\n\22\r\22"+
		"\16\22}\3\23\3\23\3\23\3\23\3\23\6\23\u0085\n\23\r\23\16\23\u0086\5\23"+
		"\u0089\n\23\3\24\3\24\5\24\u008d\n\24\3\24\3\24\5\24\u0091\n\24\3\24\7"+
		"\24\u0094\n\24\f\24\16\24\u0097\13\24\3\25\3\25\3\25\7\25\u009c\n\25\f"+
		"\25\16\25\u009f\13\25\3\25\3\25\3\26\6\26\u00a4\n\26\r\26\16\26\u00a5"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u00ae\n\27\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\5\30\u00b6\n\30\3\31\3\31\3\31\5\31\u00bb\n\31\3\31\3\31\3\32\3"+
		"\32\2\2\33\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\2\3\2\16\3\2"+
		"))\3\2$$\4\2GGgg\4\2--//\4\2ZZzz\5\2\62;CHch\6\2&&C\\aac|\4\2\2\u0101"+
		"\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aac|\5\2\13\f"+
		"\17\17\"\"\2\u00d2\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\3O\3\2\2\2\5Q\3\2\2\2\7S\3\2\2\2\tU\3\2"+
		"\2\2\13W\3\2\2\2\rY\3\2\2\2\17[\3\2\2\2\21]\3\2\2\2\23_\3\2\2\2\25a\3"+
		"\2\2\2\27e\3\2\2\2\31i\3\2\2\2\33k\3\2\2\2\35m\3\2\2\2\37t\3\2\2\2!x\3"+
		"\2\2\2#{\3\2\2\2%\u0088\3\2\2\2\'\u008c\3\2\2\2)\u0098\3\2\2\2+\u00a3"+
		"\3\2\2\2-\u00ad\3\2\2\2/\u00b5\3\2\2\2\61\u00ba\3\2\2\2\63\u00be\3\2\2"+
		"\2\659\5\5\3\2\668\n\2\2\2\67\66\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2"+
		"\2:<\3\2\2\2;9\3\2\2\2<=\5\5\3\2=?\3\2\2\2>\65\3\2\2\2?@\3\2\2\2@>\3\2"+
		"\2\2@A\3\2\2\2AP\3\2\2\2BF\5\7\4\2CE\n\3\2\2DC\3\2\2\2EH\3\2\2\2FD\3\2"+
		"\2\2FG\3\2\2\2GI\3\2\2\2HF\3\2\2\2IJ\5\7\4\2JL\3\2\2\2KB\3\2\2\2LM\3\2"+
		"\2\2MK\3\2\2\2MN\3\2\2\2NP\3\2\2\2O>\3\2\2\2OK\3\2\2\2P\4\3\2\2\2QR\7"+
		")\2\2R\6\3\2\2\2ST\7$\2\2T\b\3\2\2\2UV\7-\2\2V\n\3\2\2\2WX\7/\2\2X\f\3"+
		"\2\2\2YZ\7\61\2\2Z\16\3\2\2\2[\\\7,\2\2\\\20\3\2\2\2]^\7*\2\2^\22\3\2"+
		"\2\2_`\7+\2\2`\24\3\2\2\2ab\7o\2\2bc\7k\2\2cd\7p\2\2d\26\3\2\2\2ef\7o"+
		"\2\2fg\7c\2\2gh\7z\2\2h\30\3\2\2\2ij\7?\2\2j\32\3\2\2\2kl\7=\2\2l\34\3"+
		"\2\2\2mq\5-\27\2np\5/\30\2on\3\2\2\2ps\3\2\2\2qo\3\2\2\2qr\3\2\2\2r\36"+
		"\3\2\2\2sq\3\2\2\2tu\5#\22\2u \3\2\2\2vy\5%\23\2wy\5\'\24\2xv\3\2\2\2"+
		"xw\3\2\2\2y\"\3\2\2\2z|\5\63\32\2{z\3\2\2\2|}\3\2\2\2}{\3\2\2\2}~\3\2"+
		"\2\2~$\3\2\2\2\177\u0080\7\60\2\2\u0080\u0089\5#\22\2\u0081\u0082\5#\22"+
		"\2\u0082\u0084\7\60\2\2\u0083\u0085\5\63\32\2\u0084\u0083\3\2\2\2\u0085"+
		"\u0086\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0089\3\2"+
		"\2\2\u0088\177\3\2\2\2\u0088\u0081\3\2\2\2\u0089&\3\2\2\2\u008a\u008d"+
		"\5%\23\2\u008b\u008d\5\37\20\2\u008c\u008a\3\2\2\2\u008c\u008b\3\2\2\2"+
		"\u008d\u008e\3\2\2\2\u008e\u0090\t\4\2\2\u008f\u0091\t\5\2\2\u0090\u008f"+
		"\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0095\3\2\2\2\u0092\u0094\5\63\32\2"+
		"\u0093\u0092\3\2\2\2\u0094\u0097\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096"+
		"\3\2\2\2\u0096(\3\2\2\2\u0097\u0095\3\2\2\2\u0098\u0099\7\62\2\2\u0099"+
		"\u009d\t\6\2\2\u009a\u009c\7\62\2\2\u009b\u009a\3\2\2\2\u009c\u009f\3"+
		"\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u00a0\3\2\2\2\u009f"+
		"\u009d\3\2\2\2\u00a0\u00a1\5+\26\2\u00a1*\3\2\2\2\u00a2\u00a4\t\7\2\2"+
		"\u00a3\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6"+
		"\3\2\2\2\u00a6,\3\2\2\2\u00a7\u00ae\t\b\2\2\u00a8\u00a9\n\t\2\2\u00a9"+
		"\u00ae\6\27\2\2\u00aa\u00ab\t\n\2\2\u00ab\u00ac\t\13\2\2\u00ac\u00ae\6"+
		"\27\3\2\u00ad\u00a7\3\2\2\2\u00ad\u00a8\3\2\2\2\u00ad\u00aa\3\2\2\2\u00ae"+
		".\3\2\2\2\u00af\u00b6\t\f\2\2\u00b0\u00b1\n\t\2\2\u00b1\u00b6\6\30\4\2"+
		"\u00b2\u00b3\t\n\2\2\u00b3\u00b4\t\13\2\2\u00b4\u00b6\6\30\5\2\u00b5\u00af"+
		"\3\2\2\2\u00b5\u00b0\3\2\2\2\u00b5\u00b2\3\2\2\2\u00b6\60\3\2\2\2\u00b7"+
		"\u00bb\t\r\2\2\u00b8\u00b9\7\17\2\2\u00b9\u00bb\7\f\2\2\u00ba\u00b7\3"+
		"\2\2\2\u00ba\u00b8\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00bd\b\31\2\2\u00bd"+
		"\62\3\2\2\2\u00be\u00bf\4\62;\2\u00bf\64\3\2\2\2\25\29@FMOqx}\u0086\u0088"+
		"\u008c\u0090\u0095\u009d\u00a5\u00ad\u00b5\u00ba\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}