package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.KnownLanguage;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.SearchTextField;
import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImages;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 @author Kayler
 @since 12/14/2016 */
public class StringTableEditorPopup extends StagePopup<VBox> {

	public StringTableEditorPopup(@NotNull StringTable table) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ApplicationBundle().getString("Popups.StringTable.popup_title"));

		StringTableEditorTabPane tabPane = new StringTableEditorTabPane(table);
		myRootElement.getChildren().add(tabPane);
		VBox.setVgrow(tabPane, Priority.ALWAYS);

		setStageSize(720, 480);

	}

	private static class StringTableEditorTabPane extends TabPane {
		private final ValueObserver<Language> previewLanguageObserver = new ValueObserver<>(KnownLanguage.Original);

		public StringTableEditorTabPane(@NotNull StringTable table) {
			getTabs().add(new EditTab(table, previewLanguageObserver));
			getTabs().add(new ConfigTab(table, previewLanguageObserver));
		}
	}

	private static class ConfigTab extends Tab { //set xml things like project name attribute (project=root tag of stringtable.xml)

		public ConfigTab(@NotNull StringTable table, @NotNull ValueObserver<Language> previewLanguageObserver) {
			super(Lang.ApplicationBundle().getString("Popups.StringTable.Tab.Config.tab_title"));
			VBox root = new VBox(10);
			root.setPadding(new Insets(10));
			root.setFillWidth(true);
			setContent(root);
			setGraphic(new ImageView(ADCImages.ICON_GEAR));
			setClosable(false);

			ResourceBundle bundle = Lang.ApplicationBundle();

			ComboBox<Language> comboBoxLanguage = new ComboBox<>(FXCollections.observableArrayList(KnownLanguage.values()));
			comboBoxLanguage.getSelectionModel().select(previewLanguageObserver.getValue());
			comboBoxLanguage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Language>() {
				@Override
				public void changed(ObservableValue<? extends Language> observable, Language oldValue, Language newValue) {
					previewLanguageObserver.updateValue(newValue);
				}
			});
			Label lblPreviewLanguage = new Label(bundle.getString("Popups.StringTable.Tab.Config.preview_language"), comboBoxLanguage);
			lblPreviewLanguage.setContentDisplay(ContentDisplay.RIGHT);
			root.getChildren().add(lblPreviewLanguage);

		}
	}

	private static class EditTab extends Tab {
		private final ObservableList<StringTableKeyDescriptor> listViewItems;

		private final List<StringTableKeyDescriptor> allItems = new LinkedList<>();
		private final ListView<StringTableKeyDescriptor> lvMatch = new ListView<>();
		private final StringTableKeyEditorPane editorPane;


		public EditTab(@NotNull StringTable table, @NotNull ValueObserver<Language> previewLanguageObserver) {
			super(Lang.ApplicationBundle().getString("Popups.StringTable.Tab.Edit.tab_title"));

			listViewItems = FXCollections.observableList(new ArrayList<>(), new Callback<StringTableKeyDescriptor, javafx.beans
					.Observable[]>() {

				public javafx.beans.Observable[] call(StringTableKeyDescriptor param) {
					return new javafx.beans.Observable[]{
							param.getKey().getValue().getLanguageTokenMap(),
							param.getKey().idObserver(),
							previewLanguageObserver,
							param.getKey().containerNameObserver(),
							param.getKey().packageNameObserver()
					};
				}
			}); //for some reason, can't have a LinkedList as the underlying list implementation if we want the list view to update the displayed cell text automatically

			previewLanguageObserver.addListener(new ValueListener<Language>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language newValue) {
					for (StringTableKeyDescriptor descriptor : allItems) {
						descriptor.setPreviewLanguage(newValue);
					}
				}
			});

			editorPane = new StringTableKeyEditorPane(previewLanguageObserver);

			setClosable(false);
			ResourceBundle bundle = Lang.ApplicationBundle();

			lvMatch.setPlaceholder(new Label(bundle.getString("Popups.StringTable.Tab.Edit.Search.no_match")));
			lvMatch.setStyle("-fx-font-family:monospace");
			final String noPackageName = bundle.getString("Popups.StringTable.no_package");
			final String noContainerName = bundle.getString("Popups.StringTable.no_container");
			for (StringTableKey key : table.getKeys()) {
				StringTableKeyDescriptor descriptor = new StringTableKeyDescriptor(key, noPackageName, noContainerName);
				descriptor.setPreviewLanguage(previewLanguageObserver.getValue());
				allItems.add(descriptor);
				listViewItems.add(descriptor);
			}
			final Comparator<StringTableKeyDescriptor> comparator = new Comparator<StringTableKeyDescriptor>() {
				@Override
				public int compare(StringTableKeyDescriptor o1, StringTableKeyDescriptor o2) {
					return o1.getKey().getId().compareToIgnoreCase(o2.getKey().getId());
				}
			};
			listViewItems.sort(comparator);
			allItems.sort(comparator);
			lvMatch.setItems(listViewItems);
			lvMatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StringTableKeyDescriptor>() {
				@Override
				public void changed(ObservableValue<? extends StringTableKeyDescriptor> observable, StringTableKeyDescriptor oldValue, StringTableKeyDescriptor selected) {
					if (selected != null) {
						editorPane.setKey(selected.getKey());
					} else {
						editorPane.setKey(null);
					}
				}
			});

			SearchTextField tfSearch = new StringTableSearchField(lvMatch, allItems);

			VBox vbRoot = new VBox(10, tfSearch, editorPane, lvMatch);
			VBox.setVgrow(lvMatch, Priority.ALWAYS);
			vbRoot.setFillWidth(true);
			vbRoot.setPadding(new Insets(10));
			setContent(vbRoot);

		}


	}


}
