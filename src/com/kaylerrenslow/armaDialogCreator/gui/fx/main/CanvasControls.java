package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.SnapConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.EditorComponentTreeView;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 @author Kayler
 Holds the step configuration contorls and the tree view for editor components.
 Created on 05/15/2016. */
class CanvasControls extends VBox implements SnapConfiguration {

	private final ADCCanvasView canvasView;
	private final EditorComponentTreeView editorComponentTreeView = new EditorComponentTreeView();
	private final ChoiceBox<Percentage> cbAltStep = new ChoiceBox<>();
	private final ChoiceBox<Percentage> cbStep = new ChoiceBox<>();
	private final CheckBox cbViewportSnap = new CheckBox(Lang.CanvasControls.VIEWPORT_SNAP);

	CanvasControls(ADCCanvasView canvasView) {
		super(5);
		this.canvasView = canvasView;
		initializeUI();
	}

	private void initializeUI() {
		cbViewportSnap.setSelected(true);
		cbViewportSnap.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				canvasView.repaintCanvas();
			}
		});
		initializeStepChoiceboxes();
		HBox hboxStep = new HBox(5);
		hboxStep.getChildren().addAll(new Label(Lang.CanvasControls.STEP), cbStep, new Label(Lang.CanvasControls.ALT_STEP), cbAltStep);

		getChildren().addAll(hboxStep,cbViewportSnap, editorComponentTreeView);
		this.setPadding(new Insets(5, 5, 0, 5));

		VBox.setVgrow(editorComponentTreeView, Priority.ALWAYS);
	}

	private void initializeStepChoiceboxes() {
		cbStep.getItems().addAll(new Percentage(1), new Percentage(2.50), new Percentage(5), new Percentage(10));
		cbAltStep.getItems().addAll(new Percentage(0.5));
		for (int i = 1; i <= 10; i++) {
			cbAltStep.getItems().add(new Percentage(i));
		}
		cbStep.getSelectionModel().select(1);
		cbAltStep.getSelectionModel().selectLast();
		cbStep.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Percentage>() {
			@Override
			public void changed(ObservableValue<? extends Percentage> observable, Percentage oldValue, Percentage newValue) {
				canvasView.repaintCanvas();
			}
		});
	}


	@Override
	public double alternateSnapPercentage() {
		return cbAltStep.getSelectionModel().getSelectedItem().percentDecimal;
	}

	@Override
	public double snapPercentage() {
		return cbStep.getSelectionModel().getSelectedItem().percentDecimal;
	}

	@Override
	public boolean snapRelativeToViewport() {
		return cbViewportSnap.isSelected();
	}

	private static class Percentage {
		private final double percentDecimal;
		private final String str;

		Percentage(double value) {
			this.percentDecimal = value / 100.0;
			this.str = value + "%";
		}

		@Override
		public String toString() {
			return str;
		}
	}

}
