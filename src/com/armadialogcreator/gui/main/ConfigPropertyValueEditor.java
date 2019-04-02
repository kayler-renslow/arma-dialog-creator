package com.armadialogcreator.gui.main;

import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.PropertyType;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

/**
 A {@link ConfigPropertyValueEditor} is used to edit a {@link ConfigProperty#getValue()}. An editor
 can only be used to edit one {@link PropertyType}.

 @author Kayler
 @see ConfigPropertyValueEditors
 @since 11/20/2016 */
interface ConfigPropertyValueEditor {

	/** @return the {@link ConfigProperty} being edited. */
	@NotNull
	ConfigProperty getConfigProperty();

	/** @return the JavaFX Node that the editor is placed on. The entire editor should be contained within this node. */
	@NotNull Node getRootNode();

	/**
	 Get the {@link PropertyType} that this editor can edit.

	 @return the allowed property type
	 */
	@NotNull PropertyType getAcceptedPropertyType();

	/**
	 Return true if the {@link #getRootNode()}'s width should fill the parent's width.
	 False if the width should be whatever it is initially. By default, will return false.
	 */
	default boolean displayFullWidth() {
		return false;
	}

	/** Clear all listeners */
	void clearListeners();

	/** Initialize all listeners */
	void initListeners();

	/** Set the editor to the {@link #getConfigProperty()} value. */
	void setToValueFromProperty();

	/** Set whether or not the property can be edited by the user. */
	default void disableEditing(boolean disable) {
		getRootNode().setDisable(disable);
	}
}
