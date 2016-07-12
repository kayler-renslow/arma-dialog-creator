package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.data.Macro;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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
class MacroGetterButton<V> extends HBox {
	private static final int MAX_RECENT_MACROS = 10;
	private static HashMap<Class<?>, LinkedList<Macro<?>>> recentMacrosMap = new HashMap<>();

	private Label lblDisplayValue = new Label("Value:?");
	private Label lblChosenMacro = new Label("Macro:?");

	private final MenuItem miChooseMacro = new MenuItem(Lang.Macros.CHOOSE_MACRO);
	private final SeparatorMenuItem miSeparator = new SeparatorMenuItem();

	private ValueObserver<V> valueObserver;

	public MacroGetterButton(Class<V> clazz, V defaultValue) {
		super(5);
		valueObserver = new ValueObserver<>(defaultValue);
		valueObserver.addValueListener(new ValueListener<V>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<V> observer, V oldValue, V newValue) {
				setDisplayValue(newValue);
			}
		});
		setToMacro(null);
		setDisplayValue(defaultValue);
		SplitMenuButton menuButton = new SplitMenuButton();
		menuButton.setText(Lang.Macros.CHOOSE_MACRO);
		lblDisplayValue.setAlignment(Pos.CENTER);
		getChildren().addAll(menuButton, lblChosenMacro, lblDisplayValue);

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
					recentMacros.removeFirst();
				}
				recentMacros.add(chosenMacro);
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
				menuButton.getItems().add(miRecentMacro);
			}
		}

	}

	private void setToMacro(@Nullable Macro<V> macro) {
		if (macro == null) {
			lblChosenMacro.setText(Lang.Macros.MACRO + "=?");
			setDisplayValue(null);
		} else {
			lblChosenMacro.setText(Lang.Macros.MACRO + "=" + macro.getKey());
			setDisplayValue(macro.getValue());
		}
	}

	public ValueObserver<V> getValueObserver() {
		return valueObserver;
	}

	private void setDisplayValue(@Nullable V value) {
		if (value == null) {
			lblDisplayValue.setText("?");
		} else {
			lblDisplayValue.setText(Lang.Macros.VALUE + "=" + value.toString());
		}
	}

	private static class ChooseMacroPopup<V> extends StagePopup<VBox> {

		private final ListView<Macro<V>> listViewMacros = new ListView<>();

		public ChooseMacroPopup(Class<V> macroClassType) {
			super(ArmaDialogCreator.getPrimaryStage(), new Stage(), new VBox(5), Lang.Macros.ChooseMacroPopup.POPUP_TITLE);
			myStage.initModality(Modality.APPLICATION_MODAL);
			myStage.initStyle(StageStyle.UTILITY);

			initRootElement(macroClassType);
		}

		private void initRootElement(Class<V> macroClassType) {
			myRootElement.setPadding(new Insets(10));
			Label lblAvail = new Label(Lang.Macros.ChooseMacroPopup.AVAILABLE_MACROS);
			List<Macro> macroList = ArmaDialogCreator.getApplicationData().getMacroRegistry().getMacros();
			for (Macro macro : macroList) {
				if (macroClassType.isInstance(macro)) {
					listViewMacros.getItems().add(macro);
				}
			}
			if (listViewMacros.getItems().size() == 0) {
				listViewMacros.setDisable(true);
				Label lblMessage = new Label(Lang.Macros.ChooseMacroPopup.MO_AVAILABLE_MACROS);
				myRootElement.getChildren().addAll(lblAvail, lblMessage);
//				setStageSize(myScene.getWidth(), myScene.getHeight());
			} else {
				HBox hbSplit = new HBox(5);
				TextArea taComment = new TextArea();
				taComment.setEditable(false);
				Label lblComment = new Label(Lang.Macros.COMMENT, taComment);
				lblComment.setContentDisplay(ContentDisplay.BOTTOM);
				hbSplit.getChildren().addAll(listViewMacros, lblComment);
				listViewMacros.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Macro>() {
					@Override
					public void changed(ObservableValue<? extends Macro> observable, Macro oldValue, Macro selected) {
						taComment.setText(selected.getComment());
					}
				});
				myRootElement.getChildren().addAll(hbSplit);
//				setStageSize(myScene.getWidth(), myScene.getHeight());
			}
			myStage.sizeToScene();
			myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(true, true ,false));
		}

		@Override
		protected void cancel() {
			listViewMacros.getSelectionModel().clearSelection();
			close();
		}

		@Nullable
		/**Return the macro chosen. If null, no macro was chosen.*/
		public Macro<V> getChosenMacro() {
			return listViewMacros.getSelectionModel().getSelectedItem();
		}
	}
}
