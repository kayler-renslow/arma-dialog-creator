package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.control.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.Option;
import com.kaylerrenslow.armaDialogCreator.gui.fx.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.*;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
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
		pane.getChildren().addAll(menuButton, new Label("="), (Node) propertyInput);

		miResetToDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				propertyInput.resetToDefaultValue();
			}
		});
		miDefaultEditor.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				propertyInput.setToMode(ControlPropertyInput.EditMode.DEFAULT);
				c.setDataOverride(false);
			}
		});
		miMacro.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				propertyInput.setToMode(ControlPropertyInput.EditMode.MACRO);
				c.setDataOverride(false);
			}
		});
		miOverride.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				propertyInput.setToMode(ControlPropertyInput.EditMode.OVVERIDE);
				c.setDataOverride(true);
			}
		});

		return pane;
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
				return new ControlPropertyArrayInput(control, controlProperty, new ArmaStringFieldDataChecker(), new ArmaStringFieldDataChecker());
			case COLOR:
				return new ControlPropertyColorPicker(control, controlProperty);
			case SOUND:
				return new ControlPropertyArrayInput(control, controlProperty, new ArmaStringFieldDataChecker(), new DoubleFieldDataChecker(), new DoubleFieldDataChecker());
			case FONT:
				return new ControlPropertyFontChoiceBox(control, controlProperty);
			case FILE_NAME:
				return new ControlPropertyInputFieldString(control, controlProperty, Lang.Popups.ControlPropertiesConfig.FILE_NAME);
			case IMAGE:
				return new ControlPropertyInputFieldString(control, controlProperty, Lang.Popups.ControlPropertiesConfig.IMAGE_PATH);
			case HEX_COLOR_STRING:
				return new ControlPropertyColorPicker(control, controlProperty);
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
			@Deprecated
			/**This is broken. Maybe fix it later.*/
					OVVERIDE,
			MACRO
		}

		void setIsOptional(boolean optional);

		void setToMode(EditMode mode);

		/** DO NOT USE THIS FOR ARRAY INPUT */
		static InputField<ArmaStringFieldDataChecker, String> createRawInput(ControlClass control, UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup, ControlProperty controlProperty) {
			InputField<ArmaStringFieldDataChecker, String> rawInput = new InputField<>(new ArmaStringFieldDataChecker());
			rawInput.getValueObserver().addValueListener(new ValueListener<String>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
					if (newValue == null) {
						controlProperty.setFirstValue(null);
					} else {
						controlProperty.getValuesObserver().updateValue(new String[]{newValue});
					}
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
		private final InputField<ArmaStringFieldDataChecker, String> rawInput;
		private MacroGetterButton<String> macro;
		private EditMode mode = EditMode.DEFAULT;

		ControlPropertyOption(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			super(10, 5);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			rawInput = ControlPropertyInput.createRawInput(control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			toggleGroup = new ToggleGroup();
			RadioButton radioButton, toSelect = null;
			boolean validData = controlProperty.valuesAreSet();
			if (lookup.options == null) {
				throw new IllegalStateException("options shouldn't be null");
			}
			radioButtons = new ArrayList<>(lookup.options.length);
			for (Option option : lookup.options) {
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
			macro = new MacroGetterButton<>(String.class, (toSelect != null ? toSelect.getUserData().toString() : null));
			macro.getValueObserver().addValueListener(new ValueListener<String>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
					if (mode == EditMode.MACRO) {
						controlProperty.setFirstValue(newValue);
						if (control != null) {
							control.getUpdateGroup().update(control);
						}
						controlPropertyUpdateGroup.update(controlProperty);
					}
				}
			});
			if (toSelect != null) {
				toggleGroup.selectToggle(toSelect);
			}
			toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					if (newValue == null) {
						controlProperty.setFirstValue(null);
						macro.getValueObserver().updateValue(null);
					} else {
						controlProperty.getValuesObserver().updateValue(new String[]{newValue.getUserData().toString()});
						macro.getValueObserver().updateValue(newValue.getUserData().toString());
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
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

		private boolean isOptional;

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
			this.mode = mode;
			getChildren().clear();
			if (mode == EditMode.OVVERIDE) {
				getChildren().add(rawInput);
			} else if (mode == EditMode.DEFAULT) {
				getChildren().addAll(radioButtons);
			} else if (mode == EditMode.MACRO) {
				getChildren().add(macro);
			}
		}
	}

	@SuppressWarnings("unchecked")
	/**Used for when the input is in a text field. The InputField class also allows for input verifying so that if something entered is wrong, the user will be notified.*/
	private static abstract class ControlPropertyInputField<T> extends StackPane implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private final InputField inputField;
		private final InputField<ArmaStringFieldDataChecker, String> rawInput;
		protected MacroGetterButton<T> macro;
		protected EditMode mode = EditMode.DEFAULT;

		ControlPropertyInputField(Class<T> clazz, @Nullable ControlClass control, @NotNull ControlProperty controlProperty, InputFieldDataChecker checker, @Nullable String promptText) {
			inputField = new InputField(checker);
			getChildren().add(inputField);

			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			rawInput = ControlPropertyInput.createRawInput(control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			if (controlProperty.valuesAreSet()) {
				inputField.setValueFromText(controlProperty.getFirstValue());
			}
			macro = new MacroGetterButton<>(clazz, (T) controlProperty.getFirstValue());
			macro.getValueObserver().addValueListener(new ValueListener<T>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<T> observer, T oldValue, T newValue) {
					if (mode == EditMode.MACRO) {
						controlProperty.setFirstValue(newValue);
						if (control != null) {
							control.getUpdateGroup().update(control);
						}
						controlPropertyUpdateGroup.update(controlProperty);
					}
				}
			});
			inputField.getValueObserver().addValueListener(new ValueListener() {
				@Override
				public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
					if (newValue == null) {
						controlProperty.getValuesObserver().updateValue(new String[]{null});
					} else {
						controlProperty.getValuesObserver().updateValue(new String[]{newValue.toString()});
					}
					macro.getValueObserver().updateValue((T) newValue);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
						if (controlProperty.valuesAreSet()) {
							inputField.setText(controlProperty.getFirstValue());
						}
					}
				});
			}
			placeTooltip(inputField, lookup);
			if (promptText != null) {
				inputField.setPromptText(promptText);
			}
			HBox.setHgrow(this, Priority.ALWAYS);
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
			setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		private boolean isOptional;

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
			this.mode = mode;
			getChildren().clear();
			if (mode == EditMode.OVVERIDE) {
				getChildren().add(rawInput);
			} else if (mode == EditMode.DEFAULT) {
				getChildren().add(inputField);
			} else if (mode == EditMode.MACRO) {
				getChildren().add(macro);
			}
		}
	}

	private static class ControlPropertyInputFieldString extends ControlPropertyInputField<String> {
		ControlPropertyInputFieldString(ControlClass control, ControlProperty controlProperty, String promptText) {
			super(String.class, control, controlProperty, new ArmaStringFieldDataChecker(), promptText);
		}
	}

	private static class ControlPropertyInputFieldDouble extends ControlPropertyInputField<Double> {
		ControlPropertyInputFieldDouble(ControlClass control, ControlProperty controlProperty, String promptText) {
			super(Double.class, control, controlProperty, new DoubleFieldDataChecker(), promptText);
		}
	}

	private static class ControlPropertyInputFieldInteger extends ControlPropertyInputField<Integer> {
		ControlPropertyInputFieldInteger(ControlClass control, ControlProperty controlProperty, String promptText) {
			super(Integer.class, control, controlProperty, new IntegerFieldDataChecker(), promptText);
		}
	}

	/** Used for when control property requires color input */
	private static class ControlPropertyColorPicker extends StackPane implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private final ColorPicker colorPicker = new ColorPicker();
		private final InputField<ArmaStringFieldDataChecker, String> rawInput;
		private final MacroGetterButton<AColor> macro;
		private EditMode mode = EditMode.DEFAULT;

		ControlPropertyColorPicker(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			getChildren().add(colorPicker);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			rawInput = ControlPropertyInput.createRawInput(control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			boolean validData = controlProperty.valuesAreSet();
			if (validData) {
				AColor value = new AColor(controlProperty.getValues());
				colorPicker.setValue(value.toJavaFXColor());
				macro = new MacroGetterButton<>(AColor.class, value);
			} else {
				colorPicker.setValue(null);
				macro = new MacroGetterButton<>(AColor.class, null);
			}
			macro.getValueObserver().addValueListener(new ValueListener<AColor>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
					if (mode == EditMode.MACRO) {
						controlProperty.setValue(newValue);
						if (control != null) {
							control.getUpdateGroup().update(control);
						}
						controlPropertyUpdateGroup.update(controlProperty);
					}
				}
			});
			colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
				@Override
				public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
					if (newValue == null) {
						controlProperty.setValues(null, null, null, null);
						macro.getValueObserver().updateValue(null);
					} else {
						AColor newColor = new AColor(newValue);
						controlProperty.setValue(newColor);
						macro.getValueObserver().updateValue(newColor);
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
						if (controlProperty.valuesAreSet()) { //maybe wasn't updated
							colorPicker.setValue(AColor.toJavaFXColor(controlProperty.getValues()));
						}
					}
				});
			}
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
			setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		private boolean isOptional;

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
			this.mode = mode;
			getChildren().clear();
			if (mode == EditMode.OVVERIDE) {
				getChildren().add(rawInput);
			} else if (mode == EditMode.DEFAULT) {
				getChildren().add(colorPicker);
			} else if (mode == EditMode.MACRO) {
				getChildren().add(macro);
			}
		}
	}

	/** Used for boolean control properties */
	private static class ControlPropertyBooleanChoiceBox extends StackPane implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private final ChoiceBox<Boolean> choiceBox = new ChoiceBox<>();
		private final InputField<ArmaStringFieldDataChecker, String> rawInput;
		private final MacroGetterButton<Boolean> macro;
		private EditMode mode = EditMode.DEFAULT;

		ControlPropertyBooleanChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			getChildren().add(choiceBox);

			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			rawInput = ControlPropertyInput.createRawInput(control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			choiceBox.getItems().addAll(true, false);
			boolean validData = controlProperty.valuesAreSet();
			if (validData) {
				choiceBox.getSelectionModel().select(controlProperty.getBooleanValue());
				macro = new MacroGetterButton<>(Boolean.class, controlProperty.getBooleanValue());
			} else {
				macro = new MacroGetterButton<>(Boolean.class, null);
			}
			macro.getValueObserver().addValueListener(new ValueListener<Boolean>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean newValue) {
					if (mode == EditMode.MACRO) {
						controlProperty.setValue(newValue);
						if (control != null) {
							control.getUpdateGroup().update(control);
						}
						controlPropertyUpdateGroup.update(controlProperty);
					}
				}
			});
			choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue == null) {
						controlProperty.setFirstValue(null);
					} else {
						controlProperty.setValue(newValue);
					}
					macro.getValueObserver().updateValue(newValue);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
						if (controlProperty.valuesAreSet()) {
							choiceBox.setValue(controlProperty.getBooleanValue());
						}
					}
				});
			}
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
			choiceBox.getSelectionModel().select(getControlProperty().getBooleanValue());
		}

		@Override
		public void disableEditing(boolean disable) {
			setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		private boolean isOptional;

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
			this.mode = mode;
			getChildren().clear();
			if (mode == EditMode.OVVERIDE) {
				getChildren().add(rawInput);
			} else if (mode == EditMode.DEFAULT) {
				getChildren().add(choiceBox);
			} else if (mode == EditMode.MACRO) {
				getChildren().add(macro);
			}
		}
	}

	@SuppressWarnings("unchecked")
	/**Used for control properties that require more than one input*/
	private static class ControlPropertyArrayInput extends StackPane implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private ArrayList<InputField> fields = new ArrayList<>();
		private HBox hBox = new HBox(5);
		private final InputField<ArmaStringFieldDataChecker, String> rawInput;
		private final MacroGetterButton<String[]> macro;
		private EditMode mode = EditMode.DEFAULT;

		ControlPropertyArrayInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty, InputFieldDataChecker... checkers) {
			getChildren().add(hBox);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			rawInput = ControlPropertyInput.createRawInput(control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			InputField<? extends InputFieldDataChecker, String> inputField;
			boolean isDefined = controlProperty.valuesAreSet(); //no need to reiterate
			for (int i = 0; i < checkers.length; i++) {
				inputField = new InputField<>(checkers[i]);
				final int index = i;
				if (isDefined) {
					inputField.setValue(controlProperty.getFirstValue());
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
						public void valueUpdated(@NotNull ValueObserver<String[]> observer, String[] oldValue, String[] newValue) {
							int i = 0;
							for (InputField field : fields) {
								field.setText(newValue[i++]);
							}
						}
					});
				}
				fields.add(inputField);
				placeTooltip(inputField, lookup);
				hBox.getChildren().add(inputField);
			}
			macro = new MacroGetterButton<>(String[].class, controlProperty.getValues());
			macro.getValueObserver().addValueListener(new ValueListener<String[]>() {
				@Override
				public void valueUpdated(ValueObserver<String[]> observer, String[] oldValue, String[] newValue) {
					if (mode == EditMode.MACRO) {
						controlProperty.setValues(newValue);
						if (control != null) {
							control.getUpdateGroup().update(control);
						}
						macro.getValueObserver().updateValue(newValue);
						controlPropertyUpdateGroup.update(controlProperty);
					}
				}
			});
			if (control != null) {
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
						if (controlProperty.valuesAreSet()) {
							for (int i = 0; i < fields.size(); i++) {
								fields.get(i).setValueFromText(controlProperty.getValues()[i]);
							}
						}
					}
				});
			}
		}

		@Override
		public boolean hasValidData() {
			for (InputField inputField : fields) {
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
			for (int i = 0; i < fields.size(); i++) {
				if (defaultValues[i] == null) {
					fields.get(i).clear();
				} else {
					fields.get(i).setText(defaultValues[i]);
				}
			}
		}

		@Override
		public void disableEditing(boolean disable) {
			setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		private boolean isOptional;

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
			this.mode = mode;
			getChildren().clear();
			if (mode == EditMode.OVVERIDE) {
				getChildren().add(rawInput);
			} else if (mode == EditMode.DEFAULT) {
				getChildren().add(hBox);
			} else if (mode == EditMode.MACRO) {
				getChildren().add(macro);
			}
		}
	}

	/** Used for control property font picking */
	private static class ControlPropertyFontChoiceBox extends StackPane implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private final ChoiceBox<AFont> choiceBox = new ChoiceBox<>();
		private final InputField<ArmaStringFieldDataChecker, String> rawInput;
		private final MacroGetterButton<AFont> macro;
		private EditMode mode = EditMode.DEFAULT;

		private final Button btnChooseDefault = new Button(Lang.Misc.DEFAULT_FONT);

		ControlPropertyFontChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			getChildren().add(new HBox(5, choiceBox, btnChooseDefault));

			btnChooseDefault.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					btnChooseDefault.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							choiceBox.getSelectionModel().select(AFont.DEFAULT);
						}
					});
				}
			});

			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			rawInput = ControlPropertyInput.createRawInput(control, controlPropertyUpdateGroup, controlProperty);
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			choiceBox.getItems().addAll(AFont.values());
			boolean validData = controlProperty.valuesAreSet();
			AFont font = null;
			if (validData) {
				try {
					font = AFont.valueOf(controlProperty.getFirstValue());
				} catch (IllegalArgumentException e) {
					e.printStackTrace(System.out);
				}
				choiceBox.getSelectionModel().select(font);
			} else {
				choiceBox.getSelectionModel().select(AFont.DEFAULT);
			}
			macro = new MacroGetterButton<>(AFont.class, font);
			macro.getValueObserver().addValueListener(new ValueListener<AFont>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<AFont> observer, AFont oldValue, AFont newValue) {
					if (mode == EditMode.MACRO) {
						controlProperty.setValue(newValue.name());
						if (control != null) {
							control.getUpdateGroup().update(control);
						}
						controlPropertyUpdateGroup.update(controlProperty);
					}
				}
			});
			choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AFont>() {
				@Override
				public void changed(ObservableValue<? extends AFont> observable, AFont oldValue, AFont newValue) {
					controlProperty.setValue(newValue.name());
					macro.getValueObserver().updateValue(newValue);
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
						choiceBox.setValue(AFont.valueOf(controlProperty.getFirstValue()));
					}
				});
			}
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
			choiceBox.getSelectionModel().select(AFont.DEFAULT);
		}

		@Override
		public void disableEditing(boolean disable) {
			setDisable(disable);
		}

		@Override
		public UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup() {
			return controlPropertyUpdateGroup;
		}

		private boolean isOptional;

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
			this.mode = mode;
			getChildren().clear();
			if (mode == EditMode.OVVERIDE) {
				getChildren().add(rawInput);
			} else if (mode == EditMode.DEFAULT) {
				getChildren().add(choiceBox);
			} else if (mode == EditMode.MACRO) {
				getChildren().add(macro);
			}
		}
	}
}
