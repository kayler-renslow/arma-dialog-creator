/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.ChooseMacroPopup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.EditMacroPopup;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;

/**
 A menu button that will make a popup to choose a macro. There is also a drop down menu of recently used macros of the same type to save time.

 @author Kayler
 @since 07/09/2016. */
public class MacroGetterButton<V extends SerializableValue> extends HBox {
	private static final int MAX_RECENT_MACROS = 10;
	private static HashMap<Class<?>, LinkedList<Macro<?>>> recentMacrosMap = new HashMap<>();

	private final Hyperlink hyplinkChosenMacro = new Hyperlink();
	private final Label lblNoChosenMacro = new Label(Lang.ApplicationBundle().getString("Macros.no_macro_set"));
	private final HBox hboxMacroLbl = new HBox(0, new Label(Lang.ApplicationBundle().getString("Macros.macro") + "="), hyplinkChosenMacro);
	private final StackPane stackPaneChosenMacroText = new StackPane(hboxMacroLbl);

	private final MenuItem miChooseMacro = new MenuItem(Lang.ApplicationBundle().getString("Macros.choose_macro"));
	private final MenuItem miClearMacro = new MenuItem(Lang.ApplicationBundle().getString("Macros.clear_macro"));
	private final SeparatorMenuItem miSeparator = new SeparatorMenuItem();

	private ValueObserver<Macro<V>> macroValueObserver;

	public MacroGetterButton(@NotNull Class<V> clazz, @Nullable Macro<V> currentMacro) {
		super(5);
		hyplinkChosenMacro.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (macroValueObserver.getValue() == null) {
					return;
				}
				EditMacroPopup editMacroPopup = new EditMacroPopup(macroValueObserver.getValue());
				editMacroPopup.show();
				hyplinkChosenMacro.setVisited(false);
			}
		});
		macroValueObserver = new ValueObserver<>(null);
		setToMacro(currentMacro);
		SplitMenuButton menuButton = new SplitMenuButton();
		menuButton.setText(Lang.ApplicationBundle().getString("Macros.choose_macro"));
		setAlignment(Pos.CENTER_LEFT);
		hboxMacroLbl.setAlignment(Pos.CENTER_LEFT);
		getChildren().addAll(menuButton, stackPaneChosenMacroText);

		menuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ChooseMacroPopup<V> popup = new ChooseMacroPopup<>(clazz);
				popup.showAndWait();
				Macro<V> chosenMacro = popup.getChosenItem();
				if (chosenMacro == null) {
					return;
				}
				setToMacro(chosenMacro);
				LinkedList<Macro<?>> recentMacros = recentMacrosMap.get(clazz);
				if (recentMacros == null) {
					recentMacros = new LinkedList<>();
					recentMacrosMap.put(clazz, recentMacros);
				}
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
		LinkedList<Macro<?>> recentMacros = recentMacrosMap.get(clazz);
		if (recentMacros == null) {
			recentMacros = new LinkedList<>();
			recentMacrosMap.put(clazz, recentMacros);
		}
		updateRecentMacrosList(menuButton, recentMacros);
	}

	private void updateRecentMacrosList(SplitMenuButton menuButton, LinkedList<Macro<?>> recentMacros) {
		menuButton.getItems().clear();
		menuButton.getItems().addAll(miChooseMacro, miClearMacro, miSeparator);

		if (recentMacros.size() == 0) {
			MenuItem miNoRecentMacros = new MenuItem(Lang.ApplicationBundle().getString("Macros.no_recent_macros"));
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

	public ValueObserver<Macro<V>> getChosenMacroValueObserver() {
		return macroValueObserver;
	}

}
