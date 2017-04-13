package com.kaylerrenslow.armaDialogCreator.installer;

import com.kaylerrenslow.armaDialogCreator.pwindow.ADCStandaloneProgressWindow;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

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
		if (this.getParameters().getUnnamed().contains("-here")) {
			InstallerProgressWindow pw = new InstallerProgressWindow(primaryStage);
			primaryStage.show();
			pw.runInstall(f);
		} else {
			InstallerConfigWindow window = new InstallerConfigWindow(primaryStage, f);
			primaryStage.show();
		}

	}

	private static class InstallerConfigWindow {

		private File installDir = new File("");

		public InstallerConfigWindow(@NotNull Stage stage, @NotNull File initInstallDir) {
			stage.setTitle(bundle.getString("InstallerWindow.title"));
			stage.getIcons().addAll(new Image("/com/kaylerrenslow/armaDialogCreator/pwindow/app.png"));

			initPane(stage);
		}

		private void initPane(Stage stage) {
			BorderPane root = new BorderPane();
			stage.setScene(new Scene(root));

			VBox vboxCenter = new VBox();

			/*Header stuff*/
			StackPane stackPaneHeader = new StackPane();
			HBox hboxHeader = new HBox(5);
			ImageView imageViewAdc = new ImageView(new Image("/com/kaylerrenslow/armaDialogCreator/installer/adc256.png"));
			imageViewAdc.setFitHeight(64);
			imageViewAdc.setFitWidth(64);
			hboxHeader.getChildren().add(imageViewAdc);

			VBox vboxHeaderTitle = new VBox();
			HBox.setHgrow(vboxHeaderTitle, Priority.ALWAYS);
			Label lblHeaderTitle = new Label(bundle.getString("InstallerWindow.title"));
			lblHeaderTitle.setFont(Font.font(24));
			vboxHeaderTitle.getChildren().add(lblHeaderTitle);


			vboxCenter.getChildren().add(stackPaneHeader);
			vboxCenter.getChildren().add(new Separator(Orientation.HORIZONTAL));

			/*stuff after header*/
		}
	}

	private static class InstallerProgressWindow extends ADCStandaloneProgressWindow {

		public InstallerProgressWindow(@NotNull Stage stage) {
			super(stage);
		}


		public void runInstall(@NotNull File installDir) {

		}
	}
}
