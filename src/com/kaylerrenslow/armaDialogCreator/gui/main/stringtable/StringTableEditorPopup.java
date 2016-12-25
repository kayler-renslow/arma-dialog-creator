package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.*;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.DownArrowMenu;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.SearchTextField;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.SyntaxTextArea;
import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImages;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
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
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
		private final ValueObserver<Language> previewLanguageObserver = new ValueObserver<>(KnownLanguage.Original);

		public StringTableEditorTabPane(@NotNull StringTable table) {
			getTabs().add(new EditTab(table, previewLanguageObserver));
			getTabs().add(new ConfigTab(table, previewLanguageObserver));
		}
	}

	private static class ConfigTab extends Tab { //set xml things like project name attribute (project=root tag of stringtable.xml)

		public ConfigTab(@NotNull StringTable table, @NotNull ValueObserver<Language> previewLanguageObserver) {
			super(Lang.ApplicationBundle().getString("Popups.StringTable.Tab.Config.tab_title"));
			VBox root = new VBox(10);
			root.setPadding(new Insets(10));
			root.setFillWidth(true);
			setContent(root);
			setGraphic(new ImageView(ADCImages.ICON_GEAR));
			setClosable(false);

			ResourceBundle bundle = Lang.ApplicationBundle();

			ComboBox<Language> comboBoxLanguage = new ComboBox<>(FXCollections.observableArrayList(KnownLanguage.values()));
			comboBoxLanguage.getSelectionModel().select(previewLanguageObserver.getValue());
			comboBoxLanguage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Language>() {
				@Override
				public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language newValue) {
					previewLanguageObserver.updateValue(newValue);
				}
			});
			Label lblPreviewLanguage = new Label(bundle.getString("Popups.StringTable.Tab.Config.preview_language"), comboBoxLanguage);
			lblPreviewLanguage.setContentDisplay(ContentDisplay.RIGHT);
			root.getChildren().add(lblPreviewLanguage);

		}
	}

	private static class EditTab extends Tab {
		private final ObservableList<StringTableKeyDescriptor> listViewItems;

		private final List<StringTableKeyDescriptor> allItems = new LinkedList<>();
		private final ListView<StringTableKeyDescriptor> lvMatch = new ListView<>();
		private final StringTableKeyEditorPane editorPane;


		public EditTab(@NotNull StringTable table, @NotNull ValueObserver<Language> previewLanguageObserver) {
			super(Lang.ApplicationBundle().getString("Popups.StringTable.Tab.Edit.tab_title"));

			listViewItems = FXCollections.observableList(new ArrayList<>(), new Callback<StringTableKeyDescriptor, javafx.beans
					.Observable[]>() {

				public javafx.beans.Observable[] call(StringTableKeyDescriptor param) {
					return new javafx.beans.Observable[]{param.getKey().getValue().getLanguageTokenMap(), param.getKey().idObserver(), previewLanguageObserver};
				}
			}); //for some reason, can't have a LinkedList as the underlying list implementation if we want the list view to update the displayed cell text automatically

			previewLanguageObserver.addListener(new ValueListener<Language>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language newValue) {
					for (StringTableKeyDescriptor descriptor : allItems) {
						descriptor.setPreviewLanguage(newValue);
					}
				}
			});

			editorPane = new StringTableKeyEditorPane(previewLanguageObserver);


			setClosable(false);
			ResourceBundle bundle = Lang.ApplicationBundle();

			lvMatch.setPlaceholder(new Label(bundle.getString("Popups.StringTable.Tab.Edit.Search.no_match")));
			lvMatch.setStyle("-fx-font-family:monospace");
			final String noPackageName = bundle.getString("Popups.StringTable.no_package");
			final String noContainerName = bundle.getString("Popups.StringTable.no_container");
			for (StringTableKey key : table.getKeys()) {
				StringTableKeyDescriptor descriptor = new StringTableKeyDescriptor(key, noPackageName, noContainerName);
				descriptor.setPreviewLanguage(previewLanguageObserver.getValue());
				allItems.add(descriptor);
				listViewItems.add(descriptor);
			}
			final Comparator<StringTableKeyDescriptor> comparator = new Comparator<StringTableKeyDescriptor>() {
				@Override
				public int compare(StringTableKeyDescriptor o1, StringTableKeyDescriptor o2) {
					return o1.getKey().getId().compareToIgnoreCase(o2.getKey().getId());
				}
			};
			listViewItems.sort(comparator);
			allItems.sort(comparator);
			lvMatch.setItems(listViewItems);
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

			SearchTextField tfSearch = new SearchTextField(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String searchText) {
					lvMatch.getItems().clear();
					searchText = searchText.trim();
					if (searchText.length() == 0) {
						addAllKeys();
					} else {
						if (searchText.length() == 1) {
							addAllKeys();
							return;
						}
						String searchTextAfter = searchText.substring(1);
						switch (searchText.charAt(0)) {
							case '\'': //intentional fall through
							case '"': { //search by text content
								searchTextAfter = searchTextAfter.toLowerCase();
								for (StringTableKeyDescriptor descriptor : allItems) {
									for (Map.Entry<Language, String> entry : descriptor.getKey().getValue().getLanguageTokenMap().entrySet()) {
										if (entry.getValue().toLowerCase().contains(searchTextAfter)) {
											lvMatch.getItems().add(descriptor);
											break;
										}
									}
								}
								break;
							}
							case '!': { //check if key does **not** have language
								searchTextAfter = searchTextAfter.toLowerCase();
								for (StringTableKeyDescriptor descriptor : allItems) {
									boolean matched = false;
									for (Language lang : descriptor.getKey().getValue().getLanguageTokenMap().keySet()) {
										if (lang.getName().toLowerCase().contains(searchTextAfter)) {
											matched = true;
											break;
										}
									}
									if (!matched) {
										lvMatch.getItems().add(descriptor);
									}
								}
								break;
							}
							case '+': { //check if key **has** a language
								searchTextAfter = searchTextAfter.toLowerCase();
								for (StringTableKeyDescriptor descriptor : allItems) {
									for (Language lang : descriptor.getKey().getValue().getLanguageTokenMap().keySet()) {
										if (lang.getName().toLowerCase().contains(searchTextAfter)) {
											lvMatch.getItems().add(descriptor);
											break;
										}
									}
								}
							}
							case '/': { //search by package name and container name
								String searchPackageName;
								String searchContainerName = "";
								if (searchTextAfter.contains("/")) {
									int slashIndex = searchTextAfter.indexOf('/');
									searchPackageName = searchTextAfter.substring(0, slashIndex);
									searchContainerName = searchTextAfter.substring(slashIndex + 1);
								} else {
									searchPackageName = searchTextAfter;
								}
								final boolean matchNullPackage = searchPackageName.equals(".");
								final boolean matchNullContainer = searchContainerName.equals(".");
								final boolean matchBothNull = matchNullContainer && matchNullPackage;
								final boolean ignoreContainer = searchContainerName.length() == 0;
								final boolean ignorePackage = searchPackageName.length() == 0;

								for (StringTableKeyDescriptor descriptor : allItems) {
									String keyContainerName = descriptor.getKey().getContainerName();
									String keyPackageName = descriptor.getKey().getPackageName();

									if (matchBothNull) {
										if (keyContainerName == null && keyPackageName == null) {
											lvMatch.getItems().add(descriptor);
										}
									} else {
										keyContainerName = keyContainerName != null ? keyContainerName.toLowerCase() : null;
										keyPackageName = keyPackageName != null ? keyPackageName.toLowerCase() : null;
										final boolean containsContainer = ignoreContainer || (keyContainerName == null ?
												matchNullContainer : keyContainerName.contains(searchContainerName.toLowerCase()));
										final boolean containsPackage = ignorePackage || (keyPackageName == null ?
												matchNullPackage : keyPackageName.contains(searchPackageName.toLowerCase()));
										if (containsContainer && containsPackage) {
											lvMatch.getItems().add(descriptor);
										}
									}
								}

								break;
							}
							default: {
								searchText = searchText.toLowerCase();
								for (StringTableKeyDescriptor descriptor : allItems) {
									if (descriptor.getKey().getId().toLowerCase().contains(searchText)) {
										lvMatch.getItems().add(descriptor);
									}
								}
								break;
							}
						}
					}
				}

				private void addAllKeys() {
					lvMatch.getItems().addAll(allItems);
				}
			});

			VBox vbRoot = new VBox(10, tfSearch, editorPane, lvMatch);
			VBox.setVgrow(lvMatch, Priority.ALWAYS);
			vbRoot.setFillWidth(true);
			vbRoot.setPadding(new Insets(10));
			setContent(vbRoot);

		}


	}


	private static class StringTableKeyEditorPane extends StackPane {
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

			menuAddLanguage = new Menu(bundle.getString("Popups.StringTable.add_language"));
			menuRemoveLanguage = new Menu(bundle.getString("Popups.StringTable.remove_language"));

			MenuItem miDefineLanguage = new MenuItem(bundle.getString("Popups.StringTable.define_language"));
			miDefineLanguage.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

				}
			});

			HBox hboxKey = new HBox(5, new DownArrowMenu(menuAddLanguage, menuRemoveLanguage/*, new SeparatorMenuItem(), miDefineLanguage*/), tfKeyId);
			hboxKey.setAlignment(Pos.CENTER_LEFT);
			VBox vboxLeft = new VBox(10, hboxKey, languagePane);
			vboxLeft.setFillWidth(true);

			SyntaxTextArea textArea = new SyntaxTextArea();
			textArea.getStylesheets().add("/com/kaylerrenslow/armaDialogCreator/gui/formatString.css");
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
						Language language = languagePane.getChosenLanguageObserver().getValue();
						if (language != null) {
							key.getValue().getLanguageTokenMap().replace(language, newValue);
						}
					}
				}
			});
			textArea.getStyleClass().add("bordered-syntax-text-area");
			languagePane.getChosenLanguageObserver().addValueListener(new ReadOnlyValueListener<Language>() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language selected) {
					if (key == null || selected == null) {
						textArea.replaceText("");
					} else {
						Map.Entry<Language, String> entry = key.getValue().getFirstLanguageTokenEntry();
						String s;
						if (entry == null || entry.getValue() == null) {
							s = "";
						} else {
							s = entry.getValue();
						}

						textArea.replaceText(s);
					}
				}

			});

			paneContent.getChildren().addAll(vboxLeft, textArea);
			vboxLeft.setPrefWidth(300);
			HBox.setHgrow(textArea, Priority.ALWAYS);

			setKey(null);
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
				added = !added;
				if (added) {
					menuAddLanguage.getItems().remove(miLanguage);
					menuRemoveLanguage.getItems().add(miLanguage);
					key.getValue().getLanguageTokenMap().put(language, "");
				} else {
					menuRemoveLanguage.getItems().remove(miLanguage);
					menuAddLanguage.getItems().add(miLanguage);
					key.getValue().getLanguageTokenMap().remove(language);
				}

			}
		}
	}

	private static class LanguageSelectionPane extends FlowPane {

		private static final String SELECTED_LINK_STYLE = "-fx-text-fill:darkgreen;-fx-font-weight:bold;";
		private static final Comparator<Node> hyperlinkComparator = new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				Hyperlink h1 = (Hyperlink) o1;
				Hyperlink h2 = (Hyperlink) o2;
				return h1.getText().compareTo(h2.getText());
			}
		};

		private final ValueObserver<Language> chosenLanguageObserver = new ValueObserver<>(null);
		private final List<Hyperlink> links = new LinkedList<>();
		private ValueObserver<Language> previewLanguageObserver;

		public LanguageSelectionPane(ValueObserver<Language> previewLanguageObserver) {
			super(10, 10);
			this.previewLanguageObserver = previewLanguageObserver;
			setToKey(null);
			chosenLanguageObserver.addListener(new ValueListener<Language>() {
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
			chosenLanguageObserver.updateValue(null);

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
			for (Map.Entry<Language, String> token : tableValue.getLanguageTokenMap().entrySet()) {
				addLanguage(token.getKey());
			}

			chosenLanguageObserver.updateValue(previewLanguageObserver.getValue()); //**** (see above comment with valueObserver.updateValue(null))
		}

		private void addLanguage(@NotNull Language language) {
			if (containsLanguage(language)) {
				return;
			}
			Hyperlink hyperlinkLanguage = new Hyperlink(language.getName());
			links.add(hyperlinkLanguage);
			links.sort(hyperlinkComparator);
			hyperlinkLanguage.setUserData(language);
			hyperlinkLanguage.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					hyperlinkLanguage.setVisited(false);
					chosenLanguageObserver.updateValue(language);
				}
			});

			getChildren().clear();
			getChildren().addAll(links);
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
		public ReadOnlyValueObserver<Language> getChosenLanguageObserver() {
			return chosenLanguageObserver.getReadOnlyValueObserver();
		}

	}

	private static class StringTableKeyDescriptor {
		private static final String FORMAT = "%-40s (%20s / %-20s) %1s %s";

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
			boolean defaultMatch = true;
			if (language != null) {
				text = key.getValue().getLanguageTokenMap().get(language);
				if (text == null) {
					defaultMatch = false;
					Map.Entry<Language, String> entry = key.getValue().getFirstLanguageTokenEntry();
					if (entry == null || entry.getValue() == null) {
						text = "";
					} else {
						text = entry.getValue();
					}
				}
				if (text == null) {
					text = "";
				} else {
					text = text.replaceAll("[\r\n\t]", "");
				}
			}
			return String.format(
					FORMAT,
					key.getId(),
					key.getPackageName() != null ? key.getPackageName() : noPackageName,
					key.getContainerName() != null ? key.getContainerName() : noContainerName,
					defaultMatch ? "" : "!",
					"\"" + text + "\""
			);
		}

		@NotNull
		public StringTableKey getKey() {
			return key;
		}
	}
}
