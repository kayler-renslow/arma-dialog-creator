package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.core.ControlProperty;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.core.PropertyType;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/20/2016 */
interface ControlPropertyValueEditor extends ControlPropertyEditor {

	enum EditMode {
		/** Using default editor */
		DEFAULT,
		/** The control property's value is set to a macro */
		MACRO
	}

	/**
	 Updating the edit mode.
	 When this method is invoked, the editor should change its visual to better edit the {@link ControlProperty}.
	 <ul>
	 <li>{@link EditMode#DEFAULT} = default editor</li>
	 <li>
	 {@link EditMode#MACRO} = setting the ControlProperty's value equal to a {@link Macro}
	 <p>
	 With this mode, nothing needs to be done on the editor's side.
	 </li>
	 </ul>
	 */
	void setToMode(@NotNull EditMode mode);

	/** @return the JavaFX Node that the editor is placed on. The entire editor should be contained within this node. */
	@NotNull Node getRootNode();

	/** @return the {@link PropertyType} type that will be used to match {@link Macro} instances */
	@NotNull PropertyType getMacroPropertyType();

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

	/** Set the editor to the {@link #getControlProperty()} value. */
	void refresh();

	@Override
	default void disableEditing(boolean disable) {
		getRootNode().setDisable(disable);
	}
}
