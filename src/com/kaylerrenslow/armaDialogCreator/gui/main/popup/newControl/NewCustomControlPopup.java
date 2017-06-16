package com.kaylerrenslow.armaDialogCreator.gui.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.export.ProjectExporter;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.BorderedImageView;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.IdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor.ControlPropertiesEditorPane;
import com.kaylerrenslow.armaDialogCreator.gui.main.fxControls.ControlClassGroupMenu;
import com.kaylerrenslow.armaDialogCreator.gui.main.fxControls.ControlClassMenuButton;
import com.kaylerrenslow.armaDialogCreator.gui.main.fxControls.ControlClassMenuItem;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.HelpUrls;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 Popup window that allows for creating a new custom control ({@link CustomControlClass}). It has a control properties editor on the left and a preview window on the right to preview the outputted .h file text

 @author Kayler
 @since 07/06/2016. */
public class NewCustomControlPopup extends StagePopup<VBox> {

	private final StackPane stackPaneProperties = new StackPane();
	private final TextArea taPreviewSample = new TextArea();
	private final InputField<IdentifierChecker, String> inClassName = new InputField<>(new IdentifierChecker());
	private final Label lblBaseControl;
	private final TextArea taComment = new TextArea();

	private ControlPropertiesEditorPane editorPane;

	private final UpdateGroupListener<ControlPropertyUpdate> controlClassListener = new UpdateGroupListener<ControlPropertyUpdate>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<ControlPropertyUpdate> group, ControlPropertyUpdate data) {
			updatePreview();
		}
	};

	public NewCustomControlPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("Popups.NewCustomControl.popup_title"));
		myRootElement.setPadding(new Insets(10));

		/*HEADER*/
		final HBox hboxHeader = new HBox(10);
		hboxHeader.setFillHeight(true);

		final Label lblControlClassName = new Label(Lang.ApplicationBundle().getString("Popups.NewCustomControl.control_class_name"));
		lblControlClassName.setFont(Font.font(18d));
		hboxHeader.getChildren().add(lblControlClassName);
		hboxHeader.getChildren().add(inClassName);

		inClassName.getValueObserver().addListener(new ValueListener<String>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
				newValue = newValue != null ? newValue : "";
				editorPane.getControlClass().setClassName(newValue);
				updatePreview();
			}
		});

		ControlClassMenuItem[] controlTypeControlClasses = new ControlClassMenuItem[ControlType.BETA_SUPPORTED.length];
		ControlClassMenuItem toSelect = null;
		for (int i = 0; i < controlTypeControlClasses.length; i++) {
			ArmaControlLookup lookup = ArmaControlLookup.findByControlType(ControlType.BETA_SUPPORTED[i]);
			ControlClass controlClass = new ControlClass(lookup.controlType.getDisplayName(), lookup.specProvider, Project.getCurrentProject());
			controlClass.findRequiredProperty(ControlPropertyLookup.TYPE).setValue(lookup.controlType.getTypeId());
			controlTypeControlClasses[i] = new ControlClassMenuItem(controlClass, new BorderedImageView(lookup.controlType.getIcon()));
			controlClass.setClassName("Custom_" + controlClass.getClassName());
			if (lookup.controlType == ControlType.Static) {
				toSelect = controlTypeControlClasses[i];
			}
		}
		final ControlClassMenuButton baseControlMenuButton = new ControlClassMenuButton(
				false, "", null,
				new ControlClassGroupMenu(Lang.ApplicationBundle().getString("Popups.NewCustomControl.base_types"), controlTypeControlClasses),
				new ControlClassGroupMenu(
						Lang.ApplicationBundle().getString("Popups.NewCustomControl.custom_controls"),
						getCustomControlsItems()
				)
		);
		baseControlMenuButton.getSelectedValueObserver().addListener(new ReadOnlyValueListener<ControlClass>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<ControlClass> observer, ControlClass oldValue, ControlClass newValue) {
				setToControlClass(newValue);
			}
		});

		baseControlMenuButton.chooseItem(toSelect);

		lblBaseControl = new Label(Lang.ApplicationBundle().getString("Popups.NewCustomControl.base_control"), baseControlMenuButton);
		lblBaseControl.setContentDisplay(ContentDisplay.RIGHT);
		hboxHeader.getChildren().add(lblBaseControl);


		/*BODY*/
		final HBox hboxBody = new HBox(10);
		VBox.setVgrow(hboxBody, Priority.ALWAYS);

		final VBox vboxProperties = new VBox(5, new Label(Lang.ApplicationBundle().getString("Popups.NewCustomControl.properties")), stackPaneProperties);
		VBox.setVgrow(vboxProperties, Priority.ALWAYS);
		VBox.setVgrow(stackPaneProperties, Priority.ALWAYS);
		hboxBody.getChildren().add(vboxProperties);

		final VBox vboxComment = new VBox(5, new Label(Lang.ApplicationBundle().getString("Popups.NewCustomControl.comment")), taComment);
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

		final VBox vboxPreview = new VBox(5, new Label(Lang.ApplicationBundle().getString("Popups.NewCustomControl.preview_sample")), taPreviewSample);
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

		myRootElement.getChildren().addAll(hboxHeader, new Separator(Orientation.HORIZONTAL), hboxBody, new Separator(Orientation.HORIZONTAL), getBoundResponseFooter(true, true, true));
		myStage.sizeToScene();
	}

	private ControlClassMenuItem[] getCustomControlsItems() {
		ReadOnlyList<CustomControlClass> customControlClasses = Project.getCurrentProject().getCustomControlClassRegistry().getControlClassList();
		ControlClassMenuItem[] items = new ControlClassMenuItem[customControlClasses.size()];
		int i = 0;
		for (CustomControlClass customControlClass : customControlClasses) {
			items[i] = new ControlClassMenuItem(customControlClass.newSpecification().constructNewControlClass(Project.getCurrentProject()));
			i++;
		}
		return items;
	}

	protected void hideBaseControlMenuButton(boolean hidden) {
		lblBaseControl.setVisible(!hidden);
	}

	/**
	 Set the editor to the given {@link ControlClass} instance. <b>Beware: the given {@link ControlClass} will be edited regardless of whether the user presses ok or not.</b>

	 @param controlClass instance to edit
	 */
	protected void setToControlClass(@NotNull ControlClass controlClass) {
		if (editorPane != null) {
			removeListeners();
		}
		editorPane = new ControlPropertiesEditorPane(controlClass);
		stackPaneProperties.getChildren().clear();
		stackPaneProperties.getChildren().add(editorPane);

		controlClass.getPropertyUpdateGroup().addListener(controlClassListener);

		boolean updatePreview = inClassName.getValue() != null && inClassName.getValue().equals(controlClass.getClassName()); //won't be triggered by class name update
		inClassName.setValue(controlClass.getClassName());
		if (updatePreview) {
			updatePreview();
		}
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
		editorPane.getControlClass().getPropertyUpdateGroup().removeListener(controlClassListener);
		editorPane.unlink();
	}

	@Override
	protected void ok() {
		CustomControlClass customControlClass = new CustomControlClass(editorPane.getControlClass());
		customControlClass.setComment(taComment.getText());
		Project.getCurrentProject().getCustomControlClassRegistry().addControlClass(customControlClass);

		super.ok();
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.NEW_CUSTOM_CONTROL_POPUP);
	}

	private void updatePreview() {
		taPreviewSample.setText(getPreviewText());
	}

	private String getPreviewText() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream(128);
		try {
			if (getTaComment().getText() != null && getTaComment().getText().length() > 0) {
				stream.write("/*\n".getBytes());
				stream.write(getTaComment().getText().getBytes());
				stream.write("\n*/\n".getBytes());
			}
			ProjectExporter.exportControlClass(Project.getCurrentProject().getExportConfiguration(), editorPane.getControlClass(), stream);
			stream.close();
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
		return stream.toString();
	}
}
