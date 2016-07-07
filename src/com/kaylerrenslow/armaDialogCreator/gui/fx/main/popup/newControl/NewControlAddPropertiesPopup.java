package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.newControl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.FXUtil;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 @author Kayler
 The popup that allows the user to pick and choose which properties go into the new control
 Created on 07/06/2016. */
public class NewControlAddPropertiesPopup extends StagePopup<VBox> {
	private final NewControlPopup newControlPopup;

	public NewControlAddPropertiesPopup(NewControlPopup newControlPopup) {
		super(ArmaDialogCreator.getPrimaryStage(), FXUtil.loadFxml("/com/kaylerrenslow/armaDialogCreator/gui/fx/main/popup/newControl/newControlAddProperties.fxml"), Lang.Popups.NewControl.AddPropertiesPopup.POPUP_TITLE);
		this.newControlPopup = newControlPopup;
		myStage.initModality(Modality.WINDOW_MODAL);
		myStage.initStyle(StageStyle.UTILITY);

		initControls();
	}

	private void initControls() {
		NewControlAddPropertiesPopupController controller = getMyLoader().getController();
		controller.btnCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				close();
			}
		});
		initTableviewAllProperties(controller);

	}

	private void initTableviewAllProperties(NewControlAddPropertiesPopupController controller) {
		//todo remove tableview and just use vbox so that search filtering will work better and we can store the propertyLookup item
		initTableview(controller);
		ObservableList<ObservableList> rows = FXCollections.observableArrayList();
		ObservableList<String> rowColumns;
		for (ControlPropertyLookup lookup : ControlPropertyLookup.values()) {
			rowColumns = FXCollections.observableArrayList(lookup.propertyName, lookup.propertyType.name(), lookup.about[0]);
			rows.add(rowColumns);
		}
		controller.tvAllProperties.setItems(rows);
	}

	private void initTableview(NewControlAddPropertiesPopupController controller) {
		ObservableList<TableColumn<ObservableList, ?>> columns = controller.tvAllProperties.getColumns();
		columns.addAll(new TableColumn(Lang.Popups.NewControl.AddPropertiesPopup.COLUMN_PROPERTY_NAME), new TableColumn(Lang.Popups.NewControl.AddPropertiesPopup.COLUMN_PROPERTY_TYPE), new TableColumn(Lang.Popups.NewControl.AddPropertiesPopup.COLUMN_PROPERTY_DESCRIPTION));
		int col = 0;
		for (TableColumn tableColumn : columns) {
			int finalCol = col;
			tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
					return new SimpleStringProperty(param.getValue().get(finalCol).toString());
				}
			});
			col++;
		}
	}

	@Override
	public void show() {
		showAndWait();
	}

//	private static class
}
