package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.arma.header.DefineMacroContent.DefineValue;
import com.kaylerrenslow.armaDialogCreator.arma.header.DefineMacroContent.ParameterDefineValue;
import com.kaylerrenslow.armaDialogCreator.data.FilePath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Scanner;

import static java.lang.Character.isWhitespace;

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
	protected final HashMap<String, DefineValue> defined = new HashMap<>();

	private static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParserBundle");

	public Preprocessor(@NotNull File processFile, @NotNull HeaderParserContext parserContext, @NotNull PreprocessCallback callback) {
		this.processFile = processFile;
		this.parserContext = parserContext;
		this.callback = callback;
	}


	public void run() throws Exception {
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
				boolean write = false;
				if (ifCount > 0) {
					if (useIfTrueCond && !discoveredElse) {
						write = true;
					} else if (!useIfTrueCond && discoveredElse) {
						write = true;
					}
				} else {
					write = true;
				}
				if (write) {
					preprocessLine(line, fileContent);
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
					DefineValue value = null;

					if (macroContent.length() == 0) {
						throw new HeaderParseException("Error.Preprocessor.Parse.macro_key_length_0");
					}

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

								value = new ParameterDefineValue(params, afterVar.substring(lastParen + 2));
							} else {
								value = new DefineMacroContent.StringDefineValue(macroContent.substring(i + 1));
							}
							break;
						}
					}
					if (definedVar == null) {
						error(bundle.getString("Error.Preprocessor.Parse.no_variable_define"));
					}

					defined.put(definedVar, value);

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

	protected void preprocessLine(@NotNull String base, @NotNull StringBuilder writeTo) throws HeaderParseException {
		if (defined.size() == 0) {
			writeTo.append(base);
			return;
		}
		for (Entry<String, DefineValue> entry : defined.entrySet()) {
			String key = entry.getKey();
			if (key.length() == 0) {
				throw new HeaderParseException("Error.Preprocessor.Parse.macro_key_length_0");
			}
		}

		int i = 0;
		final char NONE = '\0';
		boolean mergeMacros = false;//case of macro1##macro2

		baseLoop:
		for (; i < base.length(); ) {
			int resetIndex = i;
			int unmatchedLength = 1;

			entryLoop:
			for (Entry<String, DefineValue> entry : defined.entrySet()) {


				int matchInd = 0;
				String key = entry.getKey();

				while (matchInd < key.length() && i < base.length()) {
					if (key.charAt(matchInd) != base.charAt(i)) {
						break;
					}
					matchInd++;
					i++;
					unmatchedLength = Math.max(unmatchedLength, matchInd);
				}
				if (matchInd != key.length()) {
					i = resetIndex;
					continue entryLoop;
				}

				final boolean entryIsParam = entry.getValue() instanceof ParameterDefineValue;

				final char nextChar = i < base.length() ? base.charAt(i) : NONE;
				final char nextNextChar = i + 1 < base.length() ? base.charAt(i + 1) : NONE;
				final char beforeResetChar = resetIndex - 1 >= 0 ? base.charAt(resetIndex - 1) : NONE;

				boolean writeReplacement = false;

				if (nextChar == '#' && nextNextChar == '#') {
					mergeMacros = true;
					writeReplacement = true;
				} else if ((nextChar == NONE || isWhitespace(nextChar) || (entryIsParam && nextChar == '(')) && (mergeMacros || beforeResetChar == NONE || isWhitespace(beforeResetChar))) {
					writeReplacement = true;
					mergeMacros = false;
				}

				if (!writeReplacement) {
					i = resetIndex;
					continue entryLoop;
				}
				if (mergeMacros) {
					//advance past ##
					//note: it should not be the case that mergeMacros==true and entry.getValue() is a parameter
					i += 2;
				}
				if (entryIsParam) {
					if ((i < base.length() && base.charAt(i) != '(')) {
						error(bundle.getString("Error.Preprocessor.Parse.define_function_missing_lparen"));
					}
					int beforeParenI = i;
					i++;//move past (
					int lastParenInd = base.indexOf(')', i);
					if (lastParenInd < 0) {
						error(bundle.getString("Error.Preprocessor.Parse.define_function_missing_rparen"));
					}

					i = lastParenInd + 1;
					final char afterParenC = i + 1 < base.length() ? base.charAt(i + 1) : NONE;
					final char afterAfterParenC = i + 2 < base.length() ? base.charAt(i + 2) : NONE;
					if (afterParenC == '#' && afterAfterParenC == '#') {
						mergeMacros = true;
						i += 2;
					} else if (afterParenC != NONE && !isWhitespace(base.charAt(i + 1))) {
						unmatchedLength += Math.max(0, i - beforeParenI);
						break;
					}

					//ok to write
					appendParameterValue(base, beforeParenI + 1, entry, writeTo);
				} else {
					writeTo.append(entry.getValue().getText());
				}
				if (writeReplacement) {
					continue baseLoop;
				}
			}

			final int end = resetIndex + unmatchedLength;
			writeTo.append(base, resetIndex, end);
			i = end;
		}
	}

	private void appendParameterValue(@NotNull String base, int baseInd, @NotNull Entry<String, DefineValue> entry, @NotNull StringBuilder writeTo) throws HeaderParseException {
		ParameterDefineValue parameterValue = (ParameterDefineValue) entry.getValue();

		final int numParams = parameterValue.getParams().length;
		int paramInd = 0;
		final char NONE = '\0';
		int[][] paramValuesPosInBase = new int[numParams][2];
		final int START_POS = 0;
		final int END_POS = 1;

		for (; baseInd < base.length(); ) {

			paramValuesPosInBase[paramInd][START_POS] = baseInd;
			int paramValueLen = 0;
			while (baseInd < base.length() && (base.charAt(baseInd) != ',' || base.charAt(baseInd) != ')')) {
				paramValueLen++;
				baseInd++;
			}

			if (paramValueLen == 0) {
				error(bundle.getString("Error.Preprocessor.Parse.no_param_value"));
			}

			paramValuesPosInBase[paramInd][END_POS] = baseInd - 1;
			paramInd++;
		}

		if (numParams != paramInd) {
			error(String.format(bundle.getString("Error.Preprocessor.Parse.wrong_amount_of_params_written_f"), numParams, paramInd));
		}


	}

	private void incrementLineNumber() {
		currentState().lineNumber++;
	}

	@NotNull
	private PreprocessState currentState() {
		return preprocessStack.peek();
	}


	protected void error(String string) throws HeaderParseException {
		throw new HeaderParseException(String.format(bundle.getString("Error.Preprocessor.Parse.error_wrapper_f"), currentState().lineNumber, string));
	}


	protected static class PreprocessState {
		private int lineNumber = 0;
		private final File processingFile;

		public PreprocessState(@NotNull File processingFile) {
			this.processingFile = processingFile;
		}
	}
}
