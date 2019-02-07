package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.core.Macro;
import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.stringtable.StringTableKey;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.data.olddata.Project;
import com.armadialogcreator.gui.main.popup.ChooseMacroDialog;
import com.armadialogcreator.gui.main.popup.EditMacroPopup;
import com.armadialogcreator.gui.main.stringtable.StringTableKeyEditorDialog;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 A menu button that will make a popup to choose a macro.
 There is also a drop down menu of recently used macros of the same type to save time.

 @author Kayler
 @since 07/09/2016. */
public class MacroGetterButton<V extends SerializableValue> extends HBox {
	private static final int MAX_RECENT_MACROS = 10;
	private static HashMap<PropertyType, LinkedList<Macro<?>>> recentMacrosMap = new HashMap<>();

	private final ResourceBundle bundle = Lang.ApplicationBundle();
	private final Hyperlink hyplinkChosenMacro = new Hyperlink();
	private final Label lblNoChosenMacro = new Label(bundle.getString("Macros.no_macro_set"));
	private final HBox hboxMacroLbl = new HBox(0, new Label(bundle.getString("Macros.macro") + "="), hyplinkChosenMacro);
	private final StackPane stackPaneChosenMacroText = new StackPane(hboxMacroLbl);

	private final MenuItem miChooseMacro = new MenuItem(bundle.getString("Macros.choose_macro"));
	private final MenuItem miClearMacro = new MenuItem(bundle.getString("Macros.clear_macro"));
	private final SeparatorMenuItem miSeparator = new SeparatorMenuItem();

	private ValueObserver<Macro<V>> macroValueObserver;

	public MacroGetterButton(@Nullable PropertyType filter, @Nullable Macro<V> currentMacro) {
		super(5);
		hyplinkChosenMacro.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (macroValueObserver.getValue() == null) {
					return;
				}
				if (macroValueObserver.getValue() instanceof StringTableKey) {
					StringTableKey key = (StringTableKey) macroValueObserver.getValue();
					StringTableKeyEditorDialog dialog = new StringTableKeyEditorDialog(Project.getCurrentProject().getStringTable(), key);
					dialog.show();

				} else {
					EditMacroPopup editMacroPopup = new EditMacroPopup(macroValueObserver.getValue());
					editMacroPopup.show();
				}
				hyplinkChosenMacro.setVisited(false);
			}
		});
		macroValueObserver = new ValueObserver<>(null);
		setToMacro(currentMacro);
		SplitMenuButton menuButton = new SplitMenuButton();
		menuButton.setText(bundle.getString("Macros.choose_macro"));
		setAlignment(Pos.CENTER_LEFT);
		hboxMacroLbl.setAlignment(Pos.CENTER_LEFT);
		Label lblNeedType = new Label(filter == null ? "*" : filter.getDisplayName());
		getChildren().addAll(menuButton, stackPaneChosenMacroText, new Separator(Orientation.VERTICAL), lblNeedType);

		menuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ChooseMacroDialog<V> popup = new ChooseMacroDialog<>(filter);
				popup.showAndWait();
				Macro<V> chosenMacro = popup.getChosenItem();
				if (chosenMacro == null) {
					return;
				}
				setToMacro(chosenMacro);
				LinkedList<Macro<?>> recentMacros = recentMacrosMap.computeIfAbsent(filter, k -> new LinkedList<>());
				if (recentMacros.size() + 1 >= MAX_RECENT_MACROS) {
					recentMacros.removeLast();
				}
				recentMacros.addFirst(chosenMacro);
				updateRecentMacrosList(menuButton, recentMacros);
			}
		});
		miChooseMacro.setOnAction(menuButton.getOnAction());
		miClearMacro.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setToMacro(null);
			}
		});
		LinkedList<Macro<?>> recentMacros = recentMacrosMap.computeIfAbsent(filter, k -> new LinkedList<>());
		updateRecentMacrosList(menuButton, recentMacros);
	}

	private void updateRecentMacrosList(SplitMenuButton menuButton, LinkedList<Macro<?>> recentMacros) {
		menuButton.getItems().clear();
		menuButton.getItems().addAll(miChooseMacro, miClearMacro, miSeparator);

		if (recentMacros.size() == 0) {
			MenuItem miNoRecentMacros = new MenuItem(bundle.getString("Macros.no_recent_macros"));
			miNoRecentMacros.setDisable(true);
			menuButton.getItems().add(miNoRecentMacros);
		} else {
			MenuItem miRecentMacro;
			for (Macro recent : recentMacros) {
				miRecentMacro = new MenuItem(recent.getKey());
				miRecentMacro.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						setToMacro(recent);
					}
				});
				miRecentMacro.setMnemonicParsing(false); //allow the first underscore in macro key to be displayed. https://bugs.openjdk.java.net/browse/JDK-8095296
				menuButton.getItems().add(miRecentMacro);
			}
		}

	}

	private void setToMacro(@Nullable Macro<V> macro) {
		stackPaneChosenMacroText.getChildren().clear();
		if (macro == null) {
			stackPaneChosenMacroText.getChildren().add(lblNoChosenMacro);
		} else {
			stackPaneChosenMacroText.getChildren().add(hboxMacroLbl);
			hyplinkChosenMacro.setText(macro.getKey());
		}
		macroValueObserver.updateValue(macro);
	}

	@NotNull
	public ValueObserver<Macro<V>> getChosenMacroValueObserver() {
		return macroValueObserver;
	}

}
