

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
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.ExpressionChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 Houses an accordion that allows manipulating multiple control properties. The data editor for each control property is specialized for the input (e.g. color property gets color picker).

 @author Kayler
 @since 07/08/2016. */
public class ControlPropertiesEditorPane extends StackPane {
	private static final Font TOOLTIP_FONT = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20d);
	private final Accordion accordion = new Accordion();
	private ControlClass controlClass;

	private LinkedList<ControlPropertyInputDescriptor> propertyDescriptors = new LinkedList<>();

	private ControlPropertiesEditorPane() {
		getChildren().add(accordion);
	}

	/** Tell all editors to stop listening to the {@link ControlProperty} values again. Invoking is ideal when the pane is no longer needed. */
	public void unlink() {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.unlink();
		}
	}

	/** Tell all editors to listen to the {@link ControlProperty} values again. */
	public void relink() {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.link();
		}
	}

	private static class ControlPropertyInputDescriptor {
		private final ControlPropertyEditorContainer container;
		private boolean optional;
		private boolean nameFound = true;
		private boolean hideIfInherited = false;


		public ControlPropertyInputDescriptor(@NotNull ControlPropertyEditorContainer container) {
			this.container = container;
		}

		@NotNull
		public ControlPropertyValueEditor getInput() {
			return container.getPropertyInput();
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
			return getInput().getControlProperty();
		}

		/** Does same thing as {@link #getInput()}.hasValidData() */
		public boolean hasValidData() {
			return getInput().hasValidData();
		}

		public void unlink() {
			container.unlink();
		}

		public void link() {
			container.link();
		}

		@NotNull
		public ControlPropertyEditorContainer getContainer() {
			return container;
		}

		public void showIfNameContains(@NotNull String name) {
			this.nameFound = name.length() == 0 || getControlProperty().getName().toLowerCase().contains(name);
			setVisible();
		}

		public void hideIfInherited(boolean hide) {
			hideIfInherited = hide;
			getContainer().hideIfInherited(hide);
			setVisible();
		}

		private void setVisible() {
			boolean visible;
			if (getControlProperty().isInherited() && hideIfInherited) {
				visible = false;
			} else {
				visible = nameFound;
			}
			getContainer().setVisible(visible);
			getContainer().setManaged(visible);
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

	@NotNull
	public ControlPropertyEditor findEditorForProperty(@NotNull ControlProperty property) {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			if (descriptor.getControlProperty() == property) {
				return descriptor.getInput();
			}
		}
		throw new IllegalArgumentException("property isn't being edited");
	}

	/** Show only the editors with property names containing <code>name</code>. If length of <code>name</code>.trim() is 0 (), will show all editors */
	public void showPropertiesWithNameContaining(@NotNull String name) {
		name = name.trim().toLowerCase();
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.showIfNameContains(name);
		}
	}

	/** Show only editors with properties that have no inherited value ({@link ControlProperty#getInherited()} == null) */
	public void hideInheritedProperties(boolean hide) {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.hideIfInherited(hide);
		}
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

	private void setupAccordion(Iterable<ControlProperty> requiredProperties, Iterable<ControlProperty> optionalProperties, Iterable<ControlProperty> eventProperties) {
		accordion.getPanes().add(getTitledPane(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.required"), requiredProperties, false));
		accordion.getPanes().add(getTitledPane(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.optional"), optionalProperties, true));
		accordion.getPanes().add(getTitledPane(Lang.ApplicationBundle().getString("ControlPropertiesEditorPane.events"), eventProperties, true));

		accordion.setExpandedPane(accordion.getPanes().get(0));

	}

	/** Get a titled pane for the accordion that holds all control properties */
	private TitledPane getTitledPane(String title, Iterable<ControlProperty> properties, boolean optional) {
		final VBox vb = new VBox(10);
		vb.setPadding(new Insets(5));
		final ScrollPane scrollPane = new ScrollPane(vb);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color:transparent");
		final TitledPane tp = new TitledPane(title, scrollPane);
		tp.setAnimated(false);
		Iterator<ControlProperty> iterator = properties.iterator();

		if (!iterator.hasNext()) {
			vb.getChildren().add(new Label(Lang.ApplicationBundle().getString("Popups.ControlPropertiesConfig.no_properties_available")));
		} else {
			while (iterator.hasNext()) {
				vb.getChildren().add(getControlPropertyEntry(iterator.next(), optional));
			}
		}
		return tp;
	}

	/** Get the pane that shows the name of the property as well as the controls to input data */
	private Node getControlPropertyEntry(ControlProperty property, boolean optional) {
		ControlPropertyEditorContainer container = new ControlPropertyEditorContainer(controlClass, getPropertyInputNode(property));
		ControlPropertyInputDescriptor descriptor = new ControlPropertyInputDescriptor(container);
		propertyDescriptors.add(descriptor);
		descriptor.setIsOptional(optional);

		return container;
	}


	/** Get node that holds the controls to input data. */
	private ControlPropertyValueEditor getPropertyInputNode(ControlProperty controlProperty) {
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

	static Tooltip getTooltip(ControlPropertyLookupConstant lookup) {
		Tooltip tp = new Tooltip(lookup.getAbout());
		tp.setFont(TOOLTIP_FONT);
		return tp;
	}

	/**
	 Places tooltip on n

	 @param n Node to place tooltip on
	 @param lookup constant to create tooltip of
	 @return tooltip placed inside n
	 */
	static Tooltip placeTooltip(Node n, ControlPropertyLookupConstant lookup) {
		Tooltip tip = getTooltip(lookup);
		Tooltip.install(n, tip);
		return tip;
	}

	/** Used for when a set amount of options are available (uses radio button group for option selecting) */
	private static class ControlPropertyInputOption extends FlowPane implements ControlPropertyValueEditor {
		private final ControlProperty controlProperty;
		private ToggleGroup toggleGroup;
		private List<RadioButton> radioButtons;
		private final InputField<StringChecker, String> rawInput = new InputField<>(new StringChecker());
		private final ValueListener<SerializableValue> controlPropertyListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
				setEditorValue(newValue);
			}
		};

		private final ChangeListener<? super Toggle> toggleGroupListener = new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (newValue == null) {
					controlProperty.setValue((SerializableValue) null);
				} else {
					controlProperty.setValue(newValue.getUserData().toString());
				}

			}
		};

		ControlPropertyInputOption(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			super(10, 5);
			this.controlProperty = controlProperty;
			ControlPropertyValueEditor.modifyRawInput(rawInput, control, controlProperty);
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
			initListeners();
		}

		private void setEditorValue(@Nullable SerializableValue newValue) {
			if (newValue == null) {
				toggleGroup.selectToggle(null);
				return;
			}
			for (Toggle toggle : toggleGroup.getToggles()) {
				if (controlProperty.getValue() == null) {
					continue;
				}
				if (toggle.getUserData().equals(controlProperty.getValue().toString())) {
					toggleGroup.selectToggle(toggle);
					return;
				}
			}
		}

		@Override
		public boolean hasValidData() {
			return toggleGroup.getSelectedToggle() != null;
		}

		@NotNull
		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void disableEditing(boolean disable) {
			setDisable(disable);
		}

		@Override
		public void setToMode(EditMode mode) {
			getChildren().clear();
			if (mode == EditMode.CUSTOM_DATA) {
				getChildren().add(rawInput);
			} else if (mode == EditMode.DEFAULT) {
				getChildren().addAll(radioButtons);
			}
		}

		@NotNull
		@Override
		public Node getRootNode() {
			return this;
		}

		@NotNull
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVString.class;
		}

		@Override
		public void clearListeners() {
			toggleGroup.selectedToggleProperty().removeListener(toggleGroupListener);
			controlProperty.getValueObserver().removeListener(controlPropertyListener);
		}

		@Override
		public void initListeners() {
			toggleGroup.selectedToggleProperty().addListener(toggleGroupListener);
			controlProperty.getValueObserver().addListener(controlPropertyListener);
		}

		@Override
		public void refresh() {
			setEditorValue(controlProperty.getValue());
		}
	}

	private static class ControlStylePropertyInput extends ControlStyleValueEditor implements ControlPropertyValueEditor {

		private final ControlProperty controlProperty;
		private final ReadOnlyValueListener<ControlStyleGroup> editorValueListener = new ReadOnlyValueListener<ControlStyleGroup>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<ControlStyleGroup> observer, ControlStyleGroup oldValue, ControlStyleGroup newValue) {
				controlProperty.setValue(newValue);
			}
		};
		private final ValueListener<SerializableValue> controlPropertyListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue == null) {
					menuButton.clearSelection();
				} else {
					setValue((ControlStyleGroup) newValue);
				}
			}
		};

		public ControlStylePropertyInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			ControlPropertyValueEditor.modifyRawInput(getCustomDataTextField(), control, controlProperty);
			if (control instanceof ArmaControl) {
				menuButton.getItems().clear();
				menuButton.getItems().addAll(((ArmaControl) control).getAllowedStyles());
				for (ControlStyle style : menuButton.getItems()) {
					menuButton.bindTooltip(style, style.documentation);
				}
			}
			this.controlProperty = controlProperty;
			setValue((ControlStyleGroup) controlProperty.getValue());
			initListeners();
		}

		@NotNull
		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public boolean hasValidData() {
			return menuButton.getSelectedItems().size() > 0;
		}

		@Override
		public void disableEditing(boolean disable) {
			setDisable(disable);
		}

		@Override
		public void setToMode(EditMode mode) {
			setToCustomData(mode == EditMode.CUSTOM_DATA);
		}

		@NotNull
		@Override
		public Node getRootNode() {
			return this;
		}

		@NotNull
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return ControlStyleGroup.class;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			controlProperty.getValueObserver().removeListener(controlPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addValueListener(editorValueListener);
			controlProperty.getValueObserver().addListener(controlPropertyListener);
		}

		@Override
		public void refresh() {
			setValue((ControlStyleGroup) controlProperty.getValue());
		}
	}

	/**
	 Used for when the input is in a text field. The InputField class also allows for input verifying so that if something entered is wrong, the user will be notified.
	 Used for {@link SVDouble}, {@link SVInteger}, {@link SVString}, {@link Expression}
	 */
	@SuppressWarnings("unchecked")
	private static abstract class ControlPropertyInputField<C extends SerializableValue> extends InputFieldValueEditor<C> implements ControlPropertyValueEditor {

		private final ControlProperty controlProperty;
		private final Class<C> macroTypeClass;
		private final ReadOnlyValueListener<C> editorValueListener = new ReadOnlyValueListener<C>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<C> observer, C oldValue, C newValue) {
				controlProperty.setValue(newValue);
			}
		};
		private final ValueListener<SerializableValue> controlPropertyListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
				if (controlProperty.getValue() != null) {
					inputField.setToButton(false);
					inputField.setValue((C) controlProperty.getValue());
				} else {
					setValue(null);
				}
			}
		};

		ControlPropertyInputField(Class<C> macroTypeClass, @Nullable ControlClass control, @NotNull ControlProperty controlProperty, InputFieldDataChecker checker, @Nullable String promptText) {
			super(checker);
			this.macroTypeClass = macroTypeClass;

			this.controlProperty = controlProperty;
			ControlPropertyValueEditor.modifyRawInput(getCustomDataTextField(), control, controlProperty);

			inputField.setValue((C) controlProperty.getValue());
			if (promptText != null) {
				inputField.setPromptText(promptText);
			}
			initListeners();
		}

		@Override
		public boolean hasValidData() {
			return inputField.hasValidData();
		}

		@NotNull
		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void disableEditing(boolean disable) {
			inputField.setDisable(disable);
		}


		@Override
		public void setToMode(EditMode mode) {
			setToCustomData(mode == EditMode.CUSTOM_DATA);
		}

		@NotNull
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return this.macroTypeClass;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			controlProperty.getValueObserver().removeListener(controlPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addValueListener(editorValueListener);
			controlProperty.getValueObserver().addListener(controlPropertyListener);
		}

		@Override
		public void refresh() {
			setValue((C) controlProperty.getValue());
		}
	}

	private static class ControlPropertyInputFieldString extends ControlPropertyInputField<SVString> {
		ControlPropertyInputFieldString(ControlClass control, ControlProperty controlProperty) {
			super(SVString.class, control, controlProperty, new SVArmaStringChecker(), Lang.LookupBundle().getString("PropertyType.string"));
		}
	}

	private static class ControlPropertyInputFieldDouble extends ControlPropertyInputField<Expression> {
		ControlPropertyInputFieldDouble(ControlClass control, ControlProperty controlProperty) {
			super(Expression.class, control, controlProperty,
					new ExpressionChecker(ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment(), ExpressionChecker.TYPE_FLOAT),
					Lang.LookupBundle().getString("PropertyType.float")
			);
		}
	}

	private static class ControlPropertyInputFieldInteger extends ControlPropertyInputField<Expression> {
		ControlPropertyInputFieldInteger(ControlClass control, ControlProperty controlProperty) {
			super(Expression.class, control, controlProperty,
					new ExpressionChecker(ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment(), ExpressionChecker.TYPE_INT),
					Lang.LookupBundle().getString("PropertyType.int")
			);
		}
	}

	/**
	 Used for when control property requires color input.
	 Use this only when the ControlProperty's value is of type {@link AColor}
	 */
	private static class ControlPropertyColorPicker extends ColorValueEditor implements ControlPropertyValueEditor {

		private final ControlProperty controlProperty;
		private final ReadOnlyValueListener<AColor> valueEditorListener = new ReadOnlyValueListener<AColor>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
				controlProperty.setValue(newValue);
			}
		};
		private final ValueListener<SerializableValue> controlPropertyListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
				if (controlProperty.getValue() != null) { //maybe wasn't updated
					colorPicker.setValue(((AColor) controlProperty.getValue()).toJavaFXColor());
				} else {
					colorPicker.setValue(null);
				}
			}
		};

		ControlPropertyColorPicker(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			ControlPropertyValueEditor.modifyRawInput(getCustomDataTextField(), control, controlProperty);
			boolean validData = controlProperty.getValue() != null;
			if (validData) {
				AColor value = (AColor) controlProperty.getValue();
				colorPicker.setValue(value.toJavaFXColor());
			} else {
				colorPicker.setValue(null);
			}
			initListeners();
		}

		@Override
		public boolean hasValidData() {
			return colorPicker.getValue() != null;
		}

		@NotNull
		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}


		@Override
		public void setToMode(EditMode mode) {
			setToCustomData(mode == EditMode.CUSTOM_DATA);
		}

		@NotNull
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return AColor.class;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(valueEditorListener);
			controlProperty.getValueObserver().removeListener(controlPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addValueListener(valueEditorListener);
			controlProperty.getValueObserver().addListener(controlPropertyListener);
		}

		@Override
		public void refresh() {
			setValue((AColor) controlProperty.getValue());
		}
	}

	/**
	 Used for boolean control properties
	 Use this editor for when the ControlProperty's value is of type {@link SVBoolean}
	 */
	private static class ControlPropertyBooleanChoiceBox extends BooleanValueEditor implements ControlPropertyValueEditor {

		private final ControlProperty controlProperty;
		private final ReadOnlyValueListener<SVBoolean> editorValueListener = new ReadOnlyValueListener<SVBoolean>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<SVBoolean> observer, SVBoolean oldValue, SVBoolean newValue) {
				controlProperty.setValue(newValue);
			}
		};
		private final ValueListener<SerializableValue> controlPropertyListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
				choiceBox.setValue(newValue == null ? null : ((SVBoolean) newValue).isTrue());
			}
		};

		ControlPropertyBooleanChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			ControlPropertyValueEditor.modifyRawInput(getCustomDataTextField(), control, controlProperty);

			boolean validData = controlProperty.getValue() != null;
			if (validData) {
				choiceBox.setValue(controlProperty.getBooleanValue());
			}
			initListeners();

		}

		@Override
		public boolean hasValidData() {
			return choiceBox.getValue() != null;
		}

		@NotNull
		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public void setToMode(EditMode mode) {
			setToCustomData(mode == EditMode.CUSTOM_DATA);
		}

		@NotNull
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVBoolean.class;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			controlProperty.getValueObserver().removeListener(controlPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addValueListener(editorValueListener);
			controlProperty.getValueObserver().addListener(controlPropertyListener);
		}

		@Override
		public void refresh() {
			setValue((SVBoolean) controlProperty.getValue());
		}
	}

	/**
	 Used for control properties that require more than one input
	 This editor will use {@link SVStringArray} as the ControlProperty's value type
	 */
	@SuppressWarnings("unchecked")
	private static class ControlPropertyArrayInput extends ArrayValueEditor implements ControlPropertyValueEditor {

		private final ControlProperty controlProperty;
		private final ReadOnlyValueListener<SVStringArray> editorValueListener = new ReadOnlyValueListener<SVStringArray>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<SVStringArray> observer, SVStringArray oldValue, SVStringArray newValue) {
				controlProperty.setValue(newValue);
			}
		};
		private final ValueListener<SerializableValue> controlPropertyListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
				setValue((SVStringArray) newValue);
			}
		};

		ControlPropertyArrayInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty, int defaultNumFields) {
			super(defaultNumFields);
			if (true) {
				throw new RuntimeException("review this code to check for correctness");
			}
			this.controlProperty = controlProperty;
			ControlPropertyValueEditor.modifyRawInput(getCustomDataTextField(), control, controlProperty);

			setValue((SVStringArray) controlProperty.getValue());
			initListeners();
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

		@NotNull
		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public void setToMode(EditMode mode) {
			setToCustomData(mode == EditMode.CUSTOM_DATA);
		}

		@NotNull
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return SVStringArray.class;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			controlProperty.getValueObserver().removeListener(controlPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addValueListener(editorValueListener);
			controlProperty.getValueObserver().addListener(controlPropertyListener);
		}

		@Override
		public void refresh() {
			setValue((SVStringArray) controlProperty.getValue());
		}
	}

	/**
	 Used for control property font picking
	 Used for ControlProperty instances where it's value is {@link AFont}
	 */
	private static class ControlPropertyFontChoiceBox extends FontValueEditor implements ControlPropertyValueEditor {

		private final ControlProperty controlProperty;
		private final ReadOnlyValueListener<AFont> editorValueListener = new ReadOnlyValueListener<AFont>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<AFont> observer, AFont oldValue, AFont newValue) {
				controlProperty.setValue(newValue);
			}
		};
		private final ValueListener<SerializableValue> controlPropertyListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
				comboBox.setValue((AFont) controlProperty.getValue());
			}
		};

		ControlPropertyFontChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			ControlPropertyValueEditor.modifyRawInput(getCustomDataTextField(), control, controlProperty);

			setValue((AFont) controlProperty.getValue());
			initListeners();
		}

		@Override
		public boolean hasValidData() {
			return !comboBox.getSelectionModel().isEmpty();
		}

		@NotNull
		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}


		@Override
		public void setToMode(EditMode mode) {
			setToCustomData(mode == EditMode.CUSTOM_DATA);
		}

		@NotNull
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return AFont.class;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			controlProperty.getValueObserver().removeListener(controlPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addValueListener(editorValueListener);
			controlProperty.getValueObserver().addListener(controlPropertyListener);
		}

		@Override
		public void refresh() {
			setValue((AFont) controlProperty.getValue());
		}
	}

	/** Use this editor for {@link ASound} ControlProperty values */
	private class ControlPropertySoundInput extends SoundValueEditor implements ControlPropertyValueEditor {

		private final ControlProperty controlProperty;
		private final ReadOnlyValueListener<ASound> editorValueListener = new ReadOnlyValueListener<ASound>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<ASound> observer, ASound oldValue, ASound newValue) {
				controlProperty.setValue(newValue);
			}
		};
		private final ValueListener<SerializableValue> controlPropertyListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, @Nullable SerializableValue oldValue, @Nullable SerializableValue newValue) {
				setValue((ASound) newValue);
			}
		};

		public ControlPropertySoundInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;

			ControlPropertyValueEditor.modifyRawInput(getCustomDataTextField(), control, controlProperty);

			initListeners();

		}

		@NotNull
		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public boolean hasValidData() {
			return getValue() != null;
		}

		@Override
		public void disableEditing(boolean disable) {
			getRootNode().setDisable(disable);
		}

		@Override
		public void setToMode(EditMode mode) {
			setToCustomData(mode == EditMode.CUSTOM_DATA);
		}

		@NotNull
		@Override
		public Class<? extends SerializableValue> getMacroClass() {
			return ASound.class;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			controlProperty.getValueObserver().removeListener(controlPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addValueListener(editorValueListener);
			controlProperty.getValueObserver().addListener(controlPropertyListener);
		}

		@Override
		public void refresh() {
			setValue((ASound) controlProperty.getValue());
		}
	}
}
