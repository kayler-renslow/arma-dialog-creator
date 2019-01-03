package com.armadialogcreator.gui.fxcontrol;

import com.armadialogcreator.gui.styles.ADCStyleSheets;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.fxmisc.richtext.Caret;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.EditableStyledDocument;

import java.util.Collection;

/**
 @author Kayler
 @since 12/13/2016 */
public class SyntaxTextArea extends CodeArea {
	public SyntaxTextArea(EditableStyledDocument<Collection<String>, String, Collection<String>> document) {
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
		getStylesheets().add(ADCStyleSheets.getStylesheet("richfx.css"));
		getStyleClass().add("syntax-text-area");
		caretPositionProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if (!isFocused()) {
					setShowCaret(Caret.CaretVisibility.AUTO);
				} else {
					showCaretProperty().setValue(Caret.CaretVisibility.ON); //force it to stay on when caret moves
				}
			}
		});
		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean focused) {
				if (focused) {
					showCaretProperty().setValue(Caret.CaretVisibility.AUTO);
				} else {
					showCaretProperty().setValue(Caret.CaretVisibility.OFF); //turn the caret off when lose focus
					deselect();//deselect all selected text
				}
			}
		});
		//prevent caret from showing if not editable
		showCaretProperty().addListener(new ChangeListener<Caret.CaretVisibility>() {
			@Override
			public void changed(ObservableValue<? extends Caret.CaretVisibility> observable, Caret.CaretVisibility oldValue, Caret.CaretVisibility newValue) {
				if (!isEditable()) {
					setShowCaret(Caret.CaretVisibility.OFF);
				} else {
					setShowCaret(newValue);
				}
			}
		});
	}
}
