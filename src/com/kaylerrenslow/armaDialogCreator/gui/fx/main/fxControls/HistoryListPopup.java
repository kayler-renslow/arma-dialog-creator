/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls;

import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author Kayler
 @since 11/18/16 */
public class HistoryListPopup extends StagePopup<VBox> {

	private final HistoryListProvider provider;
	private final GridPane containerContent = new GridPane();

	public HistoryListPopup(@Nullable String popupTitle, @NotNull HistoryListProvider provider) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), popupTitle);
		this.provider = provider;
		myRootElement.getChildren().addAll(containerContent, new Separator(Orientation.HORIZONTAL), getBoundResponseFooter(false, true, false));

		containerContent.setVgap(15);
		containerContent.setHgap(5);
		ColumnConstraints constraints = new ColumnConstraints(-1, -1, Double.MAX_VALUE, Priority.ALWAYS, HPos.CENTER, true);
		containerContent.getColumnConstraints().addAll(constraints, constraints);

		fillContent();
	}

	private void fillContent() {
		containerContent.getChildren().clear();
		List<HistoryListItem> items = provider.collectItems();
		boolean need2Columns = false;
		for (HistoryListItem item : items) {
			if (item.getGraphic() != null) {
				need2Columns = true;
				break;
			}
		}

		int row = 0;
		for (HistoryListItem item : items) {
			HistoryListItemNode listItemNode = new HistoryListItemNode(item);
			if (need2Columns) {
				containerContent.addRow(row++, item.getGraphic(), listItemNode);
			} else {
				containerContent.addRow(row++, listItemNode);
			}
		}
	}

	private static class HistoryListItemNode extends HBox {
		public HistoryListItemNode(@NotNull HistoryListItem item) {
			super(5);
			final VBox vboxTitle = new VBox(5);
			final Label lblTitle = new Label(item.getItemTitle());
			lblTitle.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 12));
			vboxTitle.getChildren().add(lblTitle);
			for (HistoryListItemSubInfo subInfo : item.getSubInfo()) {
				Label lbl = new Label(subInfo.getLabel());
				
			}
		}
	}
}
