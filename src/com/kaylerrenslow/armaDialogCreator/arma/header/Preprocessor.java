package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.data.FilePath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 @author Kayler
 @since 03/21/2017 */
public class Preprocessor {
	private final File processFile;
	private final HeaderParserContext parserContext;
	private final PreprocessCallback callback;
	private boolean preprocessed = false;

	protected final LinkedList<File> processedFiles = new LinkedList<>();
	protected final LinkedList<PreprocessState> preprocessStack = new LinkedList<>();
	protected final HashMap<String, DefinedValueWrapper> defined = new HashMap<>();

	private static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParserBundle");

	public Preprocessor(@NotNull File processFile, @NotNull HeaderParserContext parserContext, @NotNull PreprocessCallback callback) {
		this.processFile = processFile;
		this.parserContext = parserContext;
		this.callback = callback;
	}


	public void preprocess() throws Exception {
		if (preprocessed) {
			throw new IllegalStateException("preprocessor already run");
		}
		preprocessed = true;

		processNow(processFile, null);
	}

	private void processNow(@NotNull File toProcess, @Nullable File parentFile) throws Exception {
		if (processedFiles.contains(toProcess)) {
			if (parentFile == null) {
				throw new IllegalStateException("parentFile shouldn't be null here");
			}
			error(String.format(bundle.getString("Error.Preprocessor.Parse.circular_include_f"), toProcess.getName(), parentFile.getName()));
		}
		processedFiles.add(toProcess);
		preprocessStack.push(new PreprocessState(toProcess));

		StringBuilder textContent = new StringBuilder((int) processFile.length());
		doProcess(toProcess, textContent);

		preprocessStack.pop();

		callback.fileProcessed(processFile, parentFile, textContent);
	}

	protected void doProcess(@NotNull File processFile, @NotNull StringBuilder fileContent) throws Exception {
		Scanner scan = new Scanner(processFile);
		doProcess(scan, fileContent);
	}

	protected void doProcess(@NotNull Scanner scan, @NotNull StringBuilder fileContent) throws Exception {
		String line;

		int ifCount = 0; //>0 if current line is inside (#ifdef or #ifndef) and before #endif
		boolean useIfTrueCond = false; //true if everything before #else should be added, false if everything after #else should be added
		boolean discoveredElse = false; //true if #else has been discovered
		final int IF_UNSET = -1;
		final int IF_DEF = 0;
		final int IF_N_DEF = 1;
		int ifType = IF_UNSET;

		while (scan.hasNextLine()) {
			incrementLineNumber();
			line = scan.nextLine().trim();
			if (!line.startsWith("#")) {
				String preprocessedLine = preprocessLine(line);
				if (ifCount > 0) {
					if (useIfTrueCond && !discoveredElse) {
						fileContent.append(preprocessedLine);
					} else if (!useIfTrueCond && discoveredElse) {
						fileContent.append(preprocessedLine);
					}
				} else {
					fileContent.append(preprocessedLine);
				}
				continue;
			}
			StringBuilder macroBuilder = new StringBuilder(line.length());
			macroBuilder.append(line);
			if (!line.startsWith("#ifdef") && !line.startsWith("#ifndef")) {
				while (scan.hasNextLine() && line.endsWith("\\")) {
					line = scan.nextLine().trim();
					incrementLineNumber();
					macroBuilder.append(line);
				}
			}
			String macroText = macroBuilder.toString();
			int spaceInd = macroText.indexOf(' ');
			if (spaceInd < 0) {
				spaceInd = macroText.length();
			}

			String macroName = macroText.substring(0, spaceInd);
			String macroContent = null;
			if (spaceInd + 1 < macroText.length()) { //nothing in body
				macroContent = macroText.substring(spaceInd + 1);
			}

			switch (macroName) {
				case "#include": {
					if (macroContent == null || macroContent.length() <= 2) {
						error(bundle.getString("Error.Preprocessor.Parse.no_included_file"));
						return;
					}
					char left = macroContent.charAt(0);
					char right = macroContent.charAt(macroContent.length() - 1);
					boolean badFormat = left != right;
					switch (left) {
						case '"': {
							//intentional fall through
						}
						case '<': {
							//intentional fall through
						}
						case '\'': {
							//do nothing
							break;
						}
						default: {
							badFormat = true;
						}
					}
					if (badFormat) {
						error(bundle.getString("Error.Preprocessor.Parse.bad_include_format"));
					}

					String filePath = macroContent.substring(1, macroContent.length() - 1);

					File f = FilePath.findFileByPath(filePath, processFile);
					if (f == null) {
						error(String.format(bundle.getString("Error.Preprocessor.Parse.bad_file_path_f"), filePath));
					}

					processNow(f, processFile);
					break;
				}
				case "#define": {
					if (macroContent == null) {
						error(bundle.getString("Error.Preprocessor.Parse.no_content_define"));
					}

					String definedVar = null;
					DefineMacroContent.DefineValue value = null;

					for (int i = 0; i < macroContent.length(); i++) {
						char c = macroContent.charAt(i);
						if (c == ' ' || c == '(') {
							definedVar = macroContent.substring(0, i);
							if (c != ' ') {
								if (i >= macroContent.length()) {
									error(bundle.getString("Error.Preprocessor.Parse.parameter_define_bad"));
								}
								String afterVar = macroContent.substring(i + 1);
								int lastParen = afterVar.indexOf(')');
								if (lastParen < 0 || lastParen + 2 >= afterVar.length()) {
									error(bundle.getString("Error.Preprocessor.Parse.parameter_define_bad"));
								}

								String[] params = afterVar.substring(0, lastParen).split(",");

								value = new DefineMacroContent.ParameterDefineValue(params, afterVar.substring(lastParen + 2));
							} else {
								value = new DefineMacroContent.StringDefineValue(macroContent.substring(i + 1));
							}
							break;
						}
					}
					if (definedVar == null) {
						error(bundle.getString("Error.Preprocessor.Parse.no_variable_define"));
					}

					defined.put(definedVar, new DefinedValueWrapper(value));

					parserContext.getMacros().add(new HeaderMacro(HeaderMacro.MacroType.Define, new DefineMacroContent(definedVar, value)));
					break;
				}
				case "#undef": {
					if (macroContent == null) {
						error(bundle.getString("Error.Preprocessor.Parse.no_content_undef"));
					}

					defined.remove(macroContent);

					parserContext.getMacros().add(new HeaderMacro(HeaderMacro.MacroType.Undefine, new UndefineMacroContent(macroContent)));
					break;
				}
				case "#ifndef": { //intentional fall through
					ifType = IF_N_DEF;
				}
				case "#ifdef": {
					if (ifType != IF_N_DEF) { //check if set from fall through
						ifType = IF_DEF;
					}

					ifCount++;

					String condition = macroContent;
					if (condition == null) {
						String error = String.format(bundle.getString("Error.Preprocessor.Parse.no_condition_f"), (ifType == IF_DEF ? "#ifdef" : "#ifndef"));
						error(error);
					}

					if (defined.containsKey(condition)) {
						useIfTrueCond = ifType == IF_DEF;
					} else {
						useIfTrueCond = ifType == IF_N_DEF;
					}

					HeaderMacro.MacroType macroType;

					if (ifType == IF_DEF) {
						macroType = HeaderMacro.MacroType.IfDef;
					} else {
						macroType = HeaderMacro.MacroType.IfNDef;
					}
					parserContext.getMacros().add(new HeaderMacro(macroType, new ConditionalMacroContent(condition)));

					break;
				}
				case "#else": {
					if (ifCount <= 0) {
						error(bundle.getString("Error.Preprocessor.Parse.unexpected_else"));
					}
					discoveredElse = true; //only mark as true if there was a preceding if
					break;
				}
				case "#endif": {
					discoveredElse = false;
					if (ifCount <= 0) {
						error(bundle.getString("Error.Preprocessor.Parse.unexpected_endif"));
					}
					ifCount--;
					ifType = IF_UNSET;
					break;
				}
				default: {
					error(String.format(bundle.getString("Error.Preprocessor.Parse.unknown_macro_f"), macroName));
				}
			}
		}
		scan.close();
	}

	private void incrementLineNumber() {
		currentState().lineNumber++;
	}

	@NotNull
	private PreprocessState currentState() {
		return preprocessStack.peek();
	}

	@NotNull
	private String preprocessLine(@NotNull String line) throws HeaderParseException {

		try {
			line = replace(line);
		} catch (HeaderParseException e) {
			error(bundle.getString("Error.Preprocessor.Parse.preprocess_line_fail_f"));
		}

		return line;
	}

	protected String replace(@NotNull String base) throws HeaderParseException {
		if (defined.size() == 0) {
			return base;
		}
		StringBuilder ret = new StringBuilder(base.length());
		StringBuilder readChars = new StringBuilder(base.length());

		final int NO_MATCH = 0;

		for (Entry<String, DefinedValueWrapper> wrapperEntry : defined.entrySet()) {
			wrapperEntry.getValue().setMatchedLength(NO_MATCH);
		}

		int numMatched = defined.size();

		int i = 0;
		for (; i < base.length(); i++) {
			char c = base.charAt(i);
			readChars.append(c);

			boolean replaced = false;
			for (Entry<String, DefinedValueWrapper> wrapperEntry : defined.entrySet()) {
				String s = wrapperEntry.getKey();
				int mi = wrapperEntry.getValue().getMatchedLength();
				boolean charMatch = mi < s.length() && s.charAt(mi) == c;
				if (mi == s.length() - 1 && charMatch) {

					final int macroStartInd = i - mi - 1;

					DefineMacroContent.DefineValue defineValue = wrapperEntry.getValue().getDefineValue();
					String replacement;

					if (defineValue instanceof DefineMacroContent.ParameterDefineValue) {
						DefineMacroContent.ParameterDefineValue paramValue = (DefineMacroContent.ParameterDefineValue) defineValue;
						String macroValueText = paramValue.getText();
						StringBuilder replacementBuilder = new StringBuilder();

						int macroValueTextInd = 0;
						if (i + 1 < base.length() && base.charAt(i + 1) == '(') {
							readChars.append('(');
							i += 2;//advance past (

							for (String param : paramValue.getParams()) {
								int paramInd = macroValueText.indexOf(param, macroValueTextInd);
								if (paramInd < 0) {
									error(String.format(bundle.getString("Error.Preprocessor.Parse.parameter_not_in_output_f"), param));
								}
								while (paramInd >= 0) {
									//write everything up till the param index
									while (macroValueTextInd < paramInd) {
										replacementBuilder.append(macroValueText.charAt(macroValueTextInd));
										macroValueTextInd++;
									}

									macroValueTextInd += param.length(); //skip past param

									//write the actual parameter value defined in the preprocessed file
									while (i < base.length() && base.charAt(i) != ',' && base.charAt(i) != ')') {
										char c1 = base.charAt(i);
										/* don't do the following because [] is perfectly valid:
										if(c1 == '[') {
											throw new HeaderParseException();
										}
										*/
										readChars.append(c1);
										replacementBuilder.append(c1);
										i++;
									}
									readChars.append(base.charAt(i)); //append , or )

									paramInd = macroValueText.indexOf(param, i);
								}
							}

							//write remainder of macro value text
							while (macroValueTextInd < macroValueText.length()) {
								replacementBuilder.append(macroValueText.charAt(macroValueTextInd));
								macroValueTextInd++;
							}
						} else {
							error(bundle.getString("Error.Preprocessor.Parse.define_function_missing_paren"));
						}
						replacement = replacementBuilder.toString();

					} else {
						replacement = defineValue.getText();
					}

					boolean allow = false;
					//check if there is whitespace following and preceding the macro, or if end of line
					if (macroStartInd == -1 || Character.isWhitespace(base.charAt(macroStartInd))) {
						if (i + 1 < base.length()) {
							if (Character.isWhitespace(base.charAt(i + 1))) {
								allow = true;
							}
						} else if (i + 1 == base.length()) {
							allow = true;
						}
					}

					//can only replace parts of words of ## is used
					if (!allow && i + 2 < base.length()) {
						if (base.charAt(i + 1) == '#' && base.charAt(i + 2) == '#') {
							allow = true;
							i += 2;
						}
					}
					if (allow) {
						ret.append(replacement);
						readChars = new StringBuilder(); //reset
						replaced = true;
						break;
					} else {
						numMatched--;
					}
				}
				if (charMatch) {
					wrapperEntry.getValue().incrementMatchedLength();
				} else {
					numMatched--;
				}

			}

			final boolean noMoreMatches = numMatched <= 0;
			if (noMoreMatches) {
				ret.append(readChars);
				readChars = new StringBuilder(base.length() - i);
				numMatched = defined.size();
			}
			if (noMoreMatches || replaced) {
				for (Entry<String, DefinedValueWrapper> wrapperEntry : defined.entrySet()) {
					wrapperEntry.getValue().setMatchedLength(NO_MATCH);
				}
			}
		}

		return ret.toString();
	}

	protected void error(String string) throws HeaderParseException {
		throw new HeaderParseException(String.format(bundle.getString("Error.Preprocessor.Parse.error_wrapper_f"), currentState().lineNumber, string));
	}

	protected static class DefinedValueWrapper {
		private DefineMacroContent.DefineValue value;
		private int matchedLength;

		public DefinedValueWrapper(@NotNull DefineMacroContent.DefineValue value) {
			this.value = value;
		}

		@NotNull
		public DefineMacroContent.DefineValue getDefineValue() {
			return value;
		}

		public void incrementMatchedLength() {
			matchedLength++;
		}

		public void setMatchedLength(int l) {
			this.matchedLength = l;
		}

		public int getMatchedLength() {
			return matchedLength;
		}
	}

	protected static class PreprocessState {
		private int lineNumber = 0;
		private final File processingFile;

		public PreprocessState(@NotNull File processingFile) {
			this.processingFile = processingFile;
		}
	}
}
