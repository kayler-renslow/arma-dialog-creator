package com.kaylerrenslow.armaDialogCreator.gui.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImages;
import com.kaylerrenslow.armaDialogCreator.gui.main.editor.UICanvasConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.EditorComponentTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ResourceBundle;
import java.util.function.Function;

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
	private final ResourceBundle bundle = Lang.ApplicationBundle();
	private final CheckBox checkBoxViewportSnapping = new CheckBox(bundle.getString("CanvasControls.viewport_snapping"));

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
				new HBox(5, new Label(bundle.getString("CanvasControls.step")), choiceBoxStep),
				new HBox(5, new Label(bundle.getString("CanvasControls.alt_step")), choiceBoxAltStep),
				checkBoxViewportSnapping
		);

		checkBoxViewportSnapping.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				canvasView.repaintCanvas();
			}
		});

		CheckBox cbShowBackgroundControls = new CheckBox(bundle.getString("CanvasControls.show"));
		cbShowBackgroundControls.setSelected(true);
		cbShowBackgroundControls.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
				for (ArmaControl control : canvasView.getEditingDisplay().getBackgroundControls()) {
					control.getRenderer().setGhost(!selected);
				}
			}
		});
		CheckBox cbShowControls = new CheckBox(bundle.getString("CanvasControls.show"));
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

		Function<EditorComponentTreeView<? extends TreeItemEntry>, Void> funcMoveUpHandler = treeView -> {
			TreeItem item = treeView.getSelectionModel().getSelectedItem();
			if (item == null) {
				//the button should be disabled when there is nothing selected
				throw new IllegalStateException("item shouldn't be null");
			}
			int index = item.getParent().getChildren().indexOf(item);
			int newIndex = Math.max(
					index - 1,
					0
			);
			if (index == newIndex) {
				return null;
			}
			treeView.moveTreeItem(item, item.getParent(), newIndex);

			//the selection index needs to be updated
			treeView.getSelectionModel().clearSelection();
			treeView.getSelectionModel().select(item);
			return null;
		};

		Function<EditorComponentTreeView<? extends TreeItemEntry>, Void> funcMoveDownHandler = treeView -> {
			TreeItem item = treeView.getSelectionModel().getSelectedItem();
			if (item == null) {
				//the button should be disabled when there is nothing selected
				throw new IllegalStateException("item shouldn't be null");
			}
			int index = item.getParent().getChildren().indexOf(item);
			int newIndex = Math.min(
					index + 1,
					item.getParent().getChildren().size() - 1
			);
			if (index == newIndex) {
				return null;
			}
			treeView.moveTreeItem(item, item.getParent(), newIndex);

			//the selection index needs to be updated
			treeView.getSelectionModel().clearSelection();
			treeView.getSelectionModel().select(item);
			return null;
		};

		//buttons for moving bg controls up/down hierarchy
		Button btnBgControlMoveUp, btnBgControlMoveDown;
		{
			btnBgControlMoveUp = new Button("", new ImageView(ADCImages.ICON_UP_ARROW));
			btnBgControlMoveUp.setOnAction(event -> {
				funcMoveUpHandler.apply(treeViewBg);
			});
			btnBgControlMoveUp.setDisable(treeViewBg.getSelectionModel().isEmpty());
			btnBgControlMoveUp.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_up")));

			btnBgControlMoveDown = new Button("", new ImageView(ADCImages.ICON_DOWN_ARROW));
			btnBgControlMoveDown.setOnAction(event -> {
				funcMoveDownHandler.apply(treeViewBg);
			});
			btnBgControlMoveDown.setDisable(treeViewBg.getSelectionModel().isEmpty());
			btnBgControlMoveDown.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_down")));

			treeViewBg.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				boolean disable = newValue == null;
				btnBgControlMoveDown.setDisable(disable);
				btnBgControlMoveUp.setDisable(disable);
			});
		}

		HBox bgControlsHbox = new HBox(10,
				new Label(bundle.getString("CanvasControls.background_controls")),
				new Separator(Orientation.VERTICAL),
				cbShowBackgroundControls,
				btnBgControlMoveUp,
				btnBgControlMoveDown
		);
		bgControlsHbox.setAlignment(Pos.CENTER_LEFT);
		VBox vbBgControls = new VBox(vertSpacing, bgControlsHbox, treeViewBg);
		bgControlsHbox.setPadding(new Insets(vertSpacing, vertSpacing, 0, vertSpacing));

		//buttons for moving controls up/down hierarchy
		Button btnControlMoveUp, btnControlMoveDown;
		{
			btnControlMoveUp = new Button("", new ImageView(ADCImages.ICON_UP_ARROW));
			btnControlMoveUp.setOnAction(event -> {
				funcMoveUpHandler.apply(treeViewMain);
			});
			btnControlMoveUp.setDisable(treeViewMain.getSelectionModel().isEmpty());
			btnControlMoveUp.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_up")));

			btnControlMoveDown = new Button("", new ImageView(ADCImages.ICON_DOWN_ARROW));
			btnControlMoveDown.setOnAction(event -> {
				funcMoveDownHandler.apply(treeViewMain);
			});
			btnControlMoveDown.setDisable(treeViewMain.getSelectionModel().isEmpty());
			btnControlMoveDown.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_down")));

			treeViewMain.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				boolean disable = newValue == null;
				btnControlMoveDown.setDisable(disable);
				btnControlMoveUp.setDisable(disable);
			});
		}


		HBox controlsHbox = new HBox(10,
				new Label(bundle.getString("CanvasControls.controls")),
				new Separator(Orientation.VERTICAL),
				cbShowControls,
				btnControlMoveUp,
				btnControlMoveDown
		);
		controlsHbox.setAlignment(Pos.CENTER_LEFT);
		VBox vbControls = new VBox(vertSpacing, controlsHbox, treeViewMain);
		controlsHbox.setPadding(bgControlsHbox.getPadding());


		VBox.setVgrow(treeViewBg, Priority.ALWAYS);
		VBox.setVgrow(treeViewMain, Priority.ALWAYS);

		SplitPane splitPane = new SplitPane(vbBgControls, vbControls);
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
