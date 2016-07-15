package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Base class for a text field control that checks if the inputted data is correct and returns the data as an object with type V
 Created on 05/31/2016. */
public class InputField<T extends InputFieldDataChecker<V>, V> extends StackPane {
	private static final String BAD_FIELD = "bad-input-text-field";
	private final T dataChecker;
	private final ValueObserver<V> observer = new ValueObserver<>(null);
	private final TextField textField = new TextField();
	private final Button button = new Button();
	private final ErrorMsgPopup errorMsgPopup = new ErrorMsgPopup(this);

	private boolean valid = false;
	private boolean buttonState = true;
	private String errMsg;

	/** Creates a new InputField (TextField with additional features). The prompt text will be set to whatever fieldDataChecker.getTypeName() returns */
	public InputField(@NotNull T fieldDataChecker) {
		this.dataChecker = fieldDataChecker;
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
				if (!focused && !valid && (getText() != null && getText().length() == 0 && !fieldDataChecker.allowEmptyData())) {
					setToButton(true);
				}
				if (!focused && valid) {
					setValue(getValue(getText()));
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
				if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
					button.setCursor(Cursor.TEXT);
				} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
					button.setCursor(Cursor.DEFAULT);
				}
			}
		};
		button.setOnMouseEntered(mouseEvent);

		button.setMaxWidth(Double.MAX_VALUE);
		getChildren().add(button);
	}

	/** Creates a new InputField (TextField with additional features). The prompt text will be set to whatever fieldDataChecker.getTypeName() returns. Also, set the initial value equal to defaultValue */
	public InputField(@NotNull T fieldDataChecker, @Nullable V defaultValue) {
		this(fieldDataChecker);
		setValue(defaultValue);
	}

	/** Get the text parsed and converted into type V. This will only return whatever the generic type E outputs from IInputFieldDataChecker.parse(String data). If no text was inputted, will return null. */
	@Nullable
	public V getValue() {
		if (!dataChecker.allowEmptyData() && getText().length() == 0) {
			return null;
		}
		return dataChecker.parse(this.getText());
	}

	/** Convert text into type V */
	@Nullable
	private V getValue(String text) {
		return dataChecker.parse(text);
	}

	/** Return true if the data inside the text field is valid, false otherwise */
	public boolean hasValidData() {
		checkIfValid(getText());
		return valid;
	}

	/** Set the value from an object. The text in the control is set to whatever V.toString() (generic type V) returns. If value given is null, will set field to button state */
	public void setValue(@Nullable V value) {
		String toString = value == null ? "" : value.toString();
		if (!dataChecker.allowEmptyData() && toString.length() == 0) {
			clear();
			return;
		}
		observer.updateValue(value);
		this.setText(toString);
		setToButton(false);
		valid = true;
		error(false);
	}

	/** Set the value from text. The value will only be set if the text is valid. If text is null, will set to button state */
	public void setValueFromText(@Nullable String text) {
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

	/** @see TextField#getCaretPosition() */
	public int getCaretPosition() {
		return textField.getCaretPosition();
	}

	/** @see TextField#getSelection() */
	public IndexRange getSelection() {
		return textField.getSelection();
	}

	/** @see TextField#positionCaret(int) */
	public void positionCaret(int position) {
		textField.positionCaret(position);
	}

	/** @see TextField#selectRange(int, int) */
	public void selectRange(int start, int end) {
		textField.selectRange(start, end);
	}

	/** @see TextField#selectAll() */
	public void selectAll() {
		textField.selectAll();
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

	/** Get the inputted text, or null if in button state */
	@NotNull
	public String getText() {
		if (buttonState) {
			return "";
		}
		return textField.getText() == null ? "" : textField.getText();
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
		errMsg = dataChecker.validData(text);
		valid = errMsg == null;
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
			errorMsgPopup.setMsg(errMsg);
			errorMsgPopup.showPopup();
		} else {
			textField.getStyleClass().removeAll(BAD_FIELD);
			errorMsgPopup.hide();
		}

		this.applyCss();
	}

	private static class ErrorMsgPopup extends Popup {
		private final InputField inputField;
		private Label lblMsg = new Label();

		public ErrorMsgPopup(InputField inputField) {
			this.inputField = inputField;
			StackPane stackPane = new StackPane(lblMsg);
			stackPane.setPadding(new Insets(5));
			stackPane.setAlignment(Pos.BOTTOM_CENTER);
			stackPane.setStyle("-fx-background-color:red;");
			lblMsg.setTextFill(Color.WHITE);

			getContent().add(stackPane);
		}

		public void setMsg(@NotNull String msg) {
			this.lblMsg.setText(msg);
		}

		public void showPopup() {
			Point2D p = inputField.localToScreen(0, -inputField.getHeight());
			show(inputField, p.getX(), p.getY());
		}
	}
}
