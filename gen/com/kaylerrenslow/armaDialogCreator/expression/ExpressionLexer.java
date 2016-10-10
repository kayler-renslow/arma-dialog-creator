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
		Plus=1, Minus=2, FSlash=3, Star=4, LParen=5, RParen=6, Identifier=7, IntegerLiteral=8, 
		FloatLiteral=9, Digits=10, DecSignificand=11, DecExponent=12, HexLiteral=13, 
		HexDigits=14, Letter=15, LetterOrDigit=16, WhiteSpace=17;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"Plus", "Minus", "FSlash", "Star", "LParen", "RParen", "Identifier", "IntegerLiteral", 
		"FloatLiteral", "Digits", "DecSignificand", "DecExponent", "HexLiteral", 
		"HexDigits", "Letter", "LetterOrDigit", "WhiteSpace", "DIGIT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'+'", "'-'", "'/'", "'*'", "'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "Plus", "Minus", "FSlash", "Star", "LParen", "RParen", "Identifier", 
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
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 14:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 15:
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\23\u0086\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3"+
		"\b\7\b\66\n\b\f\b\16\b9\13\b\3\t\3\t\3\n\3\n\5\n?\n\n\3\13\6\13B\n\13"+
		"\r\13\16\13C\3\f\3\f\3\f\3\f\3\f\6\fK\n\f\r\f\16\fL\5\fO\n\f\3\r\3\r\5"+
		"\rS\n\r\3\r\3\r\5\rW\n\r\3\r\7\rZ\n\r\f\r\16\r]\13\r\3\16\3\16\3\16\7"+
		"\16b\n\16\f\16\16\16e\13\16\3\16\3\16\3\17\6\17j\n\17\r\17\16\17k\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\5\20t\n\20\3\21\3\21\3\21\3\21\3\21\3\21\5\21"+
		"|\n\21\3\22\3\22\3\22\5\22\u0081\n\22\3\22\3\22\3\23\3\23\2\2\24\3\3\5"+
		"\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21"+
		"!\22#\23%\2\3\2\f\4\2GGgg\4\2--//\4\2ZZzz\5\2\62;CHch\6\2&&C\\aac|\4\2"+
		"\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aac|"+
		"\5\2\13\f\17\17\"\"\u0093\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3\'\3\2\2\2\5)\3\2\2\2\7+\3\2\2\2\t-\3\2"+
		"\2\2\13/\3\2\2\2\r\61\3\2\2\2\17\63\3\2\2\2\21:\3\2\2\2\23>\3\2\2\2\25"+
		"A\3\2\2\2\27N\3\2\2\2\31R\3\2\2\2\33^\3\2\2\2\35i\3\2\2\2\37s\3\2\2\2"+
		"!{\3\2\2\2#\u0080\3\2\2\2%\u0084\3\2\2\2\'(\7-\2\2(\4\3\2\2\2)*\7/\2\2"+
		"*\6\3\2\2\2+,\7\61\2\2,\b\3\2\2\2-.\7,\2\2.\n\3\2\2\2/\60\7*\2\2\60\f"+
		"\3\2\2\2\61\62\7+\2\2\62\16\3\2\2\2\63\67\5\37\20\2\64\66\5!\21\2\65\64"+
		"\3\2\2\2\669\3\2\2\2\67\65\3\2\2\2\678\3\2\2\28\20\3\2\2\29\67\3\2\2\2"+
		":;\5\25\13\2;\22\3\2\2\2<?\5\27\f\2=?\5\31\r\2><\3\2\2\2>=\3\2\2\2?\24"+
		"\3\2\2\2@B\5%\23\2A@\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\26\3\2\2\2"+
		"EF\7\60\2\2FO\5\25\13\2GH\5\25\13\2HJ\7\60\2\2IK\5%\23\2JI\3\2\2\2KL\3"+
		"\2\2\2LJ\3\2\2\2LM\3\2\2\2MO\3\2\2\2NE\3\2\2\2NG\3\2\2\2O\30\3\2\2\2P"+
		"S\5\27\f\2QS\5\21\t\2RP\3\2\2\2RQ\3\2\2\2ST\3\2\2\2TV\t\2\2\2UW\t\3\2"+
		"\2VU\3\2\2\2VW\3\2\2\2W[\3\2\2\2XZ\5%\23\2YX\3\2\2\2Z]\3\2\2\2[Y\3\2\2"+
		"\2[\\\3\2\2\2\\\32\3\2\2\2][\3\2\2\2^_\7\62\2\2_c\t\4\2\2`b\7\62\2\2a"+
		"`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2\2df\3\2\2\2ec\3\2\2\2fg\5\35\17"+
		"\2g\34\3\2\2\2hj\t\5\2\2ih\3\2\2\2jk\3\2\2\2ki\3\2\2\2kl\3\2\2\2l\36\3"+
		"\2\2\2mt\t\6\2\2no\n\7\2\2ot\6\20\2\2pq\t\b\2\2qr\t\t\2\2rt\6\20\3\2s"+
		"m\3\2\2\2sn\3\2\2\2sp\3\2\2\2t \3\2\2\2u|\t\n\2\2vw\n\7\2\2w|\6\21\4\2"+
		"xy\t\b\2\2yz\t\t\2\2z|\6\21\5\2{u\3\2\2\2{v\3\2\2\2{x\3\2\2\2|\"\3\2\2"+
		"\2}\u0081\t\13\2\2~\177\7\17\2\2\177\u0081\7\f\2\2\u0080}\3\2\2\2\u0080"+
		"~\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0083\b\22\2\2\u0083$\3\2\2\2\u0084"+
		"\u0085\4\62;\2\u0085&\3\2\2\2\20\2\67>CLNRV[cks{\u0080\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}