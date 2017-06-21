package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlClassRequirementSpecification;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
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
public class ControlPropertiesEditorPane extends StackPane {
	private final Accordion accordion = new Accordion();
	private ControlClass controlClass;

	private LinkedList<ControlPropertyInputDescriptor> propertyDescriptors = new LinkedList<>();

	private final ResourceBundle bundle = Lang.getBundle("ControlPropertyEditorBundle");

	private ControlPropertiesEditorPane() {
		getChildren().add(accordion);
	}

	/** Tell all editors to stop listening to the {@link ControlProperty} values again. Invoking is ideal when the pane is no longer needed. */
	public void unlink() {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.unlink();
		}
	}

	/** Tell all editors to listen to the {@link ControlProperty} values again. */
	public void relink() {
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			descriptor.link();
		}
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

		/** Return true if the {@link ControlProperty} returned from {@link #getControlProperty()} is optional (not in required list returned from {@link ControlClassRequirementSpecification#getRequiredProperties()}) */
		public void setIsOptional(boolean optional) {
			this.optional = optional;
		}

		/** Does same thing as {@link #getValueEditor()}.getControlProperty() */
		public ControlProperty getControlProperty() {
			return getValueEditor().getControlProperty();
		}

		/** Does same thing as {@link #getValueEditor()}.hasValidData() */
		public boolean hasValidData() {
			return getValueEditor().hasValidData();
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


	/**
	 Creates the accordion according to the control class's specification. For the inputted values in the accordion, they are fetched from {@link ControlClass#getRequiredProperties()}, {@link ControlClass#getOptionalProperties()}, and {@link ControlClass#getEventProperties()}<br>
	 It is important to note that when the control properties inside the control are edited, they will be updated in the control class as well. There is no copying of the controlClass's control properties and everything is passed by reference.

	 @param controlClass control class that has the properties to edit
	 */
	public ControlPropertiesEditorPane(@NotNull ControlClass controlClass) {
		this();
		this.controlClass = controlClass;
		setupAccordion(controlClass.getRequiredProperties(), controlClass.getOptionalProperties(), controlClass.getEventProperties());
	}

	/** Return true if all values entered for all properties are good/valid, false if at least one is bad. */
	public boolean allValuesAreGood() {
		return getMissingProperties().size() == 0;
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

	/** Get all missing properties (control properties that are required by have no valid data entered). */
	public List<ControlProperty> getMissingProperties() {
		List<ControlProperty> properties = new ArrayList<>(propertyDescriptors.size());
		for (ControlPropertyInputDescriptor descriptor : propertyDescriptors) {
			if (!descriptor.getValueEditor().hasValidData() && !descriptor.isOptional()) {
				properties.add(descriptor.getControlProperty());
			}
		}
		return properties;
	}

	private void setupAccordion(Iterable<ControlProperty> requiredProperties, Iterable<ControlProperty> optionalProperties, Iterable<ControlProperty> eventProperties) {
		accordion.getPanes().add(getTitledPane(bundle.getString("required"), requiredProperties, false));
		accordion.getPanes().add(getTitledPane(bundle.getString("optional"), optionalProperties, true));
		accordion.getPanes().add(getTitledPane(bundle.getString("events"), eventProperties, true));

		accordion.setExpandedPane(accordion.getPanes().get(0));

	}

	/** Get a titled pane for the accordion that holds all control properties */
	private TitledPane getTitledPane(String title, Iterable<ControlProperty> properties, boolean optional) {
		final VBox vb = new VBox(10);
		vb.setPadding(new Insets(5));
		final ScrollPane scrollPane = new ScrollPane(vb);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color:transparent");
		final TitledPane tp = new TitledPane(title, scrollPane);
		tp.setAnimated(false);
		Iterator<ControlProperty> iterator = properties.iterator();

		if (!iterator.hasNext()) {
			vb.getChildren().add(new Label(bundle.getString("ControlPropertiesConfig.no_properties_available")));
		} else {
			while (iterator.hasNext()) {
				vb.getChildren().add(getControlPropertyEntry(iterator.next(), optional));
			}
		}
		return tp;
	}

	/** Get the pane that shows the name of the property as well as the controls to input data */
	private Node getControlPropertyEntry(ControlProperty property, boolean optional) {
		ControlPropertyEditorContainer container = new ControlPropertyEditorContainer(controlClass, property);
		ControlPropertyInputDescriptor descriptor = new ControlPropertyInputDescriptor(container);
		propertyDescriptors.add(descriptor);
		descriptor.setIsOptional(optional);

		return container;
	}



}
