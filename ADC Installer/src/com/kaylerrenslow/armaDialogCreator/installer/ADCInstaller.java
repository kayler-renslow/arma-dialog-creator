package com.kaylerrenslow.armaDialogCreator.installer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.util.ResourceBundle;

/**
 @author kayler
 @since 4/10/17 */
public class ADCInstaller extends Application {
	static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.installer.InstallBundle");

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		File f = new File(".");
		if (this.getParameters().getUnnamed().contains("-z")) {
			String zipFilePath = this.getParameters().getNamed().get("f");
			if (zipFilePath == null) {
				System.out.println("f is missing. Expected --f=zipFile");
				Platform.exit();
				return;
			}
			File zipFile = new File(zipFilePath);

			InstallerProgressWindow pw = new InstallerProgressWindow(primaryStage);
			primaryStage.show();
			pw.runInstall(zipFile, f);
		} else {
			InstallerConfigWindow window = new InstallerConfigWindow(primaryStage, f);
			primaryStage.show();
		}

	}

}
