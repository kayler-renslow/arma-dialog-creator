package com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.data.Project;
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
