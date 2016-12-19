package com.kaylerrenslow.armaDialogCreator.gui.fx.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.SearchTextField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.SyntaxTextArea;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
		}); //for some reason, can't have a LinkedList as the underlying list implementation if we want the list view to update the displayed cell text automatically

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
				public void changed(ObservableValue<? extends String> observable, String oldValue, String searchText) {
					lvMatch.getItems().clear();
					searchText = searchText.trim().toLowerCase();
					if (searchText.length() == 0) {
						addAllKeys();
					} else {
						if (searchText.charAt(0) == '"' || searchText.charAt(0) == '\'') { //search by text value
							if (searchText.length() == 1) {
								addAllKeys();
								return;
							}
							searchText = searchText.substring(1);
							for (StringTableKeyDescriptor key : allItems) {
								for (Map.Entry<Language, String> entry : key.getKey().getValue().getLanguageTokenMap().entrySet()) {
									if (entry.getValue().contains(searchText)) {
										lvMatch.getItems().add(key);
									}
								}
							}
						} else {
							for (StringTableKeyDescriptor key : allItems) {
								if (key.getKey().getId().toLowerCase().contains(searchText)) {
									lvMatch.getItems().add(key);
								}
							}
						}
					}
				}

				private void addAllKeys() {
					lvMatch.getItems().addAll(allItems);
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

		private final LanguageSelectionPane languagePane = new LanguageSelectionPane();
		private final String noKeySelected = Lang.ApplicationBundle().getString("Popups.StringTable.Editor.no_selected_key");
		private final Label lblKeyId = new Label(noKeySelected);
		private StringTableKey key;

		public StringTableKeyEditorPane() {
			setDisable(true);
			HBox paneContent = new HBox(10);
			getChildren().add(paneContent);

			lblKeyId.setFont(FONT_KEY_ID);
			VBox vboxLeft = new VBox(10, lblKeyId, languagePane);
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
						Language language = languagePane.getValueObserver().getValue();
						if (language != null) {
							key.getValue().getLanguageTokenMap().replace(language, newValue);
						}
					}
				}
			});
			textArea.getStyleClass().add("bordered-syntax-text-area");
			languagePane.getValueObserver().addValueListener(new ReadOnlyValueListener<Language>() {
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
			languagePane.setToKey(key);
			if (key == null) {
				lblKeyId.setText(noKeySelected);
			} else {
				lblKeyId.setText(key.getId());
			}
			setDisable(key == null);
		}
	}

	private static final Pattern PATTERN = Pattern.compile("(%[0-9]+)");

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		text = text.replaceAll("%%", "__");//prevent matching %%1 or %%%%1. We can't remove the %% however because that would mess with the indexes
		Matcher matcher = PATTERN.matcher(text);
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

	private static class LanguageSelectionPane extends FlowPane {

		private static final String SELECTED_LINK_STYLE = "-fx-text-fill:darkgreen;-fx-font-weight:bold;";

		private final ValueObserver<Language> valueObserver = new ValueObserver<>(null);
		private final List<Hyperlink> links = new LinkedList<>();

		public LanguageSelectionPane() {
			super(10, 10);
			setToKey(null);
			valueObserver.addListener(new ValueListener<Language>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language newValue) {
					for (Hyperlink hyperlink : links) {
						if (hyperlink.getUserData().equals(newValue)) {
							hyperlink.setStyle(LanguageSelectionPane.SELECTED_LINK_STYLE);
						} else {
							hyperlink.setStyle("");
						}
					}
				}
			});
		}

		public void setToKey(@Nullable StringTableKey key) {
			getChildren().clear();
			links.clear();
			setDisable(key == null);

			//force an update to null so that if the Language is the same as the previous in this observer, the listeners will get notified below at comment marked with: ****
			valueObserver.updateValue(null);

			if (key == null) {
				return;
			}

			StringTableValue tableValue = key.getValue();
			tableValue.getLanguageTokenMap().addListener(new MapChangeListener<Language, String>() {
				@Override
				public void onChanged(Change<? extends Language, ? extends String> change) {
					if (change.wasAdded()) {
						addLanguage(change.getKey());
					} else if (change.wasRemoved()) {
						removeLanguage(change.getKey());
					} else {
						throw new IllegalStateException("unexpected change type:" + change);
					}
				}
			});
			Language setToMe = null;
			for (Map.Entry<Language, String> token : tableValue.getLanguageTokenMap().entrySet()) {
				addLanguage(token.getKey());
				if (setToMe == null) {
					setToMe = token.getKey();
				}
			}

			valueObserver.updateValue(setToMe); //**** (see above comment with valueObserver.updateValue(null))
		}

		private void addLanguage(@NotNull Language language) {
			if (containsLanguage(language)) {
				return;
			}
			Hyperlink hyperlinkLanguage = new Hyperlink(language.getName());
			links.add(hyperlinkLanguage);
			hyperlinkLanguage.setUserData(language);
			hyperlinkLanguage.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					hyperlinkLanguage.setVisited(false);
					valueObserver.updateValue(language);
				}
			});

			getChildren().add(hyperlinkLanguage);
		}

		private void removeLanguage(@NotNull Language language) {
			Hyperlink remove = null;
			for (Hyperlink hyperlink : links) {
				if (hyperlink.getUserData() == language) {
					remove = hyperlink;
					break;
				}
			}
			if (remove != null) {
				links.remove(remove);
				getChildren().remove(remove);
			}
		}

		private boolean containsLanguage(@NotNull Language language) {
			for (Hyperlink hyperlink : links) {
				if (hyperlink.getUserData().equals(language)) {
					return true;
				}
			}
			return false;
		}

		@NotNull
		public ReadOnlyValueObserver<Language> getValueObserver() {
			return valueObserver.getReadOnlyValueObserver();
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
