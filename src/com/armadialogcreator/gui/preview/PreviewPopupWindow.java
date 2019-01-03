package com.armadialogcreator.gui.preview;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.HelpUrls;
import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.ArmaDisplay;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.data.ApplicationData;
import com.armadialogcreator.data.DataKeys;
import com.armadialogcreator.gui.StagePopup;
import com.armadialogcreator.gui.main.BrowserUtil;
import com.armadialogcreator.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;

/**
 Created by Kayler on 06/14/2016.
 */
public class PreviewPopupWindow extends StagePopup<VBox> {

	private static PreviewPopupWindow showingInstance = null;

	private final ControlFocusHandler focusHandler;
	private final UICanvasPreview previewCanvas;
	private final ArmaDisplay armaDisplay;
	private final ToggleGroup toggleGroupFocusedControl = new ToggleGroup();
	private final ResourceBundle bundle = Lang.getBundle("PreviewWindowBundle");
	private final ChangeListener<? extends Toggle> toggleGroupListener;

	public PreviewPopupWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(0), null);
		setTitle(bundle.getString("popup_title"));


		ApplicationData data = ApplicationData.getManagerInstance();
		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(data);
		armaDisplay = data.getCurrentProject().getEditingDisplay();

		focusHandler = new ControlFocusHandler(armaDisplay);

		toggleGroupListener = (observable, oldValue, newValue) -> {
			if (newValue == null) {
				focusHandler.autoFocusToControl();
				return;
			}
			focusHandler.setFocusedControl((ArmaControl) newValue.getUserData());
		};

		MenuBar menuBar = new MenuBar();
		{
			Menu miSetFocusedControl = new Menu(bundle.getString("MenuBar.setFocusedControl"));
			menuBar.getMenus().add(miSetFocusedControl);
			{
				MenuItem miReset = new MenuItem(bundle.getString("MenuBar.setFocusedControl.reset"));
				miSetFocusedControl.getItems().add(miReset);
				miReset.setOnAction(event -> {
					toggleGroupFocusedControl.selectToggle(null);
				});

				Menu menuControls = new Menu(bundle.getString("MenuBar.setFocusedControl.controls"));

				miSetFocusedControl.setOnShowing(event -> {
					//remove toggle group listener because just clearing it would reset
					//the focused control in the same way that pushing reset button would
					//since the clear method will set the selected toggle to null
					toggleGroupFocusedControl.selectedToggleProperty().removeListener((ChangeListener) toggleGroupListener);

					menuControls.getItems().clear();
					for (ArmaControl armaControl : armaDisplay.getControls().deepIterator()) {
						RadioMenuItem mi = new RadioMenuItem(
								armaControl.getClassName(),
								new ImageView(armaControl.getControlType().getIcon())
						);
						mi.setUserData(armaControl);
						mi.setToggleGroup(toggleGroupFocusedControl);
						menuControls.getItems().add(mi);
						if (armaControl.getRenderer().hasFocus()) {
							toggleGroupFocusedControl.selectToggle(mi);
						}
					}
					if (menuControls.getItems().size() == 0) {
						MenuItem miNoControls = new MenuItem(bundle.getString("MenuBar.setFocusedControl.noControls"));
						miNoControls.setDisable(true);
						menuControls.getItems().add(miNoControls);
					}

					toggleGroupFocusedControl.selectedToggleProperty().addListener((ChangeListener) toggleGroupListener);

				});
				miSetFocusedControl.getItems().add(menuControls);
			}

			Menu menuHelp = new Menu(bundle.getString("MenuBar.help"));
			menuBar.getMenus().add(menuHelp);
			{
				MenuItem menuItemWiki = new MenuItem(bundle.getString("MenuBar.wiki"));
				menuItemWiki.setOnAction(event -> {
					help();
				});
				menuHelp.getItems().add(menuItemWiki);
			}
		}

		focusHandler.autoFocusToControl();
		myRootElement.getChildren().add(menuBar);

		previewCanvas = new UICanvasPreview(resolution, armaDisplay, focusHandler);
		myRootElement.getChildren().add(previewCanvas);

		previewCanvas.setDisplay(armaDisplay);
		previewCanvas.updateResolution(resolution);
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.ARMA_PREVIEW_WINDOW);
	}

	@Override
	protected void closing() {
		previewCanvas.clearListeners();
		previewCanvas.getTimer().stop();
		showingInstance = null;
	}

	public static void showWindow() {
		if (showingInstance == null || !showingInstance.isShowing()) {
			//note: the instance may be hidden without the closing() method being invoked which sets showingInstance to null
			if (showingInstance != null) {
				showingInstance.closing();
			}
			showingInstance = new PreviewPopupWindow();
			showingInstance.show();
		} else {
			showingInstance.beepFocus();
		}
	}
}
