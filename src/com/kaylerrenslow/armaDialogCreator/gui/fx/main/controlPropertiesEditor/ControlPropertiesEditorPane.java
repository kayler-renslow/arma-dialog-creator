package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.ASound;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 Houses an accordion that allows manuipulating multiple control properties. The data editor for each control property is specialized for the input (e.g. color property gets color picker).
 Created on 07/08/2016. */
public class ControlPropertiesEditorPane extends StackPane {
	private final Accordion accordion = new Accordion();
	private @Nullable ControlClass control;

	private ArrayList<ControlPropertyInput> propertyInputs = new ArrayList<>();
	private ControlPropertyEditor[] propertyEditors;

	private ControlPropertiesEditorPane() {
		ScrollPane scrollPane = new ScrollPane(accordion);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		getChildren().add(scrollPane);
	}


	/**
	 Creates the accordion with the given specification provider. In this case, <b>all</b> of the control properties are set to a null/empty value.

	 @param specProvider the specification provider
	 @param controlType control type
	 */
	public ControlPropertiesEditorPane(@NotNull ControlClassSpecificationProvider specProvider, @NotNull ControlType controlType) {
		this();
		ControlPropertyLookup[] requiredLookup = specProvider.getRequiredProperties();
		ControlPropertyLookup[] optionalLookup = specProvider.getOptionalProperties();
		ControlPropertyLookup[] eventLookup = specProvider.getEventProperties();
		ControlProperty[] requiredProperties = new ControlProperty[requiredLookup.length];
		ControlProperty[] optionalProperties = new ControlProperty[optionalLookup.length];
		ControlProperty[] eventProperties = new ControlProperty[eventLookup.length];
		for (int i = 0; i < requiredProperties.length; i++) {
			if (requiredLookup[i] == ControlPropertyLookup.TYPE) {
				requiredProperties[i] = requiredLookup[i].getIntProperty(controlType.typeId);
			} else {
				requiredProperties[i] = requiredLookup[i].getPropertyWithNoData();
			}
		}
		for (int i = 0; i < optionalProperties.length; i++) {
			optionalProperties[i] = optionalLookup[i].getPropertyWithNoData();
		}
		for (int i = 0; i < eventProperties.length; i++) {
			eventProperties[i] = eventLookup[i].getPropertyWithNoData();
		}

		setupAccordion(requiredProperties, optionalProperties, eventProperties);
	}

	/**
	 Creates the accordion according to the control class's specification. For the inputted values in the accordion, they are fetched from {@link ControlClass#getRequiredProperties()}, {@link ControlClass#getOptionalProperties()}, and {@link ControlClass#getEventProperties()}<br>
	 It is important to note that when the control properties inside the control are edited, they will be updated in the control class as well. There is no copying of the controlClass's control properties and everything is passed by reference.

	 @param control control class that has the properties to edit
	 */
	public ControlPropertiesEditorPane(@NotNull ControlClass control) {
		this();
		this.control = control;
		setupAccordion(control.getRequiredProperties(), control.getOptionalProperties(), control.getEventProperties());
	}

	/** Return true if all values entered for all properties are good/valid, false if at least one is bad. */
	public boolean allValuesAreGood() {
		return getMissingProperties().size() == 0;
	}


	/** Return all properties that are being edited */
	public List<ControlProperty> getAllProperties() {
		List<ControlProperty> properties = new ArrayList<>(propertyInputs.size());
		for (ControlPropertyInput input : propertyInputs) {
			properties.add(input.getControlProperty());
		}
		return properties;
	}

	public ControlPropertyEditor[] getEditors() {
		return propertyEditors;
	}

	/** Get all missing properties (control properties that are required by have no valid data entered). */
	public List<ControlProperty> getMissingProperties() {
		List<ControlProperty> properties = new ArrayList<>(propertyInputs.size());
		for (ControlPropertyInput input : propertyInputs) {
			if (!input.hasValidData() && !input.isOptional()) {
				System.out.printf("%-20s: validData:%b optional:%b valuesSet:%b\n", input.getControlProperty().getName(), input.hasValidData(), input.isOptional(), input.getControlProperty().valuesAreSet());
				properties.add(input.getControlProperty());
			}
		}
		return properties;
	}

	private void setupAccordion(ControlProperty[] requiredProperties, ControlProperty[] optionalProperties, ControlProperty[] eventProperties) {
		accordion.getPanes().add(getTitledPane("Required", requiredProperties, false));
		accordion.getPanes().add(getTitledPane("Optional", optionalProperties, true));
		accordion.getPanes().add(getTitledPane("Events", eventProperties, true));

		accordion.setExpandedPane(accordion.getPanes().get(0));
		propertyEditors = propertyInputs.toArray(new ControlPropertyEditor[propertyInputs.size()]);
	}

	/** Get a titled pane for the accordion that holds all control properties */
	private TitledPane getTitledPane(String title, ControlProperty[] properties, boolean optional) {
		VBox vb = new VBox(10);
		TitledPane tp = new TitledPane(title, vb);
		tp.setAnimated(false);
		if (properties.length == 0) {
			vb.getChildren().add(new Label(Lang.Popups.ControlPropertiesConfig.NO_PROPERTIES_AVAILABLE));
		} else {
			for (ControlProperty controlProperty : properties) {
				vb.getChildren().add(getControlPropertyEntry(controlProperty, optional));
			}
		}
		return tp;
	}

	/** Get the pane that shows the name of the property as well as the controls to input data */
	private Node getControlPropertyEntry(ControlProperty c, boolean optional) {
		HBox pane = new HBox(5);
		pane.setAlignment(Pos.TOP_LEFT);
		StackPane stackPanePropertyInput = new StackPane();

		MenuItem miDefaultEditor = new MenuItem(Lang.ControlPropertiesEditorPane.USE_DEFAULT_EDITOR);
		MenuItem miResetToDefault = new MenuItem(Lang.ControlPropertiesEditorPane.RESET_TO_DEFAULT);
		MenuItem miMacro = new MenuItem(Lang.ControlPropertiesEditorPane.SET_TO_MACRO);
		MenuItem miOverride = new MenuItem(Lang.ControlPropertiesEditorPane.VALUE_OVERRIDE);//broken. Maybe fix it later. Don't delete this in case you change your mind
		MenuButton menuButton = new MenuButton(c.getName(), null, miDefaultEditor, new SeparatorMenuItem(), miResetToDefault, miMacro/*,miOverride*/);

		ControlPropertyInput propertyInput = getPropertyInputNode(c);
		propertyInput.disableEditing(c.getPropertyLookup() == ControlPropertyLookup.TYPE);
		switch (c.getPropertyLookup()) {
			case TYPE: {
				for (MenuItem item : menuButton.getItems()) {
					item.setDisable(true);
				}
				break;
			}
			//intentional fallthrough for all below properties in case statements
			case STYLE:
			case X:
			case Y:
			case W:
			case H: {
				miOverride.setDisable(true);//NEVER allow custom input
				break;
			}
		}
		propertyInputs.add(propertyInput);
		propertyInput.setIsOptional(optional);
		stackPanePropertyInput.getChildren().add(propertyInput.getRootNode());
		pane.getChildren().addAll(menuButton, new Label("="), stackPanePropertyInput);

		miResetToDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				propertyInput.resetToDefaultValue();
			}
		});
		miDefaultEditor.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updatePropertyInputMode(stackPanePropertyInput, propertyInput, ControlPropertyInput.EditMode.DEFAULT);
			}
		});
		miMacro.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updatePropertyInputMode(stackPanePropertyInput, propertyInput, ControlPropertyInput.EditMode.MACRO);

			}
		});
		miOverride.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updatePropertyInputMode(stackPanePropertyInput, propertyInput, ControlPropertyInput.EditMode.OVERRIDE);
			}
		});

		if (c.isDataOverride()) {
			updatePropertyInputMode(stackPanePropertyInput, propertyInput, ControlPropertyInput.EditMode.OVERRIDE);
		} else if (c.getMacro() != null) {
			updatePropertyInputMode(stackPanePropertyInput, propertyInput, ControlPropertyInput.EditMode.MACRO);
		}

		return pane;
	}

	@SuppressWarnings("unchecked")
	private void updatePropertyInputMode(StackPane stackPanePropertyInput, ControlPropertyInput propertyInput, ControlPropertyInput.EditMode mode) {
		stackPanePropertyInput.getChildren().clear();
		if (mode == ControlPropertyInput.EditMode.MACRO) {
			MacroGetterButton<? extends SerializableValue> macroGetterButton = new MacroGetterButton(propertyInput.getMacroClass(), propertyInput.getControlProperty().getMacro());
			stackPanePropertyInput.getChildren().add(macroGetterButton);
			macroGetterButton.getChosenMacroValueObserver().addValueListener(new ValueListener() {
				@Override
				public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
					Macro m = (Macro) newValue;
					propertyInput.getControlProperty().setValueToMacro(m);
				}
			});
		} else {
			stackPanePropertyInput.getChildren().add(propertyInput.getRootNode());
			propertyInput.setToMode(mode);
			propertyInput.getControlProperty().setDataOverride(mode == ControlPropertyInput.EditMode.OVERRIDE);
		}
	}

	/** Get node that holds the controls to input data. */
	private ControlPropertyInput getPropertyInputNode(ControlProperty controlProperty) {
		ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
		if (lookup.options != null && lookup.options.length > 0) {
			return new ControlPropertyOption(control, controlProperty);
		}
		ControlProperty.PropertyType propertyType = lookup.propertyType;
		switch (propertyType) {
			case INT:
				return new ControlPropertyInputFieldInteger(control, controlProperty, Lang.Popups.ControlPropertiesConfig.INT);
			case FLOAT:
				return new ControlPropertyInputFieldDouble(control, controlProperty, Lang.Popups.ControlPropertiesConfig.FLOAT);
			case BOOLEAN:
				return new ControlPropertyBooleanChoiceBox(control, controlProperty);
			case STRING:
				return new ControlPropertyInputFieldString(control, controlProperty, Lang.Popups.ControlPropertiesConfig.STRING);
			case ARRAY:
				return new ControlPropertyArrayInput(control, controlProperty, 2);
			case COLOR:
				return new ControlPropertyColorPicker(control, controlProperty);
			case SOUND:
				return new ControlPropertySoundInput(control, controlProperty);
			case FONT:
				return new ControlPropertyFontChoiceBox(control, controlProperty);
			case FILE_NAME:
				return new ControlPropertyInputFieldString(control, controlProperty, Lang.Popups.ControlPropertiesConfig.FILE_NAME);
			case IMAGE:
				return new ControlPropertyInputFieldString(control, controlProperty, Lang.Popups.ControlPropertiesConfig.IMAGE_PATH);
			case HEX_COLOR_STRING:
				return new ControlPropertyColorPicker(control, controlProperty); //todo have hex color editor
			case TEXTURE:
				return new ControlPropertyInputFieldString(control, controlProperty, Lang.Popups.ControlPropertiesConfig.TEXTURE);
			case EVENT:
				return new ControlPropertyInputFieldString(control, controlProperty, Lang.Popups.ControlPropertiesConfig.SQF_CODE);
			case SQF:
				return new ControlPropertyInputFieldString(control, controlProperty, Lang.Popups.ControlPropertiesConfig.SQF_CODE);
		}
		throw new IllegalStateException("Should have made a match");
	}

	private static Tooltip getTooltip(ControlPropertyLookup lookup) {
		String tooltip = "";
		for (int i = 0; i < lookup.about.length; i++) {
			if (lookup.propertyType == ControlProperty.PropertyType.EVENT) {
				if (i == 2) { //priority
					tooltip += "Priority: " + lookup.about[i];
				} else if (i == 3) { //scope
					continue;
				}
			} else {
				tooltip += lookup.about[i];
			}
			tooltip += "\n";
		}
		Tooltip tp = new Tooltip(tooltip);
		tp.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20));
		FXUtil.hackTooltipStartTiming(tp, 0);
		return tp;
	}

	private static void placeTooltip(Control c, ControlPropertyLookup lookup) {
		c.setTooltip(getTooltip(lookup));
	}

	private static void placeTooltip(InputField inputField, ControlPropertyLookup lookup) {
		inputField.setTooltip(getTooltip(lookup));
	}


	private interface ControlPropertyInput extends ControlPropertyEditor {

		enum EditMode {
			DEFAULT,
			/** This is broken. Maybe fix it later. */
			@Deprecated
			OVERRIDE,
			MACRO
		}

		void setIsOptional(boolean optional);

		void setToMode(EditMode mode);

		Node getRootNode();

		Class<? extends SerializableValue> getMacroClass();

		/** DO NOT USE THIS FOR ARRAY INPUT */
		static InputField<ArmaStringChecker, String> modifyRawInput(InputField<ArmaStringChecker, String> rawInput, ControlClass control, UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup, ControlProperty controlProperty) {
			if (controlProperty.getType() == ControlProperty.PropertyType.ARRAY) {
				throw new IllegalArgumentException("don't use this method for ARRAY property type");
			}
			rawInput.getValueObserver().addValueListener(new ValueListener<String>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
					controlProperty.setFirstValue(newValue);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			return rawInput;
		}
	}

	/** Used for when a set amount of options are available (uses radio button group for option selecting) */
	private static class ControlPropertyOption extends FlowPane implements ControlPropertyInput {
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private ToggleGroup toggleGroup;
		private List<RadioButton> radioButtons;
		private final InputField<ArmaStringChecker, String> rawInput = new InputField<>(new ArmaStringChecker());
		private boolean isOptional;

		ControlPropertyOption(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			super(10, 5);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(rawInput, control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			toggleGroup = new ToggleGroup();
			RadioButton radioButton, toSelect = null;
			boolean validData = controlProperty.valuesAreSet();
			if (lookup.options == null) {
				throw new IllegalStateException("options shouldn't be null");
			}
			radioButtons = new ArrayList<>(lookup.options.length);
			for (com.kaylerrenslow.armaDialogCreator.control.ControlPropertyOption option : lookup.options) {
				if (option == null) {
					throw new IllegalStateException("option shouldn't be null");
				}
				radioButton = new RadioButton(option.displayName);
				radioButton.setUserData(option.value);
				radioButton.setTooltip(new Tooltip(option.description));
				radioButton.setToggleGroup(toggleGroup);
				getChildren().add(radioButton);
				radioButtons.add(radioButton);
				if (validData && controlProperty.getFirstValue().equals(option.value)) {
					toSelect = radioButton;
				}
			}

			if (toSelect != null) {
				toggleGroup.selectToggle(toSelect);
			}
			toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					if (newValue == null) {
						controlProperty.setFirstValue(null);
					} else {
						controlProperty.setFirstValue(newValue.getUserData().toString());
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String[]> observer, @Nullable String[] oldValue, @Nullable String[] newValue) {
					if (!controlProperty.valuesAreSet()) {
						return;
					}
					for (Toggle toggle : toggleGroup.getToggles()) {
						if (toggle.getUserData().equals(controlProperty.getFirstValue())) {
							toggleGroup.selectToggle(toggle);
							return;
						}
					}
				}
			});
		}

		@Override
		public boolean hasValidData() {
			return toggleGroup.getSelectedToggle() != null;
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefaultValue() {
			toggleGroup.selectToggle(null);
		}

		@Override
		public void disableEditing(boolean disable) {
			setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		@Override
		public boolean isOptional() {
			return isOptional;
		}

		@Override
		public void setIsOptional(boolean optional) {
			this.isOptional = optional;
		}

		@Override
		public void setToMode(EditMode mode) {
			getChildren().clear();
			if (mode == EditMode.OVERRIDE) {
				getChildren().add(rawInput);
			} else if (mode == EditMode.DEFAULT) {
				getChildren().addAll(radioButtons);
			}
		}

		@Override
		public Node getRootNode() {
			return this;
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVString.class;
		}
	}

	/** Used for when the input is in a text field. The InputField class also allows for input verifying so that if something entered is wrong, the user will be notified. */
	@SuppressWarnings("unchecked")
	private static abstract class ControlPropertyInputField<C extends SerializableValue> extends InputFieldValueEditor<C> implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private final Class<? extends SerializableValue> macroTypeClass;
		private boolean isOptional;

		ControlPropertyInputField(Class<C> macroTypeClass, @Nullable ControlClass control, @NotNull ControlProperty controlProperty, InputFieldDataChecker checker, @Nullable String promptText) {
			super(checker);
			this.macroTypeClass = macroTypeClass;

			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			if (controlProperty.valuesAreSet()) {
				inputField.setValueFromText(controlProperty.getFirstValue());
			}
			inputField.getValueObserver().addValueListener(new ValueListener() {
				@Override
				public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
					controlProperty.setFirstValue(newValue);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String[]> observer, @Nullable String[] oldValue, @Nullable String[] newValue) {
					if (controlProperty.valuesAreSet()) {
						inputField.setText(controlProperty.getFirstValue());
					}
				}
			});
			placeTooltip(inputField, lookup);
			if (promptText != null) {
				inputField.setPromptText(promptText);
			}
			HBox.setHgrow(getRootNode(), Priority.ALWAYS);
		}

		@Override
		public boolean hasValidData() {
			return inputField.hasValidData();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefaultValue() {
			if (getControlProperty().getFirstDefaultValue() == null) {
				inputField.setValue(null);
			} else {
				inputField.setText(getControlProperty().getFirstValue());
			}
		}

		@Override
		public void disableEditing(boolean disable) {
			inputField.setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		@Override
		public boolean isOptional() {
			return isOptional;
		}

		@Override
		public void setIsOptional(boolean optional) {
			this.isOptional = optional;
		}

		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return this.macroTypeClass;
		}
	}

	private static class ControlPropertyInputFieldString extends ControlPropertyInputField<SVString> {
		ControlPropertyInputFieldString(ControlClass control, ControlProperty controlProperty, String promptText) {
			super(SVString.class, control, controlProperty, new SVArmaStringChecker(), promptText);
		}
	}

	private static class ControlPropertyInputFieldDouble extends ControlPropertyInputField<SVDouble> {
		ControlPropertyInputFieldDouble(ControlClass control, ControlProperty controlProperty, String promptText) {
			super(SVDouble.class, control, controlProperty, new SVDoubleChecker(), promptText);
		}
	}

	private static class ControlPropertyInputFieldInteger extends ControlPropertyInputField<SVInteger> {
		ControlPropertyInputFieldInteger(ControlClass control, ControlProperty controlProperty, String promptText) {
			super(SVInteger.class, control, controlProperty, new SVIntegerChecker(), promptText);
		}
	}

	/** Used for when control property requires color input */
	private static class ControlPropertyColorPicker extends ColorValueEditor implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private boolean isOptional;

		ControlPropertyColorPicker(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			boolean validData = controlProperty.valuesAreSet();
			if (validData) {
				AColor value = new AColor(controlProperty.getValues());
				colorPicker.setValue(value.toJavaFXColor());
			} else {
				colorPicker.setValue(null);
			}
			colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
				@Override
				public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
					if (newValue == null) {
						controlProperty.setValues(null, null, null, null);
					} else {
						AColor newColor = new AColor(newValue);
						controlProperty.setValue(newColor);
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String[]> observer, @Nullable String[] oldValue, @Nullable String[] newValue) {
					if (controlProperty.valuesAreSet()) { //maybe wasn't updated
						colorPicker.setValue(AColor.toJavaFXColor(controlProperty.getValues()));
					}
				}
			});

			placeTooltip(colorPicker, lookup);
		}

		@Override
		public boolean hasValidData() {
			return colorPicker.getValue() != null;
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefaultValue() {
			try {
				colorPicker.setValue(AColor.toJavaFXColor(getControlProperty().getDefaultValues()));
			} catch (NullPointerException e) {
				colorPicker.setValue(null);
			}
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		@Override
		public boolean isOptional() {
			return isOptional;
		}

		@Override
		public void setIsOptional(boolean optional) {
			this.isOptional = optional;
		}

		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return AColor.class;
		}
	}

	/** Used for boolean control properties */
	private static class ControlPropertyBooleanChoiceBox extends BooleanValueEditor implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private boolean isOptional;

		ControlPropertyBooleanChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();

			boolean validData = controlProperty.valuesAreSet();
			if (validData) {
				choiceBox.getSelectionModel().select(controlProperty.getBooleanValue() ? SVBoolean.TRUE : SVBoolean.FALSE);
			}
			choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SVBoolean>() {
				@Override
				public void changed(ObservableValue<? extends SVBoolean> observable, SVBoolean oldValue, SVBoolean newValue) {
					if (newValue == null) {
						controlProperty.setFirstValue(null);
					} else {
						controlProperty.setValue(newValue.isTrue());
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
				@Override
				public void valueUpdated(ValueObserver<String[]> observer, @Nullable String[] oldValue, @Nullable String[] newValue) {
					if (controlProperty.valuesAreSet()) {
						choiceBox.setValue(controlProperty.getBooleanValue() ? SVBoolean.TRUE : SVBoolean.FALSE);
					}
				}
			});

			placeTooltip(choiceBox, lookup);
		}

		@Override
		public boolean hasValidData() {
			return !choiceBox.getSelectionModel().isEmpty();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefaultValue() {
			if (getControlProperty().getDefaultValues()[0] == null) { //no intellij this is not always false
				choiceBox.getSelectionModel().clearSelection();
				return;
			}
			choiceBox.getSelectionModel().select(getControlProperty().getBooleanValue() ? SVBoolean.TRUE : SVBoolean.FALSE);
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}


		@Override
		public boolean isOptional() {
			return isOptional;
		}

		@Override
		public void setIsOptional(boolean optional) {
			this.isOptional = optional;
		}

		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVBoolean.class;
		}
	}

	/** Used for control properties that require more than one input */
	@SuppressWarnings("unchecked")
	private static class ControlPropertyArrayInput extends ArrayValueEditor implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private boolean isOptional;

		ControlPropertyArrayInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty, int defaultNumFields) {
			super(defaultNumFields);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			InputField<ArmaStringChecker, String> inputField;
			boolean isDefined = controlProperty.valuesAreSet(); //no need to reiterate
			for (int i = 0; i < editors.size(); i++) {
				inputField = editors.get(i);
				final int index = i;
				if (isDefined) {
					editors.get(i).setText(controlProperty.getFirstValue());
				}

				inputField.getValueObserver().addValueListener(new ValueListener() {
					@Override
					public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
						controlProperty.setValue(newValue.toString(), index);
						if (control != null) {
							control.getUpdateGroup().update(control);
						}
						controlPropertyUpdateGroup.update(controlProperty);
					}
				});
				if (control != null) {
					controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
						@Override
						public void valueUpdated(@NotNull ValueObserver<String[]> observer, @Nullable String[] oldValue, String[] newValue) {
							int i = 0;
							for (InputField field : editors) {
								field.setText(newValue[i++]);
							}
						}
					});
				}
				placeTooltip(inputField, lookup);
			}
			controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String[]> observer, @Nullable String[] oldValue, @Nullable String[] newValue) {
					if (controlProperty.valuesAreSet()) {
						for (int i = 0; i < editors.size(); i++) {
							editors.get(i).setValueFromText(controlProperty.getValues()[i]);
						}
					}
				}
			});
		}

		@Override
		public boolean hasValidData() {
			for (InputField inputField : editors) {
				if (!inputField.hasValidData()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefaultValue() {
			String[] defaultValues = getControlProperty().getDefaultValues();
			for (int i = 0; i < editors.size(); i++) {
				if (defaultValues[i] == null) {
					editors.get(i).clear();
				} else {
					editors.get(i).setText(defaultValues[i]);
				}
			}
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		@Override
		public boolean isOptional() {
			return isOptional;
		}

		@Override
		public void setIsOptional(boolean optional) {
			this.isOptional = optional;
		}

		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVStringArray.class;
		}
	}

	/** Used for control property font picking */
	private static class ControlPropertyFontChoiceBox extends FontValueEditor implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private boolean isOptional;

		ControlPropertyFontChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			boolean validData = controlProperty.valuesAreSet();
			AFont font = null;
			if (validData) {
				try {
					font = AFont.valueOf(controlProperty.getFirstValue());
				} catch (IllegalArgumentException e) {
					e.printStackTrace(System.out);
				}
				comboBox.getSelectionModel().select(font);
			}
			comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AFont>() {
				@Override
				public void changed(ObservableValue<? extends AFont> observable, AFont oldValue, AFont newValue) {
					if (newValue == null) {
						controlProperty.setFirstValue(null);
					} else {
						controlProperty.setValue(newValue.name());
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String[]> observer, @Nullable String[] oldValue, @Nullable String[] newValue) {
					comboBox.setValue(AFont.valueOf(controlProperty.getFirstValue()));
				}
			});
			placeTooltip(comboBox, lookup);
		}

		@Override
		public boolean hasValidData() {
			return !comboBox.getSelectionModel().isEmpty();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefaultValue() {
			if (controlProperty.getFirstDefaultValue() == null) {
				comboBox.getSelectionModel().clearSelection();
			} else {
				comboBox.getSelectionModel().select(AFont.valueOf(controlProperty.getFirstDefaultValue()));
			}
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		@Override
		public boolean isOptional() {
			return isOptional;
		}

		@Override
		public void setIsOptional(boolean optional) {
			this.isOptional = optional;
		}

		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return AFont.class;
		}
	}

	private class ControlPropertySoundInput extends SoundValueEditor implements ControlPropertyInput {
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private boolean isOptional;

		private final int NAME = 0;
		private final int DB = 1;
		private final int PITCH = 2;

		public ControlPropertySoundInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();

			boolean validData = controlProperty.valuesAreSet();
			ASound sound = null;
			if (validData) {
				sound = new ASound(controlProperty.getValues());
			}
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);

			initInputField(inSoundName, NAME, sound != null ? sound.getSoundName() : null);
			initInputField(inDb, DB, sound != null ? sound.getDb() + "" : null);
			initInputField(inPitch, PITCH, sound != null ? sound.getPitch() + "" : null);

			controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String[]> observer, @Nullable String[] oldValue, @Nullable String[] newValue) {
					//						comboBox.setString(AFont.valueOf(controlProperty.getFirstValue())); TODO
				}
			});

		}

		private void initInputField(InputField inputField, int typeIndex, String defaultValue) {
			inputField.setText(defaultValue);

			inputField.getValueObserver().addValueListener(new ValueListener() {
				@Override
				public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
					controlProperty.setValue(newValue != null ? newValue.toString() : null, typeIndex);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
					@Override
					public void valueUpdated(@NotNull ValueObserver<String[]> observer, @Nullable String[] oldValue, String[] newValue) {
						for (String s : newValue) {
							if (s == null) {
								setValue(null);
								return;
							}
						}
						setValue(new ASound(newValue));
					}
				});
			}
			placeTooltip(inputField, controlProperty.getPropertyLookup());
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public boolean hasValidData() {
			return getValue() != null;
		}

		@Override
		public void resetToDefaultValue() {
			if (controlProperty.getDefaultValues()[0] == null) {
				setValue(null);
			}
			setValue(new ASound(controlProperty.getDefaultValues()));
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public boolean isOptional() {
			return isOptional;
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		@Override
		public void setIsOptional(boolean optional) {
			this.isOptional = optional;
		}

		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return ASound.class;
		}
	}
}
