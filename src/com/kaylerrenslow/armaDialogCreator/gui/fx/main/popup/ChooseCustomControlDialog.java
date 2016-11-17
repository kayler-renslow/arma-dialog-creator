/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls.ChooseItemPopup;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 This dialog asks the user to select a {@link CustomControlClass} that exists in the {@link com.kaylerrenslow.armaDialogCreator.data.CustomControlClassRegistry}.

 @author Kayler
 @since 11/13/2016 */
public class ChooseCustomControlDialog extends ChooseItemPopup<CustomControlClass> {
	private static final ItemCategory<CustomControlClass>[] categories = new ControlClassCategory[]{new ControlClassCategory()};

	public ChooseCustomControlDialog() {
		super(categories, ApplicationDataManager.getInstance().getCurrentProject().getCustomControlClassRegistry().getControlClassList(),
				Lang.ApplicationBundle().getString("Popups.ChooseCustomControl.dialog_title"),
				Lang.ApplicationBundle().getString("Popups.ChooseCustomControl.header_title")
		);
	}

	private static class ControlClassCategory implements ItemCategory<CustomControlClass> {

		private final CustomControlCategoryNode categoryNode;

		public ControlClassCategory() {
			this.categoryNode = new CustomControlCategoryNode();
		}

		@NotNull
		@Override
		public String categoryDisplayName() {
			return Lang.ApplicationBundle().getString("Popups.ChooseCustomControl.Category.All.display_title");
		}

		@NotNull
		@Override
		public String noItemsPlaceholderText() {
			return Lang.ApplicationBundle().getString("Popups.ChooseCustomControl.Category.All.no_items_placeholder");
		}

		@NotNull
		@Override
		public String availableItemsDisplayText() {
			return Lang.ApplicationBundle().getString("Popups.ChooseCustomControl.Category.All.available_items");
		}

		@Override
		public boolean itemInCategory(@NotNull CustomControlClass item) {
			return true;
		}

		@Nullable
		@Override
		public Node getMiscCategoryNode() {
			return categoryNode;
		}

		@Override
		public void newItemSelected(CustomControlClass item) {
			categoryNode.setToControlClass(item);
		}
	}

	private static class CustomControlCategoryNode extends StackPane {

		private final TextArea taComment = new TextArea();
		private final Label lblNoComment = new Label(Lang.ApplicationBundle().getString("Popups.ChooseCustomControl.Category.All.no_comment"));
		private final StackPane stackPaneComment = new StackPane();

		public CustomControlCategoryNode() {
			setAlignment(Pos.TOP_CENTER);

			taComment.setEditable(false);

			Label lblComment = new Label(Lang.ApplicationBundle().getString("Popups.ChooseCustomControl.Category.All.comment"));
			lblComment.setAlignment(Pos.TOP_CENTER);

			VBox vbox = new VBox(5, lblComment, stackPaneComment);
			VBox.setVgrow(stackPaneComment, Priority.ALWAYS);
			stackPaneComment.setAlignment(Pos.TOP_CENTER);
			vbox.setAlignment(Pos.TOP_CENTER);
			vbox.setFillWidth(true);
			getChildren().add(vbox);
		}

		public void setToControlClass(CustomControlClass controlClass) {
			stackPaneComment.getChildren().clear();
			if (controlClass.getComment() == null) {
				stackPaneComment.getChildren().add(lblNoComment);
			} else {
				stackPaneComment.getChildren().add(taComment);
				taComment.setText(controlClass.getComment());
			}
		}
	}
}
