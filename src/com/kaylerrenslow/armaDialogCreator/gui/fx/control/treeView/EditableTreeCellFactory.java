/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class EditableTreeCellFactory<E extends TreeItemData> extends TreeCell<E> {
	/** How long it takes for the current hovered tree item that is expandable for it to expand and show its children. Value is in milliseconds. */
	private static final long WAIT_DURATION_TREE_VIEW_FOLDER = 500;
	private static final Color COLOR_TREE_VIEW_DRAG = Color.ORANGE;

	private static final long DOUBLE_CLICK_WAIT_TIME_MILLIS = 250;
	private static final String HOVER_TREE_CELL = "hover-tree-cell";

	private final TreeCellSelectionUpdate treeCellSelectionUpdate;
	private final EditableTreeView<E> treeView;

	private TextField textField;

	private long waitStartTime = 0;

	/** Millisecond epoch when this cell was selected (used for double click detection) */
	private long lastSelectedTime = -1;

	private static TreeItem<?> dragging; //must be static since the factory is created for each cell and dragging takes place over more than once cell
	private boolean hasDoubleClicked;

	private static TreeCell<?> hoveredChildParent; //static for the same reasons as dragging

	EditableTreeCellFactory(@NotNull EditableTreeView<E> treeView, @Nullable TreeCellSelectionUpdate treeCellSelectionUpdate) {
		this.treeCellSelectionUpdate = treeCellSelectionUpdate;
		this.treeView = treeView;
		this.setEditable(true);
		// first method called when the user clicks and drags a tree item
		TreeCell<E> myTreeCell = this;
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				long now = System.currentTimeMillis();
				hasDoubleClicked = now - lastSelectedTime <= DOUBLE_CLICK_WAIT_TIME_MILLIS;
				if (hasDoubleClicked) {
					startEdit();
					hasDoubleClicked = false; //I don't know why this statement makes it work correctly
				}
				lastSelectedTime = now;
			}
		});
		this.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (getTreeItem() == null) {
					event.consume();
					return;
				}
				// disables right clicking to start drag and drop
				if (event.isSecondaryButtonDown()) {
					event.consume();
					return;
				}

				EditableTreeView view = (EditableTreeView) getTreeView();

				Dragboard dragboard = view.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content;
				dragging = getTreeItem();
				content = new ClipboardContent();
				content.putString("");// don't remove. drag and drop doesn't work if this doesn't happen for a strange reason
				dragboard.setContent(content);

				event.consume();

			}
		});
		this.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				TreeItem<E> dragging = getDragging();
				// not dragging on the tree
				if (getTreeItem() == null) {
					return;
				}

				// trying to drag into itself
				if (getTreeItem().getValue().equals(getTreeView().getSelectionModel().getSelectedItem().getValue())) {
					return;
				}

				// if it's a folder, we need to make sure it doesn't drag itself into its children
				if (dragging.getValue().canHaveChildren()) {
					if (TreeUtil.hasDescendant(dragging, getTreeItem())) {
						return;
					}
				}

				// auto expands the hovered tree item if it has children after a period of time.
				// timer is reset when the mouse moves away from the current hovered item
				if (getTreeItem().getValue().canHaveChildren()) {
					if (!getTreeItem().isExpanded()) {
						if (waitStartTime == 0) {
							waitStartTime = System.currentTimeMillis();
						} else {
							long now = System.currentTimeMillis();
							// wait a while before the tree item is expanded
							if (waitStartTime + WAIT_DURATION_TREE_VIEW_FOLDER <= now) {
								getTreeItem().setExpanded(true);
								waitStartTime = 0;
							}
						}
					}
					hoveredChildParent = myTreeCell;
					if(!hoveredChildParent.getStyleClass().contains(HOVER_TREE_CELL)){ //don't add multiple times
						hoveredChildParent.getStyleClass().add(HOVER_TREE_CELL);
					}
				} else {
					setEffect(new InnerShadow(1.0, 0, 2.0, COLOR_TREE_VIEW_DRAG));
				}

				// when it reaches this point, the tree item is okay to move
				event.acceptTransferModes(TransferMode.MOVE);
			}
		});

		this.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				TreeItem<E> dragging = getDragging();
				event.acceptTransferModes(TransferMode.MOVE);

				if (getTreeItem().getValue().canHaveChildren()) {
					treeView.moveTreeItem(dragging, getTreeItem(), getTreeItem().getChildren().size());
				}else {
					int index = getTreeItem().getParent().getChildren().lastIndexOf(getTreeItem());
					treeView.moveTreeItem(dragging, getTreeItem().getParent(), index);
					getTreeView().getSelectionModel().select(index + 1);
				}


				EditableTreeCellFactory.dragging = null;
				getTreeView().getSelectionModel().clearSelection();
				clearHoveredParentHoverStyle();
			}

		});

		this.setOnDragExited(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				clearHoveredParentHoverStyle();
				if (getTreeItem() == null) {
					// this happens when something is still being dragged but it isn't over the tree
					return;
				}

				waitStartTime = 0;
				setEffect(null);
				setBorder(null);
			}

		});
	}

	private void clearHoveredParentHoverStyle() {
		if (hoveredChildParent != null) {
			hoveredChildParent.getStyleClass().remove(HOVER_TREE_CELL);
		}
	}

	EditableTreeCellFactory<E> getNewInstance() {
		return new EditableTreeCellFactory<>(this.treeView, this.treeCellSelectionUpdate);
	}

	@SuppressWarnings("unchecked")
	private TreeItem<E> getDragging() {
		return (TreeItem<E>) EditableTreeCellFactory.dragging;
	}

	@Override
	public void startEdit() {
		if (!hasDoubleClicked) {
			return;
		}
		if (getTreeItem() == null) {
			return;
		}
		super.startEdit();
		if (textField == null) {
			createTextField();
		}
		setText(null);
		setGraphic(textField);
		textField.requestFocus();
		textField.selectAll();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getTreeItem().getValue().getText());
		setGraphic(getTreeItem().getValue().getGraphic());
		textField = null;
	}

	@Override
	protected void updateItem(E node, boolean empty) {
		super.updateItem(node, empty);
		// this adds a textfield to the tree item to get a new name
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
				}
				setText(null);
				setGraphic(textField);
			} else {
				if (textField != null) {
					setText(textField.getText());
					getTreeItem().getValue().setText(textField.getText());
					textField = null;
				} else {
					setText(getItem().getText());
				}
				setGraphic(getTreeItem().getValue().getGraphic());
			}
		}
		if (node != null) {
			node.setText(getText());
		}
	}

	private void createTextField() {
		textField = new TextField(getString());
		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ENTER) {
					commitEdit(getItem());
				} else if (t.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			}
		});
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean focused) {
				if (!focused) {
					cancelEdit();
				}
			}
		});

	}

	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		if (getTreeItem() == null) {
			if (treeCellSelectionUpdate != null) {
				treeCellSelectionUpdate.selectionUpdate(null, null);
			}
			return;
		}
		if (treeCellSelectionUpdate != null) {
			CellType parentType = (getTreeItem().getParent() != null ? getTreeItem().getParent().getValue().getCellType() : null);
			if (getTreeItem().isLeaf()) {
				treeCellSelectionUpdate.selectionUpdate(CellType.LEAF, parentType);
			} else if (getTreeItem().getValue().isFolder()) {
				treeCellSelectionUpdate.selectionUpdate(CellType.FOLDER, parentType);
			} else if (getTreeItem().getValue().isComposite()) {
				treeCellSelectionUpdate.selectionUpdate(CellType.COMPOSITE, parentType);
			} else {
				treeCellSelectionUpdate.selectionUpdate(null, parentType);
			}
		}
	}

	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}

}
