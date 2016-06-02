package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.EventHandler;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Base class for a text field control that checks if the inputted data is correct and returns the data as an object with type V
 Created on 05/31/2016. */
public class InputField<E extends IInputFieldDataChecker<V>, V> extends TextField {
	private static final String BAD_FIELD = "bad-input-text-field";
	private final E fieldData;
	private boolean valid = true;
	private ValueObserver<V> observer = new ValueObserver<>(null);

	/** Creates a new InputField (TextField with additional features). The prompt text will be set to whatever fieldDataChecker.getTypeName() returns */
	public InputField(@NotNull E fieldDataChecker) {
		this.fieldData = fieldDataChecker;
		EventHandler<KeyEvent> keyEvent = new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					setValueFromText(getText());
				}
			}
		};
		setPromptText(fieldDataChecker.getTypeName());
		setTooltip(new Tooltip(fieldDataChecker.getTypeName()));
		this.setOnKeyReleased(keyEvent);
		this.setOnKeyTyped(keyEvent);
	}

	/** Get the text parsed and converted into type V. This will only return whatever the generic type E outputs from IInputFieldDataChecker.parse(String data) */
	public V getValue() {
		return fieldData.parse(this.getText());
	}

	/** Return true if the data inside the text field is valid, false otherwise */
	public boolean hasValidData() {
		checkIfValid(getText());
		return valid;
	}

	/** Set the value from an object. The text in the control is set to whatever V.toString() (generic type V) returns. */
	public void setValue(V value) {
		this.setText(value.toString());
		observer.updateValue(value);
		valid = true;
	}

	/** Set the value from text. The value will only be set if the text is valid. */
	public void setValueFromText(String text) {
		int cursorPosition = getCaretPosition();
		IndexRange selection = getSelection();
		checkIfValid(text);
		if (valid) {
			setText(text);
			setValue(getValue());
		}
		positionCaret(cursorPosition);
		selectRange(selection.getStart(), selection.getEnd());
	}

	/** Get the value observer */
	public ValueObserver<V> getValueObserver() {
		return observer;
	}

	private void checkIfValid(String text) {
		if (fieldData.validData(text)) {
			getStyleClass().removeAll(BAD_FIELD);
			this.applyCss();
			valid = true;
			return;
		}
		valid = false;
		getStyleClass().add(BAD_FIELD);
	}
}
