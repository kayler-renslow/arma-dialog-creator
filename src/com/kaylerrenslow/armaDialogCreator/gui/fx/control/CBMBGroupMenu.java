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
