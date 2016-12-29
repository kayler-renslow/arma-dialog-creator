package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.model.EditableStyledDocument;

import java.util.Collection;

/**
 @author Kayler
 @since 12/13/2016 */
public class SyntaxTextArea extends CodeArea {
	public SyntaxTextArea(EditableStyledDocument<Collection<String>, Collection<String>> document) {
		super(document);
		init();
	}

	public SyntaxTextArea() {
		init();
	}

	public SyntaxTextArea(String text) {
		super(text);
		init();
	}

	private void init() {
		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		getStylesheets().add("/com/kaylerrenslow/armaDialogCreator/gui/richfx.css");
		getStyleClass().add("syntax-text-area");
		caretPositionProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if (!isFocused()) {
					setShowCaret(StyledTextArea.CaretVisibility.AUTO);
				} else {
					showCaretProperty().setValue(StyledTextArea.CaretVisibility.ON); //force it to stay on when caret moves
				}
			}
		});
		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean focused) {
				if (focused) {
					showCaretProperty().setValue(StyledTextArea.CaretVisibility.AUTO);
				} else {
					showCaretProperty().setValue(StyledTextArea.CaretVisibility.OFF); //turn the caret off when lose focus
					deselect();//deselect all selected text
				}
			}
		});
		//prevent caret from showing if not editable
		showCaretProperty().addListener(new ChangeListener<CaretVisibility>() {
			@Override
			public void changed(ObservableValue<? extends CaretVisibility> observable, CaretVisibility oldValue, CaretVisibility newValue) {
				if (!isEditable()) {
					setShowCaret(CaretVisibility.OFF);
				} else {
					setShowCaret(newValue);
				}
			}
		});
	}
}
