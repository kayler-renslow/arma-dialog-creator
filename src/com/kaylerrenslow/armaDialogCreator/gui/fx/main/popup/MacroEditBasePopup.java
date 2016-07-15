package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ValueEditor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Base class for a popup that manipulates/creates macro data. Extend this class to provide more functionality.
 Created on 07/15/2016. */
public abstract class MacroEditBasePopup extends StagePopup<VBox> {
	private ValueEditor editor;

	private StackPane stackPaneEditor = new StackPane();

	private final TextField tfMacroDescription = new TextField();
	private final InputField<IdentifierChecker, String> inMacroKey = new InputField<>(new IdentifierChecker());
	private final ChoiceBox<PropertyType> cbMacroType = new ChoiceBox<>();

	private final Label lblNoTypeChosen = new Label(Lang.Popups.MacroEdit.NO_TYPE_CHOSEN);

	public MacroEditBasePopup() {
		super(ArmaDialogCreator.getPrimaryStage(), new Stage(), new VBox(5), Lang.Popups.MacroEdit.POPUP_TITLE);
		myRootElement.setPadding(new Insets(10));
		stackPaneEditor.minWidth(0d);
		stackPaneEditor.setAlignment(Pos.CENTER_LEFT);

		stackPaneEditor.getChildren().add(lblNoTypeChosen);

		EventHandler<? super KeyEvent> oldEvent = inMacroKey.getOnKeyReleased();
		inMacroKey.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				int cursorPosition = inMacroKey.getCaretPosition();
				IndexRange selection = inMacroKey.getSelection();

				inMacroKey.setText(inMacroKey.getText().toUpperCase().replaceAll("\\s", "_"));

				inMacroKey.positionCaret(cursorPosition);
				inMacroKey.selectRange(selection.getStart(), selection.getEnd());

				if (oldEvent != null) {
					oldEvent.handle(event);
				}
			}
		});
		inMacroKey.setOnKeyTyped(inMacroKey.getOnKeyReleased());

		cbMacroType.getItems().addAll(PropertyType.values());

		cbMacroType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PropertyType>() {
			@Override
			public void changed(ObservableValue<? extends PropertyType> observable, PropertyType oldValue, PropertyType selected) {
				editor = ValueEditor.getEditor(selected);
				stackPaneEditor.getChildren().clear();
				stackPaneEditor.getChildren().add(editor.getRootNode());
			}
		});

		VBox vbTop = new VBox(5);
		vbTop.setFillWidth(true);
		vbTop.getChildren().addAll(hbox(Lang.Popups.MacroEdit.MACRO_KEY, inMacroKey), hbox(Lang.Popups.MacroEdit.MACRO_TYPE, cbMacroType), hbox(Lang.Popups.MacroEdit.MACRO_VALUE, stackPaneEditor), hbox(Lang.Popups.MacroEdit.MACRO_COMMENT, tfMacroDescription));
		myRootElement.getChildren().add(vbTop);
		VBox.setVgrow(vbTop, Priority.ALWAYS);

		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.initStyle(StageStyle.UTILITY);
		myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(true, true, false));

		myStage.setMinWidth(480d);
		myStage.setWidth(500d);
		myStage.setHeight(240);
	}

	private static HBox hbox(String text, Node graphic) {
		Label lbl = new Label(text);
		HBox.setHgrow(graphic, Priority.ALWAYS);
		return new HBox(5, lbl, graphic);
	}

	/**Set the editor to the given macro*/
	protected void setToMacro(@Nullable Macro m) {
		if (m == null) {
			inMacroKey.clear();
			editor = null;
			stackPaneEditor.getChildren().clear();
			stackPaneEditor.getChildren().add(lblNoTypeChosen);
			cbMacroType.setValue(null);
			return;
		}
		inMacroKey.setValue(m.getKey());
		editor = ValueEditor.getEditor(m.getPropertyType());
		cbMacroType.setValue(m.getPropertyType());
	}

	/** Return a new Macro instance with the current settings.*/
	@Nullable
	protected Macro<? extends SerializableValue> getMacro() {
		if (editor == null || editor.getValue() == null || inMacroKey.getValue() == null || cbMacroType.getValue() == null) {
			return null;
		}
		Macro<? extends SerializableValue> m = new Macro<>(inMacroKey.getValue(), editor.getValue(), cbMacroType.getValue());
		m.setComment(tfMacroDescription.getText());
		return m;
	}

}
