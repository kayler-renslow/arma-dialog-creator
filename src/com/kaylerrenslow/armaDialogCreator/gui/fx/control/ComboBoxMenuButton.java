/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.control;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 10/19/2016.
 */
public class ComboBoxMenuButton<V> extends StackPane {

	private final MenuButton menuButton = new MenuButton();
	private final ValueObserver<V> selectedItemObserver = new ValueObserver<>(null);
	private final String placeholderText;
	private final Node placeholderGraphic;

	@SafeVarargs
	public ComboBoxMenuButton(String placeholderText, Node placeholderGraphic, CBMBMenuItem<V>[]... classGroups) {
		this(true, placeholderText, placeholderGraphic, classGroups);
	}

	@SafeVarargs
	public ComboBoxMenuButton(boolean allowClear, String placeholderText, Node placeholderGraphic, CBMBMenuItem<V>[]... classGroups) {
		this.placeholderText = placeholderText;
		this.placeholderGraphic = placeholderGraphic;
		getChildren().add(menuButton);

		int groupInd = 0;
		if (allowClear) {
			initializeClearMenuItem();
			if (classGroups.length > 0) {
				menuButton.getItems().add(new SeparatorMenuItem());
			}
		}
		clearSelectedValue();
		for (CBMBMenuItem<V>[] group : classGroups) {
			for (CBMBMenuItem<V> item : group) {
				menuButton.getItems().add(item);
				item.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						handleMenuItemAction(item);
						item.actionEvent(event);
					}
				});
			}
			if (classGroups.length > 1 && groupInd < classGroups.length) {
				menuButton.getItems().add(new SeparatorMenuItem());
			}
			groupInd++;
		}

	}

	private void initializeClearMenuItem() {
		final MenuItem clearMenuItem = new MenuItem(Lang.ApplicationBundle().getString("ComboBoxMenuButton.select_none"));
		clearMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearSelectedValue();
			}
		});
		menuButton.getItems().add(clearMenuItem);
	}

	public void clearSelectedValue() {
		selectedItemObserver.updateValue(null);
		menuButton.setText(placeholderText);
		menuButton.setGraphic(placeholderGraphic);
	}

	private void handleMenuItemAction(CBMBMenuItem<V> menuItem) {
		ImageView graphic = (ImageView) menuItem.getGraphic();
		if (graphic != null) {
			menuButton.setGraphic(new ImageView(graphic.getImage()));
		} else {
			menuItem.setGraphic(null);
		}
		menuButton.setText(menuItem.getValue().toString());
		selectedItemObserver.updateValue(menuItem.getValue());
	}

	@NotNull
	public ReadOnlyValueObserver<V> getSelectedItemObserver() {
		return selectedItemObserver.getReadOnlyValueObserver();
	}

	public static class CBMBMenuItem<V> extends MenuItem {
		protected final V value;

		public CBMBMenuItem(@NotNull V value, @Nullable ImageView graphic) {
			super(value.toString(), graphic);
			this.value = value;
			setMnemonicParsing(false);
		}

		public CBMBMenuItem(@NotNull V value) {
			this(value, null);
		}

		public V getValue() {
			return value;
		}

		protected void actionEvent(ActionEvent event) {

		}
	}
}
