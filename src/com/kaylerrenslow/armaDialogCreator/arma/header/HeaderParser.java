package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		Scanner scan = new Scanner(headerFile);
		String line;

		while (scan.hasNextLine()) {
			line = scan.nextLine().trim();
			if (!line.startsWith("#")) {
				continue;
			}
			StringBuilder macroBuilder = new StringBuilder(line.length() * 2);
			if (!line.startsWith("#ifdef") && !line.startsWith("#ifndef")) {
				while (scan.hasNextLine() && line.endsWith("\\")) {
					line = scan.nextLine().trim();
					macroBuilder.append(line);
				}
			}
			String macroText = macroBuilder.toString();
			int spaceInd = macroText.indexOf(' ');
			if (spaceInd < 0) {
				continue;
			}
			if (spaceInd + 1 >= macroText.length()) { //nothing in body
				continue;
			}
			String macroName = macroText.substring(0, spaceInd);
			String macroContent = macroText.substring(spaceInd + 1);
			boolean ifndef = false;
			switch (macroName) {
				case "include": {
					//do nothing
					break;
				}
				case "define": {
					parserContext.getMacros().add(new HeaderMacro(HeaderMacro.MacroType.Define, new HeaderMacroContent.StringContent(macroContent)));
					break;
				}
				case "undef": {
					parserContext.getMacros().add(new HeaderMacro(HeaderMacro.MacroType.Undefine, new HeaderMacroContent.StringContent(macroContent)));
					break;
				}
				case "ifndef": { //intentional fall through
					ifndef = true;
				}
				case "ifdef": {
					StringBuilder ifBody = new StringBuilder(10);
					StringBuilder elseBody = new StringBuilder(10);
					StringBuilder append = ifBody;
					while (scan.hasNextLine() && !line.equals("#endif")) {
						line = scan.nextLine().trim();
						if (line.equals("#else")) {
							append = elseBody;
							continue;
						}
						append.append(line);
					}
					HeaderMacroContent content = new HeaderMacroContent.Conditional(ifBody.toString(), elseBody.toString());
					if (ifndef) {
						parserContext.getMacros().add(new HeaderMacro(HeaderMacro.MacroType.IfNDef, content));
					} else {
						parserContext.getMacros().add(new HeaderMacro(HeaderMacro.MacroType.IfDef, content));
					}
					break;
				}
			}
		}
		scan.close();

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
