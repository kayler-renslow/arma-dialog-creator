package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.*;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.Option;
import com.kaylerrenslow.armaDialogCreator.gui.fx.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopupUndecorated;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 Created by Kayler on 05/31/2016.
 */
public class ControlPropertiesConfigPopup extends StagePopupUndecorated<VBox> {
	private ArmaControl control;

	private ArrayList<ControlPropertyInput> propertyInputs = new ArrayList<>();

	public ControlPropertiesConfigPopup(@NotNull ArmaControl control) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null);
		initialize();
		initialize(control);
	}

	private void initialize() {
		myRootElement.getStyleClass().add("rounded-node");
		myStage.initStyle(StageStyle.TRANSPARENT);

		myScene.setFill(Color.TRANSPARENT);
		final double padding = 20.0;
		myRootElement.setPadding(new Insets(padding / 2, padding, padding, padding));
	}

	/**
	 Configures the popup to edit the given control.

	 @return true if the initialization was successful, or false if the initialization was canceled
	 */
	public boolean initialize(ArmaControl c) {
		if (myRootElement.getChildren().size() > 0) {
			if (!allValuesAreGood()) {
				return false;
			}
		}
		this.control = c;
		Color bg = control.getRenderer().getBackgroundColor();
		control.getRenderer().getBackgroundColorObserver().addValueListener(new ValueListener<AColor>() {
			@Override
			public void valueUpdated(AColor oldValue, AColor newValue) {
				if (newValue != null) {
					setBorderColor(newValue.toJavaFXColor()); //update the popup's border color
				}
			}
		});
		setBorderColor(bg);
		myRootElement.getChildren().clear();
		addCloseButton();
		addPropertiesAccordion();
		return true;
	}

	/** Return true if all values entered for all properties are good/valid, false if at least one is bad */
	private boolean allValuesAreGood() {
		return getMissingProperties().size() == 0;
	}

	private List<ControlProperty> getMissingProperties() {
		List<ControlProperty> properties = new ArrayList<>();
		for (ControlPropertyInput input : propertyInputs) {
			if (!input.hasValidData() && !input.isOptional()) {
				properties.add(input.getControlProperty());
			}
		}
		return properties;
	}

	private void addCloseButton() {
		Button closeBtn = new Button("x");
		closeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				WindowEvent windowEvent = new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST);
				myStage.getOnCloseRequest().handle(windowEvent);
				List<ControlProperty> missing = getMissingProperties();
				boolean goodValues = missing.size() == 0;
				if (!windowEvent.isConsumed() && goodValues) {
					close();
				}
				if (!goodValues) {
					System.out.println(Arrays.toString(missing.toArray()));
					beepFocus();
				}
			}
		});
		closeBtn.getStyleClass().add("close-button");
		ComboBox<String> test = new ComboBox<>();
		myRootElement.getChildren().add(new BorderPane(null, null, closeBtn, null, test));
	}

	private void addPropertiesAccordion() {
		Accordion accordion = new Accordion();
		ScrollPane scrollPane = new ScrollPane(accordion);

		accordion.getPanes().add(getTitledPane("Required", control.getRequiredProperties(), control, false));
		accordion.getPanes().add(getTitledPane("Optional", control.getOptionalProperties(), control, true));
		accordion.getPanes().add(getTitledPane("Events", control.getEventProperties(), control, true));

		accordion.setExpandedPane(accordion.getPanes().get(0));

		myRootElement.getChildren().add(scrollPane);
	}


	private void setBorderColor(Color bg) {
		myRootElement.setStyle(String.format("-fx-border-color: rgba(%f%%,%f%%,%f%%,%f);", bg.getRed() * 100.0, bg.getGreen() * 100.0, bg.getBlue() * 100.0, bg.getOpacity()));
	}

	/** Get a titled pane for the accordion that holds all control properties */
	private TitledPane getTitledPane(String title, ControlProperty[] properties, ArmaControl control, boolean optional) {
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
		ControlPropertyInput propertyInput = getPropertyInputNode(c);
		propertyInputs.add(propertyInput);

		propertyInput.setIsOptional(optional);

		Node propertyInputNode = (Node) propertyInput;
		propertyInputNode.setDisable(ControlPropertyLookup.TYPE == c.getPropertyLookup());

		Hyperlink hyperReset = new Hyperlink(Lang.Popups.ControlPropertiesConfig.RESET);
		hyperReset.getStyleClass().add("hyper-link-fixed-color");
		hyperReset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				propertyInput.resetToDefault();
			}
		});

		pane.getChildren().addAll(hyperReset, new Label(c.getName()), propertyInputNode);

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

	private static void placeTooltip(Control c, ControlPropertyLookup lookup) {
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
		setTooltip(c, tooltip);
	}

	private static void setTooltip(Control c, String tooltip) {
		Tooltip tp = new Tooltip(tooltip);
		tp.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20));
		c.setTooltip(tp);
		FXUtil.hackTooltipStartTiming(tp, 0);
	}

	@Override
	protected void onCloseRequest(WindowEvent event) {
		super.onCloseRequest(event);
	}

	public ArmaControl getControl() {
		return control;
	}

	private interface ControlPropertyInput {

		/** Returns true if all data entered is valid, false if the data is not valid */
		boolean hasValidData();

		/** Get the control property associated with this input node */
		ControlProperty getControlProperty();

		/** Set the data to the default values */
		void resetToDefault();

		boolean isOptional();

		void setIsOptional(boolean optional);
	}

	/** Used for when a set amount of options are available (uses radio button group for option selecting) */
	private static class ControlPropertyOption extends FlowPane implements ControlPropertyInput {
		private final ControlProperty controlProperty;
		private ToggleGroup toggleGroup;

		ControlPropertyOption(ArmaControl control, ControlProperty controlProperty) {
			super(10, 5);
			this.controlProperty = controlProperty;
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			toggleGroup = new ToggleGroup();
			RadioButton radioButton, toSelect = null;
			boolean validData = controlProperty.valuesAreSet();
			for (Option option : lookup.options) {
				radioButton = new RadioButton(option.displayName);
				radioButton.setUserData(option.value);
				setTooltip(radioButton, option.description);
				radioButton.setToggleGroup(toggleGroup);
				getChildren().add(radioButton);
				if (validData && controlProperty.getStringValue().equals(option.value)) {
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
					control.getControlListener().updateValue(control);
				}
			});
			control.getControlListener().addValueListener(new ValueListener<ArmaControlClass>() {
				@Override
				public void valueUpdated(ArmaControlClass oldValue, ArmaControlClass newValue) {
					if (!controlProperty.valuesAreSet()) {
						return;
					}
					for (Toggle toggle : toggleGroup.getToggles()) {
						if (toggle.getUserData().equals(controlProperty.getStringValue())) {
							toggleGroup.selectToggle(toggle);
							return;
						}
					}
				}
			});
		}

		@Override
		public boolean hasValidData() {
			return controlProperty.valuesAreSet();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefault() {
			toggleGroup.selectToggle(null);
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

		private final ControlProperty controlProperty;

		ControlPropertyInputField(ArmaControl control, ControlProperty controlProperty, IInputFieldDataChecker checker, @Nullable String promptText) {
			super(checker);
			this.controlProperty = controlProperty;
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			if (controlProperty.valuesAreSet()) {
				setValueFromText(controlProperty.getStringValue());
			}
			getValueObserver().addValueListener(new ValueListener() {
				@Override
				public void valueUpdated(Object oldValue, Object newValue) {
					controlProperty.getValuesObserver().updateValue(new String[]{newValue.toString()});
					control.getControlListener().updateValue(control);
				}
			});
			placeTooltip(this, lookup);
			if (promptText != null) {
				setPromptText(promptText);
			}
			InputField myself = this;
			control.getControlListener().addValueListener(new ValueListener<ArmaControlClass>() {
				@Override
				public void valueUpdated(ArmaControlClass oldValue, ArmaControlClass newValue) {
					if (controlProperty.valuesAreSet()) {
						myself.setText(controlProperty.getStringValue());
					}
				}
			});
		}

		ControlPropertyInputField(ArmaControl control, ControlProperty controlProperty, IInputFieldDataChecker checker) {
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
			setText(controlProperty.getStringValue());
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

		private final ControlProperty controlProperty;

		ControlPropertyColorPicker(ArmaControl control, ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			boolean validData = controlProperty.valuesAreSet();
			if (validData) {
				setValue(AColor.toJavaFXColor(controlProperty.getValues()));
			}
			valueProperty().addListener(new ChangeListener<Color>() {
				@Override
				public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
					controlProperty.setValue(new AColor(newValue));
					control.getControlListener().updateValue(control);
				}
			});
			ColorPicker myself = this;
			control.getControlListener().addValueListener(new ValueListener<ArmaControlClass>() {
				@Override
				public void valueUpdated(ArmaControlClass oldValue, ArmaControlClass newValue) {
					if (controlProperty.valuesAreSet()) { //maybe wasn't updated
						myself.setValue(AColor.toJavaFXColor(controlProperty.getValues()));
					}
				}
			});
			placeTooltip(this, lookup);
		}

		@Override
		public boolean hasValidData() {
			return controlProperty.valuesAreSet();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefault() {
			setValue(AColor.toJavaFXColor(controlProperty.getDefaultValues()));
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

		private final ControlProperty controlProperty;

		ControlPropertyBooleanChoiceBox(ArmaControl control, ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
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
					control.getControlListener().updateValue(control);
				}
			});
			ChoiceBox<Boolean> myself = this;
			control.getControlListener().addValueListener(new ValueListener<ArmaControlClass>() {
				@Override
				public void valueUpdated(ArmaControlClass oldValue, ArmaControlClass newValue) {
					if (controlProperty.valuesAreSet()) {
						myself.setValue(controlProperty.getBooleanValue());
					}
				}
			});
			placeTooltip(this, lookup);
		}

		@Override
		public boolean hasValidData() {
			return controlProperty.valuesAreSet();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefault() {
			if (controlProperty.getDefaultValues()[0] == null) { //no intellij this is not always false
				getSelectionModel().clearSelection();
				return;
			}
			getSelectionModel().select(controlProperty.getBooleanValue());
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

		private final ControlProperty controlProperty;
		private ArrayList<InputField> fields = new ArrayList<>();

		ControlPropertyArrayInput(ArmaControl control, ControlProperty controlProperty, IInputFieldDataChecker... checkers) {
			super(5);
			this.controlProperty = controlProperty;
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			InputField<? extends IInputFieldDataChecker, String> inputField;
			boolean isDefined = controlProperty.valuesAreSet(); //no need to reiterate
			for (int i = 0; i < checkers.length; i++) {
				inputField = new InputField<>(checkers[i]);
				final int index = i;
				if (isDefined) {
					inputField.setValue(controlProperty.getStringValue());
				}

				inputField.getValueObserver().addValueListener(new ValueListener() {
					@Override
					public void valueUpdated(Object oldValue, Object newValue) {
						controlProperty.setValue(newValue.toString(), index);
						control.getControlListener().updateValue(control);
					}
				});
				controlProperty.getValuesObserver().addValueListener(new ValueListener<String[]>() {
					@Override
					public void valueUpdated(String[] oldValue, String[] newValue) {
						int i = 0;
						for (InputField field : fields) {
							field.setText(newValue[i++]);
						}
					}
				});
				fields.add(inputField);
				placeTooltip(inputField, lookup);
				getChildren().add(inputField);
			}
			control.getControlListener().addValueListener(new ValueListener<ArmaControlClass>() {
				@Override
				public void valueUpdated(ArmaControlClass oldValue, ArmaControlClass newValue) {
					if (controlProperty.valuesAreSet()) {
						for (int i = 0; i < fields.size(); i++) {
							fields.get(i).setValueFromText(controlProperty.getValues()[i]);
						}
					}
				}
			});
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
			for (int i = 0; i < fields.size(); i++) {
				fields.get(i).setText(controlProperty.getDefaultValues()[i]);
			}
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

		private final ControlProperty controlProperty;

		ControlPropertyFontChoiceBox(ArmaControl control, ControlProperty controlProperty) {
			this.controlProperty = controlProperty;
			ControlPropertyLookup lookup = controlProperty.getPropertyLookup();
			getItems().addAll(AFont.values());
			boolean validData = controlProperty.valuesAreSet();
			AFont font = AFont.DEFAULT;
			if (validData) {
				try {
					font = AFont.valueOf(controlProperty.getStringValue());
				} catch (IllegalArgumentException e) {
					e.printStackTrace(System.out);
				}
			}
			getSelectionModel().select(font);
			getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AFont>() {
				@Override
				public void changed(ObservableValue<? extends AFont> observable, AFont oldValue, AFont newValue) {
					controlProperty.setValue(newValue.name());
					control.getControlListener().updateValue(control);
				}
			});
			ChoiceBox<AFont> myself = this;
			control.getControlListener().addValueListener(new ValueListener<ArmaControlClass>() {
				@Override
				public void valueUpdated(ArmaControlClass oldValue, ArmaControlClass newValue) {
					myself.setValue(AFont.valueOf(controlProperty.getStringValue()));
				}
			});
			placeTooltip(this, lookup);
		}

		@Override
		public boolean hasValidData() {
			return controlProperty.valuesAreSet();
		}

		@Override
		public ControlProperty getControlProperty() {
			return controlProperty;
		}

		@Override
		public void resetToDefault() {
			getSelectionModel().select(AFont.DEFAULT);
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
