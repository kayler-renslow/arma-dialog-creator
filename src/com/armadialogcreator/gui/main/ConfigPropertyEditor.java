package com.armadialogcreator.gui.main;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.core.*;
import com.armadialogcreator.core.sv.SVRaw;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.core.sv.SerializableValueConversionException;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.gui.SimpleResponseDialog;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.main.sveditor.ConvertValueDialog;
import com.armadialogcreator.gui.main.sveditor.MacroGetterButton;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.armadialogcreator.gui.main.ConfigPropertyValueEditors.*;

/**
 A type of {@link ConfigPropertyDisplayBox} in which the user can edit an existing {@link ConfigProperty}.
 This editor allows for changing the {@link ConfigProperty#getBoundMacro()},
 whether a {@link ConfigProperty} is inherited ({@link ConfigClass#propertyIsInherited(String)}),
 and features specialized value editors for the {@link ConfigProperty#getValue()}.

 @author Kayler
 @since 11/20/2016
 @see ConfigPropertyValueEditor*/
public class ConfigPropertyEditor extends ConfigPropertyDisplayBox {
	private enum EditMode {
		MACRO, DEFAULT
	}
	private static final ResourceBundle bundle = Lang.getBundle("ConfigPropertyEditorBundle");

	private final ConfigProperty configProperty;
	private boolean propertyIsInherited;

	private MenuItem inheritanceMenuItem;
	private ConfigPropertyValueEditor propertyValueEditor;
	private UpdateGroupListener<ConfigPropertyUpdate> configPropertyUpdateListener;
	private UpdateGroupListener<ConfigClassUpdate> configClassUpdateListener;

	private boolean hideIfInherited;
	private Node stackPaneTint;
	private Consumer<Object> propertyInheritUpdate;
	private EditMode currentEditMode;

	public ConfigPropertyEditor(@NotNull ConfigClass configClass, @NotNull ConfigProperty property) {
		super(configClass, property);
		this.configClass = configClass;
		this.configProperty = property;

		resetPropertyValueEditor();
		init();
	}

	private void init() {
		final MenuItem miDisplayType = new MenuItem(bundle.getString("display_type"));
		final MenuItem miDefaultEditor = new MenuItem(bundle.getString("use_default_editor"));
		final MenuItem miResetToInitial = new MenuItem(bundle.getString("reset_to_initial"));
		final MenuItem miConvert = new MenuItem(bundle.getString("convert_value"));
		final MenuItem miMacro = new MenuItem(bundle.getString("set_to_macro"));
		inheritanceMenuItem = new MenuItem("inheritanceMenuItem");
		final MenuItem miRemove = new MenuItem(bundle.getString("remove"));
		final CheckMenuItem miRaw = new CheckMenuItem(bundle.getString("raw"));
		final MenuItem miShowDoc = new MenuItem(bundle.getString("show_documentation"));
		miShowDoc.setOnAction(event -> {
			new MenuButtonPopup(TextUtil.getMultilineText(getDocumentation(), 60)).showPopup();
		});

		menuButtonOptions.setText(configProperty.getName());
		menuButtonOptions.getItems().setAll(
				miShowDoc,
				miDisplayType,
				miDefaultEditor,
				new SeparatorMenuItem(),
				miResetToInitial,
				miMacro,
				miConvert,
				inheritanceMenuItem,
				new SeparatorMenuItem(),
				miRaw,
				miRemove
		);

		if (SerializableValue.getTypesCanConvertTo(configProperty.getPropertyType()).size() == 1) {
			menuButtonOptions.getItems().remove(miConvert);
		}

		configClassUpdateListener = new UpdateGroupListener<>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ConfigClassUpdate> group, @NotNull ConfigClassUpdate data) {
				ConfigClassUpdate.Type type = data.getType();
				switch (type) {
					case ExtendClass: {
						ConfigClassUpdate.ExtendClassUpdate update = (ConfigClassUpdate.ExtendClassUpdate) data;
						inheritanceMenuItem.setVisible(update.getNewExtendClass() != null);
						break;
					}
					case InheritProperty: {
						ConfigClassUpdate.InheritPropertyUpdate update = (ConfigClassUpdate.InheritPropertyUpdate) data;

						if (configProperty.getName().equals(update.getInheritedPropertyName())) {
							propertyIsInherited = true;
							propertyInheritUpdate.accept(null);
						}
						break;
					}
				}
			}
		};
		configClass.getClassUpdateGroup().addListener(configClassUpdateListener);

		this.propertyInheritUpdate = (v) -> {
			updateContainerInheritanceTint();
			contentStackPane.setDisable(propertyIsInherited);
			miDefaultEditor.setDisable(propertyIsInherited);
			miResetToInitial.setDisable(propertyIsInherited);
			miMacro.setDisable(propertyIsInherited);
			miRemove.setDisable(propertyIsInherited);
			miRaw.setDisable(propertyIsInherited);
			miConvert.setDisable(propertyIsInherited);

			if (propertyIsInherited) {
				inheritanceMenuItem.setText(bundle.getString("override"));
			} else {
				inheritanceMenuItem.setText(bundle.getString("inherit"));
			}
			hideIfInherited(ConfigPropertyEditor.this.hideIfInherited);
		};

		configPropertyUpdateListener = new UpdateGroupListener<>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ConfigPropertyUpdate> group, @NotNull ConfigPropertyUpdate data) {
				switch (data.getType()) {
					case Macro: {
						updateEditMode(EditMode.MACRO);
						break;
					}
					case Value: {
						ConfigPropertyUpdate.ValueUpdate update = (ConfigPropertyUpdate.ValueUpdate) data;
						if (propertyValueEditor.getAcceptedPropertyType() != update.getNewValue().getPropertyType()) {
							resetPropertyValueEditor();
						}
						updateEditMode(EditMode.DEFAULT);
						break;
					}
				}
			}
		};
		configProperty.getPropertyUpdateGroup().addListener(configPropertyUpdateListener);

		miDisplayType.setOnAction(event -> {
			SerializableValue sv = configProperty.getValue();
			MenuButtonPopup popup = new MenuButtonPopup(
					String.format(bundle.getString("type_f"), sv.getPropertyType().getDisplayName())
			);
			popup.showPopup();
		});
		miResetToInitial.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				throw new UnsupportedOperationException();
				//				configProperty.setValue(configProperty.getDefaultValue());
			}
		});
		miDefaultEditor.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				updateEditMode(EditMode.DEFAULT);
			}
		});
		miMacro.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				updateEditMode(EditMode.MACRO);
			}
		});
		miConvert.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				PropertyType type;
				{
					ChooseNewPropertyTypeDialog dialog = new ChooseNewPropertyTypeDialog(configProperty);
					dialog.show();
					type = dialog.getSelectedType();
				}
				if (type == null) {
					return;
				}
				SerializableValue value = configProperty.getValue();
				propertyValueEditor.clearListeners();
				try {
					configProperty.setValue(
							SerializableValue.convert(ExpressionEnvManager.instance.getEnv(), value, type)
					);
				} catch (SerializableValueConversionException e) {
					ConvertValueDialog convertDialog = new ConvertValueDialog(value, type,
							ExpressionEnvManager.instance.getEnv());
					convertDialog.show();
					SerializableValue newValue = convertDialog.getConvertedValue();
					if (!convertDialog.wasCancelled() && newValue != null) {
						configProperty.setValue(newValue);
					}
				}
				resetPropertyValueEditor();
			}
		});
		inheritanceMenuItem.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				//if the inherited property type doesn't match the current editor type, there will be an exception
				propertyValueEditor.clearListeners();

				if (propertyIsInherited) {
					configClass.overrideProperty(configProperty.getName(), configProperty.getValue());
				} else {
					boolean inherited = configClass.inheritProperty(configProperty.getName());
					if (!inherited) {
						MenuButtonPopup popup = new MenuButtonPopup(bundle.getString("nothing_to_inherit"));
						popup.showPopup();
					}
				}
				resetPropertyValueEditor();
				propertyInheritUpdate.accept(null);
			}
		});
		miRemove.setOnAction(new EventHandler<>() {
			@Override
			public void handle(ActionEvent event) {
				SimpleResponseDialog dialog = new SimpleResponseDialog(
						ArmaDialogCreator.getPrimaryStage(),
						bundle.getString("RemovePropertyPopup.popup_title"),
						bundle.getString("RemovePropertyPopup.body"), true, true, false
				);
				dialog.setResizable(false);
				ResourceBundle appBundle = Lang.ApplicationBundle();
				dialog.getFooter().getBtnCancel().setText(appBundle.getString("Confirmation.no"));
				dialog.getFooter().getBtnOk().setText(appBundle.getString("Confirmation.yes"));
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				configClass.removeProperty(configProperty.getName());
			}
		});
		miRaw.setSelected(configProperty.getValue() instanceof SVRaw);
		miRaw.setOnAction(event -> {
			propertyValueEditor.clearListeners();
			if (miRaw.isSelected()) {
				String val = configProperty.getValue().toString();
				configProperty.setValue(new SVRaw(val, configProperty.getPropertyType()));
			} else {
				if (configProperty.getValue() instanceof SVRaw) {
					SVRaw raw = (SVRaw) configProperty.getValue();
					try {
						SerializableValue v = raw.newSubstituteTypeValue(ExpressionEnvManager.instance.getEnv());
						if (v != null) {
							configProperty.setValue(v);
						}
					} catch (Exception e) {
						PropertyType newType = raw.getSubstituteType() == null ?
								configProperty.getPropertyType() :
								raw.getSubstituteType();
						ConvertValueDialog dialog = new ConvertValueDialog(raw, newType,
								ExpressionEnvManager.instance.getEnv()
						);
						dialog.show();
						SerializableValue newValue = dialog.getConvertedValue();
						if (dialog.wasCancelled() || newValue == null) {
							//set the menuItem's selected state back to its previous state
							miRaw.setSelected(true);
							return;
						}
						configProperty.setValue(newValue);
					}
				}
			}
			resetPropertyValueEditor();
		});
		if (configProperty.getValueOptions().notEmpty()) {
			menuButtonOptions.getItems().remove(miRaw);
		}

		if (configProperty.nameEquals(ConfigPropertyLookup.TYPE)) {
			propertyValueEditor.disableEditing(true);
			menuButtonOptions.getItems().clear();
			MenuItem miNoEdit = new MenuItem(bundle.getString("type_immutable"));
			miNoEdit.setDisable(true);
			menuButtonOptions.getItems().add(miNoEdit);
		}

		updateEditModeFromProperty();
		propertyInheritUpdate.accept(null);
	}

	private void updateContainerInheritanceTint() {
		if (propertyIsInherited) {
			if (stackPaneTint == null) {
				stackPaneTint = new StackPane();
				stackPaneTint.setStyle("-fx-background-color:rgba(0, 132, 180, 0.23)");
				contentStackPane.getChildren().add(stackPaneTint);
			} else {
				if (!contentStackPane.getChildren().contains(stackPaneTint)) {
					contentStackPane.getChildren().add(stackPaneTint);
				}
			}
		} else {
			contentStackPane.getChildren().removeIf(node -> node == stackPaneTint);
			stackPaneTint = null;
		}
	}

	/**
	 Will invoke {@link #updateEditMode(EditMode)}
	 based upon the current property's value and macro
	 */
	private void updateEditModeFromProperty() {
		if (configProperty.getBoundMacro() != null) {
			updateEditMode(EditMode.MACRO);
		} else {
			updateEditMode(EditMode.DEFAULT);
		}
	}

	/**
	 Will update the {@link #contentStackPane}
	 to present the correct editor ({@link #propertyValueEditor} or a Macro editor).

	 @param mode the mode to set to
	 */
	@SuppressWarnings("unchecked")
	private void updateEditMode(@NotNull EditMode mode) {
		if (mode == currentEditMode) {
			return;
		}
		if (mode == EditMode.MACRO) {
			MacroGetterButton macroGetterButton =
					new MacroGetterButton(configProperty.getPropertyType(), configProperty.getBoundMacro());

			macroGetterButton.getChosenMacroValueObserver().updateValue(configProperty.getBoundMacro());

			contentStackPane.getChildren().set(0, macroGetterButton);
			macroGetterButton.getChosenMacroValueObserver().addListener(new ValueListener() {
				@Override
				public void valueUpdated(@NotNull ValueObserver observer, Object oldValue, Object newValue) {
					Macro m = (Macro) newValue;
					configProperty.bindToMacro(m);
				}
			});
		} else {
			if (configProperty.getBoundMacro() != null) {
				SimpleResponseDialog dialog = new SimpleResponseDialog(
						ArmaDialogCreator.getPrimaryStage(),
						bundle.getString("RemoveMacroDialog.dialog_title"),
						bundle.getString("RemoveMacroDialog.body"),
						true, true, false
				);
				dialog.getFooter().getBtnCancel().setText(bundle.getString("Confirmation.no"));
				dialog.getFooter().getBtnOk().setText(bundle.getString("Confirmation.yes"));
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				configProperty.clearMacro();
			}
			contentStackPane.getChildren().set(0, propertyValueEditor.getRootNode());
		}
		currentEditMode = mode;
	}

	public void unlink() {
		propertyValueEditor.clearListeners();
		configClass.getClassUpdateGroup().removeListener(this.configClassUpdateListener);
		configProperty.getPropertyUpdateGroup().removeListener(this.configPropertyUpdateListener);
	}

	@NotNull
	public ConfigPropertyValueEditor getPropertyValueEditor() {
		return propertyValueEditor;
	}

	/**
	 Resets the the current value editor. The new value editor will be what {@link #constructNewPropertyValueEditor()}
	 returns.
	 <p>
	 Before invoking this method, be sure to clear the listeners of {@link #propertyValueEditor} with
	 {@link ConfigPropertyValueEditor#clearListeners()}.
	 */
	private void resetPropertyValueEditor() {
		if (propertyValueEditor != null) {
			propertyValueEditor.clearListeners();
		}
		propertyValueEditor = constructNewPropertyValueEditor();
		HBox.setHgrow(contentStackPane, propertyValueEditor.displayFullWidth() ?
				Priority.ALWAYS : Priority.NEVER
		);
		contentStackPane.getChildren().setAll(propertyValueEditor.getRootNode());
		if (configProperty.getValue() instanceof SVRaw) {
			if (configProperty.getValueOptions().length == 0) {
				contentStackPane.setPadding(new Insets(1));
				contentStackPane.setBorder(
						new Border(
								new BorderStroke(
										Color.DARKORANGE,
										BorderStrokeStyle.DASHED,
										CornerRadii.EMPTY,
										BorderStroke.THIN
								)
						)
				);
			}
		} else {
			contentStackPane.setBorder(null);
			contentStackPane.setPadding(Insets.EMPTY);
		}

		updateEditModeFromProperty();
		updateContainerInheritanceTint();
	}

	/**
	 Constructs a new {@link ConfigPropertyValueEditor}. If the {@link #configProperty}'s value is null,
	 the editor that will be returned is one relevant for {@link ConfigProperty#getPropertyType()}

	 @return node that holds the controls to input data.
	 */
	@NotNull
	private ConfigPropertyValueEditor constructNewPropertyValueEditor() {
		if (configProperty.getValueOptions().length > 0) {
			return new ConfigPropertyOptionEditor(configProperty);
		}
		switch (configProperty.getPropertyType()) {
			case Int:
				return new IntegerEditor(configProperty);
			case Float:
				return new FloatEditor(configProperty);
			case ControlStyle:
				return new ControlStyleEditor(configClass, configProperty);
			case Boolean:
				return new BooleanChoiceBoxEditor(configProperty);
			case String:
				return new StringEditor(configProperty);
			case Array:
				return new ArrayEditor(configProperty);
			case Color:
				return new ColorArrayEditor(configProperty);
			case Sound:
				return new SoundEditor(configProperty);
			case Font:
				return new FontChoiceBoxEditor(configProperty);
			case FileName:
				return new FileNameEditor(configProperty);
			case Image:
				return new ImageEditor(configProperty);
			case HexColorString:
				return new HexColorEditor(configProperty);
			case Texture:
				return new StringEditor(configProperty);
			case SQF:
				return new SQFEditor(configProperty);
			case Raw:
				return new RawEditor(configProperty);
		}
		throw new IllegalStateException("Should have made a match");
	}

	@NotNull
	public ConfigProperty getConfigProperty() {
		return configProperty;
	}

	public void hideIfInherited(boolean hide) {
		this.hideIfInherited = hide;
		hide(propertyIsInherited);
	}

	private static class ChooseNewPropertyTypeDialog extends StageDialog<VBox> {

		private final ComboBox<PropertyType> comboBoxType = new ComboBox<>();

		public ChooseNewPropertyTypeDialog(@NotNull ConfigProperty property) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), bundle.getString("ConvertValueDialog.popup_title"), true, true, false);

			Label lbl = new Label(String.format(bundle.getString("ConvertValueDialog.body_f"), property.getName()));
			lbl.setWrapText(true);
			myRootElement.getChildren().add(lbl);

			PropertyType fromType = property.getPropertyType();

			for (PropertyType type : PropertyType.values()) {
				if (SerializableValue.isConvertible(fromType, type)) {
					comboBoxType.getItems().add(type);
				}
			}
			myRootElement.getChildren().add(comboBoxType);
			comboBoxType.getSelectionModel().select(property.getPropertyType());

			Button btnUseInitial = new Button(bundle.getString("ConvertValueDialog.use_initial"));
			btnUseInitial.setOnAction((e) -> comboBoxType.getSelectionModel().select(property.getPropertyType()));
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
