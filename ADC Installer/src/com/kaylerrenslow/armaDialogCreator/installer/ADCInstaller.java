package com.kaylerrenslow.armaDialogCreator.installer;

import com.kaylerrenslow.armaDialogCreator.pwindow.ADCStandaloneProgressWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 @author kayler
 @since 4/10/17 */
public class ADCInstaller extends Application {
	static final ResourceBundle bundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.installer.InstallBundle");

	@Override
	public void start(Stage primaryStage) throws Exception {
		InstallerConfigWindow window = new InstallerConfigWindow();
		primaryStage.show();
	}

	private static class InstallerConfigWindow {

	}

	private static class InstallerProgressWindow extends ADCStandaloneProgressWindow{

		public InstallerProgressWindow(@NotNull Stage stage) {
			super(stage);
		}
	}
}
