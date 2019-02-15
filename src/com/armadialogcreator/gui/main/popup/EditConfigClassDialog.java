package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.data.ConfigClassRegistry;
import com.armadialogcreator.gui.SimpleResponseDialog;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.main.controlPropertiesEditor.ControlPropertiesEditorPane;
import com.armadialogcreator.gui.main.fxControls.ConfigClassGroupMenu;
import com.armadialogcreator.gui.main.fxControls.ConfigClassMenuButton;
import com.armadialogcreator.lang.Lang;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 Dialog window that allows for editing a ({@link ConfigClass}) directly.
 It has a {@link ControlPropertiesEditorPane} embedded into it.

 @author Kayler
 @since 06/30/2017 */
public class EditConfigClassDialog extends StageDialog<VBox> {

	private final ConfigClassMenuButton extendClassMenuButton;

	private ControlPropertiesEditorPane editorPane;

	private final ResourceBundle bundle = Lang.ApplicationBundle();

	/**
	 @param configClass the {@link ConfigClass} to edit
	 */
	public EditConfigClassDialog(@NotNull ConfigClass configClass) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, false, true, false);
		setTitle(bundle.getString("Popups.EditNestedControlClass.popup_title"));

		editorPane = new ControlPropertiesEditorPane(configClass);

		editorPane.setMinWidth(720);
		editorPane.setMinHeight(480);

		/*HEADER*/
		{
			HBox hboxHeader = new HBox(10);
			hboxHeader.setFillHeight(true);

			Font fontHeader = Font.font(18);

			//control class name
			{
				Label lblControlClassName = new Label(configClass.getClassName());
				lblControlClassName.setFont(fontHeader);
				hboxHeader.getChildren().add(lblControlClassName);

			}

			//extend/parent class
			{
				Label lblColon = new Label(":");
				lblColon.setFont(fontHeader);
				hboxHeader.getChildren().add(lblColon);
				extendClassMenuButton = new ConfigClassMenuButton(
						true,
						bundle.getString("Popups.NewCustomControl.no_parent_class"),
						null
				);

				hboxHeader.getChildren().add(extendClassMenuButton);

				{
					ConfigClassRegistry registry = ConfigClassRegistry.instance;
					Map<DataLevel, List<ConfigClass>> map = registry.copyAllClassesToMap();

					ConfigClassGroupMenu menuAll = new ConfigClassGroupMenu("All");
					for (DataLevel level : map.keySet()) {
						ConfigClassGroupMenu menu = new ConfigClassGroupMenu(level.name());
						List<ConfigClass> classes = map.get(level);
						menu.addAllValues(classes, null);
						extendClassMenuButton.addGroup(menu);
						menuAll.addAllValues(classes, null);
					}
					extendClassMenuButton.addGroup(menuAll);

				}

				extendClassMenuButton.getSelectedValueObserver().addListener((observer, oldValue, newValue) -> {
					if (newValue != null) {
						if (configClass.hasInheritanceLoop(newValue)) {
							SimpleResponseDialog dialog = new SimpleResponseDialog(
									myStage,
									bundle.getString("Popups.NewCustomControl.inheritance_loop_title"),
									String.format(
											bundle.getString("Popups.NewCustomControl.inheritance_loop_body_f"),
											configClass.getClassName()
									),
									false, true, false
							);
							dialog.setStageSize(400, 120);
							dialog.show();
							extendClassMenuButton.chooseItem((ConfigClass) null);
							return;
						}
						configClass.extendConfigClass(newValue);
					} else {
						configClass.clearExtendConfigClass();
					}
				});
				extendClassMenuButton.chooseItem(configClass.getExtendClass());

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

	@Override
	protected void closing() {
		editorPane.unlink();
	}

}
