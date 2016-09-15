/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file;

import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/20/2016.
 */
public class FileExportAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ExportProjectConfigurationDialog dialog = new ExportProjectConfigurationDialog();
		dialog.show();
		System.out.println("FileExportAction.handle dialog.getConfiguration()=" + dialog.getConfiguration());
	}

	private static class ExportProjectConfigurationDialog extends StageDialog<VBox> {

		private ProjectExportConfiguration configuration;

		private enum DisplayType {
			DIALOG(Lang.Popups.ExportProject.DisplayProperties.DIALOG), TITLE(Lang.Popups.ExportProject.DisplayProperties.TITLE);

			private final String displayName;

			DisplayType(String displayName) {
				this.displayName = displayName;
			}

			public static final DisplayType DEFAULT = DIALOG;
		}

		private DisplayType selectedDisplayType = DisplayType.DEFAULT;
		private final Insets padding10 = new Insets(10);

		public ExportProjectConfigurationDialog() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), Lang.Popups.ExportProject.DIALOG_TITLE, true, true, false);
			setStageSize(720, 480);
			myRootElement.setPadding(new Insets(10d));
			final Label lblTitle = new Label(Lang.Popups.ExportProject.TITLE_LABEL);
			lblTitle.setFont(Font.font(17));
			myRootElement.getChildren().add(lblTitle);
			myRootElement.getChildren().add(new Separator(Orientation.HORIZONTAL));
			final Tab tabDisplayProperties = new Tab(Lang.Popups.ExportProject.DISPLAY_PROPERTIES);
			tabDisplayProperties.setClosable(false);
			final Tab tabExportParameters = new Tab(Lang.Popups.ExportProject.EXPORT_PARAMETERS);
			tabExportParameters.setClosable(false);
			final Tab tabExportPreview = new Tab(Lang.Popups.ExportProject.EXPORT_PREVIEW);
			tabExportPreview.setClosable(false);

			final TabPane tabPane = new TabPane(tabDisplayProperties, tabExportParameters, tabExportPreview);
			VBox.setVgrow(tabPane, Priority.ALWAYS);

			myRootElement.getChildren().add(tabPane);

			initTabDisplayProperties(tabDisplayProperties);
			initTabExportParameters(tabExportParameters);
			initTabExportPreview(tabExportPreview);
		}

		private void initTabDisplayProperties(Tab tabDisplayProperties) {
			final VBox vbox = new VBox(10);
			vbox.setPadding(padding10);
			tabDisplayProperties.setContent(vbox);

			final Label lblClassName = new Label("");
			final InputField<IdentifierChecker, String> inputFieldClassName = new InputField<>(new IdentifierChecker());
			HBox.setHgrow(inputFieldClassName, Priority.ALWAYS);
			final HBox hboxClassName = new HBox(5, lblClassName, inputFieldClassName);
			vbox.getChildren().add(hboxClassName);

			final Label lblDisplayType = new Label(Lang.Popups.ExportProject.DisplayProperties.DISPLAY_TYPE);
			final ToggleGroup toggleGroup = new ToggleGroup();
			final FlowPane flowPaneDisplayType = new FlowPane(Orientation.HORIZONTAL, 5, 10);
			final HBox hboxDisplayType = new HBox(5, lblDisplayType, flowPaneDisplayType);
			toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					selectedDisplayType = (DisplayType) newValue.getUserData();
					lblClassName.setText(String.format(Lang.Popups.ExportProject.DisplayProperties.CLASS_NAME_F, selectedDisplayType.displayName));
				}
			});
			for (DisplayType type : DisplayType.values()) {
				final RadioButton radioButton = new RadioButton(type.displayName);
				radioButton.setUserData(type);
				radioButton.setToggleGroup(toggleGroup);
				if (type == selectedDisplayType) {
					radioButton.setSelected(true);
				}
				flowPaneDisplayType.getChildren().add(radioButton);
			}
			vbox.getChildren().add(hboxDisplayType);
		}

		private void initTabExportParameters(Tab tabExportParameters) {

		}

		private void initTabExportPreview(Tab tabExportPreview) {

		}

		@Nullable
		public ProjectExportConfiguration getConfiguration() {
			return configuration;
		}
	}
}
