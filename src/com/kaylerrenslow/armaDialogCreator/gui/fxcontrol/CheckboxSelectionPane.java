package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 A scrollable pane that has a list of checkboxes to select. The checkboxes change color when selected.

 @author Kayler
 @since 05/05/2017 */
public class CheckboxSelectionPane<E> extends StackPane {
	private final LinkedList<CheckBox> boxList = new LinkedList<>();
	private final VBox vboxCheckBoxes = new VBox(0);
	private final Insets padding = new Insets(4);
	private final ObservableList<E> selectedList = FXCollections.observableArrayList();

	public CheckboxSelectionPane() {
		ScrollPane pane = new ScrollPane(vboxCheckBoxes);
		pane.setFitToWidth(true);
		pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		vboxCheckBoxes.setFillWidth(true);
		getChildren().add(pane);
		selectedList.addListener((ListChangeListener<? super E>) c -> {
			while (c.next()) {
				if (c.wasAdded()) {
					for (int i = c.getFrom(); i < c.getTo(); i++) {
						E e = selectedList.get(i);
						for (CheckBox box : boxList) {
							if (box.getUserData().equals(e)) {
								box.setSelected(true);
								break;
							}
						}
					}
				} else if (c.wasRemoved()) {
					for (E e : c.getRemoved()) {
						for (CheckBox box : boxList) {
							if (box.getUserData().equals(e)) {
								box.setSelected(false);
								break;
							}
						}
					}
				}
			}
		});

		setPadding(padding);
	}

	public void addItem(@NotNull E item) {
		CheckBox box = newCheckBox(item);
		boxList.add(box);
		vboxCheckBoxes.getChildren().add(box);
	}

	public void removeItem(@NotNull E item) {
		CheckBox remove = null;
		int removeInd = 0;
		for (CheckBox box : boxList) {
			if (box.getUserData().equals(item)) {
				remove = box;
				break;
			}
			removeInd++;
		}
		if (remove == null) {
			return;
		}
		boxList.remove(removeInd);
		vboxCheckBoxes.getChildren().remove(remove);
	}

	@NotNull
	public ObservableList<E> getSelected() {
		return selectedList;
	}

	@NotNull
	private CheckBox newCheckBox(@NotNull E data) {
		CheckBox box = new CheckBox(data.toString());
		box.setUserData(data);
		box.minWidthProperty().bind(vboxCheckBoxes.widthProperty());
		box.selectedProperty().addListener((observable, oldValue, selected) -> {
			if (selected) {
				if (!selectedList.contains(data)) {
					selectedList.add((E) box.getUserData());
				}
				box.setStyle("-fx-background-color:-fx-accent;");
			} else {
				selectedList.remove(box.getUserData());
				box.setStyle("");
			}
		});
		box.setPadding(padding);
		return box;
	}
}
