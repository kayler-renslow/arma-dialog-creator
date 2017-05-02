package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.header.*;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeNode;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.data.xml.ProjectSaveXmlWriter;
import com.kaylerrenslow.armaDialogCreator.util.Reference;
import org.jetbrains.annotations.NotNull;

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

		HashMap<HeaderClass, HeaderClass> inheritanceHelper = new HashMap<>();

		List<String> discoveredDialogClassNames = new ArrayList<>();
		for (HeaderClass hc : headerFile.getClasses()) {
			boolean hasIddAssign = hc.getAssignments().getAssignmentByVarName("idd", true) != null;
			if (!hasIddAssign) {
				setupInheritanceMap(inheritanceHelper, hc);
			}
			if (hasIddAssign || inheritanceHelper.get(hc) != null) {
				discoveredDialogClassNames.add(hc.getClassName());
			}
		}

		List<String> convertClasses = callback.selectClassesToSave(discoveredDialogClassNames);
		for (String className : convertClasses) {
			for (HeaderClass hc : headerFile.getClasses()) {
				if (!className.equals(hc.getClassName())) {
					continue;
				}
				setupInheritanceMap(inheritanceHelper, hc);

				//begin conversion
				results.add(saveToWorkspace(headerFile, inheritanceHelper, workspace, hc));
			}
		}


		return results;
	}

	private static void setupInheritanceMap(@NotNull HashMap<HeaderClass, HeaderClass> inheritanceHelper, @NotNull HeaderClass hc) throws HeaderConversionException {
		if (inheritanceHelper.get(hc) != null) {
			return; //already done
		}
		if (hc.getExtendClassName() != null) {
			Reference<HeaderClass> extendHeaderClassRef = new Reference<>();

			HeaderClass last = hc.traverseUpwards((parent) -> {
				if (parent.classNameEquals(hc.getExtendClassName(), true)) {
					extendHeaderClassRef.setValue(parent);
					return false;
				}
				HeaderClass c = parent.getNestedClasses().getClassByName(hc.getExtendClassName(), true);
				if (c != null) {
					extendHeaderClassRef.setValue(c);
					return false;
				}
				return true;
			});

			if (extendHeaderClassRef.getValue() == null) {
				throw new HeaderConversionException(String.format(bundle.getString("Error.extend_class_dne_f"), hc.getClassName(), hc.getExtendClassName()));
			}

			inheritanceHelper.put(hc, last);

			//build the inheritance tree for the extended class as well
			setupInheritanceMap(inheritanceHelper, extendHeaderClassRef.getValue());
		}
	}

	public static void noClassError(String className) throws HeaderConversionException {
		throw new HeaderConversionException(String.format(bundle.getString("Error.Error.no_class_f"), className));
	}

	@NotNull
	private static String saveToWorkspace(@NotNull HeaderFile headerFile, @NotNull HashMap<HeaderClass, HeaderClass> inheritanceHelper, @NotNull Workspace workspace, @NotNull HeaderClass displayClass) {
		//This is string to return. If length is 0, there was success in converting.
		// If length is not 0, ret will be wrapped in another String saying there was a failure with the reason equal to old value of ret
		String ret = "";

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


		//first up, load controls and bgControls

		HeaderClass controlsClass = displayClass.getNestedClasses().getClassByName(CONTROLS, false);
		if (controlsClass != null) {

		} else {
			HeaderAssignment controlsAssignment = displayClass.getAssignments().getAssignmentByVarName(CONTROLS, false);
			if (controlsAssignment instanceof HeaderArrayAssignment) {

			} else {
				ret = appendError(ret, bundle.getString("Convert.FailReason.controls_assignment_not_array"));
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
				ret = appendError(ret, bundle.getString("Convert.FailReason.bg_controls_assignment_not_array"));
			}
		}

		//create display and attach properties
		ArmaDisplay armaDisplay = new ArmaDisplay();
		project.setEditingDisplay(armaDisplay);


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
			ret = appendError(ret, bundle.getString("Convert.FailReason.write_file_fail"));
		}

		//done converting and conversion is written to file by here.

		if (ret.length() == 0) {
			ret = String.format(bundle.getString("Convert.success_f"), displayClass.getClassName());
		} else {
			ret = String.format(bundle.getString("Convert.fail_f"), ret);
		}

		return ret;
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

	private static String appendError(String s, String append) {
		if (s.length() == 0) {
			s = append;
		}
		return s + "\n" + append;
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
