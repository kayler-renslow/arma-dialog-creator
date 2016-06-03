package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Base class for a text field control that checks if the inputted data is correct and returns the data as an object with type V
 Created on 05/31/2016. */
public class InputField<E extends IInputFieldDataChecker<V>, V> extends HBox {
	private static final String BAD_FIELD = "bad-input-text-field";
	private final E fieldData;
	private boolean valid = true;
	private ValueObserver<V> observer = new ValueObserver<>(null);
	private TextField textField = new TextField();
	private boolean buttonState = true;
	private Button button = new Button();

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

		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean focused) {
				checkIfValid(getText());
				if (!focused && textField.getText().length() == 0 && !valid) {
					setToButton(true);
				}
				if(!focused){
					setValueFromText(getText());
				}
			}
		});

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setText("");
				setToButton(false);
			}
		});

		EventHandler<MouseEvent> mouseEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getEventType() == MouseEvent.MOUSE_ENTERED){
					button.setCursor(Cursor.TEXT);
				}else if(event.getEventType() == MouseEvent.MOUSE_EXITED){
					button.setCursor(Cursor.DEFAULT);
				}
			}
		};
		button.setOnMouseEntered(mouseEvent);

		button.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(button, Priority.ALWAYS);
		HBox.setHgrow(textField, Priority.ALWAYS);
		getChildren().add(button);
	}

	/** Get the text parsed and converted into type V. This will only return whatever the generic type E outputs from IInputFieldDataChecker.parse(String data). If no text was inputted, will return null. */
	@Nullable
	public V getValue() {
		if (getText() == null || this.getText().length() == 0) {
			return null;
		}
		return fieldData.parse(this.getText());
	}

	/** Return true if the data inside the text field is valid, false otherwise */
	public boolean hasValidData() {
		checkIfValid(getText());
		return valid;
	}

	/** Set the value from an object. The text in the control is set to whatever V.toString() (generic type V) returns. If value given is null, will set field to button state */
	public void setValue(@Nullable V value) {
		if (value == null) {
			clear();
			return;
		}
		this.setText(value.toString());
		observer.updateValue(value);
		setToButton(false);
		valid = true;
		error(false);
	}

	/** Set the value from text. The value will only be set if the text is valid. If text is null, will set to button state */
	public void setValueFromText(@Nullable String text) {
		if (text == null) {
			clear();
			return;
		}
		int cursorPosition = textField.getCaretPosition();
		IndexRange selection = textField.getSelection();
		checkIfValid(text);
		if (valid) {
			setText(text);
			setValue(getValue());
			setToButton(false);
		}
		error(!valid);
		textField.positionCaret(cursorPosition);
		textField.selectRange(selection.getStart(), selection.getEnd());
	}

	/** Get the value observer */
	public ValueObserver<V> getValueObserver() {
		return observer;
	}

	/** Clears the text and the error. Then sets the field to button state. */
	public void clear() {
		error(false);
		setToButton(true);
	}

	/** Sets the text without updating the error state. If text is null, will be put into buttons state. */
	public void setText(@Nullable String text) {
		if (text == null) {
			clear();
		} else {
			textField.setText(text);
		}
	}

	@Nullable
	/**Get the inputted text, or null if in button state*/
	public String getText() {
		if (buttonState) {
			return null;
		}
		return textField.getText();
	}

	/** Set the prompt text of the text field and the text of the button */
	public void setPromptText(String text) {
		textField.setPromptText(text);
		button.setText(text);
	}

	/** Set the tooltip */
	public void setTooltip(Tooltip tooltip) {
		textField.setTooltip(tooltip);
		button.setTooltip(tooltip);
	}

	private void checkIfValid(String text) {
		if (text != null && fieldData.validData(text)) {
			valid = true;
			return;
		}
		valid = false;
	}

	private void setToButton(boolean toButton) {
		if (buttonState == toButton) {
			return;
		}
		buttonState = toButton;
		if (toButton) {
			getChildren().remove(textField);
			getChildren().add(button);
		} else {
			getChildren().remove(button);
			getChildren().add(textField);
			textField.requestFocus();
		}
	}

	private void error(boolean e) {
		if (e) {
			textField.getStyleClass().add(BAD_FIELD);
		} else {
			textField.getStyleClass().removeAll(BAD_FIELD);
		}
		this.applyCss();
	}
}
