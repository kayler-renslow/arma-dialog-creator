/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.HelpUrls;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author Kayler
 Used for displaying known macros of a given type and allowing the user to choose which macro they want.
 Created on 08/09/2016. */
public class ChooseMacroPopup<V extends SerializableValue> extends StagePopup<VBox> {
	
	private static final Font TITLE_FONT = Font.font(15d);
	private final ListView<Macro<V>> listViewMacros = new ListView<>();
	
	public ChooseMacroPopup(@NotNull Class<V> macroClassType) {
		super(ArmaDialogCreator.getPrimaryStage(), new Stage(), new VBox(5), Lang.Popups.ChooseMacro.POPUP_TITLE);
		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.initStyle(StageStyle.UTILITY);
		
		initRootElement(macroClassType);
		myStage.setResizable(false);
	}
	
	private void initRootElement(Class<V> macroClassType) {
		myRootElement.setPadding(new Insets(10));
		final Label lblChooseMacro = new Label(Lang.Popups.ChooseMacro.CHOOSE_MACRO_TITLE);
		lblChooseMacro.setFont(TITLE_FONT);
		myRootElement.getChildren().add(lblChooseMacro);
		myRootElement.getChildren().add(new Separator(Orientation.HORIZONTAL));
		
		List<Macro> macroList = ArmaDialogCreator.getApplicationData().getCurrentProject().getMacroRegistry().getMacros();
		for (Macro macro : macroList) {
			if (macroClassType.isInstance(macro.getValue())) {
				listViewMacros.getItems().add(macro);
			}
		}
		if (listViewMacros.getItems().size() == 0) {
			listViewMacros.setDisable(true);
			Label lblMessage = new Label(Lang.Popups.ChooseMacro.NO_AVAILABLE_MACROS);
			myRootElement.getChildren().addAll(new Label(Lang.Popups.ChooseMacro.AVAILABLE_MACROS), lblMessage);
		} else {
			HBox hbSplit = new HBox(5);
			Label lblListViewMacros = new Label(Lang.Popups.ChooseMacro.AVAILABLE_MACROS, listViewMacros);
			lblListViewMacros.setContentDisplay(ContentDisplay.BOTTOM);
			
			final double height = 100;
			VBox vbRight = new VBox(10);
			TextArea taComment = new TextArea();
			taComment.setPrefHeight(height);
			taComment.setEditable(false);
			Label lblComment = new Label(Lang.Macros.COMMENT, taComment);
			lblComment.setContentDisplay(ContentDisplay.BOTTOM);
			TextArea taValue = new TextArea();
			taValue.setEditable(false);
			taValue.setPrefHeight(height);
			Label lblValue = new Label(Lang.Macros.VALUE, taValue);
			lblValue.setContentDisplay(ContentDisplay.BOTTOM);
			
			vbRight.getChildren().addAll(lblValue, lblComment);
			
			hbSplit.getChildren().addAll(lblListViewMacros, vbRight);
			listViewMacros.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Macro>() {
				@Override
				public void changed(ObservableValue<? extends Macro> observable, Macro oldValue, Macro selected) {
					if (selected != null) {
						taComment.setText(selected.getComment());
						taValue.setText(selected.getValue().toString());
					}
				}
			});
			myRootElement.getChildren().addAll(hbSplit);
		}
		myStage.sizeToScene();
		myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(true, true, true));
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.MACROS);
	}

	@Override
	protected void cancel() {
		listViewMacros.getSelectionModel().clearSelection();
		close();
	}
	
	/** Return the macro chosen. If null, no macro was chosen. */
	@Nullable
	public Macro<V> getChosenMacro() {
		return listViewMacros.getSelectionModel().getSelectedItem();
	}
	
	@Override
	public void show() {
		showAndWait();
	}
}
