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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 @author Kayler
 @since 03/21/2017 */
class Preprocessor {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParserBundle");

	private static final String beforeMacro = "^|##|#|[^#a-zA-Z_0-9$]";
	private static final String afterMacro = "$|##|#|[^#a-zA-Z_0-9$]";
	private static final Pattern macroReferencePattern = Pattern.compile(
			String.format(
					"(?<BEFORE>%s)(?<MACRO>%s)(?<PARAMS>%s)?(?=%s)",
					beforeMacro,
					"[a-zA-Z_0-9$]+", //identifier
					"\\([a-zA-Z_0-9$,]+\\)", //parameters
					afterMacro
			)
	);

	private static final Pattern macroParamOutputTextPattern = Pattern.compile(
			String.format(
					"(?<BEFORE>%s)(?<PARAM>%s)(?=%s)",
					beforeMacro, //before
					"[a-zA-Z_0-9$]+", //identifier
					afterMacro //after
			)
	);


	private final File processFile;
	private final HeaderParserContext parserContext;
	private final PreprocessCallback callback;
	private final File workingDirectory;
	private boolean preprocessed = false;

	protected final LinkedList<File> processingFiles = new LinkedList<>();
	protected final LinkedList<File> processedFiles = new LinkedList<>();
	protected final LinkedList<PreprocessState> preprocessStack = new LinkedList<>();
	protected final HashMap<String, DefineValue> defined = new HashMap<>();

	public Preprocessor(@NotNull File processFile, @NotNull HeaderParserContext parserContext, @NotNull PreprocessCallback callback) {
		this.processFile = processFile;
		this.parserContext = parserContext;
		this.callback = callback;
		this.workingDirectory = processFile.getParentFile();
	}


	public void run() throws Exception {
		if (preprocessed) {
			throw new IllegalStateException("preprocessor already run");
		}
		preprocessed = true;

		processNow(processFile, null);
	}

	private void processNow(@NotNull File toProcess, @Nullable File parentFile) throws Exception {
		if (processingFiles.contains(toProcess)) {
			if (parentFile == null) {
				throw new IllegalStateException("parentFile shouldn't be null here");
			}
			error(String.format(bundle.getString("Error.Preprocessor.Parse.circular_include_f"), toProcess.getName(), parentFile.getName()));
		}
		processingFiles.add(toProcess);
		preprocessStack.push(new PreprocessState(toProcess));

		StringBuilder textContent = new StringBuilder((int) processFile.length());
		doProcess(toProcess, textContent);

		preprocessStack.pop();
		processedFiles.add(processingFiles.pop()); //don't worry about duplicates. The header parser should report relevant errors to duplicate information

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
			line = scan.nextLine();

			if (!startsWithIgnoreSpace(line, "#")) {
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
					fileContent.append('\n');
				}
				continue;
			}
			StringBuilder macroBuilder = new StringBuilder(line.length());
			macroBuilder.append(line);
			if (!startsWithIgnoreSpace(line, "#ifdef") && !startsWithIgnoreSpace(line, "#ifndef")) {
				while (scan.hasNextLine() && line.endsWith("\\")) {
					line = scan.nextLine();
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

					File f = FilePath.findFileByPath(filePath, workingDirectory);
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

		Matcher m = macroReferencePattern.matcher(base);

		int baseInd = 0;
		while (m.find()) {

			//write everything that precedes the matched input
			if (baseInd < m.start()) {
				writeTo.append(base, baseInd, m.start());
				baseInd = m.start();
			}

			String macroName = m.group("MACRO");
			Entry<String, DefineValue> matchedEntry = null;
			for (Entry<String, DefineValue> entry : defined.entrySet()) {
				if (entry.getKey().equals(macroName)) {
					matchedEntry = entry;
					break;
				}
			}

			if (matchedEntry == null) {
				continue;
			}

			String before = m.group("BEFORE");
			String parameterText = m.group("PARAMS");

			if (matchedEntry.getValue() instanceof ParameterDefineValue) {
				if (parameterText == null) {
					continue;
				}
			}

			boolean quote = false;

			if (before.equals("#")) {
				quote = true;
				writeTo.append('"');
			} else if (!before.equals("##")) {
				writeTo.append(before);
			}

			//write replacement
			writeDefineValue(matchedEntry, parameterText, writeTo);

			if (quote) {
				writeTo.append('"');
			}

			if (parameterText != null) {
				baseInd = m.end("PARAMS");
			} else {
				baseInd = m.end("MACRO");
			}

			//skip past ## if it exists
			if (baseInd + 1 < base.length() && base.charAt(baseInd) == '#' && base.charAt(baseInd + 1) == '#') {
				baseInd += 2;
			}
		}

		if (baseInd < base.length()) {
			writeTo.append(base, baseInd, base.length());
		}
	}

	private void writeDefineValue(@NotNull Entry<String, DefineValue> entry, @Nullable String parameterText, @NotNull StringBuilder writeTo) throws HeaderParseException {
		String text = entry.getValue().getText();

		if (entry.getValue() instanceof ParameterDefineValue) {
			if (parameterText == null) {
				throw new IllegalArgumentException("parameterText should not be null if entry is a ParameterDefineValue");
			}
			ParameterDefineValue paramDefineValue = (ParameterDefineValue) entry.getValue();

			ParameterDefineValue parameterValue = (ParameterDefineValue) entry.getValue();
			String[] args = parameterText.substring(1, parameterText.length() - 1).split(",");
			final int numParams = parameterValue.getParams().length;
			if (args.length != numParams) {
				error(String.format(bundle.getString("Error.Preprocessor.Parse.wrong_amount_of_params_written_f"), numParams, args.length));
			}

			Matcher m = macroParamOutputTextPattern.matcher(text);

			int ind = 0;
			while (m.find()) {
				String param = m.group("PARAM");
				String before = m.group("BEFORE");

				boolean quote = false;
				if (before.equals("#")) {
					quote = true;
				}

				{
					int start = m.start("PARAM");

					if (ind < start) {
						if (quote || before.equals("##")) {
							int startBefore = m.start("BEFORE");
							writeTo.append(text, ind, startBefore);
						} else {
							writeTo.append(text, ind, start);
						}
						ind = start;
					}
				}

				int paramInd = 0;
				boolean found = false;
				for (String paramDefined : paramDefineValue.getParams()) {
					if (param.equals(paramDefined)) {
						found = true;
						break;
					}
					paramInd++;
				}
				if (!found) {
					continue;
				}
				String paramReplacement = args[paramInd];
				for (Entry<String, DefineValue> entry1 : defined.entrySet()) {
					if (entry1 instanceof ParameterDefineValue) {
						continue;
					}
					if (entry1.getKey().equals(paramReplacement)) {
						paramReplacement = entry1.getValue().getText();
						break;
					}
				}

				if (quote) {
					writeTo.append('"');
				}

				writeTo.append(paramReplacement);
				if (quote) {
					writeTo.append('"');
				}

				ind = m.end("PARAM");
				if (ind + 1 < text.length() && text.charAt(ind) == '#' && text.charAt(ind + 1) == '#') {
					ind += 2;
				}
			}
			if (ind < text.length()) {
				writeTo.append(text, ind, text.length());
			}

		} else {
			if (parameterText != null) {
				error(bundle.getString("Error.Preprocessor.Parse.unexp_lparen"));
			}

			writeTo.append(text);
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

	private static boolean startsWithIgnoreSpace(@NotNull String s, @NotNull String prefix) {
		int spaceInd = 0;
		boolean found = false;
		for (; spaceInd < s.length(); spaceInd++) {
			if (!Character.isWhitespace(s.charAt(spaceInd))) {
				found = true;
				break;
			}
		}
		if (found) {
			return s.startsWith(prefix, spaceInd);
		}
		return s.startsWith(prefix);
	}
}
