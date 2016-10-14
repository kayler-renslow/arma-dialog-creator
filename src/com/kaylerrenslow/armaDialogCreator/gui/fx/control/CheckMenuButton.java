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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 Created by Kayler on 08/05/2016.
 */
public class CheckMenuButton<E> extends StackPane {
	private static final Font TOOLTIP_FONT = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20d);
	private final MenuButton menuButton;
	private final ObservableList<E> selected = FXCollections.observableArrayList();
	private final ObservableList<E> selectedReadOnly = FXCollections.unmodifiableObservableList(selected);

	private final ObservableList<E> items;

	private static final String CHECK_BOX_BOLD_TEXT = "-fx-font-weight:900";

	/**
	 Creates a {@link MenuButton} that allows for selecting many items.

	 @param title text to display on the button
	 @param graphic graphic to put on the button
	 @param initialItems items to initially add
	 */
	public CheckMenuButton(String title, Node graphic, E... initialItems) {
		this.items = FXCollections.observableArrayList(new ArrayList<>(initialItems.length));
		items.addListener(new ListChangeListener<E>() {
			@Override
			public void onChanged(Change<? extends E> c) {
				while (c.next()) {
					if (c.wasAdded()) {
						for (E item : c.getAddedSubList()) {
							final CheckBox checkBox = new CheckBox(item.toString());
							final CustomMenuItem menuItem = new CustomMenuItem(checkBox, false);
							menuItem.setUserData(item);
							checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean checked) {
									if (checked) {
										if (!selected.contains(item)) {
											selected.add(item);
										}
										checkBox.setStyle(CHECK_BOX_BOLD_TEXT);
									} else {
										selected.remove(item);
										checkBox.setStyle(null);
									}
								}
							});
							menuButton.getItems().add(menuItem);
						}
					}
					if (c.wasRemoved()) {
						List<MenuItem> menuItems = menuButton.getItems();
						int i = 0;
						for (E item : c.getRemoved()) {
							while (i < menuItems.size()) {
								if (menuItems.get(i).getUserData() == item) {
									menuItems.remove(i);
									break;
								}
								i++;
							}
						}
					}
				}
			}
		});
		menuButton = new MenuButton(title, graphic);
		getChildren().add(menuButton);
		Collections.addAll(this.items, initialItems);
	}

	/** Bind a tooltip to one of the items, If the tooltip is null, will remove the tooltip. */
	public void bindTooltip(@NotNull E itemToBindTo, @Nullable String tooltip) {
		for (MenuItem menuItem : menuButton.getItems()) {
			CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
			CheckBox checkBox = (CheckBox) customMenuItem.getContent();
			if (customMenuItem.getUserData() == itemToBindTo) {
				if (tooltip == null) {
					checkBox.setTooltip(null);
				} else {
					Tooltip tp = new Tooltip(tooltip);
					tp.setFont(TOOLTIP_FONT);
					checkBox.setTooltip(tp);
				}
				return;
			}
		}
	}

	/** Set the tooltip of the underlying {@link MenuButton} (equivalent to {@link MenuButton#setTooltip(Tooltip)}) */
	public void setTooltip(Tooltip tooltip) {
		menuButton.setTooltip(tooltip);
	}

	/** Get all items added */
	public ObservableList<E> getItems() {
		return items;
	}

	/** Clears the selection and sets what items are selected. */
	public void setSelected(E[] items) {
		for (MenuItem menuItem : menuButton.getItems()) {
			CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
			CheckBox checkBox = (CheckBox) customMenuItem.getContent();
			boolean found = false;
			for (E item : items) {
				if (customMenuItem.getUserData() == item) {
					checkBox.setSelected(true);
					found = true;
					break;
				}
			}
			if (!found) {
				checkBox.setSelected(false);
			}

		}
	}

	/** Clears the selection */
	public void clearSelection() {
		for (MenuItem menuItem : menuButton.getItems()) {
			CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
			CheckBox checkBox = (CheckBox) customMenuItem.getContent();
			checkBox.setSelected(false);
		}
	}

	/** Get all selected items (read-only list) */
	public ObservableList<E> getSelectedItems() {
		return selectedReadOnly;
	}
}
