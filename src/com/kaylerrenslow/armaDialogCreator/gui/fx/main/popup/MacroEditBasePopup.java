package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.MacroIdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ValueEditor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.HelpUrls;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

/**
 Base class for a popup that manipulates/creates macro data.

 @author Kayler
 @since 07/15/2016. */
public abstract class MacroEditBasePopup extends StageDialog<VBox> {
	private final Env env;
	private ValueEditor editor;

	private final HBox paneEditor = new HBox();

	private final TextField tfMacroDescription = new TextField();
	private final InputField<MacroIdentifierChecker, String> inMacroKey = new InputField<>(new MacroIdentifierChecker());
	private final ChoiceBox<PropertyType> cbMacroType = new ChoiceBox<>();

	private final Label lblNoTypeChosen = new Label(Lang.ApplicationBundle().getString("Popups.MacroEdit.no_type_chosen"));

	/**
	 Creates a Macro editor.

	 @param env instance used for evaluating {@link com.kaylerrenslow.armaDialogCreator.control.sv.Expression} based Macros' values. The env is only used for checking that an expression evaluates properly.
	 */
	public MacroEditBasePopup(Env env) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("Popups.MacroEdit.popup_title"), true, true, true);
		this.env = env;
		myStage.initStyle(StageStyle.UTILITY);
		myStage.setMinWidth(480d);
		myStage.setWidth(520d);
		myStage.setHeight(240);

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
				editor = ValueEditor.getEditor(selected, env);
				paneEditor.getChildren().clear();
				Node editorNode = editor.getRootNode();
				paneEditor.getChildren().add(editorNode);
				if (editor.displayFullWidth()) {
					HBox.setHgrow(editorNode, Priority.ALWAYS);
				} else {
					HBox.setHgrow(editorNode, Priority.NEVER);
				}
			}
		});

		final GridPane gridPaneTop = new GridPane();

		final double hgap = 5;
		final double vgap = 5;
		gridPaneTop.setHgap(hgap);
		gridPaneTop.setVgap(vgap);

		gridPaneTop.addRow(0, new Label(Lang.ApplicationBundle().getString("Popups.MacroEdit.macro_key")), inMacroKey);
		gridPaneTop.addRow(1, new Label(Lang.ApplicationBundle().getString("Popups.MacroEdit.macro_type")), cbMacroType);
		gridPaneTop.addRow(2, new Label(Lang.ApplicationBundle().getString("Popups.MacroEdit.macro_value")), paneEditor);

		RowConstraints constraint = new RowConstraints(-1, -1, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true);
		gridPaneTop.getRowConstraints().addAll(constraint, constraint, constraint);

		GridPane.setHgrow(inMacroKey, Priority.ALWAYS);
		GridPane.setHgrow(tfMacroDescription, Priority.ALWAYS);

		final HBox hboxMacroDescription = new HBox(hgap, new Label(Lang.ApplicationBundle().getString("Popups.MacroEdit.macro_comment")), tfMacroDescription);
		HBox.setHgrow(tfMacroDescription, Priority.ALWAYS);

		paneEditor.setAlignment(Pos.CENTER_LEFT);
		paneEditor.getChildren().add(lblNoTypeChosen);
		GridPane.setHgrow(paneEditor, Priority.ALWAYS);

		myRootElement.getChildren().add(gridPaneTop);
		myRootElement.getChildren().add(hboxMacroDescription);

		VBox.setVgrow(gridPaneTop, Priority.SOMETIMES);
		VBox.setVgrow(hboxMacroDescription, Priority.SOMETIMES);

	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.MACROS);
	}


	/** Return true if all fields have their input set (macro key, editor has valid value, type is set). If at least one input isn't set, will return false and the input will request focus. */
	protected boolean checkFields() {
		inMacroKey.submitValue();
		if (editor == null) {
			beep();
			return false;
		}
		editor.submitCurrentData();
		if (inMacroKey.getValue() == null) {
			inMacroKey.requestFocus();
			beep();
			return false;
		}
		if (editor == null) {
			cbMacroType.requestFocus();
			beep();
			return false;
		}
		if (editor.getValue() == null) {
			editor.focusToEditor();
			beep();
			return false;
		}
		return true;
	}

	/** Set the editor to the given macro */
	protected void setToMacro(@Nullable Macro m) {
		if (m == null) {
			inMacroKey.clear();
			cbMacroType.setDisable(false);
			editor = null;
			paneEditor.getChildren().clear();
			paneEditor.getChildren().add(lblNoTypeChosen);
			cbMacroType.setValue(null);
			return;
		}
		inMacroKey.setValue(m.getKey());
		tfMacroDescription.setText(m.getComment());
		cbMacroType.setValue(m.getPropertyType());
		cbMacroType.setDisable(true);
		editor.setValue(m.getValue());
	}

	/** Return a new Macro instance with the current settings. */
	@Nullable
	protected Macro<? extends SerializableValue> getMacro() {
		if (!checkFields()) {
			return null;
		}
		Macro<? extends SerializableValue> m = new Macro<>(inMacroKey.getValue(), editor.getValue(), cbMacroType.getValue());
		m.setComment(tfMacroDescription.getText());
		return m;
	}

}
