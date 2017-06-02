package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValueConversionException;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.gui.popup.SimpleResponseDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

import static com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor.ControlPropertyValueEditors.*;

/**
 @author Kayler
 @since 11/20/2016 */
class ControlPropertyEditorContainer extends HBox {
	private static final Font TOOLTIP_FONT = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20d);

	private static final ResourceBundle bundle = Lang.getBundle("ControlPropertyEditorBundle");

	private final ControlClass controlClass;
	private final ControlProperty controlProperty;

	private final StackPane stackPanePropertyInput = new StackPane();
	private final MenuButton menuButtonOptions = new MenuButton();
	private MenuItem miInheritanceButton;
	private ControlPropertyValueEditor propertyValueEditor;
	private ControlPropertyUpdateListener controlPropertyUpdateListener;
	private ControlClassUpdateListener controlClassUpdateListener;

	private boolean hideIfInherited;

	public ControlPropertyEditorContainer(@NotNull ControlClass controlClass, @NotNull ControlProperty property) {
		super(5);
		this.controlClass = controlClass;
		this.controlProperty = property;

		updatePropertyValueEditor();

		setAlignment(Pos.TOP_LEFT);
		init();
	}

	private void init() {
		currentValueEditor().disableEditing(controlProperty.getPropertyLookup() == ControlPropertyLookup.TYPE);

		placeTooltip(menuButtonOptions, currentValueEditor().getControlProperty().getPropertyLookup());

		final MenuItem miDefaultEditor = new MenuItem(bundle.getString("use_default_editor"));
		final MenuItem miResetToInitial = new MenuItem(bundle.getString("reset_to_initial"));
		final MenuItem miConvert = new MenuItem(bundle.getString("convert_value"));
		final MenuItem miMacro = new MenuItem(bundle.getString("set_to_macro"));
		final MenuItem miCustomData = new MenuItem(bundle.getString("value_custom_data"));//broken. Maybe fix it later. Don't delete this in case you change your mind
		miInheritanceButton = new MenuItem(
				controlProperty.isInherited() ? bundle.getString("override") :
						bundle.getString("inherit")
		);
		final MenuItem miClearValue = new MenuItem(bundle.getString("clear_value"));
		menuButtonOptions.setText(controlProperty.getName());
		menuButtonOptions.getItems().setAll(
				miDefaultEditor,
				miConvert,
				new SeparatorMenuItem(),
				miResetToInitial,
				miMacro,
				miInheritanceButton,
				miClearValue
				/*,miCustomData*/
		);


		controlClassUpdateListener = new ControlClassUpdateListener(controlClass) {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, ControlClassUpdate data) {
				if (data instanceof ControlClassExtendUpdate) {
					ControlClassExtendUpdate update = (ControlClassExtendUpdate) data;
					miInheritanceButton.setVisible(update.getNewValue() != null);
				}
			}
		};
		controlClass.getControlClassUpdateGroup().addListener(controlClassUpdateListener);

		if (controlProperty.getPropertyLookup() instanceof ControlPropertyLookup) {
			switch ((ControlPropertyLookup) controlProperty.getPropertyLookup()) {
				case TYPE: {
					for (MenuItem item : menuButtonOptions.getItems()) {
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
					miCustomData.setDisable(true);//NEVER allow custom input
					break;
				}
			}
		}

		controlPropertyUpdateListener = new ControlPropertyUpdateListener(controlProperty) {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlPropertyUpdate> group, ControlPropertyUpdate data) {
				if (data instanceof ControlPropertyInheritUpdate) {
					ControlPropertyInheritUpdate update = (ControlPropertyInheritUpdate) data;

					boolean disable = update.wasInherited();
					stackPanePropertyInput.setDisable(disable);
					miCustomData.setDisable(disable);
					miDefaultEditor.setDisable(disable);
					miResetToInitial.setDisable(disable);
					miMacro.setDisable(disable);
					miClearValue.setDisable(disable);
					miConvert.setDisable(disable || update.getControlProperty().getValue() == null);

					if (update.wasInherited()) {
						miInheritanceButton.setText(bundle.getString("override"));
					} else {
						miInheritanceButton.setText(bundle.getString("inherit"));
					}
					hideIfInherited(ControlPropertyEditorContainer.this.hideIfInherited);
				} else if (data instanceof ControlPropertyMacroUpdate) {
					ControlPropertyMacroUpdate macroUpdate = (ControlPropertyMacroUpdate) data;
					updatePropertyInputMode(macroUpdate.getNewMacro() != null ? ControlPropertyValueEditor.EditMode.MACRO : ControlPropertyValueEditor.EditMode.DEFAULT);
				}
			}
		};
		controlProperty.getControlPropertyUpdateGroup().addListener(controlPropertyUpdateListener);

		getChildren().addAll(menuButtonOptions, new Label("="), stackPanePropertyInput);

		miResetToInitial.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getControlProperty().setValue(getControlProperty().getDefaultValue());
			}
		});
		miDefaultEditor.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updatePropertyInputMode(ControlPropertyValueEditor.EditMode.DEFAULT);
			}
		});
		miMacro.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updatePropertyInputMode(ControlPropertyValueEditor.EditMode.MACRO);
			}
		});
		miConvert.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ChooseNewPropertyTypeDialog dialog = new ChooseNewPropertyTypeDialog(controlProperty);
				dialog.show();
				PropertyType type = dialog.getSelectedType();
				if (type == null) {
					return;
				}
				if (controlProperty.getValue() == null) {
					throw new IllegalStateException("shouldn't be able to convert a null value");
				}
				try {
					propertyValueEditor.clearListeners();
					controlProperty.setValue(SerializableValue.convert(ApplicationData.getManagerInstance(), controlProperty.getValue(), type));
					updatePropertyValueEditor();
				} catch (SerializableValueConversionException e) {
					ExceptionHandler.error(e);
				}
			}
		});
		miCustomData.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updatePropertyInputMode(ControlPropertyValueEditor.EditMode.CUSTOM_DATA);
			}
		});
		miInheritanceButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (getControlProperty().isInherited()) {
					getControlProperty().inherit(null);
				} else {
					controlClass.inheritProperty(getControlProperty().getPropertyLookup());
				}
			}
		});
		miClearValue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (getControlProperty().getValue() == null) {
					return;
				}
				if (getControlProperty().getMacro() != null) {
					if (!askClearMacro()) {
						return;
					}
				}

				SimpleResponseDialog dialog = new SimpleResponseDialog(
						ArmaDialogCreator.getPrimaryStage(),
						bundle.getString("ClearValuePopup.popup_title"),
						bundle.getString("ClearValuePopup.body"), true, true, false
				);
				dialog.getFooter().getBtnCancel().setText(bundle.getString("Confirmation.no"));
				dialog.getFooter().getBtnOk().setText(bundle.getString("Confirmation.yes"));
				dialog.setStageSize(300, 120);
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				getControlProperty().setValue((SerializableValue) null);
			}
		});

		updateContainer();
	}

	private void updateContainer() {
		miInheritanceButton.setVisible(controlClass.getExtendClass() != null);
		if (getControlProperty().isUsingCustomData()) {
			updatePropertyInputMode(ControlPropertyValueEditor.EditMode.CUSTOM_DATA);
		} else if (getControlProperty().getMacro() != null) {
			updatePropertyInputMode(ControlPropertyValueEditor.EditMode.MACRO);
		}
	}

	@SuppressWarnings("unchecked")
	private void updatePropertyInputMode(ControlPropertyValueEditor.EditMode mode) {
		if (mode == ControlPropertyValueEditor.EditMode.MACRO) {
			stackPanePropertyInput.getChildren().clear();

			MacroGetterButton<? extends SerializableValue> macroGetterButton = new MacroGetterButton(currentValueEditor().getMacroClass(), currentValueEditor().getControlProperty().getMacro());

			macroGetterButton.getChosenMacroValueObserver().updateValue(getControlProperty().getMacro());

			stackPanePropertyInput.getChildren().add(macroGetterButton);
			macroGetterButton.getChosenMacroValueObserver().addListener(new ValueListener() {
				@Override
				public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
					Macro m = (Macro) newValue;
					currentValueEditor().getControlProperty().setValueToMacro(m);
				}
			});
		} else {
			if (currentValueEditor().getControlProperty().getMacro() != null) {
				if (!askClearMacro()) {
					return;
				}
				currentValueEditor().getControlProperty().setValueToMacro(null);
			}
			stackPanePropertyInput.getChildren().clear();
			stackPanePropertyInput.getChildren().add(currentValueEditor().getRootNode());
			currentValueEditor().setToMode(mode);
			currentValueEditor().getControlProperty().setUsingCustomData(mode == ControlPropertyValueEditor.EditMode.CUSTOM_DATA);
		}
	}

	private boolean askClearMacro() {
		SimpleResponseDialog dialog = new SimpleResponseDialog(
				ArmaDialogCreator.getPrimaryStage(),
				bundle.getString("RemoveMacroDialog.dialog_title"),
				bundle.getString("RemoveMacroDialog.body"),
				true, true, false
		);
		dialog.getFooter().getBtnCancel().setText(bundle.getString("Confirmation.no"));
		dialog.getFooter().getBtnOk().setText(bundle.getString("Confirmation.yes"));
		dialog.show();
		return !dialog.wasCancelled();
	}

	public void unlink() {
		propertyValueEditor.clearListeners();
		controlClass.getControlClassUpdateGroup().removeListener(this.controlClassUpdateListener);
		getControlProperty().getControlPropertyUpdateGroup().removeListener(this.controlPropertyUpdateListener);
	}

	public void link() {
		propertyValueEditor.initListeners();
		propertyValueEditor.refresh();
		controlClass.getControlClassUpdateGroup().addListener(this.controlClassUpdateListener);
		getControlProperty().getControlPropertyUpdateGroup().addListener(this.controlPropertyUpdateListener);
		updateContainer();
	}

	@NotNull
	public ControlPropertyValueEditor currentValueEditor() {
		return propertyValueEditor;
	}

	protected void updatePropertyValueEditor() {
		stackPanePropertyInput.getChildren().clear();
		propertyValueEditor = constructNewPropertyValueEditor();
		if (propertyValueEditor.displayFullWidth()) {
			HBox.setHgrow(stackPanePropertyInput, Priority.ALWAYS);
		}
		stackPanePropertyInput.getChildren().add(propertyValueEditor.getRootNode());

	}

	/** Get node that holds the controls to input data. */
	@NotNull
	private ControlPropertyValueEditor constructNewPropertyValueEditor() {
		ControlPropertyLookupConstant lookup = controlProperty.getPropertyLookup();
		if (lookup.getOptions() != null && lookup.getOptions().length > 0) {
			return new ControlPropertyInputOption(controlClass, controlProperty);
		}
		PropertyType propertyType = controlProperty.getPropertyType() == null ? controlProperty.getInitialPropertyType() : controlProperty.getPropertyType();
		switch (propertyType) {
			case Int:
				return new ControlPropertyInputFieldInteger(controlClass, controlProperty);
			case Float:
				return new ControlPropertyInputFieldDouble(controlClass, controlProperty);
			case ControlStyle:
				return new ControlStylePropertyInput(controlClass, controlProperty);
			case Boolean:
				return new ControlPropertyBooleanChoiceBox(controlClass, controlProperty);
			case String:
				return new ControlPropertyInputFieldString(controlClass, controlProperty);
			case Array:
				return new ControlPropertyArrayInput(controlClass, controlProperty, 2);
			case Color:
				return new ControlPropertyColorPicker(controlClass, controlProperty);
			case Sound:
				return new ControlPropertySoundInput(controlClass, controlProperty);
			case Font:
				return new ControlPropertyFontChoiceBox(controlClass, controlProperty);
			case FileName:
				return new ControlPropertyInputFieldString(controlClass, controlProperty);
			case Image:
				return new ControlPropertyInputFieldString(controlClass, controlProperty); //todo use proper value editor
			case HexColorString:
				return new ControlPropertyColorPicker(controlClass, controlProperty); //todo have hex color editor (or maybe just take the AColor value instance and create a AHexColor instance from it)
			case Texture:
				return new ControlPropertyInputFieldString(controlClass, controlProperty);
			case SQF:
				return new ControlPropertyInputFieldString(controlClass, controlProperty);
		}
		throw new IllegalStateException("Should have made a match");
	}

	@NotNull
	public ControlProperty getControlProperty() {
		return controlProperty;
	}

	public void hideIfInherited(boolean hide) {
		this.hideIfInherited = hide;
		boolean visible = !(hide && getControlProperty().isInherited());
		setVisible(visible);
		setManaged(visible);
	}

	static Tooltip getTooltip(@NotNull ControlPropertyLookupConstant lookup) {
		Tooltip tp = new Tooltip(lookup.getAbout());
		tp.setFont(TOOLTIP_FONT);
		return tp;
	}

	/**
	 Places tooltip on n

	 @param n Node to place tooltip on
	 @param lookup constant to create tooltip of
	 */
	static void placeTooltip(Node n, ControlPropertyLookupConstant lookup) {
		Tooltip tip = getTooltip(lookup);
		Tooltip.install(n, tip);
	}


	private static class ChooseNewPropertyTypeDialog extends StageDialog<VBox> {

		private final ComboBox<PropertyType> comboBoxType = new ComboBox<>();

		public ChooseNewPropertyTypeDialog(@NotNull ControlProperty property) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), bundle.getString("ConvertValueDialog.popup_title"), true, true, true);

			Label lbl = new Label(String.format(bundle.getString("ConvertValueDialog.body_f"), property.getName()));
			lbl.setWrapText(true);
			myRootElement.getChildren().add(lbl);

			myRootElement.getChildren().add(comboBoxType);

			for (PropertyType type : PropertyType.values()) {
				comboBoxType.getItems().add(type);
			}
			comboBoxType.getSelectionModel().select(property.getPropertyType());

			Button btnUseInitial = new Button(bundle.getString("ConvertValueDialog.use_initial"));
			btnUseInitial.setOnAction((e) -> comboBoxType.getSelectionModel().select(property.getInitialPropertyType()));
			myRootElement.getChildren().add(btnUseInitial);
			btnUseInitial.setTooltip(new Tooltip(bundle.getString("ConvertValueDialog.use_initial_tooltip")));


			setStageSize(420, 180);
			setResizable(false);
		}

		@Nullable
		public PropertyType getSelectedType() {
			return wasCancelled() ? null : comboBoxType.getValue();
		}
	}

}
