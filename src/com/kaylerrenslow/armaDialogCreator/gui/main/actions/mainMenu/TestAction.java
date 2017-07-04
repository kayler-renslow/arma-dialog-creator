package com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.util.StructuredTextParseException;
import com.kaylerrenslow.armaDialogCreator.arma.util.StructuredTextParser;
import com.kaylerrenslow.armaDialogCreator.arma.util.TextSection;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.xml.DefaultStringTableXmlParser;
import com.kaylerrenslow.armaDialogCreator.data.xml.StringTableXmlWriter;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.InputDialog;
import com.kaylerrenslow.armaDialogCreator.gui.main.stringtable.StringTableEditorPopup;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

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
								File f = new StringTableSelector().chosen;
								if (f == null) {
									return;
								}
								try {
									StringTable table = new DefaultStringTableXmlParser(f).createStringTableInstance();
									new StringTableEditorPopup(table, new StringTableXmlWriter(), new DefaultStringTableXmlParser(f)).show();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}),
						new ButtonWithAction("Set Project String Table", new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								File f = new StringTableSelector().chosen;
								if (f == null) {
									return;
								}
								try {
									StringTable table = new DefaultStringTableXmlParser(f).createStringTableInstance();
									Project.getCurrentProject().setStringTable(table);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						})
				);
				myRootElement.getChildren().add(new ButtonWithAction("Structured Text Test", event1 -> {
					ComboBox<String> comboBox = new ComboBox<>();
					comboBox.getItems().addAll(
							"<t shadow='1'><t color='#ff0000'>Red text with shadow</t> text with just shadow</t>",
							"<t shadow='1'>text with just shadow</t>",
							"text"
					);
					comboBox.setEditable(true);
					comboBox.setValue("");
					InputDialog<ComboBox<String>> dialog = new InputDialog<>("Enter structured text", "text:", comboBox);

					dialog.show();
					if (dialog.wasCancelled()) {
						return;
					}
					StructuredTextParser p = new StructuredTextParser(comboBox.getValue());
					try {
						List<TextSection> sections = p.parse();
						System.out.println(sections);
					} catch (StructuredTextParseException e) {
						ExceptionHandler.error(e);
					}
				}));

				myRootElement.setPrefSize(720, 480);
				super.show();
			}
		}.show();


	}

	private static class StringTableSelector {
		private File chosen;

		public StringTableSelector() {
			ListView<File> listView = new ListView<>();
			StageDialog<VBox> dialog = new StageDialog<VBox>(null, new VBox(5), "", false, true, false) {
				@Override
				public void show() {
					myRootElement.getChildren().add(listView);
					listView.getItems().add(new File("D:\\My Documents\\Arma 3 - Other Profiles\\K-Town\\missions\\altisLife.Altis\\stringtable.xml"));
					listView.getItems().add(new File("tests/com/kaylerrenslow/armaDialogCreator/data/xml/stringtable.xml"));
					listView.getItems().add(new File("D:\\DATA\\Steam\\steamapps\\common\\Arma 3\\Addons\\languagemissions_f_epa\\stringtable.xml"));
					super.show();
				}
			};
			dialog.show();
			chosen = listView.getSelectionModel().getSelectedItem();
		}
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
