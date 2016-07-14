package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.data.Macro;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IdentifierFieldDataChecker;
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
 Popup for creating a new macro.
 Created on 07/10/2016. */
public class NewMacroPopup extends StagePopup<VBox> {
	private ValueEditor editor;

	private StackPane stackPaneEditor = new StackPane();

	private final TextField tfMacroDescription = new TextField();
	private final InputField<IdentifierFieldDataChecker, String> inMacroKey = new InputField<>(new IdentifierFieldDataChecker());

	public NewMacroPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), new Stage(), new VBox(5), Lang.Popups.NewMacro.POPUP_TITLE);
		myRootElement.setPadding(new Insets(10));
		stackPaneEditor.minWidth(0d);
		stackPaneEditor.setAlignment(Pos.CENTER_LEFT);

		stackPaneEditor.getChildren().add(new Label(Lang.Popups.NewMacro.NO_TYPE_CHOSEN));

		EventHandler<? super KeyEvent> oldEvent = inMacroKey.getOnKeyReleased();
		inMacroKey.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				int cursorPosition = inMacroKey.getCaretPosition();
				IndexRange selection = inMacroKey.getSelection();

				if (inMacroKey.getText() != null) {
					inMacroKey.setText(inMacroKey.getText().toUpperCase().replaceAll("\\s", "_"));
				}

				inMacroKey.positionCaret(cursorPosition);
				inMacroKey.selectRange(selection.getStart(), selection.getEnd());

				if (oldEvent != null) {
					oldEvent.handle(event);
				}
			}
		});
		inMacroKey.setOnKeyTyped(inMacroKey.getOnKeyReleased());

		ChoiceBox<ControlProperty.PropertyType> cbMacroType = new ChoiceBox<>();
		cbMacroType.getItems().addAll(ControlProperty.PropertyType.values());

		cbMacroType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ControlProperty.PropertyType>() {
			@Override
			public void changed(ObservableValue<? extends ControlProperty.PropertyType> observable, ControlProperty.PropertyType oldValue, ControlProperty.PropertyType selected) {
				editor = ValueEditor.getEditor(selected);
				stackPaneEditor.getChildren().clear();
				stackPaneEditor.getChildren().add(editor.getRootNode());
			}
		});

		VBox vbTop = new VBox(5);
		vbTop.setFillWidth(true);
		vbTop.getChildren().addAll(hbox(Lang.Popups.NewMacro.MACRO_KEY, inMacroKey), hbox(Lang.Popups.NewMacro.MACRO_TYPE, cbMacroType), hbox(Lang.Popups.NewMacro.MACRO_VALUE, stackPaneEditor), hbox(Lang.Popups.NewMacro.MACRO_COMMENT, tfMacroDescription));
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

	/**Return the macro that has been created. (should be invoked after the popup has closed)*/
	@Nullable
	private Macro getCreatedMacro() {
		if (editor == null || editor.getValue() == null || inMacroKey.getValue() == null) {
			return null;
		}
		Macro m = new Macro<>(inMacroKey.getValue(), editor.getValue());
		m.setComment(tfMacroDescription.getText());
		return m;
	}

	@Override
	protected void ok() {
		Macro macro = getCreatedMacro();
		if (macro != null) {
			ArmaDialogCreator.getApplicationData().getMacroRegistry().getMacros().add(macro);
		}
		close();
	}


}
