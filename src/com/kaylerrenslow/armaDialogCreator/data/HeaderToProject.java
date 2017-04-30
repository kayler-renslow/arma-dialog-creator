package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderClass;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderFile;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParseException;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 Used for converting an Arma 3 config file (header file) into a project that is usable by Arma Dialog Creator.

 @author Kayler
 @since 04/30/2017 */
public class HeaderToProject {

	private static final String CFG_DIALOGS = "CfgDialogs";

	@NotNull
	public static List<String> convertAndSaveToWorkspace(@NotNull Workspace workspace, @NotNull File descriptionExt, @NotNull SelectClassesCallback callback)
			throws FileNotFoundException, HeaderParseException, HeaderConversionException {

		ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.HeaderConversionBundle");

		List<String> dialogsConverted = new ArrayList<>();
		HeaderFile headerFile = HeaderParser.parse(descriptionExt);

		HeaderClass cfgDialog = headerFile.getClasses().findClassByPath(CFG_DIALOGS);
		if (cfgDialog == null) {
			throw new HeaderConversionException(String.format(bundle.getString("Error.Error.no_class_f"), CFG_DIALOGS));
		}

		List<String> discoveredClasses = new ArrayList<>();
		for (HeaderClass hc : cfgDialog.getNestedClasses()) {
			discoveredClasses.add(hc.getClassName());
		}

		List<String> convertClasses = callback.selectClassesToSave(discoveredClasses);
		for (String className : convertClasses) {
			for (HeaderClass hc : cfgDialog.getNestedClasses()) {
				if (!className.equals(hc.getClassName())) {
					continue;
				}

				//begin conversion

			}
		}


		return dialogsConverted;
	}

	/**
	 Used in conjunction with {@link HeaderToProject}

	 @author Kayler
	 @since 04/30/2017
	 */
	public interface SelectClassesCallback {
		@NotNull List<String> selectClassesToSave(@NotNull List<String> classesDiscovered);
	}
}
