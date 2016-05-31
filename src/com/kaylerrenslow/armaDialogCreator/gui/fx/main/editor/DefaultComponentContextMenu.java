package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlPropertiesLookup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopupUndecorated;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

/**
 Created by Kayler on 05/27/2016.
 */
public class DefaultComponentContextMenu extends ContextMenu {

	private MenuItem configure = new MenuItem("Configure Properties");

	public DefaultComponentContextMenu(ArmaControl c) {
		getItems().add(configure);

		configure.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				new StagePopupUndecorated(ArmaDialogCreator.getPrimaryStage(), new ScrollPane(new Accordion()), "") {
					@Override
					public void show() {
						myRootElement.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 2; -fx-border-color: -fx-accent;");
						myStage.initStyle(StageStyle.TRANSPARENT);
						ScrollPane rootElement = ((ScrollPane) myRootElement);
						Accordion accordion = (Accordion) rootElement.getContent();
						myScene.setFill(Color.TRANSPARENT);
						final double padding = 20.0;
						rootElement.setPadding(new Insets(padding));
						TitledPane tp;
						VBox vb;
						String[] panes = {"Required", "Optional"};
						for (int i = 0; i < panes.length; i++) {
							vb = new VBox(5);
							tp = new TitledPane(panes[i], vb);
							tp.setAnimated(false);
							ControlProperty[] reqs = c.getRequiredProperties();
							for (ControlProperty req : reqs) {
								vb.getChildren().add(getHbox(req, ControlPropertiesLookup.TYPE.matches(req)));
							}
							accordion.getPanes().add(tp);
						}
						accordion.setExpandedPane(accordion.getPanes().get(0));
						super.show();
					}

					private HBox getHbox(ControlProperty c, boolean disabled) {
						HBox h = new HBox(5);
						Control right;
						if (c.isType(ControlProperty.PropertyType.COLOR)) {
							right = new ColorPicker();
						} else {
							right = new TextField();
						}
						right.setDisable(disabled);
						h.getChildren().addAll(new Label(c.getName()), right);
						h.setAlignment(Pos.TOP_RIGHT);

						return h;
					}
				}.show();
			}
		});
	}
}
