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

	private ArmaControl focusedControl = null;
	private final UICanvasPreview previewCanvas;
	private final ArmaDisplay armaDisplay;
	private final ToggleGroup toggleGroupFocusedControl = new ToggleGroup();
	private final ResourceBundle bundle = Lang.getBundle("PreviewWindowBundle");
	private final ChangeListener<? extends Toggle> toggleGroupListener = (observable, oldValue, newValue) -> {
		if (newValue == null) {
			updateFocusedControl();
			return;
		}
		if (focusedControl != null) {
			setControlFocused(focusedControl, false);
		}
		focusedControl = (ArmaControl) newValue.getUserData();
		setControlFocused(focusedControl, true);
	};

	public PreviewPopupWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(0), null);
		setTitle(bundle.getString("popup_title"));

		ApplicationData data = ApplicationData.getManagerInstance();
		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(data);
		armaDisplay = data.getCurrentProject().getEditingDisplay();

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

		updateFocusedControl();
		myRootElement.getChildren().add(menuBar);

		previewCanvas = new UICanvasPreview(resolution, armaDisplay);
		myRootElement.getChildren().add(previewCanvas);

		previewCanvas.setDisplay(armaDisplay);
		previewCanvas.updateResolution(resolution);
	}

	private void updateFocusedControl() {//find the last non bg control that wants focus
		//if no control wants focus, set focus to the last control that can have focus
		Reference<ArmaControl> focusToMe = new Reference<>();
		Reference<ArmaControl> lastControl = new Reference<>();
		armaDisplay.getControls().deepIterator().forEach(armaControl -> {
			setControlFocused(armaControl, false);
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
			setControlFocused(focusedControl, true);
		}
	}

	private void setControlFocused(ArmaControl armaControl, boolean focused) {
		armaControl.getRenderer().setFocused(focused);
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
