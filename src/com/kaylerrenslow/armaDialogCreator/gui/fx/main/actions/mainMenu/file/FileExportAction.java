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

import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExporter;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 Created by Kayler on 05/20/2016.
 */
public class FileExportAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		ExportProjectConfigurationDialog dialog = new ExportProjectConfigurationDialog(ApplicationDataManager.getInstance().getCurrentProject());
		dialog.show();
		System.out.println("FileExportAction.handle dialog.getConfiguration()=" + dialog.getConfiguration());
	}

	private static class ExportProjectConfigurationDialog extends StageDialog<VBox> {

		private enum DisplayType {
			DIALOG(Lang.Popups.ExportProject.DisplayProperties.DIALOG), TITLE(Lang.Popups.ExportProject.DisplayProperties.TITLE);

			private final String displayName;

			DisplayType(String displayName) {
				this.displayName = displayName;
			}

			public static final DisplayType DEFAULT = DIALOG;

		}
		private boolean cancel = false;
		private DisplayType selectedDisplayType = DisplayType.DEFAULT;

		private final Insets padding10 = new Insets(10);
		private final ProjectExportConfiguration configuration;
		private final UpdateListenerGroup<Object> updatePreviewGroup = new UpdateListenerGroup<>();

		private final ValueObserver<File> exportDirectoryObserver = new ValueObserver<>(ApplicationDataManager.getInstance().getAppSaveDataDirectory());
		private final ValueObserver<Boolean> exportMacrosToFileObserver = new ValueObserver<>(false);
		private ValueObserver<String> classNameObserver;

		public ExportProjectConfigurationDialog(@NotNull Project project) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), Lang.Popups.ExportProject.DIALOG_TITLE, true, true, false);
			configuration = new ProjectExportConfiguration("", exportDirectoryObserver.getValue(), project, false);
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

			classNameObserver.addValueListener(new ValueListener<String>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
					updatePreviewGroup.update("");
				}
			});
			exportMacrosToFileObserver.addValueListener(new ValueListener<Boolean>() {
				@Override
				public void valueUpdated(ValueObserver<Boolean> observer, Boolean oldValue, Boolean newValue) {
					updatePreviewGroup.update("");
				}
			});

			updatePreviewGroup.update("");
		}

		private void initTabDisplayProperties(Tab tabDisplayProperties) {
			final VBox tabRoot = new VBox(10);
			tabRoot.setPadding(padding10);
			tabDisplayProperties.setContent(tabRoot);

			final Label lblClassName = new Label("");
			final InputField<IdentifierChecker, String> inputFieldClassName = new InputField<>(new IdentifierChecker());
			classNameObserver = inputFieldClassName.getValueObserver();
			classNameObserver.addValueListener(new ValueListener<String>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
					configuration.setExportClassName(newValue);
				}
			});
			HBox.setHgrow(inputFieldClassName, Priority.ALWAYS);
			final HBox hboxClassName = new HBox(5, lblClassName, inputFieldClassName);
			tabRoot.getChildren().add(hboxClassName);

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
			tabRoot.getChildren().add(hboxDisplayType);
		}

		private void initTabExportParameters(Tab tabExportParameters) {

		}

		private void initTabExportPreview(Tab tabExportPreview) {
			final StackPane tabRoot = new StackPane();
			tabExportPreview.setContent(tabRoot);

			final SplitPane splitPane = new SplitPane();
			splitPane.setOrientation(Orientation.HORIZONTAL);
			tabRoot.getChildren().add(splitPane);

			final VBox vboxDisplayPreview = new VBox(5);
			final Label lblDisplayExportPreview = new Label(String.format(Lang.Popups.ExportProject.DISPLAY_FILE_F, ""));
			final TextArea textAreaDisplay = new TextArea();
			textAreaDisplay.setEditable(false);
			vboxDisplayPreview.getChildren().add(textAreaDisplay);
			vboxDisplayPreview.getChildren().add(lblDisplayExportPreview);
			VBox.setVgrow(textAreaDisplay, Priority.ALWAYS);

			final VBox vboxMacrosPreview = new VBox(5);
			final Label lblMacrosExportPreview = new Label(String.format(Lang.Popups.ExportProject.MACROS_FILE, ""));
			final TextArea textAreaMacros = new TextArea();
			textAreaMacros.setEditable(false);
			vboxMacrosPreview.getChildren().add(textAreaMacros);
			vboxMacrosPreview.getChildren().add(lblMacrosExportPreview);
			VBox.setVgrow(textAreaMacros, Priority.ALWAYS);

			classNameObserver.addValueListener(new ValueListener<String>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
					lblDisplayExportPreview.setText(String.format(Lang.Popups.ExportProject.DISPLAY_FILE_F, newValue));
					lblMacrosExportPreview.setText(String.format(Lang.Popups.ExportProject.MACROS_FILE, newValue));
				}
			});

			exportMacrosToFileObserver.addValueListener(new ValueListener<Boolean>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean export) {
					vboxMacrosPreview.setDisable(!export);
				}
			});


			updatePreviewGroup.addListener(new UpdateListener<Object>() {
				@Override
				public void update(Object data) {
					final ByteArrayOutputStream outDisplay = new ByteArrayOutputStream();
					final ByteArrayOutputStream outMacros = new ByteArrayOutputStream();
					try {
						ProjectExporter.export(configuration, outDisplay, outMacros);
					} catch (Exception e) {
						textAreaDisplay.setText("Could not export: " + e.getMessage());
						ExceptionHandler.error(e);
					}
					textAreaDisplay.setText(new String(outDisplay.toByteArray()));
					textAreaMacros.setText(new String(outMacros.toByteArray()));
					try {
						outDisplay.close();
						outMacros.close();
					} catch (Exception e) {
						ExceptionHandler.error(e);
					}
				}
			});

			splitPane.getItems().add(vboxDisplayPreview);
			splitPane.getItems().add(vboxMacrosPreview);
		}

		@Override
		protected void cancel() {
			super.cancel();
			this.cancel = true;
		}

		@Nullable
		public ProjectExportConfiguration getConfiguration() {
			return cancel ? null : configuration;
		}
	}
}
