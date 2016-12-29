package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.SearchTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import org.jetbrains.annotations.NotNull;

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
	}


}
