package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVArray;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVRaw;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.RawChecker;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Toolkit;
import java.util.ArrayList;

/**
 Created by Kayler on 07/13/2016.
 */
public class ArrayValueEditor implements ValueEditor<SVArray> {

	private final ValueListener<SVRaw> editorValueUpdateListener = new ValueListener<SVRaw>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<SVRaw> observer, SVRaw oldValue, SVRaw newValue) {
			valueObserver.updateValue(createValue());
		}
	};
	private ArrayList<InputField> editors = new ArrayList<>();

	private static final double gap = 5;
	private static final int numEditorsPerRow = 3;
	private static final double tfPrefWidth = 100d;
	private final FlowPane editorsPane = new FlowPane(gap, gap);
	private final HBox masterPane;
	private final Button btnDecreaseSize, btnIncreaseSize;

	private final ValueObserver<SVArray> valueObserver = new ValueObserver<>(null);

	public ArrayValueEditor() {
		masterPane = new HBox(5);
		editorsPane.setPrefWrapLength(tfPrefWidth * numEditorsPerRow + gap * numEditorsPerRow); //have room for 3 text fields
		editorsPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		btnDecreaseSize = new Button("-");
		btnIncreaseSize = new Button("+");
		btnDecreaseSize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (editors.size() > 0) {
					removeLastEditor();
					valueObserver.updateValue(createValue());
				}
			}
		});
		btnIncreaseSize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (editors.size() > 0) {
					for (InputField inf : editors) {
						if (inf.getValue() == null || inf.getText().length() == 0) {
							inf.requestFocus();
							beep();
							return;
						}
					}
				}
				addNewEditor();
			}
		});

		addNullValueFiller(true);

		addSpaceFiller();
	}

	private void addNullValueFiller(boolean add) {
		masterPane.getChildren().clear();
		if (add) {
			Button btnInsert = new Button("{}");
			btnInsert.setFont(Font.font("monospace", 14));
			btnInsert.setOnAction(event -> {
				addNullValueFiller(false);
				valueObserver.updateValue(new SVArray());
			});
			masterPane.getChildren().add(btnInsert);
		} else {
			masterPane.getChildren().addAll(editorsPane, btnDecreaseSize, btnIncreaseSize);
		}
	}

	private void removeLastEditor() {
		InputField removed = editors.remove(editors.size() - 1);
		editorsPane.getChildren().remove(removed);
		if (editors.size() == 0) {
			addSpaceFiller();
		}
	}

	private void addSpaceFiller() {
		Label lbl = new Label("{}");
		lbl.setFont(Font.font(15));
		editorsPane.getChildren().add(lbl);
		Separator sep = new Separator(Orientation.HORIZONTAL);
		sep.setOpaqueInsets(new Insets(lbl.getHeight() / 2));
		sep.setPrefWidth(numEditorsPerRow * tfPrefWidth - lbl.getWidth() - 10);
		editorsPane.getChildren().add(sep);
	}

	private void addNewEditor() {
		if (editors.size() == 0) {
			//remove the {} label
			editorsPane.getChildren().clear();
		}
		InputField<RawChecker, SVRaw> in = new InputField<>(new RawChecker(null));
		in.setPrefWidth(tfPrefWidth);
		in.setValue(new SVRaw("", null));
		editors.add(in);
		editorsPane.getChildren().add(in);
		editorsPane.autosize();

		in.getValueObserver().addListener(editorValueUpdateListener);
	}

	private void beep() {
		Toolkit.getDefaultToolkit().beep();
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
			while (!editors.isEmpty()) {
				removeLastEditor();
			}
			addNullValueFiller(true);
			valueObserver.updateValue(null);
		} else {
			addNullValueFiller(false);
			int i = 0;
			String[] vals = val.getAsStringArray();
			while (editors.size() > vals.length) {
				removeLastEditor();
			}
			while (editors.size() < vals.length) {
				addNewEditor();
				i++;
			}
			i = 0;
			for (String s : vals) {
				editors.get(i++).setText(s);
			}
		}
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public boolean displayFullWidth() {
		return true;
	}
}
