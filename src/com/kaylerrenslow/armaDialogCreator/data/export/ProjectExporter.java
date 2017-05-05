package com.kaylerrenslow.armaDialogCreator.data.export;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ResourceBundle;

/**
 Created by Kayler on 09/13/2016.
 */
public class ProjectExporter {
	private final Project project;
	private final ProjectExportConfiguration configuration;

	private static final String CONTROLS_BACKGROUND = "ControlsBackground";
	private static final String OBJECTS = "Objects";
	private static final String CONTROLS = "Controls";

	private ResourceBundle bundle = Lang.ApplicationBundle();

	public ProjectExporter(@NotNull ProjectExportConfiguration configuration) {
		this.configuration = configuration;
		this.project = configuration.getProject();
	}

	public static void export(@NotNull ProjectExportConfiguration configuration) throws IOException {
		new ProjectExporter(configuration).export();
	}

	public static void export(@NotNull ProjectExportConfiguration configuration, @NotNull OutputStream displayOutputStream, @Nullable OutputStream macrosOutputStream) throws IOException {
		new ProjectExporter(configuration).export(displayOutputStream, macrosOutputStream);
	}

	public void export() throws IOException {
		if (!configuration.getExportLocation().exists()) {
			configuration.getExportLocation().mkdirs();
		}
		if (!configuration.getExportLocation().isDirectory()) {
			throw new IllegalArgumentException("exportLocation ('"+configuration.getExportLocation().getPath()+"') is not a directory");
		}
		final String exportDirectoryPath = configuration.getExportLocation().getPath() + "/";
		final File exportDirectory = configuration.getExportLocation();

		exportDirectory.mkdir();

		final File exportDisplayFile = new File(exportDirectoryPath + getDisplayFileName(configuration));
		exportDisplayFile.createNewFile();
		final FileOutputStream fosDisplay = getFos(exportDisplayFile);

		FileOutputStream fosMacros = null;
		if (configuration.shouldExportMacrosToFile()) {
			final File macrosExportFile = new File(exportDirectoryPath + getMacrosFileName(configuration));
			macrosExportFile.createNewFile();
			fosMacros = getFos(macrosExportFile);
		}

		export(fosDisplay, fosMacros);

		fosDisplay.close();
		if (fosMacros != null) {
			fosMacros.close();
		}
	}

	public void export(@NotNull OutputStream displayOutputStream, @Nullable OutputStream macrosOutputStream) throws IOException {
		if (macrosOutputStream == null || !configuration.shouldExportMacrosToFile()) {
			exportMacros(displayOutputStream); //save the macros inside the display header file
		} else {
			exportMacros(macrosOutputStream);
			if (configuration.shouldPlaceAdcNotice()) {
				writelnComment(displayOutputStream, bundle.getString("Misc.adc_export_notice"));
				writeln(displayOutputStream, "");
			}
		}

		exportDisplay(displayOutputStream);

		displayOutputStream.flush();
		if (macrosOutputStream != null && macrosOutputStream != displayOutputStream) {
			macrosOutputStream.flush();
		}
	}

	public static void exportControlClass(@NotNull ProjectExportConfiguration configuration, @NotNull ControlClass controlClass, @NotNull OutputStream stream) throws IOException {
		ProjectExporter exporter = new ProjectExporter(configuration);
		stream.write(exporter.getExportClassString(controlClass, 0, null).getBytes());
		stream.flush();
	}


	private void exportMacros(@NotNull OutputStream os) throws IOException {
		if (configuration.shouldPlaceAdcNotice()) {
			writelnComment(os, bundle.getString("Misc.adc_export_notice"));
			writeln(os, "");
		}

		for (Macro macro : project.getMacroRegistry().getMacros()) {
			if (macro.getComment() != null && macro.getComment().length() != 0) {
				writelnComment(os, macro.getComment());
			}
			writeln(os, "#define " + macro.getKey() + " " + getExportValueString(macro.getValue(), macro.getPropertyType()));
		}

		writeln(os, "");
	}

	private void exportDisplay(@NotNull OutputStream os) throws IOException {
		if (configuration.shouldExportMacrosToFile()) {
			writeln(os, "#include \"" + getMacrosFileName(configuration) + "\"");
			writeln(os, "");
		}
		if (project.getProjectDescription() != null && project.getProjectDescription().length() > 0) {
			writelnComment(os, project.getProjectDescription());
		}
		ArmaDisplay display = project.getEditingDisplay();

		String displayBody = getExportControlPropertyString(1, display.getDisplayProperties()) + "\n\n";
		String body = "";
		for (ArmaControl control : display.getBackgroundControls()) {
			body += getExportControlString(control, 2);
		}
		displayBody += getExportClassString(CONTROLS_BACKGROUND, null, 1, body) + "\n";

		body = "";
		for (ArmaControl control : display.getControls()) {
			body += getExportControlString(control, 2);
		}
		displayBody += getExportClassString(CONTROLS, null, 1, body);

		writeln(os, getExportClassString(configuration.getExportClassName(), null, 0, displayBody));

	}

	private String getExportControlString(@NotNull ArmaControl control, int tab) {
		if (control instanceof ArmaControlGroup) {
			String controlsBody = "";
			for (ArmaControl subControl : ((ArmaControlGroup) control).getControls()) {
				controlsBody += getExportControlString(subControl, tab + 2);
			}
			return getExportClassString(control, tab, getExportClassString(CONTROLS, null, tab + 1, controlsBody));
		}
		return getExportClassString(control, tab, null);
	}

	private String getExportClassString(@NotNull ControlClass controlClass, int tab, @Nullable String additionalBodyContent) {
		String body = getExportControlPropertyString(tab + 1, controlClass.getDefinedProperties());

		String tabS = tab(tab);
		for (ControlClass subclass : controlClass.getAllNestedClasses()) {
			body += tabS + getExportClassString(subclass, tab + 1, null);
		}
		if (additionalBodyContent != null) {
			body += "\n";
			if (!additionalBodyContent.startsWith(tabS)) {
				body += tabS;
			}
			body += additionalBodyContent;
		}
		return getExportClassString(controlClass.getClassName(), controlClass.getExtendClass() != null ? controlClass.getExtendClass().getClassName() : null, tab, body);
	}

	private String getExportClassString(@NotNull String className, @Nullable String extendClass, int parentTab, String body) {
		final String classFormatString = "%3$sclass %s%s\n%3$s{\n%4$s\n%3$s};";
		return String.format(classFormatString, className, extendClass != null ? (" : " + extendClass) : "", tab(parentTab), body);
	}

	private static String getExportControlPropertyString(int tabNum, @NotNull Iterable<? extends ControlProperty> controlProperties) {
		String body = "";
		final String tab = tab(tabNum);
		final String itemFormatString = tab + "%s = %s;";
		final String itemArrayFormatString = tab + "%s[] = %s;";
		for (ControlProperty property : controlProperties) {
			if (property.getValue() == null/* && editor.isOptional()*/) { //can allow for partial implementation, so we don't need to check if it is optional
				continue;
			}
			if (property.isInherited()) {
				continue;
			}
			if (property.getMacro() != null) {
				StringTableKey stringKey = null;
				if (property.getMacro() instanceof StringTableKey) {
					stringKey = (StringTableKey) property.getMacro();
				}
				body += String.format(itemFormatString, property.getName(), stringKey != null ? stringKey.getHeaderMacroId() : property.getMacro().getKey());
			} else {
				if (property.getValue().getAsStringArray().length == 1) {
					body += String.format(itemFormatString, property.getName(), getExportValueString(property.getValue(), property.getPropertyType()));
				} else {
					body += String.format(itemArrayFormatString, property.getName(), getExportValueString(property.getValue(), property.getPropertyType()));
				}
			}
			body += "\n";
		}
		return body.substring(0, body.length() - 1); //exclude the last newline character
	}

	private static String tab(int num) {
		String s = "";
		for (int t = 0; t < num; t++) {
			s += "\t";
		}
		return s;
	}

	@NotNull
	public static String getMacrosFileName(@NotNull ProjectExportConfiguration configuration) {
		return configuration.getExportClassName() + "_Macros" + configuration.getHeaderFileType().getExtension();
	}

	@NotNull
	public static String getDisplayFileName(@NotNull ProjectExportConfiguration configuration) {
		return configuration.getExportClassName() + configuration.getHeaderFileType().getExtension();
	}

	public static String getExportValueString(@NotNull SerializableValue value, @NotNull PropertyType type) {
		String[] arr = value.getAsStringArray();
		String ret = "";
		String v;
		for (int i = 0; i < arr.length; i++) {
			v = arr[i];
			for (int quoteIndex : type.getIndexesWithQuotes()) {
				if (quoteIndex == i) {
					v = "\"" + v + "\"";
					break;
				}
			}
			ret += v + (i != arr.length - 1 ? "," : "");
		}
		return arr.length > 1 ? "{" + ret + "}" : ret;
	}

	private static void writelnComment(OutputStream os, String s) throws IOException {
		if (s.contains("\n")) {
			writeln(os, "/*");
			writeln(os, s);
			writeln(os, "*/");
		} else {
			writeln(os, "//" + s);
		}
	}

	private static void writeln(OutputStream fos, String s) throws IOException {
		fos.write((s + "\n").getBytes());
	}

	private static FileOutputStream getFos(@NotNull File file) {
		try {
			return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
