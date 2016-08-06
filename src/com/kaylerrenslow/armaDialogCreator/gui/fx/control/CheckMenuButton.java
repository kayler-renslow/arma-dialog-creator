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
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 Created by Kayler on 08/05/2016.
 */
public class CheckMenuButton<E> extends StackPane {
	private final MenuButton menuButton;
	private final ObservableList<E> selected = FXCollections.observableArrayList();
	private final ObservableList<E> selectedReadOnly = FXCollections.unmodifiableObservableList(selected);
	
	private final ObservableList<E> items;
	
	public CheckMenuButton(String title, Node graphic, E... initialItems) {
		this.items = FXCollections.observableArrayList(new ArrayList<>(initialItems.length));
		items.addListener(new ListChangeListener<E>() {
			@Override
			public void onChanged(Change<? extends E> c) {
				while (c.next()) {
					for (E item : c.getAddedSubList()) {
						CheckBox checkBox = new CheckBox(item.toString());
						CustomMenuItem check = new CustomMenuItem(checkBox, false);
						check.setUserData(item);
						checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
							@Override
							public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean checked) {
								if (checked) {
									if (!selected.contains(item)) {
										selected.add(item);
									}
								} else {
									selected.remove(item);
								}
							}
						});
						menuButton.getItems().add(check);
					}
					List<MenuItem> items = menuButton.getItems();
					int i = 0;
					for (E item : c.getRemoved()) {
						while (i < items.size()) {
							if (items.get(i).getUserData() == item) {
								items.remove(i);
								break;
							}
							i++;
						}
					}
				}
			}
		});
		menuButton = new MenuButton(title, graphic);
		getChildren().add(menuButton);
		Collections.addAll(this.items, initialItems);
	}
	
	public ObservableList<E> getItems() {
		return items;
	}
	
	public void setSelected(E[] items) {
		menuItems:
		for (MenuItem menuItem : menuButton.getItems()) {
			CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
			CheckBox checkBox = (CheckBox) customMenuItem.getContent();
			checkBox.setSelected(false);
			for (E item : items) {
				if (customMenuItem.getUserData() == item) {
					checkBox.setSelected(true);
					continue menuItems;
				}
			}
		}
	}
	
	public void clearSelection() {
		for (MenuItem menuItem : menuButton.getItems()) {
			CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
			CheckBox checkBox = (CheckBox) customMenuItem.getContent();
			checkBox.setSelected(false);
		}
	}
	
	public ObservableList<E> getSelectedItems() {
		return selectedReadOnly;
	}
}
