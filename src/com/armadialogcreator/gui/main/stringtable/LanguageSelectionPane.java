package com.armadialogcreator.gui.main.stringtable;

import com.armadialogcreator.core.stringtable.Language;
import com.armadialogcreator.core.stringtable.StringTableKey;
import com.armadialogcreator.util.ReadOnlyValueObserver;
import com.armadialogcreator.util.ValueListener;
import com.armadialogcreator.util.ValueObserver;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.FlowPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
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
	private final List<Hyperlink> links = new LinkedList<>();
	private final Language defaultLanguage;

	public LanguageSelectionPane(@Nullable Language defaultPreviewLanguage) {
		super(10, 10);
		this.defaultLanguage = defaultPreviewLanguage;
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

		key.getLanguageTokenMap().addListener(new MapChangeListener<Language, String>() {
			@Override
			public void onChanged(Change<? extends Language, ? extends String> change) {
				if (change.wasAdded()) {
					addLanguage(change.getKey());
					if (links.size() == 1) {
						chosenLanguageObserver.updateValue(null);
						chosenLanguageObserver.updateValue(change.getKey());
					}
				} else if (change.wasRemoved()) {
					removeLanguage(change.getKey());
				} else {
					throw new IllegalStateException("unexpected change type:" + change);
				}
			}
		});
		for (Map.Entry<Language, String> token : key.getLanguageTokenMap().entrySet()) {
			addLanguage(token.getKey());
		}

		chosenLanguageObserver.updateValue(defaultLanguage); //**** (see above comment with valueObserver.updateValue(null))
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
