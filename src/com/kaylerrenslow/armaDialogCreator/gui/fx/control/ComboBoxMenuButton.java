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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

/**
 Created by Kayler on 10/19/2016.
 */
public class ComboBoxMenuButton<V> extends StackPane {

	private final MenuButton menuButton = new MenuButton();
	private final ValueObserver<V> selectedItemObserver = new ValueObserver<>(null);
	private final String placeholderText;
	private final Node placeholderGraphic;

	@SafeVarargs
	public ComboBoxMenuButton(String placeholderText, Node placeholderGraphic, CBMBGroupMenu<V>... classGroups) {
		this(true, placeholderText, placeholderGraphic, classGroups);
	}

	@SafeVarargs
	public ComboBoxMenuButton(boolean allowClear, String placeholderText, Node placeholderGraphic, CBMBGroupMenu<V>... classGroups) {
		this.placeholderText = placeholderText;
		this.placeholderGraphic = placeholderGraphic;
		getChildren().add(menuButton);

		if (allowClear) {
			initializeClearMenuItem();
			if (classGroups.length > 0) {
				menuButton.getItems().add(new SeparatorMenuItem());
			}
		}
		clearSelectedValue();
		for (CBMBGroupMenu<V> group : classGroups) {
			menuButton.getItems().add(group);
			for (CBMBMenuItem<V> item : group.getCbmbMenuItems()) {
				item.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						chooseItem(item);
						item.actionEvent(event);
					}
				});
			}
		}

	}

	private void initializeClearMenuItem() {
		final MenuItem clearMenuItem = new MenuItem(Lang.FxControlBundle().getString("ComboBoxMenuButton.select_none"));
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

	public void chooseItem(@Nullable CBMBMenuItem<V> menuItem) {
		if (menuItem == null) {
			clearSelectedValue();
			return;
		}
		ImageContainer container = menuItem.imageContainer;
		if (container != null) {
			menuButton.setGraphic(container.copy().getNode());
		} else {
			menuButton.setGraphic(null);
		}
		menuButton.setText(menuItem.getValue().toString());
		selectedItemObserver.updateValue(menuItem.getValue());
	}

	@NotNull
	public ReadOnlyValueObserver<V> getSelectedItemObserver() {
		return selectedItemObserver.getReadOnlyValueObserver();
	}

	public static class CBMBGroupMenu<V> extends Menu {

		protected final CBMBMenuItem<V>[] cbmbMenuItems;

		@SafeVarargs
		public CBMBGroupMenu(@NotNull String groupName, CBMBMenuItem<V>... cbmbMenuItems) {
			super(groupName);
			this.cbmbMenuItems = cbmbMenuItems;
			Collections.addAll(getItems(), cbmbMenuItems);
		}

		public CBMBMenuItem<V>[] getCbmbMenuItems() {
			return cbmbMenuItems;
		}
	}

	public static class CBMBMenuItem<V> extends MenuItem {
		protected final V value;
		protected final ImageContainer imageContainer;

		public CBMBMenuItem(@NotNull V value, @Nullable ImageContainer image) {
			super(value.toString(), image == null ? null : image.getNode());
			this.value = value;
			this.imageContainer = image;
			setMnemonicParsing(false);
		}

		public CBMBMenuItem(@NotNull V value) {
			this(value, null);
		}

		@NotNull
		public V getValue() {
			return value;
		}

		protected void actionEvent(ActionEvent event) {

		}

	}
}
