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
		Count=24, For=25, From=26, To=27, Step=28, Do=29, EqEq=30, NotEq=31, Lt=32, 
		LtEq=33, Gt=34, GtEq=35, Equal=36, Semicolon=37, Identifier=38, IntegerLiteral=39, 
		FloatLiteral=40, Digits=41, DecSignificand=42, DecExponent=43, HexLiteral=44, 
		HexDigits=45, Letter=46, LetterOrDigit=47, WhiteSpace=48, Comment=49;
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
		"For", "From", "To", "Step", "Do", "EqEq", "NotEq", "Lt", "LtEq", "Gt", 
		"GtEq", "Equal", "Semicolon", "Identifier", "IntegerLiteral", "FloatLiteral", 
		"Digits", "DecSignificand", "DecExponent", "HexLiteral", "HexDigits", 
		"Letter", "LetterOrDigit", "WhiteSpace", "Comment", "DIGIT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'''", "'\"'", "'{'", "'}'", "'['", "']'", "'+'", "'-'", "'/'", 
		"'%'", "'^'", "'*'", "'('", "')'", "','", "'min'", "'max'", "'if'", "'then'", 
		"'else'", "'exitWith'", "'select'", "'count'", "'for'", "'from'", "'to'", 
		"'step'", "'do'", "'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'='", 
		"';'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Quote", "DQuote", "LCurly", "RCurly", "LBracket", "RBracket", 
		"Plus", "Minus", "FSlash", "Perc", "Caret", "Star", "LParen", "RParen", 
		"Comma", "Min", "Max", "If", "Then", "Else", "ExitWith", "Select", "Count", 
		"For", "From", "To", "Step", "Do", "EqEq", "NotEq", "Lt", "LtEq", "Gt", 
		"GtEq", "Equal", "Semicolon", "Identifier", "IntegerLiteral", "FloatLiteral", 
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
		case 45:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 46:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\63\u015e\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\3\2"+
		"\3\2\7\2j\n\2\f\2\16\2m\13\2\3\2\3\2\6\2q\n\2\r\2\16\2r\3\2\3\2\7\2w\n"+
		"\2\f\2\16\2z\13\2\3\2\3\2\6\2~\n\2\r\2\16\2\177\5\2\u0082\n\2\3\3\3\3"+
		"\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f"+
		"\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3"+
		"\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3"+
		"\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3"+
		"\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3"+
		"\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3"+
		"\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\3\"\3\"\3\"\3#\3#\3$"+
		"\3$\3$\3%\3%\3&\3&\3\'\3\'\7\'\u00f7\n\'\f\'\16\'\u00fa\13\'\3(\3(\3)"+
		"\3)\5)\u0100\n)\3*\6*\u0103\n*\r*\16*\u0104\3+\3+\3+\3+\3+\6+\u010c\n"+
		"+\r+\16+\u010d\5+\u0110\n+\3,\3,\5,\u0114\n,\3,\3,\5,\u0118\n,\3,\7,\u011b"+
		"\n,\f,\16,\u011e\13,\3-\3-\3-\7-\u0123\n-\f-\16-\u0126\13-\3-\3-\3.\6"+
		".\u012b\n.\r.\16.\u012c\3/\3/\3/\3/\3/\3/\5/\u0135\n/\3\60\3\60\3\60\3"+
		"\60\3\60\3\60\5\60\u013d\n\60\3\61\3\61\3\61\5\61\u0142\n\61\3\61\3\61"+
		"\3\62\3\62\3\62\3\62\6\62\u014a\n\62\r\62\16\62\u014b\3\62\3\62\3\62\3"+
		"\62\7\62\u0152\n\62\f\62\16\62\u0155\13\62\3\62\3\62\5\62\u0159\n\62\3"+
		"\62\3\62\3\63\3\63\3\u0153\2\64\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31"+
		"\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60"+
		"_\61a\62c\63e\2\3\2\17\3\2))\3\2$$\4\2GGgg\4\2--//\4\2ZZzz\5\2\62;CHc"+
		"h\6\2&&C\\aac|\4\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001"+
		"\7\2&&\62;C\\aac|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\2\u0173\2\3\3\2\2\2"+
		"\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"+
		"\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"+
		"\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2"+
		"\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2"+
		"\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2"+
		"\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2"+
		"\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W"+
		"\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2"+
		"\2\2\3\u0081\3\2\2\2\5\u0083\3\2\2\2\7\u0085\3\2\2\2\t\u0087\3\2\2\2\13"+
		"\u0089\3\2\2\2\r\u008b\3\2\2\2\17\u008d\3\2\2\2\21\u008f\3\2\2\2\23\u0091"+
		"\3\2\2\2\25\u0093\3\2\2\2\27\u0095\3\2\2\2\31\u0097\3\2\2\2\33\u0099\3"+
		"\2\2\2\35\u009b\3\2\2\2\37\u009d\3\2\2\2!\u009f\3\2\2\2#\u00a1\3\2\2\2"+
		"%\u00a5\3\2\2\2\'\u00a9\3\2\2\2)\u00ac\3\2\2\2+\u00b1\3\2\2\2-\u00b6\3"+
		"\2\2\2/\u00bf\3\2\2\2\61\u00c6\3\2\2\2\63\u00cc\3\2\2\2\65\u00d0\3\2\2"+
		"\2\67\u00d5\3\2\2\29\u00d8\3\2\2\2;\u00dd\3\2\2\2=\u00e0\3\2\2\2?\u00e3"+
		"\3\2\2\2A\u00e6\3\2\2\2C\u00e8\3\2\2\2E\u00eb\3\2\2\2G\u00ed\3\2\2\2I"+
		"\u00f0\3\2\2\2K\u00f2\3\2\2\2M\u00f4\3\2\2\2O\u00fb\3\2\2\2Q\u00ff\3\2"+
		"\2\2S\u0102\3\2\2\2U\u010f\3\2\2\2W\u0113\3\2\2\2Y\u011f\3\2\2\2[\u012a"+
		"\3\2\2\2]\u0134\3\2\2\2_\u013c\3\2\2\2a\u0141\3\2\2\2c\u0158\3\2\2\2e"+
		"\u015c\3\2\2\2gk\5\5\3\2hj\n\2\2\2ih\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2"+
		"\2\2ln\3\2\2\2mk\3\2\2\2no\5\5\3\2oq\3\2\2\2pg\3\2\2\2qr\3\2\2\2rp\3\2"+
		"\2\2rs\3\2\2\2s\u0082\3\2\2\2tx\5\7\4\2uw\n\3\2\2vu\3\2\2\2wz\3\2\2\2"+
		"xv\3\2\2\2xy\3\2\2\2y{\3\2\2\2zx\3\2\2\2{|\5\7\4\2|~\3\2\2\2}t\3\2\2\2"+
		"~\177\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080\u0082\3\2\2\2\u0081"+
		"p\3\2\2\2\u0081}\3\2\2\2\u0082\4\3\2\2\2\u0083\u0084\7)\2\2\u0084\6\3"+
		"\2\2\2\u0085\u0086\7$\2\2\u0086\b\3\2\2\2\u0087\u0088\7}\2\2\u0088\n\3"+
		"\2\2\2\u0089\u008a\7\177\2\2\u008a\f\3\2\2\2\u008b\u008c\7]\2\2\u008c"+
		"\16\3\2\2\2\u008d\u008e\7_\2\2\u008e\20\3\2\2\2\u008f\u0090\7-\2\2\u0090"+
		"\22\3\2\2\2\u0091\u0092\7/\2\2\u0092\24\3\2\2\2\u0093\u0094\7\61\2\2\u0094"+
		"\26\3\2\2\2\u0095\u0096\7\'\2\2\u0096\30\3\2\2\2\u0097\u0098\7`\2\2\u0098"+
		"\32\3\2\2\2\u0099\u009a\7,\2\2\u009a\34\3\2\2\2\u009b\u009c\7*\2\2\u009c"+
		"\36\3\2\2\2\u009d\u009e\7+\2\2\u009e \3\2\2\2\u009f\u00a0\7.\2\2\u00a0"+
		"\"\3\2\2\2\u00a1\u00a2\7o\2\2\u00a2\u00a3\7k\2\2\u00a3\u00a4\7p\2\2\u00a4"+
		"$\3\2\2\2\u00a5\u00a6\7o\2\2\u00a6\u00a7\7c\2\2\u00a7\u00a8\7z\2\2\u00a8"+
		"&\3\2\2\2\u00a9\u00aa\7k\2\2\u00aa\u00ab\7h\2\2\u00ab(\3\2\2\2\u00ac\u00ad"+
		"\7v\2\2\u00ad\u00ae\7j\2\2\u00ae\u00af\7g\2\2\u00af\u00b0\7p\2\2\u00b0"+
		"*\3\2\2\2\u00b1\u00b2\7g\2\2\u00b2\u00b3\7n\2\2\u00b3\u00b4\7u\2\2\u00b4"+
		"\u00b5\7g\2\2\u00b5,\3\2\2\2\u00b6\u00b7\7g\2\2\u00b7\u00b8\7z\2\2\u00b8"+
		"\u00b9\7k\2\2\u00b9\u00ba\7v\2\2\u00ba\u00bb\7Y\2\2\u00bb\u00bc\7k\2\2"+
		"\u00bc\u00bd\7v\2\2\u00bd\u00be\7j\2\2\u00be.\3\2\2\2\u00bf\u00c0\7u\2"+
		"\2\u00c0\u00c1\7g\2\2\u00c1\u00c2\7n\2\2\u00c2\u00c3\7g\2\2\u00c3\u00c4"+
		"\7e\2\2\u00c4\u00c5\7v\2\2\u00c5\60\3\2\2\2\u00c6\u00c7\7e\2\2\u00c7\u00c8"+
		"\7q\2\2\u00c8\u00c9\7w\2\2\u00c9\u00ca\7p\2\2\u00ca\u00cb\7v\2\2\u00cb"+
		"\62\3\2\2\2\u00cc\u00cd\7h\2\2\u00cd\u00ce\7q\2\2\u00ce\u00cf\7t\2\2\u00cf"+
		"\64\3\2\2\2\u00d0\u00d1\7h\2\2\u00d1\u00d2\7t\2\2\u00d2\u00d3\7q\2\2\u00d3"+
		"\u00d4\7o\2\2\u00d4\66\3\2\2\2\u00d5\u00d6\7v\2\2\u00d6\u00d7\7q\2\2\u00d7"+
		"8\3\2\2\2\u00d8\u00d9\7u\2\2\u00d9\u00da\7v\2\2\u00da\u00db\7g\2\2\u00db"+
		"\u00dc\7r\2\2\u00dc:\3\2\2\2\u00dd\u00de\7f\2\2\u00de\u00df\7q\2\2\u00df"+
		"<\3\2\2\2\u00e0\u00e1\7?\2\2\u00e1\u00e2\7?\2\2\u00e2>\3\2\2\2\u00e3\u00e4"+
		"\7#\2\2\u00e4\u00e5\7?\2\2\u00e5@\3\2\2\2\u00e6\u00e7\7>\2\2\u00e7B\3"+
		"\2\2\2\u00e8\u00e9\7>\2\2\u00e9\u00ea\7?\2\2\u00eaD\3\2\2\2\u00eb\u00ec"+
		"\7@\2\2\u00ecF\3\2\2\2\u00ed\u00ee\7@\2\2\u00ee\u00ef\7?\2\2\u00efH\3"+
		"\2\2\2\u00f0\u00f1\7?\2\2\u00f1J\3\2\2\2\u00f2\u00f3\7=\2\2\u00f3L\3\2"+
		"\2\2\u00f4\u00f8\5]/\2\u00f5\u00f7\5_\60\2\u00f6\u00f5\3\2\2\2\u00f7\u00fa"+
		"\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9N\3\2\2\2\u00fa"+
		"\u00f8\3\2\2\2\u00fb\u00fc\5S*\2\u00fcP\3\2\2\2\u00fd\u0100\5U+\2\u00fe"+
		"\u0100\5W,\2\u00ff\u00fd\3\2\2\2\u00ff\u00fe\3\2\2\2\u0100R\3\2\2\2\u0101"+
		"\u0103\5e\63\2\u0102\u0101\3\2\2\2\u0103\u0104\3\2\2\2\u0104\u0102\3\2"+
		"\2\2\u0104\u0105\3\2\2\2\u0105T\3\2\2\2\u0106\u0107\7\60\2\2\u0107\u0110"+
		"\5S*\2\u0108\u0109\5S*\2\u0109\u010b\7\60\2\2\u010a\u010c\5e\63\2\u010b"+
		"\u010a\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u010b\3\2\2\2\u010d\u010e\3\2"+
		"\2\2\u010e\u0110\3\2\2\2\u010f\u0106\3\2\2\2\u010f\u0108\3\2\2\2\u0110"+
		"V\3\2\2\2\u0111\u0114\5U+\2\u0112\u0114\5O(\2\u0113\u0111\3\2\2\2\u0113"+
		"\u0112\3\2\2\2\u0114\u0115\3\2\2\2\u0115\u0117\t\4\2\2\u0116\u0118\t\5"+
		"\2\2\u0117\u0116\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u011c\3\2\2\2\u0119"+
		"\u011b\5e\63\2\u011a\u0119\3\2\2\2\u011b\u011e\3\2\2\2\u011c\u011a\3\2"+
		"\2\2\u011c\u011d\3\2\2\2\u011dX\3\2\2\2\u011e\u011c\3\2\2\2\u011f\u0120"+
		"\7\62\2\2\u0120\u0124\t\6\2\2\u0121\u0123\7\62\2\2\u0122\u0121\3\2\2\2"+
		"\u0123\u0126\3\2\2\2\u0124\u0122\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0127"+
		"\3\2\2\2\u0126\u0124\3\2\2\2\u0127\u0128\5[.\2\u0128Z\3\2\2\2\u0129\u012b"+
		"\t\7\2\2\u012a\u0129\3\2\2\2\u012b\u012c\3\2\2\2\u012c\u012a\3\2\2\2\u012c"+
		"\u012d\3\2\2\2\u012d\\\3\2\2\2\u012e\u0135\t\b\2\2\u012f\u0130\n\t\2\2"+
		"\u0130\u0135\6/\2\2\u0131\u0132\t\n\2\2\u0132\u0133\t\13\2\2\u0133\u0135"+
		"\6/\3\2\u0134\u012e\3\2\2\2\u0134\u012f\3\2\2\2\u0134\u0131\3\2\2\2\u0135"+
		"^\3\2\2\2\u0136\u013d\t\f\2\2\u0137\u0138\n\t\2\2\u0138\u013d\6\60\4\2"+
		"\u0139\u013a\t\n\2\2\u013a\u013b\t\13\2\2\u013b\u013d\6\60\5\2\u013c\u0136"+
		"\3\2\2\2\u013c\u0137\3\2\2\2\u013c\u0139\3\2\2\2\u013d`\3\2\2\2\u013e"+
		"\u0142\t\r\2\2\u013f\u0140\7\17\2\2\u0140\u0142\7\f\2\2\u0141\u013e\3"+
		"\2\2\2\u0141\u013f\3\2\2\2\u0142\u0143\3\2\2\2\u0143\u0144\b\61\2\2\u0144"+
		"b\3\2\2\2\u0145\u0146\7\61\2\2\u0146\u0147\7\61\2\2\u0147\u0149\3\2\2"+
		"\2\u0148\u014a\n\16\2\2\u0149\u0148\3\2\2\2\u014a\u014b\3\2\2\2\u014b"+
		"\u0149\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u0159\3\2\2\2\u014d\u014e\7\61"+
		"\2\2\u014e\u014f\7,\2\2\u014f\u0153\3\2\2\2\u0150\u0152\13\2\2\2\u0151"+
		"\u0150\3\2\2\2\u0152\u0155\3\2\2\2\u0153\u0154\3\2\2\2\u0153\u0151\3\2"+
		"\2\2\u0154\u0156\3\2\2\2\u0155\u0153\3\2\2\2\u0156\u0157\7,\2\2\u0157"+
		"\u0159\7\61\2\2\u0158\u0145\3\2\2\2\u0158\u014d\3\2\2\2\u0159\u015a\3"+
		"\2\2\2\u015a\u015b\b\62\2\2\u015bd\3\2\2\2\u015c\u015d\4\62;\2\u015df"+
		"\3\2\2\2\30\2krx\177\u0081\u00f8\u00ff\u0104\u010d\u010f\u0113\u0117\u011c"+
		"\u0124\u012c\u0134\u013c\u0141\u014b\u0153\u0158\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}