package com.armadialogcreator.data.oldprojectloader;


/**
 A project loader for save-version='1'

 @author Kayler
 @since 08/07/2016. */
public class ProjectLoaderVersion1 {
	//
	//	private final ProjectDescriptor info;
	//	private ArmaResolution resolution;
	//	private Env env;
	//	private final ResourceBundle bundle = Lang.getBundle("ProjectXmlParseBundle");
	//
	//	protected ProjectLoaderVersion1(@NotNull ProjectDescriptor info, @NotNull Document d) throws XmlParseException {
	//		super(loader);
	//		this.info = info;
	//	}
	//
	//	@Override
	//	public void readDocument() throws XmlParseException {
	//		loadProject();
	//	}
	//
	//	private void loadProject() throws XmlParseException {
	//		try {
	//			resolution = DataKeys.ARMA_RESOLUTION.get(dataContext);
	//			env = Env.ENV.get(dataContext);
	//			String projectName = document.getDocumentElement().getAttribute("name");
	//			project = new Project(this.loader.applicationData, info);
	//			project.setProjectName(projectName);
	//
	//			ControlClassXmlHelper controlClassXmlHelper = new ControlClassXmlHelper(project, this.loader);
	//
	//			loadStringtableXml();
	//			loadMacroRegistry();
	//			loadCustomControlClassRegistries(controlClassXmlHelper);
	//
	//			ArmaDisplay editingDisplay = fetchEditingDisplay(project.getMacroRegistry().getMacros(), controlClassXmlHelper);
	//			if (editingDisplay != null) {
	//				project.setEditingDisplay(editingDisplay);
	//			}
	//			project.setProjectDescription(getProjectDescription());
	//
	//			fetchExportConfiguration();
	//			loadResourceRegistry();
	//
	//			controlClassXmlHelper.runJobs();
	//
	//		} catch (Exception e) {
	//			e.printStackTrace(System.out);
	//			throw new XmlParseException(e.getMessage(), e);
	//		}
	//	}
	//
	//	private void loadStringtableXml() {
	//		List<Element> stringtableElementList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "stringtable");
	//		if (stringtableElementList.size() <= 0) {
	//			return;
	//		}
	//		try {
	//			StringTable stringTable = new DefaultStringTableXmlParser(new File(XmlUtil.getImmediateTextContent(stringtableElementList.get(0)))).createStringTableInstance();
	//			project.setStringTable(stringTable);
	//		} catch (Exception e) {
	//			addError(new ParseError(bundle.getString("ProjectLoad.couldnt_load_stringtable_xml"), bundle.getString("ProjectLoad.couldnt_load_stringtable_xml_recover")));
	//		}
	//	}
	//
	//	private void fetchExportConfiguration() {
	//		List<Element> exportConfigElementList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "export-config");
	//		if (exportConfigElementList.size() <= 0) {
	//			return;
	//		}
	//		Element exportConfigElement = exportConfigElementList.get(0);
	//		List<Element> configAttributeElementList = XmlUtil.getChildElementsWithTagName(exportConfigElement, "config-attribute");
	//		if (configAttributeElementList.size() == 0) {
	//			return;
	//		}
	//		final ProjectExportConfiguration exportConfiguration = project.getExportConfiguration();
	//		for (Element configAttributeElement : configAttributeElementList) {
	//			String attributeName = configAttributeElement.getAttribute("name");
	//			switch (attributeName) {
	//				case "export-class-name": {
	//					String exportClassName = XmlUtil.getImmediateTextContent(configAttributeElement);
	//					exportConfiguration.setExportClassName(exportClassName.trim());
	//					break;
	//				}
	//				case "export-location": {
	//					String exportLocation = XmlUtil.getImmediateTextContent(configAttributeElement);
	//					File exportLocationFile = new File(exportLocation.trim());
	//					if (!exportLocationFile.isDirectory()) {
	//						return;
	//					}
	//					exportConfiguration.setExportDirectory(exportLocationFile);
	//					break;
	//				}
	//				case "place-adc-notice": {
	//					boolean placeAdcNotice = XmlUtil.getImmediateTextContent(configAttributeElement).trim().equalsIgnoreCase("true");
	//					exportConfiguration.setPlaceAdcNotice(placeAdcNotice);
	//					break;
	//				}
	//				case "export-macros-to-file": {
	//					boolean exportMacrosToFile = XmlUtil.getImmediateTextContent(configAttributeElement).trim().equalsIgnoreCase("true");
	//					exportConfiguration.setExportMacrosToFile(exportMacrosToFile);
	//					break;
	//				}
	//				case "export-file-type-ext": {
	//					String fileTypeExt = XmlUtil.getImmediateTextContent(configAttributeElement).trim();
	//					for (HeaderFileType type : HeaderFileType.values()) {
	//						if (type.getExtension().equalsIgnoreCase(fileTypeExt)) {
	//							exportConfiguration.setFileType(type);
	//							break;
	//						}
	//					}
	//					break;
	//				}
	//				default: {
	//					break;
	//				}
	//			}
	//		}
	//	}
	//
	//	private void loadResourceRegistry() {
	//		List<Element> externalResourcesElementGroups = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "external-resources");
	//		for (Element externalResourcesElementGroup : externalResourcesElementGroups) {
	//			//			ResourceRegistryXmlLoader.loadRegistryFromElement(project.getFileDependencyRegistry(), externalResourcesElementGroup);
	//		}
	//	}
	//
	//	private void loadCustomControlClassRegistries(@NotNull ControlClassXmlHelper controlClassXmlHelper) throws Exception {
	//		//load workspace custom control classes
	//		WorkspaceCustomControlClassXmlReader workspaceLoader = new WorkspaceCustomControlClassXmlReader(
	//				this.loader.applicationData, controlClassXmlHelper, this.project
	//		);
	//		workspaceLoader.readDocument();
	//
	//		ProjectXmlUtil.loadCustomControlClasses(document.getDocumentElement(), dataContext, this.loader,
	//				controlClassSpecification -> {
	//					controlClassXmlHelper.addJob(new CreateCustomControlClassJob(controlClassSpecification, true));
	//					return null;
	//				}
	//		);
	//	}
	//
	//	private void loadMacroRegistry() {
	//		List<Element> macrosGroupElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "macros");
	//		List<Element> macroElements;
	//		final String macro = "macro";
	//		final String key = "key";
	//		final String propertyTypeId = "property-type-id";
	//		final String comment = "comment";
	//		for (Element macrosGroupElement : macrosGroupElements) {
	//			macroElements = XmlUtil.getChildElementsWithTagName(macrosGroupElement, macro);
	//			for (Element macroElement : macroElements) {
	//				String keyAttr = macroElement.getAttribute(key);
	//				String propertyTypeAttr = macroElement.getAttribute(propertyTypeId);
	//				String commentAttr = macroElement.getAttribute(comment);
	//				if (keyAttr.length() == 0 || propertyTypeAttr.length() == 0) {
	//					addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_macro_key_or_type_f"), keyAttr, propertyTypeAttr), bundle.getString("ProjectLoad.macro_cleared")));
	//					continue;
	//				}
	//				PropertyType propertyType;
	//				try {
	//					propertyType = PropertyType.findById(Integer.parseInt(propertyTypeAttr));
	//				} catch (IllegalArgumentException e) { //will catch number format exception
	//					addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_macro_property_type_f"), propertyTypeAttr)));
	//					continue;
	//				}
	//				SerializableValue value = null;
	//				if (propertyType == PropertyType.Raw) {
	//					value = ProjectXmlUtil.loadRawValue("MacroRegistry", null, macroElement, this.loader);
	//				} else {
	//					value = getValue("MacroRegistry", propertyType, macroElement);
	//				}
	//				if (value == null) {
	//					continue;
	//				}
	//				Macro<?> macroObj = Macro.newMacro(keyAttr, value);
	//				project.getMacroRegistry().addMacro(macroObj);
	//				macroObj.setComment(commentAttr);
	//			}
	//		}
	//
	//	}
	//
	//	private String getProjectDescription() {
	//		List<Element> descriptionElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "project-description");
	//		if (descriptionElements.size() > 0) {
	//			return XmlUtil.getImmediateTextContent(descriptionElements.get(0));
	//		}
	//		return null;
	//	}
	//
	//	private ArmaDisplay fetchEditingDisplay(List<Macro> macros, ControlClassXmlHelper controlClassXmlHelper) {
	//		List<Element> displayElements = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "display");
	//		if (displayElements.size() <= 0) {
	//			return null;
	//		}
	//		Element displayElement = displayElements.get(0);
	//		ArmaDisplay display = new ArmaDisplay();
	//
	//		List<Element> displayPropertyElements = XmlUtil.getChildElementsWithTagName(displayElement, "display-property");
	//		for (Element displayPropertyElement : displayPropertyElements) {
	//			String lookupId = displayPropertyElement.getAttribute("id");
	//			try {
	//				int id = Integer.parseInt(lookupId);
	//				DisplayPropertyLookup lookup = DisplayPropertyLookup.findById(id);
	//				SerializableValue value = getValue("Display." + lookup.getPropertyName(), lookup.getPropertyType(), displayPropertyElement);
	//				switch (lookup) {
	//					case IDD: {
	//						display.getIddProperty().setValue(value);
	//						break;
	//					}
	//					default: {
	//						display.getDisplayProperties().add(new DisplayProperty(lookup, value));
	//						break;
	//					}
	//				}
	//
	//			} catch (IllegalArgumentException e) {
	//				addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_display_property_lookup_id_f"), lookupId), genericRecover("-1")));
	//			}
	//		}
	//
	//		List<Element> displayControlElements = XmlUtil.getChildElementsWithTagName(displayElement, "display-controls");
	//		List<ArmaControl> controls;
	//		String controlsType;
	//		for (Element displayControlElement : displayControlElements) {
	//			controlsType = displayControlElement.getAttribute("type");
	//			switch (controlsType) {
	//				case "background": {
	//					controls = buildStructureAndGetControls(treeStructureBg.getRoot(), displayControlElement, macros, controlClassXmlHelper);
	//					for (ArmaControl control : controls) {
	//						display.getBackgroundControls().add(control);
	//					}
	//					break;
	//				}
	//				case "main": {
	//					controls = buildStructureAndGetControls(treeStructureMain.getRoot(), displayControlElement, macros, controlClassXmlHelper);
	//					for (ArmaControl control : controls) {
	//						display.getControls().add(control);
	//					}
	//					break;
	//				}
	//			}
	//		}
	//		return display;
	//	}
	//
	//	private List<ArmaControl> buildStructureAndGetControls(TreeNode<ArmaControl> parent, Element parentElement,
	//														   List<Macro> macros, ControlClassXmlHelper controlClassXmlHelper) {
	//		List<ArmaControl> controls = new LinkedList<>();
	//		List<Element> tagElements = XmlUtil.getChildElementsWithTagName(parentElement, "*");
	//		ArmaControl control;
	//		TreeNode<ArmaControl> treeNode;
	//		for (Element controlElement : tagElements) {
	//			switch (controlElement.getTagName()) {
	//				case "control": {
	//					control = getControl(controlElement, macros, controlClassXmlHelper);
	//					if (control == null) {
	//						return controls;
	//					}
	//					treeNode = new TreeNode.Simple<>(control, control.getClassName(), false);
	//					parent.getChildren().add(treeNode);
	//					break;
	//				}
	//				case "control-group": {
	//					control = getControl(controlElement, macros, controlClassXmlHelper);
	//					if (control == null) {
	//						return controls;
	//					}
	//					ArmaControlGroup group = (ArmaControlGroup) control;
	//					treeNode = new TreeNode.Simple<>(group, group.getClassName(), false);
	//					parent.getChildren().add(treeNode);
	//					List<ArmaControl> controlsToAdd = buildStructureAndGetControls(treeNode, controlElement, macros, controlClassXmlHelper);
	//					for (ArmaControl add : controlsToAdd) {
	//						group.getControls().add(add);
	//					}
	//					break;
	//				}
	//				case "folder": {
	//					treeNode = new TreeNode.Simple<>(null, controlElement.getAttribute("name"), true);
	//					parent.getChildren().add(treeNode);
	//					controls.addAll(buildStructureAndGetControls(treeNode, controlElement, macros, controlClassXmlHelper));
	//					continue;
	//				}
	//				default: {
	//					continue;
	//				}
	//			}
	//			controls.add(control);
	//		}
	//
	//		return controls;
	//	}
	//
	//	private ArmaControl getControl(Element controlElement, List<Macro> macros,
	//								   ControlClassXmlHelper controlClassXmlHelper) {
	//		//enabled setup
	//		boolean enabled = true;
	//		{
	//			String enabledAttr = controlElement.getAttribute("enabled").trim();
	//			if (enabledAttr.length() != 0) {
	//				enabled = enabledAttr.equals("t");
	//			}
	//		}
	//
	//		//ghost setup
	//		boolean ghost = false;
	//		{
	//			String ghostAttr = controlElement.getAttribute("ghost").trim();
	//			if (ghostAttr.length() != 0) {
	//				ghost = ghostAttr.equals("t");
	//			}
	//		}
	//
	//		//class name
	//		String controlClassName = controlElement.getAttribute("class-name");
	//		if (controlClassName.trim().length() == 0) {
	//			addError(new ParseError(String.format(bundle.getString("ProjectLoad.missing_control_name"), controlElement.getTextContent())));
	//			return null;
	//		}
	//
	//
	//		//control type
	//		ControlType controlType;
	//		String controlTypeStr = controlElement.getAttribute("control-id");
	//		try {
	//			int controlTypeId = Integer.parseInt(controlTypeStr);
	//			controlType = ControlType.findById(controlTypeId);
	//		} catch (IllegalArgumentException e) { //will catch number format exception as well
	//			addError(new ParseError(String.format(bundle.getString("ProjectLoad.bad_control_type_f"), controlTypeStr, controlClassName)));
	//			return null;
	//		}
	//
	//		ArmaControlLookup armaControlLookup = ArmaControlLookup.findByControlType(controlType);
	//
	//		//control properties
	//		List<Element> controlPropertyElements = XmlUtil.getChildElementsWithTagName(controlElement, "property");
	//		LinkedList<ControlPropertySpecification> properties = new LinkedList<>();
	//		for (Element controlPropertyElement : controlPropertyElements) {
	//			ControlPropertySpecification property = ProjectXmlUtil.loadControlProperty(controlClassName, controlPropertyElement, dataContext, this.loader);
	//			if (property != null) {
	//				properties.add(property);
	//			}
	//		}
	//
	//
	//		//control construction
	//		ArmaControl control = ArmaControl.createControl(controlClassName, armaControlLookup, resolution, env, project);
	//
	//
	//		//property matching and value setting
	//		for (ControlPropertySpecification specification : properties) {
	//			ControlProperty p = control.findPropertyNullable(specification.getPropertyLookup());
	//			if (p == null) {
	//				continue;
	//			}
	//			p.setTo(specification, project);
	//
	//			if (!(p.getValue() instanceof SVControlStyleGroup)) {
	//				continue;
	//			}
	//			if (!(armaControlLookup.specProvider instanceof AllowedStyleProvider)) {
	//				continue;
	//			}
	//
	//			if (p.getValue() instanceof SVControlStyleGroup) {
	//				p.setValue(SVControlStyleGroup.fixMisidentified((SVControlStyleGroup) p.getValue(), (AllowedStyleProvider) armaControlLookup.specProvider));
	//			}
	//		}
	//
	//		//load nested classes
	//		List<Element> reqNestedClassesElementGroups = XmlUtil.getChildElementsWithTagName(controlElement, "nested-required");
	//		List<ControlClassSpecification> nestedRequired = null;
	//		List<ControlClassSpecification> nestedOptional = null;
	//		if (reqNestedClassesElementGroups.size() > 0) {
	//			nestedRequired = ProjectXmlUtil.loadControlClassSpecifications(reqNestedClassesElementGroups.get(0), dataContext, this.loader);
	//		}
	//
	//		List<Element> optNestedClassesElementGroups = XmlUtil.getChildElementsWithTagName(controlElement, "nested-optional");
	//		if (optNestedClassesElementGroups.size() > 0) {
	//			nestedOptional = ProjectXmlUtil.loadControlClassSpecifications(optNestedClassesElementGroups.get(0), dataContext, this.loader);
	//		}
	//
	//		controlClassXmlHelper.addJob(new ControlNestedClassesJob(control, nestedRequired, nestedOptional));
	//
	//		List<ConfigPropertyLookup> inheritControlProperties = ProjectXmlUtil.loadInheritedControlProperties(controlElement, this.loader);
	//
	//		//add extend job if needed
	//		String extendClassName = controlElement.getAttribute("extend-class");
	//		if (extendClassName.length() > 0) {
	//			controlClassXmlHelper.addJob(new ControlExtendJob(extendClassName, control, inheritControlProperties));
	//		}
	//
	//		controlClassXmlHelper.registerExistingControlClass(control);
	//
	//		//must set ghost state first since ghost=!visible && !enabled
	//		//and we don't want to overwrite enabled when ghost is set
	//		control.getRenderer().setGhost(ghost);
	//		control.getRenderer().setEnabled(enabled);
	//
	//		return control;
	//	}
	//
	//	private SerializableValue getValue(@NotNull String requester, @NotNull PropertyType propertyType, @NotNull Element controlPropertyElement) {
	//		return ProjectXmlUtil.loadValue(requester, controlPropertyElement, propertyType, dataContext, this.loader);
	//	}
	//
	//	@NotNull
	//	public static String genericRecover(String value) {
	//		return String.format(Lang.ApplicationBundle().getString("XmlParse.generic_recover_message_f"), value);
	//	}
}
