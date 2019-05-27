package com.armadialogcreator.gui.main;

import com.armadialogcreator.core.*;
import com.armadialogcreator.gui.fxcontrol.PlaceholderTitledPane;
import com.armadialogcreator.gui.main.popup.EditConfigClassDialog;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.ReadOnlyIterable;
import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 Houses an accordion that allows manipulating multiple control properties. The data editor for each control property is specialized for the input (e.g. color property gets color picker).

 @author Kayler
 @since 07/08/2016. */
public class ConfigPropertiesEditorPane extends StackPane {
	private final Accordion accordion = new Accordion();
	private final ConfigClass configClass;

	private final Map<ConfigPropertyKey, ConfigPropertyEditor> propertyEditors = new HashMap<>();
	private final Map<ConfigPropertyKey, ConfigPropertyPlaceholder> propertyPlaceholders = new HashMap<>();

	private final ResourceBundle bundle = Lang.getBundle("ConfigPropertyEditorBundle");
	private final UpdateGroupListener<ConfigClassUpdate> configClassUpdateListener;
	private final PlaceholderTitledPane required, optional, events;

	/**
	 Creates the accordion for editing all owned {@link ConfigClass#iterateProperties()} instances.
	 <p>
	 It is important to note that when the control properties inside the control are edited,
	 they will be updated in the control class as well. There is no copying of the controlClass's
	 control properties and everything is passed by reference.

	 @param configClass control class that has the properties to edit
	 */
	public ConfigPropertiesEditorPane(@NotNull ConfigClass configClass) {
		getChildren().add(accordion);
		setMaxWidth(Double.MAX_VALUE);

		this.configClass = configClass;

		required = getPropertiesTitledPane(bundle.getString("required"));
		optional = getPropertiesTitledPane(bundle.getString("optional"));
		events = getPropertiesTitledPane(bundle.getString("events"));

		configClassUpdateListener = new UpdateGroupListener<>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ConfigClassUpdate> group, @NotNull ConfigClassUpdate data) {
				switch (data.getType()) {
					case AddProperty: {
						ConfigClassUpdate.AddPropertyUpdate update = (ConfigClassUpdate.AddPropertyUpdate) data;
						ConfigPropertyKey addedPropertyKey = update.getAddedProperty().getAsKey();
						ConfigPropertyPlaceholder placeholder = propertyPlaceholders.remove(addedPropertyKey);
						if (placeholder != null) {
							placeholder.unlink();
							PlaceholderTitledPane titledPane = getTitledPane(addedPropertyKey);
							titledPane.removeContentChild(placeholder);
							sortTitlePane(titledPane);
						}
						ConfigPropertyEditor existingEditor = propertyEditors.remove(addedPropertyKey);
						if (existingEditor != null) {
							existingEditor.unlink();
						}
						ConfigProperty property = configClass.findProperty(addedPropertyKey);
						PlaceholderTitledPane titledPane = addPropertyEditorToTitlePane(property);
						sortTitlePane(titledPane);
						break;
					}
					case RemoveProperty: {
						ConfigClassUpdate.RemovePropertyUpdate update = (ConfigClassUpdate.RemovePropertyUpdate) data;
						ConfigPropertyKey removedPropertyKey = update.getRemovedProperty().getAsKey();
						ConfigPropertyEditor existingEditor = propertyEditors.remove(removedPropertyKey);
						if (existingEditor != null) {
							existingEditor.unlink();
							PlaceholderTitledPane titledPane = getTitledPane(removedPropertyKey);
							titledPane.removeContentChild(existingEditor);
							sortTitlePane(titledPane);
						}
						ConfigPropertyLookupConstant lookup = configClass.getLookup(removedPropertyKey.getPropertyName());
						if (lookup != null) {
							ConfigPropertyPlaceholder existingPlaceholder = propertyPlaceholders.get(lookup.getHashSafeKey());
							if (existingPlaceholder == null) {
								PlaceholderTitledPane titlePane = addPropertyPlaceholderToTitlePane(lookup);
								sortTitlePane(titlePane);
							}
						}
						break;
					}
				}
			}
		};
		configClass.getClassUpdateGroup().addListener(configClassUpdateListener);

		for (ConfigProperty p : configClass.iterateProperties()) {
			addPropertyEditorToTitlePane(p);
		}

		ReadOnlyIterable<ConfigPropertyLookupConstant> lookupIterable = configClass.iterateLookupProperties();
		if (lookupIterable != null) {
			for (ConfigPropertyLookupConstant constant : lookupIterable) {
				if (propertyEditors.get(constant.getHashSafeKey()) != null) {
					continue;
				}
				addPropertyPlaceholderToTitlePane(constant);
			}
		}

		sortTitlePane(required);
		sortTitlePane(optional);
		sortTitlePane(events);

		accordion.getPanes().add(required);
		accordion.getPanes().add(optional);
		accordion.getPanes().add(events);
		accordion.getPanes().add(getNestedClassesTitledPane());

		accordion.setExpandedPane(accordion.getPanes().get(0));
		accordion.expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
			accordion.autosize();
		});
		accordion.setMaxWidth(Double.MAX_VALUE);
	}

	private void sortTitlePane(@NotNull PlaceholderTitledPane titledPane) {
		ObservableList<Node> children = titledPane.getContentChildren();
		FXCollections.sort(children, (node, node2) -> {
			boolean n1 = node instanceof ConfigPropertyDisplayBox;
			boolean n2 = node2 instanceof ConfigPropertyDisplayBox;
			if (n1 && n2) {
				ConfigPropertyDisplayBox box = (ConfigPropertyDisplayBox) node;
				ConfigPropertyDisplayBox box2 = (ConfigPropertyDisplayBox) node2;

				return ConfigPropertyKey.PRIORITY_SORT.compare(box.configPropertyKey, box2.configPropertyKey);
			}
			if (n1) {
				return -1;
			}
			return 1;
		});
	}

	@NotNull
	private PlaceholderTitledPane addPropertyEditorToTitlePane(@NotNull ConfigProperty p) {
		PlaceholderTitledPane titledPane = getTitledPane(p);
		ConfigPropertyEditor node = getConfigPropertyEditorNode(p);
		titledPane.addContentChild(node);
		propertyEditors.put(p, node);
		return titledPane;
	}

	@NotNull
	private PlaceholderTitledPane addPropertyPlaceholderToTitlePane(@NotNull ConfigPropertyLookupConstant p) {
		PlaceholderTitledPane titledPane = getTitledPane(p.getHashSafeKey());
		ConfigPropertyPlaceholder placeholder = new ConfigPropertyPlaceholder(configClass, p);
		titledPane.addContentChild(placeholder);
		propertyPlaceholders.put(p.getHashSafeKey(), placeholder);
		return titledPane;
	}

	@NotNull
	private PlaceholderTitledPane getTitledPane(@NotNull ConfigPropertyKey key) {
		ConfigPropertyCategory propertyCat = configClass.getPropertyCategory(key);
		switch (propertyCat) {
			case Basic: //fall
			case Optional: {
				return optional;
			}
			case Event: {
				return events;
			}
			case User: {
				break;
			}
			case Required: {
				return required;
			}
		}
		return optional;
	}

	/**
	 Tell all editors to stop listening to the {@link ConfigProperty} values again.
	 Invoking is ideal when the pane is no longer needed.
	 */
	public void unlink() {
		for (Map.Entry<ConfigPropertyKey, ConfigPropertyEditor> editor : propertyEditors.entrySet()) {
			editor.getValue().unlink();
		}
		configClass.getClassUpdateGroup().removeListener(configClassUpdateListener);
	}

	/** Show only the editors with property names containing <code>name</code>. If length of <code>name</code>.trim() is 0 (), will show all editors */
	public void showPropertiesWithNameContaining(@NotNull String name) {
		name = name.trim().toLowerCase();
		for (Map.Entry<ConfigPropertyKey, ConfigPropertyEditor> entry : propertyEditors.entrySet()) {
			entry.getValue().hide(name.length() > 0 && !entry.getKey().getPropertyName().contains(name));
		}
	}

	/** Show only editors with properties that aren't inherited ({@link ConfigClassSpecification#propertyIsInherited(String)}) */
	public void hideInheritedProperties(boolean hide) {
		for (Map.Entry<ConfigPropertyKey, ConfigPropertyEditor> entry : propertyEditors.entrySet()) {
			entry.getValue().hideIfInherited(hide);
		}
	}

	/** Get the {@link ConfigClassSpecification} being edited */
	@NotNull
	public ConfigClassSpecification getConfigClass() {
		return configClass;
	}

	/** @return all missing properties (control properties that are required by have no valid data entered). */
	@NotNull
	public List<String> getMissingProperties() {
		List<String> properties = new ArrayList<>(propertyEditors.size());
		for (ConfigProperty p : configClass.iterateProperties()) {
			if (propertyEditors.containsKey(p)) {
				continue;
			}
			ConfigPropertyCategory category = configClass.getPropertyCategory(p);
			if (category == ConfigPropertyCategory.Required) {
				properties.add(p.getName());
			}
		}

		return properties;
	}

	@NotNull
	private TitledPane getNestedClassesTitledPane() {
		VBox vboxNestedClasses = new VBox(10);
		vboxNestedClasses.setPadding(new Insets(5));
		for (ConfigClass cc : configClass.iterateNestedClasses()) {
			MenuButton menuButton = new MenuButton(cc.getClassName());

			MenuItem miEdit = new MenuItem(bundle.getString("edit_nested_class"));
			menuButton.getItems().add(miEdit);

			miEdit.setOnAction(event -> {
				EditConfigClassDialog dialog = new EditConfigClassDialog(cc);
				dialog.show();
			});
			menuButton.setUserData(cc);

			vboxNestedClasses.getChildren().add(menuButton);
		}

		PlaceholderTitledPane tp = new PlaceholderTitledPane(
				bundle.getString("nested_classes"),
				new Label(bundle.getString("no_classes")),
				vboxNestedClasses,
				true
		);
		tp.getScrollPane().setFitToWidth(true);
		tp.setAnimated(false);
		vboxNestedClasses.setFillWidth(true);

		return tp;
	}

	/** @return a titled pane for the accordion that holds all properties of a certain {@link ConfigPropertyCategory} */
	@NotNull
	private PlaceholderTitledPane getPropertiesTitledPane(@NotNull String title) {
		final VBox vb = new VBox(10);
		vb.setFillWidth(true);
		vb.setPadding(new Insets(5));
		final Label lblNoProps = new Label(bundle.getString("ControlPropertiesConfig.no_properties_available"));
		final PlaceholderTitledPane tp = new PlaceholderTitledPane(title, lblNoProps, vb, true);
		final ScrollPane scrollPane = tp.getScrollPane();
		if (scrollPane != null) {
			scrollPane.setFitToWidth(true);
			scrollPane.setStyle("-fx-background-color:transparent");
		}
		tp.setAnimated(false);

		return tp;
	}

	/** @return the a Node that has a label and an {@link ConfigPropertyEditor} in a form-like layout (lbl:container) */
	@NotNull
	private ConfigPropertyEditor getConfigPropertyEditorNode(@NotNull ConfigProperty property) {
		ConfigPropertyEditor editor = new ConfigPropertyEditor(configClass, property);
		propertyEditors.put(property, editor);
		editor.setUserData(property);
		return editor;
	}


}
