package com.armadialogcreator.arma.header;

import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.CharSequenceReader;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 A parser for Arma 3 header files (.h, .hh, etc)

 @author Kayler
 @since 03/19/2017 */
public class HeaderParser {
	private static final ResourceBundle bundle = Lang.getBundle("HeaderParserBundle");
	private static final String S_EOF = bundle.getString("Error.HeaderParser.eof");
	private static final char EOF = 26;
	private static final char EOT = 3; //end of text
	private static final String S_EOT = bundle.getString("Error.HeaderParser.eot");

	private final HeaderFileTextProvider parsingFile;
	private final HeaderParserContext parserContext;

	protected HeaderParser(@NotNull HeaderFileTextProvider parsingFile, @NotNull File tempDirectory) throws IOException {
		this.parsingFile = parsingFile;
		if (tempDirectory.exists() && !tempDirectory.isDirectory()) {
			throw new IllegalArgumentException("tempDirectory is not a directory");
		}
		if (!tempDirectory.exists()) {
			tempDirectory.mkdirs();
		}
		this.parserContext = new HeaderParserContext(tempDirectory);
	}

	/**
	 Create a new header parser, preprocess the header file, parse the file,
	 and return a {@link HeaderParseResult} instance containing the results.

	 @param parsingFile the header file to parse
	 @param tempDirectory a directory
	 @return the result
	 */
	@NotNull
	public static HeaderParseResult parse(@NotNull File parsingFile, @NotNull File tempDirectory) throws IOException, HeaderParseException {
		return parse(new HeaderFileTextProvider.BasicFileInput(parsingFile), tempDirectory);
	}

	/**
	 Create a new header parser, preprocess the header file, parse the file,
	 and return a {@link HeaderParseResult} instance containing the results.

	 @param parsingFile the header file to parse
	 @param tempDirectory a directory
	 @return the result
	 */
	@NotNull
	public static HeaderParseResult parse(@NotNull HeaderFileTextProvider parsingFile, @NotNull File tempDirectory) throws IOException, HeaderParseException {
		HeaderParser p = new HeaderParser(parsingFile, tempDirectory);
		HeaderFile file = p.parse();

		Map<String, String> defineMacros = new HashMap<>();
		for (HeaderMacro macro : p.getMacros()) {
			if (!(macro.getContent() instanceof DefineMacroContent)) {
				continue;
			}
			DefineMacroContent define = (DefineMacroContent) macro.getContent();
			DefineMacroContent.DefineValue defineValue = define.getDefinedValue();
			if (defineValue instanceof DefineMacroContent.ParameterDefineValue) {
				DefineMacroContent.ParameterDefineValue paramDefineValue = (DefineMacroContent.ParameterDefineValue) defineValue;
				StringBuilder key = new StringBuilder();
				key.append(define.getDefinedVar());
				key.append('(');
				int i = 0;
				for (String param : paramDefineValue.getParams()) {
					key.append(param);
					if (i != paramDefineValue.getParams().length - 1) {
						key.append(',');
					}
					i++;
				}
				key.append(')');
				defineMacros.put(key.toString(), defineValue.getResultTemplateText());
			} else if (defineValue instanceof DefineMacroContent.StringDefineValue) {
				DefineMacroContent.StringDefineValue stringDefineValue = (DefineMacroContent.StringDefineValue) defineValue;
				defineMacros.put(define.getDefinedVar(), stringDefineValue.getResultTemplateText());
			}
		}
		return new HeaderParseResult(file, defineMacros);
	}

	/** @return the header file path being parsed (.h, .hh, etc) */
	@NotNull
	public String getParsingFilePath() {
		return parsingFile.getFilePath();
	}

	/** @return all macros discovered (#include, #ifdef, #else, #define, etc) */
	@NotNull
	public List<HeaderMacro> getMacros() {
		return parserContext.getMacros();
	}

	@NotNull
	public HeaderFile parse() throws HeaderParseException, FileNotFoundException {
		try {
			return doParse();
		} catch (Exception e) {
			if (e instanceof HeaderParseException) {
				throw (HeaderParseException) e;
			}
			if (e instanceof FileNotFoundException) {
				throw (FileNotFoundException) e;
			}
			throw new HeaderParseException(e);
		}
	}

	private HeaderFile doParse() throws Exception {
		HeaderFile headerFile = new HeaderFile(parsingFile.getFilePath());

		Preprocessor pre = new Preprocessor(parsingFile, parserContext);
		Preprocessor.PreprocessorFileReader fileContentStream = pre.run();

		parseText(headerFile, fileContentStream);

		return headerFile;
	}

	private void parseText(@NotNull HeaderFile parsingFile, @NotNull Preprocessor.PreprocessorFileReader fileContentStream) throws HeaderParseException {
		HeaderAntlrLexer l = getLexer(fileContentStream);
		HeaderAntlrParser p = getParser(new CommonTokenStream(l));
		l.getErrorListeners().clear();
		p.getErrorListeners().clear();

		p.addErrorListener(HeaderParserErrorListener.INSTANCE);

		AST.HeaderClassNode rootClass = p.root_class(parsingFile).ast;

		for (HeaderClass hc : rootClass.getNestedClasses()) {
			parsingFile.getClassesMutable().add(hc);
		}

		for (HeaderAssignment ha : rootClass.getAssignments()) {
			parsingFile.getAssignmentsMutable().add(ha);
		}

	}

	protected String expected(char exp, char got) {
		return String.format(
				bundle.getString("Error.HeaderParser.expected_got_f"),
				exp,
				got == EOF ? S_EOF : got == EOT ? S_EOT : got
		);
	}

	protected String unexpected(char c) {
		return String.format(bundle.getString("Error.HeaderParser.unexpected_f"), c);
	}

	protected void error(@NotNull String string, @NotNull CharSequenceReader r) throws HeaderParseException {
		throw new HeaderParseException(String.format(bundle.getString("Error.HeaderParser.parse_error_wrapper_f"), r.getLineCount(), r.getPosInLine(), string));
	}

	@NotNull
	private HeaderAntlrParser getParser(CommonTokenStream stream) {
		return new HeaderAntlrParser(stream);
	}

	@NotNull
	private HeaderAntlrLexer getLexer(@NotNull Preprocessor.PreprocessorFileReader r) {
		try {
			return new HeaderAntlrLexer(CharStreams.fromReader(r));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public static class HeaderParserErrorListener extends BaseErrorListener {
		public static final HeaderParserErrorListener INSTANCE = new HeaderParserErrorListener();

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
			//			String sourceName = recognizer.getInputStream().getSourceName();
			//			System.out.println("HeaderParserErrorListener.syntaxError offendingSymbol=" + offendingSymbol);
			//			System.out.println("HeaderParserErrorListener.syntaxError offendingSymbol.getClass()=" + offendingSymbol.getClass());
			//			CommonToken t = (CommonToken) offendingSymbol;
			//
			//			if (!sourceName.isEmpty()) {
			//				sourceName = String.format("%s:%d:%d: ", sourceName, line, charPositionInLine);
			//			}
			//
			//			System.err.println(sourceName+"line "+line+":"+charPositionInLine+" "+msg);
		}

		@Override
		public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
			super.reportAmbiguity(recognizer, dfa, startIndex, stopIndex, exact, ambigAlts, configs);
			//			System.out.println("HeaderParserErrorListener.reportAmbiguity");
		}

		@Override
		public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
			super.reportAttemptingFullContext(recognizer, dfa, startIndex, stopIndex, conflictingAlts, configs);
			//			System.out.println("HeaderParserErrorListener.reportAttemptingFullContext");
		}

		@Override
		public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
			super.reportContextSensitivity(recognizer, dfa, startIndex, stopIndex, prediction, configs);
			//			System.out.println("HeaderParserErrorListener.reportContextSensitivity");
		}
	}
}
