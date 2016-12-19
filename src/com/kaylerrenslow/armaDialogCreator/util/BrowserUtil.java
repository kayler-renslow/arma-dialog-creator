package com.kaylerrenslow.armaDialogCreator.util;

import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.net.URI;

/**
 Created by Kayler on 05/26/2016.
 */
public class BrowserUtil {
	/** Attempts to open the browser at the specified url. If the operation succeeded, this method will return true. If the operation failed, will return false. */
	public static void browse(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			new StageDialog<>(ArmaDialogCreator.getPrimaryStage(), new TextArea(String.format(Lang.ApplicationBundle().getString("Misc.visit_link_in_browser_f"), url)), Lang.ApplicationBundle()
					.getString("Popups.generic_popup_title"),
					false,
					true, false).show();
		}
	}
}
