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
import com.kaylerrenslow.armaDialogCreator.util.*;
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

	private static final ResourceBundle bundle = Lang.ApplicationBundle();

	private final ControlClass controlClass;
	private final ControlProperty controlProperty;

	private final StackPane stackPanePropertyInput = new StackPane();
	private final MenuButton menuButtonOptions = new MenuButton();
	private final PropertyTypeListener propertyTypeListener;
	private MenuItem miInheritanceButton;
	private ControlPropertyValueEditor propertyValueEditor;
	private ControlPropertyUpdateListener controlPropertyUpdateListener;
	private ControlClassUpdateListener controlClassUpdateListener;

	private boolean hideIfInherited;

	public ControlPropertyEditorContainer(@NotNull ControlClass controlClass, @NotNull ControlProperty property) {
		super(5);
		this.controlClass = controlClass;
		this.controlProperty = property;

		setCurrentPropertyValueEditor(controlProperty.getPropertyType(), false);

		this.propertyTypeListener = new PropertyTypeListener(controlProperty, this);
		controlProperty.getReadOnlyPropertyTypeObserver().addListener(propertyTypeListener);

		setAlignment(Pos.TOP_LEFT);
		init();
	}

	private void init() {
		currentValueEditor().disableEditing(controlProperty.getPropertyLookup() == ControlPropertyLookup.TYPE);

		placeTooltip(menuButtonOptions, currentValueEditor().getControlProperty().getPropertyLookup());

		final MenuItem miDefaultEditor = new MenuItem(bundle.getString("ControlPropertiesEditorPane.use_default_editor"));
		final MenuItem miResetToInitial = new MenuItem(bundle.getString("ControlPropertiesEditorPane.reset_to_initial"));
		final MenuItem miConvert = new MenuItem(bundle.getString("ControlPropertiesEditorPane.convert_value"));
		final MenuItem miMacro = new MenuItem(bundle.getString("ControlPropertiesEditorPane.set_to_macro"));
		final MenuItem miCustomData = new MenuItem(bundle.getString("ControlPropertiesEditorPane.value_custom_data"));//broken. Maybe fix it later. Don't delete this in case you change your mind
		miInheritanceButton = new MenuItem(
				controlProperty.isInherited() ? bundle.getString("ControlPropertiesEditorPane.override") :
						bundle.getString("ControlPropertiesEditorPane.inherit")
		);
		final MenuItem miClearValue = new MenuItem(bundle.getString("ControlPropertiesEditorPane.clear_value"));
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
					miConvert.setDisable(disable);

					if (update.wasInherited()) {
						miInheritanceButton.setText(bundle.getString("ControlPropertiesEditorPane.override"));
					} else {
						miInheritanceButton.setText(bundle.getString("ControlPropertiesEditorPane.inherit"));
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
				controlProperty.setPropertyType(type);
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
						bundle.getString("ControlPropertiesEditorPane.ClearValuePopup.popup_title"),
						bundle.getString("ControlPropertiesEditorPane.ClearValuePopup.body"), true, true, false
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
				bundle.getString("ControlPropertiesEditorPane.RemoveMacroDialog.dialog_title"),
				bundle.getString("ControlPropertiesEditorPane.RemoveMacroDialog.body"),
				true, true, false
		);
		dialog.getFooter().getBtnCancel().setText(bundle.getString("Confirmation.no"));
		dialog.getFooter().getBtnOk().setText(bundle.getString("Confirmation.yes"));
		dialog.show();
		return !dialog.wasCancelled();
	}

	public void unlink() {
		propertyValueEditor.clearListeners();
		propertyTypeListener.unlink();
		controlClass.getControlClassUpdateGroup().removeListener(this.controlClassUpdateListener);
		getControlProperty().getControlPropertyUpdateGroup().removeListener(this.controlPropertyUpdateListener);
	}

	public void link() {
		propertyValueEditor.initListeners();
		propertyTypeListener.link();
		propertyValueEditor.refresh();
		controlClass.getControlClassUpdateGroup().addListener(this.controlClassUpdateListener);
		getControlProperty().getControlPropertyUpdateGroup().addListener(this.controlPropertyUpdateListener);
		updateContainer();
	}

	@NotNull
	public ControlPropertyValueEditor currentValueEditor() {
		return propertyValueEditor;
	}

	protected void setCurrentPropertyValueEditor(@NotNull PropertyType propertyType, boolean convertType) {
		if (propertyValueEditor != null) {
			propertyValueEditor.clearListeners();
			stackPanePropertyInput.getChildren().clear();
		}

		if (controlProperty.getValue() != null && convertType) {
			try {
				controlProperty.setValue(SerializableValue.convert(ApplicationData.getInstance(), controlProperty.getValue(), propertyType));
			} catch (SerializableValueConversionException e) {
				ExceptionHandler.error(e);
			}
		}

		propertyValueEditor = constructNewPropertyValueEditor(propertyType);
		if (propertyValueEditor.displayFullWidth()) {
			HBox.setHgrow(stackPanePropertyInput, Priority.ALWAYS);
		}
		stackPanePropertyInput.getChildren().add(propertyValueEditor.getRootNode());

	}

	/** Get node that holds the controls to input data. */
	private ControlPropertyValueEditor constructNewPropertyValueEditor(@NotNull PropertyType propertyType) {
		ControlPropertyLookupConstant lookup = controlProperty.getPropertyLookup();
		if (lookup.getOptions() != null && lookup.getOptions().length > 0) {
			return new ControlPropertyInputOption(controlClass, controlProperty);
		}
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
	 @return tooltip placed inside n
	 */
	static Tooltip placeTooltip(Node n, ControlPropertyLookupConstant lookup) {
		Tooltip tip = getTooltip(lookup);
		Tooltip.install(n, tip);
		return tip;
	}

	private static class PropertyTypeListener implements ReadOnlyValueListener<PropertyType> {

		private final ControlProperty property;
		private final ControlPropertyEditorContainer editorContainer;

		public PropertyTypeListener(@NotNull ControlProperty property, @NotNull ControlPropertyEditorContainer editorContainer) {
			this.property = property;
			this.editorContainer = editorContainer;
		}

		public void link() {
			property.getReadOnlyPropertyTypeObserver().addListener(this);
		}

		public void unlink() {
			property.getReadOnlyPropertyTypeObserver().removeListener(this);
		}

		@Override
		public void valueUpdated(@NotNull ReadOnlyValueObserver<PropertyType> observer, @Nullable PropertyType oldValue, @Nullable PropertyType newValue) {
			editorContainer.setCurrentPropertyValueEditor(newValue, true);
		}
	}

	private static class ChooseNewPropertyTypeDialog extends StageDialog<VBox> {

		private final ComboBox<PropertyType> comboBoxType = new ComboBox<>();

		public ChooseNewPropertyTypeDialog(@NotNull ControlProperty property) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), bundle.getString("ControlPropertiesEditorPane.ConvertValueDialog.popup_title"), true, true, true);

			Label lbl = new Label(String.format(bundle.getString("ControlPropertiesEditorPane.ConvertValueDialog.body_f"), property.getName()));
			lbl.setWrapText(true);
			myRootElement.getChildren().add(lbl);

			myRootElement.getChildren().add(comboBoxType);

			for (PropertyType type : PropertyType.values()) {
				if (SerializableValue.isConvertible(type, property.getPropertyType())) {
					comboBoxType.getItems().add(type);
				}
			}
			comboBoxType.getSelectionModel().select(property.getPropertyType());

			Button btnUseInitial = new Button(bundle.getString("ControlPropertiesEditorPane.ConvertValueDialog.use_initial"));
			btnUseInitial.setOnAction((e) -> comboBoxType.getSelectionModel().select(property.getInitialPropertyType()));
			myRootElement.getChildren().add(btnUseInitial);
			btnUseInitial.setTooltip(new Tooltip(bundle.getString("ControlPropertiesEditorPane.ConvertValueDialog.use_initial_tooltip")));


			setStageSize(420, 180);
			setResizable(false);
		}

		@Nullable
		public PropertyType getSelectedType() {
			return wasCancelled() ? null : comboBoxType.getValue();
		}
	}

}
