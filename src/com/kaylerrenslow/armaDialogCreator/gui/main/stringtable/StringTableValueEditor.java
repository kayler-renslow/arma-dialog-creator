package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 12/24/2016 */
class StringTableValueEditor extends StringTableLanguageTokenEditor {

	public StringTableValueEditor(@NotNull StringTableKeyEditorPane editorPane) {
		textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (editorPane.getKey() != null) {
					newValue = newValue != null ? newValue : "";
					Language language = editorPane.getLanguagePane().getChosenLanguageObserver().getValue();
					if (language != null) {
						editorPane.getKey().getLanguageTokenMap().replace(language, newValue);
					}
				}
			}
		});
	}

}
