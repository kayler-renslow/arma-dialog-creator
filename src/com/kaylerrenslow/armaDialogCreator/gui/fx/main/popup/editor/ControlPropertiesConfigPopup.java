package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlPropertiesLookup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/31/2016.
 */
public class ControlPropertiesConfigPopup extends StagePopupUndecorated<ScrollPane> {
	private final ArmaControl control;

	public ControlPropertiesConfigPopup(@NotNull ArmaControl control) {
		super(ArmaDialogCreator.getPrimaryStage(), new ScrollPane(new Accordion()), null);
		this.control = control;
	}

	@Override
	public void show() {
		myRootElement.getStyleClass().add("rounded-node");
		Color bg = control.getRenderer().getBackgroundColor();
		control.getRenderer().getBackgroundColorObserver().addValueListener(new ValueListener<AColor>() {
			@Override
			public void valueUpdated(AColor oldValue, AColor newValue) {
				if (newValue != null) {
					setBorderColor(newValue.toJavaFXColor());
					control.getControlListener().updateValue(control);
				}
			}
		});
		setBorderColor(bg);
		myStage.initStyle(StageStyle.TRANSPARENT);
		Accordion accordion = (Accordion) myRootElement.getContent();
		myScene.setFill(Color.TRANSPARENT);
		final double padding = 20.0;
		myRootElement.setPadding(new Insets(padding));

		accordion.getPanes().add(getTitledPane("Required", control.getRequiredProperties()));
		accordion.getPanes().add(getTitledPane("Optional", control.getOptionalProperties()));
		//TODO inside lookup enum, have static method to retrieve all event properties

		accordion.setExpandedPane(accordion.getPanes().get(0));
		super.show();
	}

	private void setBorderColor(Color bg) {
		myRootElement.setStyle(String.format("-fx-border-color: rgba(%f%%,%f%%,%f%%,%f);", bg.getRed() * 100.0, bg.getGreen() * 100.0, bg.getBlue() * 100.0, bg.getOpacity()));
	}

	/** Get a titled pane for the accordion that holds all control properties */
	private TitledPane getTitledPane(String title, ControlProperty[] properties) {
		VBox vb = new VBox(10);
		TitledPane tp = new TitledPane(title, vb);
		tp.setAnimated(false);
		for (ControlProperty controlProperty : properties) {
			vb.getChildren().add(getControlPropertyEntry(controlProperty));
		}
		return tp;
	}

	/** Get the hbox that shows the name of the property as well as the controls to input data */
	private HBox getControlPropertyEntry(ControlProperty c) {
		HBox h = new HBox(10);
		h.setAlignment(Pos.TOP_LEFT);
		Node right = getPropertyInputNode(c);

		right.setDisable(ControlPropertiesLookup.TYPE == c.getPropertyLookup());
		h.getChildren().addAll(new Label(c.getName()), right);

		return h;
	}

	/** Get node that holds the controls to input data. */
	private Node getPropertyInputNode(ControlProperty controlProperty) {
		/*TODO set the values inside the controls from here*/
		ControlPropertiesLookup lookup = controlProperty.getPropertyLookup();
		if (lookup.options != null && lookup.options.length > 0) {
			return getNodeFromOptions(controlProperty);
		}
		ControlProperty.PropertyType propertyType = lookup.propertyType;
		switch (propertyType) {
			case INT:
				return getInputField(controlProperty, new IntegerFieldDataChecker());
			case FLOAT:
				return getInputField(controlProperty, new DoubleFieldDataChecker());
			case BOOLEAN:
				return getBooleanChoiceBox(controlProperty);
			case STRING:
				return getStringInputNode(controlProperty);
			case ARRAY:
				return getArrayInputNode(controlProperty);
			case COLOR:
				return getColorPicker(controlProperty);
			case SOUND:
				return getArrayInputNode(controlProperty, null, new IntegerFieldDataChecker(), new DoubleFieldDataChecker());
			case FONT:
				return getFontChoiceBox(controlProperty);
			case FILE_NAME:
				return getTextField(controlProperty, Lang.Popups.ControlPropertiesConfig.FILE_NAME);
			case IMAGE:
				return getTextField(controlProperty, Lang.Popups.ControlPropertiesConfig.IMAGE_PATH);
			case HEX_COLOR_STRING:
				return getColorPicker(controlProperty);
			case TEXTURE:
				return getTextField(controlProperty, Lang.Popups.ControlPropertiesConfig.TEXTURE);
			case EVENT:
				return getTextField(controlProperty, Lang.Popups.ControlPropertiesConfig.SQF_CODE);
			case SQF:
				return getTextField(controlProperty, Lang.Popups.ControlPropertiesConfig.SQF_CODE);
		}
		throw new IllegalStateException("Should have made a match");
	}

	private Control placeTooltip(Control c, ControlPropertiesLookup lookup) {
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
		return c;
	}

	private void setTooltip(Control c, String tooltip) {
		Tooltip tp = new Tooltip(tooltip);
		tp.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20));
		c.setTooltip(tp);
		FXUtil.hackTooltipStartTiming(tp, 0);
	}

	private Node getNodeFromOptions(ControlProperty controlProperty) {
		ControlPropertiesLookup lookup = controlProperty.getPropertyLookup();
		FlowPane pane = new FlowPane(10, 5);
		ToggleGroup tg = new ToggleGroup();
		RadioButton radioButton;
		for (Option option : lookup.options) {
			radioButton = new RadioButton(option.displayName);
			radioButton.setUserData(option.value);
			setTooltip(radioButton, option.description);
			radioButton.setToggleGroup(tg);
			pane.getChildren().add(radioButton);
		}
		tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				controlProperty.getValuesObserver().updateValue(new String[]{newValue.getUserData().toString()});
			}
		});
		return pane;
	}


	private Control getInputField(ControlProperty controlProperty, IInputFieldDataChecker c) {
		ControlPropertiesLookup lookup = controlProperty.getPropertyLookup();
		InputField inf = new InputField<>(c);
		inf.getValueObserver().addValueListener(new ValueListener() {
			@Override
			public void valueUpdated(Object oldValue, Object newValue) {
				controlProperty.getValuesObserver().updateValue(new String[]{newValue.toString()});
			}
		});
		return placeTooltip(inf, lookup);
	}

	private Control getColorPicker(ControlProperty controlProperty) {
		ControlPropertiesLookup lookup = controlProperty.getPropertyLookup();
		ColorPicker cp = new ColorPicker();
		cp.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				controlProperty.setValue(new AColor(newValue));
			}
		});
		return placeTooltip(cp, lookup);
	}

	private Control getBooleanChoiceBox(/*boolean initialValue*/ControlProperty controlProperty) {
		ControlPropertiesLookup lookup = controlProperty.getPropertyLookup();
		ChoiceBox<Boolean> cb = new ChoiceBox<>();
		cb.getItems().addAll(true, false);
		cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				controlProperty.setValue(newValue);
			}
		});
		//		cb.getSelectionModel().select(initialValue);
		return placeTooltip(cb, lookup);
	}

	private HBox getStringInputNode(ControlProperty controlProperty) {
		HBox hBox = new HBox();
		hBox.getChildren().addAll(new Label("\""), getTextField(controlProperty, ""), new Label("\""));

		return hBox;
	}

	private HBox getArrayInputNode(ControlProperty controlProperty) {
		//TODO add button that allows to insert a another field
		return getArrayInputNode(controlProperty, new StringFieldDataChecker(), new StringFieldDataChecker());
	}

	@SuppressWarnings("unchecked")
	private HBox getArrayInputNode(ControlProperty controlProperty, @NotNull IInputFieldDataChecker... checkers) {
		ControlPropertiesLookup lookup = controlProperty.getPropertyLookup();
		HBox hBox = new HBox(5);
		InputField<? extends IInputFieldDataChecker, String> tf;
		for (int i = 0; i < checkers.length; i++) {
			tf = new InputField<>(checkers[i]);
			final int index = i;

			tf.getValueObserver().addValueListener(new ValueListener() {
				@Override
				public void valueUpdated(Object oldValue, Object newValue) {
					controlProperty.setValue(newValue.toString(), index);
				}
			});

			hBox.getChildren().add(placeTooltip(tf, lookup));
		}
		return hBox;
	}

	private Control getFontChoiceBox(ControlProperty controlProperty) {
		ControlPropertiesLookup lookup = controlProperty.getPropertyLookup();
		ChoiceBox<AFont> cb = new ChoiceBox<>();
		cb.getItems().addAll(AFont.values());
		cb.getSelectionModel().select(AFont.PuristaMedium);
		cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AFont>() {
			@Override
			public void changed(ObservableValue<? extends AFont> observable, AFont oldValue, AFont newValue) {
				controlProperty.setValue(newValue.name());
			}
		});
		return placeTooltip(cb, lookup);
	}

	private Control getTextField(ControlProperty controlProperty, String promptText) {
		ControlPropertiesLookup lookup = controlProperty.getPropertyLookup();
		InputField<StringFieldDataChecker, String> inf = new InputField<>(new StringFieldDataChecker());
		inf.getValueObserver().addValueListener(new ValueListener<String>() {
			@Override
			public void valueUpdated(String oldValue, String newValue) {
				controlProperty.setValue(newValue);
			}
		});

		inf.setPromptText(promptText);

		return placeTooltip(inf, lookup);
	}

}
