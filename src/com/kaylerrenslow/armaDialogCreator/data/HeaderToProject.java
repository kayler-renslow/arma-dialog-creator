package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.arma.header.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValueConstructionException;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeNode;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.data.xml.ProjectSaveXmlWriter;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

	public static void convertAndSaveToWorkspace(@NotNull File workspaceDir, @NotNull File descExt, @NotNull HeaderToProject.ConversionCallback c)
			throws FileNotFoundException, HeaderConversionException {
		new HeaderToProject(new Workspace(workspaceDir), descExt, c).run();
	}

	//
	// End static stuff
	//

	private final ResourceBundle bundle = Lang.getBundle("HeaderConversionBundle");
	private final Workspace workspace;
	private final File descExt;
	private final ConversionCallback callback;
	private final ApplicationData dataContext;
	private final ArmaResolution resolution;
	private final Env env;
	private HeaderFile headerFile;

	protected HeaderToProject(@NotNull Workspace workspace, @NotNull File descExt, @NotNull HeaderToProject.ConversionCallback callback) {
		this.workspace = workspace;
		this.descExt = descExt;
		this.callback = callback;
		this.dataContext = new ApplicationData();

		resolution = DataKeys.ARMA_RESOLUTION.get(dataContext);
		env = DataKeys.ENV.get(dataContext);

		if (resolution == null || env == null) {
			throw new IllegalArgumentException("dataContext doesn't have DataKeys.ARMA_RESOLUTION and/or DataKeys.ENV");
		}
	}

	private void run() throws FileNotFoundException, HeaderConversionException {
		callback.progressUpdate(0, -1);
		callback.message(bundle.getString("Status.parsing"));
		try {
			headerFile = HeaderParser.parse(descExt);
			callback.finishedParse();
		} catch (HeaderParseException e) {
			throw new HeaderConversionException(e.getMessage());
		}


		callback.message(bundle.getString("Status.locating_dialogs"));

		List<String> discoveredDialogClassNames = new ArrayList<>();
		for (HeaderClass hc : headerFile.getClasses()) {
			boolean hasIddAssign = hc.getAssignments().getByVarName(IDD, true) != null;
			if (hasIddAssign || headerFile.getAssignmentByVarName(hc, IDD, true) != null) {
				discoveredDialogClassNames.add(hc.getClassName());
			}
		}

		callback.message(bundle.getString("Status.requesting_dialogs_to_save"));


		List<String> convertClasses = callback.selectClassesToSave(discoveredDialogClassNames);

		int progress = 0;
		int maxProgress = convertClasses.size();
		callback.progressUpdate(progress, maxProgress);
		for (String className : convertClasses) {
			for (HeaderClass hc : headerFile.getClasses()) {
				if (!className.equals(hc.getClassName())) {
					continue;
				}

				callback.progressUpdate(++progress, maxProgress);

				callback.message(String.format(bundle.getString("Status.converting_dialog_f"), className));

				//begin conversion of dialog and save to workspace
				try {
					saveToWorkspace(hc);
				} catch (HeaderConversionException e) {
					callback.conversionFailed(className, e);
				}
			}
		}

	}


	private void saveToWorkspace(@NotNull HeaderClass displayClass) throws HeaderConversionException {
		int progress = 0;
		int maxProgress = 1/*create project*/
				+ 1/*create dialog object*/
				+ 1/*create controls*/
				+ 1/*write to file*/;

		//Project instance
		Project project;
		//display instance
		ArmaDisplay armaDisplay;

		callback.progressUpdate(progress, maxProgress);

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

		dataContext.setCurrentProject(project);

		callback.progressUpdate(++progress, maxProgress);

		//todo add macros to macro registry?
		//todo add the stringtable

		//create display and attach properties
		{
			armaDisplay = new ArmaDisplay();
			project.setEditingDisplay(armaDisplay);
		}

		callback.progressUpdate(++progress, maxProgress);

		//load controls from nested classes or array of classes
		{
			//class Controls or controls[]={MyControl, ...}
			HeaderClass controlsClass = displayClass.getNestedClasses().getByName(CONTROLS, false);
			if (controlsClass != null) {
				for (HeaderClass hc : controlsClass.getNestedClasses()) {
					armaDisplay.getControls().add(getArmaControl(project, hc));
				}
			} else {
				HeaderAssignment controlsAssignment = displayClass.getAssignments().getByVarName(CONTROLS, false);
				addArrayControls(project, controlsAssignment, armaDisplay.getControls(), CONTROLS);
			}

			//class ControlsBackground or controlsBackground[]={MyControl, ...}
			HeaderClass bgControlsClass = displayClass.getNestedClasses().getByName(BG_CONTROLS, false);
			if (bgControlsClass != null) {
				for (HeaderClass hc : bgControlsClass.getNestedClasses()) {
					armaDisplay.getBackgroundControls().add(getArmaControl(project, hc));
				}
			} else {
				HeaderAssignment bgControlsAssignment = displayClass.getAssignments().getByVarName(BG_CONTROLS, false);
				addArrayControls(project, bgControlsAssignment, armaDisplay.getBackgroundControls(), BG_CONTROLS);
			}
		}

		callback.progressUpdate(++progress, maxProgress);

		//build the structure for the controls and bg controls
		TreeStructure<ArmaControl> structureMain = new TreeStructure.Simple<>(TreeNode.Simple.newRoot());
		TreeStructure<ArmaControl> structureBg = new TreeStructure.Simple<>(TreeNode.Simple.newRoot());
		buildStructure(armaDisplay.getControls(), structureMain);
		buildStructure(armaDisplay.getBackgroundControls(), structureBg);

		//write to file
		ProjectSaveXmlWriter writer = new ProjectSaveXmlWriter(project, structureMain, structureBg);
		try {
			callback.message(String.format(bundle.getString("Status.saving_dialog_f"), displayClass.getClassName()));
			writer.write();
		} catch (IOException e) {
			convertError(String.format(bundle.getString("Convert.FailReason.write_file_fail_f"), displayClass.getClassName()));
		}

		callback.progressUpdate(++progress, maxProgress);

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

			SerializableValue v = createValueFromAssignment(assignment, property.getInitialPropertyType());
			if (v != null) {
				property.setValue(v);
			} else {
				property.setCustomDataValue(assignment.getValue().getContent());
				property.setUsingCustomData(true);
			}
		}


		//get the extend class
		ControlClass extendClass = null;
		if (headerClass.getExtendClassName() != null) {
			extendClass = project.getCustomControlClassRegistry().findControlClassByName(headerClass.getExtendClassName());
			if (extendClass == null) {
				HeaderClass extendHeaderClass = headerFile.getExtendClass(headerClass, false);
				if (extendHeaderClass == null) {
					convertError(String.format(bundle.getString("Error.no_class_f"), headerClass.getExtendClassName()));
				}
				extendClass = createAndAppendCustomControlClass(project, extendHeaderClass);
			}
		}


		// After setting the properties, extend the class. As mentioned in the ControlProperty.inherit() documentation,
		// the previous values of the control property will be saved
		armaControl.extendControlClass(extendClass);

		return armaControl;
	}

	@NotNull
	private ControlClass createAndAppendCustomControlClass(@NotNull Project project, @NotNull HeaderClass headerClass) {
		List<ControlPropertySpecification> optional = new ArrayList<>(headerClass.getAssignments().size());
		for (HeaderAssignment assignment : headerClass.getAssignments()) {
			//todo handle custom properties because the user may use the dialog class for more than just dialogs!
			for (ControlPropertyLookup lookup : ControlPropertyLookup.values()) {
				if (assignment.getVariableName().equalsIgnoreCase(lookup.getPropertyName())) {
					optional.add(new ControlPropertySpecification(
							lookup,
							createValueFromAssignment(assignment, lookup.getPropertyType()),
							null
					));
				}
			}
		}
		ControlClassSpecification ccs = new ControlClassSpecification(
				headerClass.getClassName(),
				Collections.emptyList(),
				optional
		);

		project.getCustomControlClassRegistry().addControlClass(ccs);

		return new ControlClass(ccs, project);
	}

	@Nullable
	private SerializableValue createValueFromAssignment(@NotNull HeaderAssignment assignment, @NotNull PropertyType initialPropertyType) {
		if (assignment.getValue() instanceof HeaderArray) {
			if (SerializableValue.isConvertible(initialPropertyType, PropertyType.ARRAY)) {
				HeaderArray headerArray = (HeaderArray) assignment.getValue();
				String[] items = new String[headerArray.getItems().size()];
				int i = 0;
				for (HeaderArrayItem item : headerArray.getItems()) {
					items[i++] = item.getValue().getContent();
				}
				return SerializableValue.constructNew(dataContext, PropertyType.ARRAY, items);
			}

		} else {
			String assignmentValue = assignment.getValue().getContent();
			if (assignmentValue.charAt(0) == '"') {
				if (SerializableValue.isConvertible(initialPropertyType, PropertyType.STRING)) {
					assignmentValue = assignmentValue.substring(1, assignmentValue.length() - 1); //chop off quotes
					return SerializableValue.constructNew(dataContext, PropertyType.STRING, assignmentValue);
				}
			} else {
				try {
					return SerializableValue.constructNew(dataContext, initialPropertyType, assignmentValue);
				} catch (SerializableValueConstructionException ignore) {

				}
			}
		}
		return null;
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

	private void convertError(@NotNull String s) throws HeaderConversionException {
		throw new HeaderConversionException(String.format(bundle.getString("Convert.fail_f"), s));
	}

	/**
	 Used in conjunction with {@link HeaderToProject}

	 @author Kayler
	 @since 04/30/2017
	 */
	public interface ConversionCallback {
		@NotNull List<String> selectClassesToSave(@NotNull List<String> classesDiscovered);

		void message(@NotNull String msg);

		void progressUpdate(int stepsCompleted, int totalSteps);

		void finishedParse();

		void conversionFailed(@NotNull String dialogClassName, @NotNull HeaderConversionException e);
	}
}
