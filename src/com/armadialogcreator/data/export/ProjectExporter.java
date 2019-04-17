package com.armadialogcreator.data.export;

import com.armadialogcreator.core.FilePathUser;
import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

/**
 A {@link Project} exporter for converting. Converts the {@link Project} into header file code (.h, .hh, etc)

 @author Kayler
 @since 09/13/2016 */
public class ProjectExporter {
	private static final String CONTROLS_BACKGROUND = "ControlsBackground";
	private static final String OBJECTS = "Objects";
	private static final String CONTROLS = "Controls";
//
//	/**
	//	 Exports the whole {@link ClassicProjectExportConfiguration#getProject()} to the configuration's specified files.
//
//	 @param configuration config to use
//	 @see #exportDisplayAndMacros(Writer, Writer)
//	 */
	//	public static void exportProject(@NotNull ClassicProjectExportConfiguration configuration) throws IOException {
//		new ProjectExporter(configuration).exportProject();
//	}
//
//	/**
	//	 Exports the {@link ClassicProjectExportConfiguration#getProject()} to the given output streams. This method will only
//	 export the {@link Project#getEditingDisplay()}, {@link Project#getMacroRegistry()}, and
//	 {@link Project#getProjectCustomControlClassRegistry()}.
//	 The streams will not be closed once this method finishes!
//
//	 @param configuration config to use
//	 @param displayWriter writer to use for writing the {@link Project#getEditingDisplay()}
//	 @param macrosWriter writer to use for writing {@link Project#getMacroRegistry()}, or null to write to same
//	 file as <code>displayOutputStream</code>
//	 */
	//	public static void exportDisplayAndMacros(@NotNull ClassicProjectExportConfiguration configuration,
//											  @NotNull Writer displayWriter,
//											  @Nullable Writer macrosWriter) throws IOException {
//		new ProjectExporter(configuration).exportDisplayAndMacros(displayWriter, macrosWriter);
//	}
//
//	/**
//	 Exports {@link Project#getWorkspaceCustomControlClassRegistry()} to the given output stream.
//
//	 @param configuration config to use
//	 @param writer stream to use
//	 */
	//	public static void exportWorkspaceCustomControls(@NotNull ClassicProjectExportConfiguration configuration,
//													 @NotNull Writer writer) throws IOException {
//		new ProjectExporter(configuration).exportWorkspaceCustomControls(writer);
//	}
//
//	@NotNull
	//	public static String getMacrosFileName(@NotNull ClassicProjectExportConfiguration configuration) {
//		return configuration.getExportClassName() + "_Macros" + configuration.getHeaderFileType().getExtension();
//	}
//
//	@NotNull
	//	public static String getDisplayFileName(@NotNull ClassicProjectExportConfiguration configuration) {
//		return configuration.getExportClassName() + configuration.getHeaderFileType().getExtension();
//	}

	@NotNull
	public static String getExportValueString(@NotNull SerializableValue value, @NotNull PropertyType type, @NotNull String exportDir) {
		String[] arr = value.getAsStringArray();
		StringBuilder ret = new StringBuilder();
		String v;
		int[] convertToFilePath = value instanceof FilePathUser ? ((FilePathUser) value).getIndicesThatUseFilePaths()
				: null;
		for (int i = 0; i < arr.length; i++) {
			v = arr[i];
			for (int quoteIndex : type.getIndexesWithQuotes()) {
				if (quoteIndex == i) {
					if (convertToFilePath != null) {
						for (int c : convertToFilePath) {
							if (c == i) {
								try {
									v = Paths.get(exportDir).relativize(Paths.get(v)).toString();
								} catch (IllegalArgumentException ignore) {
									//if we get an exception, we'll just write the file path given
								}
							}
						}
					}
					v = "\"" + v + "\"";
					break;
				}
			}
			ret.append(v).append(i != arr.length - 1 ? "," : "");
		}
		return (type.getPropertyValuesSize() > 1) ? "{" + ret + "}" : ret.toString();
	}
//
	//	public static void exportControlClass(@NotNull ClassicProjectExportConfiguration configuration, @NotNull ControlClassOld controlClass, @NotNull Writer writer) throws IOException {
//		ProjectExporter exporter = new ProjectExporter(configuration);
//		BufferedIndentedStringBuilder builder = getBuilder(writer);
//		exporter.writeControlClass(builder, controlClass, null);
//		writer.write(builder.toString());
//		writer.flush();
//	}
//
//	private static BufferedIndentedStringBuilder getBuilder(@NotNull Writer writer) {
//		return new BufferedIndentedStringBuilder(4, true, 20000/*20 kb*/, s -> {
//			try {
//				writer.write(s);
//				writer.flush();
//			} catch (IOException e) {
//				throw new IllegalStateException(e);
//			}
//			return null;
//		});
//	}
//
//	private final Project project;
	//	private final ClassicProjectExportConfiguration conf;
//	private final ResourceBundle bundle = Lang.ApplicationBundle();
//
//	private BufferedIndentedStringBuilder displayStringBuilder;
//
//	private BufferedIndentedStringBuilder macrosStringBuilder;
//
	//	public ProjectExporter(@NotNull ClassicProjectExportConfiguration configuration) {
//		this.conf = configuration;
//		this.project = configuration.getProject();
//
//	}
//
//	/**
	//	 Exports the {@link ClassicProjectExportConfiguration#getProject()} to the configuration's specified files.
//	 <p>
//	 This invokes {@link #exportDisplayAndMacros(Writer, Writer)} and {@link #exportWorkspaceCustomControls()}
//	 */
//	public void exportProject() throws IOException {
//		initConfExportDirectory();
//		File exportDirectory = conf.getExportDirectory();
//
//		exportDirectory.mkdir();
//
//		File exportDisplayFile = conf.getFileForExportDirectory(getDisplayFileName(conf));
//		exportDisplayFile.createNewFile();
//		UTF8FileWriter fosDisplay = new UTF8FileWriter(exportDisplayFile);
//
//		Writer writerMacros = null;
//		if (conf.shouldExportMacrosToFile()) {
//			final File macrosExportFile = conf.getFileForExportDirectory(getMacrosFileName(conf));
//			macrosExportFile.createNewFile();
//			writerMacros = new UTF8FileWriter(macrosExportFile);
//		}
//
//		exportDisplayAndMacros(fosDisplay, writerMacros);
//
//		fosDisplay.close();
//		if (writerMacros != null) {
//			writerMacros.close();
//		}
//
//		this.exportWorkspaceCustomControls();
//	}
//
//	private void initConfExportDirectory() {
//		if (!conf.getExportDirectory().exists()) {
//			conf.getExportDirectory().mkdirs();
//		}
//		if (!conf.getExportDirectory().isDirectory()) {
//			throw new IllegalArgumentException("exportLocation ('" + conf.getExportDirectory().getPath() + "') is not a directory");
//		}
//	}
//
//	/**
	//	 Exports the {@link ClassicProjectExportConfiguration#getProject()} to the given output streams.
//	 The streams will not be closed once this method finishes!
//
//	 @param displayWriter stream to use for writing the {@link Project#getEditingDisplay()}
//	 @param macrosWriter stream to use for writing {@link Project#getMacroRegistry()}, or null to write to same
//	 file as <code>displayWriter</code>
//	 */
//	public void exportDisplayAndMacros(@NotNull Writer displayWriter, @Nullable Writer macrosWriter)
//			throws IOException {
//		if (macrosWriter == null || !conf.shouldExportMacrosToFile()) {
//			macrosWriter = displayWriter; //save the macros inside the display header file
//		}
//
//		displayStringBuilder = getBuilder(displayWriter);
//		macrosStringBuilder = getBuilder(macrosWriter);
//
//		exportMacros(macrosStringBuilder);
//		//write remainder stuff
//		macrosWriter.write(macrosStringBuilder.toString());
//
//		exportDisplay(displayStringBuilder);
//
//		//write the remainder of the string builders
//		displayWriter.write(displayStringBuilder.toString());
//
//		//one last flush
//		displayWriter.flush();
//		macrosWriter.flush();
//
//		//don't close streams
//
//	}
//
//	public void exportWorkspaceCustomControls() throws IOException {
//		initConfExportDirectory();
//
//		File exportFile = conf.getFileForExportDirectory(conf.getCustomClassesExportFileName());
//		exportFile.createNewFile();
//		UTF8FileWriter writer = new UTF8FileWriter(exportFile);
//
//		exportWorkspaceCustomControls(writer);
//
//		writer.flush();
//		writer.close();
//	}
//
//	public void exportWorkspaceCustomControls(@NotNull Writer writer) throws IOException {
//		IndentedStringBuilder stringBuilder = getBuilder(writer);
//
//		if (conf.shouldPlaceAdcNotice()) {
//			writelnComment(stringBuilder, bundle.getString("Misc.adc_export_notice"));
//		}
//
//		//create a header guard
//		String headerGuard = "HG_" + conf.getCustomClassesExportFileName();
//		Pattern p = Pattern.compile("(\\s+)|([^a-zA-Z_]+)");
//		Matcher matcher = p.matcher(headerGuard);
//		StringBuffer sb = new StringBuffer();
//		while (matcher.find()) {
//			String g = matcher.group(1);
//			if (g != null) {
//				matcher.appendReplacement(sb, "_");
//				continue;
//			}
//			g = matcher.group(2);
//			if (g == null) {
//				throw new IllegalStateException("nothing was matched");
//			}
//			matcher.appendReplacement(sb, "");
//		}
//		matcher.appendTail(sb);
//		headerGuard = sb.toString();
//		writeln(stringBuilder, "#ifndef " + headerGuard);
//		writeln(stringBuilder, "#define " + headerGuard + " 1");
//		writelnComment(stringBuilder, "Create a header guard to prevent duplicate include.");
//		writeln(stringBuilder, "");
//
//		exportCustomControlClasses(stringBuilder, conf.getProject().getWorkspaceCustomControlClassRegistry());
//
//		writeln(stringBuilder, "#endif");
//
//		//write remainder of text
//		writer.write(stringBuilder.toString());
//
//		writer.flush();
//		//don't close stream
//	}
//
//	private void exportCustomControlClasses(@NotNull IndentedStringBuilder stringBuilder,
//											@NotNull CustomControlClassRegistry registry) throws IOException {
//		for (ControlClassOld cc : sortControlClasses(registry.controlClassIterator())) {
//			for (CustomControlClass ccc : registry) {
//				if (cc == ccc.getControlClass()) {
//					if (ccc.getComment() != null && ccc.getComment().trim().length() > 0) {
//						writelnComment(stringBuilder, ccc.getComment());
//					}
//					writeControlClass(stringBuilder, ccc.getControlClass(), null);
//				}
//			}
//		}
//	}
//
//	private void writeln(@NotNull IndentedStringBuilder stringBuilder, @NotNull String s) {
//		stringBuilder.append(s);
//		stringBuilder.append('\n');
//	}
//
//	private void write(@NotNull IndentedStringBuilder stringBuilder, @NotNull String s) {
//		stringBuilder.append(s);
//	}
//
//	private void exportMacros(@NotNull IndentedStringBuilder stringBuilder) throws IOException {
//		List<Macro> macros = project.getMacroRegistry().getMacros();
//		for (Macro macro : macros) {
//			if (macro.getComment() != null && macro.getComment().length() != 0) {
//				writelnComment(stringBuilder, macro.getComment());
//			}
//			write(stringBuilder, "#define ");
//			write(stringBuilder, macro.getKey());
//			write(stringBuilder, " ");
//			writeln(stringBuilder, getExportValueString(
//					macro.getValue(),
//					macro.getPropertyType(), conf.getExportDirectory().getAbsolutePath())
//			);
//		}
//		if (macros.size() > 0) {
//			stringBuilder.append('\n');
//		}
//	}
//
//	private void exportDisplay(@NotNull IndentedStringBuilder stringBuilder) throws IOException {
//		File customClassesExportFile = conf.getFileForExportDirectory(conf.getCustomClassesExportFileName());
//		if (!customClassesExportFile.exists() &&
//				!conf.getProject().getWorkspaceCustomControlClassRegistry().getControlClassList().isEmpty()) {
//			customClassesExportFile.getParentFile().mkdirs();
//			customClassesExportFile.createNewFile();
//		}
//
//		if (conf.shouldPlaceAdcNotice()) {
//			writelnComment(stringBuilder, bundle.getString("Misc.adc_export_notice"));
//			writeln(stringBuilder, "");
//		}
//
//		if (conf.shouldExportMacrosToFile()) {
//			writeln(stringBuilder, "#include \"" + getMacrosFileName(conf) + "\"");
//			writeln(stringBuilder, "");
//		}
//		writeln(stringBuilder, "#include \"" + conf.getCustomClassesExportFileName() + "\"");
//		if (project.getProjectDescription() != null && project.getProjectDescription().length() > 0) {
//			writelnComment(stringBuilder, project.getProjectDescription());
//		}
//
//		exportCustomControlClasses(stringBuilder, conf.getProject().getProjectCustomControlClassRegistry());
//
//		ArmaDisplay display = project.getEditingDisplay();
//
//		writeClass(stringBuilder, conf.getExportClassName(), null, stringBuilderCopy -> {
//			//write display properties
//
//			writeControlProperties(displayStringBuilder, display.getDisplayProperties());
//			displayStringBuilder.append('\n');
//
//
//			//write background controls
//			writeClass(stringBuilder, CONTROLS_BACKGROUND, null, sb -> {
//				for (ArmaControl control : sortControlClasses(display.getBackgroundControls())) {
//					writeControl(stringBuilder, control);
//				}
//				return null;
//			});
//
//			//write controls
//			writeClass(stringBuilder, CONTROLS, null, sb -> {
//				for (ArmaControl control : sortControlClasses(display.getControls())) {
//					writeControl(stringBuilder, control);
//				}
//				return null;
//			});
//
//
//			return null;
//		});
//
//	}
//
//	private void writeControl(@NotNull IndentedStringBuilder stringBuilder, @NotNull ArmaControl control) {
//		//write control body
//		writeControlClass(stringBuilder, control, sb -> {
//			if (control instanceof ArmaControlGroup) {
//				//write group's "Controls" class
//				writeClass(sb, CONTROLS, null, sb2 -> {
//					for (ArmaControl subControl : sortControlClasses(((ArmaControlGroup) control).getControls())) {
//						writeControl(sb2, subControl);
//					}
//					return null;
//				});
//
//			}
//			return null;
//		});
//	}
//
//	private void writeControlClass(@NotNull IndentedStringBuilder stringBuilder, @NotNull ControlClassOld controlClass,
//								   @Nullable Function<IndentedStringBuilder, Void> insertBodyFunc) {
//		writeClass(stringBuilder, controlClass.getClassName(), controlClass.getExtendClass() == null ? null : controlClass.getExtendClass().getClassName(), sb -> {
//			writeControlProperties(sb, controlClass.getAllChildProperties());
//			for (ControlClassOld nested : sortControlClasses(controlClass.getAllNestedClasses())) {
//				writeControlClass(stringBuilder, nested, null);
//			}
//			if (insertBodyFunc != null) {
//				insertBodyFunc.apply(stringBuilder);
//			}
//			return null;
//		});
//	}
//
//	private void writeClass(@NotNull IndentedStringBuilder stringBuilder, @NotNull String className, @Nullable String extendClassName, @NotNull Function<IndentedStringBuilder, Void> writeBodyFunc) {
//		//class example : thing
//		//{
//		//	writeBodyFunc.apply(stringBuilder)
//		//};
//
//		stringBuilder.append("class ");
//		stringBuilder.append(className);
//		if (extendClassName != null) {
//			stringBuilder.append(" : ");
//			stringBuilder.append(extendClassName);
//			stringBuilder.append(" ");
//		}
//
//		stringBuilder.append('\n');
//		stringBuilder.incrementTabCount();
//		stringBuilder.append('{');
//
//		stringBuilder.append('\n');
//
//		writeBodyFunc.apply(stringBuilder);
//
//		stringBuilder.decrementTabCount();
//		stringBuilder.append('\n');
//		stringBuilder.append("};");
//		stringBuilder.append('\n');
//	}
//
//	private void writeControlProperties(@NotNull IndentedStringBuilder stringBuilder, @NotNull Iterable<? extends ControlProperty> controlProperties) {
//		String itemFormatString = "%s = %s;";
//		String itemArrayFormatString = "%s[] = %s;";
//		for (ControlProperty property : controlProperties) {
//			if (property.getValue() == null/* && editor.isOptional()*/) { //can allow for partial implementation, so we don't need to check if it is optional
//				continue;
//			}
//			if (property.isInherited()) {
//				continue;
//			}
//			if (property.getMacro() != null) {
//				String format = itemFormatString;
//				Macro m = property.getMacro();
//				StringTableKey stringKey = null;
//				if (property.getMacro() instanceof StringTableKey) {
//					stringKey = (StringTableKey) property.getMacro();
//				}
//				if (m.getValue().getPropertyType().getPropertyValuesSize() != 1) {
//					format = itemArrayFormatString;
//				} else if (m.getValue() instanceof SVRaw) {
//					String rawString = ((SVRaw) m.getValue()).getString().trim();
//					if (rawString.charAt(0) == '{') {
//						format = itemArrayFormatString;
//					}
//				}
//
//				stringBuilder.append(String.format(format, property.getName(), stringKey != null ? stringKey.getHeaderMacroId() : property.getMacro().getKey()));
//			} else {
//				if (property.getValue().getAsStringArray().length == 1) {
//					String format = itemFormatString;
//					if (property.getValue() instanceof SVRaw) {
//						String rawString = ((SVRaw) property.getValue()).getString().trim();
//						if (rawString.charAt(0) == '{') {
//							format = itemArrayFormatString;
//						}
//					}
//					stringBuilder.append(
//							String.format(
//									format,
//									property.getName(),
//									getExportValueString(property.getValue(), property.getPropertyType(), conf.getExportDirectory().getAbsolutePath())
//							)
//					);
//				} else {
//					stringBuilder.append(
//							String.format(
//									itemArrayFormatString,
//									property.getName(),
//									getExportValueString(property.getValue(), property.getPropertyType(), conf.getExportDirectory().getAbsolutePath())
//							)
//					);
//				}
//			}
//			stringBuilder.append('\n');
//		}
//	}
//
//	/**
//	 Writes a comment and then appends a new line character. If the comment has a newline character inside it, the
//	 comment will be a block comment, otherwise the comment will be a single line comment (//etc).
//
//	 @param stringBuilder builder to use
//	 @param comment the comment
//	 */
//	private void writelnComment(@NotNull IndentedStringBuilder stringBuilder, String comment) throws IOException {
//		if (comment.contains("\n")) {
//			writeln(stringBuilder, "/*");
//			writeln(stringBuilder, comment);
//			writeln(stringBuilder, "*/");
//		} else {
//			writeln(stringBuilder, "//" + comment);
//		}
//	}
//
//	/**
//	 Combines all classes into a list and "sorts" them. This isn't a normal sort however.
//	 How it works:
//	 <ol>
//	 <li>The {@link ControlClassOld} instances that have no extend class
//	 ({@link ControlClassOld#getExtendClass()} == null) will appear at the start of the list</li>
//	 <li>If a {@link ControlClassOld} has an extend class, it will be appended after it's extend class has been appended to the list.</li>
//	 <li>If a {@link ControlClassOld} has an extend class that isn't in this iterable, it will be added to the end of the list</li>
//	 </ol>
//	 */
//	private static <T extends ControlClassOld> Iterable<T> sortControlClasses(@NotNull Iterable<T> controlClasses) {
//		LinkedList<T> toVisit = new LinkedList<>();
//		for (T cc : controlClasses) {
//			toVisit.add(cc);
//		}
//
//		List<T> sorted = new ArrayList<>(toVisit.size());
//
//		{
//			Iterator<T> iter = toVisit.iterator();
//			while (iter.hasNext()) {
//				T cc = iter.next();
//				if (cc.getExtendClass() == null) {
//					sorted.add(cc);
//					iter.remove();
//				}
//			}
//		}
//
//		while (!toVisit.isEmpty()) {
//			Iterator<T> iter = toVisit.iterator();
//			boolean didRemove = false;
//			while (iter.hasNext()) {
//				T cc = iter.next();
//				if (sorted.contains(cc.getExtendClass())) {
//					sorted.add(cc);
//					iter.remove();
//					didRemove = true;
//					continue;
//				}
//			}
//			if (!didRemove) {
//				T cc = toVisit.removeFirst();
//				sorted.add(cc);
//				continue;
//			}
//		}
//
//		return sorted;
//	}
//
//	private static class BufferedIndentedStringBuilder extends IndentedStringBuilder {
//
//		private final int cacheSize;
//		private int appendCount;
//		private Function<String, Object> onFull;
//
//		public BufferedIndentedStringBuilder(int tabSizeInSpaces, boolean useTabCharacter, int cacheSize, @NotNull Function<String, Object> onFull) {
//			super(tabSizeInSpaces, useTabCharacter);
//			this.cacheSize = cacheSize;
//			this.onFull = onFull;
//		}
//
//		@Override
//		public void append(char c) {
//			super.append(c);
//			appendCount++;
//			if (appendCount >= cacheSize) {
//				onFull.apply(this.toString());
//				this.getBuilder().delete(0, length());
//				appendCount = 0;
//			}
//		}
//	}
}
