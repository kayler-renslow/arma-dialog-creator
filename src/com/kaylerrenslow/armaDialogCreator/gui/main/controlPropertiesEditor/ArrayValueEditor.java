package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVArray;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.ArmaStringChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 Created by Kayler on 07/13/2016.
 */
public class ArrayValueEditor implements ValueEditor<SVArray> {

	private final ValueListener<String> editorValueUpdateListener = new ValueListener<String>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
			valueObserver.updateValue(createValue());
		}
	};
	protected ArrayList<InputField<ArmaStringChecker, String>> editors = new ArrayList<>();

	protected final Button btnDecreaseSize = new Button("-");
	protected final Button btnIncreaseSize = new Button("+");
	private final double gap = 5;
	private final double tfPrefWidth = 100d;
	private final FlowPane editorsPane = new FlowPane(gap, gap);
	private final HBox masterPane;

	private final ValueObserver<SVArray> valueObserver = new ValueObserver<>(null);

	public ArrayValueEditor(int numInitialFields) {
		masterPane = new HBox(5, editorsPane);
		editorsPane.minWidth(0d);
		editorsPane.prefWidth(0d);
		editorsPane.setPrefWrapLength(tfPrefWidth * 3 + gap * numInitialFields); //have room for 3 text fields
		masterPane.minWidth(0d);

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
				InputField<ArmaStringChecker, String> in = getTextField();
				editors.add(in);
				editorsPane.getChildren().add(in);
				editorsPane.autosize();

				addEditorValueUpdateListener();
			}
		});
		InputField<ArmaStringChecker, String> in;
		for (int i = 0; i < numInitialFields; i++) {
			in = getTextField();
			editors.add(in);
			editorsPane.getChildren().add(in);
		}
		editorsPane.autosize();

		addEditorValueUpdateListener();
	}

	private void addEditorValueUpdateListener() {
		for (InputField<ArmaStringChecker, String> inf : editors) {
			inf.getValueObserver().addListener(editorValueUpdateListener);
		}
	}

	@Override
	public void focusToEditor() {
		if (editors.size() == 0) {
			return;
		}
		for (InputField tf : editors) {
			if (tf.getValue() == null) {
				tf.requestFocus();
				return;
			}
		}
		editors.get(0).requestFocus();
	}

	@NotNull
	@Override
	public ReadOnlyValueObserver<SVArray> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}

	private InputField<ArmaStringChecker, String> getTextField() {
		InputField<ArmaStringChecker, String> tf = new InputField<>(new ArmaStringChecker());
		tf.setPrefWidth(tfPrefWidth);
		return tf;
	}

	@Override
	public void submitCurrentData() {
		for (InputField tf : editors) {
			tf.submitValue();
		}
	}

	@Override
	public SVArray getValue() {
		return valueObserver.getValue();
	}

	@Nullable
	private SVArray createValue() {
		String[] values = new String[editors.size()];
		int i = 0;
		for (InputField tf : editors) {
			if (tf.getValue() == null) {
				return null;
			}
			values[i++] = tf.getText();
		}
		return new SVArray(values);
	}

	@Override
	public void setValue(SVArray val) {
		if (val == null) {
			for (InputField<ArmaStringChecker, String> editor : editors) {
				editor.setValue(null);
			}
		} else {
			int i = 0;
			for (String s : val.getAsStringArray()) {
				editors.get(i++).setValue(s);
			}
		}
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public boolean displayFullWidth() {
		return false;
	}
}
