package com.armadialogcreator.gui.fxcontrol;

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
							CustomMenuItem menuItem = createCheckBoxMenuItem(item);
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

	@NotNull
	private CustomMenuItem createCheckBoxMenuItem(E item) {
		CheckBox checkBox = new CheckBox(item.toString());
		CustomMenuItem menuItem = new CustomMenuItem(checkBox, false);
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
		return menuItem;
	}

	/** Bind a tooltip to one of the items, If the tooltip is null, will remove the tooltip. */
	public void bindTooltip(@NotNull E itemToBindTo, @Nullable String tooltip) {
		for (MenuItem menuItem : menuButton.getItems()) {
			if (!(menuItem instanceof CustomMenuItem)) {
				continue;
			}
			CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
			if (!(customMenuItem.getContent() instanceof CheckBox)) {
				continue;
			}
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

	/** @return all items added */
	@NotNull
	public ObservableList<E> getItems() {
		return items;
	}

	/** Adds a {@link SeparatorMenuItem} */
	public void addSeparator() {
		menuButton.getItems().add(new SeparatorMenuItem());
	}

	public void addMenu(@NotNull String menuName, @NotNull List<E> items) {
		Menu m = new Menu(menuName);
		menuButton.getItems().add(m);
		for (E e : items) {
			m.getItems().add(createCheckBoxMenuItem(e));
		}
	}

	/** Clears the selection and sets what items are selected. */
	public void setSelected(E[] items) {
		setSelected(null, items, menuButton.getItems());
	}

	/** Clears the selection and sets what items are selected. */
	public void setSelected(List<E> items) {
		setSelected(items, null, menuButton.getItems());
	}

	private void setSelected(@Nullable List<E> itemsInList, @Nullable E[] itemsInArray, List<MenuItem> menuItems) {
		for (MenuItem menuItem : menuItems) {
			if (menuItem instanceof Menu) {
				setSelected(itemsInList, itemsInArray, ((Menu) menuItem).getItems());
			}
			if (!(menuItem instanceof CustomMenuItem)) {
				continue;
			}
			CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
			if (!(customMenuItem.getContent() instanceof CheckBox)) {
				continue;
			}
			CheckBox checkBox = (CheckBox) customMenuItem.getContent();
			boolean found = false;
			if (itemsInList != null) {
				for (E item : itemsInList) {
					if (customMenuItem.getUserData() == item) {
						checkBox.setSelected(true);
						found = true;
						break;
					}
				}
			}
			if (!found && itemsInArray != null) {
				for (E item : itemsInArray) {
					if (customMenuItem.getUserData() == item) {
						checkBox.setSelected(true);
						found = true;
						break;
					}
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
			if (!(menuItem instanceof CustomMenuItem)) {
				continue;
			}
			CustomMenuItem customMenuItem = (CustomMenuItem) menuItem;
			if (!(customMenuItem.getContent() instanceof CheckBox)) {
				continue;
			}
			CheckBox checkBox = (CheckBox) customMenuItem.getContent();
			checkBox.setSelected(false);
		}
	}

	/** Get all selected items (read-only list) */
	public ObservableList<E> getSelectedItemsReadOnly() {
		return selectedReadOnly;
	}
}
