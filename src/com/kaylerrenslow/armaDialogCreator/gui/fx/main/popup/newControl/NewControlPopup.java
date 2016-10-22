/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.io.export.ProjectExporter;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.BorderedImageView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ControlClassMenuButton;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ControlPropertiesEditorPane;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor.ControlPropertyEditor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.HelpUrls;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Popup window that allows for creating a new control. It has a control properties editor on the left and a preview window on the right to preview the outputted .h file text
 Created on 07/06/2016. */
public class NewControlPopup extends StagePopup<VBox> {
	private final StackPane stackPaneProperties = new StackPane();
	private final TextArea taPreviewSample = new TextArea();
	private final InputField<IdentifierChecker, String> inClassName = new InputField<>(new IdentifierChecker());

	private ControlPropertiesEditorPane editorPane;


	private final ValueListener<SerializableValue> controlPropertyObserverListener = new ValueListener<SerializableValue>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
			updatePreview();
		}
	};

	public NewControlPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("Popups.NewControl.popup_title"));
		myRootElement.setPadding(new Insets(10));

		/*HEADER*/
		final HBox hboxHeader = new HBox(10);
		hboxHeader.setFillHeight(true);

		final Label lblControlClassName = new Label(Lang.ApplicationBundle().getString("Popups.NewControl.control_class_name"));
		lblControlClassName.setFont(Font.font(18d));
		hboxHeader.getChildren().add(lblControlClassName);
		hboxHeader.getChildren().add(inClassName);

		inClassName.setValue("New_ADC_Control");
		inClassName.getValueObserver().addValueListener(new ValueListener<String>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
				updatePreview();
			}
		});

		ControlClassMenuButton.ControlClassMenuItem[] contolTypeControlClasses = new ControlClassMenuButton.ControlClassMenuItem[ControlType.BETA_SUPPORTED.length];
		ControlClassMenuButton.ControlClassMenuItem toSelect = null;
		for (int i = 0; i < contolTypeControlClasses.length; i++) {
			ArmaControlLookup lookup = ArmaControlLookup.findByControlType(ControlType.BETA_SUPPORTED[i]);
			ControlClass controlClass = new ControlClass(lookup.controlType.displayName, lookup.specProvider);
			controlClass.findRequiredProperty(ControlPropertyLookup.TYPE).setValue(lookup.controlType.typeId);
			contolTypeControlClasses[i] = new ControlClassMenuButton.ControlClassMenuItem(controlClass, new BorderedImageView(lookup.controlType.icon));
			if (lookup.controlType == ControlType.STATIC) {
				toSelect = contolTypeControlClasses[i];
			}
		}
		final ControlClassMenuButton controlClassMenuButton = new ControlClassMenuButton(false, "", null, new ControlClassMenuButton.ControlClassGroupMenu(Lang.ApplicationBundle().getString
				("Popups.NewControl.base_types"), contolTypeControlClasses));
		controlClassMenuButton.getSelectedItemObserver().addValueListener(new ReadOnlyValueListener<ControlClass>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<ControlClass> observer, ControlClass oldValue, ControlClass newValue) {
				setToControlClass(newValue);
			}
		});

		controlClassMenuButton.chooseItem(toSelect);

		final Label lblBaseControl = new Label(Lang.ApplicationBundle().getString("Popups.NewControl.base_control"), controlClassMenuButton);
		lblBaseControl.setContentDisplay(ContentDisplay.RIGHT);
		hboxHeader.getChildren().add(lblBaseControl);


		/*BODY*/
		final HBox hboxBody = new HBox(10);
		VBox.setVgrow(hboxBody, Priority.ALWAYS);

		final VBox vboxProperties = new VBox(5, new Label(Lang.ApplicationBundle().getString("Popups.NewControl.properties")), stackPaneProperties);
		VBox.setVgrow(vboxProperties, Priority.ALWAYS);
		hboxBody.getChildren().add(vboxProperties);

		final VBox vboxPreview = new VBox(5, new Label(Lang.ApplicationBundle().getString("Popups.NewControl.preview_sample")), taPreviewSample);
		taPreviewSample.setEditable(false);
		taPreviewSample.setPrefWidth(300d);
		VBox.setVgrow(taPreviewSample, Priority.ALWAYS);
		VBox.setVgrow(vboxPreview, Priority.ALWAYS);
		hboxBody.getChildren().add(vboxPreview);

		myRootElement.getChildren().addAll(hboxHeader, new Separator(Orientation.HORIZONTAL), hboxBody, new Separator(Orientation.HORIZONTAL), getBoundResponseFooter(true, true, true));
		myStage.sizeToScene();
	}

	private void setToControlClass(@NotNull ControlClass controlClass) {
		editorPane = new ControlPropertiesEditorPane(controlClass);
		stackPaneProperties.getChildren().clear();
		stackPaneProperties.getChildren().add(editorPane);

		ControlPropertyEditor[] editors = editorPane.getEditors();
		for (ControlPropertyEditor editor : editors) {
			editor.getControlProperty().getValueObserver().addValueListener(controlPropertyObserverListener);
		}

		updatePreview();
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.NEW_CONTROL_POPUP);
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
				body += String.format(itemFormatString, property.getName(), ProjectExporter.getExportValueString(property.getValue(), property.getPropertyType()));
			} else {
				body += String.format(itemArrayFormatString, property.getName(), ProjectExporter.getExportValueString(property.getValue(), property.getPropertyType()));
			}
		}
		return String.format(classFormatString, inClassName.getValue(), body);
	}
}
