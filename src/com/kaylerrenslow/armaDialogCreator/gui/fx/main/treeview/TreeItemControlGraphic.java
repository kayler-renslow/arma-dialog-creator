package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 @author Kayler
 Graphic for tree items that represent a control
 Created on 06/08/2016. */
public class TreeItemControlGraphic extends HBox {
	private static final Font LABEL_FONT = Font.font(Font.getDefault().getFamily(), FontPosture.ITALIC, Font.getDefault().getSize());
	private static final Insets margin = new Insets(0, 5, 0, 0);
	private static final String BORDER_STYLE = "-fx-background-color:#b3b3b3,white;-fx-background-insets:0,20;-fx-padding:1px;";
	private final RadioButton rbSelected = new RadioButton();
	private final Canvas box = new Canvas(16, 16);

	public TreeItemControlGraphic() {
		rbSelected.setSelected(true);
	}

	public void init(ControlTreeItemEntry entry) {
		rbSelected.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				entry.updateVisibilityFromButton(newValue);
			}
		});
		Label lblType = new Label("(" + entry.getControlTypeText() + ")");
		lblType.setFont(LABEL_FONT);

		fillBox(entry.getPrimaryColor());
		StackPane boxBorder = new StackPane(box);
		boxBorder.setStyle(BORDER_STYLE);
		HBox.setMargin(boxBorder, margin);
		getChildren().addAll(boxBorder, rbSelected, lblType);
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
