package com.kaylerrenslow.armaDialogCreator.installer;

import com.kaylerrenslow.armaDialogCreator.pwindow.ADCStandaloneProgressWindow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
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
			stage.setTitle(bundle.getString("InstallerWindow.window_title"));
			stage.getIcons().addAll(new Image("/com/kaylerrenslow/armaDialogCreator/pwindow/app.png"));

			this.installDir = initInstallDir;

			stage.setWidth(700);
			stage.setHeight(400);
			stage.setResizable(false);

			initPane(stage);
		}

		private void initPane(Stage stage) {
			BorderPane root = new BorderPane();
			stage.setScene(new Scene(root));

			final Insets padding = new Insets(15);

			VBox vboxBpCenter = new VBox();
			root.setCenter(vboxBpCenter);

			//header stuff
			{
				StackPane stackPaneHeaderContainer = new StackPane(); //contains hbox that contains header title/subtitle and icon
				stackPaneHeaderContainer.setPadding(padding);
				stackPaneHeaderContainer.setStyle("-fx-background-color:linear-gradient(from 50% 30% to 100% 100%, #ffffff, #e7e7e7)");
				HBox hboxHeaderIconAndTitle = new HBox(5);//contains adc icon and title and subtitle
				stackPaneHeaderContainer.getChildren().add(hboxHeaderIconAndTitle);

				//add adc icon
				ImageView imageViewAdc = new ImageView(new Image("/com/kaylerrenslow/armaDialogCreator/installer/adc256.png"));
				imageViewAdc.setFitHeight(64);
				imageViewAdc.setFitWidth(64);
				hboxHeaderIconAndTitle.getChildren().add(imageViewAdc);

				//title and sub title stuff
				{
					VBox vboxHeaderTitles = new VBox(); //contains title and sub title

					//adc installer title
					HBox.setHgrow(vboxHeaderTitles, Priority.ALWAYS);
					Label lblHeaderTitle = new Label(bundle.getString("InstallerWindow.title"));
					lblHeaderTitle.setFont(Font.font(24));
					vboxHeaderTitles.getChildren().add(lblHeaderTitle);

					//adc installer subtitle (created with <3 ...)
					HBox hboxSubtitle = new HBox(2);
					Label lblCreatedWith = new Label(bundle.getString("InstallerWindow.created_with"));
					Label lblByK = new Label(bundle.getString("InstallerWindow.by_k"));
					ImageView imageViewHeart = new ImageView("/com/kaylerrenslow/armaDialogCreator/installer/heart.png");
					imageViewHeart.setFitHeight(16);
					imageViewHeart.setFitWidth(16);
					hboxSubtitle.getChildren().addAll(lblCreatedWith, imageViewHeart, lblByK);
					vboxHeaderTitles.getChildren().add(hboxSubtitle);

					hboxHeaderIconAndTitle.getChildren().add(vboxHeaderTitles);
				}
				vboxBpCenter.getChildren().add(stackPaneHeaderContainer);
			}

			vboxBpCenter.getChildren().add(new Separator(Orientation.HORIZONTAL));

			VBox vboxAfterHeader = new VBox(5);
			vboxBpCenter.getChildren().add(vboxAfterHeader);
			vboxAfterHeader.setPadding(padding);

			//options for install
			{
				TextField tfDir = new TextField(installDir.getAbsolutePath());
				tfDir.setEditable(false);
				HBox.setHgrow(tfDir, Priority.ALWAYS);

				Button btnChange = new Button(bundle.getString("InstallerWindow.choose_install_loc"));
				btnChange.setOnAction((e) -> {
					DirectoryChooser dc = new DirectoryChooser();
					dc.setInitialDirectory(installDir);
					dc.setTitle(btnChange.getText());
					File chosen = dc.showDialog(stage);
					if (chosen == null) {
						return;
					}
					tfDir.setText(chosen.getAbsolutePath());
				});

				vboxAfterHeader.getChildren().add(new Label(bundle.getString("InstallerWindow.change_dir_if_needed")));
				vboxAfterHeader.getChildren().add(new HBox(5, tfDir, btnChange));
			}

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
