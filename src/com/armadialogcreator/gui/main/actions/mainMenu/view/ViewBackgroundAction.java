package com.armadialogcreator.gui.main.actions.mainMenu.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
		//chooseBackground(this.background, event);
		lastBackground = this.background;
	}
/*
	private void chooseBackground(int background, ActionEvent event) {
		if (background == IMAGE_1) {
			ArmaDialogCreator.getCanvasEditor().setCanvasBackgroundToImage(ADCImagePaths.BG_1);
		} else if (background == IMAGE_2) {
			ArmaDialogCreator.getCanvasEditor().setCanvasBackgroundToImage(ADCImagePaths.BG_2);
		} else if (background == IMAGE_3) {
			ArmaDialogCreator.getCanvasEditor().setCanvasBackgroundToImage(ADCImagePaths.BG_3);
		} else if (background == NO_IMAGE) {
			ArmaDialogCreator.getCanvasEditor().setCanvasBackgroundToImage(null);
		} else if (background == IMAGE_CUSTOM) {
			FileChooser c = new FileChooser();
			FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.png", "*.gif", "*.jpg", "*.mpo");
			c.getExtensionFilters().add(filter);
			c.setSelectedExtensionFilter(filter);
			c.setTitle(Lang.ApplicationBundle().getString("Misc.file_chooser_background_img_title"));
			File chosen = c.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
			if (chosen != null) {
				ArmaDialogCreator.getCanvasEditor().setCanvasBackgroundToImage(chosen.toURI().toString());
			} else {
				RadioMenuItem target = (RadioMenuItem) event.getTarget();
				target.getToggleGroup().selectToggle(target.getToggleGroup().getToggles().get(lastBackground));
			}
		}
	}*/
}
