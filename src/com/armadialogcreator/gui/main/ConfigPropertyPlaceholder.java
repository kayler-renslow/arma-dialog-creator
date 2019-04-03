package com.armadialogcreator.gui.main;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.lang.Lang;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 @author K
 @since 4/2/19 */
public class ConfigPropertyPlaceholder extends ConfigPropertyDisplayBox {

	private static final ResourceBundle bundle = Lang.getBundle("ConfigPropertyEditorBundle");

	public ConfigPropertyPlaceholder(@NotNull ConfigClass cc, @NotNull ConfigPropertyLookupConstant property) {
		super(cc, property);

		MenuItem miInitialize = new MenuItem(bundle.getString("initialize"));
		MenuItem miShowDoc = new MenuItem(bundle.getString("show_documentation"));
		miShowDoc.setOnAction(event -> {
			new MenuButtonPopup(getDocumentation()).showPopup();
		});
		miInitialize.setOnAction(event -> {
			//todo
		});

		menuButtonOptions.getItems().addAll(miShowDoc, miInitialize);

		HBox.setHgrow(this.contentStackPane, Priority.ALWAYS);
		StackPane stackPaneInitialize = new StackPane();
		HBox.setHgrow(stackPaneInitialize, Priority.ALWAYS);
		Separator separator = new Separator(Orientation.HORIZONTAL);
		stackPaneInitialize.getChildren().add(separator);

		Label lbl = new Label(bundle.getString("initialize"));
		lbl.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(1), new Insets(-4))));
		lbl.setVisible(false);
		stackPaneInitialize.setAlignment(Pos.CENTER_LEFT);

		stackPaneInitialize.getChildren().add(lbl);

		contentStackPane.getChildren().add(stackPaneInitialize);

		contentStackPane.setOnMouseMoved(event -> {
			final double paneWidth = contentStackPane.getWidth();
			double left = Math.min(paneWidth, Math.max(0d, event.getX()));
			double pos = left;
			final double lblMid = lbl.getWidth() / 2;
			final double lblRight = lbl.getWidth();

			if (left < lblMid) {
				pos = 0;
			} else if (left >= lblMid && left < paneWidth - lblMid) {
				pos = left - lblMid;
			} else if (left >= paneWidth - lblRight) {
				pos = paneWidth - lblRight;
			}
			lbl.setTranslateX(pos);
		});
		contentStackPane.setOnMouseExited(event -> {
			lbl.setVisible(false);
		});
		contentStackPane.setOnMouseEntered(event -> {
			lbl.setVisible(true);
		});
	}
}
