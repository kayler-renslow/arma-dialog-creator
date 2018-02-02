package com.kaylerrenslow.armaDialogCreator.installer;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.kaylerrenslow.armaDialogCreator.installer.ADCInstaller.bundle;

/**
 This installer window is used for when the user downloaded the installer.
 The installer should have the contents to install inside this class's jar, therefore a .zip file
 is unnecessary.

 @author kayler
 @since 4/17/17 */
class InstallerConfigWindow {

	private final Stage stage;
	private File installDir = new File("");
	private BooleanProperty closeInstallerBtnDisable;
	private final BooleanProperty installFailedProperty = new SimpleBooleanProperty(false);
	private final VBox vboxAfterHeader = new VBox(5);
	private final TextArea taDetails = new TextArea("");

	public InstallerConfigWindow(@NotNull Stage stage, @NotNull File initInstallDir) {
		this.stage = stage;
		stage.setTitle(bundle.getString("InstallerWindow.window_title"));
		stage.getIcons().addAll(new Image("/app.png"));

		this.installDir = initInstallDir;

		stage.setWidth(700);
		stage.setHeight(400);
		stage.setResizable(false);

		initPane(stage);
	}

	public void show() {
		stage.show();
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
			ImageView imageViewAdc = new ImageView(new Image("/com/kaylerrenslow/armaDialogCreator/installer/adc64.png"));
			imageViewAdc.setFitHeight(64);
			imageViewAdc.setFitWidth(64);
			hboxHeaderIconAndTitle.getChildren().add(imageViewAdc);
			{ //icon animation
				AnimationTimer timer = new AnimationTimer() {
					@Override
					public void handle(long now) {
						imageViewAdc.rotateProperty().set(imageViewAdc.rotateProperty().doubleValue() + .5);
					}
				};
				timer.start();
			}

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

		vboxBpCenter.getChildren().add(vboxAfterHeader);
		vboxAfterHeader.setPadding(padding);
		vboxAfterHeader.setFillWidth(true);

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
				installDir = chosen;
			});

			vboxAfterHeader.getChildren().add(new Label(bundle.getString("InstallerWindow.change_dir_if_needed")));
			vboxAfterHeader.getChildren().add(new HBox(5, tfDir, btnChange));
		}

		//footer of borderpane
		{
			Button btnClose = new Button(bundle.getString("InstallerWindow.close_installer"));
			btnClose.setOnAction((e) -> Platform.exit());
			closeInstallerBtnDisable = btnClose.disableProperty();

			Button btnInstall = new Button(bundle.getString("InstallerWindow.install"));
			btnInstall.setOnAction((e) -> {
				install();
				btnInstall.setDisable(true);
			});

			HBox hboxFooter = new HBox(10, btnClose, btnInstall);
			hboxFooter.setAlignment(Pos.CENTER_RIGHT);
			hboxFooter.setPadding(new Insets(5));

			root.setBottom(hboxFooter);
		}
	}

	private void setupInstallPane(@NotNull ADCInstallerTask task) {
		vboxAfterHeader.getChildren().clear();

		ProgressBar progress = new ProgressBar();
		Label lblError = new Label();
		vboxAfterHeader.getChildren().add(new HBox(5, progress, lblError));
		progress.progressProperty().bind(task.progressProperty());

		//Installer.install_failed_notify

		VBox vboxDetails = new VBox();
		ToggleButton btnShowDetails = new ToggleButton(bundle.getString("InstallerWindow.show_details"));
		btnShowDetails.setOnAction((e) -> {
			vboxDetails.getChildren().clear();
			if (btnShowDetails.isSelected()) {
				vboxDetails.getChildren().add(taDetails);
			}
		});
		installFailedProperty.addListener((observable, oldValue, newValue) -> lblError.setText(bundle.getString("Installer.install_failed_notify")));
		task.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateDetails(newValue);
			}
		});
		vboxAfterHeader.getChildren().add(btnShowDetails);
		vboxAfterHeader.getChildren().add(vboxDetails);
	}

	private void updateDetails(String msg) {
		msg = msg == null ? "" : msg;
		taDetails.appendText(msg + "\n");
	}

	private void install() {
		ADCInstallerTask installTask = new ADCInstallerTask(new InstallPackage.JarInstallPackage(), installDir);

		setupInstallPane(installTask);

		closeInstallerBtnDisable.setValue(true);

		installTask.setOnCancelled((e) -> {
			installFail();
		});
		installTask.setOnSucceeded(event -> {
			installSucceed();
		});
		installTask.setOnFailed((e) -> {
			installFail();
		});
		installTask.exceptionProperty().addListener(new ChangeListener<Throwable>() {
			@Override
			public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
				updateDetails("!!!! " + ADCInstaller.getExceptionString(newValue));
				installFail();
			}
		});

		try {
			new Thread(installTask).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void installFail() {
		enableCloseInstallButton();
		installFailedProperty.setValue(true);
	}

	private void installSucceed() {
		enableCloseInstallButton();
	}

	private void enableCloseInstallButton() {
		closeInstallerBtnDisable.setValue(false);
	}

	@NotNull
	public File getInstallDir() {
		return installDir;
	}
}
