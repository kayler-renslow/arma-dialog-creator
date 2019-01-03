package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.control.sv.SVFont;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.ReadOnlyValueObserver;
import com.armadialogcreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/13/2016.
 */
public class FontValueEditor implements ValueEditor<SVFont> {
	private final Button btnChooseDefault = new Button(Lang.ApplicationBundle().getString("ValueEditors.FontValueEditor.default_font"));
	protected final ComboBox<SVFont> comboBox = new ComboBox<>(FXCollections.observableArrayList(SVFont.values()));
	private final HBox editorHbox = new HBox(5, comboBox, btnChooseDefault);
	private final StackPane masterPane = new StackPane(editorHbox);
	private final ValueObserver<SVFont> valueObserver = new ValueObserver<>(null);

	public FontValueEditor() {
		btnChooseDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				comboBox.getSelectionModel().select(SVFont.DEFAULT);
			}
		});
		comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SVFont>() {
			@Override
			public void changed(ObservableValue<? extends SVFont> observable, SVFont oldValue, SVFont newValue) {
				valueObserver.updateValue(newValue);
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	public SVFont getValue() {
		return comboBox.getValue();
	}

	@Override
	public void setValue(SVFont val) {
		comboBox.setValue(val);
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public void focusToEditor() {
		comboBox.requestFocus();
	}

	@Override
	public boolean displayFullWidth() {
		return false;
	}

	@NotNull
	@Override
	public ReadOnlyValueObserver<SVFont> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}
}
