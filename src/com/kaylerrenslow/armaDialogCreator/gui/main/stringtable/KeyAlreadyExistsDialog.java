package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.gui.popup.SimpleResponseDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 12/26/2016 */
public class KeyAlreadyExistsDialog extends SimpleResponseDialog {
	public KeyAlreadyExistsDialog(@NotNull StringTableKey key) {
		super(
				ArmaDialogCreator.getPrimaryStage(),
				Lang.ApplicationBundle().getString("StringTable.key_exists"),
				String.format(Lang.ApplicationBundle().getString("StringTable.key_exists_f"), key.getId()),
				false, true, false
		);
		setStageSize(320, 180);
		getFooter().getBtnOk().requestFocus();
	}
}
