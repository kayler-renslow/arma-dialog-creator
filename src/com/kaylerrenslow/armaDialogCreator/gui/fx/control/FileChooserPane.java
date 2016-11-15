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

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 A pane that has a locate {@link Button} to choose a file and a {@link TextField} to show the path of the file that was chosen.

 @author Kayler
 @since 09/16/2016. */
public class FileChooserPane extends HBox {
	protected final Button btnLocate = new Button(Lang.FxControlBundle().getString("FileChooserPane.locate"));
	protected final TextField tfFile = new TextField();

	private ValueObserver<File> chosenFileObserver = new ValueObserver<>(null);

	public enum ChooserType {
		DIRECTORY, FILE
	}

	public FileChooserPane(Window chooserPopupWindowOwner, ChooserType chooserType, String fileChooserPopupTitle, File defaultChooserPopupLocation, FileChooser.ExtensionFilter... filters) {
		super(5);
		HBox.setHgrow(tfFile, Priority.ALWAYS);
		this.getChildren().addAll(btnLocate, tfFile);
		chosenFileObserver.addValueListener(new ValueListener<File>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<File> observer, File oldValue, File newValue) {
				setChosenFile(newValue);
			}
		});
		btnLocate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				switch (chooserType) {
					case DIRECTORY: {
						DirectoryChooser chooser = new DirectoryChooser();
						chooser.setTitle(fileChooserPopupTitle);
						chooser.setInitialDirectory(defaultChooserPopupLocation);
						File f = chooser.showDialog(chooserPopupWindowOwner);
						if (f == null) {
							return;
						}
						setChosenFile(f);
						break;
					}
					case FILE: {
						FileChooser chooser = new FileChooser();
						chooser.setTitle(fileChooserPopupTitle);
						chooser.getExtensionFilters().addAll(filters);
						chooser.setInitialDirectory(defaultChooserPopupLocation);
						File f = chooser.showOpenDialog(chooserPopupWindowOwner);
						if (f == null) {
							return;
						}
						setChosenFile(f);
						break;
					}
					default: {
						throw new IllegalStateException("unexpected chooser type:" + chooserType);
					}
				}
			}
		});
		tfFile.setEditable(false);
	}

	public void setChosenFile(File f) {
		chosenFileObserver.updateValue(f);
		if (f == null) {
			tfFile.setText("");
		} else {
			tfFile.setText(f.getPath());
		}
	}

	@NotNull
	public ValueObserver<File> getChosenFileObserver() {
		return chosenFileObserver;
	}

	@Nullable
	public File getChosenFile() {
		return chosenFileObserver.getValue();
	}
}
