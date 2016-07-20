package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.*;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
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
	
	private ArrayList<ControlPropertyInputDescriptor> propertyDescriptors = new ArrayList<>();
	private ControlPropertyEditor[] propertyEditors;
	
	private ControlPropertiesEditorPane() {
		ScrollPane scrollPane = new ScrollPane(accordion);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		getChildren().add(scrollPane);
	}
	
	private static class ControlPropertyInputDescriptor {
		private final ControlPropertyInput input;
		private boolean optional;
		
		public ControlPropertyInputDescriptor(ControlPropertyInput input) {
			this.input = input;
		}
		
		public ControlPropertyInput getInput() {
			return input;
		}
		
		public boolean isOptional() {
			return optional;
		}
		
		/** Return true if the {@link ControlProperty} returned from {@link #getControlProperty()} is optional (not in required list returned from {@link ControlClassSpecificationProvider#getRequiredProperties()}) */
		public void setIsOptional(boolean optional) {
			this.optional = optional;
		}
		
		/** Does same thing as {@link #getInput()}.getControlProperty() */
		public ControlProperty getControlProperty() {
			return input.getControlProperty();
		}
		
		/** Does same thing as {@link #getInput()}.hasValidData() */
		public boolean hasValidData() {
			return input.hasValidData();
		}
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
		List<ControlProperty> properties = new ArrayList<>(propertyDescriptors.size());
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			properties.add(descriptor.getControlProperty());
		}
		return properties;
	}
	
	public ControlPropertyEditor[] getEditors() {
		return propertyEditors;
	}
	
	/** Get all missing properties (control properties that are required by have no valid data entered). */
	public List<ControlProperty> getMissingProperties() {
		List<ControlProperty> properties = new ArrayList<>(propertyDescriptors.size());
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			if (!descriptor.getInput().hasValidData() && !descriptor.isOptional()) {
				System.out.printf("%-20s: validData:%b optional:%b valuesSet:%b\n", descriptor.getControlProperty().getName(), descriptor.hasValidData(), descriptor.isOptional(), descriptor.getControlProperty() != null);
				properties.add(descriptor.getControlProperty());
			}
		}
		return properties;
	}
	
	private void setupAccordion(ControlProperty[] requiredProperties, ControlProperty[] optionalProperties, ControlProperty[] eventProperties) {
		accordion.getPanes().add(getTitledPane(Lang.ControlPropertiesEditorPane.REQUIRED, requiredProperties, false));
		accordion.getPanes().add(getTitledPane(Lang.ControlPropertiesEditorPane.OPTIONAL, optionalProperties, true));
		accordion.getPanes().add(getTitledPane(Lang.ControlPropertiesEditorPane.EVENTS, eventProperties, true));
		
		accordion.setExpandedPane(accordion.getPanes().get(0));
		propertyEditors = new ControlPropertyEditor[propertyDescriptors.size()];
		for (int i = 0; i < propertyEditors.length; i++) {
			propertyEditors[i] = propertyDescriptors.get(i).getInput();
		}
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
		
		ControlPropertyInput propertyInput = getPropertyInputNode(c);
		propertyInput.disableEditing(c.getPropertyLookup() == ControlPropertyLookup.TYPE);
		
		if (propertyInput.displayFullWidth()) {
			HBox.setHgrow(stackPanePropertyInput, Priority.ALWAYS);
		}
		
		MenuItem miDefaultEditor = new MenuItem(Lang.ControlPropertiesEditorPane.USE_DEFAULT_EDITOR);
		MenuItem miResetToDefault = new MenuItem(Lang.ControlPropertiesEditorPane.RESET_TO_DEFAULT);
		MenuItem miMacro = new MenuItem(Lang.ControlPropertiesEditorPane.SET_TO_MACRO);
		MenuItem miOverride = new MenuItem(Lang.ControlPropertiesEditorPane.VALUE_OVERRIDE);//broken. Maybe fix it later. Don't delete this in case you change your mind
		MenuButton menuButton = new MenuButton(c.getName(), null, miDefaultEditor, new SeparatorMenuItem(), miResetToDefault, miMacro/*,miOverride*/);
		
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
		propertyDescriptors.add(new ControlPropertyInputDescriptor(propertyInput));
		propertyDescriptors.get(propertyDescriptors.size() - 1).setIsOptional(optional);
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
			return new ControlPropertyInputOption(control, controlProperty);
		}
		PropertyType propertyType = lookup.propertyType;
		switch (propertyType) {
			case INT:
				return new ControlPropertyInputFieldInteger(control, controlProperty);
			case FLOAT:
				return new ControlPropertyInputFieldDouble(control, controlProperty);
			case EXP:
				return new ControlPropertyExprInput(control, controlProperty);
			case BOOLEAN:
				return new ControlPropertyBooleanChoiceBox(control, controlProperty);
			case STRING:
				return new ControlPropertyInputFieldString(control, controlProperty);
			case ARRAY:
				return new ControlPropertyArrayInput(control, controlProperty, 2);
			case COLOR:
				return new ControlPropertyColorPicker(control, controlProperty);
			case SOUND:
				return new ControlPropertySoundInput(control, controlProperty);
			case FONT:
				return new ControlPropertyFontChoiceBox(control, controlProperty);
			case FILE_NAME:
				return new ControlPropertyInputFieldString(control, controlProperty);
			case IMAGE:
				return new ControlPropertyInputFieldString(control, controlProperty);
			case HEX_COLOR_STRING:
				return new ControlPropertyColorPicker(control, controlProperty); //todo have hex color editor (or maybe just take the AColor value instance and create a AHexColor instance from it)
			case TEXTURE:
				return new ControlPropertyInputFieldString(control, controlProperty);
			case EVENT:
				return new ControlPropertyInputFieldString(control, controlProperty);
			case SQF:
				return new ControlPropertyInputFieldString(control, controlProperty);
		}
		throw new IllegalStateException("Should have made a match");
	}
	
	private static Tooltip getTooltip(ControlPropertyLookup lookup) {
		String tooltip = "";
		for (int i = 0; i < lookup.about.length; i++) {
			if (lookup.propertyType == PropertyType.EVENT) {
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
			/** Using default editor */
			DEFAULT,
			/**
			 Uses a raw input field in which the user can enter anything they want. When this scenario happens, must be careful with casting after this is turned off.
			 It is recommended to just clear the user's entered data to prevent exceptions from occurring.
			 
			 @deprecated This is broken. Maybe fix it later.
			 */
			@Deprecated
			OVERRIDE,
			/** The control property's value is set to a macro */
			MACRO
		}
		
		/**
		 Updating the edit mode. When this is invoked, the proper new editor should be used.
		 <ul>
		 <li>{@link EditMode#DEFAULT} = default editor</li>
		 <li>{@link EditMode#OVERRIDE} = use an editor that allows for literally any input (use a {@link TextField} for input)</li>
		 <li>{@link EditMode#MACRO} = setting the ControlProperty's value equal to a {@link Macro}</li>
		 </ul>
		 */
		void setToMode(EditMode mode);
		
		Node getRootNode();
		
		/** Get the Class type that */
		Class<? extends SerializableValue> getMacroClass();
		
		/**
		 Return true if the {@link #getRootNode()}'s width should fill the parent's width.
		 False if the width should be whatever it is initially. By default, will return false.
		 */
		default boolean displayFullWidth() {
			return false;
		}
		
		/** DO NOT USE THIS FOR ARRAY INPUT */
		static InputField<StringChecker, String> modifyRawInput(InputField<StringChecker, String> rawInput, ControlClass control, UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup, ControlProperty controlProperty) {
			if (controlProperty.isPropertyType(PropertyType.ARRAY)) {
				throw new IllegalArgumentException("don't use this method for ARRAY property type");
			}
			rawInput.getValueObserver().addValueListener(new ValueListener<String>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
					controlProperty.setValue(new SVString(newValue));
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
	private static class ControlPropertyInputOption extends FlowPane implements ControlPropertyInput {
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private ToggleGroup toggleGroup;
		private List<RadioButton> radioButtons;
		private final InputField<StringChecker, String> rawInput = new InputField<>(new StringChecker());
		
		ControlPropertyInputOption(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			super(10, 5);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(rawInput, control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			toggleGroup = new ToggleGroup();
			RadioButton radioButton, toSelect = null;
			boolean validData = controlProperty.getValue() != null;
			if (lookup.options == null) {
				throw new IllegalStateException("options shouldn't be null");
			}
			radioButtons = new ArrayList<>(lookup.options.length);
			for (ControlPropertyOption option : lookup.options) {
				if (option == null) {
					throw new IllegalStateException("option shouldn't be null");
				}
				radioButton = new RadioButton(option.displayName);
				radioButton.setUserData(option.value);
				radioButton.setTooltip(new Tooltip(option.description));
				radioButton.setToggleGroup(toggleGroup);
				getChildren().add(radioButton);
				radioButtons.add(radioButton);
				if (validData && controlProperty.getValue().toString().equals(option.value)) {
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
						controlProperty.setValue((SerializableValue) null);
					} else {
						controlProperty.setValue(newValue.getUserData().toString());
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					if (newValue == null) {
						return;
					}
					for (Toggle toggle : toggleGroup.getToggles()) {
						if (toggle.getUserData().equals(controlProperty.getValue().toString())) {
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
	
	/**
	 Used for when the input is in a text field. The InputField class also allows for input verifying so that if something entered is wrong, the user will be notified.
	 Used for {@link SVDouble}, {@link SVInteger}, {@link SVString}, {@link Expression}
	 */
	@SuppressWarnings("unchecked")
	private static abstract class ControlPropertyInputField<C extends SerializableValue> extends InputFieldValueEditor<C> implements ControlPropertyInput {
		
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private final Class<C> macroTypeClass;
		
		ControlPropertyInputField(Class<C> macroTypeClass, @Nullable ControlClass control, @NotNull ControlProperty controlProperty, InputFieldDataChecker checker, @Nullable String promptText) {
			super(checker);
			this.macroTypeClass = macroTypeClass;
			
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			if (controlProperty.getValue() != null) {
				inputField.setValue((C) controlProperty.getValue());
			}
			inputField.getValueObserver().addValueListener(new ValueListener() {
				@Override
				public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
					controlProperty.setValue((C) newValue);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					if (controlProperty.getValue() != null) {
						inputField.setToButton(false);
						inputField.setText(controlProperty.getValue().toString());
					}
				}
			});
			placeTooltip(inputField, lookup);
			if (promptText != null) {
				inputField.setPromptText(promptText);
			}
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
			if (getControlProperty().getDefaultValue() == null) {
				inputField.setValue(null);
			} else {
				inputField.setValueFromText(getControlProperty().getDefaultValue().toString());
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
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}
		
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return this.macroTypeClass;
		}
		
		@Override
		public boolean displayFullWidth() {
			return true;
		}
	}
	
	private static class ControlPropertyInputFieldString extends ControlPropertyInputField<SVString> {
		ControlPropertyInputFieldString(ControlClass control, ControlProperty controlProperty) {
			super(SVString.class, control, controlProperty, new SVArmaStringChecker(), Lang.Popups.ControlPropertiesConfig.STRING);
		}
	}
	
	private static class ControlPropertyInputFieldDouble extends ControlPropertyInputField<SVDouble> {
		ControlPropertyInputFieldDouble(ControlClass control, ControlProperty controlProperty) {
			super(SVDouble.class, control, controlProperty, new SVDoubleChecker(), Lang.Popups.ControlPropertiesConfig.FLOAT);
		}
	}
	
	private static class ControlPropertyInputFieldInteger extends ControlPropertyInputField<SVInteger> {
		ControlPropertyInputFieldInteger(ControlClass control, ControlProperty controlProperty) {
			super(SVInteger.class, control, controlProperty, new SVIntegerChecker(), Lang.Popups.ControlPropertiesConfig.INT);
		}
	}
	
	private class ControlPropertyExprInput extends ControlPropertyInputField<Expression> {
		public ControlPropertyExprInput(ControlClass control, ControlProperty controlProperty) {
			super(Expression.class, control, controlProperty,
					new ExpressionChecker(ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment()),
					Lang.Popups.ControlPropertiesConfig.EXP);
		}
	}
	
	/**
	 Used for when control property requires color input.
	 Use this only when the ControlProperty's value is of type {@link AColor}
	 */
	private static class ControlPropertyColorPicker extends ColorValueEditor implements ControlPropertyInput {
		
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		
		ControlPropertyColorPicker(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			boolean validData = controlProperty.getValue() != null;
			if (validData) {
				AColor value = (AColor) controlProperty.getValue();
				colorPicker.setValue(value.toJavaFXColor());
			} else {
				colorPicker.setValue(null);
			}
			colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
				@Override
				public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
					if (newValue == null) {
						controlProperty.setValue((SerializableValue) null);
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
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					if (controlProperty.getValue() != null) { //maybe wasn't updated
						colorPicker.setValue(((AColor) controlProperty.getValue()).toJavaFXColor());
					} else {
						colorPicker.setValue(null);
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
				colorPicker.setValue(((AColor) controlProperty.getValue()).toJavaFXColor());
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
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}
		
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return AColor.class;
		}
	}
	
	/**
	 Used for boolean control properties
	 Use this editor for when the ControlProperty's value is of type {@link SVBoolean}
	 */
	private static class ControlPropertyBooleanChoiceBox extends BooleanValueEditor implements ControlPropertyInput {
		
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		
		ControlPropertyBooleanChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			
			boolean validData = controlProperty.getValue() != null;
			if (validData) {
				choiceBox.getSelectionModel().select(controlProperty.getBooleanValue() ? SVBoolean.TRUE : SVBoolean.FALSE);
			}
			choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SVBoolean>() {
				@Override
				public void changed(ObservableValue<? extends SVBoolean> observable, SVBoolean oldValue, SVBoolean newValue) {
					controlProperty.setValue(newValue);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					if (controlProperty.getValue() != null) {
						choiceBox.setValue((SVBoolean) newValue);
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
			if (getControlProperty().getDefaultValue() == null) { //no intellij this is not always false
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
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}
		
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVBoolean.class;
		}
	}
	
	/**
	 Used for control properties that require more than one input
	 This editor will use {@link SVStringArray} as the ControlProperty's value type
	 */
	@SuppressWarnings("unchecked")
	private static class ControlPropertyArrayInput extends ArrayValueEditor implements ControlPropertyInput {
		
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private SVStringArray svStringArray;
		
		ControlPropertyArrayInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty, int defaultNumFields) {
			super(defaultNumFields);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			svStringArray = (SVStringArray) controlProperty.getValue();
			InputField<ArmaStringChecker, String> inputField;
			boolean isDefined = controlProperty.getValue() != null; //no need to reiterate
			String[] values = controlProperty.getValue().getAsStringArray();
			for (int i = 0; i < editors.size(); i++) {
				inputField = editors.get(i);
				final int index = i;
				if (isDefined) {
					editors.get(i).setText(values[i]);
				}
				
				inputField.getValueObserver().addValueListener(new ValueListener() {
					@Override
					public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
						if (svStringArray == null) {
							//todo
						}
						svStringArray.setString(newValue.toString(), index);
						controlProperty.setValue(svStringArray);
						if (control != null) {
							control.getUpdateGroup().update(control);
						}
						controlPropertyUpdateGroup.update(controlProperty);
					}
				});
				if (control != null) {
					controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
						@Override
						public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, SerializableValue newValue) {
							String[] values = newValue.getAsStringArray();
							int i = 0;
							for (InputField field : editors) {
								field.setText(values[i++]);
							}
						}
					});
				}
				placeTooltip(inputField, lookup);
			}
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					if (controlProperty.getValue() != null) {
						String[] values = newValue.getAsStringArray();
						for (int i = 0; i < editors.size(); i++) {
							editors.get(i).setValueFromText(values[i]);
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
			SerializableValue defaultValue = controlProperty.getDefaultValue();
			String[] defaultValues;
			if (defaultValue == null) {
				defaultValues = new String[controlProperty.getPropertyType().propertyValuesSize];
			} else {
				defaultValues = defaultValue.getAsStringArray();
			}
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
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}
		
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVStringArray.class;
		}
	}
	
	/**
	 Used for control property font picking
	 Used for ControlProperty instances where it's value is {@link AFont}
	 */
	private static class ControlPropertyFontChoiceBox extends FontValueEditor implements ControlPropertyInput {
		
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		
		ControlPropertyFontChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			boolean validData = controlProperty.getValue() != null;
			AFont font = null;
			if (validData) {
				try {
					font = (AFont) controlProperty.getValue();
				} catch (IllegalArgumentException e) {
					e.printStackTrace(System.out);
				}
				comboBox.getSelectionModel().select(font);
			}
			comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AFont>() {
				@Override
				public void changed(ObservableValue<? extends AFont> observable, AFont oldValue, AFont newValue) {
					controlProperty.setValue(newValue);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					comboBox.setValue((AFont) controlProperty.getValue());
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
			if (controlProperty.getDefaultValue() == null) {
				comboBox.getSelectionModel().clearSelection();
			} else {
				comboBox.getSelectionModel().select((AFont) controlProperty.getDefaultValue());
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
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}
		
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return AFont.class;
		}
	}
	
	/** Use this editor for {@link ASound} ControlProperty values */
	private class ControlPropertySoundInput extends SoundValueEditor implements ControlPropertyInput {
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		
		private final int NAME = 0;
		private final int DB = 1;
		private final int PITCH = 2;
		
		private String name = "";
		private Double db = 0.0;
		private Double pitch = 0.0;
		
		private ASound aSound;
		
		public ControlPropertySoundInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			
			boolean validData = controlProperty.getValue() != null;
			if (validData) {
				aSound = (ASound) controlProperty.getValue();
			}
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlPropertyUpdateGroup, controlProperty);
			
			initInputField(inSoundName, NAME, aSound != null ? aSound.getSoundName() : null);
			initInputField(inDb, DB, aSound != null ? aSound.getDb() : null);
			initInputField(inPitch, PITCH, aSound != null ? aSound.getPitch() : null);
			
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					ASound sound = (ASound) newValue;
					if (sound == null) {
						inSoundName.clear();
						inDb.clear();
						inPitch.clear();
					} else {
						inSoundName.setText(sound.getSoundName());
						inDb.setText(sound.getDb() + "");
						inPitch.setText(sound.getPitch() + "");
					}
				}
			});
			
		}
		
		private void initInputField(InputField inputField, int typeIndex, Object defaultValue) {
			inputField.setText(defaultValue.toString());
			switch (typeIndex) {
				case NAME: {
					name = defaultValue.toString();
					break;
				}
				case DB: {
					db = (Double) defaultValue;
					break;
				}
				case PITCH: {
					pitch = (Double) defaultValue;
					break;
				}
			}
			
			inputField.getValueObserver().addValueListener(new ValueListener() {
				@Override
				public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
					switch (typeIndex) {
						case NAME: {
							name = newValue.toString();
							break;
						}
						case DB: {
							db = (Double) newValue;
							break;
						}
						case PITCH: {
							pitch = (Double) newValue;
							break;
						}
					}
					if (name != null && db != null && pitch != null) {
						if (aSound == null) {
							aSound = new ASound(name, db, pitch);
						} else {
							aSound.setSoundName(name);
							aSound.setDb(db);
							aSound.setPitch(pitch);
						}
					}
					controlProperty.setValue(aSound);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
					@Override
					public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, SerializableValue newValue) {
						setValue((ASound) newValue);
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
			setValue((ASound) controlProperty.getDefaultValue());
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
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}
		
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return ASound.class;
		}
	}
}
