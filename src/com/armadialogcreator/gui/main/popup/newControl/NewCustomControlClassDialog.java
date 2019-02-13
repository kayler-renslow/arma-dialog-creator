package com.armadialogcreator.gui.main.popup.newControl;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.ExceptionHandler;
import com.armadialogcreator.HelpUrls;
import com.armadialogcreator.control.impl.ArmaControlLookup;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.core.old.ControlClassOld;
import com.armadialogcreator.core.old.ControlPropertyUpdate;
import com.armadialogcreator.core.old.CustomControlClass;
import com.armadialogcreator.data.export.ProjectExporter;
import com.armadialogcreator.gui.SimpleResponseDialog;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.fxcontrol.BorderedImageView;
import com.armadialogcreator.gui.fxcontrol.CBMBGroupMenu;
import com.armadialogcreator.gui.fxcontrol.CBMBMenuItem;
import com.armadialogcreator.gui.fxcontrol.ComboBoxMenuButton;
import com.armadialogcreator.gui.fxcontrol.inputfield.IdentifierChecker;
import com.armadialogcreator.gui.fxcontrol.inputfield.InputField;
import com.armadialogcreator.gui.main.BrowserUtil;
import com.armadialogcreator.gui.main.controlPropertiesEditor.ControlPropertiesEditorPane;
import com.armadialogcreator.gui.main.fxControls.ControlClassMenuButton;
import com.armadialogcreator.gui.main.fxControls.ControlClassMenuItem;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import com.armadialogcreator.util.ValueListener;
import com.armadialogcreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 Dialog window that allows for creating a new custom control ({@link CustomControlClass}).
 It has a control properties editor on the left and a preview window on the right to preview the outputted .h file text

 @author Kayler
 @since 07/06/2016. */
public class NewCustomControlClassDialog extends StageDialog<VBox> {

	private final StackPane stackPaneProperties = new StackPane();
	private final TextArea taPreviewSample = new TextArea();
	private final InputField<IdentifierChecker, String> inClassName = new InputField<>(new IdentifierChecker());
	private final Label lblBaseControl;
	private final TextArea taComment = new TextArea();
	private final ControlClassMenuButton extendClassMenuButton;
	private final ComboBox<CustomControlClass.Scope> comboBoxScope = new ComboBox<>();

	private ControlPropertiesEditorPane editorPane;

	private final ResourceBundle bundle = Lang.ApplicationBundle();

	private final UpdateGroupListener<ControlPropertyUpdate> controlClassListener = new UpdateGroupListener<ControlPropertyUpdate>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<ControlPropertyUpdate> group, @NotNull ControlPropertyUpdate data) {
			updatePreview();
		}
	};

	private final Project project = Project.getCurrentProject();

	public NewCustomControlClassDialog() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, true, true, true);
		setTitle(bundle.getString("Popups.NewCustomControl.popup_title"));

		myRootElement.setPrefHeight(560);
		myRootElement.setPrefWidth(720);

		/*HEADER*/
		{
			VBox vboxHeader = new VBox(10);
			myRootElement.getChildren().add(vboxHeader);
			HBox hboxTopHeader = new HBox(10);
			HBox hboxBottomHeader = new HBox(10);

			vboxHeader.getChildren().add(hboxTopHeader);
			vboxHeader.getChildren().add(hboxBottomHeader);

			hboxTopHeader.setFillHeight(true);

			//control class name
			{
				final Label lblControlClassName = new Label(bundle.getString("Popups.NewCustomControl.control_class_name"));
				lblControlClassName.setFont(Font.font(18d));
				hboxTopHeader.getChildren().add(lblControlClassName);
				hboxTopHeader.getChildren().add(inClassName);

				inClassName.getValueObserver().addListener(new ValueListener<String>() {
					@Override
					public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
						newValue = newValue != null ? newValue : "";
						editorPane.getConfigClassSpecification().setClassName(newValue);
						updatePreview();
					}
				});
			}

			//extend/parent class
			{
				hboxTopHeader.getChildren().add(new Label(":"));
				extendClassMenuButton = new ControlClassMenuButton(
						true,
						bundle.getString("Popups.NewCustomControl.no_parent_class"),
						null
				);

				hboxTopHeader.getChildren().add(extendClassMenuButton);

				extendClassMenuButton.addItems(getCustomControlClassesItems());
				extendClassMenuButton.getSelectedValueObserver().addListener((observer, oldValue, newValue) -> {
					if (getEditorPane() == null) {
						return;
					}
					ControlClassOld editClass = getEditorPane().getConfigClassSpecification();
					if (newValue != null) {
						if (editClass.hasInheritanceLoop(newValue)) {
							SimpleResponseDialog dialog = new SimpleResponseDialog(
									myStage,
									bundle.getString("Popups.NewCustomControl.inheritance_loop_title"),
									String.format(
											bundle.getString("Popups.NewCustomControl.inheritance_loop_body_f"),
											editClass.getClassName()
									),
									false, true, false
							);
							dialog.setStageSize(400, 120);
							dialog.show();
							extendClassMenuButton.chooseItem((ControlClassOld) null);
							return;
						}
					}
					editClass.extendControlClass(newValue);
					updatePreview();
				});

			}

			//scope
			{
				comboBoxScope.getItems().addAll(CustomControlClass.Scope.values());
				comboBoxScope.getSelectionModel().select(CustomControlClass.Scope.Workspace);
				HBox hbox = new HBox(5,
						new Label(bundle.getString("Popups.NewCustomControl.scope")),
						comboBoxScope
				);
				hbox.setAlignment(Pos.CENTER_LEFT);
				hboxBottomHeader.getChildren().add(hbox);

				comboBoxScope.setTooltip(new Tooltip(bundle.getString("Popups.NewCustomControl.scope_tooltip")));

			}

			//templates
			{
				List<CBMBMenuItem<String>> controlTypeControlClasses = new ArrayList<>(
						ControlType.AVAILABLE_TYPES.length
				);
				CBMBMenuItem<String> toSelect = null;
				for (int i = 0; i < ControlType.AVAILABLE_TYPES.length; i++) {
					ArmaControlLookup lookup = ArmaControlLookup.findByControlType(ControlType.AVAILABLE_TYPES[i]);
					CBMBMenuItem<String> menuItem = new CBMBMenuItem<>(
							lookup.controlType.getDisplayName(),
							new BorderedImageView(lookup.controlType.getIcon())
					);
					menuItem.setUserData(lookup);
					controlTypeControlClasses.add(menuItem);
					if (lookup.controlType == ControlType.Static) {
						toSelect = menuItem;
					}
				}
				final ComboBoxMenuButton<String> templateControlMenuButton = new ComboBoxMenuButton<>(
						false, "", null,
						new CBMBGroupMenu<>(
								bundle.getString("Popups.NewCustomControl.control_types"),
								controlTypeControlClasses
						)
				);
				{
					CBMBGroupMenu<String> customControlsMenu = new CBMBGroupMenu<>(
							bundle.getString("Popups.NewCustomControl.custom_controls"),
							getCustomControlClassesNamesItems()
					);
					if (customControlsMenu.getItems().size() > 0) {
						templateControlMenuButton.addGroup(customControlsMenu);
					}
				}
				templateControlMenuButton.getSelectedItemObserver().addListener((observer, oldValue, selectedItem) -> {
					if (selectedItem == null) {
						throw new IllegalStateException("selectedItem shouldn't be null");
					}
					if (selectedItem.getUserData() instanceof ArmaControlLookup) {
						ArmaControlLookup lookup = (ArmaControlLookup) selectedItem.getUserData();
						ControlClassOld cc = new ControlClassOld(
								"Custom_" + lookup.controlType.getNameAsClassName(),
								lookup.specProvider,
								project
						);
						setToControlClass(cc);
					} else if (selectedItem.getUserData() instanceof CustomControlClass) {
						CustomControlClass ccc = (CustomControlClass) selectedItem.getUserData();
						setToControlClass(ccc.newSpecification().constructNewControlClass(project));
					} else {
						throw new IllegalStateException("unknown user data");
					}
				});

				templateControlMenuButton.chooseItem(toSelect);

				lblBaseControl = new Label(bundle.getString("Popups.NewCustomControl.template"), templateControlMenuButton);
				lblBaseControl.setContentDisplay(ContentDisplay.RIGHT);
				hboxBottomHeader.getChildren().add(lblBaseControl);
			}
		}


		myRootElement.getChildren().add(new Separator(Orientation.HORIZONTAL));

		//BODY
		{
			final HBox hboxBody = new HBox(10);

			VBox.setVgrow(hboxBody, Priority.ALWAYS);

			final VBox vboxProperties = new VBox(5, new Label(bundle.getString("Popups.NewCustomControl.properties")), stackPaneProperties);
			VBox.setVgrow(vboxProperties, Priority.ALWAYS);
			VBox.setVgrow(stackPaneProperties, Priority.ALWAYS);
			hboxBody.getChildren().add(vboxProperties);

			final VBox vboxComment = new VBox(5, new Label(bundle.getString("Popups.NewCustomControl.comment")), taComment);
			vboxComment.setFillWidth(true);
			VBox.setVgrow(taComment, Priority.ALWAYS);
			VBox.setVgrow(vboxComment, Priority.ALWAYS);
			taComment.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean focused) {
					if (!focused) {
						updatePreview();
					}
				}
			});

			final VBox vboxPreview = new VBox(5, new Label(bundle.getString("Popups.NewCustomControl.preview_sample")), taPreviewSample);
			taPreviewSample.setEditable(false);
			vboxPreview.setFillWidth(true);
			VBox.setVgrow(taPreviewSample, Priority.ALWAYS);
			VBox.setVgrow(vboxPreview, Priority.ALWAYS);

			final SplitPane splitPane = new SplitPane(vboxComment, vboxPreview);
			splitPane.setStyle("-fx-background-color:transparent;"); //remove border
			splitPane.setPrefWidth(300d);
			splitPane.setOrientation(Orientation.VERTICAL);

			hboxBody.getChildren().add(splitPane);

			for (Node n : hboxBody.getChildren()) {
				HBox.setHgrow(n, Priority.ALWAYS);
			}

			myRootElement.getChildren().add(hboxBody);
		}

		myStage.sizeToScene();
	}

	private List<CBMBMenuItem<String>> getCustomControlClassesNamesItems() {
		List<CustomControlClass> cccList = project.getAllCustomControlClasses();
		List<CBMBMenuItem<String>> items = new ArrayList<>(cccList.size());
		for (CustomControlClass ccc : cccList) {
			CBMBMenuItem<String> menuItem = new CBMBMenuItem<>(ccc.getControlClass().getClassName());
			menuItem.setUserData(ccc);
			items.add(menuItem);
		}
		return items;
	}

	private List<CBMBMenuItem<ControlClassOld>> getCustomControlClassesItems() {
		List<CustomControlClass> cccList = project.getAllCustomControlClasses();
		List<CBMBMenuItem<ControlClassOld>> items = new ArrayList<>(cccList.size());
		for (CustomControlClass ccc : cccList) {
			items.add(new ControlClassMenuItem(ccc.getControlClass()));
		}
		return items;
	}

	/**
	 Set which scope is selected for the combobox used to select {@link CustomControlClass.Scope}

	 @param scope the scope to select
	 */
	protected void setComboBoxScope(@NotNull CustomControlClass.Scope scope) {
		comboBoxScope.getSelectionModel().select(scope);
	}

	protected void hideBaseControlMenuButton(boolean hidden) {
		lblBaseControl.setVisible(!hidden);
	}

	/**
	 Set the editor to the given {@link ControlClassOld} instance.
	 <b>Beware: the given {@link ControlClassOld} will be edited regardless of whether the user presses ok or not.</b>

	 @param controlClass instance to edit
	 */
	protected void setToControlClass(@NotNull ControlClassOld controlClass) {
		if (editorPane != null) {
			removeListeners();
		}
		editorPane = new ControlPropertiesEditorPane(controlClass);
		stackPaneProperties.getChildren().clear();
		stackPaneProperties.getChildren().add(editorPane);

		controlClass.getPropertyUpdateGroup().addListener(controlClassListener);
		inClassName.setValue(controlClass.getClassName());
		extendClassMenuButton.chooseItem(controlClass.getExtendClass());
		updatePreview();
	}

	@NotNull
	protected InputField<IdentifierChecker, String> getInClassName() {
		return inClassName;
	}

	@NotNull
	protected TextArea getTaComment() {
		return taComment;
	}

	@Nullable
	protected ControlPropertiesEditorPane getEditorPane() {
		return editorPane;
	}

	@Override
	protected void closing() {
		removeListeners();
	}

	private void removeListeners() {
		editorPane.getConfigClassSpecification().getPropertyUpdateGroup().removeListener(controlClassListener);
		editorPane.unlink();
	}

	@Override
	protected void ok() {
		if (!checkIfEntriesValid()) {
			return;
		}
		super.ok();
	}

	/** @return true if entries are valid, false otherwise */
	protected boolean checkIfEntriesValid() {
		if (inClassName.getValue() == null) {
			beep();
			inClassName.requestFocus();
			return false;
		}
		return true;
	}

	/** @return a {@link CustomControlClass} instance that was created */
	@NotNull
	public CustomControlClass getCustomControlClass() {
		CustomControlClass customControlClass = new CustomControlClass(editorPane.getConfigClassSpecification(), comboBoxScope.getValue());
		customControlClass.setComment(taComment.getText());
		return customControlClass;
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.CUSTOM_CONTROL_CLASSES);
	}

	private void updatePreview() {
		taPreviewSample.setText(getPreviewText());
	}

	private String getPreviewText() {
		StringWriter writer = new StringWriter(128);
		try {
			if (getTaComment().getText() != null && getTaComment().getText().length() > 0) {
				writer.write("/*\n");
				writer.write(getTaComment().getText());
				writer.write("\n*/\n");
			}
			ProjectExporter.exportControlClass(project.getExportConfiguration(), editorPane.getConfigClassSpecification(), writer);
			writer.close();
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
		return writer.toString();
	}
}
