package com.kaylerrenslow.armaDialogCreator.installer;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
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
		File f = new File(System.getProperty("user.home") + "/Arma Dialog Creator");
		if (!f.exists()) {
			f.mkdirs();
		} else if (f.exists() && !f.isDirectory()) {
			f = new File(System.getProperty("user.home"));
		}
		if (this.getParameters().getUnnamed().contains("-nocfg")) { //install where installer is and dont use config window
			InstallerProgressWindow pw = new InstallerProgressWindow(primaryStage);
			primaryStage.show();
			pw.runInstall(f);
		} else {
			InstallerConfigWindow window = new InstallerConfigWindow(primaryStage, f);
			primaryStage.show();
		}
	}

	@NotNull
	public static String getExceptionString(@NotNull Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}

}
