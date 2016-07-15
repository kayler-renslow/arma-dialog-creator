package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ControlPropertiesEditorPane;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopupUndecorated;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 Created by Kayler on 05/31/2016.
 */
public class ControlPropertiesConfigPopup extends StagePopupUndecorated<VBox> {
	private ArmaControl control;
	private ControlPropertiesEditorPane editorPane;

	public ControlPropertiesConfigPopup(@NotNull ArmaControl control) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null);
		initializePopup();
		editorPane = new ControlPropertiesEditorPane(control);
		initializeToControl(control);
	}

	private void initializePopup() {
		myRootElement.getStyleClass().add("rounded-node");
		myStage.initStyle(StageStyle.TRANSPARENT);

		myScene.setFill(Color.TRANSPARENT);
		final double padding = 20.0;
		myRootElement.setPadding(new Insets(padding, padding, padding, padding));
	}

	/**
	 Configures the popup to edit the given control.

	 @return true if the initialization was successful, or false if the initialization was canceled
	 */
	public boolean initializeToControl(ArmaControl c) {
		if (myRootElement.getChildren().size() > 0) {
			if (!editorPane.allValuesAreGood()) {
				return false;
			}
		}
		this.control = c;
		Color bg = control.getRenderer().getBackgroundColor();
		control.getRenderer().getBackgroundColorObserver().addValueListener(new ValueListener<AColor>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
				if (newValue != null) {
					setBorderColor(newValue.toJavaFXColor()); //update the popup's border color
				}
			}
		});
		setBorderColor(bg);
		myRootElement.getChildren().clear();
		addCloseButton();
		myRootElement.getChildren().add(editorPane);
		CheckBox cbIsBackgroundControl = new CheckBox(Lang.Popups.ControlPropertiesConfig.IS_BACKGROUND_CONTROL);
		myRootElement.getChildren().add(cbIsBackgroundControl);
		return true;
	}


	private void addCloseButton() {
		Button btnClose = new Button("x");
		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				WindowEvent windowEvent = new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST);
				myStage.getOnCloseRequest().handle(windowEvent);
				List<ControlProperty> missing = editorPane.getMissingProperties();
				boolean goodValues = missing.size() == 0;
				if (!windowEvent.isConsumed() && goodValues) {
					close();
				}
				if (!goodValues) {
					StagePopup popup = new MissingControlPropertiesConfigPopup(myStage, missing);
					popup.show();
					popup.requestFocus();
				}
			}
		});
		btnClose.getStyleClass().add("close-button");

		ComboBox<String> cbExtendClass = new ComboBox<>(FXCollections.observableArrayList("-", "RscStatic", "RscPicture"));
		cbExtendClass.getSelectionModel().select(0);
		Label lblExtendClass = new Label(Lang.Popups.ControlPropertiesConfig.EXTEND_CLASS, cbExtendClass);

		myRootElement.getChildren().add(new BorderPane(null, null, btnClose, null, lblExtendClass));
	}

	private void setBorderColor(Color bg) {
		myRootElement.setStyle(String.format("-fx-border-color: rgba(%f%%,%f%%,%f%%,%f);", bg.getRed() * 100.0, bg.getGreen() * 100.0, bg.getBlue() * 100.0, bg.getOpacity()));
	}

	@Override
	protected void onCloseRequest(WindowEvent event) {
		super.onCloseRequest(event);
	}

	public ArmaControl getControl() {
		return control;
	}

}
