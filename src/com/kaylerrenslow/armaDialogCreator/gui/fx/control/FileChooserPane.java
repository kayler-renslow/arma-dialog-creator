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
		chosenFileObserver.addListener(new ValueListener<File>() {
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
						if (defaultChooserPopupLocation.exists()) {
							chooser.setInitialDirectory(defaultChooserPopupLocation);
						}
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
						if (defaultChooserPopupLocation.exists()) {
							chooser.setInitialDirectory(defaultChooserPopupLocation);
						}
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
