package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 Used to convert a {@link SerializableValue} into another, visually

 @author Kayler
 @since 06/22/2017 */
public class ConvertValueDialog extends StageDialog<VBox> {

	private final ValueEditor convertedValueEditor;
	private SerializableValue value;

	/**
	 Create the dialog with the given value to convert. The values will not be mutated and will use the
	 {@link SerializableValue#deepCopy()} method to create copies

	 @param value1 value to convert to
	 @param toType type to convert to
	 @param env env to use
	 */
	public ConvertValueDialog(@NotNull SerializableValue value1, @NotNull PropertyType toType, @NotNull Env env) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), null, true, true, false);
		ResourceBundle bundle = Lang.ApplicationBundle();

		setTitle(bundle.getString("Popups.ConvertValueDialog.dialog_title"));

		SerializableValue copy1 = value1.deepCopy();

		ValueEditor currentValueEditor = ValueEditor.getEditor(copy1.getPropertyType(), env);
		convertedValueEditor = ValueEditor.getEditor(toType, env);

		currentValueEditor.setValue(copy1);

		myRootElement.getChildren().add(new Label(bundle.getString("Popups.ConvertValueDialog.header")));
		myRootElement.getChildren().add(new Label(bundle.getString("Popups.ConvertValueDialog.current_value")));
		myRootElement.getChildren().add(currentValueEditor.getRootNode());
		myRootElement.getChildren().add(new Label(bundle.getString("Popups.ConvertValueDialog.new_value")));
		myRootElement.getChildren().add(convertedValueEditor.getRootNode());

		convertedValueEditor.focusToEditor();

		myStage.setMinWidth(440);
	}

	/** @return the converted value, or null if {@link #wasCancelled()} */
	@Nullable
	public SerializableValue getConvertedValue() {
		return wasCancelled() ? null : convertedValueEditor.getValue();
	}
}
