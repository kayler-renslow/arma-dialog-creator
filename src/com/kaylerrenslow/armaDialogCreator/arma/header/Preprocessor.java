package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.data.FilePath;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 @author Kayler
 @since 03/21/2017 */
public class Preprocessor {
	private File processFile;
	private HeaderParserContext parserContext;
	private boolean preprocessed = false;

	private int lineNum = -1;
	private final HashMap<String, DefineMacroContent.DefineValue> defined = new HashMap<>();

	private final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParserBundle");

	public Preprocessor(@NotNull File processFile, @NotNull HeaderParserContext parserContext) {
		this.processFile = processFile;
		this.parserContext = parserContext;
	}


	public void preprocess(@NotNull PreprocessCallback callback) throws Exception {
		if (preprocessed) {
			throw new IllegalStateException("preprocessor already run");
		}
		preprocessed = true;

		LinkedList<File> processedFiles = new LinkedList<>();
		processedFiles.add(processFile);

		StringBuilder textContent = new StringBuilder((int) processFile.length());
		parseMacros(processFile, textContent, new ProcessorHandler() {
			@Override
			public void processNow(@NotNull String filePath, @NotNull File parentFile) throws Exception {
				File f = FilePath.findFileByPath(filePath, parentFile);
				if (f == null) {
					error(String.format(bundle.getString("Error.Preprocessor.Parse.bad_file_path_f"), filePath));
				}
				if (processedFiles.contains(f)) {
					error(String.format(bundle.getString("Error.Preprocessor.Parse.circular_include_f"), f.getName(), parentFile.getName()));
				}
				processedFiles.add(f);

				StringBuilder textContent = new StringBuilder((int) processFile.length());
				parseMacros(f, textContent, this);
				callback.fileProcessed(f, parentFile, textContent);
			}
		});

		callback.fileProcessed(processFile, null, textContent);

	}

	private void parseMacros(@NotNull File processFile, @NotNull StringBuilder fileContent, @NotNull ProcessorHandler handler) throws Exception {
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
			lineNum++;
			line = scan.nextLine().trim();
			if (!line.startsWith("#")) {
				if (ifCount > 0) {
					if (useIfTrueCond && !discoveredElse) {
						fileContent.append(line);
					} else if (!useIfTrueCond && discoveredElse) {
						fileContent.append(line);
					}
				} else {
					fileContent.append(line);
				}
				continue;
			}
			StringBuilder macroBuilder = new StringBuilder(line.length());
			macroBuilder.append(line);
			if (!line.startsWith("#ifdef") && !line.startsWith("#ifndef")) {
				while (scan.hasNextLine() && line.endsWith("\\")) {
					line = scan.nextLine().trim();
					lineNum++;
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
					handler.processNow(filePath, processFile);
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

	private void error(String string) throws HeaderParseException {
		throw new HeaderParseException(String.format(bundle.getString("Error.Preprocessor.Parse.error_wrapper_f"), lineNum, string));
	}

	private interface ProcessorHandler {
		void processNow(@NotNull String filePath, @NotNull File parentFile) throws Exception;
	}
}
