package com.armadialogcreator.gui.main.stringtable;

import com.armadialogcreator.core.stringtable.Language;
import com.armadialogcreator.core.stringtable.StringTableKeyPath;
import com.armadialogcreator.gui.fxcontrol.SearchTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 @author Kayler
 @since 12/24/2016 */
class StringTableSearchField extends SearchTextField {
	public StringTableSearchField(@NotNull ListView<StringTableKeyDescriptor> lvMatch, @NotNull List<StringTableKeyDescriptor> allItems) {
		this.textProperty().addListener(new ChangeListener<String>() {
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
								for (Map.Entry<Language, String> entry : descriptor.getKey().getLanguageTokenMap().entrySet()) {
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
								for (Language lang : descriptor.getKey().getLanguageTokenMap().keySet()) {
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
								for (Language lang : descriptor.getKey().getLanguageTokenMap().keySet()) {
									if (lang.getName().toLowerCase().contains(searchTextAfter)) {
										lvMatch.getItems().add(descriptor);
										break;
									}
								}
							}
							break;
						}
						case '/': { //search by package name and container name
							handleSearchSlash(searchTextAfter);

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

			private void handleSearchSlash(String searchTextAfter) {
				String[] tokens;
				if (searchTextAfter.contains("/")) {
					searchTextAfter = searchTextAfter.replaceAll("//", "/ /");
					tokens = searchTextAfter.split("/");
					if (tokens.length == 0) {
						tokens = new String[]{""};
					}
				} else {
					tokens = new String[]{searchTextAfter};
				}
				for (int i = 0; i < tokens.length; i++) {
					tokens[i] = tokens[i].toLowerCase().trim();
				}
				String searchPackageName = tokens[0];

				final boolean matchNullPackage = tokens[0].equals(".");
				final boolean ignorePackage = tokens[0].length() == 0;

				LinkedList<StringTableKeyDescriptor> match = new LinkedList<>();
				match.addAll(allItems);
				for (int i = 1; i < tokens.length; i++) {
					String searchContainerName = tokens[i];
					final boolean ignoreContainer = searchContainerName.length() == 0;
					final int containerInd = i - 1;
					for (StringTableKeyDescriptor descriptor : allItems) {
						final StringTableKeyPath path = descriptor.getKey().getPath();
						if (containerInd >= path.getContainers().size()) {
							match.remove(descriptor);
							continue;
						}
						String keyContainerName = path.getContainers().get(containerInd).toLowerCase();
						String keyPackageName = path.getPackageName().toLowerCase();

						final boolean containsContainer = ignoreContainer || keyContainerName.contains(searchContainerName);
						final boolean containsPackage = ignorePackage || keyPackageName.contains(searchPackageName);
						if (containsContainer && containsPackage) {
							//do nothing
						} else {
							match.remove(descriptor);
						}
					}
				}
				if (tokens.length == 1) {
					for (StringTableKeyDescriptor descriptor : allItems) {
						final StringTableKeyPath path = descriptor.getKey().getPath();
						if (path.noPackageName() && matchNullPackage) {
							//do nothing
						} else if (ignorePackage || path.getPackageName().toLowerCase().contains(searchPackageName)) {
							//do nothing
						} else {
							match.remove(descriptor);
						}
					}
				}
				lvMatch.getItems().addAll(match);

			}

			private void addAllKeys() {
				lvMatch.getItems().addAll(allItems);
			}
		});
	}


}
