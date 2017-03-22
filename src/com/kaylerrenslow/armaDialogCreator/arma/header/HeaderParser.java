package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderParser {
	private final File headerFile;
	private final HeaderParserContext parserContext;

	public HeaderParser(@NotNull File headerFile, @Nullable HeaderParserContext parserContext) {
		this.headerFile = headerFile;
		this.parserContext = parserContext == null ? new HeaderParserContext() : parserContext;

	}

	@NotNull
	public File getHeaderFile() {
		return headerFile;
	}

	@NotNull
	public HeaderParserContext getParserContext() {
		return parserContext;
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
		Preprocessor pre = new Preprocessor(headerFile, parserContext);

		List<HeaderAssignment> assignments = new ArrayList<>();
		List<HeaderClass> classes = new ArrayList<>();


		pre.preprocess(new PreprocessCallback() {
			@Override
			public void fileProcessed(@NotNull File file, @Nullable File includedFrom, @NotNull StringBuilder textContent) {

			}
		});

		return new HeaderFile(headerFile, assignments, classes);
	}




	private static void skipComment(@NotNull FileInputStream fis, boolean isBlock) throws IOException {
		int in;
		char c;
		char lastChar = ' '; //set to something that will initially fail for the first read
		while ((in = fis.read()) >= 0) {
			c = (char) in;
			if (isBlock) {
				if (lastChar == '*' && c == '/') {
					return;
				}
			} else {
				if (c == '\n') {
					return;
				}
			}
			lastChar = c;
		}

	}

	private static void skipWhitespace(@NotNull FileInputStream fis) throws IOException {
		int in;
		while ((in = fis.read()) >= 0 && isWhitespace((char) in)) {
			//do nothing
		}
	}

	private static void readTillAfter(@NotNull FileInputStream fis, char c) throws IOException {
		int in;
		while ((in = fis.read()) >= 0 && ((char) in) != c) {
			//do nothing
		}
	}

	private static boolean isWhitespace(char c) {
		return Character.isWhitespace(c);
	}

	private enum Token {
		Eq("="),
		Class("class"),
		Comma(","),
		Colon(":"),
		Semicolon(";"),
		//		Plus("+"),
		//		Minus("-"),
		//		Star("*"),
		//		FSlash("/"),
		Equal("="),
		//		LParen("("),
		//		RParen(")"),
		LBrace("{"),
		RBrace("}"),
		BracketPair("[]"),;

		final String s;

		Token(String s) {
			this.s = s;
		}
	}

}
