package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.header.*;
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

	private static final String CONTROLS = "Controls";
	private static final String BG_CONTROLS = "ControlsBackground";

	private static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.HeaderConversionBundle");


	@NotNull
	public static List<String> convertAndSaveToWorkspace(@NotNull Workspace workspace, @NotNull File descriptionExt, @NotNull SelectClassesCallback callback)
			throws FileNotFoundException, HeaderConversionException {

		List<String> results = new ArrayList<>();
		HeaderFile headerFile;
		try {
			headerFile = HeaderParser.parse(descriptionExt);
		} catch (HeaderParseException e) {
			throw new HeaderConversionException(e.getMessage());
		}

		List<String> discoveredDialogClassNames = new ArrayList<>();
		for (HeaderClass hc : headerFile.getClasses()) {
			if (hc.getAssignments().getAssignmentByVarName("idd", true) != null) {
				//todo check inherited attributes
				discoveredDialogClassNames.add(hc.getClassName());
			}
		}

		List<String> convertClasses = callback.selectClassesToSave(discoveredDialogClassNames);
		for (String className : convertClasses) {
			for (HeaderClass hc : headerFile.getClasses()) {
				if (!className.equals(hc.getClassName())) {
					continue;
				}

				//begin conversion
				results.add(saveToWorkspace(headerFile, workspace, hc));
			}
		}


		return results;
	}

	public static void noClassError(String className) throws HeaderConversionException {
		throw new HeaderConversionException(String.format(bundle.getString("Error.Error.no_class_f"), className));
	}

	@NotNull
	private static String saveToWorkspace(@NotNull HeaderFile headerFile, @NotNull Workspace workspace, @NotNull HeaderClass displayClass) {
		//This is string to return. If null, there was success in converting.
		// If not null, ret will be wrapped in another String saying there was a failure with the reason equal to old value of ret
		String ret = null;

		//list of controls for dialog
		List<HeaderClass> controls = new ArrayList<>();
		//list of bg controls for dialog
		List<HeaderClass> bgControls = new ArrayList<>();


		//create project instance
		File dialogDir = workspace.getFileForName(displayClass.getClassName());
		if (dialogDir.exists()) {
			dialogDir = workspace.getFileForName(dialogDir.getName() + System.currentTimeMillis());
			while (dialogDir.exists()) {
				dialogDir = workspace.getFileForName(dialogDir.getName() + "_" + System.currentTimeMillis());
			}
			dialogDir.mkdirs();
		}
		Project project = new Project(new ProjectInfo(displayClass.getClassName(), dialogDir));


		//first up, load controls and bgControls

		HeaderClass controlsClass = displayClass.getNestedClasses().getClassByName(CONTROLS, false);
		if (controlsClass != null) {

		} else {
			HeaderAssignment controlsAssignment = displayClass.getAssignments().getAssignmentByVarName(CONTROLS, false);
			if (controlsAssignment instanceof HeaderArrayAssignment) {

			} else {
				ret = bundle.getString("Convert.FailReason.controls_assignment_not_array");
			}
		}

		HeaderClass bgControlsClass = displayClass.getNestedClasses().getClassByName(BG_CONTROLS, false);
		if (bgControlsClass != null) {

		} else {
			HeaderAssignment bgControlsAssignment = displayClass.getAssignments().getAssignmentByVarName(BG_CONTROLS, false);
			if (bgControlsAssignment instanceof HeaderArrayAssignment) {
				HeaderArray bgcArray = ((HeaderArrayAssignment) bgControlsAssignment).getArray();
				bgcArray.getItems();
			} else {
				ret = bundle.getString("Convert.FailReason.bg_controls_assignment_not_array");
			}
		}

		//create display and attach properties
		ArmaDisplay armaDisplay = new ArmaDisplay();
		project.setEditingDisplay(armaDisplay);




		//we are done converting after this

		if (ret == null) {
			ret = String.format(bundle.getString("Convert.success_f"), displayClass.getClassName());
		} else {
			ret = String.format(bundle.getString("Convert.fail_f"), ret);
		}

		return ret;
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
