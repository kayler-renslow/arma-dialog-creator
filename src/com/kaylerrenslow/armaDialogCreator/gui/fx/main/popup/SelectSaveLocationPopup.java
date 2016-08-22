/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaTools;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.HelpUrls;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 @author Kayler
 Used for setting application save directory {@link ApplicationDataManager#getAppSaveDataDirectory()} and Arma 3 Tools directory {@link ApplicationDataManager#getArma3ToolsDirectory()}<br>
 The application save directory is not allowed to be empty/not set, however, the Arma 3 Tools directory can be not set. All changes will be set inside the popup.
 Created on 05/26/2016. */
public class SelectSaveLocationPopup extends StageDialog<VBox> {

	private final TextField tfAppDataSaveDir = new TextField();
	private final TextField tfA3ToolsDir = new TextField();

	private BadArma3ToolsDirectoryPopup badArma3ToolsDirectoryPopup;

	/**
	 Creates the "change directories" popup
	 */
	public SelectSaveLocationPopup(@Nullable File initialDirectoryAppDataSave, @Nullable File a3ToolsDir) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.Popups.SelectSaveLocation.POPUP_TITLE, true, true, true);
		initialize(initialDirectoryAppDataSave, a3ToolsDir);
		myStage.setMinWidth(600d);
		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.initStyle(StageStyle.UTILITY);
	}

	private void initialize(@Nullable File initialAppSaveDirectory, @Nullable File a3ToolsDir) {
		tfA3ToolsDir.setEditable(false);
		tfAppDataSaveDir.setEditable(false);

		if (a3ToolsDir != null) {
			tfA3ToolsDir.setText(a3ToolsDir.getPath());
		}
		if (initialAppSaveDirectory != null) {
			tfAppDataSaveDir.setText(initialAppSaveDirectory.getPath());
		}

		Label lblAppDataSaveDir = new Label(Lang.Popups.SelectSaveLocation.LBL_APP_DATA_SAVE_DIR);
		Label lblA3ToolsDir = new Label(Lang.Popups.SelectSaveLocation.LBL_A3_TOOLS_DIR);


		final Button btnChangeAppData = new Button(Lang.Popups.SelectSaveLocation.BTN_CHANGE);
		final Button btnChangeA3Tools = new Button(Lang.Popups.SelectSaveLocation.BTN_CHANGE);
		final Button btnClearA3ToolsDir = new Button(Lang.Popups.SelectSaveLocation.BTN_CLEAR);
		
		/*set events*/
		btnChangeAppData.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser dc = new DirectoryChooser();
				dc.setInitialDirectory(initialAppSaveDirectory);
				dc.setTitle(lblAppDataSaveDir.getText());
				File f = dc.showDialog(myStage);
				if (f == null) {
					return;
				}
				chooseAppDataSaveDir(f);
			}
		});
		btnChangeA3Tools.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser dc = new DirectoryChooser();
				dc.setInitialDirectory(a3ToolsDir);
				dc.setTitle(lblA3ToolsDir.getText());
				File f = dc.showDialog(myStage);
				if (f == null) {
					return;
				}
				if (ArmaTools.isValidA3ToolsDirectory(f)) {
					chooseA3ToolsSaveDir(f);
				} else {
					if (badArma3ToolsDirectoryPopup == null) {
						badArma3ToolsDirectoryPopup = new BadArma3ToolsDirectoryPopup();
					}
					badArma3ToolsDirectoryPopup.show();
				}
			}
		});
		btnClearA3ToolsDir.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				chooseA3ToolsSaveDir(null);
			}
		});


		HBox hbTop = new HBox(5);
		hbTop.getChildren().addAll(tfAppDataSaveDir, btnChangeAppData);
		HBox.setHgrow(tfAppDataSaveDir, Priority.ALWAYS);

		HBox hbMid = new HBox(5);
		hbMid.getChildren().addAll(tfA3ToolsDir, btnChangeA3Tools, btnClearA3ToolsDir);
		HBox.setHgrow(tfA3ToolsDir, Priority.ALWAYS);


		myRootElement.getChildren().addAll(lblAppDataSaveDir, hbTop, lblA3ToolsDir, hbMid);
		myStage.setResizable(false);
	}

	private void chooseA3ToolsSaveDir(@Nullable File f) {
		tfA3ToolsDir.setText(f != null ? f.getPath() : "");
	}

	private void chooseAppDataSaveDir(@NotNull File f) {
		tfAppDataSaveDir.setText(f.getPath());
	}

	@Nullable
	public String getApplicationDataSaveLocationPath() {
		String s = tfAppDataSaveDir.getText();
		if (s.length() == 0) {
			return null;
		}
		return tfAppDataSaveDir.getText();
	}

	@Nullable
	public String getArma3ToolsDirectoryPath() {
		String s = tfA3ToolsDir.getText();
		if (s.length() == 0) {
			return null;
		}
		return s;
	}

	@Override
	protected void ok() {
		String appSaveDataLocation = appSaveDataLocation();
		if (appSaveDataLocation == null) {
			return;
		}
		String a3tools = getArma3ToolsDirectoryPath();
		ArmaDialogCreator.getApplicationDataManager().setAppSaveDataLocation(new File(appSaveDataLocation));
		if (a3tools != null) {
			ArmaDialogCreator.getApplicationDataManager().setArma3ToolsLocation(new File(a3tools));
		} else {
			ArmaDialogCreator.getApplicationDataManager().setArma3ToolsLocation(null);
		}
		ArmaDialogCreator.getApplicationDataManager().saveApplicationProperties();

		close();
	}

	@Override
	protected void cancel() {
		if (appSaveDataLocation() == null) {
			return;
		}
		super.cancel();
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.CONFIGURE_DIRECTORIES_POPUP);
	}

	@Nullable
	private String appSaveDataLocation() {
		String appSaveDataLocation = getApplicationDataSaveLocationPath();
		if (appSaveDataLocation == null) {
			beepFocus();
			tfAppDataSaveDir.requestFocus();
			return null;
		}
		return appSaveDataLocation;
	}


	@Override
	protected void onCloseRequest(WindowEvent event) {
		if (appSaveDataLocation() == null) {
			event.consume();
		}
	}

	private static class BadArma3ToolsDirectoryPopup extends StageDialog<VBox> {

		public BadArma3ToolsDirectoryPopup() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5, new Label(Lang.Popups.SelectSaveLocation.BAD_A3_TOOLS_DIR)), Lang.Popups.GENERIC_POPUP_TITLE, false, true, false);
			myStage.setWidth(300d);
		}
	}
}
