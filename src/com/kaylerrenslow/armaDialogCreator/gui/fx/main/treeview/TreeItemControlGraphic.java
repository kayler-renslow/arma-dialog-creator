package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.Label;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 @author Kayler
 Graphic for tree items that represent a control
 Created on 06/08/2016. */
class TreeItemControlGraphic extends HBox {
	private static final Font LABEL_FONT = Font.font(Font.getDefault().getFamily(), FontPosture.ITALIC, Font.getDefault().getSize());
	private final RadioButton rbSelected = new RadioButton();

	TreeItemControlGraphic() {
		rbSelected.setSelected(true);
		setAlignment(Pos.CENTER_LEFT);
	}

	void init(ControlTreeItem treeItem) {
		rbSelected.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				treeItem.updateVisibilityFromButton(newValue);
			}
		});
		Label lblType = new Label("(" + treeItem.getControlTypeText() + ")");
		lblType.setFont(LABEL_FONT);

		getChildren().addAll(rbSelected, lblType);
	}

	void updateVisibilityRadioButton(boolean visible) {
		rbSelected.setSelected(visible);
	}
}
