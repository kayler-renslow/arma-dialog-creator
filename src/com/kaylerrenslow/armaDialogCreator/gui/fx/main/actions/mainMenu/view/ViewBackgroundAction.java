/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view;

import com.kaylerrenslow.armaDialogCreator.gui.img.ImagePaths;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.FileChooser;

import java.io.File;

/**
 Created by Kayler on 05/20/2016.
 */
public class ViewBackgroundAction implements EventHandler<ActionEvent> {
	public static final int IMAGE_1 = 0;
	public static final int IMAGE_2 = 1;
	public static final int IMAGE_3 = 2;
	public static final int IMAGE_CUSTOM = 3;
	public static final int NO_IMAGE = 4;

	private final int background;
	private static int lastBackground = NO_IMAGE;

	public ViewBackgroundAction(int background) {
		this.background = background;
	}

	@Override
	public void handle(ActionEvent event) {
		chooseBackground(this.background, event);
		lastBackground = this.background;
	}

	private void chooseBackground(int background, ActionEvent event) {
		if (background == IMAGE_1) {
			ArmaDialogCreator.getCanvasView().setCanvasBackgroundToImage(ImagePaths.BG_1);
		} else if (background == IMAGE_2) {
			ArmaDialogCreator.getCanvasView().setCanvasBackgroundToImage(ImagePaths.BG_2);
		} else if (background == IMAGE_3) {
			ArmaDialogCreator.getCanvasView().setCanvasBackgroundToImage(ImagePaths.BG_3);
		} else if (background == NO_IMAGE) {
			ArmaDialogCreator.getCanvasView().setCanvasBackgroundToImage(null);
		} else if (background == IMAGE_CUSTOM) {
			FileChooser c = new FileChooser();
			FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.png", "*.gif", "*.jpg", "*.mpo");
			c.getExtensionFilters().add(filter);
			c.setSelectedExtensionFilter(filter);
			c.setTitle(Lang.ApplicationBundle().getString("Misc.file_chooser_background_img_title"));
			File chosen = c.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
			if (chosen != null) {
				ArmaDialogCreator.getCanvasView().setCanvasBackgroundToImage(chosen.toURI().toString());
			} else {
				RadioMenuItem target = (RadioMenuItem) event.getTarget();
				target.getToggleGroup().selectToggle(target.getToggleGroup().getToggles().get(lastBackground));
			}
		}
	}
}
