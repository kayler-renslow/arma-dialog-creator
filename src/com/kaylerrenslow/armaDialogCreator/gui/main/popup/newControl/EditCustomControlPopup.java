package com.kaylerrenslow.armaDialogCreator.gui.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlClassUpdate;
import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 Popup window that allows for editing a {@link CustomControlClass}.

 @author Kayler
 @since 11/14/2016. */
public class EditCustomControlPopup extends NewCustomControlPopup {

	private final CustomControlClass toEdit;
	private final LinkedList<ControlClassUpdate> updates = new LinkedList<>();
	private final ControlClass duplicate;

	public EditCustomControlPopup(@NotNull CustomControlClass toEdit) {
		this.toEdit = toEdit;
		myStage.setTitle(Lang.ApplicationBundle().getString("Popups.EditCustomControl.popup_title"));

		duplicate = toEdit.newSpecification().constructNewControlClass(Project.getCurrentProject());
		duplicate.getControlClassUpdateGroup().addListener(new UpdateGroupListener<ControlClassUpdate>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, ControlClassUpdate data) {
				updates.add(data);
			}
		});

		getTaComment().setText(toEdit.getComment()); //set before control class to make sure preview is correct
		setToControlClass(duplicate);
		hideBaseControlMenuButton(true);
	}

	@Override
	protected void ok() {
		for (ControlClassUpdate update : updates) {
			toEdit.getControlClass().update(update, false);
		}
		toEdit.setComment(getTaComment().getText());
		close();
	}

}
