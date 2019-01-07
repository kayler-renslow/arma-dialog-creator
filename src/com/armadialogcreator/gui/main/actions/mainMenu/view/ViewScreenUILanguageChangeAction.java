package com.armadialogcreator.gui.main.actions.mainMenu.view;

import com.armadialogcreator.arma.stringtable.Language;
import com.armadialogcreator.data.olddata.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 @author Kayler
 @since 12/29/2016 */
public class ViewScreenUILanguageChangeAction implements ChangeListener<Language> {
	@Override
	public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language newValue) {
		Project.getCurrentProject().setDefaultLanguage(newValue);
	}
}
