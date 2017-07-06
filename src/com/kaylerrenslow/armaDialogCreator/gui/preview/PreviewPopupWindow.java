package com.kaylerrenslow.armaDialogCreator.gui.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.Reference;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 Created by Kayler on 06/14/2016.
 */
public class PreviewPopupWindow extends StagePopup<VBox> {

	private static PreviewPopupWindow showingInstance = null;
	private final UICanvasPreview previewCanvas;
	private ToggleGroup toggleGroupFocusedControl;
	private ArmaControl focusedControl = null;
	private final ResourceBundle bundle = Lang.getBundle("PreviewWindowBundle");

	public PreviewPopupWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(0), null);
		setTitle(bundle.getString("popup_title"));

		ApplicationData data = ApplicationData.getManagerInstance();
		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(data);
		ArmaDisplay armaDisplay = data.getCurrentProject().getEditingDisplay();

		MenuBar menuBar = new MenuBar();
		{
			Menu miSetFocusedControl = new Menu(bundle.getString("MenuBar.setFocusedControl"));
			menuBar.getMenus().add(miSetFocusedControl);

			MenuItem miReset = new MenuItem(bundle.getString("MenuBar.setFocusedControl.reset"));
			miSetFocusedControl.getItems().add(miReset);
			miReset.setOnAction(event -> {
				toggleGroupFocusedControl.selectToggle(null);
			});

			Menu menuControls = new Menu(bundle.getString("MenuBar.setFocusedControl.controls"));
			Menu menuBgControls = new Menu(bundle.getString("MenuBar.setFocusedControl.bg_controls"));

			miSetFocusedControl.setOnShowing(event -> {
				//reinitialize the toggle group because just clearing it would reset
				//the focused control in the same way that pushing reset button would
				//since the clear method will set the selected toggle to null
				toggleGroupFocusedControl = new ToggleGroup();

				buildMenu(armaDisplay.getControls().deepIterator(), menuControls);
				buildMenu(armaDisplay.getBackgroundControls().deepIterator(), menuBgControls);

				toggleGroupFocusedControl.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
					if (newValue == null) {
						updateFocusedControl(armaDisplay);
						return;
					}
					if (focusedControl != null) {
						focusedControl.getRenderer().setFocused(false);
					}
					focusedControl = (ArmaControl) newValue.getUserData();
					focusedControl.getRenderer().setFocused(true);
				});

			});
			miSetFocusedControl.getItems().add(menuControls);
			miSetFocusedControl.getItems().add(menuBgControls);
		}

		updateFocusedControl(armaDisplay);
		myRootElement.getChildren().add(menuBar);

		previewCanvas = new UICanvasPreview(resolution, armaDisplay);
		myRootElement.getChildren().add(previewCanvas);

		previewCanvas.setDisplay(armaDisplay);
		previewCanvas.updateResolution(resolution);
	}

	private void updateFocusedControl(@NotNull ArmaDisplay armaDisplay) {
		if (focusedControl != null) {
			focusedControl.getRenderer().setFocused(false);
		}
		//find the last non bg control that wants focus
		//if no control wants focus, set focus to the last control that can have focus
		Reference<ArmaControl> focusToMe = new Reference<>();
		Reference<ArmaControl> lastControl = new Reference<>();
		armaDisplay.getControls().deepIterator().forEach(armaControl -> {
			if (armaControl.getRenderer().requestingFocus()) {
				focusToMe.setValue(armaControl);
			}
			if (armaControl.getRenderer().canHaveFocus()) {
				lastControl.setValue(armaControl);
			}
		});
		if (focusToMe.getValue() == null) {
			focusedControl = lastControl.getValue();
		} else {
			focusedControl = focusToMe.getValue();
		}
		if (focusedControl != null) {
			focusedControl.getRenderer().setFocused(true);
		}
	}

	private void buildMenu(Iterable<ArmaControl> controls, Menu menuControls) {
		menuControls.getItems().clear();
		for (ArmaControl armaControl : controls) {
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
	}

	@Override
	protected void closing() {
		previewCanvas.clearListeners();
	}

	public static void showWindow() {
		if (showingInstance == null || !showingInstance.isShowing()) {
			showingInstance = new PreviewPopupWindow();
			showingInstance.show();
		} else {
			showingInstance.beepFocus();
		}
	}
}
