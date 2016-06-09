package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.Label;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.ISnapConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.EditorComponentTreeView;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 @author Kayler
 Holds the step configuration contorls and the tree view for editor components.
 Created on 05/15/2016. */
class CanvasControls extends VBox implements ISnapConfiguration {

	private final CanvasView canvasView;
	private final EditorComponentTreeView editorComponentTreeView = new EditorComponentTreeView();
	private final ChoiceBox<Percentage> cbAltStep = new ChoiceBox<>();
	private final ChoiceBox<Percentage> cbStep = new ChoiceBox<>();

	CanvasControls(CanvasView canvasView) {
		super(5);
		this.canvasView = canvasView;
		initializeUI();
	}

	private void initializeUI() {
		initializeStepChoiceboxes();
		HBox hboxStep = new HBox(5);
		hboxStep.getChildren().addAll(new Label(Lang.CanvasControls.STEP), cbStep, new Label(Lang.CanvasControls.ALT_STEP), cbAltStep);

		getChildren().addAll(hboxStep, editorComponentTreeView);
		this.setPadding(new Insets(5, 5, 0, 5));

		VBox.setVgrow(editorComponentTreeView, Priority.ALWAYS);
	}

	private void initializeStepChoiceboxes() {
		cbStep.getItems().addAll(new Percentage(1), new Percentage(5), new Percentage(10));
		cbAltStep.getItems().addAll(new Percentage(0.5), new Percentage(1), new Percentage(2), new Percentage(3), new Percentage(4), new Percentage(5));
		cbStep.getSelectionModel().select(0);
		cbAltStep.getSelectionModel().select(0);
		cbStep.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Percentage>() {
			@Override
			public void changed(ObservableValue<? extends Percentage> observable, Percentage oldValue, Percentage newValue) {
				canvasView.repaintCanvas();
			}
		});
	}


	@Override
	public double alternateSnapPercentage() {
		return cbAltStep.getSelectionModel().getSelectedItem().value;
	}

	@Override
	public double snapPercentage() {
		return cbStep.getSelectionModel().getSelectedItem().value;
	}

	private static class Percentage {
		private final double value;

		Percentage(double value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value + "%";
		}
	}
}
