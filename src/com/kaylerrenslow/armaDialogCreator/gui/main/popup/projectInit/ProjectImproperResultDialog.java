package com.kaylerrenslow.armaDialogCreator.gui.main.popup.projectInit;

import com.kaylerrenslow.armaDialogCreator.data.xml.ParseError;
import com.kaylerrenslow.armaDialogCreator.data.xml.ProjectXmlLoader;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 Shown when the project successfully loaded, however there was errors.
 @author Kayler
 @since 11/23/2016 */
public class ProjectImproperResultDialog extends StageDialog<ScrollPane> {

	public ProjectImproperResultDialog(@NotNull ProjectXmlLoader.ProjectParseResult result) {
		super(ArmaDialogCreator.getPrimaryStage(), new ScrollPane(new VBox(15)), null, false, true, false);
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("ProjectResultErrorPopup.popup_title"));

		myRootElement.setFitToWidth(true);
		myRootElement.setFitToHeight(true);
		VBox root = (VBox) myRootElement.getContent();

		//swap out the dialog's root element padding for the vbox that is the root element of scrollpane
		root.setPadding(super.myRootElement.getPadding());
		super.myRootElement.setPadding(new Insets(0));

		root.getChildren().add(new Label(bundle.getString("ProjectResultErrorPopup.errors_title")));
		root.getChildren().add(new Separator(Orientation.HORIZONTAL));
		for (ParseError error : result.getErrors()) {
			VBox vbErrorMsg = new VBox(5);
			vbErrorMsg.getChildren().addAll(
					getLabel(bundle.getString("ProjectResultErrorPopup.error_message") + " " + error.getMessage(), null),
					getLabel(
							bundle.getString("ProjectResultErrorPopup.recovered"),
							getLabel(
									error.recovered() ? bundle.getString("ProjectResultErrorPopup.yes")
											: bundle.getString("ProjectResultErrorPopup.no"),
									null
							)
					)
			);
			if (error.recovered()) {
				vbErrorMsg.getChildren().add(getLabel(bundle.getString("ProjectResultErrorPopup.recover_message") + " " + error.getRecoverMessage(), null));
			}

			root.getChildren().add(vbErrorMsg);
		}
		myStage.setWidth(340d);
	}

	private Label getLabel(String text, Node graphic) {
		Label label = new Label(text, graphic);
		label.setContentDisplay(ContentDisplay.RIGHT);
		return label;
	}

}
