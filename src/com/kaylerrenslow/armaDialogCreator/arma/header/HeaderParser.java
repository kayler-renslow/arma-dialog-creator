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
		parseMacros();


		FileInputStream fis = new FileInputStream(headerFile);

		List<HeaderAssignment> assignments = new ArrayList<>();
		List<HeaderClass> classes = new ArrayList<>();

		return new HeaderFile(headerFile, assignments, classes);
	}

	private void parseMacros() throws IOException {
		FileInputStream fis = new FileInputStream(headerFile);
		int in;
		char c;
		StringBuilder macroNameBuilder = new StringBuilder(10);
		StringBuilder macroContent = new StringBuilder(10);
		boolean readingMacro = false;
		boolean readingMacroName = false;
		boolean backslash = false;
		/*
		* todo:
		* we need to handle # and ## macro things
		* handle parameter defines
		* handle ifdef and ifndef
		* */
		while ((in = fis.read()) >= 0) {
			c = (char) in;
			if (readingMacro) {
				if (readingMacroName) {
					if (isWhitespace(c)) {
						readingMacroName = false;
						continue;
					} else {
						macroNameBuilder.append(c);
					}
				} else {
					if (c == '\\') {
						backslash = true;
						continue;
					}
					if (c == '\n') {
						if (!backslash) {
							//todo handle parameter defines: #define THING(ARG, ARG2) ARG=ARG2
							parserContext.getMacroMap().put(macroNameBuilder.toString(), macroContent.toString());
							macroNameBuilder = new StringBuilder(10);
							macroContent = new StringBuilder(10);
							readingMacro = false;
							throw new RuntimeException("todo handle parameter defines: #define THING(ARG, ARG2) ARG=ARG2");
						} else {
							backslash = false;
							skipWhiteSpace(fis);
						}
					}
					macroContent.append(c);
				}
			}
			if (c == '#') {
				readingMacro = true;
				readingMacroName = true;
			}
		}
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

	private void skipWhiteSpace(@NotNull FileInputStream fis) throws IOException {
		int in;
		while ((in = fis.read()) >= 0 && isWhitespace((char) in)) {
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
