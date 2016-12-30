package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 User is presented with a down arrow and when the user moves the mouse over the arrow, a menu automatically appears.

 @author Kayler
 @since 12/24/2016 */
public class DownArrowMenu extends StackPane {
	public static final Image ICON_DOWN_ARROW = new Image("/com/kaylerrenslow/armaDialogCreator/gui/img/icons/down_arrow.png");

	private final ContextMenu popupMenu = new ContextMenu();

	public DownArrowMenu(@NotNull Image arrowImg, @NotNull MenuItem... items) {
		ImageView img = new ImageView(arrowImg);
		img.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (popupMenu.isShowing()) {
					return;
				}
				popupMenu.show(DownArrowMenu.this, Side.BOTTOM, 0, 0);
			}
		});
		popupMenu.setAutoHide(true);
		getItems().addAll(items);

		setAlignment(Pos.CENTER_LEFT);
		getChildren().add(img);
	}

	public DownArrowMenu(@NotNull MenuItem... items) {
		this(ICON_DOWN_ARROW, items);
	}

	@NotNull
	public ObservableList<MenuItem> getItems() {
		return popupMenu.getItems();
	}
}
