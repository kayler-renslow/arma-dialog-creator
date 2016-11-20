/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and
 * noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out
 * of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.*;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 Houses an accordion that allows manipulating multiple control properties. The data editor for each control property is specialized for the input (e.g. color property gets color picker).

 @author Kayler
 @since 07/08/2016. */
public class ControlPropertiesEditorPane extends StackPane {
	private static final Font TOOLTIP_FONT = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20d);
	private final Accordion accordion = new Accordion();
	private ControlClass controlClass;

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

		/** Return true if the {@link ControlProperty} returned from {@link #getControlProperty()} is optional (not in required list returned from {@link ControlClassRequirementSpecification#getRequiredProperties()}) */
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
	 Creates the accordion according to the control class's specification. For the inputted values in the accordion, they are fetched from {@link ControlClass#getRequiredProperties()}, {@link ControlClass#getOptionalProperties()}, and {@link ControlClass#getEventProperties()}<br>
	 It is important to note that when the control properties inside the control are edited, they will be updated in the control class as well. There is no copying of the controlClass's control properties and everything is passed by reference.

	 @param controlClass control class that has the properties to edit
	 */
	public ControlPropertiesEditorPane(@NotNull ControlClass controlClass) {
		this();
		this.controlClass = controlClass;
		setupAccordion(controlClass.getRequiredProperties(), controlClass.getOptionalProperties(), controlClass.getEventProperties());
	}

	/** Return true if all values entered for all properties are good/valid, false if at least one is bad. */
	public boolean allValuesAreGood() {
		return getMissingProperties().size() == 0;
	}


	public ControlPropertyEditor[] getEditors() {
		return propertyEditors;
	}

	/** Get the {@link ControlClass} being edited */
	@NotNull
	public ControlClass getControlClass() {
		return controlClass;
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

	private void setupAccordion(ReadOnlyList<ControlProperty> requiredProperties, ReadOnlyList<ControlProperty> optionalProperties, ReadOnlyList<ControlProperty> eventProperties) {
		accordion.getPanes().add(getTitledPane(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.required"), requiredProperties, false));
		accordion.getPanes().add(getTitledPane(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.optional"), optionalProperties, true));
		accordion.getPanes().add(getTitledPane(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.events"), eventProperties, true));

		accordion.setExpandedPane(accordion.getPanes().get(0));
		propertyEditors = new ControlPropertyEditor[propertyDescriptors.size()];
		for (int i = 0; i < propertyEditors.length; i++) {
			propertyEditors[i] = propertyDescriptors.get(i).getInput();
		}
	}

	/** Get a titled pane for the accordion that holds all control properties */
	private TitledPane getTitledPane(String title, List<ControlProperty> properties, boolean optional) {
		VBox vb = new VBox(10);
		TitledPane tp = new TitledPane(title, vb);
		tp.setAnimated(false);
		if (properties.size() == 0) {
			vb.getChildren().add(new Label(Lang.ApplicationBundle().getString("Popups.ControlPropertiesConfig.no_properties_available")));
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

		final ControlPropertyInput propertyInput = getPropertyInputNode(c);

		propertyInput.disableEditing(c.getPropertyLookup() == ControlPropertyLookup.TYPE);

		if (propertyInput.displayFullWidth()) {
			HBox.setHgrow(stackPanePropertyInput, Priority.ALWAYS);
		}

		if (c.getMacro() != null) {
			updatePropertyInputMode(stackPanePropertyInput, propertyInput, ControlPropertyInput.EditMode.MACRO);
		}

		final MenuItem miDefaultEditor = new MenuItem(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.use_default_editor"));
		final MenuItem miResetToDefault = new MenuItem(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.reset_to_default"));
		final MenuItem miMacro = new MenuItem(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.set_to_macro"));
		final MenuItem miOverride = new MenuItem(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.value_override"));//broken. Maybe fix it later. Don't delete this in case you change your mind
		final MenuButton menuButton = new MenuButton(c.getName(), null, miDefaultEditor, new SeparatorMenuItem(), miResetToDefault, miMacro/*,miOverride*/);
		placeTooltip(menuButton, propertyInput.getControlProperty().getPropertyLookup());

		if (c.getPropertyLookup() instanceof ControlPropertyLookup) {
			switch ((ControlPropertyLookup) c.getPropertyLookup()) {
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

		if (c.isCustomData()) {
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
			propertyInput.getControlProperty().setHasCustomData(mode == ControlPropertyInput.EditMode.OVERRIDE);
		}
	}

	/** Get node that holds the controls to input data. */
	private ControlPropertyInput getPropertyInputNode(ControlProperty controlProperty) {
		ControlPropertyLookupConstant lookup = controlProperty.getPropertyLookup();
		if (lookup.getOptions() != null && lookup.getOptions().length > 0) {
			return new ControlPropertyInputOption(controlClass, controlProperty);
		}
		PropertyType propertyType = lookup.getPropertyType();
		switch (propertyType) {
			case INT:
				return new ControlPropertyInputFieldInteger(controlClass, controlProperty);
			case FLOAT:
				return new ControlPropertyInputFieldDouble(controlClass, controlProperty);
			case EXP:
				return new ControlPropertyExprInput(controlClass, controlProperty);
			case CONTROL_STYLE:
				return new ControlStylePropertyInput(controlClass, controlProperty);
			case BOOLEAN:
				return new ControlPropertyBooleanChoiceBox(controlClass, controlProperty);
			case STRING:
				return new ControlPropertyInputFieldString(controlClass, controlProperty);
			case ARRAY:
				return new ControlPropertyArrayInput(controlClass, controlProperty, 2);
			case COLOR:
				return new ControlPropertyColorPicker(controlClass, controlProperty);
			case SOUND:
				return new ControlPropertySoundInput(controlClass, controlProperty);
			case FONT:
				return new ControlPropertyFontChoiceBox(controlClass, controlProperty);
			case FILE_NAME:
				return new ControlPropertyInputFieldString(controlClass, controlProperty);
			case IMAGE:
				return new ControlPropertyInputFieldString(controlClass, controlProperty); //todo use proper value editor
			case HEX_COLOR_STRING:
				return new ControlPropertyColorPicker(controlClass, controlProperty); //todo have hex color editor (or maybe just take the AColor value instance and create a AHexColor instance from it)
			case TEXTURE:
				return new ControlPropertyInputFieldString(controlClass, controlProperty);
			case SQF:
				return new ControlPropertyInputFieldString(controlClass, controlProperty);
		}
		throw new IllegalStateException("Should have made a match");
	}

	private Tooltip getTooltip(ControlPropertyLookupConstant lookup) {
		String tooltip = "";
		for (int i = 0; i < lookup.getAbout().length; i++) {
			tooltip += lookup.getAbout()[i] + "\n";
		}
		Tooltip tp = new Tooltip(tooltip);
		tp.setFont(TOOLTIP_FONT);
		return tp;
	}

	/**
	 Places tooltip on n

	 @param n Node to place tooltip on
	 @param lookup constant to create tooltip of
	 @return tooltip placed inside n
	 */
	private Tooltip placeTooltip(Node n, ControlPropertyLookupConstant lookup) {
		Tooltip tip = getTooltip(lookup);
		Tooltip.install(n, tip);
		return tip;
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
		static InputField<StringChecker, String> modifyRawInput(InputField<StringChecker, String> rawInput, ControlClass control, ControlProperty controlProperty) {
			if (controlProperty.isPropertyType(PropertyType.ARRAY)) {
				throw new IllegalArgumentException("don't use this method for ARRAY property type");
			}
			rawInput.getValueObserver().addValueListener(new ValueListener<String>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
					controlProperty.setValue(new SVString(newValue));
				}
			});
			return rawInput;
		}
	}

	/** Used for when a set amount of options are available (uses radio button group for option selecting) */
	private static class ControlPropertyInputOption extends FlowPane implements ControlPropertyInput {
		private final ControlProperty controlProperty;
		private ToggleGroup toggleGroup;
		private List<RadioButton> radioButtons;
		private final InputField<StringChecker, String> rawInput = new InputField<>(new StringChecker());

		ControlPropertyInputOption(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			super(10, 5);
			this.controlProperty = controlProperty;
			ControlPropertyInput.modifyRawInput(rawInput, control, controlProperty);
			ControlPropertyLookup lookup = (ControlPropertyLookup) controlProperty.getPropertyLookup();
			toggleGroup = new ToggleGroup();
			RadioButton radioButton, toSelect = null;
			boolean validData = controlProperty.getValue() != null;
			if (lookup.getOptions() == null) {
				throw new IllegalStateException("options shouldn't be null");
			}
			radioButtons = new ArrayList<>(lookup.getOptions().length);
			for (ControlPropertyOption option : lookup.getOptions()) {
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

				}
			});
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					if (newValue == null) {
						toggleGroup.selectToggle(null);
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

	private static class ControlStylePropertyInput extends ControlStyleValueEditor implements ControlPropertyInput {

		private final ControlProperty controlProperty;

		public ControlStylePropertyInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlProperty);
			if (control instanceof ArmaControl) {
				menuButton.getItems().clear();
				menuButton.getItems().addAll(((ArmaControl) control).getAllowedStyles());
				for (ControlStyle style : menuButton.getItems()) {
					menuButton.bindTooltip(style, style.documentation);
				}
			}
			this.controlProperty = controlProperty;
			setValue((ControlStyleGroup) controlProperty.getValue());
			getReadOnlyObserver().addValueListener(new ReadOnlyValueListener<ControlStyleGroup>() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver<ControlStyleGroup> observer, ControlStyleGroup oldValue, ControlStyleGroup newValue) {
					controlProperty.setValue(newValue);
				}
			});
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
					if (newValue == null) {
						menuButton.clearSelection();
					} else {
						setValue((ControlStyleGroup) newValue);
					}
				}
			});
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public boolean hasValidData() {
			return menuButton.getSelectedItems().size() > 0;
		}

		@Override
		public void resetToDefaultValue() {
			if (controlProperty.getDefaultValue() == null) {
				menuButton.clearSelection();
			} else {
				ControlStyleGroup group = (ControlStyleGroup) controlProperty.getDefaultValue();
				menuButton.setSelected(group.getValues());
			}
		}

		@Override
		public void disableEditing(boolean disable) {
			setDisable(disable);
		}

		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@NotNull
		@Override
		public Node getRootNode() {
			return this;
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return ControlStyleGroup.class;
		}
	}

	/**
	 Used for when the input is in a text field. The InputField class also allows for input verifying so that if something entered is wrong, the user will be notified.
	 Used for {@link SVDouble}, {@link SVInteger}, {@link SVString}, {@link Expression}
	 */
	@SuppressWarnings("unchecked")
	private static abstract class ControlPropertyInputField<C extends SerializableValue> extends InputFieldValueEditor<C> implements ControlPropertyInput {

		private final ControlProperty controlProperty;
		private final Class<C> macroTypeClass;

		ControlPropertyInputField(Class<C> macroTypeClass, @Nullable ControlClass control, @NotNull ControlProperty controlProperty, InputFieldDataChecker checker, @Nullable String promptText) {
			super(checker);
			this.macroTypeClass = macroTypeClass;

			this.controlProperty = controlProperty;
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlProperty);

			inputField.setValue((C) controlProperty.getValue());
			getReadOnlyObserver().addValueListener(new ReadOnlyValueListener<C>() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver<C> observer, C oldValue, C newValue) {
					controlProperty.setValue(newValue);
				}
			});
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					if (controlProperty.getValue() != null) {
						inputField.setToButton(false);
						inputField.setValue((C) controlProperty.getValue());
					} else {
						setValue(null);
					}
				}
			});
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
			super(SVString.class, control, controlProperty, new SVArmaStringChecker(), Lang.LookupBundle().getString("PropertyType.string"));
		}
	}

	private static class ControlPropertyInputFieldDouble extends ControlPropertyInputField<SVDouble> {
		ControlPropertyInputFieldDouble(ControlClass control, ControlProperty controlProperty) {
			super(SVDouble.class, control, controlProperty, new SVDoubleChecker(), Lang.LookupBundle().getString("PropertyType.float"));
		}
	}

	private static class ControlPropertyInputFieldInteger extends ControlPropertyInputField<SVInteger> {
		ControlPropertyInputFieldInteger(ControlClass control, ControlProperty controlProperty) {
			super(SVInteger.class, control, controlProperty, new SVIntegerChecker(), Lang.LookupBundle().getString("PropertyType.int"));
		}
	}

	private class ControlPropertyExprInput extends ControlPropertyInputField<Expression> {
		public ControlPropertyExprInput(ControlClass control, ControlProperty controlProperty) {
			super(Expression.class, control, controlProperty,
					new ExpressionChecker(ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment()),
					Lang.LookupBundle().getString("PropertyType.exp"));
		}
	}

	/**
	 Used for when control property requires color input.
	 Use this only when the ControlProperty's value is of type {@link AColor}
	 */
	private static class ControlPropertyColorPicker extends ColorValueEditor implements ControlPropertyInput {

		private final ControlProperty controlProperty;

		ControlPropertyColorPicker(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlProperty);
			ControlPropertyLookup lookup = (ControlPropertyLookup) controlProperty.getPropertyLookup();
			boolean validData = controlProperty.getValue() != null;
			if (validData) {
				AColor value = (AColor) controlProperty.getValue();
				colorPicker.setValue(value.toJavaFXColor());
			} else {
				colorPicker.setValue(null);
			}
			getReadOnlyObserver().addValueListener(new ReadOnlyValueListener<AColor>() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
					controlProperty.setValue(newValue);
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
			if (controlProperty.getDefaultValue() == null) {
				setValue(null);
				return;
			}
			AColor color = (AColor) controlProperty.getDefaultValue();
			colorPicker.setValue(color.toJavaFXColor());
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}


		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return AColor.class;
		}

		@Override
		public boolean displayFullWidth() {
			return false;
		}
	}

	/**
	 Used for boolean control properties
	 Use this editor for when the ControlProperty's value is of type {@link SVBoolean}
	 */
	private static class ControlPropertyBooleanChoiceBox extends BooleanValueEditor implements ControlPropertyInput {

		private final ControlProperty controlProperty;

		ControlPropertyBooleanChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlProperty);
			ControlPropertyLookup lookup = (ControlPropertyLookup) controlProperty.getPropertyLookup();

			boolean validData = controlProperty.getValue() != null;
			if (validData) {
				choiceBox.setValue(controlProperty.getBooleanValue());
			}
			getReadOnlyObserver().addValueListener(new ReadOnlyValueListener<SVBoolean>() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver<SVBoolean> observer, SVBoolean oldValue, SVBoolean newValue) {
					controlProperty.setValue(newValue);
				}
			});
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					choiceBox.setValue(newValue == null ? null : ((SVBoolean) newValue).isTrue());
				}
			});

		}

		@Override
		public boolean hasValidData() {
			return choiceBox.getValue() != null;
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefaultValue() {
			SVBoolean def = (SVBoolean) getControlProperty().getDefaultValue();
			if (def == null) { //no intellij this is not always false
				choiceBox.setValue(null);
				return;
			}
			choiceBox.setValue(def.isTrue());
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVBoolean.class;
		}

		@Override
		public boolean displayFullWidth() {
			return false;
		}
	}

	/**
	 Used for control properties that require more than one input
	 This editor will use {@link SVStringArray} as the ControlProperty's value type
	 */
	@SuppressWarnings("unchecked")
	private static class ControlPropertyArrayInput extends ArrayValueEditor implements ControlPropertyInput {

		private final ControlProperty controlProperty;

		ControlPropertyArrayInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty, int defaultNumFields) {
			super(defaultNumFields);
			if (true) {
				throw new RuntimeException("review this code to check for correctness");
			}
			this.controlProperty = controlProperty;
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlProperty);
			ControlPropertyLookup lookup = (ControlPropertyLookup) controlProperty.getPropertyLookup();

			InputField<ArmaStringChecker, String> inputField;

			getReadOnlyObserver().addValueListener(new ReadOnlyValueListener<SVStringArray>() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver<SVStringArray> observer, SVStringArray oldValue, SVStringArray newValue) {
					controlProperty.setValue(newValue);
				}
			});
			setValue((SVStringArray) controlProperty.getValue());
			for (InputField<ArmaStringChecker, String> editor : editors) {
				inputField = editor;
			}
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					setValue((SVStringArray) newValue);
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
					editors.get(i).setValue(defaultValues[i]);
				}
			}
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVStringArray.class;
		}

		@Override
		public boolean displayFullWidth() {
			return false;
		}
	}

	/**
	 Used for control property font picking
	 Used for ControlProperty instances where it's value is {@link AFont}
	 */
	private static class ControlPropertyFontChoiceBox extends FontValueEditor implements ControlPropertyInput {


		private final ControlProperty controlProperty;

		ControlPropertyFontChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlProperty);
			ControlPropertyLookup lookup = (ControlPropertyLookup) controlProperty.getPropertyLookup();

			setValue((AFont) controlProperty.getValue());
			getReadOnlyObserver().addValueListener(new ReadOnlyValueListener<AFont>() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver<AFont> observer, AFont oldValue, AFont newValue) {
					controlProperty.setValue(newValue);
				}
			});
			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					comboBox.setValue((AFont) controlProperty.getValue());
				}
			});
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
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return AFont.class;
		}

		@Override
		public boolean displayFullWidth() {
			return false;
		}
	}

	/** Use this editor for {@link ASound} ControlProperty values */
	private class ControlPropertySoundInput extends SoundValueEditor implements ControlPropertyInput {

		private final ControlProperty controlProperty;

		public ControlPropertySoundInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;

			ControlPropertyInput.modifyRawInput(getOverrideTextField(), control, controlProperty);

			getReadOnlyObserver().addValueListener(new ReadOnlyValueListener<ASound>() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver<ASound> observer, ASound oldValue, ASound newValue) {
					controlProperty.setValue(newValue);
				}
			});

			controlProperty.getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
					setValue((ASound) newValue);
				}
			});

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
		public void setToMode(EditMode mode) {
			setToOverride(mode == EditMode.OVERRIDE);
		}

		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return ASound.class;
		}

		@Override
		public boolean displayFullWidth() {
			return false;
		}
	}
}
