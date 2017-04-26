package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.util.CharSequenceReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderParser {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParserBundle");
	private static final String S_EOF = bundle.getString("Error.HeaderParser.eof");
	private static final char EOF = 26;
	private static final char EOT = 3; //end of text
	private static final String S_EOT = bundle.getString("Error.HeaderParser.eot");

	private final File parsingFile;
	private final HeaderParserContext parserContext;

	protected HeaderParser(@NotNull File parsingFile) {
		this.parsingFile = parsingFile;
		this.parserContext = new HeaderParserContext();
	}

	@NotNull
	public static HeaderFile parse(@NotNull File parsingFile) throws FileNotFoundException, HeaderParseException {
		HeaderParser p = new HeaderParser(parsingFile);
		return p.parse();
	}

	@NotNull
	public File getParsingFile() {
		return parsingFile;
	}

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
		HeaderFile headerFile = new HeaderFile(getParsingFile(), new ArrayList<>(), new ArrayList<>());

		PreprocessCallback callback = new PreprocessCallback() {
			@Override
			public void fileProcessed(@NotNull File file, @Nullable File includedFrom, @NotNull StringBuilder textContent) throws HeaderParseException {
				parseText(headerFile, file, includedFrom, textContent);
			}
		};

		Preprocessor pre = new Preprocessor(parsingFile, parserContext, callback);
		pre.run();

		HeaderClass root = parserContext.getClassStack().removeFirst();

		headerFile.getAssignments().addAll(root.getAssignments());
		headerFile.getClasses().addAll(root.getNestedClasses());

		return headerFile;
	}

	private void parseText(@NotNull HeaderFile parsingFile, @NotNull File file, @Nullable File includedFrom, @NotNull StringBuilder textContent) throws HeaderParseException {
		HeaderAntlrLexer l = getLexer(textContent);
		HeaderAntlrParser p = getParser(new CommonTokenStream(l));


	}


	@NotNull
	protected HeaderClass getCurrentClass() {
		return parserContext.getClassStack().getFirst();
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
	private HeaderAntlrLexer getLexer(StringBuilder s) {
		return new HeaderAntlrLexer(new ANTLRInputStream(s.toString()));
	}


}
