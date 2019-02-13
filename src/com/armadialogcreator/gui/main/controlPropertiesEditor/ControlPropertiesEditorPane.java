package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.core.ConfigClassSpecification;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ConfigPropertyCategory;
import com.armadialogcreator.gui.fxcontrol.PlaceholderTitledPane;
import com.armadialogcreator.gui.main.popup.EditNestedControlClassDialog;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 Houses an accordion that allows manipulating multiple control properties. The data editor for each control property is specialized for the input (e.g. color property gets color picker).

 @author Kayler
 @since 07/08/2016. */
public class ControlPropertiesEditorPane extends StackPane {
	private final Accordion accordion = new Accordion();
	private ConfigClassSpecification configClassSpecification;
	private boolean listenersAreValid = true;

	private LinkedList<ControlPropertyInputDescriptor> propertyDescriptors = new LinkedList<>();

	private final ResourceBundle bundle = Lang.getBundle("ControlPropertyEditorBundle");

	private ControlPropertiesEditorPane() {
		getChildren().add(accordion);
		setMaxWidth(Double.MAX_VALUE);
	}

	/**
	 Tell all editors to stop listening to the {@link ConfigProperty} values again.
	 Invoking is ideal when the pane is no longer needed. Invoking {@link #link()} after invoking this
	 may create issues.
	 */
	public void unlink() {
		listenersAreValid = false;
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.unlink();
		}
	}

	/** Tell all editors to listen to the {@link ConfigProperty} values. */
	public void link() {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.link();
		}
	}


	/**
	 Creates the accordion for editing all owned {@link ConfigClassSpecification#iterateProperties()} instances.
	 <p>
	 It is important to note that when the control properties inside the control are edited,
	 they will be updated in the control class as well. There is no copying of the controlClass's
	 control properties and everything is passed by reference.

	 @param configClassSpecification control class that has the properties to edit
	 */
	public ControlPropertiesEditorPane(@NotNull ConfigClassSpecification configClassSpecification) {
		this();
		this.configClassSpecification = configClassSpecification;

		PlaceholderTitledPane required = getPropertiesTitledPane(bundle.getString("required"), ConfigPropertyCategory.Required);
		PlaceholderTitledPane optional = getPropertiesTitledPane(bundle.getString("optional"), ConfigPropertyCategory.Optional);
		PlaceholderTitledPane events = getPropertiesTitledPane(bundle.getString("events"), ConfigPropertyCategory.Event);

		for (ConfigProperty p : configClassSpecification.iterateProperties()) {
			ConfigPropertyCategory propertyCat = configClassSpecification.getPropertyCategory(p);
			switch (propertyCat) {
				case Basic: //fall
				case Optional: {

					break;
				}
				case Event: {
					break;
				}
				case User: {
					break;
				}
				case Required: {
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

	/** Show only the editors with property names containing <code>name</code>. If length of <code>name</code>.trim() is 0 (), will show all editors */
	public void showPropertiesWithNameContaining(@NotNull String name) {
		name = name.trim().toLowerCase();
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.showIfNameContains(name);
		}
	}

	/** Show only editors with properties that aren't inherited ({@link ConfigClassSpecification#propertyIsInherited(ConfigProperty)}) */
	public void hideInheritedProperties(boolean hide) {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.hideIfInherited(hide);
		}
	}

	/** Get the {@link ConfigClassSpecification} being edited */
	@NotNull
	public ConfigClassSpecification getConfigClassSpecification() {
		return configClassSpecification;
	}

	/** @return all missing properties (control properties that are required by have no valid data entered). */
	@NotNull
	public List<String> getMissingProperties() {
		List<String> properties = new ArrayList<>(propertyDescriptors.size());
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			if (!descriptor.getValueEditor().hasValue() && !descriptor.isOptional()) {
				properties.add(descriptor.getConfigProperty());
			}
		}
		return properties;
	}

	@NotNull
	private TitledPane getNestedClassesTitledPane() {
		VBox vboxClassCategories = new VBox(10);
		vboxClassCategories.setPadding(new Insets(5));

		Function<ControlClassOld, Node> funcGetClassNode = cc -> {
			MenuButton menuButton = new MenuButton(cc.getClassName());

			MenuItem miEdit = new MenuItem(bundle.getString("edit_nested_class"));
			menuButton.getItems().add(miEdit);

			miEdit.setOnAction(event -> {
				EditNestedControlClassDialog dialog = new EditNestedControlClassDialog(cc);
				dialog.show();
			});

			configClassSpecification.getControlClassUpdateGroup().addListener(new UpdateGroupListener<ControlClassUpdate>() {
				@Override
				public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, @NotNull ControlClassUpdate data) {
					if (data instanceof ControlClassTemporaryNestedClassUpdate) {
						ControlClassTemporaryNestedClassUpdate update = (ControlClassTemporaryNestedClassUpdate) data;
						if (update.getNestedClass() == cc) {
							miEdit.setDisable(update.isAdded());
						}
					}
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
			final boolean hasRequiredClasses = configClassSpecification.getRequiredNestedClasses().size() > 0;
			Label lblNoRequiredClasses = new Label(bundle.getString("no_classes"));
			if (hasRequiredClasses) {
				for (ControlClassOld nested : configClassSpecification.getRequiredNestedClasses()) {
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
			final boolean hasOptionalClasses = configClassSpecification.getOptionalNestedClasses().size() > 0;
			Label lblNoOptionalClasses = new Label(bundle.getString("no_classes"));
			VBox vboxOptional = new VBox(5);
			if (hasOptionalClasses) {
				for (ControlClassOld nested : configClassSpecification.getOptionalNestedClasses()) {
					vboxOptional.getChildren().add(funcGetClassNode.apply(nested));
				}
			} else {
				vboxOptional.getChildren().add(lblNoOptionalClasses);
			}

			vboxClassCategories.getChildren().add(vboxOptional);

			configClassSpecification.getControlClassUpdateGroup().addListener(new UpdateGroupListener<ControlClassUpdate>() {
				@Override
				public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, @NotNull ControlClassUpdate data) {
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

		TitledPane tp = new TitledPane(bundle.getString("nested_classes"), scrollPane);
		tp.setAnimated(false);
		vboxClassCategories.setFillWidth(true);

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
	private Node getConfigPropertyEntry(@NotNull ConfigProperty property) {
		ConfigPropertyEditor container = new ConfigPropertyEditor(configClassSpecification, property);
		ControlPropertyInputDescriptor descriptor = new ControlPropertyInputDescriptor(container);
		propertyDescriptors.add(descriptor);
		container.setUserData(property);
		return container;
	}


	private static class ControlPropertyInputDescriptor {
		private final ConfigPropertyEditor container;
		private boolean optional;
		private boolean nameFound = true;
		private boolean hideIfInherited = false;


		public ControlPropertyInputDescriptor(@NotNull ConfigPropertyEditor container) {
			this.container = container;
		}

		@NotNull
		public ConfigPropertyValueEditor getValueEditor() {
			return container.getPropertyValueEditor();
		}

		/** Does same thing as {@link #getValueEditor()}.getConfigProperty() */
		public ConfigProperty getConfigProperty() {
			return getValueEditor().getConfigProperty();
		}

		public void unlink() {
			container.unlink();
		}

		public void link() {
			container.link();
		}

		@NotNull
		public ConfigPropertyEditor getContainer() {
			return container;
		}

		public void showIfNameContains(@NotNull String name) {
			this.nameFound = name.length() == 0 || getConfigProperty().getName().toLowerCase().contains(name);
			setVisible();
		}

		public void hideIfInherited(boolean hide) {
			hideIfInherited = hide;
			getContainer().hideIfInherited(hide);
			setVisible();
		}

		private void setVisible() {
			boolean visible;
			if (getConfigProperty().isInherited() && hideIfInherited) {
				visible = false;
			} else {
				visible = nameFound;
			}
			getContainer().setVisible(visible);
			getContainer().setManaged(visible);
		}
	}


}
