package com.armadialogcreator.gui.main;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigClassSpecification;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ConfigPropertyCategory;
import com.armadialogcreator.gui.fxcontrol.PlaceholderTitledPane;
import com.armadialogcreator.lang.Lang;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
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
	private boolean listenersAreValid = true;

	private final Map<String, ConfigPropertyEditor> propertyEditors = new HashMap<>();

	private final ResourceBundle bundle = Lang.getBundle("ConfigPropertyEditorBundle");

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

		PlaceholderTitledPane required = getPropertiesTitledPane(bundle.getString("required"), ConfigPropertyCategory.Required);
		PlaceholderTitledPane optional = getPropertiesTitledPane(bundle.getString("optional"), ConfigPropertyCategory.Optional);
		PlaceholderTitledPane events = getPropertiesTitledPane(bundle.getString("events"), ConfigPropertyCategory.Event);

		for (ConfigProperty p : configClass.iterateProperties()) {
			ConfigPropertyCategory propertyCat = configClass.getPropertyCategory(p);
			switch (propertyCat) {
				case Basic: //fall
				case Optional: {
					optional.addContentChild(getConfigPropertyEditorNode(p));
					break;
				}
				case Event: {
					break;
				}
				case User: {
					break;
				}
				case Required: {
					required.addContentChild(getConfigPropertyEditorNode(p));
					break;
				}
			}
		}

		accordion.getPanes().add(
				required
		);
		accordion.getPanes().add(
				optional
		);
		accordion.getPanes().add(
				events
		);
		accordion.getPanes().add(getNestedClassesTitledPane());

		accordion.setExpandedPane(accordion.getPanes().get(0));
		accordion.expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
			accordion.autosize();
		});
		accordion.setMaxWidth(Double.MAX_VALUE);
	}

	/**
	 Tell all editors to stop listening to the {@link ConfigProperty} values again.
	 Invoking is ideal when the pane is no longer needed. Invoking {@link #link()} after invoking this
	 may create issues.
	 */
	public void unlink() {
		listenersAreValid = false;
		for (Map.Entry<String, ConfigPropertyEditor> editor : propertyEditors.entrySet()) {
			editor.getValue().unlink();
		}
	}

	/** Tell all editors to listen to the {@link ConfigProperty} values. */
	public void link() {
		for (Map.Entry<String, ConfigPropertyEditor> editor : propertyEditors.entrySet()) {
			editor.getValue().link();
		}
	}

	/** Show only the editors with property names containing <code>name</code>. If length of <code>name</code>.trim() is 0 (), will show all editors */
	public void showPropertiesWithNameContaining(@NotNull String name) {
		name = name.trim().toLowerCase();
		for (Map.Entry<String, ConfigPropertyEditor> entry : propertyEditors.entrySet()) {
			entry.getValue().hide(name.length() > 0 && !entry.getKey().contains(name));
		}
	}

	/** Show only editors with properties that aren't inherited ({@link ConfigClassSpecification#propertyIsInherited(String)}) */
	public void hideInheritedProperties(boolean hide) {
		for (Map.Entry<String, ConfigPropertyEditor> entry : propertyEditors.entrySet()) {
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
			if (propertyEditors.containsKey(p.getName())) {
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
		/*
		VBox vboxClassCategories = new VBox(10);
		vboxClassCategories.setPadding(new Insets(5));

		Function<ConfigClass, Node> funcGetClassNode = cc -> {
			MenuButton menuButton = new MenuButton(cc.getClassName());

			MenuItem miEdit = new MenuItem(bundle.getString("edit_nested_class"));
			menuButton.getItems().add(miEdit);

			miEdit.setOnAction(event -> {
				EditConfigClassDialog dialog = new EditConfigClassDialog(cc);
				dialog.show();
			});

			configClass.getClassUpdateGroup().addListener(new UpdateGroupListener<>() {
				@Override
				public void update(@NotNull UpdateListenerGroup<ConfigClassUpdate> group, @NotNull ConfigClassUpdate data) {

				}

				@Override
				public boolean hasExpired() {
					return !listenersAreValid;
				}
			});

			menuButton.setUserData(cc);

			return menuButton;
		};

		//add required nested
		vboxClassCategories.getChildren().add(new Label(bundle.getString("required_nested_classes")));
		{
			VBox vboxRequired = new VBox(5);
			final boolean hasRequiredClasses = configClass.getRequiredNestedClasses().size() > 0;
			Label lblNoRequiredClasses = new Label(bundle.getString("no_classes"));
			if (hasRequiredClasses) {
				for (ConfigClass nested : configClass.getRequiredNestedClasses()) {
					vboxRequired.getChildren().add(funcGetClassNode.apply(nested));
				}
			} else {
				vboxRequired.getChildren().add(lblNoRequiredClasses);
			}

			vboxClassCategories.getChildren().add(vboxRequired);
		}

		vboxClassCategories.getChildren().add(new Separator(Orientation.HORIZONTAL));

		//add optional nested
		vboxClassCategories.getChildren().add(new Label(bundle.getString("optional_nested_classes")));
		{
			final boolean hasOptionalClasses = configClass.getOptionalNestedClasses().size() > 0;
			Label lblNoOptionalClasses = new Label(bundle.getString("no_classes"));
			VBox vboxOptional = new VBox(5);
			if (hasOptionalClasses) {
				for (ConfigClass nested : configClass.getOptionalNestedClasses()) {
					vboxOptional.getChildren().add(funcGetClassNode.apply(nested));
				}
			} else {
				vboxOptional.getChildren().add(lblNoOptionalClasses);
			}

			vboxClassCategories.getChildren().add(vboxOptional);

			configClass.getClassUpdateGroup().addListener(new UpdateGroupListener<>() {
				@Override
				public void update(@NotNull UpdateListenerGroup<ConfigClassUpdate> group, @NotNull ConfigClassUpdate data) {
					if (data instanceof ControlClassTemporaryNestedClassUpdate) {
						ControlClassTemporaryNestedClassUpdate update = (ControlClassTemporaryNestedClassUpdate) data;
						if (update.isAdded()) {
							if (!hasOptionalClasses) {
								vboxOptional.getChildren().remove(lblNoOptionalClasses);
							}
							vboxOptional.getChildren().add(funcGetClassNode.apply(update.getNestedClass()));
						} else {
							if (!hasOptionalClasses) {
								vboxOptional.getChildren().setAll(lblNoOptionalClasses);
							}
							vboxOptional.getChildren().removeIf(node -> node.getUserData() == update.getNestedClass());
						}
					}
				}

				@Override
				public boolean hasExpired() {
					return !listenersAreValid;
				}
			});
		}

		ScrollPane scrollPane = new ScrollPane(vboxClassCategories);
		scrollPane.setFitToWidth(true);
*/
		TitledPane tp = new TitledPane(bundle.getString("nested_classes"), new Label("temporary label")/*scrollPane*/);
		tp.setAnimated(false);
		//vboxClassCategories.setFillWidth(true);

		return tp;
	}

	/** @return a titled pane for the accordion that holds all properties of a certain {@link ConfigPropertyCategory} */
	@NotNull
	private PlaceholderTitledPane getPropertiesTitledPane(@NotNull String title, @NotNull ConfigPropertyCategory category) {
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
	private Node getConfigPropertyEditorNode(@NotNull ConfigProperty property) {
		ConfigPropertyEditor editor = new ConfigPropertyEditor(configClass, property);
		propertyEditors.put(property.getName(), editor);
		editor.setUserData(property);
		return editor;
	}


}
