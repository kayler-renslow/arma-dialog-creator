package com.kaylerrenslow.armaDialogCreator.gui.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.gui.main.editor.UICanvasConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.EditorComponentTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 Holds the step configuration contorls and the tree view for editor components.

 @author Kayler
 @since 05/15/2016. */
class CanvasControls extends VBox implements UICanvasConfiguration {

	private final ADCCanvasView canvasView;
	private final EditorComponentTreeView<? extends TreeItemEntry> treeViewMain;
	private final EditorComponentTreeView<? extends TreeItemEntry> treeViewBg;
	private final ChoiceBox<Percentage> choiceBoxAltStep = new ChoiceBox<>();
	private final ChoiceBox<Percentage> choiceBoxStep = new ChoiceBox<>();
	private final CheckBox checkBoxViewportSnapping = new CheckBox(Lang.ApplicationBundle().getString("CanvasControls.viewport_snapping"));

	public static final double PREFERRED_WIDTH = 250d;
	private boolean showGrid = true;

	CanvasControls(ADCCanvasView canvasView) {
		super(5);
		this.canvasView = canvasView;
		treeViewMain = new EditorComponentTreeView<>(false);
		treeViewBg = new EditorComponentTreeView<>(true);
		initializeUI();
	}

	private void initializeUI() {
		initializeStepChoiceboxes();
		FlowPane flowPaneStep = new FlowPane(5, 5,
				new HBox(5, new Label(Lang.ApplicationBundle().getString("CanvasControls.step")), choiceBoxStep),
				new HBox(5, new Label(Lang.ApplicationBundle().getString("CanvasControls.alt_step")), choiceBoxAltStep),
				checkBoxViewportSnapping
		);

		checkBoxViewportSnapping.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				canvasView.repaintCanvas();
			}
		});

		final CheckBox cbShowBackgroundControls = new CheckBox(Lang.ApplicationBundle().getString("CanvasControls.show"));
		cbShowBackgroundControls.setSelected(true);
		cbShowBackgroundControls.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
				for (ArmaControl control : canvasView.getEditingDisplay().getBackgroundControls()) {
					control.getRenderer().setGhost(!selected);
				}
			}
		});
		final CheckBox cbShowControls = new CheckBox(Lang.ApplicationBundle().getString("CanvasControls.show"));
		cbShowControls.setSelected(true);
		cbShowControls.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
				for (ArmaControl control : canvasView.getEditingDisplay().getControls()) {
					control.getRenderer().setGhost(!selected);
				}
			}
		});

		final double vertSpacing = 5;
		final HBox bgControlsHbox = new HBox(10, new Label(Lang.ApplicationBundle().getString("CanvasControls.background_controls")), cbShowBackgroundControls);
		final VBox vbBgControls = new VBox(vertSpacing, bgControlsHbox, treeViewBg);
		final HBox controlHbox = new HBox(10, new Label(Lang.ApplicationBundle().getString("CanvasControls.controls")), cbShowControls);
		final VBox vbControls = new VBox(vertSpacing, controlHbox, treeViewMain);
		// padding
		VBox.setVgrow(treeViewBg, Priority.ALWAYS);
		VBox.setVgrow(treeViewMain, Priority.ALWAYS);
		bgControlsHbox.setPadding(new Insets(vertSpacing, vertSpacing, 0, vertSpacing));
		controlHbox.setPadding(bgControlsHbox.getPadding());

		final SplitPane splitPane = new SplitPane(vbBgControls, vbControls);
		splitPane.setOrientation(Orientation.VERTICAL);
		getChildren().addAll(flowPaneStep, splitPane);
		VBox.setVgrow(splitPane, Priority.ALWAYS);
		splitPane.setPrefHeight(getHeight());

		this.setPadding(new Insets(5, 5, 0, 5));
	}

	private void initializeStepChoiceboxes() {
		choiceBoxStep.getItems().addAll(new Percentage(1), new Percentage(2.50), new Percentage(5), new Percentage(10));
		choiceBoxAltStep.getItems().addAll(new Percentage(0.5));
		for (int i = 1; i <= 10; i++) {
			choiceBoxAltStep.getItems().add(new Percentage(i));
		}
		choiceBoxStep.getSelectionModel().select(1);
		choiceBoxAltStep.getSelectionModel().selectLast();
		ChangeListener<? super Percentage> selectUpdate = new ChangeListener<Percentage>() {
			@Override
			public void changed(ObservableValue<? extends Percentage> observable, Percentage oldValue, Percentage newValue) {
				canvasView.repaintCanvas();
			}
		};
		choiceBoxStep.getSelectionModel().selectedItemProperty().addListener(selectUpdate);
		choiceBoxAltStep.getSelectionModel().selectedItemProperty().addListener(selectUpdate);
	}


	@Override
	public double alternateSnapPercentage() {
		return choiceBoxAltStep.getSelectionModel().getSelectedItem().percentDecimal;
	}

	@Override
	public double snapPercentage() {
		return choiceBoxStep.getSelectionModel().getSelectedItem().percentDecimal;
	}

	@Override
	public boolean viewportSnapEnabled() {
		return checkBoxViewportSnapping.isSelected();
	}

	@Override
	public boolean isSafeMovement() {
		return false;
	}

	@Override
	public boolean showGrid() {
		return showGrid;
	}

	@Override
	public void setViewportSnapEnabled(boolean set) {
		checkBoxViewportSnapping.setSelected(set);
	}

	@Override
	public void setSafeMovement(boolean safeMovement) {

	}

	@Override
	public void setShowGrid(boolean set) {
		this.showGrid = set;
		canvasView.repaintCanvas();
	}

	/** Get the tree view used for storing controls that is meant for non-background controls */
	public EditorComponentTreeView<? extends TreeItemEntry> getTreeViewMain() {
		return treeViewMain;
	}

	/** Get the tree view used for storing controls that is meant <b>for</b> background controls */
	public EditorComponentTreeView<? extends TreeItemEntry> getTreeViewBackground() {
		return treeViewBg;
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
