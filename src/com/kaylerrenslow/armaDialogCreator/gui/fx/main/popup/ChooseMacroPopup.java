package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls.ChooseItemPopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.HelpUrls;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 Used for displaying known macros of a given type and allowing the user to choose which macro they want.

 @author Kayler
 @since 08/09/2016. */
public class ChooseMacroPopup<V extends SerializableValue> extends ChooseItemPopup<Macro<V>> {

	private static final MacroItemCategory[] categories = new MacroItemCategory[Macro.MacroType.values().length + 1];

	static {
		categories[0] = new AllMacrosCategory();
		int i = 1;
		for (Macro.MacroType type : Macro.MacroType.values()) {
			categories[i++] = new ImplicitMacroTypeCategory(type);
		}
	}

	@SuppressWarnings("unchecked")
	private static <V extends SerializableValue> List<Macro<V>> getMacrosOfType(@NotNull Class<V> macroClassType) {
		List<Macro<V>> macros = new ArrayList<>();
		for (Macro macro : ArmaDialogCreator.getApplicationData().getCurrentProject().getMacroRegistry().getMacros()) {
			if (macroClassType.isInstance(macro.getValue())) {
				macros.add(macro);
			}
		}
		return macros;
	}

	@SuppressWarnings("unchecked")
	public ChooseMacroPopup(@NotNull Class<V> macroClassType) {
		super(categories, getMacrosOfType(macroClassType),
				Lang.ApplicationBundle().getString("Popups.ChooseMacro.popup_title"),
				Lang.ApplicationBundle().getString("Popups.ChooseMacro.choose_macro_title")
		);
		myStage.sizeToScene();
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.MACROS);
	}


	@Override
	public void show() {
		showAndWait();
	}

	private static class ImplicitMacroTypeCategory<V extends SerializableValue> extends MacroItemCategory<V> {

		private final Macro.MacroType type;

		public ImplicitMacroTypeCategory(@NotNull Macro.MacroType type) {
			this.type = type;
		}

		@NotNull
		@Override
		public String categoryDisplayName() {
			return type.getDisplayText();
		}

		@Override
		public boolean itemInCategory(@NotNull Macro item) {
			return item.getMacroType() == type;
		}
	}

	private static class AllMacrosCategory<V extends SerializableValue> extends MacroItemCategory<V> {

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

	private static abstract class MacroItemCategory<V extends SerializableValue> implements ItemCategory<Macro<V>> {

		private final Node categoryNode;

		private final TextArea taComment = new TextArea();
		private final TextArea taValue = new TextArea();

		public MacroItemCategory() {
			final double height = 100;
			VBox vb = new VBox(10);
			taComment.setPrefHeight(height);
			taComment.setEditable(false);
			Label lblComment = new Label(Lang.ApplicationBundle().getString("Macros.comment"), taComment);
			lblComment.setContentDisplay(ContentDisplay.BOTTOM);
			taValue.setEditable(false);
			taValue.setPrefHeight(height);
			Label lblValue = new Label(Lang.ApplicationBundle().getString("Macros.value"), taValue);
			lblValue.setContentDisplay(ContentDisplay.BOTTOM);

			vb.getChildren().addAll(lblValue, lblComment);
			categoryNode = vb;
		}


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

		@Nullable
		@Override
		public Node getMiscCategoryNode() {
			return categoryNode;
		}

		@Override
		public void newItemSelected(Macro<V> selected) {
			if (selected != null) {
				taComment.setText(selected.getComment());
				taValue.setText(selected.getValue().toString());
			}
		}
	}
}
