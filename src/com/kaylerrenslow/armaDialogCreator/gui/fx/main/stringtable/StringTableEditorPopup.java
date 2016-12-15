package com.kaylerrenslow.armaDialogCreator.gui.fx.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.CBMBMenuItem;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.ComboBoxMenuButton;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.SearchTextField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.SyntaxTextArea;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 @author Kayler
 @since 12/14/2016 */
public class StringTableEditorPopup extends StagePopup<VBox> {

	public StringTableEditorPopup(@NotNull StringTable table) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("Popups.StringTable.popup_title"));

		StringTableEditorTabPane tabPane = new StringTableEditorTabPane(table);
		myRootElement.getChildren().add(tabPane);
		VBox.setVgrow(tabPane, Priority.ALWAYS);

		setStageSize(720, 480);

	}

	private static class StringTableEditorTabPane extends TabPane {
		public StringTableEditorTabPane(@NotNull StringTable table) {

			getTabs().add(new SearchTab(table));

		}
	}

	private static class SearchTab extends Tab {
		private final ObservableList<StringTableKeyDescriptor> listViewItems = FXCollections.observableList(new ArrayList<>(), new Callback<StringTableKeyDescriptor, javafx.beans.Observable[]>() {

			public javafx.beans.Observable[] call(StringTableKeyDescriptor param) {
				return new javafx.beans.Observable[]{param.getKey().getValue().getLanguageTokenMap()};
			}
		}); //for some reason, can't have a LinkedList as the underlying list implementation

		private final List<StringTableKeyDescriptor> allItems = new LinkedList<>();
		private final ListView<StringTableKeyDescriptor> lvMatch = new ListView<>(listViewItems);
		private final StringTableKeyEditorPane editorPane = new StringTableKeyEditorPane();

		public SearchTab(@NotNull StringTable table) {
			super(Lang.ApplicationBundle().getString("Popups.StringTable.Search.tab_title"));
			setGraphic(new ImageView(SearchTextField.SEARCH_ICON));
			ResourceBundle bundle = Lang.ApplicationBundle();

			lvMatch.setPlaceholder(new Label(bundle.getString("Popups.StringTable.Search.no_match")));
			lvMatch.setStyle("-fx-font-family:monospace");
			final String noPackageName = bundle.getString("Popups.StringTable.no_package");
			final String noContainerName = bundle.getString("Popups.StringTable.no_container");


			for (StringTableKey key : table.getKeys()) {
				StringTableKeyDescriptor descriptor = new StringTableKeyDescriptor(key, noPackageName, noContainerName);
				allItems.add(descriptor);
				listViewItems.add(descriptor);
			}
			lvMatch.setItems(listViewItems);

			SearchTextField tfSearch = new SearchTextField(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					lvMatch.getItems().clear();

					newValue = newValue.trim().toLowerCase();
					if (newValue.length() == 0) {
						lvMatch.getItems().addAll(allItems);
					} else {
						for (StringTableKeyDescriptor key : allItems) {
							if (key.getKey().getId().toLowerCase().contains(newValue)) {
								lvMatch.getItems().add(key);
							}
						}
					}
				}
			});
			lvMatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StringTableKeyDescriptor>() {
				@Override
				public void changed(ObservableValue<? extends StringTableKeyDescriptor> observable, StringTableKeyDescriptor oldValue, StringTableKeyDescriptor selected) {
					if (selected != null) {
						editorPane.setKey(selected.getKey());
					} else {
						editorPane.setKey(null);
					}
				}
			});
			VBox vbRoot = new VBox(10, tfSearch, editorPane, lvMatch);
			VBox.setVgrow(lvMatch, Priority.ALWAYS);
			vbRoot.setFillWidth(true);
			vbRoot.setPadding(new Insets(10));
			setContent(vbRoot);

			setClosable(false);
		}
	}


	private static class StringTableKeyEditorPane extends StackPane {
		private static final Font FONT_KEY_ID = Font.font(15);

		private final LanguageComboBoxMenuButton langButton = new LanguageComboBoxMenuButton();
		private final String noKeySelected = Lang.ApplicationBundle().getString("Popups.StringTable.Editor.no_selected_key");
		private final Label lblKeyId = new Label(noKeySelected);
		private StringTableKey key;

		public StringTableKeyEditorPane() {
			setDisable(true);
			HBox paneContent = new HBox(10);
			getChildren().add(paneContent);

			lblKeyId.setFont(FONT_KEY_ID);
			VBox vboxLeft = new VBox(10, lblKeyId, langButton);
			vboxLeft.setFillWidth(true);

			SyntaxTextArea textArea = new SyntaxTextArea();
			textArea.getStylesheets().add("/com/kaylerrenslow/armaDialogCreator/gui/fx/formatString.css");
			textArea.richChanges()
					.filter(c -> !c.getInserted().equals(c.getRemoved()))
					.subscribe(c -> {
						textArea.setStyleSpans(0, computeHighlighting(textArea.getText()));
					});
			textArea.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (key != null) {
						newValue = newValue != null ? newValue : "";
						Language language = langButton.getSelectedValueObserver().getValue();
						if (language != null) {
							key.getValue().getLanguageTokenMap().replace(language, newValue);
						}
					}
				}
			});
			langButton.getSelectedValueObserver().addValueListener(new ReadOnlyValueListener<Language>() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language selected) {
					if (key == null || selected == null) {
						textArea.replaceText("");
					} else {
						textArea.replaceText(key.getValue().getLanguageTokenMap().get(selected));
					}
				}
			});

			paneContent.getChildren().addAll(vboxLeft, textArea);
			vboxLeft.setPrefWidth(300);
			HBox.setHgrow(textArea, Priority.ALWAYS);
		}

		public void setKey(@Nullable StringTableKey key) {
			this.key = key;
			langButton.setToKey(key);
			if (key == null) {
				lblKeyId.setText(noKeySelected);
			} else {
				lblKeyId.setText(key.getId());
				if (!key.getValue().getLanguageTokenMap().keySet().isEmpty()) {
					langButton.chooseItem(key.getValue().getLanguageTokenMap().keySet().iterator().next());
				}
			}
			setDisable(key == null);
		}
	}


	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		final Pattern pattern = Pattern.compile("(%[0-9]+)");
		text = text.replaceAll("%%", "__");//prevent matching %%1 or %%%%1. We can't remove the %% however because that would mess with the indexes
		Matcher matcher = pattern.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton("param"), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

	private static class LanguageComboBoxMenuButton extends ComboBoxMenuButton<Language> {

		private final LinkedList<CBMBMenuItem<Language>> items = new LinkedList<>();

		public LanguageComboBoxMenuButton() {
			super(false, Lang.ApplicationBundle().getString("Popups.StringTable.language_placeholder"), null);
			setDisable(true);
		}

		public void setToKey(@Nullable StringTableKey key) {
			clearMenu();
			items.clear();
			setDisable(key == null);
			if (key == null) {
				return;
			}
			StringTableValue tableValue = key.getValue();
			tableValue.getLanguageTokenMap().addListener(new MapChangeListener<Language, String>() {
				@Override
				public void onChanged(Change<? extends Language, ? extends String> change) {
					if (change.wasAdded()) {
						items.add(new CBMBMenuItem<>(change.getKey()));
						addItem(items.getLast());
					} else if (change.wasRemoved()) {
						CBMBMenuItem<Language> toRemove = null;
						for (CBMBMenuItem<Language> item : items) {
							if (item.getValue() == change.getKey()) {
								toRemove = item;
								removeItem(item);
								break;
							}
						}
						if (toRemove != null) {
							items.remove(toRemove);
						}
					} else {
						throw new IllegalStateException("unexpected change type:" + change);
					}
				}
			});
			for (Map.Entry<Language, String> token : tableValue.getLanguageTokenMap().entrySet()) {
				items.add(new CBMBMenuItem<>(token.getKey()));
				addItem(items.getLast());
			}
		}
	}

	private static class StringTableKeyDescriptor {
		private static final int MAX_TEXT_LENGTH = 40;
		private static final String FORMAT = "%-40s %-" + (MAX_TEXT_LENGTH + 2 + 3 /*+2 to for quotes on either side of text, +3 for ...*/) + "s (%s / %s)";

		private StringTableKey key;
		private final String noPackageName;
		private final String noContainerName;
		private Language language;

		public StringTableKeyDescriptor(@NotNull StringTableKey key, @NotNull String noPackageName, @NotNull String noContainerName) {
			this.key = key;
			this.noPackageName = noPackageName;
			this.noContainerName = noContainerName;
			if (!key.getValue().getLanguageTokenMap().keySet().isEmpty()) {
				language = key.getValue().getLanguageTokenMap().keySet().iterator().next();
			}
		}

		public void setPreviewLanguage(@Nullable Language language) {
			this.language = language;
		}

		@Override
		public String toString() {
			String text = "";
			if (language != null) {
				text = key.getValue().getLanguageTokenMap().get(language).replaceAll("[\r\n\t]", "");
				if (text == null) {
					text = "";
				}
			}
			return String.format(
					FORMAT,
					key.getId(),
					"\"" + text.substring(0, Math.min(MAX_TEXT_LENGTH, text.length())) + (text.length() > MAX_TEXT_LENGTH ? "..." : "") + "\"",
					key.getPackageName() != null ? key.getPackageName() : noPackageName,
					key.getContainerName() != null ? key.getContainerName() : noContainerName
			);
		}

		@NotNull
		public StringTableKey getKey() {
			return key;
		}
	}
}
