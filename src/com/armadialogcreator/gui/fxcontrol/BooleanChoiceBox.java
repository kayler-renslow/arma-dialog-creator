package com.armadialogcreator.gui.fxcontrol;

import com.armadialogcreator.util.ValueListener;
import com.armadialogcreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 09/16/2016.
 */
public class BooleanChoiceBox extends StackPane{
	private final ValueObserver<Boolean> valueObserver = new ValueObserver<>(null);
	private final ChoiceBox<Boolean> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(true, false));

	public BooleanChoiceBox() {
		valueObserver.addListener(new ValueListener<Boolean>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean newValue) {
				choiceBox.setValue(newValue);
			}
		});
		choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				valueObserver.updateValue(newValue);
			}
		});

		this.getChildren().add(choiceBox);
	}

	public void setValue(@Nullable Boolean value){
		choiceBox.setValue(value);
	}

	@Nullable
	public Boolean getValue(){
		return choiceBox.getValue();
	}

	@NotNull
	public ValueObserver<Boolean> getValueObserver() {
		return valueObserver;
	}
}
