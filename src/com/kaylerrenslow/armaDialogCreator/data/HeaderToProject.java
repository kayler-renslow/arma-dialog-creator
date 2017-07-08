package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.arma.header.*;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVRaw;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValueConstructionException;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeNode;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.data.xml.DefaultStringTableXmlParser;
import com.kaylerrenslow.armaDialogCreator.data.xml.ProjectSaveXmlWriter;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.KeyValue;
import com.kaylerrenslow.armaDialogCreator.util.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

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

	/**
	 Runs the description.ext to {@link Project} converter and saves the converted {@link Project}s to files listed in the return value.

	 @param workspaceDir the directory of the workspace. This directory must exist, or an exception will be thrown.
	 @param descExt the description.ext file
	 @param c callback to use
	 @return a list of Files (with the key being the name of the dialog) that link to the project xml for each of the converted dialogs.
	 The size may not be equal to the number of dialogs requested to be converted, however, it will never
	 be larger than the requested amount.
	 */
	@NotNull
	public static List<KeyValue<String, File>> convertAndSaveToWorkspace(@NotNull File workspaceDir, @NotNull File descExt, @NotNull HeaderToProject.ConversionCallback c)
			throws FileNotFoundException, HeaderConversionException {
		return new HeaderToProject(new Workspace(workspaceDir), descExt, c).run();
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

	private List<KeyValue<String, File>> run() throws FileNotFoundException, HeaderConversionException {
		List<KeyValue<String, File>> ret = new ArrayList<>();
		callback.progressUpdate(0, -1);
		callback.message(bundle.getString("Status.parsing"));
		try {
			headerFile = HeaderParser.parse(descExt, workspace.getFileInAdcDirectory("temp"));
			callback.finishedParse();
		} catch (HeaderParseException e) {
			throw new HeaderConversionException(e.getMessage(), e);
		} catch (IOException e2) {
			ExceptionHandler.error(e2);
		}


		callback.message(bundle.getString("Status.locating_dialogs"));

		List<String> discoveredDialogClassNames = new ArrayList<>();
		for (HeaderClass hc : headerFile.getClasses()) {
			boolean hasIddAssign = hc.getAssignments().getByVarName(IDD, false) != null;
			if (hasIddAssign || headerFile.getAssignmentByVarName(hc, IDD, false) != null) {
				discoveredDialogClassNames.add(hc.getClassName());
			}
		}

		callback.message(bundle.getString("Status.requesting_dialogs_to_save"));

		List<String> convertClasses = callback.selectClassesToSave(discoveredDialogClassNames);

		//load StringTable, if it exists
		File[] stringtableArray = descExt.getParentFile().listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.equalsIgnoreCase("stringtable.xml");
			}
		});
		StringTable stringTable = null;
		if (stringtableArray != null && stringtableArray.length > 0) {

			callback.message(bundle.getString("Status.loading_stringtable"));

			try {
				stringTable = new DefaultStringTableXmlParser(stringtableArray[0]).createStringTableInstance();
			} catch (Exception ignore) {

			}
		}

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
					File projectXml = saveToWorkspace(hc, stringTable);
					ret.add(new KeyValue<>(hc.getClassName(), projectXml));
				} catch (HeaderConversionException e) {
					callback.conversionFailed(className, e);
				}
			}
		}

		return ret;
	}

	@NotNull
	private File saveToWorkspace(@NotNull HeaderClass displayClass, @Nullable StringTable stringTable) throws HeaderConversionException {
		final String dialogClassName = displayClass.getClassName();

		int progress = 0;
		final int maxProgress = 1/*create project*/
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
			File dialogDir = workspace.getFileForName(dialogClassName);
			if (dialogDir.exists()) {
				dialogDir = workspace.getFileForName(dialogDir.getName() + System.currentTimeMillis());
				while (dialogDir.exists()) {
					dialogDir = workspace.getFileForName(dialogDir.getName() + "_" + System.currentTimeMillis());
				}
				dialogDir.mkdir();
			} else {
				dialogDir.mkdirs();
				dialogDir.mkdir();
			}

			project = new Project(dataContext, new ProjectInfo(dialogClassName, dialogDir));
		}

		dataContext.setCurrentProject(project);

		callback.progressUpdate(++progress, maxProgress);

		//todo add macros to macro registry?

		//create display, attach properties, and look for StringTable
		{
			armaDisplay = new ArmaDisplay();
			project.setEditingDisplay(armaDisplay);
			project.setStringTable(stringTable);
		}

		callback.progressUpdate(++progress, maxProgress);

		//load controls from nested classes or array of classes
		{
			//class Controls or controls[]={MyControl, ...}
			HeaderClass controlsClass = displayClass.getNestedClasses().getByName(CONTROLS, false);
			if (controlsClass != null) {
				for (HeaderClass hc : controlsClass.getNestedClasses()) {
					armaDisplay.getControls().add(getArmaControl(dialogClassName, project, hc));
				}
			} else {
				HeaderAssignment controlsAssignment = displayClass.getAssignments().getByVarName(CONTROLS, false);
				addArrayControls(dialogClassName, project, controlsAssignment, armaDisplay.getControls(), CONTROLS);
			}

			//class ControlsBackground or controlsBackground[]={MyControl, ...}
			HeaderClass bgControlsClass = displayClass.getNestedClasses().getByName(BG_CONTROLS, false);
			if (bgControlsClass != null) {
				for (HeaderClass hc : bgControlsClass.getNestedClasses()) {
					armaDisplay.getBackgroundControls().add(getArmaControl(dialogClassName, project, hc));
				}
			} else {
				HeaderAssignment bgControlsAssignment = displayClass.getAssignments().getByVarName(BG_CONTROLS, false);
				addArrayControls(dialogClassName, project, bgControlsAssignment, armaDisplay.getBackgroundControls(), BG_CONTROLS);
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
			callback.message(String.format(bundle.getString("Status.saving_dialog_f"), dialogClassName));
			writer.write(null);
		} catch (IOException e) {
			convertError(dialogClassName, bundle.getString("Convert.FailReason.write_file_fail"));
		}

		callback.progressUpdate(++progress, maxProgress);

		//done converting and conversion of dialog/display is written to file by here.

		return project.getProjectSaveFile();
	}

	private void addArrayControls(@NotNull String ownerDialogName, @NotNull Project project, @Nullable HeaderAssignment arrayOfControlsAssignment, @NotNull List<ArmaControl> controls, @NotNull String
			sourceName)
			throws HeaderConversionException {

		HeaderArray arrayOfControls = null;
		if (arrayOfControlsAssignment instanceof HeaderArrayAssignment) {
			arrayOfControls = ((HeaderArrayAssignment) arrayOfControlsAssignment).getArray();
		} else {
			convertError(ownerDialogName, bundle.getString("Convert.FailReason.bg_controls_assignment_not_array"));
		}

		List<HeaderArrayItem> items = arrayOfControls.getItems();
		for (HeaderArrayItem arrayItem : items) {
			HeaderValue v = arrayItem.getValue();
			if (v instanceof HeaderArray) {
				convertError(ownerDialogName, String.format(bundle.getString("Convert.FailReason.bg_controls_assignment_not_array_f"), arrayOfControlsAssignment.getVariableName()));
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
				convertError(ownerDialogName, String.format(bundle.getString("Convert.FailReason.control_class_in_array_dne_f"), className, sourceName));
			}
			controls.add(getArmaControl(ownerDialogName, project, matchedClassRef.getValue()));
		}
	}

	@NotNull
	private ArmaControl getArmaControl(@NotNull String ownerDialogName, @NotNull Project project, @NotNull HeaderClass headerClass) throws HeaderConversionException {
		final String controlClassName = headerClass.getClassName();

		//get control type
		HeaderAssignment controlTypeAssign = headerFile.getAssignmentByVarName(headerClass, CONTROL_TYPE, false);
		if (controlTypeAssign == null) {
			convertError(ownerDialogName, String.format(bundle.getString("Convert.FailReason.control_type_missing_f"), controlClassName));
		}
		ControlType controlType = null;
		try {
			controlType = ControlType.findById(Integer.parseInt(controlTypeAssign.getValue().getContent()));
		} catch (IllegalArgumentException e) {
			convertError(ownerDialogName, String.format(bundle.getString("Convert.FailReason.control_type_nan_f"), controlClassName));
		}

		//create the control
		ArmaControl armaControl = ArmaControl.createControl(controlClassName, ArmaControlLookup.findByControlType(controlType), resolution, env, project);


		//load all properties and nested classes
		List<ControlPropertyLookupConstant> inheritProperties = new LinkedList<>();
		loadAllProperties(project, headerClass, armaControl, inheritProperties);

		//get the extend class
		ControlClass extendClass = null;
		if (headerClass.getExtendClassName() != null) {
			extendClass = project.getCustomControlClassRegistry().findControlClassByName(headerClass.getExtendClassName());
			if (extendClass == null) {
				HeaderClass extendHeaderClass = headerFile.getExtendClass(headerClass, false);
				if (extendHeaderClass == null) {
					convertError(ownerDialogName, String.format(bundle.getString("Error.no_class_f"), headerClass.getExtendClassName()));
				}
				extendClass = createAndAppendCustomControlClass(project, extendHeaderClass);
			}
		}


		// After setting the properties, extend the class. As mentioned in the ControlProperty.inherit() documentation,
		// the previous values of the control property will be saved
		armaControl.extendControlClass(extendClass);

		for (ControlPropertyLookupConstant inherit : inheritProperties) {
			armaControl.inheritProperty(inherit);
		}

		return armaControl;
	}

	/**
	 Creates values from the given {@link HeaderClass} by looking through all of its {@link HeaderAssignment}
	 instances. For each of those {@link HeaderAssignment}, a {@link ControlProperty} will try to be set to the
	 matching {@link HeaderAssignment} by comparing {@link HeaderAssignment#getVariableName()} and
	 {@link ControlProperty#getName()} (comparison is not case sensitive). The {@link ControlProperty} instances will
	 come from {@link ControlClass#getAllChildProperties()}.
	 <p>
	 This method will also load any nested {@link ControlClass} inside <code>controlClass</code> by recursively
	 calling this method.

	 @param project project to use
	 @param headerClass the {@link HeaderClass} to fetch {@link HeaderAssignment} from
	 @param controlClass the {@link ControlClass} to pair {@link HeaderAssignment}s with {@link ControlProperty}.
	 @param inheritProperties a list to insert {@link ControlProperty} instances that couldn't be paired with a
	 {@link HeaderAssignment}
	 */
	private void loadAllProperties(@NotNull Project project, @NotNull HeaderClass headerClass,
								   @NotNull ControlClass controlClass, @Nullable List<ControlPropertyLookupConstant>
										   inheritProperties) {
		for (ControlProperty property : controlClass.getAllChildProperties()) {
			HeaderAssignment assignment = headerClass.getAssignments().getByVarName(property.getName(), false);
			if (assignment == null && inheritProperties != null) {
				inheritProperties.add(property.getPropertyLookup());
				continue;
			}

			SerializableValue v = createValueFromAssignment(assignment, property.getInitialPropertyType());
			if (v != null) {
				property.setValue(v);
			} else {
				property.setValue(new SVRaw(assignment.getAsString(), property.getInitialPropertyType()));
			}

			Macro m = checkAndGetStringTableMacro(assignment.getValue().getContent(), project);
			if (m != null) {
				property.setValueToMacro(m);
			}
		}

		for (ControlClass nestedClass : controlClass.getAllNestedClasses()) {
			HeaderClass match = headerClass.getNestedClasses().getByName(nestedClass.getClassName(), false);
			if (match != null) {
				loadAllProperties(project, match, nestedClass, null);
			}
		}
	}

	@NotNull
	private ControlClass createAndAppendCustomControlClass(@NotNull Project project, @NotNull HeaderClass headerClass) {
		List<ControlPropertySpecification> optional = new ArrayList<>(headerClass.getAssignments().size());
		for (HeaderAssignment assignment : headerClass.getAssignments()) {
			//todo handle custom properties because the user may use the dialog class for more than just dialogs!
			List<ControlPropertyLookup> matchedByName = ControlPropertyLookup.getAllOfByName(assignment.getVariableName(), false);
			if (matchedByName.isEmpty()) {
				//todo
				continue;
			}
			SerializableValue value = null;
			ControlPropertyLookup usedLookup = null;
			String macroName = null;
			for (ControlPropertyLookup lookup : matchedByName) {
				Macro m = checkAndGetStringTableMacro(assignment.getValue().getContent(), project);
				macroName = m == null ? null : m.getKey();

				value = createValueFromAssignment(assignment, lookup.getPropertyType());
				if (value != null) {
					usedLookup = lookup;
					break;
				}
			}
			if (usedLookup == null) {
				//todo
				continue;
			}
			optional.add(new ControlPropertySpecification(usedLookup, value, macroName));
		}
		ControlClassSpecification ccs = new ControlClassSpecification(
				headerClass.getClassName(),
				Collections.emptyList(),
				optional
		);

		CustomControlClass ccc = project.getCustomControlClassRegistry().addControlClass(ccs);


		if (headerClass.getExtendClassName() != null) {
			ControlClass cc = createAndAppendCustomControlClass(project, headerFile.getExtendClass(headerClass, false));
			ccc.getControlClass().extendControlClass(cc);
		}

		return ccc.getControlClass();
	}

	/**
	 Attempts to create a {@link SerializableValue} from the given {@link HeaderAssignment}. The value will be
	 created with {@link PropertyType} <code>initialPropertyType</code>.

	 @param assignment assignment to use
	 @param initialPropertyType type to use
	 @return the created value with type ({@link SerializableValue#getPropertyType()}) equal to
	 <code>initialPropertyType</code>, or null if value couldn't be created
	 */
	private SerializableValue createValueFromAssignment(@NotNull HeaderAssignment assignment, @NotNull PropertyType initialPropertyType) {
		int vCount = initialPropertyType.getPropertyValuesSize();
		if (assignment.getValue() instanceof HeaderArray) {
			HeaderArray headerArray = (HeaderArray) assignment.getValue();
			if (vCount < headerArray.getItems().size()) {
				return null;
			}
			String[] items = new String[headerArray.getItems().size()];
			int i = 0;
			for (HeaderArrayItem arrayItem : headerArray.getItems()) {
				items[i] = removeQuotes(arrayItem.getAsString());
				i++;
			}
			try {
				return SerializableValue.constructNew(dataContext, initialPropertyType, items);
			} catch (SerializableValueConstructionException ignore) {

			}
		} else {
			if (vCount > 1) {
				return null;
			}
			String assignmentValue = assignment.getValue().getContent();
			boolean wasString = false;
			if (assignmentValue.charAt(0) == '"' || assignmentValue.charAt(0) == '\'') {
				assignmentValue = removeQuotes(assignmentValue); //chop off quotes
				wasString = true;
			}
			try {
				return SerializableValue.constructNew(dataContext, initialPropertyType, assignmentValue);
			} catch (SerializableValueConstructionException ignore) {

			}
			if (wasString) {
				try {
					return SerializableValue.constructNew(dataContext, PropertyType.String, assignmentValue);
				} catch (SerializableValueConstructionException ignore) {

				}
			}
		}
		return null;
	}

	@Nullable
	private Macro checkAndGetStringTableMacro(@NotNull String possibleStr_Key, @NotNull Project project) {
		possibleStr_Key = possibleStr_Key.toLowerCase();
		final boolean str_ = possibleStr_Key.startsWith("$str_");
		final boolean quote_str_ = possibleStr_Key.startsWith("\"$str_") || possibleStr_Key.startsWith("'$str_");
		if ((str_ || quote_str_) && project.getStringTable() != null) {
			StringTableKey key = project.getStringTable()
					.getKeyById(
							str_ ? possibleStr_Key.substring(1) : possibleStr_Key.substring(2, possibleStr_Key.length() - 1)
					); //remove $ or "$ at start
			return key;
		}
		return null;
	}


	@NotNull
	private String removeQuotes(String stringLiteral) {
		if (stringLiteral.length() >= 2 && (stringLiteral.charAt(0) == '"' || stringLiteral.charAt(0) == '\'') && stringLiteral.charAt(0) == stringLiteral.charAt(stringLiteral.length() - 1)) {
			return stringLiteral.substring(1, stringLiteral.length() - 1);
		}
		return stringLiteral;
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

	private void convertError(@NotNull String dialogClassName, @NotNull String s) throws HeaderConversionException {
		s = String.format(bundle.getString("Convert.fail_f"), dialogClassName, s);
		throw new HeaderConversionException(s);
	}

	/**
	 Used in conjunction with {@link HeaderToProject}

	 @author Kayler
	 @since 04/30/2017
	 */
	public interface ConversionCallback {
		/**
		 Invoked when the conversion requires dialogs (given as their class names) to be converted.

		 @param classesDiscovered all discovered dialogs
		 @return a list of dialog class names that should be converted.
		 */
		@NotNull List<String> selectClassesToSave(@NotNull List<String> classesDiscovered);

		/**
		 Used to pass a message (often a status update)

		 @param msg the message
		 */
		void message(@NotNull String msg);

		/**
		 Get a progress update on the conversion. A percentage can be acquired by doing stepsCompleted/totalSteps

		 @param stepsCompleted how many steps have been complete
		 @param totalSteps number of total steps
		 */
		void progressUpdate(int stepsCompleted, int totalSteps);

		/**
		 Invoked when all dialogs have converted

		 @see #selectClassesToSave(List)
		 */
		void finishedParse();

		/**
		 Invoked when a dialog couldn't be converted

		 @param dialogClassName the dialog's class name
		 @param e the exception that occurred
		 */
		void conversionFailed(@NotNull String dialogClassName, @NotNull HeaderConversionException e);
	}

}
