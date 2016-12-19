package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.DefaultStringTableXmlParser;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.stringtable.StringTableEditorPopup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.File;

/**

 Implementation varies. Used for debugging/testing specific features
 @author Kayler
 @since 09/14/2016. */
public class TestAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent event) {
		new StagePopup<VBox>(ArmaDialogCreator.getPrimaryStage(), new VBox(5), "Debug Menu") {
			@Override
			public void show() {
				myRootElement.setPadding(new Insets(10));
				myRootElement.getChildren().addAll(
						new ButtonWithAction("String Table Test", new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								try {
									StringTable table = new DefaultStringTableXmlParser(
											new File("D:\\Archive\\Intellij Files\\Arma Tools\\Arma Dialog Creator\\tests\\com\\kaylerrenslow\\armaDialogCreator\\data\\io\\xml\\stringtable.xml"))
											.createStringTableInstance();
									new StringTableEditorPopup(table).show();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						})
				);
				myRootElement.setPrefSize(720, 480);
				super.show();
			}
		}.show();


	}

	private static class ButtonWithAction extends Button {
		public ButtonWithAction(String text, EventHandler<ActionEvent> event) {
			super(text);
			setOnAction(event);
		}

		public ButtonWithAction(String text, Node graphic, EventHandler<ActionEvent> event) {
			super(text, graphic);
			setOnAction(event);
		}
	}
}
