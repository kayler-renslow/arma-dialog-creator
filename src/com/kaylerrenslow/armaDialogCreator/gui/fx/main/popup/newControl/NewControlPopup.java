package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ControlPropertiesEditorPane;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ControlPropertyEditor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Popup window that allows for creating a new control. It has a control properties editor on the left and a preview window on the right to preview the outputted .h file text
 Created on 07/06/2016. */
public class NewControlPopup extends StagePopup<VBox> {
	private final StackPane stackPaneProperties;
	private final TextArea taPreviewSample;
	private final InputField<IdentifierChecker, String> inClassName = new InputField<>(new IdentifierChecker());
	private ControlPropertiesEditorPane editorPane;

	private final UpdateListener<ControlProperty> controlPropertyObserverListener = new UpdateListener<ControlProperty>() {
		@Override
		public void update(ControlProperty data) {
			updatePreview();
		}
	};

	public NewControlPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), FXUtil.loadFxml("/com/kaylerrenslow/armaDialogCreator/gui/fx/main/popup/newControl/newControl.fxml"), Lang.Popups.NewControl.POPUP_TITLE);
		if(getMyLoader() == null){
			throw new IllegalStateException("getMyLoader() should not return null");
		}
		NewControlPopupController controller = getMyLoader().getController();

		initClassNameInputField(controller);

		taPreviewSample = controller.taPreviewSample;
		stackPaneProperties = controller.stackPaneProperties;
		controller.cobBaseControl.getItems().addAll(ControlType.values());
		controller.cobBaseControl.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ControlType>() {
			@Override
			public void changed(ObservableValue<? extends ControlType> observable, ControlType oldValue, ControlType selected) {
				setToControlType(selected);
			}
		});
		controller.cobBaseControl.getSelectionModel().select(ControlType.STATIC);
		myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(true, true, false));
		myStage.sizeToScene();
	}

	private void initClassNameInputField(NewControlPopupController controller) {
		HBox hbHeader = controller.hbHeader;
		int indexTf = hbHeader.getChildren().indexOf(controller.tfClassName);
		hbHeader.getChildren().add(indexTf, inClassName);
		hbHeader.getChildren().remove(controller.tfClassName);

		inClassName.setValue("New_ADC_Control");
		inClassName.getValueObserver().addValueListener(new ValueListener<String>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
				updatePreview();
			}
		});
	}

	private void setToControlType(ControlType type) {
		ArmaControlLookup lookup = ArmaControlLookup.findByControlType(type);
		editorPane = new ControlPropertiesEditorPane(lookup.specProvider, type);
		stackPaneProperties.getChildren().clear();
		stackPaneProperties.getChildren().add(editorPane);

		ControlPropertyEditor[] editors = editorPane.getEditors();
		for (ControlPropertyEditor editor : editors) {
			editor.getControlPropertyUpdateGroup().addListener(controlPropertyObserverListener);
		}

		updatePreview();
	}

	private void updatePreview() {
		taPreviewSample.setText(getPreviewText());
	}

	private String getPreviewText() {
		String body = "";
		ControlPropertyEditor[] editors = editorPane.getEditors();
		ControlProperty property;
		final String itemFormatString = "\t%s = %s;\n";
		final String itemArrayFormatString = "\t%s[] = %s;\n";
		final String classFormatString = "class %s \n{\n%s};";
		for (ControlPropertyEditor editor : editors) {
			property = editor.getControlProperty();
			if (property.getValue() == null/* && editor.isOptional()*/) { //can allow for partial implementation, so we don't need to check if it is optional
				continue;
			}
			if (property.getValue().getAsStringArray().length == 1) {
				body += String.format(itemFormatString, property.getName(), property.getValuesForExport());
			} else {
				body += String.format(itemArrayFormatString, property.getName(), property.getValuesForExport());
			}
		}
		return String.format(classFormatString, inClassName.getValue(), body);
	}
}
