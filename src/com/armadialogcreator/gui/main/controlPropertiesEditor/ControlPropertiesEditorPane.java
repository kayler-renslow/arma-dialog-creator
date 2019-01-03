package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.control.*;
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
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 Houses an accordion that allows manipulating multiple control properties. The data editor for each control property is specialized for the input (e.g. color property gets color picker).

 @author Kayler
 @since 07/08/2016. */
public class ControlPropertiesEditorPane extends StackPane {
	private final Accordion accordion = new Accordion();
	private ControlClass controlClass;
	private boolean listenersAreValid = true;

	private LinkedList<ControlPropertyInputDescriptor> propertyDescriptors = new LinkedList<>();

	private final ResourceBundle bundle = Lang.getBundle("ControlPropertyEditorBundle");

	private ControlPropertiesEditorPane() {
		getChildren().add(accordion);
		setMaxWidth(Double.MAX_VALUE);
	}

	/**
	 Tell all editors to stop listening to the {@link ControlProperty} values again.
	 Invoking is ideal when the pane is no longer needed. Invoking {@link #link()} after invoking this
	 may create issues.
	 */
	public void unlink() {
		listenersAreValid = false;
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.unlink();
		}
	}

	/** Tell all editors to listen to the {@link ControlProperty} values. */
	public void link() {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.link();
		}
	}


	/**
	 Creates the accordion according to the control class's specification.
	 For the inputted values in the accordion, they are fetched from {@link ControlClass#getRequiredProperties()},
	 {@link ControlClass#getOptionalPropertiesWithoutEvents()}, and {@link ControlClass#getEventProperties()}
	 <p>
	 It is important to note that when the control properties inside the control are edited,
	 they will be updated in the control class as well. There is no copying of the controlClass's
	 control properties and everything is passed by reference.

	 @param controlClass control class that has the properties to edit
	 */
	public ControlPropertiesEditorPane(@NotNull ControlClass controlClass) {
		this();
		this.controlClass = controlClass;

		accordion.getPanes().add(
				getPropertiesTitledPane(bundle.getString("required"), controlClass.getRequiredProperties(), false)
		);
		accordion.getPanes().add(
				getPropertiesTitledPane(bundle.getString("optional"), controlClass.getOptionalPropertiesWithoutEvents(), true)
		);
		accordion.getPanes().add(
				getPropertiesTitledPane(bundle.getString("events"), controlClass.getEventProperties(), true)
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

	/** Show only editors with properties that have no inherited value ({@link ControlProperty#getInherited()} == null) */
	public void hideInheritedProperties(boolean hide) {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.hideIfInherited(hide);
		}
	}

	/** Get the {@link ControlClass} being edited */
	@NotNull
	public ControlClass getControlClass() {
		return controlClass;
	}

	/** @return all missing properties (control properties that are required by have no valid data entered). */
	@NotNull
	public List<ControlProperty> getMissingProperties() {
		List<ControlProperty> properties = new ArrayList<>(propertyDescriptors.size());
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			if (descriptor.getControlProperty().getValue() == null && !descriptor.isOptional()) {
				properties.add(descriptor.getControlProperty());
			}
		}
		return properties;
	}

	@NotNull
	private TitledPane getNestedClassesTitledPane() {
		VBox vboxClassCategories = new VBox(10);
		vboxClassCategories.setPadding(new Insets(5));

		Function<ControlClass, Node> funcGetClassNode = cc -> {
			MenuButton menuButton = new MenuButton(cc.getClassName());

			MenuItem miEdit = new MenuItem(bundle.getString("edit_nested_class"));
			menuButton.getItems().add(miEdit);

			miEdit.setOnAction(event -> {
				EditNestedControlClassDialog dialog = new EditNestedControlClassDialog(cc);
				dialog.show();
			});

			controlClass.getControlClassUpdateGroup().addListener(new UpdateGroupListener<ControlClassUpdate>() {
				@Override
				public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, @Nullable ControlClassUpdate data) {
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
			final boolean hasRequiredClasses = controlClass.getRequiredNestedClasses().size() > 0;
			Label lblNoRequiredClasses = new Label(bundle.getString("no_classes"));
			if (hasRequiredClasses) {
				for (ControlClass nested : controlClass.getRequiredNestedClasses()) {
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
			final boolean hasOptionalClasses = controlClass.getOptionalNestedClasses().size() > 0;
			Label lblNoOptionalClasses = new Label(bundle.getString("no_classes"));
			VBox vboxOptional = new VBox(5);
			if (hasOptionalClasses) {
				for (ControlClass nested : controlClass.getOptionalNestedClasses()) {
					vboxOptional.getChildren().add(funcGetClassNode.apply(nested));
				}
			} else {
				vboxOptional.getChildren().add(lblNoOptionalClasses);
			}

			vboxClassCategories.getChildren().add(vboxOptional);

			controlClass.getControlClassUpdateGroup().addListener(new UpdateGroupListener<ControlClassUpdate>() {
				@Override
				public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, @Nullable ControlClassUpdate data) {
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

	/** Get a titled pane for the accordion that holds all control properties */
	private TitledPane getPropertiesTitledPane(String title, Iterable<ControlProperty> properties, boolean optional) {
		final VBox vb = new VBox(10);
		vb.setFillWidth(true);
		vb.setPadding(new Insets(5));
		final ScrollPane scrollPane = new ScrollPane(vb);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color:transparent");
		final TitledPane tp = new TitledPane(title, scrollPane);
		tp.setAnimated(false);
		Iterator<ControlProperty> iterator = properties.iterator();

		final boolean hasProperties = iterator.hasNext();
		Label lblNoProperties = new Label(bundle.getString("ControlPropertiesConfig.no_properties_available"));

		if (!hasProperties) {
			vb.getChildren().add(lblNoProperties);
		} else {
			while (iterator.hasNext()) {
				vb.getChildren().add(getControlPropertyEntry(iterator.next(), optional));
			}
		}

		controlClass.getControlClassUpdateGroup().addListener(new UpdateGroupListener<ControlClassUpdate>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, @Nullable ControlClassUpdate data) {
				if (optional) {
					//we are going to add the temp properties to the optional titled pane
					if (data instanceof ControlClassTemporaryPropertyUpdate) {
						ControlClassTemporaryPropertyUpdate update = (ControlClassTemporaryPropertyUpdate) data;
						if (update.isAdded()) {
							if (!hasProperties) {
								vb.getChildren().remove(lblNoProperties);
							}
							vb.getChildren().add(ControlPropertiesEditorPane.this.getControlPropertyEntry(update.getProperty(), true));
						} else {
							if (!hasProperties) {
								vb.getChildren().setAll(lblNoProperties);
								return;
							}
							vb.getChildren().removeIf(node -> node.getUserData() == update.getProperty());
						}
					}
				}
			}

			@Override
			public boolean hasExpired() {
				return !listenersAreValid;
			}
		});

		return tp;
	}

	/** Get the pane that shows the name of the property as well as the controls to input data */
	private Node getControlPropertyEntry(ControlProperty property, boolean optional) {
		ControlPropertyEditorContainer container = new ControlPropertyEditorContainer(controlClass, property);
		ControlPropertyInputDescriptor descriptor = new ControlPropertyInputDescriptor(container);
		propertyDescriptors.add(descriptor);
		descriptor.setIsOptional(optional);
		container.setUserData(property);
		return container;
	}


	private static class ControlPropertyInputDescriptor {
		private final ControlPropertyEditorContainer container;
		private boolean optional;
		private boolean nameFound = true;
		private boolean hideIfInherited = false;


		public ControlPropertyInputDescriptor(@NotNull ControlPropertyEditorContainer container) {
			this.container = container;
		}

		@NotNull
		public ControlPropertyValueEditor getValueEditor() {
			return container.currentValueEditor();
		}

		public boolean isOptional() {
			return optional;
		}

		/**
		 Return true if the {@link ControlProperty} returned from {@link #getControlProperty()}
		 is optional (not in required list returned from
		 {@link ControlClassRequirementSpecification#getRequiredProperties()})
		 */
		public void setIsOptional(boolean optional) {
			this.optional = optional;
		}

		/** Does same thing as {@link #getValueEditor()}.getControlProperty() */
		public ControlProperty getControlProperty() {
			return getValueEditor().getControlProperty();
		}

		public void unlink() {
			container.unlink();
		}

		public void link() {
			container.link();
		}

		@NotNull
		public ControlPropertyEditorContainer getContainer() {
			return container;
		}

		public void showIfNameContains(@NotNull String name) {
			this.nameFound = name.length() == 0 || getControlProperty().getName().toLowerCase().contains(name);
			setVisible();
		}

		public void hideIfInherited(boolean hide) {
			hideIfInherited = hide;
			getContainer().hideIfInherited(hide);
			setVisible();
		}

		private void setVisible() {
			boolean visible;
			if (getControlProperty().isInherited() && hideIfInherited) {
				visible = false;
			} else {
				visible = nameFound;
			}
			getContainer().setVisible(visible);
			getContainer().setManaged(visible);
		}
	}


}
