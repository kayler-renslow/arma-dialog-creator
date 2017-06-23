package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVRaw;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValueConversionException;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.gui.popup.SimpleResponseDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
	private static final ControlPropertyDocumentationProvider lookupDocProvider = new ControlPropertyDocumentationProvider();

	private final ControlClass controlClass;
	private final ControlProperty controlProperty;

	private final StackPane stackPanePropertyInput = new StackPane();
	private final MenuButton menuButtonOptions = new MenuButton();
	private MenuItem inheritanceMenuItem;
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

		placeTooltip(controlClass, menuButtonOptions, currentValueEditor().getControlProperty().getPropertyLookup());

		final MenuItem miDefaultEditor = new MenuItem(bundle.getString("use_default_editor"));
		final MenuItem miResetToInitial = new MenuItem(bundle.getString("reset_to_initial"));
		final MenuItem miConvert = new MenuItem(bundle.getString("convert_value"));
		final MenuItem miMacro = new MenuItem(bundle.getString("set_to_macro"));
		inheritanceMenuItem = new MenuItem(
				controlProperty.isInherited() ? bundle.getString("override") :
						bundle.getString("inherit")
		);
		final MenuItem miClearValue = new MenuItem(bundle.getString("clear_value"));
		final CheckMenuItem miRaw = new CheckMenuItem(bundle.getString("raw"));
		menuButtonOptions.setText(controlProperty.getName());
		menuButtonOptions.getItems().setAll(
				miDefaultEditor,
				miConvert,
				miRaw,
				new SeparatorMenuItem(),
				miResetToInitial,
				miMacro,
				inheritanceMenuItem,
				miClearValue
		);

		if (SerializableValue.getTypesCanConvertTo(controlProperty.getInitialPropertyType()).size() == 1) {
			menuButtonOptions.getItems().remove(miConvert);
		}

		controlClassUpdateListener = new ControlClassUpdateListener(controlClass) {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, ControlClassUpdate data) {
				if (data instanceof ControlClassExtendUpdate) {
					ControlClassExtendUpdate update = (ControlClassExtendUpdate) data;
					inheritanceMenuItem.setVisible(update.getNewValue() != null);
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
			}
		}

		controlPropertyUpdateListener = new ControlPropertyUpdateListener(controlProperty) {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlPropertyUpdate> group, ControlPropertyUpdate data) {
				if (data instanceof ControlPropertyInheritUpdate) {
					ControlPropertyInheritUpdate update = (ControlPropertyInheritUpdate) data;

					boolean disable = update.wasInherited();
					stackPanePropertyInput.setDisable(disable);
					miDefaultEditor.setDisable(disable);
					miResetToInitial.setDisable(disable);
					miMacro.setDisable(disable);
					miClearValue.setDisable(disable);
					miRaw.setDisable(disable);
					miConvert.setDisable(disable || update.getControlProperty().getValue() == null);

					if (update.wasInherited()) {
						inheritanceMenuItem.setText(bundle.getString("override"));
					} else {
						inheritanceMenuItem.setText(bundle.getString("inherit"));
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
				PropertyType type;
				{
					ChooseNewPropertyTypeDialog dialog = new ChooseNewPropertyTypeDialog(controlProperty);
					dialog.show();
					type = dialog.getSelectedType();
				}
				if (type == null) {
					return;
				}
				SerializableValue value = controlProperty.getValue();
				if (value == null) {
					throw new IllegalStateException("shouldn't be able to convert a null value");
				}
				propertyValueEditor.clearListeners();
				try {
					controlProperty.setValue(
							SerializableValue.convert(ApplicationData.getManagerInstance(), value, type)
					);
				} catch (SerializableValueConversionException e) {
					ConvertValueDialog convertDialog = new ConvertValueDialog(value, type,
							ApplicationData.getManagerInstance().getGlobalExpressionEnvironment());
					convertDialog.show();
					SerializableValue newValue = convertDialog.getConvertedValue();
					if (!convertDialog.wasCancelled()) {
						controlProperty.setValue(newValue);
					}
				}
				updatePropertyValueEditor();
			}
		});
		inheritanceMenuItem.setOnAction(new EventHandler<ActionEvent>() {
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
				ResourceBundle appBundle = Lang.ApplicationBundle();
				dialog.getFooter().getBtnCancel().setText(appBundle.getString("Confirmation.no"));
				dialog.getFooter().getBtnOk().setText(appBundle.getString("Confirmation.yes"));
				dialog.setStageSize(300, 120);
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				getControlProperty().setValue((SerializableValue) null);
			}
		});
		miRaw.setSelected(controlProperty.getValue() instanceof SVRaw);
		miRaw.setOnAction(event -> {
			propertyValueEditor.clearListeners();
			if (miRaw.isSelected()) {
				String val = controlProperty.getValue() == null ? "" : controlProperty.getValue().toString();
				controlProperty.setValue(new SVRaw(val, controlProperty.getPropertyType()));
			} else {
				if (controlProperty.getValue() instanceof SVRaw) {
					SVRaw raw = (SVRaw) controlProperty.getValue();
					try {
						controlProperty.setValue(raw.newSubstituteTypeValue(ApplicationData.getManagerInstance()));
					} catch (Exception e) {
						PropertyType newType = raw.getSubstituteType() == null ?
								controlProperty.getInitialPropertyType() :
								raw.getSubstituteType();
						ConvertValueDialog dialog = new ConvertValueDialog(raw, newType,
								ApplicationData.getManagerInstance().getGlobalExpressionEnvironment()
						);
						dialog.show();
						SerializableValue newValue = dialog.getConvertedValue();
						if (dialog.wasCancelled()) {
							//set the menuItem's selected state back to its previous state
							miRaw.setSelected(true);
							return;
						}
						controlProperty.setValue(newValue);
					}
				} else {
					throw new IllegalStateException("control property should have raw value");
				}
			}
			updatePropertyValueEditor();
		});
		if (controlProperty.getPropertyLookup().getOptions() != null) {
			menuButtonOptions.getItems().remove(miRaw);
		}

		updateContainer();
	}

	private void updateContainer() {
		inheritanceMenuItem.setVisible(controlClass.getExtendClass() != null);
		if (getControlProperty().getMacro() != null) {
			updatePropertyInputMode(ControlPropertyValueEditor.EditMode.MACRO);
		} else {
			updatePropertyInputMode(ControlPropertyValueEditor.EditMode.DEFAULT);
		}
	}

	@SuppressWarnings("unchecked")
	private void updatePropertyInputMode(ControlPropertyValueEditor.EditMode mode) {
		if (mode == ControlPropertyValueEditor.EditMode.MACRO) {
			stackPanePropertyInput.getChildren().clear();

			MacroGetterButton<? extends SerializableValue> macroGetterButton = new MacroGetterButton(currentValueEditor().getMacroPropertyType(), currentValueEditor().getControlProperty().getMacro());

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
		HBox.setHgrow(stackPanePropertyInput, propertyValueEditor.displayFullWidth() ?
				Priority.ALWAYS : Priority.NEVER
		);
		stackPanePropertyInput.getChildren().add(propertyValueEditor.getRootNode());
		if (controlProperty.getValue() instanceof SVRaw) {
			stackPanePropertyInput.setPadding(new Insets(1));
			stackPanePropertyInput.setBorder(
					new Border(
							new BorderStroke(
									Color.DARKORANGE,
									BorderStrokeStyle.DASHED,
									CornerRadii.EMPTY,
									BorderStroke.THIN
							)
					)
			);
		} else {
			stackPanePropertyInput.setBorder(null);
			stackPanePropertyInput.setPadding(Insets.EMPTY);
		}
	}

	/** Get node that holds the controls to input data. */
	@NotNull
	private ControlPropertyValueEditor constructNewPropertyValueEditor() {
		ControlPropertyLookupConstant lookup = controlProperty.getPropertyLookup();
		if (lookup.getOptions() != null && lookup.getOptions().length > 0) {
			return new ControlPropertyOptionEditor(controlClass, controlProperty);
		}
		PropertyType propertyType = controlProperty.getPropertyType() == null ? controlProperty.getInitialPropertyType() : controlProperty.getPropertyType();
		switch (propertyType) {
			case Int:
				return new IntegerEditor(controlClass, controlProperty);
			case Float:
				return new FloatEditor(controlClass, controlProperty);
			case ControlStyle:
				return new ControlStyleEditor(controlClass, controlProperty);
			case Boolean:
				return new BooleanChoiceBoxEditor(controlClass, controlProperty);
			case String:
				return new StringEditor(controlClass, controlProperty);
			case Array:
				return new ArrayEditor(controlClass, controlProperty, 2);
			case Color:
				return new ColorArrayEditor(controlClass, controlProperty);
			case Sound:
				return new SoundEditor(controlClass, controlProperty);
			case Font:
				return new FontChoiceBoxEditor(controlClass, controlProperty);
			case FileName:
				return new StringEditor(controlClass, controlProperty);
			case Image:
				return new ImageEditor(controlClass, controlProperty);
			case HexColorString:
				return new HexColorEditor(controlClass, controlProperty);
			case Texture:
				return new StringEditor(controlClass, controlProperty);
			case SQF:
				return new StringEditor(controlClass, controlProperty);
			case Raw:
				return new RawEditor(controlClass, controlProperty);
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

	static Tooltip getTooltip(@NotNull ControlClass controlClass, @NotNull ControlPropertyLookupConstant lookup) {
		ControlType type = null;
		{
			ControlProperty prop = controlClass.findPropertyByNameNullable("type");
			if (prop != null && prop.getInitialPropertyType() == PropertyType.Int) {
				try {
					type = ControlType.findById(prop.getIntValue());
				} catch (IllegalArgumentException | NullPointerException ignore) {

				}
			}
		}
		Tooltip tp = new Tooltip(lookupDocProvider.getDocumentation(lookup, type));
		tp.setFont(TOOLTIP_FONT);
		return tp;
	}

	/**
	 Places tooltip on n

	 @param controlClass used for getting a {@link ControlType}, which assists in getting documentation
	 @param n Node to place tooltip on
	 @param lookup constant to create tooltip of
	 */
	static void placeTooltip(@NotNull ControlClass controlClass, @NotNull Node n, @NotNull ControlPropertyLookupConstant lookup) {
		Tooltip tip = getTooltip(controlClass, lookup);
		Tooltip.install(n, tip);
	}


	private static class ChooseNewPropertyTypeDialog extends StageDialog<VBox> {

		private final ComboBox<PropertyType> comboBoxType = new ComboBox<>();

		public ChooseNewPropertyTypeDialog(@NotNull ControlProperty property) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), bundle.getString("ConvertValueDialog.popup_title"), true, true, true);

			Label lbl = new Label(String.format(bundle.getString("ConvertValueDialog.body_f"), property.getName()));
			lbl.setWrapText(true);
			myRootElement.getChildren().add(lbl);

			PropertyType fromType;
			if (property.getValue() == null) {
				fromType = property.getInitialPropertyType();
			} else {
				fromType = property.getPropertyType();
			}

			for (PropertyType type : PropertyType.values()) {
				if (SerializableValue.isConvertible(fromType, type)) {
					comboBoxType.getItems().add(type);
				}
			}
			myRootElement.getChildren().add(comboBoxType);
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
