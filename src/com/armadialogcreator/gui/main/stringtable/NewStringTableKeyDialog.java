package com.armadialogcreator.gui.main.stringtable;

import com.armadialogcreator.core.stringtable.KnownLanguage;
import com.armadialogcreator.core.stringtable.Language;
import com.armadialogcreator.core.stringtable.StringTable;
import com.armadialogcreator.core.stringtable.StringTableKey;
import com.armadialogcreator.data.SimpleStringTableKey;
import com.armadialogcreator.gui.fxcontrol.CheckMenuButton;
import com.armadialogcreator.gui.main.popup.NameTextFieldDialog;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.MapObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 @author Kayler
 @since 12/26/2016 */
class NewStringTableKeyDialog extends NameTextFieldDialog {

	private final StringTableKey key = new SimpleStringTableKey("str_tag_name", new MapObserver<>(new HashMap<>()));
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
		menuButtonLangs.getSelectedItemsReadOnly().addListener(new ListChangeListener<Language>() {
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
						myNode.getStyleClass().add(badInput);
						getCanOkProperty().setValue(false);
					}
				} else {
					added = false;
					myNode.getStyleClass().remove(badInput);
					getCanOkProperty().setValue(true);
					key.setId(newValue);
				}
			}
		});

		myNode.requestFocus();
		myNode.positionCaret(myNode.getText().indexOf('_'));
		myNode.selectEnd();

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
			myNode.requestFocus();
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
