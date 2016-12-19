package com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaUIScale;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.UIScale;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 Created by Kayler on 05/20/2016.
 */
public class ViewScreenUIScaleChangeAction implements ChangeListener<UIScale> {
	
	@Override
	public void changed(ObservableValue<? extends UIScale> observable, UIScale oldValue, UIScale newValue) {
		if (newValue == null) {
			newValue = ArmaUIScale.DEFAULT;
		}
		DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData()).setUIScale(newValue);
	}
}
