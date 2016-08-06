package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.main.FXControlLang;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Base class for a text field control that checks if the inputted data is correct and returns the data as an object with type V
 Created on 05/31/2016. */
public class InputField<C extends InputFieldDataChecker<V>, V> extends StackPane {
	private static final String BAD_FIELD = "bad-input-text-field";
	private static final String DATA_NEEDS_SUBMITION = "-fx-background-color:green";
	private static final String DATA_BAD = "-fx-background-color:red";
	private static final String DATA_SUBMITTED = "";
	
	private final C dataChecker;
	private final ValueObserver<V> observer = new ValueObserver<>(null);
	private final TextField textField = new TextField();
	private final HBox hboxTextField = new HBox(2, textField);
	private final Button button = new Button();
	private final ErrorMsgPopup errorMsgPopup = new ErrorMsgPopup(this);
	private final Button btnSubmit = new Button("");
	
	private boolean valid = false;
	private boolean buttonState = true;
	private String errMsg;
	private boolean isError;
	
	/**
	 Creates a new InputField (TextField with additional features). The InputField has two states: "Input State" and "Button State".
	 <br><b>"Input State":</b>
	 <ul>
	 <li>A normal {@link TextField} is used as the underlying text input. Whatever {@link InputFieldDataChecker#getTypeName()} returns is what will be passed in {@link TextField#setPromptText(String)}</li>
	 <li>User can enter any text. When enter key is pressed, the text is checked to see if valid (via {@link InputFieldDataChecker#validData(String)}). If not valid, the text field will turn red and an error popup will appear with the error message.</li>
	 <li>If the inner TextField instance loses focus, the TextField has no data (no text), and {@link InputFieldDataChecker#allowEmptyData()} is false, and {@link InputFieldDataChecker#getDefaultValue()}==null, the text field will enter Button State.<br>
	 The scenario where {@link InputFieldDataChecker#getDefaultValue()}!=null, TextField has no data, and the TextField loses focus, the InputField will never enter the Button State. {@link InputFieldDataChecker#getDefaultValue()} will only be used for when the InputField/TextField loses focus.
	 If there can be no data (no text), the InputField will stay in Input State.</li>
	 </ul>
	 <br><b>"Button State":</b>
	 <ul>
	 <li>The Button State is used to show that no text has been inputted in the TextField and that the InputField can't have "" has valid input.</li>
	 <li>The InputField will become a {@link Button} and {@link InputFieldDataChecker#getTypeName()} will be the text inside the button. The user's mouse will become {@link Cursor#TEXT} when hovered over the button.
	 When the button is pressed, the InputField will enter Input State</li>
	 </ul>
	 The InputField will update it's value when:<br>
	 <ul>
	 <li>When submit button is pressed</li>
	 <li>Pressing the 'enter' key</li>
	 </ul>
	 
	 @param fieldDataChecker the {@link InputFieldDataChecker} instance to use
	 */
	public InputField(@NotNull C fieldDataChecker) {
		this.dataChecker = fieldDataChecker;
		HBox.setHgrow(textField, Priority.ALWAYS);
		btnSubmit.setPrefWidth(10d);
		btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setValueFromText(getText(), true, false);
			}
		});
		btnSubmit.setTooltip(new Tooltip(FXControlLang.InputField.SUBMIT_BTN_TOOLTIP));
		this.hboxTextField.getChildren().add(btnSubmit);
		textField.setStyle("-fx-background-radius:0px");
		
		EventHandler<KeyEvent> keyEvent = new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					setValueFromText(getText(), true, false);
				} else {
					btnSubmit.setStyle(DATA_NEEDS_SUBMITION);
				}
			}
		};
		setPromptText(fieldDataChecker.getTypeName());
		setTooltip(new Tooltip(fieldDataChecker.getTypeName()));
		this.setOnKeyReleased(keyEvent);
		this.setOnKeyTyped(keyEvent);
		
		textField.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (isError) {
					errorMsgPopup.showPopup();
				}
			}
		});
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean focused) {
				if (!focused && (getText().length() == 0 && !dataChecker.allowEmptyData())) {
					if (dataChecker.getDefaultValue() != null) {
						getValueObserver().updateValue(dataChecker.getDefaultValue());
					} else {
						clear();
						getValueObserver().updateValue(null);
					}
					return;
				}
				if (focused) {
					setToButton(false);
					return;
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
	
	/**
	 Creates an InputField with the value set to defaultValue
	 
	 @see #InputField(InputFieldDataChecker)
	 */
	public InputField(@NotNull C fieldDataChecker, @Nullable V defaultValue) {
		this(fieldDataChecker);
		setValue(defaultValue);
	}
	
	/**
	 Get the text parsed and converted into type V. This will only return whatever the generic type E outputs from {@link InputFieldDataChecker#parse(String)}.
	 If no text was inputted and the InputFieldDataChecker doesn't allow empty data, will return null. Also, if the InputField is in Button State, will return null.
	 */
	@Nullable
	public V getValue() {
		if (buttonState) {
			return null;
		}
		if (!valid) {
			return null;
		}
		return dataChecker.parse(this.getText());
	}
	
	/** Return true if the data inside the text field is valid, false otherwise. If the InputField is in the Button State, will return false. */
	public boolean hasValidData() {
		return !buttonState && valid;
	}
	
	/**
	 Set the value from an object. The text in the control is set to whatever {@link V#toString()} returns. If value given is null, will set field to Button State.
	 No matter what is passed, the internal {@link ValueObserver} instance will be notified of the value update.
	 */
	public void setValue(@Nullable V value) {
		observer.updateValue(value);
		if (value == null) {
			clear();
			valid = false;
			return;
		}
		btnSubmit.setStyle(DATA_SUBMITTED);
		this.setText(value.toString());
		setToButton(false);
		valid = true;
		error(false);
	}
	
	/**
	 Set the value from text. If text==null, the InputField will be set to the Button State and the InputField's {@link ValueObserver#getValue()} will be null.<br>
	 If text != null, the text will be checked to see if valid (via {@link InputFieldDataChecker#validData(String)}.<br>
	 If the text is valid, the value will be set to whatever {@link InputFieldDataChecker#parse(String)} returns with text passed as the parameter.<br>
	 If the text is not valid, the inner {@link TextField} instance will turn red and a popup will appear with the error message and the {@link ValueObserver} will update value to null
	 */
	public void setValueFromText(@Nullable String text) {
		setValueFromText(text, true, true);
	}
	
	private void setValueFromText(@Nullable String text, boolean updateCursor, boolean updateValueIfNotValid) {
		if (text == null) {
			setValue(null);
			return;
		}
		checkIfValid(text);
		int cursorPosition = textField.getCaretPosition();
		IndexRange selection = textField.getSelection();
		if (valid) {
			setText(text);
			setValue(getValue());
		} else {
			setToButton(false);
			if (updateValueIfNotValid) {
				observer.updateValue(null);
			}
			error(true);
			setText(text);
		}
		if (updateCursor) {
			textField.positionCaret(cursorPosition);
			textField.selectRange(selection.getStart(), selection.getEnd());
		}
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
	
	/** Clears the text and the error (also hides the error popup). Then sets the InputField to Button State. */
	public void clear() {
		error(false);
		setToButton(true);
	}
	
	/** Sets the text without updating the error state or value observer. If text is null, will be put into button state. */
	public void setText(@Nullable String text) {
		if (text == null) {
			clear();
		} else {
			textField.setText(text);
		}
	}
	
	/** Get the inputted text, or empty String if in Button State (may also not be in Button State and the text is actually just ""). */
	@NotNull
	public String getText() {
		if (buttonState) {
			return "";
		}
		if (textField.getText() == null) {
			textField.setText("");
		}
		return textField.getText();
	}
	
	/** Set the prompt text of the text field and the text of the button */
	public void setPromptText(String text) {
		textField.setPromptText(text);
		button.setText(text);
	}
	
	/** Set the tooltip for the inner {@link TextField} and {@link Button} */
	public void setTooltip(Tooltip tooltip) {
		textField.setTooltip(tooltip);
		button.setTooltip(tooltip);
	}
	
	private boolean checkIfValid(@NotNull String text) {
		errMsg = dataChecker.validData(text);
		valid = (errMsg == null);
		return valid;
	}
	
	/** Set the state of the InputField. if toButton==true, will set to Button state. If toButton == false, will set to TextField state. */
	public void setToButton(boolean toButton) {
		if (buttonState == toButton) {
			return;
		}
		buttonState = toButton;
		if (toButton) {
			getChildren().remove(hboxTextField);
			getChildren().add(button);
		} else {
			getChildren().remove(button);
			getChildren().add(hboxTextField);
			textField.requestFocus();
		}
	}
	
	@Override
	public void requestFocus() {
		if (buttonState) {
			button.requestFocus();
		} else {
			textField.requestFocus();
		}
	}
	
	/**
	 If e==true:
	 <ul>
	 <li>set text field background color to red</li>
	 <li>show error popup with message</li>
	 </ul>
	 If e==false:
	 <ul>
	 <li>set text field background color to normal</li>
	 <li>hide error popup</li>
	 </ul>
	 */
	private void error(boolean e) {
		this.isError = e;
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
			this.setAutoHide(true);
			
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
