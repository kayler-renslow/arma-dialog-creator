package com.armadialogcreator.gui.main.stringtable;

import com.armadialogcreator.core.stringtable.Language;
import com.armadialogcreator.core.stringtable.StringTableKey;
import com.armadialogcreator.util.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 @author Kayler
 @since 12/24/2016 */
class LanguageSelectionPane extends FlowPane {

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
	private final Map<Language, Hyperlink> links = new HashMap<>();
	private final Language defaultLanguage;

	public LanguageSelectionPane(@Nullable Language defaultPreviewLanguage) {
		super(10, 10);
		this.defaultLanguage = defaultPreviewLanguage;
		setToKey(null);
		chosenLanguageObserver.addListener(new ValueListener<Language>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language newValue) {
				for (Hyperlink hyperlink : links.values()) {
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

		key.getLanguageTokenMap().addListener((map, change) -> {
			switch (change.getChangeType()) {
				case Clear: {
					map.forEach((key1, value) -> removeLanguage(key1));
					break;
				}
				case Put: {
					MapObserverChangePut<Language, String> put = change.getPut();
					addLanguage(put.getKey());
					if (links.size() == 1) {
						chosenLanguageObserver.updateValue(null);
						chosenLanguageObserver.updateValue(put.getKey());
					}
					break;
				}
				case Move: {
					MapObserverChangeMove<Language, String> moved = change.getMoved();
					if (moved.isSourceMapChange()) {
						removeLanguage(moved.getKey());
					} else {
						addLanguage(moved.getKey());
					}
					break;
				}
				case Remove: {
					MapObserverChangeRemove<Language, String> removed = change.getRemoved();
					removeLanguage(removed.getKey());
					break;
				}
				case Replace: {
					MapObserverChangeReplace<Language, String> replace = change.getReplace();
					Hyperlink hyperlink = links.get(replace.getKey());
					hyperlink.setText(replace.getNewValue());
					break;
				}
			}

		});
		for (Map.Entry<Language, String> token : key.getLanguageTokenMap().entrySet()) {
			addLanguage(token.getKey());
		}

		chosenLanguageObserver.updateValue(defaultLanguage); //**** (see above comment with valueObserver.updateValue(null))
	}

	private void addLanguage(@NotNull Language language) {
		if (links.containsKey(language)) {
			return;
		}
		Hyperlink hyperlinkLanguage = new Hyperlink(language.getName());
		hyperlinkLanguage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				hyperlinkLanguage.setVisited(false);
				chosenLanguageObserver.updateValue(language);
			}
		});

		getChildren().add(hyperlinkLanguage);
	}

	private void removeLanguage(@NotNull Language language) {
		Hyperlink removedHyperlink = links.get(language);
		if (removedHyperlink != null) {
			links.remove(language);
			getChildren().remove(removedHyperlink);
		}
	}

	@NotNull
	public ReadOnlyValueObserver<Language> getChosenLanguageObserver() {
		return chosenLanguageObserver.getReadOnlyValueObserver();
	}

}
