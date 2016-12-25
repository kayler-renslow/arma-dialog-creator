package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.KnownLanguage;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.DownArrowMenu;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.NameInputDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.SimpleResponseDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.ResourceBundle;

/**
 @author Kayler
 @since 12/24/2016 */
class StringTableKeyEditorPane extends StackPane {
	private static final Font FONT_KEY_ID = Font.font(15);

	private final LanguageSelectionPane languagePane;
	private final String noKeySelected = Lang.ApplicationBundle().getString("Popups.StringTable.Editor.no_selected_key");
	private final TextField tfKeyId = new TextField();
	private final Menu menuAddLanguage;
	private final Menu menuRemoveLanguage;
	private StringTableKey key;

	public StringTableKeyEditorPane(@NotNull ValueObserver<Language> previewLanguageObserver) {
		languagePane = new LanguageSelectionPane(previewLanguageObserver);
		ResourceBundle bundle = Lang.ApplicationBundle();

		HBox paneContent = new HBox(10);
		getChildren().add(paneContent);

		tfKeyId.setFont(FONT_KEY_ID);
		tfKeyId.getStyleClass().removeAll("text-field", "text-input");
		tfKeyId.setStyle("-fx-text-fill:-fx-text-inner-color;");
		tfKeyId.setCursor(Cursor.TEXT);
		tfKeyId.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (key == null) {
					return;
				}
				newValue = newValue != null ? newValue : "";
				key.setId(newValue);
			}
		});
		HBox.setHgrow(tfKeyId, Priority.ALWAYS);

		menuAddLanguage = new Menu(bundle.getString("Popups.StringTable.Tab.Edit.add_language"));
		menuRemoveLanguage = new Menu(bundle.getString("Popups.StringTable.Tab.Edit.remove_language"));

		MenuItem miEditPackage = new MenuItem(bundle.getString("Popups.StringTable.Tab.Edit.edit_package"));
		miEditPackage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (key == null) {
					return;
				}
				NameInputDialog dialog = new NameInputDialog(
						String.format(bundle.getString("Popups.StringTable.Tab.Edit.edit_package_popup_title_f"), key.getId()),
						bundle.getString("Popups.StringTable.Tab.Edit.new_package_name"),
						bundle.getString("Popups.StringTable.no_package")
				);
				dialog.setInputText(key.getPackageName());
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				key.setPackageName(dialog.getInputText());
			}
		});
		MenuItem miEditContainer = new MenuItem(bundle.getString("Popups.StringTable.Tab.Edit.edit_container"));
		miEditContainer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (key == null) {
					return;
				}
				NameInputDialog dialog = new NameInputDialog(
						String.format(bundle.getString("Popups.StringTable.Tab.Edit.edit_container_popup_title_f"), key.getId()),
						bundle.getString("Popups.StringTable.Tab.Edit.new_container_name"),
						bundle.getString("Popups.StringTable.no_container")
				);
				dialog.setInputText(key.getContainerName());
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				key.setContainerName(dialog.getInputText());
			}
		});

		HBox hboxKey = new HBox(5, new DownArrowMenu(menuAddLanguage, menuRemoveLanguage, new SeparatorMenuItem(), miEditPackage, miEditContainer), tfKeyId);
		hboxKey.setAlignment(Pos.CENTER_LEFT);
		VBox vboxLeft = new VBox(10, hboxKey, languagePane);
		vboxLeft.setFillWidth(true);

		StringTableValueEditor taValue = new StringTableValueEditor(this);
		languagePane.getChosenLanguageObserver().addValueListener(new ReadOnlyValueListener<Language>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language selected) {
				if (key == null || selected == null) {
					taValue.replaceText("");
				} else {
					String s = key.getValue().getLanguageTokenMap().get(selected);
					if (s == null) {
						Map.Entry<Language, String> entry = key.getValue().getFirstLanguageTokenEntry();
						if (entry == null || entry.getValue() == null) {
							s = "";
						} else {
							s = entry.getValue();
						}
					}

					taValue.replaceText(s);
				}
			}

		});

		paneContent.getChildren().addAll(vboxLeft, taValue);
		vboxLeft.setPrefWidth(300);
		HBox.setHgrow(taValue, Priority.ALWAYS);

		setKey(null);
	}

	public void setKey(@Nullable StringTableKey key) {
		this.key = key;
		languagePane.setToKey(key);
		if (key == null) {
			tfKeyId.setText(noKeySelected);
		} else {
			tfKeyId.setText(key.getId());
		}
		handleKeyDownMenus();
		setDisable(key == null);
	}

	@NotNull
	public LanguageSelectionPane getLanguagePane() {
		return languagePane;
	}

	@Nullable
	public StringTableKey getKey() {
		return key;
	}

	private void handleKeyDownMenus() {
		menuRemoveLanguage.getItems().clear();
		menuAddLanguage.getItems().clear();
		if (key == null) {
			return;
		}
		for (Language language : KnownLanguage.values()) {
			boolean found = false;
			MenuItem miLanguage = new MenuItem(language.getName());
			miLanguage.setUserData(language);

			for (Map.Entry<Language, String> entry : key.getValue().getLanguageTokenMap().entrySet()) {
				if (entry.getKey().equals(language)) {
					found = true;
					menuRemoveLanguage.getItems().add(miLanguage);
					miLanguage.setOnAction(new LanguageMenuItemEvent(true, key, language, miLanguage, menuAddLanguage, menuRemoveLanguage));
					break;
				}
			}
			if (!found) {
				menuAddLanguage.getItems().add(miLanguage);
				miLanguage.setOnAction(new LanguageMenuItemEvent(false, key, language, miLanguage, menuAddLanguage, menuRemoveLanguage));
			}
		}
	}

	private class LanguageMenuItemEvent implements EventHandler<ActionEvent> {
		private final StringTableKey key;
		private final Language language;
		private final MenuItem miLanguage;
		private final Menu menuAddLanguage;
		private final Menu menuRemoveLanguage;
		private boolean added;

		public LanguageMenuItemEvent(boolean added, @NotNull StringTableKey key, @NotNull Language language, @NotNull MenuItem miLanguage, @NotNull Menu menuAddLanguage, @NotNull Menu
				menuRemoveLanguage) {
			this.added = added;
			this.key = key;
			this.language = language;
			this.miLanguage = miLanguage;
			this.menuAddLanguage = menuAddLanguage;
			this.menuRemoveLanguage = menuRemoveLanguage;
		}

		@Override
		public void handle(ActionEvent event) {
			boolean added = !this.added;
			ResourceBundle bundle = Lang.ApplicationBundle();
			if (added) {
				menuAddLanguage.getItems().remove(miLanguage);
				menuRemoveLanguage.getItems().add(miLanguage);
				key.getValue().getLanguageTokenMap().put(language, "");
			} else {
				SimpleResponseDialog dialog = new SimpleResponseDialog(
						ArmaDialogCreator.getPrimaryStage(),
						bundle.getString("Popups.StringTable.Tab.Edit.remove_language"),
						String.format(bundle.getString("Popups.StringTable.Tab.Edit.remove_language_confirmation_f"), language.getName()),
						true, true, false
				);
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				menuRemoveLanguage.getItems().remove(miLanguage);
				menuAddLanguage.getItems().add(miLanguage);
				key.getValue().getLanguageTokenMap().remove(language);
			}

			this.added = added;

		}
	}

}
