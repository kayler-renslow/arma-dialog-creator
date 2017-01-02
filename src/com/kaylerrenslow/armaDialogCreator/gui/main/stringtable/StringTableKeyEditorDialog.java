package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.geometry.Orientation;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 12/29/2016 */
public class StringTableKeyEditorDialog extends StageDialog<VBox> {
	private final StringTableKey copyKey;
	private StringTableKey editKey;

	public StringTableKeyEditorDialog(@NotNull StringTable ownerTable, @NotNull StringTableKey editKey) {
		super(
				ArmaDialogCreator.getPrimaryStage(),
				new VBox(5),
				String.format(Lang.ApplicationBundle().getString("Popups.StringTableKeyEditor.popup_title_f"), editKey.getId()),
				true, true, false
		);

		this.editKey = editKey;
		copyKey = editKey.deepCopy();
		setResizable(false);
		StringTableKeyEditorPane editorPane = new StringTableKeyEditorPane(copyKey.getDefaultLanguage());
		editorPane.getPaneContent().setPrefHeight(200);
		editorPane.getPaneContent().setOrientation(Orientation.VERTICAL);
		editorPane.setKey(copyKey, ownerTable.getKeys());
		myRootElement.getChildren().add(editorPane);
	}

	@Override
	protected void ok() {
		editKey.setTo(copyKey);
		super.ok();
	}
}
