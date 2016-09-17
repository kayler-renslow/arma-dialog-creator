/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.displayPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.DisplayProperty;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ValueEditor;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 @author Kayler
 Editor pane that edits {@link ArmaDisplay#getDisplayProperties()}
 Created on 09/17/2016. */
public class DisplayPropertiesEditorPane extends StackPane {
	private final ArmaDisplay display;
	private final LinkedList<DisplayPropertyEditorPane> editorPanes = new LinkedList<>();
	private final VBox root = new VBox(5);

	/**
	 @param display display to edit
	 */
	public DisplayPropertiesEditorPane(@NotNull ArmaDisplay display) {
		initRootPane();
		this.display = display;
		display.getDisplayProperties().addListener(new SetChangeListener<DisplayProperty>() {
			@Override
			public void onChanged(Change<? extends DisplayProperty> change) {
				if (change.wasAdded()) {
					addPropertyEditor(change.getElementAdded());
				} else if (change.wasRemoved()) {
					removePropertyEditor(change.getElementRemoved());
				}
			}
		});
		addPropertyEditors();
	}

	private void initRootPane() {
		final ScrollPane scrollPane = new ScrollPane(root);
		scrollPane.setPadding(new Insets(5));
		getChildren().add(scrollPane);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
	}

	private void addPropertyEditors() {
		for (DisplayProperty displayProperty : display.getDisplayProperties()) {
			addPropertyEditor(displayProperty);
		}
	}

	private void removePropertyEditor(@NotNull DisplayProperty property) {
		DisplayPropertyEditorPane found = null;
		for (DisplayPropertyEditorPane pane : editorPanes) {
			if (pane.getDisplayProperty().equals(property)) {
				found = pane;
				break;
			}
		}
		if(found == null){
			return;
		}
		root.getChildren().remove(found.getRootNode());
	}

	private void addPropertyEditor(@NotNull DisplayProperty displayProperty) {
		editorPanes.add(new DisplayPropertyEditorPane(displayProperty));
		root.getChildren().add(editorPanes.getLast().getRootNode());
	}

	private static class DisplayPropertyEditorPane {
		private final HBox hBox;
		private DisplayProperty displayProperty;

		public DisplayPropertyEditorPane(@NotNull DisplayProperty property) {
			this.displayProperty = property;
			final ValueEditor editor = ValueEditor.getEditor(property.getPropertyType(), ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment());
			editor.setValue(property.getValue());
			editor.getReadOnlyObserver().addValueListener(new ReadOnlyValueListener() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver observer, Object oldValue, Object newValue) {
					property.setValue((SerializableValue) newValue);
				}
			});
			if (editor.displayFullWidth()) {
				HBox.setHgrow(editor.getRootNode(), Priority.ALWAYS);
			}

			hBox = new HBox(5, new Label(property.getName() + "="), editor.getRootNode());
			hBox.setAlignment(Pos.CENTER_LEFT);
		}

		@NotNull
		public Node getRootNode() {
			return hBox;
		}

		@NotNull
		public DisplayProperty getDisplayProperty() {
			return displayProperty;
		}
	}
}
