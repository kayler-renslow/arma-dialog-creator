package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.KnownLanguage;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl.StringTableKeyImpl;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.CheckMenuButton;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.NameInputDialog;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 @author Kayler
 @since 12/26/2016 */
class NewStringTableKeyDialog extends NameInputDialog {

	private final StringTableKey key = new StringTableKeyImpl("str_tag_name", FXCollections.observableHashMap());
	private final StringTable table;

	private final ResourceBundle bundle = Lang.getBundle("StringTableBundle");

	public NewStringTableKeyDialog(@NotNull StringTable table) {
		super("", "");
		setMessage(bundle.getString("StringTableEditorPopup.Tab.Edit.insert_key_popup_body"));
		setTitle(bundle.getString("StringTableEditorPopup.Tab.Edit.insert_key_tooltip"));

		this.table = table;

		setInputText(key.getId());

		CheckMenuButton<Language> menuButtonLangs = new CheckMenuButton<>(bundle.getString("StringTableEditorPopup.Tab.Edit.insert_key_popup_lang_button_text"), null, KnownLanguage.values());
		menuButtonLangs.setAlignment(Pos.CENTER_LEFT);
		menuButtonLangs.getSelectedItems().addListener(new ListChangeListener<Language>() {
			@Override
			public void onChanged(Change<? extends Language> c) {
				while (c.next()) {
					if (c.wasRemoved()) {
						key.getLanguageTokenMap().remove(c.getRemoved());
					} else if (c.wasAdded()) {
						for (int i = c.getFrom(); i < c.getTo(); i++) {
							key.getLanguageTokenMap().put(c.getList().get(i), "");
						}
					}
				}
			}
		});
		myRootElement.getChildren().add(menuButtonLangs);
		inputTextProperty().addListener(new ChangeListener<String>() {
			boolean added;
			final String badInput = "bad-input-text-field";

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				newValue = newValue != null ? newValue : "";
				if (!StringTableKey.idIsProper(newValue)) {
					if (!added) {
						added = true;
						textField.getStyleClass().add(badInput);
						getCanOkProperty().setValue(false);
					}
				} else {
					added = false;
					textField.getStyleClass().remove(badInput);
					getCanOkProperty().setValue(true);
					key.setId(newValue);
				}
			}
		});

		textField.requestFocus();
		textField.positionCaret(textField.getText().indexOf('_'));
		textField.selectEnd();

	}

	@Override
	protected void ok() {
		StringTableKey existingKey = table.getKeyById(key.getId());
		if (existingKey != null) {
			new KeyAlreadyExistsDialog(key).show();
			return;
		}
		if (key.idIsProper()) {
			super.ok();
		} else {
			textField.requestFocus();
			beep();
		}
	}

	/**
	 Get the new key. If the dialog was cancelled, will still return a non-null key

	 @return new key
	 */
	@NotNull
	public StringTableKey getKey() {
		return key;
	}
}
