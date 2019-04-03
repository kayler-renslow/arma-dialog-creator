package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.main.sveditor.ValueEditor;
import com.armadialogcreator.lang.Lang;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;


/**
 Popup for a single {@link ValueEditor}

 @author Kayler
 @since 04/2/2019. */
public class SimpleValueEditorDialog extends StageDialog<VBox> {

	private final ValueEditor editor;

	public SimpleValueEditorDialog(@NotNull PropertyType propertyType) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, true, true, false);
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("Popups.SimpleValueEditorDialog.popup_title"));

		myStage.setMinWidth(480d);
		myStage.setWidth(520d);

		editor = ValueEditor.getEditor(propertyType, ExpressionEnvManager.instance.getEnv());

		myRootElement.getChildren().add(new Label(propertyType.getDisplayName()));
		myRootElement.getChildren().add(editor.getRootNode());
	}

	/** @return the value before the dialog was closed */
	@Nullable
	public SerializableValue getValue() {
		return editor.getValue();
	}

	@Override
	public void show() {
		myStage.sizeToScene();
		super.show();
	}
}
