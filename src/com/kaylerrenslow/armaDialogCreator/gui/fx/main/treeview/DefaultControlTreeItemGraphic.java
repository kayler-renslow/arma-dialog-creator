/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.BorderedImageView;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Graphic for tree items that represent a control

 @author Kayler
 @since 06/08/2016. */
public class DefaultControlTreeItemGraphic extends HBox {
	private static final Insets margin = new Insets(0, 5, 0, 0);
	private static final String BORDER_STYLE = "-fx-background-color:#b3b3b3,white;-fx-background-insets:0,20;-fx-padding:1px;";
	private final RadioButton rbSelected = new RadioButton();
	private final Canvas box = new Canvas(16, 16);

	public DefaultControlTreeItemGraphic() {
		super(5);
		rbSelected.setSelected(true);
	}

	public void init(ControlTreeItemEntry entry) {
		rbSelected.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				entry.updateVisibilityFromButton(newValue);
			}
		});
		entry.getMyArmaControl().getRenderer().getEnabledObserver().addListener(new ValueListener<Boolean>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean enabled) {
				updateVisibilityRadioButton(enabled);
			}
		});
		entry.getMyArmaControl().getRenderer().getBackgroundColorObserver().addListener(new ValueListener<AColor>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
				if (newValue == null) {
					fillBox(Color.TRANSPARENT);
				} else {
					fillBox(newValue.toJavaFXColor());
				}
			}
		});

		final BorderedImageView imageView = new BorderedImageView(entry.getMyArmaControl().getControlType().icon);
		Tooltip.install(imageView, new Tooltip(entry.getMyArmaControl().getControlType().displayName));

		fillBox(entry.getPrimaryColor());
		StackPane boxBorder = new StackPane(box);
		boxBorder.setStyle(BORDER_STYLE);
		HBox.setMargin(boxBorder, margin);
		getChildren().addAll(imageView, boxBorder, rbSelected);
	}

	private void fillBox(Color color) {
		GraphicsContext gc = box.getGraphicsContext2D();
		gc.save();
		gc.setFill(color);
		gc.fillRect(0, 0, box.getWidth(), box.getHeight());
		gc.restore();
	}

	public void updateVisibilityRadioButton(boolean visible) {
		rbSelected.setSelected(visible);
	}

	public void setBoxColor(Color color) {
		fillBox(color);
	}
}
