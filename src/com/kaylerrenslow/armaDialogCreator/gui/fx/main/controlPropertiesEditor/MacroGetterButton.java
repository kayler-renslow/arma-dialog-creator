package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 A menu button that will make a popup to choose a macro. There is also a drop down menu of recently used macros of the same type to save time.
 Created on 07/09/2016. */
class MacroGetterButton<V extends SerializableValue> extends HBox {
	private static final int MAX_RECENT_MACROS = 10;
	private static HashMap<Class<?>, LinkedList<Macro<?>>> recentMacrosMap = new HashMap<>();

	private Label lblChosenMacro = new Label("Macro:?");

	private final MenuItem miChooseMacro = new MenuItem(Lang.Macros.CHOOSE_MACRO);
	private final SeparatorMenuItem miSeparator = new SeparatorMenuItem();

	private ValueObserver<Macro<V>> macroValueObserver;

	public MacroGetterButton(@NotNull Class<V> clazz, @Nullable Macro<V> currentMacro) {
		super(5);
		macroValueObserver = new ValueObserver<>(null);
		setToMacro(currentMacro);
		SplitMenuButton menuButton = new SplitMenuButton();
		menuButton.setText(Lang.Macros.CHOOSE_MACRO);
		getChildren().addAll(menuButton, lblChosenMacro);

		menuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ChooseMacroPopup<V> popup = new ChooseMacroPopup<>(clazz);
				popup.showAndWait();
				Macro<V> chosenMacro = popup.getChosenMacro();
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
		LinkedList<Macro<?>> recentMacros = recentMacrosMap.get(clazz);
		if (recentMacros == null) {
			recentMacros = new LinkedList<>();
			recentMacrosMap.put(clazz, recentMacros);
		}
		updateRecentMacrosList(menuButton, recentMacros);
	}

	private void updateRecentMacrosList(SplitMenuButton menuButton, LinkedList<Macro<?>> recentMacros) {
		menuButton.getItems().clear();
		menuButton.getItems().addAll(miChooseMacro, miSeparator);

		if (recentMacros.size() == 0) {
			MenuItem miNoRecentMacros = new MenuItem(Lang.Macros.ChooseMacroPopup.NO_RECENT_MACROS);
			miNoRecentMacros.setDisable(true);
			miNoRecentMacros.getStyleClass().add("darker-disabled-menu-item");
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
		if (macro == null) {
			lblChosenMacro.setText(Lang.Macros.MACRO + "=?");
		} else {
			lblChosenMacro.setText(Lang.Macros.MACRO + "=" + macro.getKey());
		}
		macroValueObserver.updateValue(macro);
	}

	public ValueObserver<Macro<V>> getChosenMacroValueObserver() {
		return macroValueObserver;
	}

	private static class ChooseMacroPopup<V extends SerializableValue> extends StagePopup<VBox> {

		private final ListView<Macro<V>> listViewMacros = new ListView<>();

		public ChooseMacroPopup(@NotNull Class<V> macroClassType) {
			super(ArmaDialogCreator.getPrimaryStage(), new Stage(), new VBox(5), Lang.Macros.ChooseMacroPopup.POPUP_TITLE);
			myStage.initModality(Modality.APPLICATION_MODAL);
			myStage.initStyle(StageStyle.UTILITY);

			initRootElement(macroClassType);
			myStage.setResizable(false);
		}

		private void initRootElement(Class<V> macroClassType) {
			myRootElement.setPadding(new Insets(10));
			List<Macro> macroList = ArmaDialogCreator.getApplicationData().getMacroRegistry().getMacros();
			for (Macro macro : macroList) {
				if (macroClassType.isInstance(macro.getValue())) {
					listViewMacros.getItems().add(macro);
				}
			}
			if (listViewMacros.getItems().size() == 0) {
				listViewMacros.setDisable(true);
				Label lblMessage = new Label(Lang.Macros.ChooseMacroPopup.MO_AVAILABLE_MACROS);
				myRootElement.getChildren().addAll(new Label(Lang.Macros.ChooseMacroPopup.AVAILABLE_MACROS), lblMessage);
			} else {
				HBox hbSplit = new HBox(5);
				Label lblListViewMacros = new Label(Lang.Macros.ChooseMacroPopup.AVAILABLE_MACROS, listViewMacros);
				lblListViewMacros.setContentDisplay(ContentDisplay.BOTTOM);

				final double height = 100;
				VBox vbRight = new VBox(10);
				TextArea taComment = new TextArea();
				taComment.setPrefHeight(height);
				taComment.setEditable(false);
				Label lblComment = new Label(Lang.Macros.COMMENT, taComment);
				lblComment.setContentDisplay(ContentDisplay.BOTTOM);
				TextArea taValue = new TextArea();
				taValue.setEditable(false);
				taValue.setPrefHeight(height);
				Label lblValue = new Label(Lang.Macros.VALUE, taValue);
				lblValue.setContentDisplay(ContentDisplay.BOTTOM);

				vbRight.getChildren().addAll(lblValue, lblComment);

				hbSplit.getChildren().addAll(lblListViewMacros, vbRight);
				listViewMacros.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Macro>() {
					@Override
					public void changed(ObservableValue<? extends Macro> observable, Macro oldValue, Macro selected) {
						if (selected != null) {
							taComment.setText(selected.getComment());
							taValue.setText(selected.getValue().toString());
						}
					}
				});
				myRootElement.getChildren().addAll(hbSplit);
			}
			myStage.sizeToScene();
			myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(true, true, false));
		}

		@Override
		protected void cancel() {
			listViewMacros.getSelectionModel().clearSelection();
			close();
		}

		/** Return the macro chosen. If null, no macro was chosen. */
		@Nullable
		public Macro<V> getChosenMacro() {
			return listViewMacros.getSelectionModel().getSelectedItem();
		}
	}
}
