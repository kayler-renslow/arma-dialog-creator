package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.ControlTypeGroup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.BorderedImageView;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.CBMBGroupMenu;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.CBMBMenuItem;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.ComboBoxMenuButton;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.IdentifierChecker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.HelpUrls;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
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
			for (ControlType controlType : ControlType.BETA_SUPPORTED) {
				if (group != controlType.getGroup()) {
					continue;
				}
				groupMenu.getCbmbMenuItems().add(new CBMBMenuItem<>(controlType, new BorderedImageView(controlType.getIcon())));
			}
			if (groupMenu.getItems().size() > 0) {
				menuButtonControlType.addGroup(groupMenu);
			}
		}

		myRootElement.addRow(0, new Label(Lang.ApplicationBundle().getString("Popups.NewControl.class_name")), inClassName);
		myRootElement.addRow(1, new Label(Lang.ApplicationBundle().getString("Popups.NewControl.control_type")), menuButtonControlType);
		myRootElement.addRow(2, new Label(Lang.ApplicationBundle().getString("Popups.NewControl.background_control")), checkBoxBackgroundControl);

		myRootElement.setVgap(10);
		myRootElement.setHgap(10);

		myStage.setResizable(false);

		menuButtonControlType.setMinWidth(250);
		if (fixedType == null) {
			menuButtonControlType.chooseItem(ControlType.STATIC);
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
