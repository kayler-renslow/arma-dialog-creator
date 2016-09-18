/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.control;

import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
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
		valueObserver.addValueListener(new ValueListener<Boolean>() {
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
