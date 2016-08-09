/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor.SnapConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.EditorComponentTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 @author Kayler
 Holds the step configuration contorls and the tree view for editor components.
 Created on 05/15/2016. */
class CanvasControls extends VBox implements SnapConfiguration {
	
	private final ADCCanvasView canvasView;
	private final EditorComponentTreeView<? extends TreeItemEntry> treeViewMain;
	private final EditorComponentTreeView<? extends TreeItemEntry> treeViewBg;
	private final ChoiceBox<Percentage> cbAltStep = new ChoiceBox<>();
	private final ChoiceBox<Percentage> cbStep = new ChoiceBox<>();
	
	public static final double PREFERRED_WIDTH = 250d;
	
	CanvasControls(ADCCanvasView canvasView) {
		super(5);
		this.canvasView = canvasView;
		treeViewMain = new EditorComponentTreeView<>(canvasView.getEditingDisplay(), false);
		treeViewBg = new EditorComponentTreeView<>(canvasView.getEditingDisplay(), true);
		initializeUI();
	}
	
	private void initializeUI() {
		initializeStepChoiceboxes();
		HBox hboxStep = new HBox(5);
		hboxStep.getChildren().addAll(new Label(Lang.CanvasControls.STEP), cbStep, new Label(Lang.CanvasControls.ALT_STEP), cbAltStep);
		
		final CheckBox cbShowBackgroundControls = new CheckBox(Lang.CanvasControls.SHOW);
		cbShowBackgroundControls.setSelected(true);
		cbShowBackgroundControls.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
				for(ArmaControl control : canvasView.getEditingDisplay().getBackgroundControls()){
					control.getRenderer().setGhost(!selected);
				}
			}
		});
		final CheckBox cbShowControls = new CheckBox(Lang.CanvasControls.SHOW);
		cbShowControls.setSelected(true);
		cbShowControls.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
				for(ArmaControl control : canvasView.getEditingDisplay().getControls()){
					control.getRenderer().setGhost(!selected);
				}
			}
		});
		
		
		final VBox vbBgControls = new VBox(5, new HBox(10, new Label(" "+Lang.CanvasControls.BACKGROUND_CONTROLS), cbShowBackgroundControls), treeViewBg); //placing space before label to give fake
		final VBox vbControls= new VBox(5, new HBox(10, new Label(" "+Lang.CanvasControls.CONTROLS), cbShowControls), treeViewMain);
		// padding
		VBox.setVgrow(treeViewBg, Priority.ALWAYS);
		VBox.setVgrow(treeViewMain, Priority.ALWAYS);
		
		final SplitPane splitPane = new SplitPane(vbBgControls, vbControls);
		splitPane.setOrientation(Orientation.VERTICAL);
		getChildren().addAll(hboxStep, splitPane);
		VBox.setVgrow(splitPane, Priority.ALWAYS);
		splitPane.setPrefHeight(getHeight());
		
		this.setPadding(new Insets(5, 5, 0, 5));
	}
	
	private void initializeStepChoiceboxes() {
		cbStep.getItems().addAll(new Percentage(1), new Percentage(2.50), new Percentage(5), new Percentage(10));
		cbAltStep.getItems().addAll(new Percentage(0.5));
		for (int i = 1; i <= 10; i++) {
			cbAltStep.getItems().add(new Percentage(i));
		}
		cbStep.getSelectionModel().select(1);
		cbAltStep.getSelectionModel().selectLast();
	}
	
	
	@Override
	public double alternateSnapPercentage() {
		return cbAltStep.getSelectionModel().getSelectedItem().percentDecimal;
	}
	
	@Override
	public double snapPercentage() {
		return cbStep.getSelectionModel().getSelectedItem().percentDecimal;
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
