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

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;

/**
 @author Kayler
 @since 11/15/2016 */
public class CBMBGroupMenu<V> extends Menu {

	protected final ObservableList<CBMBMenuItem<V>> cbmbMenuItems = FXCollections.observableArrayList(new LinkedList<>());
	private boolean addedFromCbmbItems = false;

	@SafeVarargs
	public CBMBGroupMenu(@NotNull String groupName, CBMBMenuItem<V>... cbmbMenuItems) {
		super(groupName);
		Collections.addAll(this.cbmbMenuItems, cbmbMenuItems);
		Collections.addAll(getItems(), cbmbMenuItems);

		this.cbmbMenuItems.addListener(new ListChangeListener<CBMBMenuItem<V>>() {
			@Override
			public void onChanged(Change<? extends CBMBMenuItem<V>> c) {
				addedFromCbmbItems = true;
				while (c.next()) {
					if (c.wasAdded()) {
						for (CBMBMenuItem<V> added : c.getAddedSubList()) {
							getItems().add(added);
						}
					} else if (c.wasRemoved()) {
						getItems().remove(c.getRemoved());
					}
				}
				addedFromCbmbItems = false;
			}
		});
		getItems().addListener(new ListChangeListener<MenuItem>() {
			@Override
			public void onChanged(Change<? extends MenuItem> c) {
				if (addedFromCbmbItems) {
					return;
				}
				while (c.next()) {
					if (c.wasAdded()) {
						for (MenuItem menuItem : c.getAddedSubList()) {
							if (menuItem instanceof CBMBMenuItem) {
								try {
									CBMBGroupMenu.this.getCbmbMenuItems().add((CBMBMenuItem<V>) menuItem);
									continue;
								} catch (ClassCastException e) {

								}
							}
							throw new IllegalStateException("can't add a non-CBMBMenuItem to this menu");
						}
					} else if (c.wasRemoved()) {
						CBMBGroupMenu.this.getCbmbMenuItems().remove(c);
					}
				}
			}
		});
	}

	@NotNull
	public ObservableList<CBMBMenuItem<V>> getCbmbMenuItems() {
		return cbmbMenuItems;
	}
}
