// Generated from D:/Archive/Intellij Files/Arma Tools/Arma Dialog Creator/src/com/kaylerrenslow/armaDialogCreator/expression\Expression.g4 by ANTLR 4.5.3
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
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Plus=1, Minus=2, FSlash=3, Star=4, LParen=5, RParen=6, Min=7, Max=8, Identifier=9, 
		IntegerLiteral=10, FloatLiteral=11, Digits=12, DecSignificand=13, DecExponent=14, 
		HexLiteral=15, HexDigits=16, Letter=17, LetterOrDigit=18, WhiteSpace=19;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"Plus", "Minus", "FSlash", "Star", "LParen", "RParen", "Min", "Max", "Identifier", 
		"IntegerLiteral", "FloatLiteral", "Digits", "DecSignificand", "DecExponent", 
		"HexLiteral", "HexDigits", "Letter", "LetterOrDigit", "WhiteSpace", "DIGIT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'+'", "'-'", "'/'", "'*'", "'('", "')'", "'min'", "'max'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "Plus", "Minus", "FSlash", "Star", "LParen", "RParen", "Min", "Max", 
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
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 16:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 17:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\25\u0092\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\7\nB\n\n\f\n\16"+
		"\nE\13\n\3\13\3\13\3\f\3\f\5\fK\n\f\3\r\6\rN\n\r\r\r\16\rO\3\16\3\16\3"+
		"\16\3\16\3\16\6\16W\n\16\r\16\16\16X\5\16[\n\16\3\17\3\17\5\17_\n\17\3"+
		"\17\3\17\5\17c\n\17\3\17\7\17f\n\17\f\17\16\17i\13\17\3\20\3\20\3\20\7"+
		"\20n\n\20\f\20\16\20q\13\20\3\20\3\20\3\21\6\21v\n\21\r\21\16\21w\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\5\22\u0080\n\22\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\5\23\u0088\n\23\3\24\3\24\3\24\5\24\u008d\n\24\3\24\3\24\3\25\3\25\2"+
		"\2\26\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\2\3\2\f\4\2GGgg\4\2--//\4\2ZZzz\5\2\62;CH"+
		"ch\6\2&&C\\aac|\4\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001"+
		"\7\2&&\62;C\\aac|\5\2\13\f\17\17\"\"\u009f\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2"+
		"\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2"+
		"\3+\3\2\2\2\5-\3\2\2\2\7/\3\2\2\2\t\61\3\2\2\2\13\63\3\2\2\2\r\65\3\2"+
		"\2\2\17\67\3\2\2\2\21;\3\2\2\2\23?\3\2\2\2\25F\3\2\2\2\27J\3\2\2\2\31"+
		"M\3\2\2\2\33Z\3\2\2\2\35^\3\2\2\2\37j\3\2\2\2!u\3\2\2\2#\177\3\2\2\2%"+
		"\u0087\3\2\2\2\'\u008c\3\2\2\2)\u0090\3\2\2\2+,\7-\2\2,\4\3\2\2\2-.\7"+
		"/\2\2.\6\3\2\2\2/\60\7\61\2\2\60\b\3\2\2\2\61\62\7,\2\2\62\n\3\2\2\2\63"+
		"\64\7*\2\2\64\f\3\2\2\2\65\66\7+\2\2\66\16\3\2\2\2\678\7o\2\289\7k\2\2"+
		"9:\7p\2\2:\20\3\2\2\2;<\7o\2\2<=\7c\2\2=>\7z\2\2>\22\3\2\2\2?C\5#\22\2"+
		"@B\5%\23\2A@\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\24\3\2\2\2EC\3\2\2"+
		"\2FG\5\31\r\2G\26\3\2\2\2HK\5\33\16\2IK\5\35\17\2JH\3\2\2\2JI\3\2\2\2"+
		"K\30\3\2\2\2LN\5)\25\2ML\3\2\2\2NO\3\2\2\2OM\3\2\2\2OP\3\2\2\2P\32\3\2"+
		"\2\2QR\7\60\2\2R[\5\31\r\2ST\5\31\r\2TV\7\60\2\2UW\5)\25\2VU\3\2\2\2W"+
		"X\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y[\3\2\2\2ZQ\3\2\2\2ZS\3\2\2\2[\34\3\2\2"+
		"\2\\_\5\33\16\2]_\5\25\13\2^\\\3\2\2\2^]\3\2\2\2_`\3\2\2\2`b\t\2\2\2a"+
		"c\t\3\2\2ba\3\2\2\2bc\3\2\2\2cg\3\2\2\2df\5)\25\2ed\3\2\2\2fi\3\2\2\2"+
		"ge\3\2\2\2gh\3\2\2\2h\36\3\2\2\2ig\3\2\2\2jk\7\62\2\2ko\t\4\2\2ln\7\62"+
		"\2\2ml\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2pr\3\2\2\2qo\3\2\2\2rs\5!"+
		"\21\2s \3\2\2\2tv\t\5\2\2ut\3\2\2\2vw\3\2\2\2wu\3\2\2\2wx\3\2\2\2x\"\3"+
		"\2\2\2y\u0080\t\6\2\2z{\n\7\2\2{\u0080\6\22\2\2|}\t\b\2\2}~\t\t\2\2~\u0080"+
		"\6\22\3\2\177y\3\2\2\2\177z\3\2\2\2\177|\3\2\2\2\u0080$\3\2\2\2\u0081"+
		"\u0088\t\n\2\2\u0082\u0083\n\7\2\2\u0083\u0088\6\23\4\2\u0084\u0085\t"+
		"\b\2\2\u0085\u0086\t\t\2\2\u0086\u0088\6\23\5\2\u0087\u0081\3\2\2\2\u0087"+
		"\u0082\3\2\2\2\u0087\u0084\3\2\2\2\u0088&\3\2\2\2\u0089\u008d\t\13\2\2"+
		"\u008a\u008b\7\17\2\2\u008b\u008d\7\f\2\2\u008c\u0089\3\2\2\2\u008c\u008a"+
		"\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008f\b\24\2\2\u008f(\3\2\2\2\u0090"+
		"\u0091\4\62;\2\u0091*\3\2\2\2\20\2CJOXZ^bgow\177\u0087\u008c\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}