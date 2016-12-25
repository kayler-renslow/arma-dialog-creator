package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.SyntaxTextArea;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 @author Kayler
 @since 12/24/2016 */
class StringTableValueEditor extends SyntaxTextArea {

	private static final Pattern PATTERN = Pattern.compile("(%[0-9]+)");

	public StringTableValueEditor(@NotNull StringTableKeyEditorPane editorPane) {
		getStylesheets().add("/com/kaylerrenslow/armaDialogCreator/gui/formatString.css");
		richChanges()
				.filter(c -> !c.getInserted().equals(c.getRemoved()))
				.subscribe(c -> {
					setStyleSpans(0, computeHighlighting(getText()));
				});
		textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (editorPane.getKey() != null) {
					newValue = newValue != null ? newValue : "";
					Language language = editorPane.getLanguagePane().getChosenLanguageObserver().getValue();
					if (language != null) {
						editorPane.getKey().getValue().getLanguageTokenMap().replace(language, newValue);
					}
				}
			}
		});
		getStyleClass().add("bordered-syntax-text-area");
	}


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


}
