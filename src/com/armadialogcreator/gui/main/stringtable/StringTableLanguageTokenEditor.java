package com.armadialogcreator.gui.main.stringtable;

import com.armadialogcreator.gui.fxcontrol.SyntaxTextArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 @author Kayler
 @since 12/28/2016 */
public class StringTableLanguageTokenEditor extends SyntaxTextArea {

	private static final Pattern PATTERN = Pattern.compile("(%[0-9]+)");

	public StringTableLanguageTokenEditor() {
		getStylesheets().add("/com/armadialogcreator/gui/formatString.css");
		richChanges()
				.filter(c -> !c.getInserted().equals(c.getRemoved()))
				.subscribe(c -> {
					setStyleSpans(0, computeHighlighting(getText()));
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
