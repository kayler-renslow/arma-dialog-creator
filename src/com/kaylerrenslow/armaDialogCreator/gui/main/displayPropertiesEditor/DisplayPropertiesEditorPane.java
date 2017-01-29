package com.kaylerrenslow.armaDialogCreator.gui.main.displayPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.DisplayProperty;
import com.kaylerrenslow.armaDialogCreator.control.DisplayPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor.ValueEditor;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 Editor pane that edits {@link ArmaDisplay#getDisplayProperties()}

 @author Kayler
 @since 09/17/2016. */
public class DisplayPropertiesEditorPane extends StackPane {
	private final ArmaDisplay display;
	private final ObservableSet<DisplayPropertyEditor> editorPanes = FXCollections.observableSet();
	private final ObservableSet<DisplayPropertyEditor> editorPanesRO = FXCollections.unmodifiableObservableSet(editorPanes);
	private final VBox root = new VBox(5);
	private final MenuButton menuButtonAddDisplayProperty = new MenuButton(Lang.ApplicationBundle().getString("DisplayPropertiesEditorPane.add_display_property"));

	/**
	 @param display display to edit
	 */
	public DisplayPropertiesEditorPane(@NotNull ArmaDisplay display) {
		initRootPane();
		initAddDisplayPropertyMB();
		this.display = display;
		addPropertyEditors();
		display.getDisplayProperties().addListener(new SetChangeListener<DisplayProperty>() {
			@Override
			public void onChanged(Change<? extends DisplayProperty> change) {
				if (change.wasAdded()) {
					addPropertyEditor(change.getElementAdded());
				} else if (change.wasRemoved()) {
					removePropertyEditor(change.getElementRemoved());
				}
			}
		});
	}

	private void initAddDisplayPropertyMB() {
		menuButtonAddDisplayProperty.setTooltip(new Tooltip(Lang.ApplicationBundle().getString("DisplayPropertiesEditorPane.add_display_property_tooltip")));
		for (DisplayPropertyLookup propertyLookup : DisplayPropertyLookup.values()) {
			final MenuItem mi = new MenuItem(propertyLookup.getPropertyName());
			mi.setUserData(propertyLookup);
			mi.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					display.getDisplayProperties().add(propertyLookup.newEmptyProperty());
				}
			});
			menuButtonAddDisplayProperty.getItems().add(mi);
		}
	}

	private void initRootPane() {
		final VBox vboxWrapperRoot = new VBox(10, root);
		vboxWrapperRoot.setFillWidth(true);
		vboxWrapperRoot.getChildren().add(menuButtonAddDisplayProperty);
		final ScrollPane scrollPane = new ScrollPane(vboxWrapperRoot);
		scrollPane.setPadding(new Insets(5));
		getChildren().add(scrollPane);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
	}

	private void addPropertyEditors() {
		for (DisplayProperty displayProperty : display.getDisplayProperties()) {
			addPropertyEditor(displayProperty);
		}
	}

	private void removePropertyEditor(@NotNull DisplayProperty property) {
		DisplayPropertyEditor found = null;
		for (DisplayPropertyEditor pane : editorPanes) {
			if (pane.getDisplayProperty().equals(property)) {
				found = pane;
				break;
			}
		}
		if (found == null) {
			return;
		}
		root.getChildren().remove(found.getRootNode());
		editorPanes.remove(found);
		disableAddPropertyMenuItem(property, false);
	}

	private void addPropertyEditor(@NotNull DisplayProperty property) {
		for (DisplayPropertyEditor editorPane : editorPanes) {
			if (editorPane.getDisplayProperty().getPropertyLookup() == property.getPropertyLookup()) {
				return;
			}
		}
		DisplayPropertyEditorPane newEditor = new DisplayPropertyEditorPane(property, this);
		editorPanes.add(newEditor);
		root.getChildren().add(newEditor.getRootNode());
		disableAddPropertyMenuItem(property, true);
	}

	private void disableAddPropertyMenuItem(@NotNull DisplayProperty property, boolean disable) {
		for (MenuItem mi : menuButtonAddDisplayProperty.getItems()) {
			if (mi.getUserData() == property.getPropertyLookup()) {
				mi.setDisable(disable);
				break;
			}
		}
	}

	@NotNull
	public ObservableSet<DisplayPropertyEditor> getEditorsReadOnlySet() {
		return editorPanesRO;
	}

	public interface DisplayPropertyEditor {
		@NotNull
		DisplayProperty getDisplayProperty();

		@NotNull
		Node getRootNode();
	}

	private static class DisplayPropertyEditorPane implements DisplayPropertyEditor {
		private final HBox hBox;
		private DisplayProperty displayProperty;

		@SuppressWarnings("unchecked")
		public DisplayPropertyEditorPane(@NotNull DisplayProperty property, @NotNull DisplayPropertiesEditorPane editorPane) {
			this.displayProperty = property;
			final ValueEditor editor = ValueEditor.getEditor(property.getPropertyType(), ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment());
			editor.setValue(property.getValue());
			editor.getReadOnlyObserver().addListener(new ReadOnlyValueListener() {
				@Override
				public void valueUpdated(@NotNull ReadOnlyValueObserver observer, Object oldValue, Object newValue) {
					property.setValue((SerializableValue) newValue);
				}
			});
			displayProperty.getValueObserver().addListener(new ValueListener<SerializableValue>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
					editor.setValue(newValue);
				}
			});
			if (editor.displayFullWidth()) {
				HBox.setHgrow(editor.getRootNode(), Priority.ALWAYS);
			}
			final Button btnRemoveProperty = new Button("-");
			btnRemoveProperty.setDisable(property.getPropertyLookup() == DisplayPropertyLookup.IDD);
			btnRemoveProperty.setTooltip(new Tooltip(Lang.ApplicationBundle().getString("DisplayPropertiesEditorPane.remove_property_tooltip")));
			btnRemoveProperty.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					editorPane.display.getDisplayProperties().remove(property);
				}
			});
			final Label lblPropertyName = new Label(property.getName() + "=");
			hBox = new HBox(5, btnRemoveProperty, lblPropertyName, editor.getRootNode());
			lblPropertyName.setTooltip(new Tooltip(property.getPropertyLookup().getAbout()));
			hBox.setAlignment(Pos.CENTER_LEFT);
		}

		@NotNull
		@Override
		public Node getRootNode() {
			return hBox;
		}

		@NotNull
		@Override
		public DisplayProperty getDisplayProperty() {
			return displayProperty;
		}
	}
}
