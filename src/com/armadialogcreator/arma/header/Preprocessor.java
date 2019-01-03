package com.armadialogcreator.arma.header;

import com.armadialogcreator.arma.header.DefineMacroContent.DefineValue;
import com.armadialogcreator.arma.header.DefineMacroContent.ParameterDefineValue;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.arma.util.ArmaUIScale;
import com.armadialogcreator.canvas.ScreenDimension;
import com.armadialogcreator.data.HeaderConversionException;
import com.armadialogcreator.expression.*;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.UTF8FileReader;
import com.armadialogcreator.util.UTF8FileWriter;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 @author Kayler
 @see <a href='https://resources.bisimulations.com/wiki/PreProcessor_Commands'>Bohemia Interactive Preprocessor Documentation</a>
 @since 03/21/2017 */
class Preprocessor {

	@RegExp
	private final String beforeMacro = "^|##|#|[^#a-zA-Z_0-9$]";
	@RegExp
	private final String afterMacro = "$|##|#|[^#a-zA-Z_0-9$]";
	private final Pattern macroReferencePattern = Pattern.compile(
			String.format(
					//using ?= so that the pattern doesn't consume tokens (consuming will prevent a future pattern match)
					"(?<BEFORE>%s)(?<MACRO>%s)(?<PARAMS>%s)?(?=%s)",
					beforeMacro,
					"[a-zA-Z_0-9$]+", //identifier

					// Parameters: allow for everything in between (), except ';'
					// OR allow everything in between () except '(' and ')'
					"\\(([^;]+|[^()]+)\\)",
					afterMacro
			)
	);

	private final Pattern macroParamOutputTextPattern = Pattern.compile(
			String.format(
					"(?<BEFORE>%s)(?<PARAM>%s)(?=%s)",
					beforeMacro, //before
					"[a-zA-Z_0-9$]+", //identifier
					afterMacro //after
			)
	);

	/** The file where the preprocessing began */
	private final HeaderFileTextProvider processFile;
	/** Provided via {@link HeaderParser} */
	private final HeaderParserContext parserContext;
	/** Set to true if this preprocessor instance has been run via {@link #run()} */
	private boolean preprocessed = false;
	/** Bundle to get things from */
	private final ResourceBundle bundle = Lang.getBundle("arma.header.HeaderParserBundle");

	/** Stream to write the fully preprocessed results to. This is currently a {@link FileOutputStream} for {@link #temporaryFullyPreprocessedResultFile} */
	private final Writer writer;
	private final File temporaryFullyPreprocessedResultFile;
	protected final LinkedList<String> processingFiles = new LinkedList<>();
	protected final LinkedList<String> processedFiles = new LinkedList<>();
	protected final LinkedList<PreprocessState> preprocessStack = new LinkedList<>();
	protected final HashMap<String, DefineValue> defined = new HashMap<>();
	private final Env preprocessorEnv = new PreprocessorEnv();

	private final ExpressionInterpreter expressionInterpreter = ExpressionInterpreter.newInstance();

	/**
	 Create a new, one time use, preprocessor for header files.
	 This will wrap processFile in a {@link HeaderFileTextProvider.BasicFileInput} instance

	 @param processFile The file to fully preprocess
	 @param parserContext context to use
	 */
	public Preprocessor(@NotNull File processFile, @NotNull HeaderParserContext parserContext) throws IOException {
		this(new HeaderFileTextProvider.BasicFileInput(processFile), parserContext);
	}

	/**
	 Create a new, one time use, preprocessor for header files.
	 This will wrap processFile in a {@link HeaderFileTextProvider.BasicFileInput} instance

	 @param processFile the {@link HeaderFileTextProvider} that will be used to retrieve text from a file and fully preprocess it
	 @param parserContext context to use
	 */
	public Preprocessor(@NotNull HeaderFileTextProvider processFile, @NotNull HeaderParserContext parserContext) throws IOException {
		this.processFile = processFile;
		this.parserContext = parserContext;

		File preprocessedResults = new File(parserContext.getTempDirectory().getAbsolutePath() + "/" + processFile.getFileName() + ".preprocessed");
		preprocessedResults.createNewFile();
		if (!preprocessedResults.exists()) {
			throw new IOException("Couldn't create the .preprocessed result file");
		}

		temporaryFullyPreprocessedResultFile = preprocessedResults;

		writer = new UTF8FileWriter(temporaryFullyPreprocessedResultFile);
	}


	/**
	 Run the preprocessor and get a stream to read the preprocesed output

	 @return preprocessed output
	 @throws Exception when error occurred
	 */
	@NotNull
	public Preprocessor.PreprocessorFileReader run() throws Exception {
		if (preprocessed) {
			throw new IllegalStateException("preprocessor already run");
		}
		preprocessed = true;

		//setup macros that are predefined
		//https://community.bistudio.com/wiki/PreProcessor_Commands
		{
			// With this config parser macro you can evaluate expressions, including previously assigned internal variables.
			// Unlike with __EXEC, __EVAL supports multiple parentheses
			defined.put("__EVAL", new ParameterDefineValue(new String[]{"a"}, "a"));// equivalent to #define __EVAL(a) a

			// This keyword gets replaced with the line number in the file where it is found.
			// For example, if __LINE__ is found on the 10th line of a file, the word __LINE__ will be replaced with the number 10.
			defined.put("__LINE__", new DefineMacroContent.StringDefineValue("`THIS VALUE SHOULD NOT HAVE BEEN WRITTEN`"));

			//This keyword gets replaced with the CURRENT file being processed.
			defined.put("__FILE__", new DefineMacroContent.StringDefineValue("`THIS VALUE SHOULD NOT HAVE BEEN WRITTEN`"));

			// This config parser macro allows you to assign values to internal variables. These variables can be used to create complex macros with counters for example.
			// Also, __EXEC can't write values.
			//Also, __EXEC supports statements and semicolons, __EVAL doesn't
			defined.put("__EXEC", new ParameterDefineValue(new String[]{"a"}, "a"));// equivalent to #define __EXEC(a) a
		}

		StringBuilderReference br = new StringBuilderReference(new StringBuilder(0));
		processNow(processFile, null, br);

		writer.flush();
		writer.close();

		expressionInterpreter.shutdownAndDisable();

		return new PreprocessorFileReader(temporaryFullyPreprocessedResultFile);
	}

	/**
	 Setup the preprocessor for preprocessing the given file and then preprocess it with {@link #doProcess(HeaderFileTextProvider, StringBuilderReference)}.

	 @param toProcess the file to preprocess
	 @param parentFile if null, the processor has just been started. If not null, the file that invoked this (needs #include) will be this parameter
	 @param builderReference where to write preprocess output
	 @throws Exception when error occurred
	 */
	private void processNow(@NotNull HeaderFileTextProvider toProcess, @Nullable HeaderFileTextProvider parentFile, @NotNull StringBuilderReference builderReference) throws Exception {
		if (processingFiles.contains(toProcess.getFilePath())) {
			if (parentFile == null) {
				throw new IllegalStateException("parentFile shouldn't be null here");
			}
			error(String.format(
					bundle.getString("Error.Preprocessor.Parse.circular_include_f"),
					toProcess.getFileName(),
					parentFile.getFileName()
			));
		}
		//store the old builder so that after processNow finishes on the requested file, the builderReference can be reset
		StringBuilder oldBuilder = builderReference.getBuilder();

		// Add the old builder's contents. This will be invoked at the start of preprocessing and when an #include has been discovered
		writer.write(oldBuilder.toString());
		writer.flush();

		// Create a new builder for the new file.
		StringBuilder textContent = new StringBuilder((int) toProcess.getFileLength());
		builderReference.setBuilder(textContent);

		//update state and place the toProcess file on the processing files stack
		processingFiles.add(toProcess.getFileName());
		preprocessStack.push(new PreprocessState(toProcess));

		//process the file given
		doProcess(toProcess, builderReference);

		writer.write(builderReference.getBuilder().toString());
		writer.flush();

		//reset the builder reference for reading a new part
		builderReference.setBuilder(new StringBuilder(Math.max(10, oldBuilder.capacity() - oldBuilder.length())));

		//no longer processing that file, so we can removed from preprocessStack
		preprocessStack.pop();

		processedFiles.add(processingFiles.pop()); //don't worry about duplicate includes. That should be handled elsewhere
	}

	/**
	 Fully preprocess the given file. This method should be invoked after a {@link PreprocessState} is present in {@link #preprocessStack}

	 @param processFile the file to preprocess
	 @param fileContent where to write the results to. This reference will change if an #include is discovered.
	 See {@link #processNow(HeaderFileTextProvider, HeaderFileTextProvider, StringBuilderReference)} implementation for more info.
	 @throws Exception when error occurred
	 */
	private void doProcess(@NotNull HeaderFileTextProvider processFile, @NotNull StringBuilderReference fileContent) throws Exception {
		Scanner scan = processFile.newTextScanner();
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

					//exclude preprocessing comments
					int indexOfLineComment = line.indexOf("//");
					int indexOfBlockComment = line.indexOf("/*");
					final boolean hasLineComment = indexOfLineComment >= 0;
					final boolean hasBlockComment = indexOfBlockComment >= 0;
					if (hasLineComment && (!hasBlockComment || indexOfLineComment < indexOfBlockComment)) {
						//if line comment comes before a block comment, let the line comment take priority

						//is line comment
						if (indexOfLineComment == 0) {
							fileContent.append(line);
						} else {
							preprocessText(line.substring(0, indexOfLineComment), fileContent);
							fileContent.append(line.substring(indexOfLineComment));
						}
						fileContent.append('\n');

					} else if (hasBlockComment && (!hasLineComment || indexOfBlockComment < indexOfLineComment)) {
						//if line block comment comes before a line comment, let the block comment take priority

						//is block comment
						int end = line.indexOf("*/");
						boolean endOfCommentOnSameLine = end >= 0;

						if (indexOfBlockComment > 0 && endOfCommentOnSameLine) {
							preprocessText(line.substring(0, indexOfBlockComment), fileContent);
						}

						if (endOfCommentOnSameLine) {
							//write the comment
							fileContent.append(line, indexOfBlockComment, end);
						}

						//if end is < 0, end of block comment is on different line
						while (end < 0 && scan.hasNextLine()) {
							fileContent.append(line);
							fileContent.append('\n');
							line = scan.nextLine();
							incrementLineNumber();
							end = line.indexOf("*/");
						}
						if (end > 0 && !endOfCommentOnSameLine) {
							//write the text that comes before */
							fileContent.append(line, 0, end);
						}
						fileContent.append("*/");
						//here, we have made it past the block comment
						//now preprocess the rest of the line
						String noCommentLine = line.substring(end + 2); //end + 2 to skip past */
						preprocessText(noCommentLine, fileContent);

						fileContent.append('\n');
					} else {
						preprocessText(line, fileContent);
						fileContent.append('\n');
					}
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

			//trim macroText to remove and potential tabs or spaces before the macro
			//i.e.:               #include ...
			//     ^^white space^^
			String macroText = macroBuilder.toString().trim();
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
					int rightMatchIndex = -1;
					switch (left) {
						case '"': {
							rightMatchIndex = macroContent.indexOf('"', 1);
							break;
						}
						case '<': {
							rightMatchIndex = macroContent.indexOf('>', 1);
							break;
						}
						case '\'': {
							rightMatchIndex = macroContent.indexOf('\'', 1);
							break;
						}
					}
					if (rightMatchIndex < 0) {
						error(bundle.getString("Error.Preprocessor.Parse.bad_include_format"));
					}

					String filePath = macroContent.substring(1, rightMatchIndex);

					HeaderFileTextProvider f = processFile.resolvePath(filePath);
					if (f == null) {
						error(String.format(bundle.getString("Error.Preprocessor.Parse.bad_file_path_f"), filePath));
					}

					processNow(f, processFile, fileContent);
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
						final boolean whitespace = c == ' ' || c == '\t';
						if (whitespace || c == '(') {
							definedVar = macroContent.substring(0, i);
							if (!whitespace) {
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
					//ignore anything else
					System.out.println(String.format("Preprocessor.java: Ignored unknown macro: %s. (No Errors)", macroName));
					break;
				}
			}
		}
		scan.close();
	}

	/**
	 Fully preprocess the given text and write the result to <code>writeTo</code>

	 @param base the unprocessed line to preprocess
	 @param writeTo where to write results
	 @throws HeaderParseException when error occurred
	 */
	private void preprocessText(@NotNull String base, @NotNull StringBuilderReference writeTo) throws HeaderParseException {
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

	/**
	 Write the preprocessed body of the macro to <code>writeTo</code>. Example bodies: <br>
	 <ul>
	 <li><code>#define D 100</code> - body is 100. Macro key is D</li>
	 <li><code>#define D_ARG(ARG) #ARG</code> - body is #ARG. Macro key is D_ARG This is also a parameter macro.</li>
	 </ul>

	 @param entry the entry that contains the macro key and the macro body stored in a {@link DefineValue} instance
	 @param parameterText the text that the user put inside the parameter macro.
	 This value should be null if not writing a parameter macro and should not be null if writing a parameter macro.
	 Example parameterText: (with D_ARG example above): D_ARG(42) and parameter text is (42)<br>
	 Another example : MACRO(parameterText,goes,between,these,parenthesis)
	 @param writeTo where to write the preprocessed body to
	 @throws HeaderParseException when an error occurred
	 */
	private void writeDefineValue(@NotNull Entry<String, DefineValue> entry, @Nullable String parameterText, @NotNull StringBuilderReference writeTo) throws HeaderParseException {
		//This method will write a macro's body and then recursively call preprocessText() on the macro body to handle any possible nested macro references.
		//It will do that by having a "buffer" StringBuilderReference that this method will write to. Then the buffer's text content will be passed into preprocessText().
		//There should not be stack overflow because preprocessText() will invoke this method only when a macro was matched in its input parameter

		//In the case that the given macro is a parameter macro, the parameter's of the macro will be written with the body, and THEN preprocessText() will be invoked.
		//So, it is a possible scenario that a parameter macro will write a body that happens to be a predefined macro name.
		//Example:
		// #define ARG 1
		// #define PARAM(A) A#RG
		// f=PARAM(A) // will result in f=1
		//
		//In Arma 3, the output will not be f=1, but it isn't clear what happens behind the scenes and this outcome makes the most sense for our code.
		//So this is the intended outcome.

		StringBuilderReference buffer = new StringBuilderReference(new StringBuilder());

		String entryValueText = entry.getValue().getResultTemplateText();

		if (entry.getValue() instanceof ParameterDefineValue) {
			if (parameterText == null) {
				throw new IllegalArgumentException("parameterText should not be null if entry is a ParameterDefineValue");
			}

			if (entry.getKey().equals("__EXEC")) {
				handle__Exec(get__ExecBody(parameterText));
				return;
			}

			if (entry.getKey().equals("__EVAL")) {
				handle__Eval(parameterText, writeTo);
				return;
			}

			ParameterDefineValue paramDefineValue = (ParameterDefineValue) entry.getValue();

			ParameterDefineValue parameterValue = (ParameterDefineValue) entry.getValue();
			String[] args = parameterText.substring(1, parameterText.length() - 1).split(",");
			final int numParams = parameterValue.getParams().length;
			if (args.length != numParams) {
				error(String.format(bundle.getString("Error.Preprocessor.Parse.wrong_amount_of_params_f"), numParams, args.length));
			}

			for (int i = 0; i < args.length; i++) {
				args[i] = args[i].trim();
			}

			Matcher m = macroParamOutputTextPattern.matcher(entryValueText);

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
							buffer.append(entryValueText, ind, startBefore);
						} else {
							buffer.append(entryValueText, ind, start);
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
				ind = m.end("PARAM");

				String paramArg = args[paramInd];
				if (startsWithIgnoreSpace(paramArg, "__EVAL(")) {
					//NOTE we don't need to handle __EXEC because you can't have semicolons in __EVAL
					handle__Eval(get__EvalBody(paramArg), buffer);
				} else {
					//check if paramArg is a macro itself like: TEST(ANOTHER_MACRO)
					for (Entry<String, DefineValue> entry1 : defined.entrySet()) {
						if (entry1 instanceof ParameterDefineValue) {
							continue;
						}
						if (entry1.getKey().equals(paramArg)) {
							paramArg = entry1.getValue().getResultTemplateText();
							break;
						}
					}
					if (quote) {
						buffer.append('"');
					}

					buffer.append(paramArg);
					if (quote) {
						buffer.append('"');
					}

					if (ind + 1 < entryValueText.length() && entryValueText.charAt(ind) == '#' && entryValueText.charAt(ind + 1) == '#') {
						ind += 2;
					}
				}

			}
			if (ind < entryValueText.length()) {
				buffer.append(entryValueText, ind, entryValueText.length());
			}

		} else {
			if (parameterText != null) {
				error(bundle.getString("Error.Preprocessor.Parse.unexp_lparen"));
			}

			switch (entry.getKey()) {
				case "__LINE__": {
					buffer.append(currentState().lineNumber + "");
					break;
				}
				case "__FILE__": {
					buffer.append(currentState().processingFile.getFileName());
					break;
				}
				default: {
					buffer.append(entryValueText);
					break;
				}
			}

		}

		//this recursive call is to handle any possible macro references after the body of this macro was written
		preprocessText(buffer.toString(), writeTo);

	}

	/** @return the text between parentheses of __EVAL() (and will not include the parentheses) */
	@NotNull
	private String get__EvalBody(@NotNull String __evalMacro) {
		final int eval = 7; //length of __EVAL(
		return __evalMacro.substring(eval, __evalMacro.length() - 1);
	}

	/** @return the text between parentheses of __EXEC() (and will not include the parentheses) */
	@NotNull
	private String get__ExecBody(@NotNull String __execMacro) {
		final int exec = 7; //length of __EXEC(
		return __execMacro.substring(exec, __execMacro.length() - 1);
	}

	/** Evaluate __EVAL(<code>parameterText</code>) and write it to <code>writeTo</code> */
	private void handle__Eval(@Nullable String parameterText, @NotNull Preprocessor.StringBuilderReference writeTo) throws HeaderParseException {
		try {
			Value value = expressionInterpreter.evaluate(parameterText, preprocessorEnv).get(10, TimeUnit.SECONDS); //.get() to block until eval is done
			//if value is a decimal, the toString method should properly use DecimalFormat on the number for getting a String
			writeTo.append(value.toString());
		} catch (Exception e) {
			if (e instanceof InterruptedException) {
				throw new HeaderParseException(String.format(bundle.getString("Error.Preprocessor.Parse.evaluate_timer_end_f"), parameterText));
			}
			throw new HeaderParseException(e.getMessage(), e);
		}
	}

	/** Evaluate __EXEC(<code>parameterText</code>). This will not actually write anything */
	private void handle__Exec(@Nullable String parameterText) throws HeaderParseException {
		try {
			expressionInterpreter.evaluateStatements(parameterText, preprocessorEnv).get(10, TimeUnit.SECONDS); //.get() to block until eval is done
			//write nothing
		} catch (Exception e) {
			if (e instanceof InterruptedException) {
				throw new HeaderParseException(String.format(bundle.getString("Error.Preprocessor.Parse.evaluate_timer_end_f"), parameterText));
			}
			throw new HeaderParseException(e.getMessage(), e);
		}
	}

	/** Increment the current state's line number */
	private void incrementLineNumber() {
		currentState().lineNumber++;
	}

	/**
	 @return The current state.
	 */
	@NotNull
	private PreprocessState currentState() {
		return preprocessStack.peek();
	}

	/**
	 Throw a {@link HeaderConversionException} with the message body as string.
	 The body will be wrapped in some other info as well. This method will invoke {@link #parseException(String)}
	 to create the exception.

	 @see #parseException(String)
	 */
	protected void error(@NotNull String string) throws HeaderParseException {
		throw parseException(string);
	}

	/**
	 Create a {@link HeaderParseException} with the given message body wrapped around some info

	 @return an exception
	 @see #error(String)
	 */
	protected HeaderParseException parseException(@NotNull String s) {
		return new HeaderParseException(String.format(
				bundle.getString("Error.Preprocessor.Parse.error_wrapper_f"),
				currentState().lineNumber,
				currentState().processingFile.getFilePath(),
				s
		));
	}


	/**
	 A {@link Env} for {@link Preprocessor}. This environment is for handling __EXEC and __EVAL for the preprocessor.

	 @author Kayler
	 @since 05/24/2017
	 */
	public class PreprocessorEnv extends SimpleEnv {

		/** Instead of constantly computing a macro's body as an expression, cache the values calculated. */
		private HashMap<Entry<String, DefineValue>, Value> cachedValues = new HashMap<>();

		public PreprocessorEnv() {
			super(new UnaryCommandValueProviderImpl());
		}

		/**
		 Search all defined macros and use their keys as Identifiers. Will also check for any values created from __EXEC.
		 No parameter macros should be allowed to be used as identifiers inside __EVAL!
		 */
		@Override
		@Nullable
		public Value getValue(@NotNull String identifier) {
			Value v = getValueFromDefined(identifier);
			if (v != null) {
				return v;
			}
			return super.getValue(identifier);
		}

		private Value getValueFromDefined(@NotNull String identifier) {
			for (Entry<String, DefineValue> defined : defined.entrySet()) {
				if (identifier.equals(defined.getKey())) {
					Value value = cachedValues.computeIfAbsent(
							defined,
							stringDefineValueEntry -> {
								if (defined.getValue() instanceof ParameterDefineValue) {
									//a parameter macro should not exist inside __EVAL or __EXEC
									throw new RuntimeException(parseException(bundle.getString("Error.Preprocessor.Parse.unexpected_parameter_macro")));
								}
								DefineMacroContent.StringDefineValue sdv = (DefineMacroContent.StringDefineValue) defined.getValue();
								try {
									return expressionInterpreter.evaluate(sdv.getResultTemplateText(), preprocessorEnv).get();
								} catch (Exception e) {
									throw new RuntimeException(
											String.format(bundle.getString("Error.Preprocessor.Parse.couldnt_evaluate_macro_body_f"), sdv.getResultTemplateText()),
											e
									);
								}
							}
					);
					return value;
				}
			}
			return null;
		}
	}


	protected static class PreprocessState {
		private int lineNumber = 0;
		private final HeaderFileTextProvider processingFile;

		public PreprocessState(@NotNull HeaderFileTextProvider processingFile) {
			this.processingFile = processingFile;
		}

		public PreprocessState(@NotNull File processingFile) {
			this.processingFile = new HeaderFileTextProvider.BasicFileInput(processingFile);
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

	static class StringBuilderReference {
		private StringBuilder b;

		public StringBuilderReference(@NotNull StringBuilder b) {
			this.b = b;
		}

		public void setBuilder(@NotNull StringBuilder b) {
			this.b = b;
		}

		@NotNull
		public StringBuilder getBuilder() {
			return b;
		}

		/** @see StringBuilder#append(char) */
		public void append(char c) {
			b.append(c);
		}

		/** @see StringBuilder#append(CharSequence) */
		public void append(@NotNull String s) {
			b.append(s);
		}

		/** @see StringBuilder#append(CharSequence, int, int) */
		public void append(String s, int start, int end) {
			b.append(s, start, end);
		}

		public void append(StringBuilder b) {
			b.append(b);
		}

		public void append(StringBuilderReference b) {
			b.append(b.getBuilder());
		}

		@Override
		public String toString() {
			return b.toString();
		}
	}

	/**
	 A simple wrapper {@link InputStream} so we can easily change how preprocessed results are stored,
	 without changing how the results are read
	 */
	protected static class PreprocessorFileReader extends UTF8FileReader {

		/**
		 Create a new Reader that reads the fully preprocessed file's contents

		 @param preprocessedFile the file that contains the fully preprocessed contents
		 */
		protected PreprocessorFileReader(@NotNull File preprocessedFile) throws FileNotFoundException {
			super(preprocessedFile);
		}

	}

	private static class UnaryCommandValueProviderImpl implements UnaryCommandValueProvider {
		private final ArmaResolution resolution;

		public UnaryCommandValueProviderImpl() {
			this.resolution = new ArmaResolution(ScreenDimension.D960, ArmaUIScale.DEFAULT);
		}

		@Override
		public @NotNull Value safeZoneX() {
			return new Value.NumVal(resolution.getSafeZoneX());
		}

		@Override
		public @NotNull Value safeZoneY() {
			return new Value.NumVal(resolution.getSafeZoneY());
		}

		@Override
		public @NotNull Value safeZoneW() {
			return new Value.NumVal(resolution.getSafeZoneW());
		}

		@Override
		public @NotNull Value safeZoneH() {
			return new Value.NumVal(resolution.getSafeZoneH());
		}

		@Override
		public @NotNull Value getResolution() {
			Value.NumVal width = new Value.NumVal(resolution.getScreenWidth());
			Value.NumVal height = new Value.NumVal(resolution.getScreenHeight());
			Value.NumVal viewportWidth = new Value.NumVal(resolution.getViewportWidth());
			Value.NumVal viewportHeight = new Value.NumVal(resolution.getViewportHeight());
			Value.NumVal aspectRatio = new Value.NumVal(resolution.getAspectRatio());
			Value.NumVal uiScale = new Value.NumVal(resolution.getUIScale().getValue());

			return _getResolution(
					width,
					height,
					viewportWidth,
					viewportHeight,
					aspectRatio,
					uiScale
			);
		}
	}
}
