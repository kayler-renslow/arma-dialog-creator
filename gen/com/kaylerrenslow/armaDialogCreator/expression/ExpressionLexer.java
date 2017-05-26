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
		Max=16, If=17, Then=18, Else=19, ExitWith=20, Select=21, For=22, From=23, 
		To=24, Step=25, Do=26, EqEq=27, NotEq=28, Lt=29, LtEq=30, Gt=31, GtEq=32, 
		Equal=33, Semicolon=34, Identifier=35, IntegerLiteral=36, FloatLiteral=37, 
		Digits=38, DecSignificand=39, DecExponent=40, HexLiteral=41, HexDigits=42, 
		Letter=43, LetterOrDigit=44, WhiteSpace=45;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"String", "Quote", "DQuote", "LCurly", "RCurly", "LBracket", "RBracket", 
		"Plus", "Minus", "FSlash", "Star", "LParen", "RParen", "Comma", "Min", 
		"Max", "If", "Then", "Else", "ExitWith", "Select", "For", "From", "To", 
		"Step", "Do", "EqEq", "NotEq", "Lt", "LtEq", "Gt", "GtEq", "Equal", "Semicolon", 
		"Identifier", "IntegerLiteral", "FloatLiteral", "Digits", "DecSignificand", 
		"DecExponent", "HexLiteral", "HexDigits", "Letter", "LetterOrDigit", "WhiteSpace", 
		"DIGIT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'''", "'\"'", "'{'", "'}'", "'['", "']'", "'+'", "'-'", "'/'", 
		"'*'", "'('", "')'", "','", "'min'", "'max'", "'if'", "'then'", "'else'", 
		"'exitWith'", "'select'", "'for'", "'from'", "'to'", "'step'", "'do'", 
		"'=='", "'!='", "'<'", "'<='", "'>'", "'>='", "'='", "';'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "String", "Quote", "DQuote", "LCurly", "RCurly", "LBracket", "RBracket", 
		"Plus", "Minus", "FSlash", "Star", "LParen", "RParen", "Comma", "Min", 
		"Max", "If", "Then", "Else", "ExitWith", "Select", "For", "From", "To", 
		"Step", "Do", "EqEq", "NotEq", "Lt", "LtEq", "Gt", "GtEq", "Equal", "Semicolon", 
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
		case 42:
			return Letter_sempred((RuleContext)_localctx, predIndex);
		case 43:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2/\u0135\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\3\2\3\2\7\2b\n\2\f\2\16\2e\13\2\3\2\3\2\6\2i\n"+
		"\2\r\2\16\2j\3\2\3\2\7\2o\n\2\f\2\16\2r\13\2\3\2\3\2\6\2v\n\2\r\2\16\2"+
		"w\5\2z\n\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n"+
		"\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3\24"+
		"\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30"+
		"\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34"+
		"\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3\37\3 \3 \3!\3!\3!\3\"\3\"\3#\3#"+
		"\3$\3$\7$\u00e5\n$\f$\16$\u00e8\13$\3%\3%\3&\3&\5&\u00ee\n&\3\'\6\'\u00f1"+
		"\n\'\r\'\16\'\u00f2\3(\3(\3(\3(\3(\6(\u00fa\n(\r(\16(\u00fb\5(\u00fe\n"+
		"(\3)\3)\5)\u0102\n)\3)\3)\5)\u0106\n)\3)\7)\u0109\n)\f)\16)\u010c\13)"+
		"\3*\3*\3*\7*\u0111\n*\f*\16*\u0114\13*\3*\3*\3+\6+\u0119\n+\r+\16+\u011a"+
		"\3,\3,\3,\3,\3,\3,\5,\u0123\n,\3-\3-\3-\3-\3-\3-\5-\u012b\n-\3.\3.\3."+
		"\5.\u0130\n.\3.\3.\3/\3/\2\2\60\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31"+
		"\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\2"+
		"\3\2\16\3\2))\3\2$$\4\2GGgg\4\2--//\4\2ZZzz\5\2\62;CHch\6\2&&C\\aac|\4"+
		"\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aa"+
		"c|\5\2\13\f\17\17\"\"\2\u0147\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2"+
		"\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2"+
		"C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3"+
		"\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2"+
		"\2\3y\3\2\2\2\5{\3\2\2\2\7}\3\2\2\2\t\177\3\2\2\2\13\u0081\3\2\2\2\r\u0083"+
		"\3\2\2\2\17\u0085\3\2\2\2\21\u0087\3\2\2\2\23\u0089\3\2\2\2\25\u008b\3"+
		"\2\2\2\27\u008d\3\2\2\2\31\u008f\3\2\2\2\33\u0091\3\2\2\2\35\u0093\3\2"+
		"\2\2\37\u0095\3\2\2\2!\u0099\3\2\2\2#\u009d\3\2\2\2%\u00a0\3\2\2\2\'\u00a5"+
		"\3\2\2\2)\u00aa\3\2\2\2+\u00b3\3\2\2\2-\u00ba\3\2\2\2/\u00be\3\2\2\2\61"+
		"\u00c3\3\2\2\2\63\u00c6\3\2\2\2\65\u00cb\3\2\2\2\67\u00ce\3\2\2\29\u00d1"+
		"\3\2\2\2;\u00d4\3\2\2\2=\u00d6\3\2\2\2?\u00d9\3\2\2\2A\u00db\3\2\2\2C"+
		"\u00de\3\2\2\2E\u00e0\3\2\2\2G\u00e2\3\2\2\2I\u00e9\3\2\2\2K\u00ed\3\2"+
		"\2\2M\u00f0\3\2\2\2O\u00fd\3\2\2\2Q\u0101\3\2\2\2S\u010d\3\2\2\2U\u0118"+
		"\3\2\2\2W\u0122\3\2\2\2Y\u012a\3\2\2\2[\u012f\3\2\2\2]\u0133\3\2\2\2_"+
		"c\5\5\3\2`b\n\2\2\2a`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2\2df\3\2\2\2"+
		"ec\3\2\2\2fg\5\5\3\2gi\3\2\2\2h_\3\2\2\2ij\3\2\2\2jh\3\2\2\2jk\3\2\2\2"+
		"kz\3\2\2\2lp\5\7\4\2mo\n\3\2\2nm\3\2\2\2or\3\2\2\2pn\3\2\2\2pq\3\2\2\2"+
		"qs\3\2\2\2rp\3\2\2\2st\5\7\4\2tv\3\2\2\2ul\3\2\2\2vw\3\2\2\2wu\3\2\2\2"+
		"wx\3\2\2\2xz\3\2\2\2yh\3\2\2\2yu\3\2\2\2z\4\3\2\2\2{|\7)\2\2|\6\3\2\2"+
		"\2}~\7$\2\2~\b\3\2\2\2\177\u0080\7}\2\2\u0080\n\3\2\2\2\u0081\u0082\7"+
		"\177\2\2\u0082\f\3\2\2\2\u0083\u0084\7]\2\2\u0084\16\3\2\2\2\u0085\u0086"+
		"\7_\2\2\u0086\20\3\2\2\2\u0087\u0088\7-\2\2\u0088\22\3\2\2\2\u0089\u008a"+
		"\7/\2\2\u008a\24\3\2\2\2\u008b\u008c\7\61\2\2\u008c\26\3\2\2\2\u008d\u008e"+
		"\7,\2\2\u008e\30\3\2\2\2\u008f\u0090\7*\2\2\u0090\32\3\2\2\2\u0091\u0092"+
		"\7+\2\2\u0092\34\3\2\2\2\u0093\u0094\7.\2\2\u0094\36\3\2\2\2\u0095\u0096"+
		"\7o\2\2\u0096\u0097\7k\2\2\u0097\u0098\7p\2\2\u0098 \3\2\2\2\u0099\u009a"+
		"\7o\2\2\u009a\u009b\7c\2\2\u009b\u009c\7z\2\2\u009c\"\3\2\2\2\u009d\u009e"+
		"\7k\2\2\u009e\u009f\7h\2\2\u009f$\3\2\2\2\u00a0\u00a1\7v\2\2\u00a1\u00a2"+
		"\7j\2\2\u00a2\u00a3\7g\2\2\u00a3\u00a4\7p\2\2\u00a4&\3\2\2\2\u00a5\u00a6"+
		"\7g\2\2\u00a6\u00a7\7n\2\2\u00a7\u00a8\7u\2\2\u00a8\u00a9\7g\2\2\u00a9"+
		"(\3\2\2\2\u00aa\u00ab\7g\2\2\u00ab\u00ac\7z\2\2\u00ac\u00ad\7k\2\2\u00ad"+
		"\u00ae\7v\2\2\u00ae\u00af\7Y\2\2\u00af\u00b0\7k\2\2\u00b0\u00b1\7v\2\2"+
		"\u00b1\u00b2\7j\2\2\u00b2*\3\2\2\2\u00b3\u00b4\7u\2\2\u00b4\u00b5\7g\2"+
		"\2\u00b5\u00b6\7n\2\2\u00b6\u00b7\7g\2\2\u00b7\u00b8\7e\2\2\u00b8\u00b9"+
		"\7v\2\2\u00b9,\3\2\2\2\u00ba\u00bb\7h\2\2\u00bb\u00bc\7q\2\2\u00bc\u00bd"+
		"\7t\2\2\u00bd.\3\2\2\2\u00be\u00bf\7h\2\2\u00bf\u00c0\7t\2\2\u00c0\u00c1"+
		"\7q\2\2\u00c1\u00c2\7o\2\2\u00c2\60\3\2\2\2\u00c3\u00c4\7v\2\2\u00c4\u00c5"+
		"\7q\2\2\u00c5\62\3\2\2\2\u00c6\u00c7\7u\2\2\u00c7\u00c8\7v\2\2\u00c8\u00c9"+
		"\7g\2\2\u00c9\u00ca\7r\2\2\u00ca\64\3\2\2\2\u00cb\u00cc\7f\2\2\u00cc\u00cd"+
		"\7q\2\2\u00cd\66\3\2\2\2\u00ce\u00cf\7?\2\2\u00cf\u00d0\7?\2\2\u00d08"+
		"\3\2\2\2\u00d1\u00d2\7#\2\2\u00d2\u00d3\7?\2\2\u00d3:\3\2\2\2\u00d4\u00d5"+
		"\7>\2\2\u00d5<\3\2\2\2\u00d6\u00d7\7>\2\2\u00d7\u00d8\7?\2\2\u00d8>\3"+
		"\2\2\2\u00d9\u00da\7@\2\2\u00da@\3\2\2\2\u00db\u00dc\7@\2\2\u00dc\u00dd"+
		"\7?\2\2\u00ddB\3\2\2\2\u00de\u00df\7?\2\2\u00dfD\3\2\2\2\u00e0\u00e1\7"+
		"=\2\2\u00e1F\3\2\2\2\u00e2\u00e6\5W,\2\u00e3\u00e5\5Y-\2\u00e4\u00e3\3"+
		"\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7"+
		"H\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00ea\5M\'\2\u00eaJ\3\2\2\2\u00eb"+
		"\u00ee\5O(\2\u00ec\u00ee\5Q)\2\u00ed\u00eb\3\2\2\2\u00ed\u00ec\3\2\2\2"+
		"\u00eeL\3\2\2\2\u00ef\u00f1\5]/\2\u00f0\u00ef\3\2\2\2\u00f1\u00f2\3\2"+
		"\2\2\u00f2\u00f0\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3N\3\2\2\2\u00f4\u00f5"+
		"\7\60\2\2\u00f5\u00fe\5M\'\2\u00f6\u00f7\5M\'\2\u00f7\u00f9\7\60\2\2\u00f8"+
		"\u00fa\5]/\2\u00f9\u00f8\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00f9\3\2\2"+
		"\2\u00fb\u00fc\3\2\2\2\u00fc\u00fe\3\2\2\2\u00fd\u00f4\3\2\2\2\u00fd\u00f6"+
		"\3\2\2\2\u00feP\3\2\2\2\u00ff\u0102\5O(\2\u0100\u0102\5I%\2\u0101\u00ff"+
		"\3\2\2\2\u0101\u0100\3\2\2\2\u0102\u0103\3\2\2\2\u0103\u0105\t\4\2\2\u0104"+
		"\u0106\t\5\2\2\u0105\u0104\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u010a\3\2"+
		"\2\2\u0107\u0109\5]/\2\u0108\u0107\3\2\2\2\u0109\u010c\3\2\2\2\u010a\u0108"+
		"\3\2\2\2\u010a\u010b\3\2\2\2\u010bR\3\2\2\2\u010c\u010a\3\2\2\2\u010d"+
		"\u010e\7\62\2\2\u010e\u0112\t\6\2\2\u010f\u0111\7\62\2\2\u0110\u010f\3"+
		"\2\2\2\u0111\u0114\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113"+
		"\u0115\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u0116\5U+\2\u0116T\3\2\2\2\u0117"+
		"\u0119\t\7\2\2\u0118\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u0118\3\2"+
		"\2\2\u011a\u011b\3\2\2\2\u011bV\3\2\2\2\u011c\u0123\t\b\2\2\u011d\u011e"+
		"\n\t\2\2\u011e\u0123\6,\2\2\u011f\u0120\t\n\2\2\u0120\u0121\t\13\2\2\u0121"+
		"\u0123\6,\3\2\u0122\u011c\3\2\2\2\u0122\u011d\3\2\2\2\u0122\u011f\3\2"+
		"\2\2\u0123X\3\2\2\2\u0124\u012b\t\f\2\2\u0125\u0126\n\t\2\2\u0126\u012b"+
		"\6-\4\2\u0127\u0128\t\n\2\2\u0128\u0129\t\13\2\2\u0129\u012b\6-\5\2\u012a"+
		"\u0124\3\2\2\2\u012a\u0125\3\2\2\2\u012a\u0127\3\2\2\2\u012bZ\3\2\2\2"+
		"\u012c\u0130\t\r\2\2\u012d\u012e\7\17\2\2\u012e\u0130\7\f\2\2\u012f\u012c"+
		"\3\2\2\2\u012f\u012d\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0132\b.\2\2\u0132"+
		"\\\3\2\2\2\u0133\u0134\4\62;\2\u0134^\3\2\2\2\25\2cjpwy\u00e6\u00ed\u00f2"+
		"\u00fb\u00fd\u0101\u0105\u010a\u0112\u011a\u0122\u012a\u012f\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}