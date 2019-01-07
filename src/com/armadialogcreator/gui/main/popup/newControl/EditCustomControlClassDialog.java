package com.armadialogcreator.gui.main.popup.newControl;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.core.ControlClassOld;
import com.armadialogcreator.core.ControlClassUpdate;
import com.armadialogcreator.core.CustomControlClass;
import com.armadialogcreator.data.olddata.Project;
import com.armadialogcreator.gui.SimpleResponseDialog;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.scene.control.Button;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 Popup window that allows for editing a {@link CustomControlClass}.

 @author Kayler
 @since 11/14/2016. */
public class EditCustomControlClassDialog extends NewCustomControlClassDialog {

	private final CustomControlClass toEdit;
	private final LinkedList<ControlClassUpdate> updates = new LinkedList<>();
	private final ControlClassOld duplicate;

	public EditCustomControlClassDialog(@NotNull CustomControlClass toEdit) {
		this.toEdit = toEdit;
		ResourceBundle bundle = Lang.ApplicationBundle();
		myStage.setTitle(bundle.getString("Popups.EditCustomControl.popup_title"));

		duplicate = toEdit.newSpecification().constructNewControlClass(Project.getCurrentProject());
		UpdateGroupListener<ControlClassUpdate> listener = new UpdateGroupListener<ControlClassUpdate>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, ControlClassUpdate data) {
				updates.add(data);
			}
		};
		duplicate.getControlClassUpdateGroup().addListener(listener);

		getTaComment().setText(toEdit.getComment()); //set before control class to make sure preview is correct
		setToControlClass(duplicate);
		hideBaseControlMenuButton(true);

		Button btnDelete = new Button(bundle.getString("Popups.EditCustomControl.delete_control_class"));
		btnDelete.setOnAction(event -> {
			SimpleResponseDialog d = new SimpleResponseDialog(
					ArmaDialogCreator.getPrimaryStage(),
					bundle.getString("Popups.EditCustomControl.delete_control_confirm_title"),
					String.format(bundle.getString("Popups.EditCustomControl.delete_control_confirm_f"), toEdit.getControlClass().getClassName()), true, true, false
			);
			d.show();
			if (d.wasCancelled()) {
				return;
			}
			duplicate.getControlClassUpdateGroup().removeListener(listener);
			Project.getCurrentProject().removeCustomControlClass(toEdit);
			toEdit.getControlClass().clearSubClasses();

			close();
		});
		footer.getRightContainer().getChildren().add(0, btnDelete);

		setComboBoxScope(toEdit.getScope());
	}

	@Override
	protected void ok() {
		if (!checkIfEntriesValid()) {
			return;
		}
		for (ControlClassUpdate update : updates) {
			toEdit.getControlClass().update(update, false);
		}
		toEdit.setComment(getTaComment().getText());
		CustomControlClass duplicatedCustomControlClass = getCustomControlClass();
		if (toEdit.getScope() != duplicatedCustomControlClass.getScope()) {
			Project project = Project.getCurrentProject();
			project.removeCustomControlClass(toEdit);
			toEdit.setScope(duplicatedCustomControlClass.getScope());
			project.addCustomControlClass(toEdit);
		}
		close();
	}

}
