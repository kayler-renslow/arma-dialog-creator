package com.kaylerrenslow.armaDialogCreator.gui.main.stringtable;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.*;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.SearchTextField;
import com.kaylerrenslow.armaDialogCreator.gui.img.ADCImages;
import com.kaylerrenslow.armaDialogCreator.gui.popup.SimpleResponseDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

/**
 @author Kayler
 @since 12/14/2016 */
public class StringTableEditorPopup extends StagePopup<VBox> {

	private final StringTableEditorTabPane tabPane;

	/** String to be used for when {@link StringTableKey#getPackageName()}==null */
	private final String noPackageName,
	/** String to be used for when {@link StringTableKey#getContainerName()}==null */
	noContainerName;

	private StringTable table;

	public StringTableEditorPopup(@NotNull StringTable table, @NotNull StringTableWriter writer, @NotNull StringTableParser parser) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(0), Lang.ApplicationBundle().getString("Popups.StringTable.popup_title"));
		this.table = table;
		ResourceBundle bundle = Lang.ApplicationBundle();

		noPackageName = bundle.getString("Popups.StringTable.no_package");
		noContainerName = bundle.getString("Popups.StringTable.no_container");

		Button btnInsert = new Button("", new ImageView(ADCImages.ICON_PLUS));
		btnInsert.setTooltip(new Tooltip(bundle.getString("Popups.StringTable.Tab.Edit.insert_key_tooltip")));
		btnInsert.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				NewStringTableKeyDialog dialog = new NewStringTableKeyDialog(getTable());
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				EditTab editTab = tabPane.getEditTab();
				StringTableKeyDescriptor newKey = editTab.addNewKey(dialog.getKey());
				getTable().getKeys().add(dialog.getKey());
				editTab.getListView().getSelectionModel().select(newKey);
				editTab.getListView().scrollTo(newKey);
			}
		});
		Button btnRemove = new Button("", new ImageView(ADCImages.ICON_MINUS));
		btnRemove.setTooltip(new Tooltip(bundle.getString("Popups.StringTable.Tab.Edit.remove_key_tooltip")));
		btnRemove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ListView<StringTableKeyDescriptor> listView = tabPane.getEditTab().getListView();
				StringTableKeyDescriptor selected = listView.getSelectionModel().getSelectedItem();
				if (selected == null) {
					return;
				}
				SimpleResponseDialog dialog = new SimpleResponseDialog(
						ArmaDialogCreator.getPrimaryStage(),
						btnRemove.getTooltip().getText(),
						String.format(bundle.getString("Popups.StringTable.Tab.Edit.remove_key_popup_body_f"), selected.getKey().getId()),
						true, true, false
				);
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				tabPane.getEditTab().removeKey(selected);
			}
		});

		tabPane = new StringTableEditorTabPane(table, btnRemove.disableProperty(), this);

		btnRemove.setDisable(tabPane.getEditTab().getListView().getSelectionModel().isEmpty());
		Button btnRefresh = new Button("", new ImageView(ADCImages.ICON_REFRESH));
		btnRefresh.setTooltip(new Tooltip(bundle.getString("Popups.StringTable.ToolBar.reload_tooltip")));
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SimpleResponseDialog dialog = new SimpleResponseDialog(
						ArmaDialogCreator.getPrimaryStage(),
						bundle.getString("Popups.StringTable.ToolBar.reload_popup_title"),
						bundle.getString("Popups.StringTable.ToolBar.reload_popup_body"),
						true, true, false
				);
				dialog.show();
				if (dialog.wasCancelled()) {
					return;
				}
				try {
					setTable(parser.createStringTableInstance());
					tabPane.setToTable(getTable());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		Button btnSave = new Button("", new ImageView(ADCImages.ICON_SAVE));
		btnSave.setTooltip(new Tooltip(bundle.getString("Popups.StringTable.ToolBar.save_tooltip")));
		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					writer.writeTable(getTable());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		btnRemove.disabledProperty();
		myRootElement.getChildren().add(new ToolBar(btnRefresh, btnSave, new Separator(Orientation.VERTICAL), btnInsert, btnRemove));
		myRootElement.getChildren().add(tabPane);
		VBox.setVgrow(tabPane, Priority.ALWAYS);

		setStageSize(720, 480);

	}

	private void setTable(StringTable table) {
		this.table = table;
	}

	private StringTable getTable() {
		return table;
	}

	private static class StringTableEditorTabPane extends TabPane {
		private final ValueObserver<Language> previewLanguageObserver = new ValueObserver<>(KnownLanguage.Original);
		private final BooleanProperty disableRemove;
		private final StringTableEditorPopup editorPopup;
		private EditTab editTab;

		public StringTableEditorTabPane(@NotNull StringTable table, @NotNull BooleanProperty disableRemove, @NotNull StringTableEditorPopup editorPopup) {
			this.disableRemove = disableRemove;
			this.editorPopup = editorPopup;
			setToTable(table);
		}

		public void setToTable(@NotNull StringTable table) {
			getTabs().clear();
			editTab = new EditTab(table, previewLanguageObserver, editorPopup);
			editTab.getListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StringTableKeyDescriptor>() {
				@Override
				public void changed(ObservableValue<? extends StringTableKeyDescriptor> observable, StringTableKeyDescriptor oldValue, StringTableKeyDescriptor selected) {
					disableRemove.setValue(selected == null);
				}
			});
			getTabs().add(editTab);
			getTabs().add(new ConfigTab(table, previewLanguageObserver));
		}

		@NotNull
		public EditTab getEditTab() {
			return editTab;
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
		private static final Comparator<StringTableKeyDescriptor> comparator = new Comparator<StringTableKeyDescriptor>() {
			@Override
			public int compare(StringTableKeyDescriptor o1, StringTableKeyDescriptor o2) {
				return o1.getKey().getId().compareToIgnoreCase(o2.getKey().getId());
			}
		};
		;
		private final ObservableList<StringTableKeyDescriptor> listViewItemList;
		private ValueObserver<Language> previewLanguageObserver;
		private StringTableEditorPopup editorPopup;

		private final List<StringTableKeyDescriptor> allItems = new LinkedList<>();
		private final ListView<StringTableKeyDescriptor> lvMatch = new ListView<>();
		private final StringTableKeyEditorPane editorPane;


		public EditTab(@NotNull StringTable table, @NotNull ValueObserver<Language> previewLanguageObserver, @NotNull StringTableEditorPopup editorPopup) {
			super(Lang.ApplicationBundle().getString("Popups.StringTable.Tab.Edit.tab_title"));

			listViewItemList = FXCollections.observableList(new ArrayList<>(), new Callback<StringTableKeyDescriptor, javafx.beans.Observable[]>() {
				public javafx.beans.Observable[] call(StringTableKeyDescriptor param) {
					return new javafx.beans.Observable[]{
							param.getKey().getLanguageTokenMap(),
							param.getKey().getIdObserver(),
							previewLanguageObserver,
							param.getKey().containerNameObserver(),
							param.getKey().packageNameObserver()
					};
				}
			}); //for some reason, can't have a LinkedList as the underlying list implementation if we want the list view to update the displayed cell text automatically
			this.previewLanguageObserver = previewLanguageObserver;
			this.editorPopup = editorPopup;

			previewLanguageObserver.addListener(new ValueListener<Language>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language newValue) {
					for (StringTableKeyDescriptor descriptor : allItems) {
						descriptor.setPreviewLanguage(newValue);
					}
				}
			});

			editorPane = new StringTableKeyEditorPane(table, previewLanguageObserver);

			ResourceBundle bundle = Lang.ApplicationBundle();

			lvMatch.setPlaceholder(new Label(bundle.getString("Popups.StringTable.Tab.Edit.Search.no_match")));
			lvMatch.setStyle("-fx-font-family:monospace");
			for (StringTableKey key : table.getKeys()) {
				addNewKey(key);
			}

			lvMatch.setItems(listViewItemList);
			lvMatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StringTableKeyDescriptor>() {
				@Override
				public void changed(ObservableValue<? extends StringTableKeyDescriptor> observable, StringTableKeyDescriptor oldValue, StringTableKeyDescriptor selected) {
					if (selected != null) {
						editorPane.setKey(selected.getKey(), table);
					} else {
						editorPane.setKey(null, table);
					}
				}
			});

			SearchTextField tfSearch = new StringTableSearchField(lvMatch, allItems);

			VBox vbRoot = new VBox(10, tfSearch, editorPane, lvMatch);
			VBox.setVgrow(lvMatch, Priority.ALWAYS);
			vbRoot.setFillWidth(true);
			vbRoot.setPadding(new Insets(10));
			setContent(vbRoot);
			setClosable(false);

		}

		@NotNull
		public ListView<StringTableKeyDescriptor> getListView() {
			return lvMatch;
		}

		/**
		 Use this instead of adding to {@link ListView#getItems()} with {@link #getListView()}

		 @return the key that was added
		 */
		public StringTableKeyDescriptor addNewKey(@NotNull StringTableKey key) {
			StringTableKeyDescriptor descriptor = new StringTableKeyDescriptor(key, editorPopup.noPackageName, editorPopup.noContainerName);
			descriptor.setPreviewLanguage(previewLanguageObserver.getValue());
			allItems.add(descriptor);
			listViewItemList.add(descriptor);
			listViewItemList.sort(comparator);
			allItems.sort(comparator);
			return descriptor;
		}

		/** Use this instead of removing from {@link ListView#getItems()} with {@link #getListView()} */
		public void removeKey(@NotNull StringTableKey key) {
			StringTableKeyDescriptor match = null;
			for (StringTableKeyDescriptor descriptor : allItems) {
				if (descriptor.getKey().equals(key)) {
					match = descriptor;
					break;
				}
			}
			if (match == null) {
				return;
			}
			removeKey(match);
		}

		public void removeKey(@NotNull StringTableKeyDescriptor key) {
			allItems.remove(key);
			listViewItemList.remove(key);
		}
	}


}
