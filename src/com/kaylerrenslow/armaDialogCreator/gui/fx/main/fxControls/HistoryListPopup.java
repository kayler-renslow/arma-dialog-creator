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
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 Used for displaying a list of things that have happened.

 @author Kayler
 @since 11/18/16 */
public class HistoryListPopup extends StagePopup<VBox> {

	private final HistoryListProvider provider;
	private final GridPane gridPaneContent = new GridPane();
	private final StackPane stackPaneWrapper = new StackPane(gridPaneContent);

	public HistoryListPopup(@Nullable String popupTitle, @NotNull HistoryListProvider provider) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), popupTitle);
		this.provider = provider;

		myRootElement.setPadding(new Insets(10));
		final ScrollPane scrollPane = new ScrollPane(stackPaneWrapper);
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color:transparent");

		myRootElement.getChildren().addAll(scrollPane, new Separator(Orientation.HORIZONTAL), getBoundResponseFooter(false, true, false));

		gridPaneContent.setVgap(15);
		gridPaneContent.setHgap(5);
		ColumnConstraints constraints = new ColumnConstraints(-1, -1, Double.MAX_VALUE, Priority.ALWAYS, HPos.CENTER, true);
		gridPaneContent.getColumnConstraints().addAll(constraints, constraints);

		myStage.setMinWidth(320);
		myStage.setMinHeight(320);
		myStage.setWidth(480);

		fillContent();
	}

	private void fillContent() {
		stackPaneWrapper.getChildren().clear();

		List<HistoryListItem> items = provider.collectItems();
		if (items.size() == 0) {
			stackPaneWrapper.getChildren().add(new Label(provider.noItemsPlaceholder()));
			return;
		}

		gridPaneContent.getChildren().clear();
		stackPaneWrapper.getChildren().add(gridPaneContent);
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
				gridPaneContent.addRow(row++, item.getGraphic(), listItemNode);
			} else {
				gridPaneContent.addRow(row++, listItemNode);
			}
		}
	}

	private static class HistoryListItemNode extends HBox {
		public HistoryListItemNode(@NotNull HistoryListItem item) {
			super(5);
			final VBox vboxTitle = new VBox(5);
			final Label lblTitle = new Label(item.getItemTitle());
			lblTitle.setFont(Font.font(15));
			vboxTitle.getChildren().add(lblTitle);

			Font subInfoLabelFont = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 10);
			Font subInfoTextFont = Font.font(subInfoLabelFont.getSize());
			for (HistoryListItemSubInfo subInfo : item.getSubInfo()) {
				final Label lbl = new Label(subInfo.getLabel());
				lbl.setFont(subInfoLabelFont);
				final Label lblInfo = new Label(subInfo.getInfo());
				lblInfo.setFont(subInfoTextFont);
				vboxTitle.getChildren().add(new HBox(5, lbl, lblInfo));
			}

			getChildren().add(vboxTitle);
			final Label lblMainInfo = new Label(item.getInformation());
			lblMainInfo.setWrapText(true);
			getChildren().add(lblMainInfo);
		}
	}
}
