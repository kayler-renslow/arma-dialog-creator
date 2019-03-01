package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.stringtable.Language;
import com.armadialogcreator.core.stringtable.StringTable;
import com.armadialogcreator.core.stringtable.StringTableKey;
import com.armadialogcreator.data.MacroRegistry;
import com.armadialogcreator.data.StringTableManager;
import com.armadialogcreator.gui.main.fxControls.ChooseItemDialog;
import com.armadialogcreator.gui.main.stringtable.StringTableLanguageTokenEditor;
import com.armadialogcreator.lang.Lang;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 Used for displaying known macros of a given type and allowing the user to choose which macro they want.

 @author Kayler
 @since 08/09/2016. */
public class ChooseMacroDialog extends ChooseItemDialog<Macro> {

	private static MacroItemCategory[] getCategories() {
		MacroItemCategory[] categories = new MacroItemCategory[6];
		categories[0] = new AllMacrosCategory();
		categories[1] = new DataLevelCategory(DataLevel.Project);
		categories[2] = new DataLevelCategory(DataLevel.Workspace);
		categories[3] = new DataLevelCategory(DataLevel.Application);
		categories[4] = new DataLevelCategory(DataLevel.System);
		categories[5] = new StringTableKeyMacroCategory();

		return categories;
	}

	private static List<Macro> getMacrosOfType(@Nullable PropertyType filter) {
		List<Macro> macros = new ArrayList<>();
		for (Macro macro : MacroRegistry.instance.iterateAllMacros()) {
			if (filter == null || macro.getValue().getPropertyType() == filter) {
				macros.add(macro);
			}
		}
		if (filter == PropertyType.String) {
			StringTable table = StringTableManager.instance.getStringTable();
			if (table != null) {
				macros.addAll(table.getKeys());
			}
		}
		return macros;
	}

	public ChooseMacroDialog(@Nullable PropertyType filter) {
		super(getCategories(), getMacrosOfType(filter),
				Lang.ApplicationBundle().getString("Popups.ChooseMacro.popup_title"),
				Lang.ApplicationBundle().getString("Popups.ChooseMacro.choose_macro_title")
		);
		myStage.sizeToScene();
	}

	@Override
	public void show() {
		showAndWait();
	}

	private static class DataLevelCategory extends BasicMacroItemCategory {

		private final DataLevel level;

		public DataLevelCategory(@NotNull DataLevel level) {
			this.level = level;
		}

		@NotNull
		@Override
		public String categoryDisplayName() {
			return level.name();
		}

		@Override
		public boolean itemInCategory(@NotNull Macro item) {
			return MacroRegistry.instance.getDataLevel(item) == level;
		}
	}

	private static class AllMacrosCategory extends BasicMacroItemCategory {

		@NotNull
		@Override
		public String categoryDisplayName() {
			return Lang.ApplicationBundle().getString("Popups.ChooseMacro.category_all_macros");
		}

		@Override
		public boolean itemInCategory(@NotNull Macro item) {
			return true;
		}
	}

	private static abstract class BasicMacroItemCategory extends MacroItemCategory {

		private final Node categoryNode;
		private final TextArea taComment = new TextArea();
		private final TextArea taValue = new TextArea();
		private final Label lblMacroPropertyType;

		public BasicMacroItemCategory() {
			final double height = 100;
			VBox vb = new VBox(10);
			categoryNode = vb;
			ResourceBundle bundle = Lang.ApplicationBundle();

			taComment.setPrefHeight(height);
			taComment.setEditable(false);
			Label lblComment = new Label(bundle.getString("Macros.comment"), taComment);
			lblComment.setContentDisplay(ContentDisplay.BOTTOM);
			taValue.setEditable(false);
			taValue.setPrefHeight(height);
			Label lblValue = new Label(bundle.getString("Macros.value"), taValue);
			lblValue.setContentDisplay(ContentDisplay.BOTTOM);

			lblMacroPropertyType = new Label(String.format(bundle.getString("Popups.ChooseMacro.property_type"), "?"));

			vb.getChildren().addAll(lblValue, lblComment, lblMacroPropertyType);
		}

		@Nullable
		@Override
		public Node getMiscCategoryNode() {
			return categoryNode;
		}

		@Override
		public void newItemSelected(Macro selected) {
			if (selected != null) {
				taComment.setText(selected.getComment());
				taValue.setText(selected.getValue().toString());
				lblMacroPropertyType.setText(String.format(Lang.ApplicationBundle().getString("Popups.ChooseMacro.property_type"), selected.getPropertyType().getDisplayName()));
			} else {
				lblMacroPropertyType.setText(String.format(Lang.ApplicationBundle().getString("Popups.ChooseMacro.property_type"), "?"));
			}
		}
	}

	private static class StringTableKeyMacroCategory extends MacroItemCategory {

		private final VBox vboxLanguages = new VBox(10);
		private final ScrollPane categoryNode = new ScrollPane(vboxLanguages);

		public StringTableKeyMacroCategory() {
			categoryNode.setFitToHeight(true);
			categoryNode.setFitToWidth(true);
			vboxLanguages.setFillWidth(true);
			categoryNode.setPadding(new Insets(5));
		}

		@NotNull
		@Override
		public String categoryDisplayName() {
			return "~~------StringTable";
		}

		@Override
		public boolean itemInCategory(@NotNull Macro item) {
			return item instanceof StringTableKey;
		}

		@Nullable
		@Override
		public Node getMiscCategoryNode() {
			return categoryNode;
		}

		@Override
		public void newItemSelected(@Nullable Macro item) {
			vboxLanguages.getChildren().clear();
			if (item == null) {
				return;
			}
			StringTableKey key = (StringTableKey) item;

			if (key.getDefaultLanguageToken() != null) {
				vboxLanguages.getChildren().add(getVboxLanguage(key.getDefaultLanguage(), key.getDefaultLanguageToken()));
			}

			for (Map.Entry<Language, String> entry : key.getLanguageTokenMap().entrySet()) {
				if (entry.getKey() == key.getDefaultLanguage()) {
					continue;
				}
				VBox vboxLanguage = getVboxLanguage(entry.getKey(), entry.getValue());
				vboxLanguages.getChildren().add(vboxLanguage);
			}
		}

		@NotNull
		private VBox getVboxLanguage(Language language, String languageToken) {
			VBox vboxLanguage = new VBox(5);
			vboxLanguage.setFillWidth(true);
			vboxLanguage.getChildren().add(new Label(language.getName()));

			StringTableLanguageTokenEditor editor = new StringTableLanguageTokenEditor();
			editor.setWrapText(true);
			editor.setEditable(false);
			editor.replaceText(languageToken);
			vboxLanguage.getChildren().add(editor);
			return vboxLanguage;
		}
	}

	private static abstract class MacroItemCategory implements ItemCategory<Macro> {

		@NotNull
		@Override
		public String noItemsPlaceholderText() {
			return Lang.ApplicationBundle().getString("Popups.ChooseMacro.no_available_macros");
		}

		@NotNull
		@Override
		public String availableItemsDisplayText() {
			return Lang.ApplicationBundle().getString("Popups.ChooseMacro.available_macros");
		}

	}


}
