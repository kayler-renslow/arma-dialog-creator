package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.HelpUrls;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.sv.SVExpression;
import com.armadialogcreator.data.MacroRegistry;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.SimpleResponseDialog;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.fxcontrol.inputfield.InputField;
import com.armadialogcreator.gui.fxcontrol.inputfield.MacroIdentifierChecker;
import com.armadialogcreator.gui.main.BrowserUtil;
import com.armadialogcreator.gui.main.sveditor.ValueEditor;
import com.armadialogcreator.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 Base class for a popup that manipulates/creates macro data.

 @author Kayler
 @since 07/15/2016. */
public abstract class MacroEditBasePopup extends StageDialog<VBox> {
	private ValueEditor editor;

	private final HBox paneEditor = new HBox();

	private final TextField tfMacroDescription = new TextField();
	private final InputField<MacroChecker, String> inMacroKey = new InputField<>(new MacroChecker());
	private final ChoiceBox<PropertyType> cbMacroType = new ChoiceBox<>();

	private final Label lblNoTypeChosen = new Label(null);

	private final ResourceBundle bundle = Lang.ApplicationBundle();
	private Macro initialMacro;

	/**
	 Creates a Macro editor.

	 @param env instance used for evaluating {@link SVExpression} based Macros' values. The env is only used for checking that an expression evaluates properly.
	 */
	public MacroEditBasePopup(@NotNull Env env) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, true, true, true);

		setTitle(bundle.getString("Popups.MacroEdit.popup_title"));

		lblNoTypeChosen.setText(bundle.getString("Popups.MacroEdit.no_type_chosen"));

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

		gridPaneTop.addRow(0, new Label(bundle.getString("Popups.MacroEdit.macro_key")), inMacroKey);
		gridPaneTop.addRow(1, new Label(bundle.getString("Popups.MacroEdit.macro_type")), cbMacroType);
		gridPaneTop.addRow(2, new Label(bundle.getString("Popups.MacroEdit.macro_value")), paneEditor);

		RowConstraints constraint = new RowConstraints(-1, -1, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true);
		gridPaneTop.getRowConstraints().addAll(constraint, constraint, constraint);

		GridPane.setHgrow(inMacroKey, Priority.ALWAYS);
		GridPane.setHgrow(tfMacroDescription, Priority.ALWAYS);

		final HBox hboxMacroDescription = new HBox(hgap, new Label(bundle.getString("Popups.MacroEdit.macro_comment")), tfMacroDescription);
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
		initialMacro = m;
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
	protected Macro getNewMacro() {
		if (!checkFields()) {
			return null;
		}
		Macro m = Macro.newMacro(inMacroKey.getValue(), editor.getValue());
		m.setComment(tfMacroDescription.getText());
		return m;
	}

	protected void addDeleteButton() {
		if (initialMacro == null) {
			throw new IllegalStateException("initialMacro is null.");
		}
		Button btnDelete = new Button(bundle.getString("Popups.MacroEdit.delete_macro"));
		btnDelete.setOnAction(event -> {
			SimpleResponseDialog d = new SimpleResponseDialog(
					ArmaDialogCreator.getPrimaryStage(),
					bundle.getString("Popups.MacroEdit.delete_macro_confirm_title"),
					String.format(bundle.getString("Popups.MacroEdit.delete_macro_confirm_f"), initialMacro), true, true, false
			);
			d.show();
			if (d.wasCancelled()) {
				return;
			}
			MacroRegistry.instance.removeMacro(initialMacro);
			close();
		});
		footer.getRightContainer().getChildren().add(0, btnDelete);
	}

	private class MacroChecker extends MacroIdentifierChecker {
		@Override
		public String errorMsgOnData(@NotNull String data) {
			String sup = super.errorMsgOnData(data);
			if (sup != null) {
				return sup;
			}
			if (initialMacro != null && !initialMacro.getKey().equals(data)) {
				if (MacroRegistry.instance.findMacroByName(data) != null) {
					return bundle.getString("Macros.key_already_exists");
				}
			}
			return null;
		}
	}
}
