package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.arma.header.impl.HeaderArrayAssignmentImpl;
import com.kaylerrenslow.armaDialogCreator.arma.header.impl.HeaderAssignmentImpl;
import com.kaylerrenslow.armaDialogCreator.arma.header.impl.HeaderClassImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Character.isWhitespace;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderParser {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParserBundle");

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

		HeaderClass c = parserContext.getClassStack().removeFirst();

		headerFile.getAssignments().addAll(c.getAssignments());
		headerFile.getClasses().addAll(c.getNestedClasses());

		return headerFile;
	}

	private void parseText(@NotNull HeaderFile parsingFile, @NotNull File file, @Nullable File includedFrom, @NotNull StringBuilder textContent) throws HeaderParseException {
		CharSequenceReader r = new CharSequenceReader(textContent);

		if (parserContext.getClassStack().isEmpty()) {
			parserContext.getClassStack().add(new HeaderClassImpl());
		}

		int curlyBraceBalance = 0;
		ParseState state = ParseState.Identifier;
		HeaderAssignmentImpl assignment = null;
		HeaderArrayAssignmentImpl arrayAssignment = null;

		StringBuilder sb = new StringBuilder();

		while (r.hasAvailable()) {
			char c = r.read();
			switch (c) {
				case Equal: {
					if (state == ParseState.Identifier) {//assignment
						assignment = new HeaderAssignmentImpl(sb.toString());
						sb = new StringBuilder();
						state = ParseState.Assignment;
					} else if (state == ParseState.Bracket_Pair) {//array assignment
						arrayAssignment = new HeaderArrayAssignmentImpl(sb.toString());
						sb = new StringBuilder();
						state = ParseState.Array_Assignment;
					} else {
						error(bundle.getString("Error.HeaderParser.unexpected_eq"), r);
					}
				}
				case LBrace: {
					break;
				}
				case RBrace: {
					break;
				}
				case Comma: {
					break;
				}
				case LBracket: {
					break;
				}
				case RBracket: {
					break;
				}
				case Colon: {
					break;
				}
				default: {
					if (c == '/') {
						if (r.canPeekAhead(1) && r.peekAhead(1) == '/') {
							skipComment(r, false);
						} else if (r.canPeekAhead(1) && r.peekAhead(1) == '*') {
							skipComment(r, true);
						}
					}
					if (isWhitespace(c)) {
						if (sb.length() > 0) {
							String s = sb.toString();
							sb = new StringBuilder();
							if (s.equals("class")) {
								state = ParseState.Class_Stub;
								continue;
							}

						}

						skipWhitespace(r);
					} else {
						state = ParseState.Identifier;
						sb.append(c);
					}
					continue;
				}
			}
			if (c == Equal) {

			}
		}

	}

	protected void error(@NotNull String string, @NotNull CharSequenceReader r) throws HeaderParseException {
		throw new HeaderParseException(String.format(bundle.getString("Error.HeaderParser.parse_error_wrapper_f"), r.getLineCount(), r.getPosInLine(), string));
	}

	private static void skipComment(@NotNull CharSequenceReader r, boolean isBlock) {
		char c = '\0'; //set to something that will initially fail for the first read
		boolean quote = false;
		char lastChar;
		while (r.hasAvailable()) {
			lastChar = c;
			c = r.read();
			if (isBlock) {
				if (c == '"') {
					quote = !quote;
				}
				if (quote) {
					continue;
				}
				if (lastChar == '*' && c == '/') {
					return;
				}
			} else {
				if (c == '\n') {
					return;
				}
			}
		}

	}

	private static void skipWhitespace(@NotNull CharSequenceReader r) {
		while (r.hasAvailable() && isWhitespace(r.read())) {
			//do nothing
		}
		r.goBack(1);
	}

	enum ParseState {
		Identifier, Class_Stub, Assignment, Array_Assignment, Bracket_Pair
	}

	static final char Comma = ',';
	static final char Colon = ':';
	static final char Semicolon = ';';
	static final char Equal = '=';
	static final char LBrace = '{';
	static final char RBrace = '}';
	static final char LBracket = '[';
	static final char RBracket = ']';


	private static class CharSequenceReader {
		private final CharSequence cs;
		private int pos = 0;
		private int lineCount = 0;
		private int posInLine = 0;

		public CharSequenceReader(@NotNull CharSequence cs) {
			this.cs = cs;
		}

		public boolean hasAvailable() {
			return pos >= cs.length();
		}

		public int getPos() {
			return pos;
		}

		public boolean canPeekAhead(int amount) {
			return pos + amount < cs.length();
		}

		public char peekAhead(int amount) {
			return cs.charAt(pos + amount);
		}

		public int getLineCount() {
			return lineCount;
		}

		public int getPosInLine() {
			return posInLine;
		}

		public void goBack(int amount) {
			pos = Math.max(0, pos - amount);
		}

		public char read() {
			if (!hasAvailable()) {
				throw new IllegalStateException("can't read when nothing left to read.");
			}
			char c = cs.charAt(pos++);
			if (c == 'n') {
				lineCount++;
				posInLine = 0;
			} else {
				posInLine++;
			}
			return c;
		}
	}

}
