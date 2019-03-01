package com.armadialogcreator.gui.main.actions.mainMenu.view;

import com.armadialogcreator.core.stringtable.Language;
import com.armadialogcreator.core.stringtable.StringTable;
import com.armadialogcreator.data.StringTableManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 @author Kayler
 @since 12/29/2016 */
public class ViewScreenUILanguageChangeAction implements ChangeListener<Language> {
	@Override
	public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language newValue) {
		StringTable stringTable = StringTableManager.instance.getStringTable();
		if (stringTable == null) {
			return;
		}
		stringTable.setDefaultLanguage(newValue);
	}
}
