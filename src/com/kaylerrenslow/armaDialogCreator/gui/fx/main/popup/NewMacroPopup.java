package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.AHexColor;
import com.kaylerrenslow.armaDialogCreator.data.Macro;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


/**
 @author Kayler
 Popup for creating a new macro.
 Created on 07/10/2016. */
public class NewMacroPopup extends StagePopup<VBox> {
	private ValueEditor editor;

	private StackPane stackPaneEditor = new StackPane();

	private TextField tfMacroDescription = new TextField();

	public NewMacroPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), new Stage(), new VBox(5), Lang.Popups.NewMacro.POPUP_TITLE);
		myRootElement.setPadding(new Insets(10));
		stackPaneEditor.minWidth(0d);
		stackPaneEditor.setAlignment(Pos.CENTER_LEFT);

		stackPaneEditor.getChildren().add(new Label(Lang.Popups.NewMacro.NO_TYPE_CHOSEN));

		InputField<IdentifierFieldDataChecker, String> inMacroKey = new InputField<>(new IdentifierFieldDataChecker());
		EventHandler<? super KeyEvent> oldEvent = inMacroKey.getOnKeyReleased();
		inMacroKey.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				int cursorPosition = inMacroKey.getCaretPosition();
				IndexRange selection = inMacroKey.getSelection();

				if (inMacroKey.getText() != null) {
					inMacroKey.setText(inMacroKey.getText().toUpperCase().replaceAll("\\s", "_"));
				}

				inMacroKey.positionCaret(cursorPosition);
				inMacroKey.selectRange(selection.getStart(), selection.getEnd());

				if (oldEvent != null) {
					oldEvent.handle(event);
				}
			}
		});
		inMacroKey.setOnKeyTyped(inMacroKey.getOnKeyReleased());

		ChoiceBox<ControlProperty.PropertyType> cbMacroType = new ChoiceBox<>();
		cbMacroType.getItems().addAll(ControlProperty.PropertyType.values());

		cbMacroType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ControlProperty.PropertyType>() {
			@Override
			public void changed(ObservableValue<? extends ControlProperty.PropertyType> observable, ControlProperty.PropertyType oldValue, ControlProperty.PropertyType selected) {
				editor = getEditor(selected);
				stackPaneEditor.getChildren().clear();
				stackPaneEditor.getChildren().add(editor.getEditorNode());
			}
		});

		VBox vbTop = new VBox(5);
		vbTop.setFillWidth(true);
		vbTop.getChildren().addAll(hbox(Lang.Popups.NewMacro.MACRO_KEY, inMacroKey), hbox(Lang.Popups.NewMacro.MACRO_TYPE, cbMacroType), hbox(Lang.Popups.NewMacro.MACRO_VALUE, stackPaneEditor), hbox(Lang.Popups.NewMacro.MACRO_COMMENT, tfMacroDescription));
		myRootElement.getChildren().add(vbTop);
		VBox.setVgrow(vbTop, Priority.ALWAYS);

		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.initStyle(StageStyle.UTILITY);
		myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(true, true ,false));

		myStage.setMinWidth(320d);
		myStage.setWidth(480d);
		myStage.setHeight(200);
//		vbTop.prefWidth(720d);
//		vbTop.prefHeight(480d);
	}

	private static HBox hbox(String text, Node graphic) {
		Label lbl = new Label(text);
		HBox.setHgrow(graphic, Priority.ALWAYS);
		return new HBox(5, lbl, graphic);
	}

	@Nullable
	public Macro getCreatedMacro() {
		return null;
		//		return editor != null ? editor.getValue() ? null;
	}

	private ValueEditor getEditor(ControlProperty.PropertyType propertyType) {
		switch (propertyType) {
			case INT:
				return new InputFieldValueEditor<>(new IntegerFieldDataChecker());
			case FLOAT:
				return new InputFieldValueEditor<>(new DoubleFieldDataChecker());
			case BOOLEAN:
				return new BooleanValueEditor();
			case STRING:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case ARRAY:
				return new ArrayValueEditor(true, new ArmaStringFieldDataChecker(), new ArmaStringFieldDataChecker());
			case COLOR:
				return new ColorValueEditor();
			case SOUND:
				return new ArrayValueEditor(false, new ArmaStringFieldDataChecker(), new DoubleFieldDataChecker(), new DoubleFieldDataChecker());
			case FONT:
				return new FontValueEditor();
			case FILE_NAME:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case IMAGE:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case HEX_COLOR_STRING:
				return new HexColorValueEditor();
			case TEXTURE:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case EVENT:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
			case SQF:
				return new InputFieldValueEditor<>(new ArmaStringFieldDataChecker());
		}
		throw new IllegalStateException("Should have made a match");
	}

	private interface ValueEditor<V> {
		V getValue();

		void setValue(V val);

		Node getEditorNode();
	}

	private static class InputFieldValueEditor<V> implements ValueEditor<V> {
		private final InputField<InputFieldDataChecker<V>, V> editor;

		public InputFieldValueEditor(InputFieldDataChecker<V> dataChecker) {
			this.editor = new InputField<>(dataChecker);
		}

		@Override
		public V getValue() {
			return editor.getValue();
		}

		@Override
		public void setValue(V val) {
			editor.setValue(val);
		}

		@Override
		public Node getEditorNode() {
			return editor;
		}
	}

	private static class BooleanValueEditor implements ValueEditor<Boolean> {
		private final ChoiceBox<Boolean> editor = new ChoiceBox<>(FXCollections.observableArrayList(true, false));

		@Override
		public Boolean getValue() {
			return editor.getValue();
		}

		@Override
		public void setValue(Boolean val) {
			editor.setValue(val);
		}

		@Override
		public Node getEditorNode() {
			return editor;
		}
	}

	private static class ArrayValueEditor implements ValueEditor<String[]> {

		private ArrayList<InputField> editors = new ArrayList<>();

		private final Button btnDecreaseSize = new Button("-");
		private final Button btnIncreaseSize = new Button("+");
		private final FlowPane editorsPane = new FlowPane(5, 5);
		private final HBox masterPane;

		public ArrayValueEditor(boolean canChangeSize, InputFieldDataChecker<?>... checkers) {
			masterPane = new HBox(5, editorsPane);
			editorsPane.minWidth(0d);
			editorsPane.prefWidth(0d);
			masterPane.minWidth(0d);
			if (canChangeSize) {
				masterPane.getChildren().addAll(btnDecreaseSize, btnIncreaseSize);
				btnDecreaseSize.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (editors.size() > 1) {
							InputField removed = editors.remove(editors.size() - 1);
							editorsPane.getChildren().remove(removed);
						}
					}
				});
				btnIncreaseSize.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						InputField in = new InputField<>(new ArmaStringFieldDataChecker());
						editors.add(in);
						editorsPane.getChildren().add(in);
						editorsPane.autosize();
					}
				});
			}
			InputField in;
			for (InputFieldDataChecker<?> checker : checkers) {
				in = new InputField<>(checker);
				editors.add(in);
				editorsPane.getChildren().add(in);
			}
			editorsPane.autosize();
		}

		@Override
		public String[] getValue() {
			String[] values = new String[editors.size()];
			int i = 0;
			for (InputField inputField : editors) {
				values[i++] = inputField.getText();
			}
			return values;
		}

		@Override
		public void setValue(String[] val) {
			int i = 0;
			for (String s : val) {
				editors.get(i++).setValueFromText(s);
			}
		}

		@Override
		public Node getEditorNode() {
			return masterPane; //need to include decrease/increase buttons
		}
	}

	private static class ColorValueEditor implements ValueEditor<AColor> {
		private final ColorPicker editor = new ColorPicker();

		@Override
		public AColor getValue() {
			return editor.getValue() == null ? null : new AColor(editor.getValue());
		}

		@Override
		public void setValue(AColor val) {
			editor.setValue(val.toJavaFXColor());
		}

		@Override
		public Node getEditorNode() {
			return editor;
		}
	}

	private static class FontValueEditor implements ValueEditor<AFont> {
		private final Button btnChooseDefault = new Button(Lang.Misc.DEFAULT_FONT);
		private final ComboBox<AFont> editor = new ComboBox<>(FXCollections.observableArrayList(AFont.values()));
		private final HBox editorNode = new HBox(5, editor, btnChooseDefault);

		public FontValueEditor() {
			btnChooseDefault.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					editor.getSelectionModel().select(AFont.DEFAULT);
				}
			});
		}

		@Override
		public AFont getValue() {
			return editor.getValue();
		}

		@Override
		public void setValue(AFont val) {
			editor.setValue(val);
		}

		@Override
		public Node getEditorNode() {
			return editorNode;
		}
	}

	private static class HexColorValueEditor implements ValueEditor<AHexColor> {
		private final ColorPicker editor = new ColorPicker();

		@Override
		public AHexColor getValue() {
			return new AHexColor(editor.getValue());
		}

		@Override
		public void setValue(AHexColor val) {
			editor.setValue(val.toJavaFXColor());
		}

		@Override
		public Node getEditorNode() {
			return editor;
		}
	}

}
