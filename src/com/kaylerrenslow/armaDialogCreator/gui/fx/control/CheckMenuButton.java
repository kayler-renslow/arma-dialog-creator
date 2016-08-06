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
							} else {
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
	
	public ObservableList<E> getItems() {
		return items;
	}
	
	public void setSelected(E[] items) {
		menuItems:
		for (MenuItem menuItem : menuButton.getItems()) {
			CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
			CheckBox checkBox = (CheckBox) customMenuItem.getContent();
			for (E item : items) {
				if (customMenuItem.getUserData() == item) {
					checkBox.setSelected(true);
					continue menuItems;
				}
			}
			checkBox.setSelected(false);
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
