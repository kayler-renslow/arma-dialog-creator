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
		Hyperlink hyperlinkLabel = new Hyperlink(c.getName());
		hyperlinkLabel.getStyleClass().add("hyper-link-fixed-color");

		ControlPropertyInput propertyInput;
		if (c.getPropertyLookup() == ControlPropertyLookup.TYPE) {
			ControlType type = ControlType.getById(c.getIntValue());
			if (type == null) {
				throw new IllegalStateException("type shouldn't be null");
			}
			ControlPropertyInputField field = new ControlPropertyInputField(control, c, new StringFieldDataChecker(), "");
			propertyInput = field;
			field.setText(type.fullDisplayText());
			propertyInput.disableEditing(true);
		} else {
			propertyInput = getPropertyInputNode(c);
		}
		propertyInputs.add(propertyInput);
		propertyInput.setIsOptional(optional);
		hyperlinkLabel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				propertyInput.resetToDefault();
			}
		});

		pane.getChildren().addAll(hyperlinkLabel, (Node) propertyInput);

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
				return new ControlPropertyInputField(control, controlProperty, new IntegerFieldDataChecker());
			case FLOAT:
				return new ControlPropertyInputField(control, controlProperty, new DoubleFieldDataChecker());
			case BOOLEAN:
				return new ControlPropertyBooleanChoiceBox(control, controlProperty);
			case STRING:
				return new ControlPropertyInputField(control, controlProperty, new StringFieldDataChecker(), Lang.Popups.ControlPropertiesConfig.STRING);
			case ARRAY:
				return new ControlPropertyArrayInput(control, controlProperty, new StringFieldDataChecker(), new StringFieldDataChecker());
			case COLOR:
				return new ControlPropertyColorPicker(control, controlProperty);
			case SOUND:
				return new ControlPropertyArrayInput(control, controlProperty, new StringFieldDataChecker(), new IntegerFieldDataChecker(), new DoubleFieldDataChecker());
			case FONT:
				return new ControlPropertyFontChoiceBox(control, controlProperty);
			case FILE_NAME:
				return new ControlPropertyInputField(control, controlProperty, new StringFieldDataChecker(), Lang.Popups.ControlPropertiesConfig.FILE_NAME);
			case IMAGE:
				return new ControlPropertyInputField(control, controlProperty, new StringFieldDataChecker(), Lang.Popups.ControlPropertiesConfig.IMAGE_PATH);
			case HEX_COLOR_STRING:
				return new ControlPropertyColorPicker(control, controlProperty);
			case TEXTURE:
				return new ControlPropertyInputField(control, controlProperty, new StringFieldDataChecker(), Lang.Popups.ControlPropertiesConfig.TEXTURE);
			case EVENT:
				return new ControlPropertyInputField(control, controlProperty, new StringFieldDataChecker(), Lang.Popups.ControlPropertiesConfig.SQF_CODE);
			case SQF:
				return new ControlPropertyInputField(control, controlProperty, new StringFieldDataChecker(), Lang.Popups.ControlPropertiesConfig.SQF_CODE);
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
		void setIsOptional(boolean optional);
	}

	/** Used for when a set amount of options are available (uses radio button group for option selecting) */
	private static class ControlPropertyOption extends FlowPane implements ControlPropertyInput {
		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private ToggleGroup toggleGroup;


		ControlPropertyOption(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			super(10, 5);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			toggleGroup = new ToggleGroup();
			RadioButton radioButton, toSelect = null;
			boolean validData = controlProperty.valuesAreSet();
			if (lookup.options == null) {
				throw new IllegalStateException("options shouldn't be null");
			}
			for (Option option : lookup.options) {
				if (option == null) {
					throw new IllegalStateException("option shouldn't be null");
				}
				radioButton = new RadioButton(option.displayName);
				radioButton.setUserData(option.value);
				radioButton.setTooltip(new Tooltip(option.description));
				radioButton.setToggleGroup(toggleGroup);
				getChildren().add(radioButton);
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
						controlProperty.getValuesObserver().updateValue(new String[]{newValue.getUserData().toString()});
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
		public void resetToDefault() {
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
	}

	@SuppressWarnings("unchecked")
	/**Used for when the input is in a text field. The InputField class also allows for input verifying so that if something entered is wrong, the user will be notified.*/
	private static class ControlPropertyInputField extends InputField implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;

		ControlPropertyInputField(@Nullable ControlClass control, @NotNull ControlProperty controlProperty, InputFieldDataChecker checker, @Nullable String promptText) {
			super(checker);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			if (controlProperty.valuesAreSet()) {
				setValueFromText(controlProperty.getFirstValue());
			}

			getValueObserver().addValueListener(new ValueListener() {
				@Override
				public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
					if (newValue == null) {
						controlProperty.getValuesObserver().updateValue(new String[]{null});
					} else {
						controlProperty.getValuesObserver().updateValue(new String[]{newValue.toString()});
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				InputField myself = this;
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
						if (controlProperty.valuesAreSet()) {
							myself.setText(controlProperty.getFirstValue());
						}
					}
				});
			}
			placeTooltip(this, lookup);
			if (promptText != null) {
				setPromptText(promptText);
			}
			HBox.setHgrow(this, Priority.ALWAYS);
		}

		ControlPropertyInputField(@Nullable ControlClass control, @NotNull ControlProperty controlProperty, InputFieldDataChecker checker) {
			this(control, controlProperty, checker, null);
		}

		@Override
		public boolean hasValidData() {
			return super.hasValidData();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefault() {
			if (getControlProperty().getDefaultValues()[0] == null) {
				clear();
			} else {
				setText(getControlProperty().getFirstValue().replaceAll("\"", "\"\""));
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
	}

	/** Used for when control property requires color input */
	private static class ControlPropertyColorPicker extends ColorPicker implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;

		ControlPropertyColorPicker(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();

			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			boolean validData = controlProperty.valuesAreSet();
			if (validData) {
				setValue(AColor.toJavaFXColor(controlProperty.getValues()));
			} else {
				setValue(null);
			}
			valueProperty().addListener(new ChangeListener<Color>() {
				@Override
				public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
					if (newValue == null) {
						controlProperty.setFirstValue(null);
					} else {
						controlProperty.setValue(new AColor(newValue));
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				ColorPicker myself = this;
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
						if (controlProperty.valuesAreSet()) { //maybe wasn't updated
							myself.setValue(AColor.toJavaFXColor(controlProperty.getValues()));
						}
					}
				});
			}
			placeTooltip(this, lookup);
		}

		@Override
		public boolean hasValidData() {
			return getValue() != null;
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefault() {
			try {
				setValue(AColor.toJavaFXColor(getControlProperty().getDefaultValues()));
			} catch (NullPointerException e) {
				setValue(null);
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
	}

	/** Used for boolean control properties */
	private static class ControlPropertyBooleanChoiceBox extends ChoiceBox<Boolean> implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;

		ControlPropertyBooleanChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			getItems().addAll(true, false);
			boolean validData = controlProperty.valuesAreSet();
			if (validData) {
				getSelectionModel().select(controlProperty.getBooleanValue());
			}
			getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue == null) {
						controlProperty.setFirstValue(null);
					} else {
						controlProperty.setValue(newValue);
					}
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			if (control != null) {
				ChoiceBox<Boolean> myself = this;
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
						if (controlProperty.valuesAreSet()) {
							myself.setValue(controlProperty.getBooleanValue());
						}
					}
				});
			}
			placeTooltip(this, lookup);
		}

		@Override
		public boolean hasValidData() {
			return !getSelectionModel().isEmpty();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefault() {
			if (getControlProperty().getDefaultValues()[0] == null) { //no intellij this is not always false
				getSelectionModel().clearSelection();
				return;
			}
			getSelectionModel().select(getControlProperty().getBooleanValue());
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
	}

	@SuppressWarnings("unchecked")
	/**Used for control properties that require more than one input*/
	private static class ControlPropertyArrayInput extends HBox implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;
		private ArrayList<InputField> fields = new ArrayList<>();

		ControlPropertyArrayInput(@Nullable ControlClass control, @NotNull ControlProperty controlProperty, InputFieldDataChecker... checkers) {
			super(5);
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
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
				getChildren().add(inputField);
			}
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
		public void resetToDefault() {
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
	}

	/** Used for control property font picking */
	private static class ControlPropertyFontChoiceBox extends ChoiceBox<AFont> implements ControlPropertyInput {

		private final UpdateListenerGroup<ControlProperty> controlPropertyUpdateGroup;
		private final ControlProperty controlProperty;

		ControlPropertyFontChoiceBox(@Nullable ControlClass control, @NotNull ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			this.controlPropertyUpdateGroup = new UpdateListenerGroup<>();
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			getItems().addAll(AFont.values());
			boolean validData = controlProperty.valuesAreSet();
			getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AFont>() {
				@Override
				public void changed(ObservableValue<? extends AFont> observable, AFont oldValue, AFont newValue) {
					controlProperty.setValue(newValue.name());
					if (control != null) {
						control.getUpdateGroup().update(control);
					}
					controlPropertyUpdateGroup.update(controlProperty);
				}
			});
			AFont font = null;
			if (validData) {
				try {
					font = AFont.valueOf(controlProperty.getFirstValue());
				} catch (IllegalArgumentException e) {
					e.printStackTrace(System.out);
				}
				getSelectionModel().select(font);
			} else {
				getSelectionModel().select(AFont.DEFAULT);
			}
			if (control != null) {
				ChoiceBox<AFont> myself = this;
				control.getUpdateGroup().addListener(new UpdateListener<Object>() {
					@Override
					public void update(Object data) {
						myself.setValue(AFont.valueOf(controlProperty.getFirstValue()));
					}
				});
			}
			placeTooltip(this, lookup);
		}

		@Override
		public boolean hasValidData() {
			return !getSelectionModel().isEmpty();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefault() {
			getSelectionModel().select(AFont.DEFAULT);
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
	}
}
