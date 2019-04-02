package com.armadialogcreator.gui.main;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 4/2/19 */
public class ConfigPropertyPlaceholder extends ConfigPropertyDisplayBox {

	public ConfigPropertyPlaceholder(@NotNull ConfigClass cc, @NotNull ConfigPropertyLookupConstant property) {
		super(property);

		HBox.setHgrow(this.contentStackPane, Priority.ALWAYS);
		StackPane stackPaneInitialize = new StackPane();
		HBox.setHgrow(stackPaneInitialize, Priority.ALWAYS);
		Separator separator = new Separator(Orientation.HORIZONTAL);
		stackPaneInitialize.getChildren().add(separator);

		Label lbl = new Label("INITIALIZE~~~");
		lbl.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(1), new Insets(-4))));
		lbl.setVisible(false);
		stackPaneInitialize.setAlignment(Pos.CENTER_LEFT);

		stackPaneInitialize.getChildren().add(lbl);

		contentStackPane.getChildren().add(stackPaneInitialize);

		contentStackPane.setOnMouseMoved(event -> {
			final double paneWidth = contentStackPane.getWidth();
			double left = Math.min(paneWidth, Math.max(0d, event.getX()));
			double pos = left;
			final boolean notLeftCorner = left >= lbl.getWidth();
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
