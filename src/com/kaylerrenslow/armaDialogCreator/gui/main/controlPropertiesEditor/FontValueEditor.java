package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.AFont;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.StringChecker;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
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
public class FontValueEditor implements ValueEditor<AFont> {
	private final Button btnChooseDefault = new Button(Lang.ApplicationBundle().getString("ValueEditors.FontValueEditor.default_font"));
	protected final ComboBox<AFont> comboBox = new ComboBox<>(FXCollections.observableArrayList(AFont.values()));
	private final HBox editorHbox = new HBox(5, comboBox, btnChooseDefault);
	private final InputField<StringChecker, String> overrideField = new InputField<>(new StringChecker());
	private final StackPane masterPane = new StackPane(editorHbox);
	private final ValueObserver<AFont> valueObserver = new ValueObserver<>(null);

	public FontValueEditor() {
		btnChooseDefault.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				comboBox.getSelectionModel().select(AFont.DEFAULT);
			}
		});
		comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AFont>() {
			@Override
			public void changed(ObservableValue<? extends AFont> observable, AFont oldValue, AFont newValue) {
				valueObserver.updateValue(newValue);
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	public AFont getValue() {
		return comboBox.getValue();
	}

	@Override
	public void setValue(AFont val) {
		comboBox.setValue(val);
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public void setToCustomData(boolean override) {
		masterPane.getChildren().clear();
		if(override){
			masterPane.getChildren().add(overrideField);
		}else{
			masterPane.getChildren().add(editorHbox);
		}
	}

	@Override
	public InputField<StringChecker, String> getCustomDataTextField() {
		return overrideField;
	}
	
	@Override
	public void focusToEditor() {
		comboBox.requestFocus();
	}

	@Override
	public boolean displayFullWidth() {
		return false;
	}

	@Override
	public ReadOnlyValueObserver<AFont> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}
}
