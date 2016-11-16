/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls;

import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 Used for displaying items separated by categories and allowing the user to choose which item they want.

 @author Kayler
 @since 11/13/2016. */
public class ChooseItemPopup<V> extends StageDialog<VBox> {

	private final List<ItemCategoryTab<V>> itemCategoryTabs;
	private final TabPane tabPane = new TabPane();
	private V selectedItem;
	private String searchText;

	public ChooseItemPopup(@NotNull ItemCategory<V>[] categories, @NotNull List<V> allItems, @NotNull String dialogTitle, @NotNull String headerTitle) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), dialogTitle, true, true, true);
		myStage.initStyle(StageStyle.UTILITY);
		myRootElement.setMinWidth(720d);

		itemCategoryTabs = new ArrayList<>(categories.length);
		for (ItemCategory<V> category : categories) {
			itemCategoryTabs.add(new ItemCategoryTab<>(category, allItems));
		}

		initRootElement(headerTitle);
		myStage.setResizable(false);
	}

	private void initRootElement(@NotNull String headerTitle) {
		myRootElement.setPadding(new Insets(10));
		final Label lblChooseMacro = new Label(headerTitle);
		lblChooseMacro.setFont(Font.font(15d));

		HBox hbSearch = initializeSearchBox();
		myRootElement.getChildren().add(new BorderPane(null, null, hbSearch, null, lblChooseMacro));
		myRootElement.getChildren().add(new Separator(Orientation.HORIZONTAL));

		final ChangeListener<? super V> selectedItemListener = new ChangeListener<V>() {
			@Override
			public void changed(ObservableValue<? extends V> observable, V oldValue, V newValue) {
				ChooseItemPopup.this.btnOk.setDisable(newValue == null);
				ChooseItemPopup.this.selectedItem = newValue;
				getSelectedTab().getCategory().newItemSelected(selectedItem);
			}
		};
		for (ItemCategoryTab<V> tab : itemCategoryTabs) {
			tabPane.getTabs().add(tab);
			tab.getListView().getSelectionModel().selectedItemProperty().addListener(selectedItemListener);
		}

		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab selected) {
				newCategorySelected(getSelectedTab());
			}
		});


		//force update selected tab
		tabPane.getSelectionModel().selectLast();
		tabPane.getSelectionModel().selectFirst();

		myRootElement.getChildren().add(tabPane);

		myStage.sizeToScene();
		btnOk.setDisable(true);
	}

	@NotNull
	private HBox initializeSearchBox() {
		TextField tfSearch = new TextField("");
		HBox.setHgrow(tfSearch, Priority.ALWAYS);
		HBox hbSearch = new HBox(5, new Label(Lang.ApplicationBundle().getString("Popups.ChooseItem.search_text_box")), tfSearch);
		hbSearch.setAlignment(Pos.CENTER_LEFT);
		tfSearch.setPrefColumnCount(20);
		hbSearch.setMaxWidth(Double.MAX_VALUE);
		hbSearch.setFillHeight(true);

		tfSearch.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				getSelectedTab().limitToSearch(newValue);
				searchText = newValue;
			}
		});

		return hbSearch;
	}

	/** A new category tab was selected. Default implementation is nothing. */
	protected void newCategorySelected(@NotNull ItemCategoryTab<V> selected) {
		selected.limitToSearch(searchText);
	}

	@SuppressWarnings("unchecked")
	protected ItemCategoryTab<V> getSelectedTab() {
		return (ItemCategoryTab<V>) tabPane.getSelectionModel().getSelectedItem();
	}

	@Override
	protected void cancel() {
		selectedItem = null;
		super.cancel();
	}

	@Override
	protected void onCloseRequest(WindowEvent event) {
		selectedItem = null;
		super.onCloseRequest(event);
	}

	/** Return the item chosen. If null, no macro was chosen. */
	@Nullable
	public V getChosenItem() {
		return selectedItem;
	}

	protected static class ItemCategoryTab<V> extends Tab {
		private final ItemCategory<V> category;
		private final ListView<V> listView;
		private final LinkedList<V> removed = new LinkedList<>();
		private final Comparator<V> comparator = new ListViewComparator<>();

		public ItemCategoryTab(@NotNull ItemCategory<V> category, @NotNull List<V> allItemsFromMasterCategory) {
			super(category.categoryDisplayName());
			this.category = category;
			List<V> allItemsInCategory = new LinkedList<>();
			for (V v : allItemsFromMasterCategory) {
				if (category.itemInCategory(v)) {
					allItemsInCategory.add(v);
				}
			}

			listView = new ListView<>();

			final Label lblListView = new Label(category.availableItemsDisplayText(), listView);
			lblListView.setContentDisplay(ContentDisplay.BOTTOM);

			final HBox root = new HBox(10, lblListView);
			root.setPadding(new Insets(5));
			final Node categoryNode = category.getMiscCategoryNode();
			if (categoryNode != null) {
				root.getChildren().add(categoryNode);
				HBox.setHgrow(categoryNode, Priority.ALWAYS);
			}
			setContent(root);
			setClosable(false);

			listView.setPlaceholder(new Label(category.noItemsPlaceholderText()));
			listView.setMinWidth(250d);
			for (V v : allItemsInCategory) {
				listView.getItems().add(v);
			}

		}

		@NotNull
		public ListView<V> getListView() {
			return listView;
		}

		@NotNull
		public ItemCategory<V> getCategory() {
			return category;
		}

		@NotNull
		public List<V> getAllItemsInCategory() {
			return listView.getItems();
		}

		public void limitToSearch(@Nullable String searchWord) {
			searchWord = searchWord != null ? searchWord.toUpperCase() : "";
			if (searchWord.length() == 0) {
				while (removed.size() > 0) {
					getListView().getItems().add(removed.removeFirst());
				}
				return;
			}

			for (int i = 0; i < getListView().getItems().size(); ) {
				V v = getListView().getItems().get(i);
				if (!v.toString().contains(searchWord)) {
					removed.add(v);
					getListView().getItems().remove(i);
					continue;
				}
				i++;
			}
			getListView().getItems().sort(comparator);
		}

	}

	public interface ItemCategory<V> {
		/** Returns a String that is presentable to the user that is the name of the category */
		@NotNull
		String categoryDisplayName();

		/** Returns a String that is presentable to the user that is used when no items are in the given category */
		@NotNull
		String noItemsPlaceholderText();

		/** Returns a String that is presentable to the user that is placed above the {@link ListView} that holds all items for this category */
		@NotNull
		String availableItemsDisplayText();

		/** Return true if the given item is inside this category, false otherwise */
		boolean itemInCategory(@NotNull V item);

		/** Get a {@link Node} instance to be placed inside a {@link Tab}'s content node alongside the {@link ListView} */
		@Nullable
		Node getMiscCategoryNode();

		/** Invoked when an item is selected. This may be used to update the {@link #getMiscCategoryNode()} */
		void newItemSelected(@Nullable V item);
	}

	private static class ListViewComparator<V> implements Comparator<V> {

		@Override
		public int compare(V o1, V o2) {
			if (o1 == null) {
				return 1;
			}
			if (o2 == null) {
				return -1;
			}
			return o1.toString().compareTo(o2.toString());
		}
	}
}
