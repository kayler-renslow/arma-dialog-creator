package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.ProjectInfo;
import com.kaylerrenslow.armaDialogCreator.data.export.HeaderFileType;
import com.kaylerrenslow.armaDialogCreator.data.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.data.tree.TreeNode;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import java.io.File;
import java.util.*;

/**
 A project loader for save-version='1'

 @author Kayler
 @since 08/07/2016. */
public class ProjectLoaderVersion1 extends ProjectVersionLoader {

	private final LinkedList<AfterLoadJob> jobs = new LinkedList<>();
	private final ProjectInfo info;
	private ArmaResolution resolution;
	private Env env;
	private ResourceBundle bundle = Lang.getBundle("ProjectXmlParseBundle");

	protected ProjectLoaderVersion1(@NotNull ProjectInfo info, @NotNull ProjectXmlLoader loader) throws XmlParseException {
		super(loader);
		this.info = info;
	}

	@Override
	public void parseDocument() throws XmlParseException {
		loadProject();

		Collections.sort(jobs);
		for (AfterLoadJob job : jobs) {
			job.doWork(project, this);
		}
	}

	private void loadProject() throws XmlParseException {
		try {
			resolution = DataKeys.ARMA_RESOLUTION.get(dataContext);
			env = DataKeys.ENV.get(dataContext);
			String projectName = document.getDocumentElement().getAttribute("name");
			project = new Project(this.loader.applicationData, info);
			project.setProjectName(projectName);
			loadStringtableXml();
			loadMacroRegistry();
			loadCustomControlClassRegistries();

			ArmaDisplay editingDisplay = fetchEditingDisplay(project.getMacroRegistry().getMacros());
			if (editingDisplay != null) {
				project.setEditingDisplay(editingDisplay);
			}
			project.setProjectDescription(getProjectDescription());

			fetchExportConfiguration();
			loadResourceRegistry();

		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(e.getMessage(), e);
		}
	}

	private void loadStringtableXml() {
		List<Element> stringtableElementList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "stringtable");
		if (stringtableElementList.size() <= 0) {
			return;
		}
		try {
			StringTable stringTable = new DefaultStringTableXmlParser(new File(XmlUtil.getImmediateTextContent(stringtableElementList.get(0)))).createStringTableInstance();
			project.setStringTable(stringTable);
		} catch (Exception e) {
			addError(new ParseError(bundle.getString("ProjectLoad.couldnt_load_stringtable_xml"), bundle.getString("ProjectLoad.couldnt_load_stringtable_xml_recover")));
		}
	}

	private void fetchExportConfiguration() {
		List<Element> exportConfigElementList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "export-config");
		if (exportConfigElementList.size() <= 0) {
			return;
		}
		Element exportConfigElement = exportConfigElementList.get(0);
		List<Element> configAttributeElementList = XmlUtil.getChildElementsWithTagName(exportConfigElement, "config-attribute");
		if (configAttributeElementList.size() == 0) {
			return;
		}
		final ProjectExportConfiguration exportConfiguration = project.getExportConfiguration();
		for (Element configAttributeElement : configAttributeElementList) {
			String attributeName = configAttributeElement.getAttribute("name");
			switch (attributeName) {
				case "export-class-name": {
					String exportClassName = XmlUtil.getImmediateTextContent(configAttributeElement);
					exportConfiguration.setExportClassName(exportClassName.trim());
					break;
				}
				case "export-location": {
					String exportLocation = XmlUtil.getImmediateTextContent(configAttributeElement);
					File exportLocationFile = new File(exportLocation.trim());
					if (!exportLocationFile.isDirectory()) {
						return;
					}
					exportConfiguration.setExportDirectory(exportLocationFile);
					break;
				}
				case "place-adc-notice": {
					boolean placeAdcNotice = XmlUtil.getImmediateTextContent(configAttributeElement).trim().equalsIgnoreCase("true");
					exportConfiguration.setPlaceAdcNotice(placeAdcNotice);
					break;
				}
				case "export-macros-to-file": {
					boolean exportMacrosToFile = XmlUtil.getImmediateTextContent(configAttributeElement).trim().equalsIgnoreCase("true");
					exportConfiguration.setExportMacrosToFile(exportMacrosToFile);
					break;
				}
				case "export-file-type-ext": {
					String fileTypeExt = XmlUtil.getImmediateTextContent(configAttributeElement).trim();
					for (HeaderFileType type : HeaderFileType.values()) {
						if (type.getExtension().equalsIgnoreCase(fileTypeExt)) {
							exportConfiguration.setFileType(type);
							break;
						}
					}
					break;
				}
				default: {
					break;
				}
			}
		}
	}

	private void loadResourceRegistry() {
		List<Element> externalResourcesElementGroups = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "external-resources");
		for (Element externalResourcesElementGroup : externalResourcesElementGroups) {
			ResourceRegistryXmlLoader.loadRegistryFromElement(project.getResourceRegistry(), externalResourcesElementGroup);
		}
	}

	private void loadCustomControlClassRegistries() throws Exception {
		//load workspace custom control classes
		if (project.getCustomControlClassesFile().exists()) {
			XmlLoader xmlLoader = new XmlLoader(project.getCustomControlClassesFile(), dataContext, this.loader.keys);
			ProjectXmlUtil.loadCustomControlClasses(xmlLoader.document.getDocumentElement(), dataContext, this.loader,
					controlClassSpecification -> {
						jobs.add(new CreateCustomControlClassJob(controlClassSpecification, false));
						return null;
					}
			);
		}
		ProjectXmlUtil.loadCustomControlClasses(document.getDocumentElement(), dataContext, this.loader,
				controlClassSpecification -> {
					jobs.add(new CreateCustomControlClassJob(controlClassSpecification, true));
					return null;
				}
		);
	}

	private void loadMacroRegistry() {
		List<Element> macrosGroupElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "macros");
		List<Element> macroElements;
		final String macro = "macro";
		final String key = "key";
		final String propertyTypeId = "property-type-id";
		final String comment = "comment";
		for (Element macrosGroupElement : macrosGroupElements) {
			macroElements = XmlUtil.getChildElementsWithTagName(macrosGroupElement, macro);
			for (Element macroElement : macroElements) {
				String keyAttr = macroElement.getAttribute(key);
				String propertyTypeAttr = macroElement.getAttribute(propertyTypeId);
				String commentAttr = macroElement.getAttribute(comment);
				if (keyAttr.length() == 0 || propertyTypeAttr.length() == 0) {
					addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_macro_key_or_type_f"), keyAttr, propertyTypeAttr)));
					continue;
				}
				PropertyType propertyType;
				try {
					propertyType = PropertyType.findById(Integer.parseInt(propertyTypeAttr));
				} catch (IllegalArgumentException e) { //will catch number format exception
					addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_macro_property_type_f"), propertyTypeAttr)));
					continue;
				}
				SerializableValue value = getValue(macro, propertyType, macroElement);
				if (value == null) {
					continue;
				}
				Macro<?> macroObj = Macro.newMacro(keyAttr, value);
				project.getMacroRegistry().addMacro(macroObj);
				macroObj.setComment(commentAttr);
			}
		}

	}

	private String getProjectDescription() {
		List<Element> descriptionElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "project-description");
		if (descriptionElements.size() > 0) {
			return XmlUtil.getImmediateTextContent(descriptionElements.get(0));
		}
		return null;
	}

	private ArmaDisplay fetchEditingDisplay(List<Macro> macros) {
		List<Element> displayElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "display");
		if (displayElements.size() <= 0) {
			return null;
		}
		Element displayElement = displayElements.get(0);
		ArmaDisplay display = new ArmaDisplay();

		List<Element> displayPropertyElements = XmlUtil.getChildElementsWithTagName(displayElement, "display-property");
		for (Element displayPropertyElement : displayPropertyElements) {
			String lookupId = displayPropertyElement.getAttribute("id");
			try {
				int id = Integer.parseInt(lookupId);
				DisplayPropertyLookup lookup = DisplayPropertyLookup.findById(id);
				SerializableValue value = getValue("Display." + lookup.getPropertyName(), lookup.getPropertyType(), displayPropertyElement);
				switch (lookup) {
					case IDD: {
						display.getIddProperty().setValue(value);
						break;
					}
					default: {
						display.getDisplayProperties().add(new DisplayProperty(lookup, value));
						break;
					}
				}

			} catch (IllegalArgumentException e) {
				addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_display_property_lookup_id_f"), lookupId), ParseError.genericRecover("-1")));
			}
		}

		List<Element> displayControlElements = XmlUtil.getChildElementsWithTagName(displayElement, "display-controls");
		List<ArmaControl> controls;
		String controlsType;
		for (Element displayControlElement : displayControlElements) {
			controlsType = displayControlElement.getAttribute("type");
			switch (controlsType) {
				case "background": {
					controls = buildStructureAndGetControls(treeStructureBg.getRoot(), displayControlElement, macros);
					for (ArmaControl control : controls) {
						display.getBackgroundControls().add(control);
					}
					break;
				}
				case "main": {
					controls = buildStructureAndGetControls(treeStructureMain.getRoot(), displayControlElement, macros);
					for (ArmaControl control : controls) {
						display.getControls().add(control);
					}
					break;
				}
			}
		}
		return display;
	}

	private List<ArmaControl> buildStructureAndGetControls(TreeNode<ArmaControl> parent, Element parentElement, List<Macro> macros) {
		List<ArmaControl> controls = new LinkedList<>();
		List<Element> tagElements = XmlUtil.getChildElementsWithTagName(parentElement, "*");
		ArmaControl control;
		TreeNode<ArmaControl> treeNode;
		for (Element controlElement : tagElements) {
			switch (controlElement.getTagName()) {
				case "control": {
					control = getControl(controlElement, macros);
					if (control == null) {
						return controls;
					}
					treeNode = new TreeNode.Simple<>(control, control.getClassName(), false);
					parent.getChildren().add(treeNode);
					break;
				}
				case "control-group": {
					control = getControl(controlElement, macros);
					if (control == null) {
						return controls;
					}
					ArmaControlGroup group = (ArmaControlGroup) control;
					treeNode = new TreeNode.Simple<>(group, group.getClassName(), false);
					parent.getChildren().add(treeNode);
					List<ArmaControl> controlsToAdd = buildStructureAndGetControls(treeNode, controlElement, macros);
					for (ArmaControl add : controlsToAdd) {
						group.getControls().add(add);
					}
					break;
				}
				case "folder": {
					treeNode = new TreeNode.Simple<>(null, controlElement.getAttribute("name"), true);
					parent.getChildren().add(treeNode);
					controls.addAll(buildStructureAndGetControls(treeNode, controlElement, macros));
					continue;
				}
				default: {
					continue;
				}
			}
			controls.add(control);
		}

		return controls;
	}

	private ArmaControl getControl(Element controlElement, List<Macro> macros) {
		//enabled setup
		boolean enabled = true;
		{
			String enabledAttr = controlElement.getAttribute("enabled").trim();
			if (enabledAttr.length() != 0) {
				enabled = enabledAttr.equals("t");
			}
		}

		//ghost setup
		boolean ghost = false;
		{
			String ghostAttr = controlElement.getAttribute("ghost").trim();
			if (ghostAttr.length() != 0) {
				ghost = ghostAttr.equals("t");
			}
		}

		//class name
		String controlClassName = controlElement.getAttribute("class-name");
		if (controlClassName.trim().length() == 0) {
			addError(new ParseError(String.format(bundle.getString("ProjectLoad.missing_control_name"), controlElement.getTextContent())));
			return null;
		}


		//control type
		ControlType controlType;
		String controlTypeStr = controlElement.getAttribute("control-id");
		try {
			int controlTypeId = Integer.parseInt(controlTypeStr);
			controlType = ControlType.findById(controlTypeId);
		} catch (IllegalArgumentException e) { //will catch number format exception as well
			addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_control_type_f"), controlTypeStr, controlClassName)));
			return null;
		}

		ArmaControlLookup armaControlLookup = ArmaControlLookup.findByControlType(controlType);

		//control properties
		List<Element> controlPropertyElements = XmlUtil.getChildElementsWithTagName(controlElement, "property");
		LinkedList<ControlPropertySpecification> properties = new LinkedList<>();
		for (Element controlPropertyElement : controlPropertyElements) {
			ControlPropertySpecification property = ProjectXmlUtil.loadControlProperty(controlClassName, controlPropertyElement, dataContext, this.loader);
			if (property != null) {
				properties.add(property);
			}
		}


		//control construction
		ArmaControl control = ArmaControl.createControl(controlClassName, armaControlLookup, resolution, env, project);


		//property matching and value setting
		for (ControlPropertySpecification specification : properties) {
			ControlProperty p = control.findPropertyNullable(specification.getPropertyLookup());
			if (p == null) {
				continue;
			}
			p.setTo(specification, project);

			if (!(p.getValue() instanceof SVControlStyleGroup)) {
				continue;
			}
			if (!(armaControlLookup.specProvider instanceof AllowedStyleProvider)) {
				continue;
			}
			// Since some styles can have an equal value, we will cross reference the allowed styles with the present styles.
			// If a style is present that isn't allowed, it will be replaced with an allowed style with an equal value.
			// This isn't 100% foolproof since multiple allowed styles can have equal values as well.
			// However, there shouldn't ever be a case where multiple allowed styles should have equal values
			// because it doesn't make intuitive sense.
			AllowedStyleProvider allowedStyleProvider = (AllowedStyleProvider) armaControlLookup.specProvider;
			ControlStyle[] allowedStyles = allowedStyleProvider.getAllowedStyles();
			List<ControlStyle> fixedStyles = new ArrayList<>(allowedStyles.length);
			boolean replace = false;
			if (p.getValue() instanceof SVControlStyleGroup) {
				SVControlStyleGroup g = (SVControlStyleGroup) p.getValue();
				ControlStyle[] controlStyles = g.getStyleArray();
				for (ControlStyle controlStyle : controlStyles) {
					boolean match = false;
					for (ControlStyle allowedStyle : allowedStyles) {
						if (controlStyle == allowedStyle) {
							match = true;
							break;
						}
					}
					if (!match) {
						replace = true;
						for (ControlStyle allowedStyle : allowedStyles) {
							if (controlStyle.styleValue == allowedStyle.styleValue) {
								fixedStyles.add(allowedStyle);
								break;
							}
						}
					} else {
						fixedStyles.add(controlStyle);
					}
				}
			}
			if (replace && fixedStyles.size() > 0) {
				p.setValue(new SVControlStyleGroup(fixedStyles));
			}
		}

		//load nested classes
		List<Element> reqNestedClassesElementGroups = XmlUtil.getChildElementsWithTagName(controlElement, "nested-required");
		List<ControlClassSpecification> nestedRequired = null;
		List<ControlClassSpecification> nestedOptional = null;
		if (reqNestedClassesElementGroups.size() > 0) {
			nestedRequired = ProjectXmlUtil.loadControlClassSpecifications(reqNestedClassesElementGroups.get(0), dataContext, this.loader);
		}

		List<Element> optNestedClassesElementGroups = XmlUtil.getChildElementsWithTagName(controlElement, "nested-optional");
		if (optNestedClassesElementGroups.size() > 0) {
			nestedOptional = ProjectXmlUtil.loadControlClassSpecifications(optNestedClassesElementGroups.get(0), dataContext, this.loader);
		}

		jobs.add(new ControlNestedClassesJob(control, nestedRequired, nestedOptional));

		List<ControlPropertyLookup> inheritControlProperties = ProjectXmlUtil.loadInheritedControlProperties(controlElement, this.loader);

		//add extend job if needed
		String extendClassName = controlElement.getAttribute("extend-class");
		if (extendClassName.length() > 0) {
			jobs.add(new ControlExtendJob(extendClassName, control, inheritControlProperties));
		}

		//must set ghost state first since ghost=!visible && !enabled
		//and we don't want to overwrite enabled when ghost is set
		control.getRenderer().setGhost(ghost);
		control.getRenderer().setEnabled(enabled);

		return control;
	}

	private SerializableValue getValue(@NotNull String requester, @NotNull PropertyType propertyType, @NotNull Element controlPropertyElement) {
		return ProjectXmlUtil.loadValue(requester, controlPropertyElement, propertyType, dataContext, this.loader);
	}


	private interface AfterLoadJob extends Comparable<AfterLoadJob> {
		void doWork(@NotNull Project project, @NotNull ProjectVersionLoader loader);
	}

	private class CreateCustomControlClassJob implements AfterLoadJob {

		private ControlClassSpecification spec;
		private final boolean loadInProjectRegistry;

		public CreateCustomControlClassJob(@NotNull ControlClassSpecification spec, boolean loadInProjectRegistry) {
			this.spec = spec;
			this.loadInProjectRegistry = loadInProjectRegistry;
		}

		@Override
		public void doWork(@NotNull Project project, @NotNull ProjectVersionLoader loader) {
			CustomControlClass customControlClass = new CustomControlClass(
					spec, project,
					loadInProjectRegistry ? CustomControlClass.Scope.Project : CustomControlClass.Scope.Workspace
			);
			project.addCustomControlClass(customControlClass);
		}

		@Override
		public int compareTo(@NotNull ProjectLoaderVersion1.AfterLoadJob o) {
			if (o instanceof CreateCustomControlClassJob) {
				CreateCustomControlClassJob other = (CreateCustomControlClassJob) o;
				if (other.spec.getExtendClassName() == null) {
					//run this first
					return -1;
				}
				if (spec.getExtendClassName() == null) {
					//run this first
					return -1;
				}
				if (spec.getExtendClassName().equals(other.spec.getClassName())) {
					//allow the other to load first
					return 1;
				}
				if (other.spec.getExtendClassName().equals(spec.getClassName())) {
					//let this job load first
					return -1;
				}

			}
			return -1;
		}
	}

	private class ControlNestedClassesJob implements AfterLoadJob {

		private final ControlClass addToMe;
		private final List<ControlClassSpecification> requiredNested;
		private final List<ControlClassSpecification> optionalNested;

		public ControlNestedClassesJob(@NotNull ControlClass addToMe, @Nullable List<ControlClassSpecification> requiredNested, @Nullable List<ControlClassSpecification> optionalNested) {
			this.addToMe = addToMe;
			this.requiredNested = requiredNested;
			this.optionalNested = optionalNested;
		}

		@Override
		public void doWork(@NotNull Project project, @NotNull ProjectVersionLoader loader) {
			if (requiredNested != null) {
				for (ControlClassSpecification nested : requiredNested) {
					loadClass(project, nested);
				}
			}
			if (optionalNested != null) {
				for (ControlClassSpecification nested : optionalNested) {
					loadClass(project, nested);
				}
			}
		}

		private void loadClass(@NotNull Project project, ControlClassSpecification nested) {
			try {
				ControlClass nestedClass = addToMe.findNestedClass(nested.getClassName());
				nestedClass.setTo(nested.constructNewControlClass(project));
			} catch (IllegalArgumentException ignore) {

			}
		}

		@Override
		public int compareTo(@NotNull ProjectLoaderVersion1.AfterLoadJob o) {
			if (o instanceof ControlNestedClassesJob) {
				return 0;
			}
			return 1;
		}
	}

	private class ControlExtendJob implements AfterLoadJob {
		private final String extendThisControlClassName;
		private final ArmaControl setMyExtend;
		private final List<ControlPropertyLookup> inheritProperties;

		public ControlExtendJob(@NotNull String extendThisControlClassName, @NotNull ArmaControl setMyExtend, @NotNull List<ControlPropertyLookup> inheritProperties) {
			this.extendThisControlClassName = extendThisControlClassName;
			this.setMyExtend = setMyExtend;
			this.inheritProperties = inheritProperties;
		}


		@Override
		public void doWork(@NotNull Project project, @NotNull ProjectVersionLoader loader) {
			ControlClass cc = project.findControlClassByName(extendThisControlClassName);

			if (cc == null) {
				loader.addError(new ParseError(String.format(bundle.getString("ProjectLoad.couldnt_match_extend_class_f"), extendThisControlClassName, setMyExtend.getClassName())));
				return;
			}

			setMyExtend.extendControlClass(cc);
			for (ControlPropertyLookup inheritProperty : inheritProperties) {
				setMyExtend.inheritProperty(inheritProperty);
			}
		}

		@Override
		public int compareTo(@NotNull ProjectLoaderVersion1.AfterLoadJob o) {
			if (o instanceof ControlExtendJob) {
				return 0;
			}
			//always let other jobs go before this one
			return 1;
		}
	}


}
