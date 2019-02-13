package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.core.*;
import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.gui.fxcontrol.inputfield.ExpressionChecker;
import com.armadialogcreator.gui.fxcontrol.inputfield.InputFieldDataChecker;
import com.armadialogcreator.gui.fxcontrol.inputfield.RawChecker;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 All editors for {@link ControlPropertiesEditorPane}

 @author Kayler
 @since 01/29/2017 */
public class ConfigPropertyValueEditors {

	@NotNull
	static <V extends SerializableValue> ValueListener<V> createEditorValueListener(@NotNull ConfigProperty property) {
		return new ValueListener<>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<V> observer, @Nullable V oldValue, @Nullable V newValue) {
				if (newValue == null) {
					return;
				}
				property.setValue(newValue);
			}
		};
	}

	@NotNull
	static <V extends SerializableValue> NotNullValueListener<V> createConfigPropertyValueListener(@NotNull ConfigPropertyValueEditor editor) {
		return new NotNullValueListener<>() {
			@Override
			public void valueUpdated(@NotNull NotNullValueObserver<V> observer, @NotNull V oldValue, @NotNull V newValue) {
				if (editor.getAcceptedPropertyType() == newValue.getPropertyType()) {
					editor.setToValueFromProperty();
				}
			}
		};
	}

	/** Used for when a set amount of options are available (uses radio button group for option selecting) */
	public static class ConfigPropertyOptionEditor extends FlowPane implements ConfigPropertyValueEditor {
		private final ConfigProperty configProperty;
		private ToggleGroup toggleGroup;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		private final ChangeListener<? super Toggle> toggleGroupListener = new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (newValue != null) {
					configProperty.setValue(new SVRaw(newValue.getUserData().toString(), configProperty.getPropertyType()));
				}
			}
		};

		public ConfigPropertyOptionEditor(@NotNull ConfigProperty configProperty) {
			super(10, 5);
			setAlignment(Pos.CENTER_LEFT);
			setPadding(new Insets(2));
			setPrefWrapLength(0); //wrap whenever

			this.configProperty = configProperty;
			toggleGroup = new ToggleGroup();
			RadioButton radioButton, toSelect = null;
			ReadOnlyArray<ConfigPropertyValueOption> options = configProperty.getValueOptions();
			this.configPropertyListener = createConfigPropertyValueListener(this);
			if (options == null) {
				throw new IllegalStateException("options shouldn't be null");
			}
			for (int i = 0; i < options.length; i++) {
				ConfigPropertyValueOption option = options.item(i);
				radioButton = new RadioButton(option.displayName);
				radioButton.setUserData(option.value);
				radioButton.setTooltip(new Tooltip(option.description));
				radioButton.setToggleGroup(toggleGroup);
				getChildren().add(radioButton);
				if (configProperty.getValue().toString().equals(option.value)) {
					toSelect = radioButton;
				}
			}

			if (toSelect != null) {
				toggleGroup.selectToggle(toSelect);
			}
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public Node getRootNode() {
			return this;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.Raw;
		}

		@Override
		public void clearListeners() {
			toggleGroup.selectedToggleProperty().removeListener(toggleGroupListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			toggleGroup.selectedToggleProperty().addListener(toggleGroupListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			String newValue = configProperty.getValue().toString();
			for (Toggle toggle : toggleGroup.getToggles()) {
				if (toggle.getUserData().equals(newValue)) {
					toggleGroup.selectToggle(toggle);
					return;
				}
			}
		}

		@Override
		public boolean displayFullWidth() {
			return true;
		}
	}

	public static class FileNameEditor extends FileNameValueEditor implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final ValueListener<SVFileName> editorValueListener;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		public FileNameEditor(@NotNull ConfigProperty configProperty) {
			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);
			setValue((SVFileName) configProperty.getValue());
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.ControlStyle;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((SVFileName) configProperty.getValue());
		}
	}

	public static class ControlStyleEditor extends ControlStyleValueEditor implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final ValueListener<SVControlStyleGroup> editorValueListener;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		public ControlStyleEditor(@Nullable AllowedStyleProvider allowedStyleProvider, @NotNull ConfigProperty configProperty) {
			if (allowedStyleProvider != null) {
				ArrayList<ControlStyle> moveAfterSeparator = new ArrayList<>();
				menuButton.getItems().clear();
				ControlStyle[] allowedStyles = allowedStyleProvider.getAllowedStyles();
				for (ControlStyle style : ControlStyle.values()) {
					boolean match = false;
					for (ControlStyle allowed : allowedStyles) {
						if (style == allowed) {
							menuButton.getItems().add(allowed);
							match = true;
							continue;
						}
					}
					if (!match) {
						moveAfterSeparator.add(style);
					}
				}
				menuButton.addMenu(Lang.ApplicationBundle().getString("ConfigPropertyValueEditors.unused_styles"), moveAfterSeparator);

				for (ControlStyle style : menuButton.getItems()) {
					menuButton.bindTooltip(style, style.documentation);
				}
			}
			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);
			setValue((SVControlStyleGroup) configProperty.getValue());
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public Node getRootNode() {
			return this;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.ControlStyle;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((SVControlStyleGroup) configProperty.getValue());
		}
	}

	/**
	 Used for when the input is in a text field. The InputField class also allows for input verifying so that if something entered is wrong, the user will be notified.
	 Used for {@link SVDouble}, {@link SVInteger}, {@link SVString}, {@link SVExpression}
	 */
	@SuppressWarnings("unchecked")
	private static abstract class InputFieldEditor<C extends SerializableValue> extends InputFieldValueEditor<C> implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final PropertyType macroType;
		private final ValueListener<C> editorValueListener;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		InputFieldEditor(@NotNull PropertyType macroType, @NotNull ConfigProperty
				configProperty, InputFieldDataChecker checker, @Nullable String promptText) {
			super(checker);
			this.macroType = macroType;

			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);
			inputField.setToButton(false);
			inputField.setValue((C) configProperty.getValue());
			if (promptText != null) {
				inputField.setPromptText(promptText);
			}
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return macroType;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((C) configProperty.getValue());
		}
	}

	public static class StringEditor extends InputFieldEditor<SVString> {
		public StringEditor(@NotNull ConfigProperty configProperty) {
			super(PropertyType.String, configProperty, new SVArmaStringChecker(),
					Lang.LookupBundle().getString("PropertyType.string"));
		}
	}

	public static class FloatEditor extends InputFieldEditor<SVExpression> {
		public FloatEditor(@NotNull ConfigProperty configProperty) {
			super(PropertyType.Float, configProperty,
					new ExpressionChecker(ExpressionEnvManager.instance.getEnv(), ExpressionChecker.TYPE_FLOAT),
					Lang.LookupBundle().getString("PropertyType.float")
			);
		}
	}

	public static class IntegerEditor extends InputFieldEditor<SVExpression> {
		public IntegerEditor(@NotNull ConfigProperty configProperty) {
			super(PropertyType.Int, configProperty,
					new ExpressionChecker(ExpressionEnvManager.instance.getEnv(), ExpressionChecker.TYPE_INT),
					Lang.LookupBundle().getString("PropertyType.int")
			);
		}
	}

	public static class RawEditor extends InputFieldEditor<SVRaw> {
		public RawEditor(@NotNull ConfigProperty configProperty) {
			super(PropertyType.Raw, configProperty,
					new RawChecker(configProperty.getPropertyType()),
					Lang.LookupBundle().getString("PropertyType.raw")
			);
		}
	}

	public static class SQFEditor extends InputFieldEditor<SVString> {
		public SQFEditor(@NotNull ConfigProperty configProperty) {
			super(PropertyType.SQF, configProperty,
					new SQFDataChecker(),
					"SQF"
			);
		}
	}

	/**
	 Used for when control property requires color input.
	 Use this only when the ConfigProperty's value is of type {@link SVColorArray}
	 */
	public static class ColorArrayEditor extends ColorArrayValueEditor implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final ValueListener<SVColorArray> editorValueListener;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		public ColorArrayEditor(@NotNull ConfigProperty configProperty) {
			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);
			SVColorArray value = (SVColorArray) configProperty.getValue();
			setValue(value);
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.Color;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((SVColorArray) configProperty.getValue());
		}
	}

	/**
	 Used for when control property requires color input.
	 Use this only when the ConfigProperty's value is of type {@link SVHexColor}
	 */
	public static class HexColorEditor extends HexColorValueEditor implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final ValueListener<SVHexColor> editorValueListener;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		public HexColorEditor(@NotNull ConfigProperty configProperty) {
			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);
			setValue((SVHexColor) configProperty.getValue());
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.HexColorString;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((SVHexColor) configProperty.getValue());
		}
	}

	/**
	 Used for boolean control properties
	 Use this editor for when the ConfigProperty's value is of type {@link SVBoolean}
	 */
	public static class BooleanChoiceBoxEditor extends BooleanValueEditor implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final ValueListener<SVBoolean> editorValueListener;

		private final NotNullValueListener<SerializableValue> configPropertyListener;

		public BooleanChoiceBoxEditor(@NotNull ConfigProperty configProperty) {
			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);

			choiceBox.setValue(configProperty.getBooleanValue());
			initListeners();

		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.Boolean;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((SVBoolean) configProperty.getValue());
		}
	}

	/**
	 Used for control properties that require more than one input
	 This editor will use {@link SVArray} as the ConfigProperty's value type
	 */
	public static class ArrayEditor extends ArrayValueEditor implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final ValueListener<SVArray> editorValueListener;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		public ArrayEditor(@NotNull ConfigProperty configProperty) {
			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);

			setValue((SVArray) configProperty.getValue());
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.Array;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((SVArray) configProperty.getValue());
		}
	}

	/**
	 Used for control property font picking
	 Used for ConfigProperty instances where it's value is {@link SVFont}
	 */
	public static class FontChoiceBoxEditor extends FontValueEditor implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final ValueListener<SVFont> editorValueListener;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		public FontChoiceBoxEditor(@NotNull ConfigProperty configProperty) {
			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);

			setValue((SVFont) configProperty.getValue());
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.Font;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((SVFont) configProperty.getValue());
		}
	}

	/**
	 Used for control property image editing
	 Used for ConfigProperty instances where it's value is {@link SVImage}
	 */
	public static class ImageEditor extends ImageValueEditor implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final ValueListener<SVImage> editorValueListener;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		public ImageEditor(@NotNull ConfigProperty configProperty) {
			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);
			setValue((SVImage) configProperty.getValue());
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.Image;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((SVImage) configProperty.getValue());
		}
	}

	/** Use this editor for {@link SVSound} ConfigProperty values */
	public static class SoundEditor extends SoundValueEditor implements ConfigPropertyValueEditor {

		private final ConfigProperty configProperty;
		private final ValueListener<SVSound> editorValueListener;
		private final NotNullValueListener<SerializableValue> configPropertyListener;

		public SoundEditor(@NotNull ConfigProperty configProperty) {
			this.configProperty = configProperty;
			this.editorValueListener = createEditorValueListener(configProperty);
			this.configPropertyListener = createConfigPropertyValueListener(this);
			setValue((SVSound) configProperty.getValue());
			initListeners();
		}

		@NotNull
		@Override
		public ConfigProperty getConfigProperty() {
			return configProperty;
		}

		@NotNull
		@Override
		public PropertyType getAcceptedPropertyType() {
			return PropertyType.Sound;
		}

		@Override
		public void clearListeners() {
			getReadOnlyObserver().removeListener(editorValueListener);
			configProperty.getValueObserver().removeListener(configPropertyListener);
		}

		@Override
		public void initListeners() {
			getReadOnlyObserver().addListener(editorValueListener);
			configProperty.getValueObserver().addListener(configPropertyListener);
		}

		@Override
		public void setToValueFromProperty() {
			setValue((SVSound) configProperty.getValue());
		}
	}
}
