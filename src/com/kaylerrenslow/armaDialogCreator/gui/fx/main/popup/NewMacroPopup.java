package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.AHexColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.ASound;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


/**
 @author Kayler
 Popup for creating a new macro.
 Created on 07/10/2016. */
public class NewMacroPopup extends StagePopup<VBox> {
	private ValueEditor editor;

	private StackPane stackPaneEditor = new StackPane();

	private final TextField tfMacroDescription = new TextField();
	private final InputField<IdentifierFieldDataChecker, String> inMacroKey = new InputField<>(new IdentifierFieldDataChecker());

	public NewMacroPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), new Stage(), new VBox(5), Lang.Popups.NewMacro.POPUP_TITLE);
		myRootElement.setPadding(new Insets(10));
		stackPaneEditor.minWidth(0d);
		stackPaneEditor.setAlignment(Pos.CENTER_LEFT);

		stackPaneEditor.getChildren().add(new Label(Lang.Popups.NewMacro.NO_TYPE_CHOSEN));

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
				stackPaneEditor.getChildren().add(editor.getNode());
			}
		});

		VBox vbTop = new VBox(5);
		vbTop.setFillWidth(true);
		vbTop.getChildren().addAll(hbox(Lang.Popups.NewMacro.MACRO_KEY, inMacroKey), hbox(Lang.Popups.NewMacro.MACRO_TYPE, cbMacroType), hbox(Lang.Popups.NewMacro.MACRO_VALUE, stackPaneEditor), hbox(Lang.Popups.NewMacro.MACRO_COMMENT, tfMacroDescription));
		myRootElement.getChildren().add(vbTop);
		VBox.setVgrow(vbTop, Priority.ALWAYS);

		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.initStyle(StageStyle.UTILITY);
		myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(true, true, false));

		myStage.setMinWidth(480d);
		myStage.setWidth(500d);
		myStage.setHeight(240);
	}

	private static HBox hbox(String text, Node graphic) {
		Label lbl = new Label(text);
		HBox.setHgrow(graphic, Priority.ALWAYS);
		return new HBox(5, lbl, graphic);
	}

	/**Return the macro that has been created. (should be invoked after the popup has closed)*/
	@Nullable
	private Macro getCreatedMacro() {
		if (editor == null || editor.getValue() == null || inMacroKey.getValue() == null) {
			return null;
		}
		Macro m = new Macro<>(inMacroKey.getValue(), editor.getValue());
		m.setComment(tfMacroDescription.getText());
		return m;
	}

	@Override
	protected void ok() {
		Macro macro = getCreatedMacro();
		if (macro != null) {
			ArmaDialogCreator.getApplicationData().getMacroRegistry().getMacros().add(macro);
		}
		close();
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
				return new ArrayValueEditor(2);
			case COLOR:
				return new ColorValueEditor();
			case SOUND:
				return new SoundValueEditor();
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

		@Nullable V getValue();

		void setValue(@Nullable V val);

		@NotNull Node getNode();
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

		@NotNull
		@Override
		public Node getNode() {
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

		@NotNull
		@Override
		public Node getNode() {
			return editor;
		}
	}

	private static class ArrayValueEditor implements ValueEditor<String[]> {

		private ArrayList<TextField> editors = new ArrayList<>();

		private final Button btnDecreaseSize = new Button("-");
		private final Button btnIncreaseSize = new Button("+");
		private final double gap = 5;
		private final FlowPane editorsPane = new FlowPane(gap, gap);
		private final HBox masterPane;

		public ArrayValueEditor(int numInitialFields) {
			masterPane = new HBox(5, editorsPane);
			editorsPane.minWidth(0d);
			editorsPane.prefWidth(0d);
			editorsPane.setPrefWrapLength(300d + 5 * numInitialFields);
			masterPane.minWidth(0d);

			masterPane.getChildren().addAll(btnDecreaseSize, btnIncreaseSize);
			btnDecreaseSize.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (editors.size() > 1) {
						TextField removed = editors.remove(editors.size() - 1);
						editorsPane.getChildren().remove(removed);
					}
				}
			});
			btnIncreaseSize.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					TextField tf = getTextField();
					editors.add(tf);
					editorsPane.getChildren().add(tf);
					editorsPane.autosize();
				}
			});
			TextField tf;
			for (int i = 0; i < numInitialFields; i++) {
				tf = getTextField();
				editors.add(tf);
				editorsPane.getChildren().add(tf);
			}
			editorsPane.autosize();
		}

		private TextField getTextField() {
			TextField tf = new TextField();
			tf.setPrefWidth(100d);
			return tf;
		}

		@Override
		public String[] getValue() {
			String[] values = new String[editors.size()];
			int i = 0;
			for (TextField tf : editors) {
				values[i++] = tf.getText();
			}
			return values;
		}

		@Override
		public void setValue(String[] val) {
			int i = 0;
			for (String s : val) {
				editors.get(i++).setText(s);
			}
		}

		@NotNull
		@Override
		public Node getNode() {
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

		@NotNull
		@Override
		public Node getNode() {
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

		@NotNull
		@Override
		public Node getNode() {
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

		@NotNull
		@Override
		public Node getNode() {
			return editor;
		}
	}

	private class SoundValueEditor implements ValueEditor<ASound> {

		private InputField<ArmaStringFieldDataChecker, String> inSoundName = new InputField<>(new ArmaStringFieldDataChecker());
		private InputField<DoubleFieldDataChecker, Double> inDb = new InputField<>(new DoubleFieldDataChecker());
		private InputField<DoubleFieldDataChecker, Double> inPitch = new InputField<>(new DoubleFieldDataChecker());

		private FlowPane flowPane = new FlowPane(5, 10, inSoundName, inDb, inPitch);

		public SoundValueEditor() {
			flowPane.setPrefWrapLength(300d);
		}

		@Override
		public ASound getValue() {
			if (inSoundName.getValue() == null) {
				return null;
			}
			if (inDb.getValue() == null) {
				return null;
			}
			if (inPitch.getValue() == null) {
				return null;
			}
			return new ASound(inSoundName.getValue(), inDb.getValue(), inPitch.getValue());
		}

		@Override
		public void setValue(ASound val) {
			inSoundName.setValue(val.getSoundName());
			inDb.setValue(val.getDb());
			inPitch.setValue(val.getPitch());
		}

		@NotNull
		@Override
		public Node getNode() {
			return flowPane;
		}
	}
}
