package com.kaylerrenslow.armaDialogCreator.gui.main.popup.export;

import com.kaylerrenslow.armaDialogCreator.data.export.HeaderFileType;
import com.kaylerrenslow.armaDialogCreator.data.export.ProjectExportConfiguration;
import com.kaylerrenslow.armaDialogCreator.data.export.ProjectExporter;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.FileChooserPane;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.IdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImagePaths;
import com.kaylerrenslow.armaDialogCreator.gui.main.displayPropertiesEditor.DisplayPropertiesEditorPane;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.HelpUrls;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 A dialog used for editing a {@link ProjectExportConfiguration}

 @author Kayler
 @since 10/02/2016 */
public class EditExportConfigurationDialog extends StageDialog<VBox> {

	private final Insets padding10t = new Insets(10, 0, 10, 0);
	private final ProjectExportConfiguration configuration;

	/*export parameter things*/
	/** observer to detect when the macros are being exported to their own file, or being placed in the display's header file */
	private final ValueObserver<Boolean> exportMacrosToFileObserver = new ValueObserver<>(false);

	private final ResourceBundle bundle = Lang.ApplicationBundle();

	/**
	 Edits the given configuration's copy

	 @param configuration configuration to make a copy of and edit
	 @see ProjectExportConfiguration#copy()
	 */
	public EditExportConfigurationDialog(@NotNull ProjectExportConfiguration configuration) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), null, true, true, true);
		setTitle(bundle.getString("Popups.EditProjectExportConfig.dialog_title"));
		btnOk.setText(bundle.getString("Popups.EditProjectExportConfig.ok_button_export"));
		this.configuration = configuration.copy();

		setStageSize(720, 480);
		myRootElement.setPadding(new Insets(10d));

		Label lblTitle = new Label(bundle.getString("Popups.EditProjectExportConfig.title_label"));
		lblTitle.setFont(Font.font(17));
		myRootElement.getChildren().add(lblTitle);
		myRootElement.getChildren().add(new Separator(Orientation.HORIZONTAL));
		Tab tabDisplayProperties = new Tab(bundle.getString("Popups.EditProjectExportConfig.display_properties"));
		tabDisplayProperties.setClosable(false);
		Tab tabExportParameters = new Tab(bundle.getString("Popups.EditProjectExportConfig.export_parameters"));
		tabExportParameters.setClosable(false);
		Tab tabExportPreview = new Tab(bundle.getString("Popups.EditProjectExportConfig.export_preview"));
		tabExportPreview.setClosable(false);

		Tab tabCustomControlsExportPreview = new Tab(
				bundle.getString("Popups.EditProjectExportConfig.custom_classes_export_preview")
		);
		tabCustomControlsExportPreview.setClosable(false);

		TabPane tabPane = new TabPane(tabDisplayProperties, tabExportParameters, tabExportPreview, tabCustomControlsExportPreview);
		VBox.setVgrow(tabPane, Priority.ALWAYS);

		myRootElement.getChildren().add(tabPane);

		initTabDisplayProperties(tabDisplayProperties);
		initTabExportParameters(tabExportParameters);
		initTabExportPreview(tabExportPreview);
		initTabCustomClassesExportPreview(tabCustomControlsExportPreview);
	}

	/*
	*
	* TAB INIT: Display Properties
	*
	*/
	private void initTabDisplayProperties(Tab tab) {
		VBox tabRoot = new VBox(10);
		tabRoot.setPadding(padding10t);
		tab.setContent(tabRoot);

		/*class name*/
		InputField<IdentifierChecker, String> inputFieldClassName = new InputField<>(new IdentifierChecker(), configuration.getExportClassName());
		ValueObserver<String> classNameObserver = inputFieldClassName.getValueObserver();
		classNameObserver.addListener(new ValueListener<String>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
				configuration.setExportClassName(newValue);
			}
		});
		HBox.setHgrow(inputFieldClassName, Priority.ALWAYS);
		HBox hboxClassName = new HBox(5, new Label(bundle.getString("Popups.EditProjectExportConfig.DisplayProperties.class_name")), inputFieldClassName);
		tabRoot.getChildren().add(hboxClassName);

		/*display properties*/
		DisplayPropertiesEditorPane editorPane = new DisplayPropertiesEditorPane(configuration.getProject().getEditingDisplay());
		tabRoot.getChildren().add(editorPane);
	}

	/*
	*
	* TAB INIT: Export Parameters
	*
	*/
	private void initTabExportParameters(Tab tab) {
		VBox tabRoot = new VBox(30);
		tabRoot.setPadding(padding10t);
		ScrollPane scrollPane = new ScrollPane(tabRoot);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		tab.setContent(scrollPane);

		/*set export directory*/
		{
			Label lblExportDirectory = new Label(bundle.getString("Popups.EditProjectExportConfig.ExportParameters.export_directory"));
			FileChooserPane chooserPane = new FileChooserPane(ArmaDialogCreator.getPrimaryStage(), FileChooserPane.ChooserType.DIRECTORY,
					bundle.getString("Popups.EditProjectExportConfig.ExportParameters.locate_export_directory"), configuration.getExportDirectory());
			Tooltip.install(chooserPane, new Tooltip(bundle.getString("Popups.EditProjectExportConfig.ExportParameters.export_directory_tooltip")));
			chooserPane.setChosenFile(configuration.getExportDirectory());
			chooserPane.getChosenFileObserver().addListener(new ValueListener<File>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<File> observer, File oldValue, File newValue) {
					configuration.setExportDirectory(newValue);
				}
			});
			tabRoot.getChildren().add(new VBox(5, lblExportDirectory, chooserPane));
		}

		/*set custom classes export file name*/
		{
			Label lbl = new Label(bundle.getString("Popups.EditProjectExportConfig.ExportParameters.custom_classes_export_file_name"));
			TextField tf = new TextField(configuration.getCustomClassesExportFileName());
			tf.setTooltip(new Tooltip(bundle.getString("Popups.EditProjectExportConfig.ExportParameters.custom_classes_tooltip")));
			tf.textProperty().addListener((observable, oldValue, newValue) -> {
				newValue = newValue == null ? "" : newValue;
				configuration.setCustomControlClassesExportFile(newValue);
			});
			tabRoot.getChildren().add(new VBox(5, lbl, tf));
		}

		/*export macros to own file*/
		{
			CheckBox checkBoxExportMacrosToFile = new CheckBox(bundle.getString("Popups.EditProjectExportConfig.ExportParameters.export_macros_to_file"));
			checkBoxExportMacrosToFile.setTooltip(new Tooltip(bundle.getString("Popups.EditProjectExportConfig.ExportParameters.export_macros_to_file_tooltip")));
			checkBoxExportMacrosToFile.setSelected(configuration.shouldExportMacrosToFile());
			checkBoxExportMacrosToFile.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
					configuration.setExportMacrosToFile(selected);
					exportMacrosToFileObserver.updateValue(selected);
				}
			});
			tabRoot.getChildren().add(checkBoxExportMacrosToFile);
		}

		/*export file extension*/
		{
			ToggleGroup toggleGroupFileExt = new ToggleGroup();
			FlowPane flowPaneFileExt = new FlowPane(Orientation.HORIZONTAL, 5, 10);
			for (HeaderFileType headerFileType : HeaderFileType.values()) {
				RadioButton radioButtonFileExt = new RadioButton(headerFileType.getExtension());
				radioButtonFileExt.setToggleGroup(toggleGroupFileExt);
				if (headerFileType == configuration.getHeaderFileType()) {
					toggleGroupFileExt.selectToggle(radioButtonFileExt);
				}
				radioButtonFileExt.setUserData(headerFileType);
				flowPaneFileExt.getChildren().add(radioButtonFileExt);
			}
			toggleGroupFileExt.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
					configuration.setFileType((HeaderFileType) newValue.getUserData());
				}
			});
			tabRoot.getChildren().add(new VBox(5, new Label(bundle.getString("Popups.EditProjectExportConfig.ExportParameters.export_file_extension")), flowPaneFileExt));
		}


		/*place adc notice*/
		{
			CheckBox checkBoxPlaceAdcNotice = new CheckBox(bundle.getString("Popups.EditProjectExportConfig.ExportParameters.place_adc_notice"));
			checkBoxPlaceAdcNotice.setSelected(configuration.shouldPlaceAdcNotice());
			checkBoxPlaceAdcNotice.setTooltip(new Tooltip(bundle.getString("Popups.EditProjectExportConfig.ExportParameters.place_adc_notice_tooltip")));
			checkBoxPlaceAdcNotice.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					configuration.setPlaceAdcNotice(newValue);
				}
			});
			tabRoot.getChildren().add(new HBox(2, checkBoxPlaceAdcNotice, new ImageView(ADCImagePaths.ICON_HEART)));
		}
	}

	/*
	*
	* TAB INIT: Export Preview
	*
	*/
	@SuppressWarnings("unchecked")
	private void initTabExportPreview(Tab tab) {
		StackPane tabRoot = new StackPane();
		tab.setContent(tabRoot);

		SplitPane splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.HORIZONTAL);
		tabRoot.getChildren().add(splitPane);

		VBox vboxDisplayPreview = new VBox(5);
		Label lblDisplayFileExportPreview = new Label("");
		TextArea textAreaDisplay = new TextArea();
		textAreaDisplay.setEditable(false);
		vboxDisplayPreview.getChildren().add(lblDisplayFileExportPreview);
		vboxDisplayPreview.getChildren().add(textAreaDisplay);
		VBox.setVgrow(textAreaDisplay, Priority.ALWAYS);

		VBox vboxMacrosPreview = new VBox(5);
		Label lblMacrosFileExportPreview = new Label("");
		TextArea textAreaMacros = new TextArea();
		textAreaMacros.setEditable(false);

		vboxMacrosPreview.getChildren().add(lblMacrosFileExportPreview);
		vboxMacrosPreview.getChildren().add(textAreaMacros);
		VBox.setVgrow(textAreaMacros, Priority.ALWAYS);


		exportMacrosToFileObserver.addListener(new ValueListener<Boolean>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean export) {
				if (export) {
					splitPane.getItems().add(vboxMacrosPreview);
				} else {
					splitPane.getItems().remove(vboxMacrosPreview);
				}
			}
		});
		tab.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {
				if (!selected) {
					return;
				}
				lblDisplayFileExportPreview.setText(ProjectExporter.getDisplayFileName(configuration));
				lblMacrosFileExportPreview.setText(ProjectExporter.getMacrosFileName(configuration));

				ByteArrayOutputStream outDisplay = new ByteArrayOutputStream();
				ByteArrayOutputStream outMacros = new ByteArrayOutputStream();
				try {
					ProjectExporter.exportDisplayAndMacros(configuration, outDisplay, outMacros);
				} catch (Exception e) {
					textAreaDisplay.setText("Could not export: " + e.getMessage());
					ExceptionHandler.error(e);
				}
				textAreaDisplay.setText(new String(outDisplay.toByteArray()));
				textAreaMacros.setText(new String(outMacros.toByteArray()));
			}
		});

		splitPane.getItems().add(vboxDisplayPreview);
		if (configuration.shouldExportMacrosToFile()) {
			splitPane.getItems().add(vboxMacrosPreview);
		}
	}

	/*
	*
	* TAB INIT: Custom Classes Export Preview
	*
	*/
	private void initTabCustomClassesExportPreview(Tab tab) {
		VBox tabRoot = new VBox(10);
		tabRoot.setPadding(padding10t);
		tab.setContent(tabRoot);

		Label lblFileName = new Label("");

		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		VBox.setVgrow(textArea, Priority.ALWAYS);

		tab.selectedProperty().addListener((observable, oldValue, selected) -> {
			if (!selected) {
				return;
			}
			lblFileName.setText(configuration.getCustomClassesExportFileName());
			try {
				ByteArrayOutputStream stm = new ByteArrayOutputStream();
				ProjectExporter.exportWorkspaceCustomControls(configuration, stm);

				textArea.setText(new String(stm.toByteArray()));
			} catch (IOException e) {
				textArea.setText("Couldn't export custom controls.");
				ExceptionHandler.error(e);
			}
		});

		tabRoot.getChildren().addAll(lblFileName, textArea);
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.EXPORT);
	}

	/** @return the new configuration, or null if was cancelled */
	@Nullable
	public ProjectExportConfiguration getConfiguration() {
		return wasCancelled() ? null : configuration;
	}
}
