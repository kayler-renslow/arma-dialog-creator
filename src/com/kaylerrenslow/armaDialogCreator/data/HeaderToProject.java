package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.header.*;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeNode;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.data.xml.ProjectSaveXmlWriter;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 Used for converting an Arma 3 config file (header file) into a project that is usable by Arma Dialog Creator.

 @author Kayler
 @since 04/30/2017 */
public class HeaderToProject {

	private static final String CONTROLS = "Controls";
	private static final String BG_CONTROLS = "ControlsBackground";
	private static final String IDD = "idd";

	static final ResourceBundle bundle = Lang.getBundle("com.kaylerrenslow.armaDialogCreator.HeaderConversionBundle");

	public static void convertAndSaveToWorkspace(@NotNull Workspace workspace, @NotNull File descriptionExt, @NotNull SelectClassesCallback callback)
			throws FileNotFoundException, HeaderConversionException {

		HeaderFile headerFile;
		try {
			headerFile = HeaderParser.parse(descriptionExt);
		} catch (HeaderParseException e) {
			throw new HeaderConversionException(e.getMessage());
		}

		HashMap<HeaderClass, HeaderClass> inheritanceHelper = new HashMap<>();

		List<String> discoveredDialogClassNames = new ArrayList<>();
		for (HeaderClass hc : headerFile.getClasses()) {
			boolean hasIddAssign = hc.getAssignments().getByVarName(IDD, true) != null;
			if (hasIddAssign || headerFile.getAssignmentByVarName(hc, IDD, true) != null) {
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
				saveToWorkspace(headerFile, inheritanceHelper, workspace, hc);
			}
		}

	}


	private static void saveToWorkspace(@NotNull HeaderFile headerFile, @NotNull HashMap<HeaderClass, HeaderClass> inheritanceHelper, @NotNull Workspace workspace,
										@NotNull HeaderClass displayClass) throws HeaderConversionException {

		//list of controls for dialog
		List<ArmaControl> controls = new ArrayList<>();
		//list of bg controls for dialog
		List<ArmaControl> bgControls = new ArrayList<>();


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

		//todo add macros to macro registry

		//first up, load controls and bgControls

		//load controls from nested classes or array of classes
		HeaderClass controlsClass = displayClass.getNestedClasses().getByName(CONTROLS, false);
		if (controlsClass != null) {
			for (HeaderClass hc : controlsClass.getNestedClasses()) {
				bgControls.add(getControl(hc));
			}
		} else {
			HeaderAssignment controlsAssignment = displayClass.getAssignments().getByVarName(CONTROLS, false);
			addArrayControls(headerFile, controlsAssignment, bgControls, CONTROLS);
		}

		//load background controls from nested classes or array of classes
		HeaderClass bgControlsClass = displayClass.getNestedClasses().getByName(BG_CONTROLS, false);
		if (bgControlsClass != null) {
			for (HeaderClass hc : bgControlsClass.getNestedClasses()) {
				bgControls.add(getControl(hc));
			}
		} else {
			HeaderAssignment bgControlsAssignment = displayClass.getAssignments().getByVarName(BG_CONTROLS, false);
			addArrayControls(headerFile, bgControlsAssignment, bgControls, BG_CONTROLS);
		}

		//create display and attach properties
		ArmaDisplay armaDisplay = new ArmaDisplay();
		project.setEditingDisplay(armaDisplay);


		//build the structure for the controls and bg controls
		TreeStructure<ArmaControl> structureMain = new TreeStructure.Simple<>(TreeNode.Simple.newRoot());
		TreeStructure<ArmaControl> structureBg = new TreeStructure.Simple<>(TreeNode.Simple.newRoot());
		buildStructure(controls, structureMain);
		buildStructure(bgControls, structureBg);

		//we are done converting now

		//write to file
		ProjectSaveXmlWriter writer = new ProjectSaveXmlWriter(project, structureMain, structureBg);
		try {
			writer.write();
		} catch (IOException e) {
			convertError(bundle.getString("Convert.FailReason.write_file_fail"));
		}

		//done converting and conversion is written to file by here.
	}

	private static void addArrayControls(@NotNull HeaderFile headerFile, @Nullable HeaderAssignment arrayOfControlsAssignment, @NotNull List<ArmaControl> controls, @NotNull String sourceName)
			throws HeaderConversionException {

		HeaderArray arrayOfControls = null;
		if (arrayOfControlsAssignment instanceof HeaderArrayAssignment) {
			arrayOfControls = ((HeaderArrayAssignment) arrayOfControlsAssignment).getArray();
		} else {
			convertError(bundle.getString("Convert.FailReason.bg_controls_assignment_not_array"));
		}

		List<HeaderArrayItem> items = arrayOfControls.getItems();
		for (HeaderArrayItem arrayItem : items) {
			HeaderValue v = arrayItem.getValue();
			if (v instanceof HeaderArray) {
				convertError(bundle.getString("Convert.FailReason.bg_controls_assignment_not_array"));
				break;
			}
			Reference<HeaderClass> matchedClassRef = new Reference<>();
			String className = v.getContent();
			headerFile.traverseDownwards((headerClass -> {
				if (headerClass.classNameEquals(className, true)) {
					matchedClassRef.setValue(headerClass);
					return false;
				}
				return true;
			}));
			if (matchedClassRef.getValue() == null) {
				convertError(String.format("Convert.FailReason.control_class_in_array_dne_f", className, sourceName));
			}
			controls.add(getControl(matchedClassRef.getValue()));
		}
	}

	private static void buildStructure(@NotNull List<ArmaControl> controls, @NotNull TreeStructure<ArmaControl> structure) {
		for (ArmaControl control : controls) {
			TreeNode<ArmaControl> treeNode = new TreeNode.Simple<>(control, control.getClassName(), false);
			buildStructure(control, treeNode);
			structure.getRoot().getChildren().add(treeNode);
		}
	}

	private static void buildStructure(@NotNull ArmaControl control, @NotNull TreeNode<ArmaControl> parentTreeNode) {
		TreeNode<ArmaControl> treeNode = new TreeNode.Simple<>(control, control.getClassName(), false);
		parentTreeNode.getChildren().add(treeNode);
		if (control instanceof ArmaControlGroup) {
			ArmaControlGroup group = (ArmaControlGroup) control;
			for (ArmaControl controlChild : group.getControls()) {
				buildStructure(controlChild, treeNode);
			}
		}
	}

	@NotNull
	private static ArmaControl getControl(@NotNull HeaderClass headerClass) {
		return null;
	}

	private static void noClassError(String className) throws HeaderConversionException {
		throw new HeaderConversionException(String.format(bundle.getString("Error.Error.no_class_f"), className));
	}

	private static String convertError(String s) throws HeaderConversionException {
		throw new HeaderConversionException(String.format(bundle.getString("Convert.fail_f"), s));
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
