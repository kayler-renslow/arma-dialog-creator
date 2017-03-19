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

		FileInputStream fis = new FileInputStream(headerFile);

		try {
			return doParse(fis);
		} catch (Exception e) {
			if (e instanceof HeaderParseException) {
				throw (HeaderParseException) e;
			}
			throw new HeaderParseException(e);
		}
	}

	private HeaderFile doParse(@NotNull FileInputStream fis) throws Exception {
		List<HeaderAssignment> assignments = new ArrayList<>();
		List<HeaderClass> classes = new ArrayList<>();

		return new HeaderFile(headerFile, assignments, classes);
	}

	private void skipComment(@NotNull FileInputStream fis, boolean isBlock) throws IOException {
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
