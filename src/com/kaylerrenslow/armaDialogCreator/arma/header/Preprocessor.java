package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.data.FilePath;
import com.kaylerrenslow.armaDialogCreator.util.KeyValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 @author Kayler
 @since 03/21/2017 */
public class Preprocessor {
	private File processFile;
	private HeaderParserContext parserContext;
	private PreprocessCallback callback;
	private boolean preprocessed = false;

	private final LinkedList<File> processedFiles = new LinkedList<>();
	private final LinkedList<PreprocessState> preprocessStack = new LinkedList<>();
	private final HashMap<String, DefineMacroContent.DefineValue> defined = new HashMap<>();
	private int definedMod = 0;
	private int lastDefinedMod = 0;
	private String[][] defineReplacements = null;

	private final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParserBundle");

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

	private void doProcess(@NotNull File processFile, @NotNull StringBuilder fileContent) throws Exception {

		Scanner scan = new Scanner(processFile);
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

					defined.put(definedVar, value);
					definedMod++;

					parserContext.getMacros().add(new HeaderMacro(HeaderMacro.MacroType.Define, new DefineMacroContent(definedVar, value)));
					break;
				}
				case "#undef": {
					if (macroContent == null) {
						error(bundle.getString("Error.Preprocessor.Parse.no_content_undef"));
					}

					defined.remove(macroContent);
					definedMod++;

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
					discoveredElse = true;
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
	private String preprocessLine(@NotNull String line) {
		if (true) {
			throw new RuntimeException("todo");
		}
		if (lastDefinedMod != definedMod || defineReplacements == null) {
			for (Map.Entry<String, DefineMacroContent.DefineValue> entry : defined.entrySet()) {

			}
			lastDefinedMod = definedMod;
		}
		line = replace(line, defineReplacements[0], defineReplacements[1]);

		return line;
	}

	@NotNull
	static String replace(@NotNull String base, @NotNull String[] toMatch, @NotNull String[] replacements) {
		if (toMatch.length != replacements.length) {
			throw new IllegalArgumentException("toMatch.length != replacements.length");
		}
		StringBuilder ret = new StringBuilder(base.length());
		StringBuilder readChars = new StringBuilder(base.length());
		ArrayList<KeyValue<String, Integer>> matched = new ArrayList<>(toMatch.length);

		final int NO_MATCH = 0;

		for (String s : toMatch) {
			if (s.length() == 0) {
				throw new IllegalArgumentException("attempting to match empty string");
			}
			matched.add(new KeyValue<>(s, NO_MATCH));
		}

		int numMatched = matched.size();

		int i = 0;
		for (; i < base.length(); i++) {
			char c = base.charAt(i);
			readChars.append(c);
			int matchInd = 0;
			boolean replaced = false;
			for (KeyValue<String, Integer> match : matched) {
				String s = match.getKey();
				int mi = match.getValue();
				boolean charMatch = mi < s.length() && s.charAt(mi) == c;
				if (mi == s.length() - 1 && charMatch) {
					boolean allow = false;
					//can only replace parts of words of ## is used
					if (i + 1 < base.length()) {
						if (Character.isWhitespace(base.charAt(i + 1))) {
							allow = true;
						}
					}
					if (!allow && i + 2 < base.length()) {
						if (base.charAt(i + 1) == '#' && base.charAt(i + 2) == '#') {
							allow = true;
							i += 2;
						}
					}
					if (allow) {
						ret.append(replacements[matchInd]);
						readChars = new StringBuilder(base.length() - i); //reset
						replaced = true;
						break;
					}
				}
				if (charMatch) {
					match.setValue(mi + 1);
				} else {
					numMatched--;
				}
				matchInd++;
			}

			final boolean noMoreMatches = numMatched <= 0;
			if (noMoreMatches) {
				ret.append(readChars);
				readChars = new StringBuilder(base.length() - i);
				numMatched = matched.size();
			}
			if (noMoreMatches || replaced) {
				for (KeyValue<String, Integer> match : matched) {
					match.setValue(NO_MATCH);
				}
			}
		}

		return ret.toString();
	}

	private void error(String string) throws HeaderParseException {
		throw new HeaderParseException(String.format(bundle.getString("Error.Preprocessor.Parse.error_wrapper_f"), currentState().lineNumber, string));
	}

	private interface ProcessorHandler {
		void processNow(@NotNull String filePath, @NotNull File parentFile) throws Exception;
	}

	private static class PreprocessState {
		private int lineNumber = 0;
		private final File processingFile;

		public PreprocessState(@NotNull File processingFile) {
			this.processingFile = processingFile;
		}
	}
}
