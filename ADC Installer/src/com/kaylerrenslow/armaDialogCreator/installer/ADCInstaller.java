package com.kaylerrenslow.armaDialogCreator.installer;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
		try {
			Class c = Class.forName("javafx.application.Application");
		} catch (ClassNotFoundException e) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(null, "JavaFX 8 isn't installed on this computer. Please install it to run Arma Dialog Creator.", "JavaFX 8 Missing", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			});
			return;
		}

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
			window.show();
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
