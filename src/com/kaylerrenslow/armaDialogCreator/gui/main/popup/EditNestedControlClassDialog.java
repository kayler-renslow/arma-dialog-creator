package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.CBMBMenuItem;
import com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor.ControlPropertiesEditorPane;
import com.kaylerrenslow.armaDialogCreator.gui.main.fxControls.ControlClassMenuButton;
import com.kaylerrenslow.armaDialogCreator.gui.main.fxControls.ControlClassMenuItem;
import com.kaylerrenslow.armaDialogCreator.gui.popup.SimpleResponseDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 Dialog window that allows for editing a ({@link ControlClass}). This dialog is specialized towards editing
 nested {@link ControlClass} instances. This editor will not allow editing of the {@link ControlClass#getClassName()}.
 It has a {@link ControlPropertiesEditorPane} embedded into it.

 @author Kayler
 @since 06/30/2017 */
public class EditNestedControlClassDialog extends StageDialog<VBox> {

	private final ControlClassMenuButton extendClassMenuButton;

	private ControlPropertiesEditorPane editorPane;

	private final ResourceBundle bundle = Lang.ApplicationBundle();

	/**
	 @param controlClass the {@link ControlClass} to edit/mutate
	 */
	public EditNestedControlClassDialog(@NotNull ControlClass controlClass) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, false, true, true);
		setTitle(bundle.getString("Popups.EditNestedControlClass.popup_title"));

		editorPane = new ControlPropertiesEditorPane(controlClass);

		editorPane.setMinWidth(720);
		editorPane.setMinHeight(480);

		/*HEADER*/
		{
			HBox hboxHeader = new HBox(10);
			hboxHeader.setFillHeight(true);

			Font fontHeader = Font.font(18);

			//control class name
			{
				Label lblControlClassName = new Label(controlClass.getClassName());
				lblControlClassName.setFont(fontHeader);
				hboxHeader.getChildren().add(lblControlClassName);

			}

			//extend/parent class
			{
				Label lblColon = new Label(":");
				lblColon.setFont(fontHeader);
				hboxHeader.getChildren().add(lblColon);
				extendClassMenuButton = new ControlClassMenuButton(
						true,
						bundle.getString("Popups.NewCustomControl.no_parent_class"),
						null
				);

				hboxHeader.getChildren().add(extendClassMenuButton);

				extendClassMenuButton.addItems(getCustomControlClassesItems());
				extendClassMenuButton.getSelectedValueObserver().addListener((observer, oldValue, newValue) -> {
					if (newValue != null) {
						if (controlClass.hasInheritanceLoop(newValue)) {
							SimpleResponseDialog dialog = new SimpleResponseDialog(
									myStage,
									bundle.getString("Popups.NewCustomControl.inheritance_loop_title"),
									String.format(
											bundle.getString("Popups.NewCustomControl.inheritance_loop_body_f"),
											controlClass.getClassName()
									),
									false, true, false
							);
							dialog.setStageSize(400, 120);
							dialog.show();
							extendClassMenuButton.chooseItem((ControlClass) null);
							return;
						}
					}
					controlClass.extendControlClass(newValue);
				});
				extendClassMenuButton.chooseItem(controlClass.getExtendClass());

			}


			myRootElement.getChildren().add(hboxHeader);
		}


		myRootElement.getChildren().add(new Separator(Orientation.HORIZONTAL));

		//BODY
		{
			VBox vboxProperties = new VBox(5,
					new Label(bundle.getString("Popups.NewCustomControl.properties")), editorPane
			);
			VBox.setVgrow(vboxProperties, Priority.ALWAYS);
			vboxProperties.setFillWidth(true);

			myRootElement.getChildren().add(vboxProperties);
		}

		myStage.sizeToScene();
	}

	private List<CBMBMenuItem<ControlClass>> getCustomControlClassesItems() {
		Project project = Project.getCurrentProject();
		List<CustomControlClass> cccList = project.getAllCustomControlClasses();
		List<CBMBMenuItem<ControlClass>> items = new ArrayList<>(cccList.size());
		for (CustomControlClass ccc : cccList) {
			items.add(new ControlClassMenuItem(ccc.getControlClass()));
		}
		return items;
	}


	@Override
	protected void closing() {
		editorPane.unlink();
	}

}
