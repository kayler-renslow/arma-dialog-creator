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
		RParen=9, Min=10, Max=11, Identifier=12, IntegerLiteral=13, FloatLiteral=14, 
		Digits=15, DecSignificand=16, DecExponent=17, HexLiteral=18, HexDigits=19, 
		Letter=20, LetterOrDigit=21, WhiteSpace=22;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"String", "Quote", "DQuote", "Plus", "Minus", "FSlash", "Star", "LParen", 
		"RParen", "Min", "Max", "Identifier", "IntegerLiteral", "FloatLiteral", 
		"Digits", "DecSignificand", "DecExponent", "HexLiteral", "HexDigits", 
		"Letter", "LetterOrDigit", "WhiteSpace", "DIGIT"
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
		case 19:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 20:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\30\u00b8\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2"+
		"\3\2\7\2\64\n\2\f\2\16\2\67\13\2\3\2\3\2\6\2;\n\2\r\2\16\2<\3\2\3\2\7"+
		"\2A\n\2\f\2\16\2D\13\2\3\2\3\2\6\2H\n\2\r\2\16\2I\5\2L\n\2\3\3\3\3\3\4"+
		"\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3"+
		"\13\3\f\3\f\3\f\3\f\3\r\3\r\7\rh\n\r\f\r\16\rk\13\r\3\16\3\16\3\17\3\17"+
		"\5\17q\n\17\3\20\6\20t\n\20\r\20\16\20u\3\21\3\21\3\21\3\21\3\21\6\21"+
		"}\n\21\r\21\16\21~\5\21\u0081\n\21\3\22\3\22\5\22\u0085\n\22\3\22\3\22"+
		"\5\22\u0089\n\22\3\22\7\22\u008c\n\22\f\22\16\22\u008f\13\22\3\23\3\23"+
		"\3\23\7\23\u0094\n\23\f\23\16\23\u0097\13\23\3\23\3\23\3\24\6\24\u009c"+
		"\n\24\r\24\16\24\u009d\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u00a6\n\25\3"+
		"\26\3\26\3\26\3\26\3\26\3\26\5\26\u00ae\n\26\3\27\3\27\3\27\5\27\u00b3"+
		"\n\27\3\27\3\27\3\30\3\30\2\2\31\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\2"+
		"\3\2\16\3\2))\3\2$$\4\2GGgg\4\2--//\4\2ZZzz\5\2\62;CHch\6\2&&C\\aac|\4"+
		"\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aa"+
		"c|\5\2\13\f\17\17\"\"\2\u00ca\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\2-\3\2\2\2\3K\3\2\2\2\5M\3\2\2\2\7O\3\2\2\2\tQ\3\2\2\2\13S\3"+
		"\2\2\2\rU\3\2\2\2\17W\3\2\2\2\21Y\3\2\2\2\23[\3\2\2\2\25]\3\2\2\2\27a"+
		"\3\2\2\2\31e\3\2\2\2\33l\3\2\2\2\35p\3\2\2\2\37s\3\2\2\2!\u0080\3\2\2"+
		"\2#\u0084\3\2\2\2%\u0090\3\2\2\2\'\u009b\3\2\2\2)\u00a5\3\2\2\2+\u00ad"+
		"\3\2\2\2-\u00b2\3\2\2\2/\u00b6\3\2\2\2\61\65\5\5\3\2\62\64\n\2\2\2\63"+
		"\62\3\2\2\2\64\67\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\67\65"+
		"\3\2\2\289\5\5\3\29;\3\2\2\2:\61\3\2\2\2;<\3\2\2\2<:\3\2\2\2<=\3\2\2\2"+
		"=L\3\2\2\2>B\5\7\4\2?A\n\3\2\2@?\3\2\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2"+
		"CE\3\2\2\2DB\3\2\2\2EF\5\7\4\2FH\3\2\2\2G>\3\2\2\2HI\3\2\2\2IG\3\2\2\2"+
		"IJ\3\2\2\2JL\3\2\2\2K:\3\2\2\2KG\3\2\2\2L\4\3\2\2\2MN\7)\2\2N\6\3\2\2"+
		"\2OP\7$\2\2P\b\3\2\2\2QR\7-\2\2R\n\3\2\2\2ST\7/\2\2T\f\3\2\2\2UV\7\61"+
		"\2\2V\16\3\2\2\2WX\7,\2\2X\20\3\2\2\2YZ\7*\2\2Z\22\3\2\2\2[\\\7+\2\2\\"+
		"\24\3\2\2\2]^\7o\2\2^_\7k\2\2_`\7p\2\2`\26\3\2\2\2ab\7o\2\2bc\7c\2\2c"+
		"d\7z\2\2d\30\3\2\2\2ei\5)\25\2fh\5+\26\2gf\3\2\2\2hk\3\2\2\2ig\3\2\2\2"+
		"ij\3\2\2\2j\32\3\2\2\2ki\3\2\2\2lm\5\37\20\2m\34\3\2\2\2nq\5!\21\2oq\5"+
		"#\22\2pn\3\2\2\2po\3\2\2\2q\36\3\2\2\2rt\5/\30\2sr\3\2\2\2tu\3\2\2\2u"+
		"s\3\2\2\2uv\3\2\2\2v \3\2\2\2wx\7\60\2\2x\u0081\5\37\20\2yz\5\37\20\2"+
		"z|\7\60\2\2{}\5/\30\2|{\3\2\2\2}~\3\2\2\2~|\3\2\2\2~\177\3\2\2\2\177\u0081"+
		"\3\2\2\2\u0080w\3\2\2\2\u0080y\3\2\2\2\u0081\"\3\2\2\2\u0082\u0085\5!"+
		"\21\2\u0083\u0085\5\33\16\2\u0084\u0082\3\2\2\2\u0084\u0083\3\2\2\2\u0085"+
		"\u0086\3\2\2\2\u0086\u0088\t\4\2\2\u0087\u0089\t\5\2\2\u0088\u0087\3\2"+
		"\2\2\u0088\u0089\3\2\2\2\u0089\u008d\3\2\2\2\u008a\u008c\5/\30\2\u008b"+
		"\u008a\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2"+
		"\2\2\u008e$\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0091\7\62\2\2\u0091\u0095"+
		"\t\6\2\2\u0092\u0094\7\62\2\2\u0093\u0092\3\2\2\2\u0094\u0097\3\2\2\2"+
		"\u0095\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0098\3\2\2\2\u0097\u0095"+
		"\3\2\2\2\u0098\u0099\5\'\24\2\u0099&\3\2\2\2\u009a\u009c\t\7\2\2\u009b"+
		"\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2"+
		"\2\2\u009e(\3\2\2\2\u009f\u00a6\t\b\2\2\u00a0\u00a1\n\t\2\2\u00a1\u00a6"+
		"\6\25\2\2\u00a2\u00a3\t\n\2\2\u00a3\u00a4\t\13\2\2\u00a4\u00a6\6\25\3"+
		"\2\u00a5\u009f\3\2\2\2\u00a5\u00a0\3\2\2\2\u00a5\u00a2\3\2\2\2\u00a6*"+
		"\3\2\2\2\u00a7\u00ae\t\f\2\2\u00a8\u00a9\n\t\2\2\u00a9\u00ae\6\26\4\2"+
		"\u00aa\u00ab\t\n\2\2\u00ab\u00ac\t\13\2\2\u00ac\u00ae\6\26\5\2\u00ad\u00a7"+
		"\3\2\2\2\u00ad\u00a8\3\2\2\2\u00ad\u00aa\3\2\2\2\u00ae,\3\2\2\2\u00af"+
		"\u00b3\t\r\2\2\u00b0\u00b1\7\17\2\2\u00b1\u00b3\7\f\2\2\u00b2\u00af\3"+
		"\2\2\2\u00b2\u00b0\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b5\b\27\2\2\u00b5"+
		".\3\2\2\2\u00b6\u00b7\4\62;\2\u00b7\60\3\2\2\2\25\2\65<BIKipu~\u0080\u0084"+
		"\u0088\u008d\u0095\u009d\u00a5\u00ad\u00b2\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}