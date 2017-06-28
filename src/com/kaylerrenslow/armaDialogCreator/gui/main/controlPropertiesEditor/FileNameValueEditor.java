package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVFileName;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.FileChooserPane;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 A ValueEditor implementation for selecting files

 @author Kayler
 @since 06/27/2017. */
public class FileNameValueEditor implements ValueEditor<SVFileName> {
	private final ResourceBundle bundle = Lang.ApplicationBundle();

	protected final FileChooserPane chooserPane = new FileChooserPane(
			ArmaDialogCreator.getPrimaryStage(),
			FileChooserPane.ChooserType.FILE,
			bundle.getString("ValueEditors.FileNameValueEditor.locate_file"),
			null
	);
	private final StackPane masterPane = new StackPane(chooserPane);

	private final ValueObserver<SVFileName> valueObserver = new ValueObserver<>(null);

	public FileNameValueEditor() {
		chooserPane.getChosenFileObserver().addListener((observer, oldValue, newValue) -> {
			if (newValue == null) {
				valueObserver.updateValue(null);
			} else {
				valueObserver.updateValue(new SVFileName(newValue));
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	@Nullable
	public SVFileName getValue() {
		return valueObserver.getValue();
	}

	@Override
	public void setValue(SVFileName val) {
		valueObserver.updateValue(val);

		if (val == null) {
			chooserPane.setChosenFile(null);
		} else {
			chooserPane.setChosenFile(val.getFile());
		}
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public void focusToEditor() {

	}

	@Override
	public ReadOnlyValueObserver<SVFileName> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}

	@Override
	public boolean displayFullWidth() {
		return true;
	}

}
