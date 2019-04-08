package com.armadialogcreator.gui.main;

import com.armadialogcreator.canvas.UICanvasConfiguration;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.data.EditorManager;
import com.armadialogcreator.gui.fxcontrol.DownArrowMenu;
import com.armadialogcreator.gui.main.treeview.EditorComponentTreeView;
import com.armadialogcreator.gui.main.treeview.UINodeTreeItemData;
import com.armadialogcreator.img.icons.ADCIcons;
import com.armadialogcreator.lang.Lang;
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
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 Holds the step configuration controls and the tree view for editor components.

 @author Kayler
 @since 05/15/2016. */
class CanvasControls extends VBox implements UICanvasConfiguration {

	private final ResourceBundle bundle = Lang.ApplicationBundle();
	private final ADCCanvasView canvasView;
	private final EditorComponentTreeView treeViewMain;
	private final EditorComponentTreeView treeViewBg;
	private final ChoiceBox<Percentage> choiceBoxAltStep = new ChoiceBox<>();
	private final ChoiceBox<Percentage> choiceBoxStep = new ChoiceBox<>();
	private final CheckBox checkBoxViewportSnapping = new CheckBox(bundle.getString("CanvasControls.viewport_snapping"));

	public static final double PREFERRED_WIDTH = 320d;
	private boolean showGrid = true;

	CanvasControls(ADCCanvasView canvasView) {
		super(5);
		this.canvasView = canvasView;
		treeViewMain = new EditorComponentTreeView(UINode.EMPTY);
		treeViewBg = new EditorComponentTreeView(UINode.EMPTY);
		initializeUI();
	}

	@SuppressWarnings("unchecked")
	private void initializeUI() {
		initializeStepChoiceboxes();
		FlowPane flowPaneStep = new FlowPane(5, 5,
				new HBox(5, new Label(bundle.getString("CanvasControls.step")), choiceBoxStep),
				new HBox(5, new Label(bundle.getString("CanvasControls.alt_step")), choiceBoxAltStep),
				checkBoxViewportSnapping
		);

		checkBoxViewportSnapping.selectedProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				canvasView.repaintCanvas();
			}
		});

		CheckBox cbShowBackgroundControls = new CheckBox(bundle.getString("CanvasControls.show"));
		cbShowBackgroundControls.setSelected(true);
		cbShowBackgroundControls.selectedProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
				ArmaDisplay display = EditorManager.instance.getEditingDisplay();
				for (UINode node : display.getBackgroundControlNodes().deepIterateChildren()) {
					if (node instanceof ArmaControl) {
						ArmaControl control = (ArmaControl) node;
						control.getRenderer().setGhost(!selected);
					}
				}
			}
		});
		CheckBox cbShowControls = new CheckBox(bundle.getString("CanvasControls.show"));
		cbShowControls.setSelected(true);
		cbShowControls.selectedProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
				ArmaDisplay display = EditorManager.instance.getEditingDisplay();
				for (UINode node : display.getControlNodes().deepIterateChildren()) {
					if (node instanceof ArmaControl) {
						ArmaControl control = (ArmaControl) node;
						control.getRenderer().setGhost(!selected);
					}
				}
			}
		});

		final double vertSpacing = 5;

		Consumer<EditorComponentTreeView> funcMoveUpHandler = treeView -> {
			TreeItem<UINodeTreeItemData> item = treeView.getSelectionModel().getSelectedItem();
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
				return;
			}
			treeView.moveTreeItem(item, item.getParent(), newIndex);
			UINodeTreeItemData uiNodeTreeItemData = item.getValue();
			UINodeTreeItemData parentUiNodeTreeItemData = item.getParent().getValue();
			if (uiNodeTreeItemData == null || parentUiNodeTreeItemData == null) {
				throw new IllegalStateException();
			}
			UINode node = uiNodeTreeItemData.getNode();
			UINode parentNode = parentUiNodeTreeItemData.getNode();
			parentNode.moveChild(node, parentNode, newIndex);

			//the selection index needs to be updated
			treeView.getSelectionModel().clearSelection();
			treeView.getSelectionModel().select(item);
			return;
		};

		Consumer<EditorComponentTreeView> funcMoveDownHandler = treeView -> {
			TreeItem<UINodeTreeItemData> item = treeView.getSelectionModel().getSelectedItem();
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
				return;
			}
			treeView.moveTreeItem(item, item.getParent(), newIndex);

			treeView.moveTreeItem(item, item.getParent(), newIndex);
			UINodeTreeItemData uiNodeTreeItemData = item.getValue();
			UINodeTreeItemData parentUiNodeTreeItemData = item.getParent().getValue();
			if (uiNodeTreeItemData == null || parentUiNodeTreeItemData == null) {
				throw new IllegalStateException();
			}
			UINode node = uiNodeTreeItemData.getNode();
			UINode parentNode = parentUiNodeTreeItemData.getNode();
			parentNode.moveChild(node, parentNode, newIndex);

			//the selection index needs to be updated
			treeView.getSelectionModel().clearSelection();
			treeView.getSelectionModel().select(item);
			return;
		};

		Consumer<EditorComponentTreeView> funcMoveDownIntoHandler = treeView -> {
			TreeItem<UINodeTreeItemData> item = treeView.getSelectionModel().getSelectedItem();
			if (item == null) {
				//the button should be disabled when there is nothing selected
				throw new IllegalStateException("item shouldn't be null");
			}
			TreeItem<UINodeTreeItemData> nextSibling = item.nextSibling();
			if (nextSibling == null) {
				return;
			}
			UINodeTreeItemData siblingUiNodeTreeItemData = nextSibling.getValue();
			if (siblingUiNodeTreeItemData == null) {
				return;
			}
			UINode siblingNode = siblingUiNodeTreeItemData.getNode();
			if (!siblingNode.canHaveChildren()) {
				return;
			}
			treeView.moveTreeItem(item, nextSibling, 0);

			UINodeTreeItemData uiNodeTreeItemData = item.getValue();
			if (uiNodeTreeItemData == null) {
				throw new IllegalStateException();
			}
			UINode node = uiNodeTreeItemData.getNode();
			UINode parentNode = node.getParentNode();
			if (parentNode == null) {
				throw new IllegalStateException();
			}
			parentNode.moveChild(node, siblingNode, 0);

			//the selection index needs to be updated
			treeView.getSelectionModel().clearSelection();
			treeView.getSelectionModel().select(item);
			return;
		};

		Consumer<EditorComponentTreeView> funcMoveUpIntoHandler = treeView -> {
			TreeItem<UINodeTreeItemData> item = treeView.getSelectionModel().getSelectedItem();
			if (item == null) {
				//the button should be disabled when there is nothing selected
				throw new IllegalStateException("item shouldn't be null");
			}
			TreeItem<UINodeTreeItemData> prevSibling = item.previousSibling();
			if (prevSibling == null) {
				return;
			}
			UINodeTreeItemData siblingUiNodeTreeItemData = prevSibling.getValue();
			if (siblingUiNodeTreeItemData == null) {
				return;
			}
			UINode siblingNode = siblingUiNodeTreeItemData.getNode();
			if (!siblingNode.canHaveChildren()) {
				return;
			}
			final int newIndex = prevSibling.getChildren().size();
			treeView.moveTreeItem(item, prevSibling, newIndex);

			UINodeTreeItemData uiNodeTreeItemData = item.getValue();
			if (uiNodeTreeItemData == null) {
				throw new IllegalStateException();
			}
			UINode node = uiNodeTreeItemData.getNode();
			UINode parentNode = siblingUiNodeTreeItemData.getNode();
			parentNode.moveChild(node, siblingNode, newIndex);

			//the selection index needs to be updated
			treeView.getSelectionModel().clearSelection();
			treeView.getSelectionModel().select(item);
			return;
		};

		Consumer<EditorComponentTreeView> funcMoveDownOutHandler = treeView -> {
			TreeItem<UINodeTreeItemData> item = treeView.getSelectionModel().getSelectedItem();
			if (item == null) {
				//the button should be disabled when there is nothing selected
				throw new IllegalStateException("item shouldn't be null");
			}
			if (item.getParent() == treeView.getRoot()) {
				return;
			}
			TreeItem<UINodeTreeItemData> newParent = item.getParent().getParent();
			UINodeTreeItemData newParentUINodeTreeItemData = newParent.getValue();
			if (newParentUINodeTreeItemData == null) {
				throw new IllegalStateException();
			}
			UINode newParentNode = newParentUINodeTreeItemData.getNode();
			if (newParent == treeView.getRoot() || newParentNode.canHaveChildren()) {
				final int newIndex = newParent.getChildren().size();
				treeView.moveTreeItem(item, newParent, newIndex);

				UINodeTreeItemData uiNodeTreeItemData = item.getValue();
				if (uiNodeTreeItemData == null) {
					throw new IllegalStateException();
				}
				UINode node = uiNodeTreeItemData.getNode();
				UINode parentNode = node.getParentNode();
				if (parentNode == null) {
					throw new IllegalStateException();
				}
				parentNode.moveChild(node, newParentNode, newIndex);

				//the selection index needs to be updated
				treeView.getSelectionModel().clearSelection();
				treeView.getSelectionModel().select(item);
			}

			return;
		};

		Consumer<EditorComponentTreeView> funcMoveUpOutHandler = treeView -> {
			TreeItem<UINodeTreeItemData> item = treeView.getSelectionModel().getSelectedItem();
			if (item == null) {
				//the button should be disabled when there is nothing selected
				throw new IllegalStateException("item shouldn't be null");
			}
			if (item.getParent() == treeView.getRoot()) {
				return;
			}
			TreeItem<UINodeTreeItemData> newParent = item.getParent().getParent();
			UINodeTreeItemData newParentUINodeTreeItemData = newParent.getValue();
			if (newParentUINodeTreeItemData == null) {
				throw new IllegalStateException();
			}
			UINode newParentNode = newParentUINodeTreeItemData.getNode();
			if (newParent == treeView.getRoot() || newParentNode.canHaveChildren()) {
				treeView.moveTreeItem(item, newParent, 0);

				UINodeTreeItemData uiNodeTreeItemData = item.getValue();
				if (uiNodeTreeItemData == null) {
					throw new IllegalStateException();
				}
				UINode node = uiNodeTreeItemData.getNode();
				UINode parentNode = node.getParentNode();
				if (parentNode == null) {
					throw new IllegalStateException();
				}
				parentNode.moveChild(node, newParentNode, 0);

				//the selection index needs to be updated
				treeView.getSelectionModel().clearSelection();
				treeView.getSelectionModel().select(item);
			}

			return;
		};

		//buttons for moving bg controls up/down hierarchy
		Button btnBgControlMoveUp, btnBgControlMoveDown, btnBgControlMoveUpInto, btnBgControlMoveDownInto, btnBgControlMoveUpOut, btnBgControlMoveDownOut;
		{
			btnBgControlMoveUp = new Button("", new ImageView(ADCIcons.ICON_UP_ARROW));
			btnBgControlMoveUp.setOnAction(event -> {
				funcMoveUpHandler.accept(treeViewBg);
			});
			btnBgControlMoveUp.setDisable(treeViewBg.getSelectionModel().isEmpty());
			btnBgControlMoveUp.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_up")));

			btnBgControlMoveDown = new Button("", new ImageView(ADCIcons.ICON_DOWN_ARROW));
			btnBgControlMoveDown.setOnAction(event -> {
				funcMoveDownHandler.accept(treeViewBg);
			});
			btnBgControlMoveDown.setDisable(treeViewBg.getSelectionModel().isEmpty());
			btnBgControlMoveDown.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_down")));


			btnBgControlMoveUpInto = new Button("", new ImageView(ADCIcons.ICON_UP_ARROW_INTO));
			btnBgControlMoveUpInto.setOnAction(event -> {
				funcMoveUpIntoHandler.accept(treeViewBg);
			});
			btnBgControlMoveUpInto.setDisable(treeViewBg.getSelectionModel().isEmpty());
			btnBgControlMoveUpInto.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_up_into")));

			btnBgControlMoveDownInto = new Button("", new ImageView(ADCIcons.ICON_DOWN_ARROW_INTO));
			btnBgControlMoveDownInto.setOnAction(event -> {
				funcMoveDownIntoHandler.accept(treeViewBg);
			});
			btnBgControlMoveDownInto.setDisable(treeViewBg.getSelectionModel().isEmpty());
			btnBgControlMoveDownInto.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_down_into")));


			btnBgControlMoveUpOut = new Button("", new ImageView(ADCIcons.ICON_UP_ARROW_OUT));
			btnBgControlMoveUpOut.setOnAction(event -> {
				funcMoveUpOutHandler.accept(treeViewBg);
			});
			btnBgControlMoveUpOut.setDisable(treeViewBg.getSelectionModel().isEmpty());
			btnBgControlMoveUpOut.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_up_out")));

			btnBgControlMoveDownOut = new Button("", new ImageView(ADCIcons.ICON_DOWN_ARROW_OUT));
			btnBgControlMoveDownOut.setOnAction(event -> {
				funcMoveDownOutHandler.accept(treeViewBg);
			});
			btnBgControlMoveDownOut.setDisable(treeViewBg.getSelectionModel().isEmpty());
			btnBgControlMoveDownOut.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_down_out")));

			treeViewBg.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				boolean disable = newValue == null;
				btnBgControlMoveDown.setDisable(disable);
				btnBgControlMoveUp.setDisable(disable);
				btnBgControlMoveDownInto.setDisable(disable);
				btnBgControlMoveUpInto.setDisable(disable);
				btnBgControlMoveDownOut.setDisable(disable);
				btnBgControlMoveUpOut.setDisable(disable);
			});
		}

		FlowPane bgControlsPane = new FlowPane(10, 10,
				new Label(bundle.getString("CanvasControls.background_controls")),
				new Separator(Orientation.VERTICAL),
				cbShowBackgroundControls,
				btnBgControlMoveUp,
				btnBgControlMoveDown,
				new DownArrowMenu(
						new CustomMenuItem(btnBgControlMoveUpInto, false),
						new CustomMenuItem(btnBgControlMoveUpOut, false),
						new SeparatorMenuItem(),
						new CustomMenuItem(btnBgControlMoveDownInto, false),
						new CustomMenuItem(btnBgControlMoveDownOut, false)
				)
		);
		bgControlsPane.setAlignment(Pos.CENTER_LEFT);
		VBox vbBgControls = new VBox(vertSpacing, bgControlsPane, treeViewBg);
		bgControlsPane.setPadding(new Insets(vertSpacing, vertSpacing, 0, vertSpacing));

		//buttons for moving controls up/down hierarchy
		Button btnControlMoveUp, btnControlMoveDown, btnControlMoveUpInto, btnControlMoveDownInto, btnControlMoveUpOut, btnControlMoveDownOut;
		{
			btnControlMoveUp = new Button("", new ImageView(ADCIcons.ICON_UP_ARROW));
			btnControlMoveUp.setOnAction(event -> {
				funcMoveUpHandler.accept(treeViewMain);
			});
			btnControlMoveUp.setDisable(treeViewMain.getSelectionModel().isEmpty());
			btnControlMoveUp.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_up")));

			btnControlMoveDown = new Button("", new ImageView(ADCIcons.ICON_DOWN_ARROW));
			btnControlMoveDown.setOnAction(event -> {
				funcMoveDownHandler.accept(treeViewMain);
			});
			btnControlMoveDown.setDisable(treeViewMain.getSelectionModel().isEmpty());
			btnControlMoveDown.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_down")));


			btnControlMoveUpInto = new Button("", new ImageView(ADCIcons.ICON_UP_ARROW_INTO));
			btnControlMoveUpInto.setOnAction(event -> {
				funcMoveUpIntoHandler.accept(treeViewMain);
			});
			btnControlMoveUpInto.setDisable(treeViewMain.getSelectionModel().isEmpty());
			btnControlMoveUpInto.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_up_into")));

			btnControlMoveDownInto = new Button("", new ImageView(ADCIcons.ICON_DOWN_ARROW_INTO));
			btnControlMoveDownInto.setOnAction(event -> {
				funcMoveDownIntoHandler.accept(treeViewMain);
			});
			btnControlMoveDownInto.setDisable(treeViewMain.getSelectionModel().isEmpty());
			btnControlMoveDownInto.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_down_into")));


			btnControlMoveUpOut = new Button("", new ImageView(ADCIcons.ICON_UP_ARROW_OUT));
			btnControlMoveUpOut.setOnAction(event -> {
				funcMoveUpOutHandler.accept(treeViewMain);
			});
			btnControlMoveUpOut.setDisable(treeViewMain.getSelectionModel().isEmpty());
			btnControlMoveUpOut.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_up_out")));

			btnControlMoveDownOut = new Button("", new ImageView(ADCIcons.ICON_DOWN_ARROW_OUT));
			btnControlMoveDownOut.setOnAction(event -> {
				funcMoveDownOutHandler.accept(treeViewMain);
			});
			btnControlMoveDownOut.setDisable(treeViewMain.getSelectionModel().isEmpty());
			btnControlMoveDownOut.setTooltip(new Tooltip(bundle.getString("CanvasControls.move_down_out")));

			treeViewMain.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				boolean disable = newValue == null;
				btnControlMoveDown.setDisable(disable);
				btnControlMoveUp.setDisable(disable);
				btnControlMoveDownInto.setDisable(disable);
				btnControlMoveUpInto.setDisable(disable);
				btnControlMoveDownOut.setDisable(disable);
				btnControlMoveUpOut.setDisable(disable);
			});
		}


		FlowPane controlsPane = new FlowPane(10, 10,
				new Label(bundle.getString("CanvasControls.controls")),
				new Separator(Orientation.VERTICAL),
				cbShowControls,
				btnControlMoveUp,
				btnControlMoveDown,
				new DownArrowMenu(
						new CustomMenuItem(btnControlMoveUpInto, false),
						new CustomMenuItem(btnControlMoveUpOut, false),
						new SeparatorMenuItem(),
						new CustomMenuItem(btnControlMoveDownInto, false),
						new CustomMenuItem(btnControlMoveDownOut, false)
				)
		);
		controlsPane.setAlignment(Pos.CENTER_LEFT);
		VBox vbControls = new VBox(vertSpacing, controlsPane, treeViewMain);
		controlsPane.setPadding(bgControlsPane.getPadding());


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
		ChangeListener<? super Percentage> selectUpdate = new ChangeListener<>() {
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
	@NotNull
	public EditorComponentTreeView getTreeViewMain() {
		return treeViewMain;
	}

	/** Get the tree view used for storing controls that is meant <b>for</b> background controls */
	@NotNull
	public EditorComponentTreeView getTreeViewBackground() {
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
