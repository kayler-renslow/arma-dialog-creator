package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.arma.header.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValueConstructionException;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeNode;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.data.xml.ProjectSaveXmlWriter;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	/**
	 Assignment variable name for getting a dialog's idd
	 */
	private static final String IDD = "idd";
	/**
	 Assignment variable name for getting a control's type from {@link HeaderClass}
	 */
	private static final String CONTROL_TYPE = "type";

	public static void convertAndSaveToWorkspace(@NotNull Workspace w, @NotNull File descExt, @NotNull SelectClassesCallback c, @NotNull DataContext dataContext)
			throws FileNotFoundException, HeaderConversionException {
		new HeaderToProject(w, descExt, c, dataContext).run();
	}

	//
	// End static stuff
	//

	private final ResourceBundle bundle = Lang.getBundle("com.kaylerrenslow.armaDialogCreator.HeaderConversionBundle");
	private final Workspace workspace;
	private final File descExt;
	private final SelectClassesCallback callback;
	private final DataContext dataContext;
	private final ArmaResolution resolution;
	private final Env env;
	private HeaderFile headerFile;

	public HeaderToProject(@NotNull Workspace workspace, @NotNull File descExt, @NotNull SelectClassesCallback callback, @NotNull DataContext dataContext) {
		this.workspace = workspace;
		this.descExt = descExt;
		this.callback = callback;
		this.dataContext = dataContext;

		resolution = DataKeys.ARMA_RESOLUTION.get(dataContext);
		env = DataKeys.ENV.get(dataContext);

		if (resolution == null || env == null) {
			throw new IllegalArgumentException("dataContext doesn't have DataKeys.ARMA_RESOLUTION and/or DataKeys.ENV");
		}
	}

	private void run() throws FileNotFoundException, HeaderConversionException {
		try {
			headerFile = HeaderParser.parse(descExt);
		} catch (HeaderParseException e) {
			throw new HeaderConversionException(e.getMessage());
		}

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

				//begin conversion of dialog and save to workspace
				saveToWorkspace(hc);
			}
		}

	}


	private void saveToWorkspace(@NotNull HeaderClass displayClass) throws HeaderConversionException {

		//list of controls for dialog
		List<ArmaControl> controls = new ArrayList<>();
		//list of bg controls for dialog
		List<ArmaControl> bgControls = new ArrayList<>();
		//Project instance
		Project project;

		//create project instance
		{
			File dialogDir = workspace.getFileForName(displayClass.getClassName());
			if (dialogDir.exists()) {
				dialogDir = workspace.getFileForName(dialogDir.getName() + System.currentTimeMillis());
				while (dialogDir.exists()) {
					dialogDir = workspace.getFileForName(dialogDir.getName() + "_" + System.currentTimeMillis());
				}
				dialogDir.mkdirs();
			}
			project = new Project(new ProjectInfo(displayClass.getClassName(), dialogDir));
		}

		//todo add macros to macro registry

		//load controls from nested classes or array of classes
		{
			//class Controls or controls[]={MyControl, ...}
			HeaderClass controlsClass = displayClass.getNestedClasses().getByName(CONTROLS, false);
			if (controlsClass != null) {
				for (HeaderClass hc : controlsClass.getNestedClasses()) {
					bgControls.add(getArmaControl(project, hc));
				}
			} else {
				HeaderAssignment controlsAssignment = displayClass.getAssignments().getByVarName(CONTROLS, false);
				addArrayControls(project, controlsAssignment, bgControls, CONTROLS);
			}

			//class ControlsBackground or controlsBackground[]={MyControl, ...}
			HeaderClass bgControlsClass = displayClass.getNestedClasses().getByName(BG_CONTROLS, false);
			if (bgControlsClass != null) {
				for (HeaderClass hc : bgControlsClass.getNestedClasses()) {
					bgControls.add(getArmaControl(project, hc));
				}
			} else {
				HeaderAssignment bgControlsAssignment = displayClass.getAssignments().getByVarName(BG_CONTROLS, false);
				addArrayControls(project, bgControlsAssignment, bgControls, BG_CONTROLS);
			}
		}

		//create display and attach properties
		{
			ArmaDisplay armaDisplay = new ArmaDisplay();
			project.setEditingDisplay(armaDisplay);
		}


		//build the structure for the controls and bg controls
		TreeStructure<ArmaControl> structureMain = new TreeStructure.Simple<>(TreeNode.Simple.newRoot());
		TreeStructure<ArmaControl> structureBg = new TreeStructure.Simple<>(TreeNode.Simple.newRoot());
		buildStructure(controls, structureMain);
		buildStructure(bgControls, structureBg);

		//write to file
		ProjectSaveXmlWriter writer = new ProjectSaveXmlWriter(project, structureMain, structureBg);
		try {
			writer.write();
		} catch (IOException e) {
			convertError(String.format(bundle.getString("Convert.FailReason.write_file_fail_f"), displayClass.getClassName()));
		}

		//done converting and conversion of dialog/display is written to file by here.
	}

	private void addArrayControls(@NotNull Project project, @Nullable HeaderAssignment arrayOfControlsAssignment, @NotNull List<ArmaControl> controls, @NotNull String sourceName)
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
				convertError(String.format(bundle.getString("Convert.FailReason.bg_controls_assignment_not_array_f"), arrayOfControlsAssignment.getVariableName()));
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
				convertError(String.format(bundle.getString("Convert.FailReason.control_class_in_array_dne_f"), className, sourceName));
			}
			controls.add(getArmaControl(project, matchedClassRef.getValue()));
		}
	}

	@NotNull
	private ArmaControl getArmaControl(@NotNull Project project, @NotNull HeaderClass headerClass) throws HeaderConversionException {
		final String controlClassName = headerClass.getClassName();

		//get control type
		HeaderAssignment controlTypeAssign = headerFile.getAssignmentByVarName(headerClass, CONTROL_TYPE, false);
		if (controlTypeAssign == null) {
			convertError(String.format(bundle.getString("Convert.FailReason.control_type_missing_f"), controlClassName));
		}
		ControlType controlType = null;
		try {
			controlType = ControlType.findById(Integer.parseInt(controlTypeAssign.getValue().getContent()));
		} catch (IllegalArgumentException e) {
			convertError(String.format(bundle.getString("Convert.FailReason.control_type_nan_f"), controlClassName));
		}

		//create the control
		ArmaControl armaControl = ArmaControl.createControl(controlClassName, ArmaControlLookup.findByControlType(controlType), resolution, env, project);

		for (ControlProperty property : armaControl.getAllChildProperties()) {
			HeaderAssignment assignment = headerClass.getAssignments().getByVarName(property.getName(), false);
			if (assignment == null) {
				continue;
			}
			//todo convert assignment value into a value for property
			if (assignment.getValue() instanceof HeaderArray) {
				//todo build array
			} else {
				String assignmentValue = assignment.getValue().getContent();
				if (assignmentValue.charAt(0) == '"') {
					assignmentValue = assignmentValue.substring(1, assignmentValue.length() - 1); //chop off quotes
					property.setValue(SerializableValue.constructNew(dataContext, PropertyType.STRING, assignmentValue));
				} else {
					try {
						property.setValue(SerializableValue.constructNew(dataContext, property.getInitialPropertyType(), assignmentValue));
					} catch (SerializableValueConstructionException e) {
						property.setCustomDataValue(assignmentValue);
						property.setUsingCustomData(true);
					}
				}
			}
		}

		return armaControl;
	}

	private void buildStructure(@NotNull List<ArmaControl> controls, @NotNull TreeStructure<ArmaControl> structure) {
		for (ArmaControl control : controls) {
			TreeNode<ArmaControl> treeNode = new TreeNode.Simple<>(control, control.getClassName(), false);
			buildStructure(control, treeNode);
			structure.getRoot().getChildren().add(treeNode);
		}
	}

	private void buildStructure(@NotNull ArmaControl control, @NotNull TreeNode<ArmaControl> parentTreeNode) {
		TreeNode<ArmaControl> treeNode = new TreeNode.Simple<>(control, control.getClassName(), false);
		parentTreeNode.getChildren().add(treeNode);
		if (control instanceof ArmaControlGroup) {
			ArmaControlGroup group = (ArmaControlGroup) control;
			for (ArmaControl controlChild : group.getControls()) {
				buildStructure(controlChild, treeNode);
			}
		}
	}

	private void noClassError(String className) throws HeaderConversionException {
		throw new HeaderConversionException(String.format(bundle.getString("Error.Error.no_class_f"), className));
	}

	private void convertError(@NotNull String s) throws HeaderConversionException {
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
