package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.arma.header.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Character.isJavaIdentifierPart;
import static java.lang.Character.isWhitespace;

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
		CharSequenceReader r = new CharSequenceReader(textContent);

		if (parserContext.getClassStack().isEmpty()) {
			parserContext.getClassStack().add(new HeaderClassImpl());
		}

		int curlyBraceBalance = 0;
		ParseState state = ParseState.Identifier;

		StringBuilder sb = new StringBuilder();

		while (r.hasAvailable()) {
			final char c = r.read();
			switch (c) {
				//				case Plus: {
				//					//todo handle += for array assignments
				//					break;
				//				}
				case Equal: {
					if (state == ParseState.Identifier) {//assignment
						HeaderAssignmentImpl assignment = new HeaderAssignmentImpl(sb.toString());
						sb = new StringBuilder();

						readUpToSemicolon(r, sb);

						assignment.setHeaderValue(new BasicHeaderValue(sb.toString()));

						getCurrentClass().getAssignments().add(assignment);

						sb = new StringBuilder();

					} else if (state == ParseState.Bracket_Pair) {//array assignment
						HeaderArrayAssignmentImpl arrayAssignment = new HeaderArrayAssignmentImpl(sb.toString());
						sb = new StringBuilder();

						readUpToSemicolon(r, sb);

						LinkedList<HeaderArray> arrayStack = new LinkedList<>();
						int lbraceCount = 0;
						int rbraceCount = 0;
						boolean rootArraySet = false;
						boolean itemRead = false;

						CharSequenceReader arrayTextReader = new CharSequenceReader(sb);
						while (arrayTextReader.hasAvailable()) {
							final char ac = arrayTextReader.read();
							if (arrayStack.size() == 0 && ac != LBrace && !isWhitespace(ac)) {
								if (rootArraySet) {
									error(unexpected(ac), r);
								}
								error(expected(LBracket, ac), r);
							}
							if (ac == LBrace) {
								if (itemRead) {
									error(expected(Comma, LBrace), r);
								}
								lbraceCount++;
								if (!arrayStack.isEmpty()) {
									sb = new StringBuilder();
								}
								arrayStack.push(new HeaderArrayImpl());
							} else if (ac == RBrace) {
								rbraceCount++;
								if (rbraceCount != lbraceCount) {
									error(unexpected(RBrace), r);
								}
								HeaderArray array = arrayStack.pop();
								if (arrayStack.isEmpty()) {//ended root
									arrayAssignment.setHeaderArray(array);
									rootArraySet = true;
								} else { //nested in array
									arrayStack.peek().getItems().add(new HeaderArrayItemImpl(array));
								}
							} else if (ac == Comma) {
								if (arrayStack.isEmpty()) {
									error(unexpected(Comma), r);
								} else {
									if (sb.length() == 0) {
										error(bundle.getString("Error.HeaderParser.array_item_length_zero"), r);
									}
									arrayStack.peek().getItems().add(new HeaderArrayItemImpl(new BasicHeaderValue(sb.toString())));
									sb = new StringBuilder();
									itemRead = false;
								}
							} else {
								sb.append(ac);
								itemRead = itemRead || !isWhitespace(ac);
							}

						}

					} else {
						error(unexpected(Equal), r);
					}
				}
				case LBrace: {
					curlyBraceBalance++;
					parserContext.getClassStack().push(new HeaderClassImpl(sb.toString()));
					sb = new StringBuilder();
					state = ParseState.Identifier;

					break;
				}
				case RBrace: {
					if (curlyBraceBalance <= 0) {
						error(unexpected(RBrace), r);
					}
					parserContext.getClassStack().pop();
					curlyBraceBalance--;

					char ahead = r.canPeekAhead(1) ? r.peekAhead(1) : EOF;
					if (ahead == Semicolon) {
						r.read();
					} else {
						if (isWhitespace(ahead)) {
							skipWhitespace(r);
							ahead = r.hasAvailable() ? r.read() : EOF;
						}

						if (ahead != Semicolon) {
							error(expected(Semicolon, ahead), r);
						}
					}
					break;
				}
				case LBracket: {
					if (state == ParseState.Identifier) {
						char ahead = r.canPeekAhead(1) ? r.peekAhead(1) : EOF;
						if (ahead == RBracket) {
							r.read();//move past ]
							state = ParseState.Bracket_Pair;
						} else {
							error(expected(RBracket, ahead), r);
						}
					} else {
						error(unexpected(LBracket), r);
					}
					break;
				}
				case RBracket: {
					if (state == ParseState.Identifier) {
						error(expected(LBracket, RBracket), r);
					}
					error(unexpected(RBracket), r);
					break;
				}
				case Colon: {
					if (state == ParseState.Class_Stub) {

					} else {

					}
					break;
				}
				case Comma: {
					error(unexpected(Comma), r);
					break;
				}
				case Semicolon: {
					error(unexpected(Semicolon), r);
					break;
				}
				default: {
					if (c == '/') {
						if (r.canPeekAhead(1) && r.peekAhead(1) == '/') {
							skipRestOfComment(r, false);
						} else if (r.canPeekAhead(1) && r.peekAhead(1) == '*') {
							skipRestOfComment(r, true);
						}
					} else {
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
							if (!isJavaIdentifierPart(c)) {
								error(unexpected(c), r);
							}
							if (state == ParseState.Class_Stub) {
								sb.append(c);
							} else {
								state = ParseState.Identifier;
								sb.append(c);
							}
						}
					}
				}
			}
		}

	}

	private void readUpToSemicolon(@NotNull CharSequenceReader r, @NotNull StringBuilder sb) throws HeaderParseException {
		boolean semicolon = false;
		char ca = EOF;
		char last = ca;
		while (r.hasAvailable()) {
			last = ca;
			ca = r.read();

			//make sure we aren't reading up to a semicolon in a String
			if (ca == DQuote || ca == Quote) {
				readString(r, sb);
			}
			if (ca == Semicolon) {
				semicolon = true;
				break;
			}
			sb.append(ca);
		}
		if (!semicolon) {
			error(expected(Semicolon, last), r);
		}
	}

	private void readString(@NotNull CharSequenceReader r, @Nullable StringBuilder writeTo) throws HeaderParseException {
		boolean quote = true;
		char quoteType = r.lookBehind(1);
		if (quoteType != Quote && quoteType != DQuote) {
			throw new IllegalStateException("can't read string which hasn't read a ' or \" prior to method call");
		}
		if (writeTo != null) {
			writeTo.append(quoteType);
		}
		boolean flag = true;
		while (r.hasAvailable() && flag) {
			char c = r.read();
			if (c == quoteType) {
				quote = !quote;
				if (!quote) {
					//in Arma, "" is used instead of \"
					flag = r.canPeekAhead(1) && r.peekAhead(1) == quoteType;
				}
			}
			if (writeTo != null) {
				writeTo.append(c);
			}
		}
		if (quote) {
			expected(quoteType, EOF);
		}
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

	private static void skipRestOfComment(@NotNull CharSequenceReader r, boolean isBlock) {
		char c = '\0'; //set to something that will initially fail for the first read
		boolean quote = false;
		char lastChar;
		while (r.hasAvailable()) {
			lastChar = c;
			c = r.read();
			if (isBlock) {
				if (c == DQuote) {
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
		while (r.canPeekAhead(1) && isWhitespace(r.peekAhead(1))) {
			r.read();
		}
	}

	enum ParseState {
		Identifier, Class_Stub, Bracket_Pair
	}

	static final char Comma = ',';
	static final char Colon = ':';
	static final char Semicolon = ';';
	static final char Plus = '+';
	static final char Equal = '=';
	static final char LBrace = '{';
	static final char RBrace = '}';
	static final char LBracket = '[';
	static final char RBracket = ']';
	static final char Quote = '\'';
	static final char DQuote = '"';


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

		public char lookBehind(int amount) {
			return cs.charAt(pos - amount);
		}

		public boolean canLookBehind(int amount) {
			return pos - amount >= 0;
		}

		public int getLineCount() {
			return lineCount;
		}

		public int getPosInLine() {
			return posInLine;
		}

		public char read() {
			if (!hasAvailable()) {
				throw new IllegalStateException("can't read when nothing left to read.");
			}
			char c = cs.charAt(pos++);
			if (c == '\n') {
				lineCount++;
				posInLine = 0;
			} else {
				posInLine++;
			}
			return c;
		}
	}


}
