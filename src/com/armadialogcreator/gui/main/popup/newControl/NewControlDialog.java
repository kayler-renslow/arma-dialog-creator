package com.armadialogcreator.gui.main.popup.newControl;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.HelpUrls;
import com.armadialogcreator.core.old.ControlType;
import com.armadialogcreator.core.ControlTypeGroup;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.fxcontrol.BorderedImageView;
import com.armadialogcreator.gui.fxcontrol.CBMBGroupMenu;
import com.armadialogcreator.gui.fxcontrol.CBMBMenuItem;
import com.armadialogcreator.gui.fxcontrol.ComboBoxMenuButton;
import com.armadialogcreator.gui.fxcontrol.inputfield.IdentifierChecker;
import com.armadialogcreator.gui.fxcontrol.inputfield.InputField;
import com.armadialogcreator.gui.main.BrowserUtil;
import com.armadialogcreator.lang.Lang;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Popup used for prompting the user to enter a control class name, {@link ControlType}, and if the control is a background control or not.

 @author Kayler
 @since 11/15/2016 */
public class NewControlDialog extends StageDialog<GridPane> {
	private final InputField<IdentifierChecker, String> inClassName = new InputField<>(new IdentifierChecker());
	private final ComboBoxMenuButton<ControlType> menuButtonControlType;
	private final CheckBox checkBoxBackgroundControl = new CheckBox();

	/**
	 Constructs a dialog prompting the user to give control class name, {@link ControlType}, and if the control is a background control or not

	 @param fixedType if null, the user can't specify the control type. If not null, the type will be fixed and the user won't be able to edit it
	 @param fixedIsBackgroundControl if null, the user can't specify the control type. If not null, the user can specify if the control is a background one or not
	 */
	public NewControlDialog(@Nullable ControlType fixedType, @Nullable Boolean fixedIsBackgroundControl) {
		super(ArmaDialogCreator.getPrimaryStage(), new GridPane(), Lang.ApplicationBundle().getString("Popups.NewControl.popup_title"), true, true, true);

		menuButtonControlType = new ComboBoxMenuButton<>(false, "", null);

		for (ControlTypeGroup group : ControlTypeGroup.values()) {
			CBMBGroupMenu<ControlType> groupMenu = new CBMBGroupMenu<>(group.getDisplayName());
			for (ControlType controlType : ControlType.AVAILABLE_TYPES) {
				if (group != controlType.getGroup()) {
					continue;
				}
				groupMenu.getCBMBMenuItems().add(new CBMBMenuItem<>(controlType, new BorderedImageView(controlType.getIcon())));
			}
			if (groupMenu.getItems().size() > 0) {
				menuButtonControlType.addGroup(groupMenu);
			}
		}

		myRootElement.addRow(0, new Label(Lang.ApplicationBundle().getString("Popups.NewControl.class_name")), inClassName);
		inClassName.setValue("Control" + inClassName.hashCode());
		myRootElement.addRow(1, new Label(Lang.ApplicationBundle().getString("Popups.NewControl.control_type")), menuButtonControlType);
		myRootElement.addRow(2, new Label(Lang.ApplicationBundle().getString("Popups.NewControl.background_control")), checkBoxBackgroundControl);

		myRootElement.setVgap(10);
		myRootElement.setHgap(10);

		myStage.setResizable(false);

		menuButtonControlType.setMinWidth(250);
		if (fixedType == null) {
			menuButtonControlType.chooseItem(ControlType.Static);
		} else {
			menuButtonControlType.chooseItem(fixedType);
			menuButtonControlType.setDisable(true);
		}
		if (fixedIsBackgroundControl != null) {
			checkBoxBackgroundControl.setSelected(fixedIsBackgroundControl);
			checkBoxBackgroundControl.setDisable(true);
		}

		inClassName.setToButton(false);
	}

	@Override
	protected void ok() {
		if (inClassName.getValue() == null) {
			inClassName.requestFocus();
			beep();
			return;
		}
		if (menuButtonControlType.getSelectedValueObserver().getValue() == null) {
			menuButtonControlType.requestFocus();
			beep();
			return;
		}
		super.ok();
	}

	@NotNull
	public String getClassName() {
		return inClassName.getValue();
	}

	@NotNull
	public ControlType getControlType() {
		return menuButtonControlType.getSelectedValueObserver().getValue();
	}

	public boolean isBackgroundControl() {
		return checkBoxBackgroundControl.isSelected();
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.NEW_CONTROL_POPUP);
	}


}
